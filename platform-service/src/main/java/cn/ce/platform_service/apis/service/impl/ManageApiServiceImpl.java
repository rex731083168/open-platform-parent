package cn.ce.platform_service.apis.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.protocol.HTTP;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;

import cn.ce.platform_service.apis.dao.IMysqlApiDao;
import cn.ce.platform_service.apis.entity.ApiEntity;
import cn.ce.platform_service.apis.entity.GatewayVersion;
import cn.ce.platform_service.apis.entity.NewApiEntity;
import cn.ce.platform_service.apis.entity.QueryApiEntity;
import cn.ce.platform_service.apis.service.IManageApiService;
import cn.ce.platform_service.apis.util.ApiTransform;
import cn.ce.platform_service.common.AuditConstants;
import cn.ce.platform_service.common.Constants;
import cn.ce.platform_service.common.DBFieldsConstants;
import cn.ce.platform_service.common.ErrorCodeNo;
import cn.ce.platform_service.common.Result;
import cn.ce.platform_service.common.gateway.ApiCallUtils;
import cn.ce.platform_service.common.gateway.GatewayUtils;
import cn.ce.platform_service.common.page.Page;
import cn.ce.platform_service.gateway.entity.GatewayColonyEntity;
import cn.ce.platform_service.util.LocalFileReadUtil;
import io.netty.handler.codec.http.HttpMethod;

/**
 * @Description : 说明
 * @Author : makangwei
 * @Date : 2017年10月12日
 */
@Service(value = "manageApiService")
@Transactional(propagation=Propagation.REQUIRED)
public class ManageApiServiceImpl implements IManageApiService {

	private static final Logger _LOGGER = LoggerFactory.getLogger(ManageApiServiceImpl.class);
	// @Resource
	// private INewApiDao newApiDao;
	// @Resource
	// private IOpenApplyDao openApplyDao;
	@Resource
	private IMysqlApiDao mysqlApiDao;

	/**
	 * @Title: auditApi
	 * @Description: 批量添加api到网关，如果中途发生异常，回滚之前添加的api，并且reload
	 * @author: makangwei
	 * @date: 2017年10月13日 下午8:08:38
	 */
	@Override
	public Result<?> auditApi(List<String> apiIds, Integer checkState, String checkMem) {
		Result<String> result = new Result<String>();

		// List<ApiEntity> apiList = newApiDao.findApiByIds(apiId);
		List<NewApiEntity> apiList = mysqlApiDao.findByIds(apiIds);

		if (checkState == AuditConstants.USER__CHECKED_FAILED) { // 批量审核不通过
			// 修改数据库状态
			for (NewApiEntity apiEntity : apiList) {
				apiEntity.setCheckState(AuditConstants.USER__CHECKED_FAILED);
				apiEntity.setCheckMem(checkMem);// 审核失败，添加审核失败原因
				// newApiDao.save(apiEntity);
				mysqlApiDao.saveOrUpdateEntity(apiEntity);
			}
			result.setSuccessMessage("修改成功");

		} else if (checkState == 2) { // 批量审核通过

			List<String> rollBackApis = new ArrayList<String>(); // 回滚集合

			// 循环添加api到网关
			for (NewApiEntity apiEntity : apiList) {

				// 根据versionId查询只有版本信息不同的其他相同的多个api
				// 添加新的版本需要将旧的版本信息和新的版本信息一同推送到网关
				List<NewApiEntity> apiVersionList = mysqlApiDao.findByVersionIdExp(apiEntity.getVersionId(),
						apiEntity.getId());

				/**
				 * 这里有一个问题是如果不同版本但是其他相同api同时提交审核会往网关推送两次，但是我们的业务是每次只能是最新的版本进行提交
				 * 所以这个bug由业务来避免了。
				 */

				//Map<String, String> map = new HashMap<String, String>(); // map中放着不同的版本和版本对应的endPoint.
				List<GatewayVersion> versionList = new ArrayList<GatewayVersion>();
				for (NewApiEntity entity : apiVersionList) { // 查询旧的版本信息和Url
					if (entity.getCheckState() == 2) {
						// TODO 这里将来会往两个网关里推，测试网关推送测试Url，正式网关推送正式Url
						//map.put(entity.getVersion() + "", entity.getListenPath()); // 这里只往正式网关里面推送
						versionList.add(new GatewayVersion(entity.getVersion(),entity.getListenPath(),
								entity.getHttpMethod().toUpperCase(),entity.getOrgPath()));
					}
				}
				//map.put(apiEntity.getVersion() + "", apiEntity.getListenPath());// 最后加上新添加的版本信息和Url
				versionList.add(new GatewayVersion(apiEntity.getVersion(),apiEntity.getListenPath(),
						apiEntity.getHttpMethod().toUpperCase(),apiEntity.getOrgPath()));

				// 打印最终所有的版本信息和每个版本的请求路径
//				for (String key : map.keySet()) {
//					_LOGGER.info("版本键：" + key + "," + "请求地址是：" + map.get(key));
//				}

				if (!apiEntity.getListenPath().startsWith("/")) {
					apiEntity.setListenPath("/" + apiEntity.getListenPath());
				}
				// if(!apiEntity.getListenPath().endsWith("/")){
				// apiEntity.setListenPath(apiEntity.getListenPath()+"/");
				// }
				String listenPath = apiEntity.getListenPath();
				String targetUrl = apiEntity.getDefaultTargetUrl(); // 如果saas-id对应的租户找不到地址。就跳到这个地址。非必填。如果传入，必须校验url格式
				String resourceType = apiEntity.getResourceType();
				/*** 添加api到网关接口 ***/
				/** 20180319 update:api支持url重定向。如果回源地址不为空。就要支持url重定向*/
				JSONObject params = generateGwApiJson(apiEntity.getVersionId(), listenPath, listenPath, resourceType,
						targetUrl, versionList, AuditConstants.GATEWAY_API_VERSIONED_TRUE);

				if (params == null) {
					_LOGGER.info("拼接网关api json发生错误");
					result.setErrorMessage("", ErrorCodeNo.SYS014);
					return result;
				}

				_LOGGER.info("推送api到网关");
				Map<String, String> headers = new HashMap<String, String>();
				headers.put(HTTP.CONTENT_TYPE, Constants.APPLICATION_JSON);
				headers.put(Constants.HEADER_KEY, Constants.HEADER_VALUE);
				List<GatewayColonyEntity> gwCols = GatewayUtils.getAllGatewayColony();

				// TODO 这里如果将来有多个网关集群，需要修改
				GatewayColonyEntity gatewayColonyEntity = gwCols.get(0);
				String resultStr = ApiCallUtils.putOrPostMethod(
						gatewayColonyEntity.getColUrl() + Constants.NETWORK_ADD_PATH + "/" + apiEntity.getVersionId(),
						params, headers, HttpMethod.POST);

				if (resultStr == null) {

					// TODO 回滚数据
					for (String rollApiId : rollBackApis) {
						Map<String, String> headers1 = new HashMap<String, String>();
						headers1.put(Constants.HEADER_KEY, Constants.HEADER_VALUE);
						ApiCallUtils.getOrDelMethod(
								gatewayColonyEntity.getColUrl() + Constants.NETWORK_DEL_PATH + "/" + rollApiId, headers,
								HttpMethod.DELETE);
					}

					result.setErrorMessage("调用网关发生异常。请稍后添加", ErrorCodeNo.SYS014);
					return result;
				} else {
					// TODO 添加回滚数据
					rollBackApis.add(apiEntity.getId());
				}
			}
			// reload
			Map<String, String> headers = new HashMap<String, String>();
			headers.put(Constants.HEADER_KEY, Constants.HEADER_VALUE);
			ApiCallUtils.getOrDelMethod(
					GatewayUtils.getAllGatewayColony().get(0).getColUrl() + Constants.NEWWORK_RELOAD_GROUP, headers,
					HttpMethod.GET);

			// 批量修改数据库
			for (NewApiEntity apiEntity2 : apiList) {
				apiEntity2.setCheckState(AuditConstants.API_CHECK_STATE_SUCCESS);
				// newApiDao.save(apiEntity2);
				mysqlApiDao.saveOrUpdateEntity(apiEntity2);
			}
			result.setSuccessMessage("修改成功");

		} else {
			result.setErrorMessage("修改状态不正确。请输入正确的状态", ErrorCodeNo.SYS012); // TODO
																			// 重复的判断
		}
		return result;
	}

	@Override
	public Result<?> showApi(String apiId) {

		Result<com.alibaba.fastjson.JSONObject> result = new Result<com.alibaba.fastjson.JSONObject>();

		// ApiEntity api = newApiDao.findApiById(apiId);
		NewApiEntity api = mysqlApiDao.findTotalOneById(apiId);
		if (api == null) {
			result.setErrorMessage("当前id不存在", ErrorCodeNo.SYS006);
			return result;
		}

		// 添加网关访问地址
		List<GatewayColonyEntity> colList = GatewayUtils.getAllGatewayColony();
		List<String> gatewayUrlList = new ArrayList<String>();
		List<String> wGatewayUrlList = new ArrayList<String>();
		for (GatewayColonyEntity gatewayColonyEntity : colList) {
			// TODO 这里的路径是否正确。网关是否修改这里
			// gatewayUrlList.add(gatewayColonyEntity.getColUrl()
			// +api.getListenPath()+api.getApiVersion().getVersion()+"/");
			gatewayUrlList.add(gatewayColonyEntity.getColUrl() + api.getListenPath());
			wGatewayUrlList.add(gatewayColonyEntity.getwColUrl() + api.getListenPath());// 外网访问地址
		}

//		com.alibaba.fastjson.JSONObject jsonObject = com.alibaba.fastjson.JSONObject
//				.parseObject(JSON.toJSONString(api));
		com.alibaba.fastjson.JSONObject jsonObject = com.alibaba.fastjson.JSONObject.parseObject(JSON.toJSONString(ApiTransform.transToApi(api)));
		jsonObject.put("gatewayUrls", gatewayUrlList);
		jsonObject.put("wGatewayUrls", wGatewayUrlList);
		result.setSuccessData(jsonObject);
		return result;
	}

	@Override
	public Result<Page<ApiEntity>> apiAllList(QueryApiEntity entity) {

		Result<Page<ApiEntity>> result = new Result<Page<ApiEntity>>();

		// Page<ApiEntity> page = newApiDao.findManagerList(entity, currentPage,
		// pageSize);
		int totalNum = mysqlApiDao.findListSize(entity);
		entity.setOrgCurrentPage(1);
		if(totalNum > 0){
			entity.setOrgPageSize(totalNum);
		}else{
			entity.setOrgPageSize(1);
		}
		List<NewApiEntity> list = mysqlApiDao.getPagedList(entity);
		List<ApiEntity> apiList= ApiTransform.transToApis(list);
		if (list != null) {
			Page<ApiEntity> page = new Page<ApiEntity>(entity.getCurrentPage(), totalNum, entity.getPageSize(),
					apiList);
			result.setSuccessData(page);
		} else {
			result.setErrorMessage("查询不到结果", ErrorCodeNo.SYS006);
		}
		return result;
	}

	@Override
	public Result<Page<ApiEntity>> apiList(QueryApiEntity entity) {

		Result<Page<ApiEntity>> result = new Result<Page<ApiEntity>>();

		// Page<ApiEntity> page =
		// newApiDao.findManagerListWithoutPage(apiEntity);
		int totalNum = mysqlApiDao.findListSize(entity);
		List<NewApiEntity> list = mysqlApiDao.getPagedList(entity);
		List<ApiEntity> apiList = ApiTransform.transToApis(list);
		
		if (list != null) {
			Page<ApiEntity> page = new Page<ApiEntity>(entity.getCurrentPage(), totalNum, entity.getPageSize(),
					apiList);
			result.setSuccessData(page);
		} else {
			result.setErrorMessage("查询不到结果", ErrorCodeNo.SYS006);
		}
		return result;
	}

	private JSONObject generateGwApiJson(String versionId, String apiEnName, String listenPath, String resourceType,
			String targetUrl, List<GatewayVersion> versionList, boolean gatewayApiVersionedTrue) {

		String path = Constants.GW_API_JSON;
		JSONObject job = LocalFileReadUtil.readLocalClassPathJson(path);

		if (job == null) {
			return null;
		}

		// 拼装新的json
		job.put(DBFieldsConstants.GW_API_NAME, apiEnName);
		job.put(DBFieldsConstants.GW_API_ID, versionId);
		job.put(DBFieldsConstants.GW_API_RESOURCE_TYPE, resourceType);
		JSONObject versions = new JSONObject();
		for (GatewayVersion gVersion : versionList) {
			JSONObject version = new JSONObject();
			version.put(DBFieldsConstants.GW_API_VERSIONS_NAME, gVersion.getVersion());
			version.put(DBFieldsConstants.GW_API_VERSIONS_EXPIRES, "-1");
			version.put(DBFieldsConstants.GW_API_VERSIONS_OVERRIDE_TARGET, gVersion.getListenPath());
			/** 20180319 update:api支持url重定向。如果回源地址不为空。就要支持url重定向*/
			if(StringUtils.isNotBlank(gVersion.getOrgPath())){
				version.put(DBFieldsConstants.GW_API_VERSIONS_USE_EXTENDED_PATHS, true);
				JSONObject extendedPaths = new JSONObject();
				JSONArray urlReArr = new JSONArray();
				JSONObject urlReObj = new JSONObject();
				urlReObj.put(DBFieldsConstants.GW_API_VERSIONS_URL_REWRITE_PATH, 
						listenPath); //TODO 这里的listenPath不同版本都必须使用相同的，做不到不同的版本不同的listenPath
				urlReObj.put(DBFieldsConstants.GW_API_VERSIONS_URL_REWRITE_METHOD, gVersion.getMethod().toUpperCase());
				urlReObj.put(DBFieldsConstants.GW_API_VERSIONS_URL_REWRITE_PATTERN, "/(.+)");
				urlReObj.put(DBFieldsConstants.GW_API_VERSIONS_URL_REWRITE_REWRITE_TO, gVersion.getOrgPath());
				urlReArr.put(urlReObj);
				extendedPaths.put(DBFieldsConstants.GW_API_VERSIONS_URL_REWRITES, urlReArr);
				version.put(DBFieldsConstants.GW_API_VERSIONS_EXTENDED_PATHS, extendedPaths);
			}
			versions.put(gVersion.getVersion(), version);
		}
		// job.put(DBFieldsConstants.GW_API_VERSIONS, versions);
		job.getJSONObject(DBFieldsConstants.GW_API_VERSION_DATA).put(DBFieldsConstants.GW_API_VERSIONS, versions);

		JSONObject proxy = (JSONObject) job.get(DBFieldsConstants.GW_API_PROXY);
		proxy.put(DBFieldsConstants.GW_API_PROXY_LISTENPATH, listenPath);
		if (StringUtils.isNotBlank(targetUrl)) {
			proxy.put(DBFieldsConstants.GW_API_PROXY_TARGETURL, targetUrl);
		}
		return job;
	}

}
