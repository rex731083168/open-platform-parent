package cn.ce.platform_service.apis.service.impl;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import org.apache.http.protocol.HTTP;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;

import cn.ce.platform_service.apis.dao.INewApiDao;
import cn.ce.platform_service.apis.entity.ApiEntity;
import cn.ce.platform_service.apis.entity.DApiBoundEntity;
import cn.ce.platform_service.apis.entity.DApiBoundList;
import cn.ce.platform_service.apis.entity.QueryApiEntity;
import cn.ce.platform_service.apis.service.IDApiBoundService;
import cn.ce.platform_service.apis.service.IManageApiService;
import cn.ce.platform_service.common.AuditConstants;
import cn.ce.platform_service.common.Constants;
import cn.ce.platform_service.common.DBFieldsConstants;
import cn.ce.platform_service.common.ErrorCodeNo;
import cn.ce.platform_service.common.Result;
import cn.ce.platform_service.common.Status;
import cn.ce.platform_service.common.gateway.ApiCallUtils;
import cn.ce.platform_service.common.gateway.GatewayUtils;
import cn.ce.platform_service.common.page.Page;
import cn.ce.platform_service.diyApply.entity.appsEntity.AppList;
import cn.ce.platform_service.gateway.entity.GatewayColonyEntity;
import cn.ce.platform_service.openApply.dao.IOpenApplyDao;
import cn.ce.platform_service.util.LocalFileReadUtil;
import cn.ce.platform_service.util.PropertiesUtil;
import io.netty.handler.codec.http.HttpMethod;

/**
* @Description : 说明
* @Author : makangwei
* @Date : 2017年10月12日
*/
@Service(value="manageApiService")
public class ManageApiServiceImpl implements IManageApiService{

	private static final Logger _LOGGER = LoggerFactory.getLogger(ManageApiServiceImpl.class);
	@Resource
	private INewApiDao newApiDao;
	@Resource
	private IOpenApplyDao openApplyDao;	
	@Resource
	private IDApiBoundService dApiBoundService;
	
	/**
	 * @Title: auditApi
	 * @Description: 批量添加api到网关，如果中途发生异常，回滚之前添加的api，并且reload
	 * @author: makangwei 
	 * @date:   2017年10月13日 下午8:08:38 
	 */
	@Override
	public Result<?> auditApi(List<String> apiId, Integer checkState, String checkMem) {
		Result<String> result = new Result<String>();

		List<ApiEntity> apiList = newApiDao.findApiByIds(apiId);
		
		if(checkState == AuditConstants.USER__CHECKED_FAILED){ //批量审核不通过
			//修改数据库状态
			for (ApiEntity apiEntity : apiList) {
				apiEntity.setCheckState(AuditConstants.USER__CHECKED_FAILED);
				apiEntity.setCheckMem(checkMem);//审核失败，添加审核失败原因
				newApiDao.save(apiEntity);
			}
			result.setSuccessMessage("修改成功");
			
		}else if(checkState == 2){ //批量审核通过
	
			List<String> rollBackApis = new ArrayList<String>(); //回滚集合
			
			//循环添加api到网关
			for (ApiEntity apiEntity : apiList) {
				
				//根据versionId查询只有版本信息不同的其他相同的多个api
				//添加新的版本需要将旧的版本信息和新的版本信息一同推送到网关
				List<ApiEntity> apiVersionList = newApiDao.findByField(DBFieldsConstants.APIS_APIVERSION_VERSIONID, apiEntity.getApiVersion().getVersionId());
				
				/** 这里有一个问题是如果不同版本但是其他相同api同时提交审核会往网关推送两次，但是我们的业务是每次只能是最新的版本进行提交
				 * 所以这个bug由业务来避免了。
				 */
				
				Map<String,String> map = new HashMap<String,String>(); //map中放着不同的版本和版本对应的endPoint.
				for (ApiEntity entity : apiVersionList) { //查询旧的版本信息和Url
					if(entity.getCheckState() == 2 ){
						// TODO 这里将来会往两个网关里推，测试网关推送测试Url，正式网关推送正式Url
						map.put(entity.getApiVersion().getVersion()+"", entity.getEndPoint()); //这里只往正式网关里面推送
					}
				}
				map.put(apiEntity.getApiVersion().getVersion()+"", apiEntity.getEndPoint());//最后加上新添加的版本信息和Url
				
				//打印最终所有的版本信息和每个版本的请求路径
				for (String key : map.keySet()) {
					_LOGGER.info("版本键："+key+","+"请求地址是："+map.get(key));
				}
				
				if(!apiEntity.getListenPath().startsWith("/")){
					apiEntity.setListenPath("/"+apiEntity.getListenPath());
				}
//				if(!apiEntity.getListenPath().endsWith("/")){
//					apiEntity.setListenPath(apiEntity.getListenPath()+"/");
//				}
				String listenPath = apiEntity.getListenPath();
				String endPoint  = apiEntity.getEndPoint(); //endPoint如果saas-id对应的租户找不到地址。就跳到这个地址。非必填。如果传入，必须校验url格式
				/*** 添加api到网关接口 ***/
				JSONObject params = generateGwApiJson(apiEntity.getApiVersion().getVersionId(), listenPath, listenPath, endPoint, map,AuditConstants.GATEWAY_API_VERSIONED_TRUE);
				
				if(params == null){
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
				String resultStr = ApiCallUtils.putOrPostMethod(gatewayColonyEntity.getColUrl()+Constants.NETWORK_ADD_PATH+"/"+apiEntity.getApiVersion().getVersionId(), 
						params, headers, HttpMethod.POST);
				
				if(resultStr == null){
					
					// TODO 回滚数据
					for (String rollApiId : rollBackApis) {
						Map<String, String> headers1 = new HashMap<String, String>();
						headers1.put(Constants.HEADER_KEY, Constants.HEADER_VALUE);
						ApiCallUtils.getOrDelMethod(gatewayColonyEntity.getColUrl()+Constants.NETWORK_DEL_PATH+"/"+rollApiId,
								headers, HttpMethod.DELETE);
					}
					
					result.setErrorMessage("调用网关发生异常。请稍后添加",ErrorCodeNo.SYS014);
					return result;
				}else{
					// TODO 添加回滚数据
					rollBackApis.add(apiEntity.getId());
				}
			}
			//reload
			Map<String, String> headers = new HashMap<String, String>();
			headers.put(Constants.HEADER_KEY, Constants.HEADER_VALUE);
			ApiCallUtils.getOrDelMethod(GatewayUtils.getAllGatewayColony().get(0).getColUrl()+Constants.NEWWORK_RELOAD_GROUP,
					headers, HttpMethod.GET);
				
			// 批量修改数据库
			for (ApiEntity apiEntity2 : apiList) {
				apiEntity2.setCheckState(AuditConstants.API_CHECK_STATE_SUCCESS);
				newApiDao.save(apiEntity2);
			}
			result.setSuccessMessage("修改成功");
			
		}else{
			result.setErrorMessage("修改状态不正确。请输入正确的状态",ErrorCodeNo.SYS012); // TODO 重复的判断
		}
		return result;
	}

	@Override
	public Result<?> showApi(String apiId) {
		
		Result<com.alibaba.fastjson.JSONObject> result = new Result<com.alibaba.fastjson.JSONObject>();
		
		ApiEntity api = newApiDao.findApiById(apiId);
		if (api == null) {
			result.setErrorMessage("当前id不存在",ErrorCodeNo.SYS006);
			return result;
		}

		// 添加网关访问地址
		List<GatewayColonyEntity> colList = GatewayUtils.getAllGatewayColony();
		List<String> gatewayUrlList = new ArrayList<String>();
		List<String> wGatewayUrlList = new ArrayList<String>();
		for (GatewayColonyEntity gatewayColonyEntity : colList) {
			// TODO 这里的路径是否正确。网关是否修改这里
//			gatewayUrlList.add(gatewayColonyEntity.getColUrl() +api.getListenPath()+api.getApiVersion().getVersion()+"/");
			gatewayUrlList.add(gatewayColonyEntity.getColUrl() +api.getListenPath());
			wGatewayUrlList.add(gatewayColonyEntity.getwColUrl()+api.getListenPath());//外网访问地址
		}
		
		com.alibaba.fastjson.JSONObject jsonObject = com.alibaba.fastjson.JSONObject.parseObject(JSON.toJSONString(api));
		jsonObject.put("gatewayUrls", gatewayUrlList);
		jsonObject.put("wGatewayUrls", wGatewayUrlList);
		result.setSuccessData(jsonObject);
		return result;
	}


	@Override
	public Result<Page<ApiEntity>> apiList(QueryApiEntity entity,int currentPage, int pageSize) {
		
		Result<Page<ApiEntity>> result = new Result<Page<ApiEntity>>();

		Page<ApiEntity> page = newApiDao.findManagerList(entity,currentPage,pageSize);
		
		if(page != null){
			result.setSuccessData(page);
		}else{
			result.setErrorMessage("查询不到结果", ErrorCodeNo.SYS006);
		}
		return result;
	}
	
	private JSONObject generateGwApiJson(String versionId, String apiEnName, String listenPath, String endPoint, Map<String, String> map,

			boolean gatewayApiVersionedTrue) {
		
		String path = Constants.GW_API_JSON;
		JSONObject job = LocalFileReadUtil.readLocalClassPathJson(path);
		
		if(job == null){
			return null;
		}
			
		//拼装新的json
		job.put(DBFieldsConstants.GW_API_NAME, apiEnName);
		job.put(DBFieldsConstants.GW_API_ID, versionId);
		JSONObject versions = new JSONObject();
		for (String key : map.keySet()) {
			JSONObject version = new JSONObject();
			version.put(DBFieldsConstants.GW_API_VERSIONS_NAME, key);
			version.put(DBFieldsConstants.GW_API_VERSIONS_EXPIRES, "-1");
			version.put(DBFieldsConstants.GW_API_VERSIONS_OVERRIDE_TARGET, map.get(key));
			versions.put(key, version);
		}
		//job.put(DBFieldsConstants.GW_API_VERSIONS, versions);
		job.getJSONObject(DBFieldsConstants.GW_API_VERSION_DATA)
			.put(DBFieldsConstants.GW_API_VERSIONS, versions);
		
		JSONObject proxy = (JSONObject)job.get(DBFieldsConstants.GW_API_PROXY);
		proxy.put(DBFieldsConstants.GW_API_PROXY_LISTENPATH, listenPath);
		
		return job;
	}

	@Override
	public String exportApis(List<String> apiIds, HttpServletResponse response) {
		
		if(apiIds == null || apiIds.size() < 1){
			return returnErrorJson("apiId不能为空", ErrorCodeNo.SYS005, response);
		}
		List<DApiBoundList> successApiRecordList = new ArrayList<DApiBoundList>();//导出成功的api的列表
		List<ApiEntity> successApiList = new ArrayList<ApiEntity>();
		String url = PropertiesUtil.getInstance().getValue("findAppsByIds");
		List<ApiEntity> apiList = newApiDao.findApiByIds(apiIds, AuditConstants.API_CHECK_STATE_SUCCESS);
		
		if(apiIds.size() != apiIds.size()){
			return returnErrorJson("部分apiId不存在或状态错误", ErrorCodeNo.DOWNLOAD001, response);
		}
		
		for (ApiEntity apiEntity : apiList) { 
			//获取产品中心applist
			String tempUrl = null;
			try {
				tempUrl = url+"?appIds="+URLEncoder.encode("["+apiEntity.getOpenApplyId()+"]","utf-8");
			} catch (UnsupportedEncodingException e1) {
			}
			String resultStr = ApiCallUtils.getOrDelMethod(tempUrl, null, HttpMethod.GET);
			
			AppList appList = null;
			try{
				com.alibaba.fastjson.JSONArray  jsonArray = com.alibaba.fastjson.JSONArray.parseObject(resultStr).getJSONArray("data");
				
				appList = jsonArray		
						.getJSONObject(0)
						.toJavaObject(AppList.class);
			}catch(Exception e){//如果从产品中心拿不到开放应用
				_LOGGER.info("获取开放应用失败："+apiEntity.getOpenApplyId());
				return returnErrorJson("开放应用(id:"+apiEntity.getOpenApplyId()+")不存在",ErrorCodeNo.DOWNLOAD002, response);
			}
			
			//封装：文档参数和api记录列表
			apiEntity.setAppCode(appList.getAppCode());
			apiEntity.setId(null);
			apiEntity.setUserId(null);
			apiEntity.setUserName(null);
			successApiList.add(apiEntity);
			successApiRecordList.add(new DApiBoundList(
					apiEntity.getId(), 
					apiEntity.getApiChName(), 
					apiEntity.getListenPath(), 
					apiEntity.getApiType(), 
					apiEntity.getOpenApplyId(), 
					apiEntity.getAppCode(), 
					appList.getAppName(), 
					apiEntity.getApiVersion(),
					true));
		}
		// TODO 20171211 mkw 这里的操作人是admin写死的。将来用户模块抽离出来的时候，这里再修改绑定用户id和用户名等
		DApiBoundEntity boundEntity = new DApiBoundEntity(
				successApiRecordList,
				new Date(),
				apiIds.size(),
				successApiList.size(),
				"admin");
		
		dApiBoundService.save(boundEntity);
		
		return returnSuccessFile(successApiList, response);
	}

	private String returnSuccessFile(List<ApiEntity> successApiList, HttpServletResponse response) {
		response.setCharacterEncoding("utf-8");
		response.setContentType("multipart/form-data");
		String dateStr = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
		String fileName = "copy_"+dateStr+".json";
		response.setHeader("Content-Disposition", "attachment; filename="+fileName);
		try{
			response.getOutputStream().write(
					com.alibaba.fastjson.JSONObject.toJSONString(successApiList)
					.getBytes("utf-8"));
			response.getOutputStream().flush();
			response.getOutputStream().close();
		}catch(IOException e){
			_LOGGER.info("返回下载文件时发生异常", e);
		}
		return null;
	}

	private String returnErrorJson(String errorMessage, ErrorCodeNo errorCode, HttpServletResponse response){
		response.setCharacterEncoding("utf-8");
		response.setContentType("application/json");
		try {
			response.getOutputStream().write(
					com.alibaba.fastjson.JSONObject.toJSONString(
							Result.errorResult(errorMessage, errorCode, null, Status.FAILED))
					.getBytes("utf-8"));
			response.getOutputStream().flush();
			response.getOutputStream().close();
		} catch (IOException e) {
			_LOGGER.info("回显错误信息发生异常", e);
		}
		return null;
	}
}
