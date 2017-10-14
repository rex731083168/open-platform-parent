package cn.ce.platform_service.apis.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.http.protocol.HTTP;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import cn.ce.platform_service.apis.dao.INewApiDao;
import cn.ce.platform_service.apis.entity.ApiEntity;
import cn.ce.platform_service.apis.service.IManageApiService;
import cn.ce.platform_service.common.AuditConstants;
import cn.ce.platform_service.common.Constants;
import cn.ce.platform_service.common.DBFieldsConstants;
import cn.ce.platform_service.common.ErrorCodeNo;
import cn.ce.platform_service.common.Result;
import cn.ce.platform_service.common.gateway.ApiCallUtils;
import cn.ce.platform_service.common.gateway.GatewayUtils;
import cn.ce.platform_service.gateway.entity.GatewayColonyEntity;
import cn.ce.platform_service.openApply.entity.OpenApplyEntity;
import cn.ce.platform_service.openApply.service.IManageOpenApplyService;
import cn.ce.platform_service.util.LocalFileReadUtil;
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
	private IManageOpenApplyService manageOpenApplyService;
	
	/**
	 * 
	 * @Title: auditApi
	 * @Description: 批量添加api到网关，如果中途发生异常，回滚之前添加的api，并且reload
	 * @author: makangwei 
	 * @date:   2017年10月13日 下午8:08:38 
	 */
	@Override
	public Result<String> auditApi(List<String> apiId, Integer checkState, String checkMem) {
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
				String apiEnName = apiList.get(0).getApiEnName();
				OpenApplyEntity openApplyEntity = manageOpenApplyService.findById(apiList.get(0).getOpenApplyId());
				String listenPath = "/"+openApplyEntity.getApplyKey()+"/"+apiEnName+"/";
				apiEntity.setListenPath(listenPath);
				
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
				
				/*** 添加api到网关接口 ***/
				JSONObject params = generateGwApiJson(apiEntity.getId(), apiEntity.getApiEnName(), listenPath, map,AuditConstants.GATEWAY_API_VERSIONED_TRUE);
				
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
				String resultStr = ApiCallUtils.putOrPostMethod(gatewayColonyEntity.getColUrl()+Constants.NETWORK_ADD_PATH+"/"+apiEntity.getId(), 
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
					headers, HttpMethod.DELETE);
				
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

	
	private JSONObject generateGwApiJson(String apiId, String apiEnName, String listenPath, Map<String, String> map,
			boolean gatewayApiVersionedTrue) {
		String basePath = System.getProperty("user.dir");
		String path = basePath+Constants.GW_API_JSON;
		JSONObject job = LocalFileReadUtil.readLocalJson(path);
		
		if(job == null){
			return null;
		}
			
		//拼装新的json
		job.put(DBFieldsConstants.GW_API_NAME, apiEnName);
		job.put(DBFieldsConstants.GW_API_ID, apiId);
		JSONObject versions = new JSONObject();
		for (String key : map.keySet()) {
			JSONObject version = new JSONObject();
			version.put(DBFieldsConstants.GW_API_VERSIONS_NAME, key);
			version.put(DBFieldsConstants.GW_API_VERSIONS_EXPIRES, "-1");
			version.put(DBFieldsConstants.GW_API_VERSIONS_OVERRIDE_TARGET, map.get(key));
			versions.put(key, version);
		}
		job.put(DBFieldsConstants.GW_API_VERSIONS, versions);
		
		JSONObject proxy = (JSONObject)job.get(DBFieldsConstants.GW_API_PROXY);
		proxy.put(DBFieldsConstants.GW_API_PROXY_LISTENPATH, listenPath);
		
		return job;
	}
}
