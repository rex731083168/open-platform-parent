package cn.ce.platform_service.common.gateway;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.codehaus.jackson.map.ObjectMapper;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import cn.ce.platform_service.common.Constants;
import cn.ce.platform_service.common.Result;
import cn.ce.platform_service.common.Status;
import cn.ce.platform_service.gateway.dao.GatewayManageDao;
import cn.ce.platform_service.gateway.dao.GatewayNodeManageDao;
import cn.ce.platform_service.gateway.entity.GatewayColonyEntity;
import cn.ce.platform_service.gateway.entity.GatewayKeyEntity;
import cn.ce.platform_service.gateway.entity.GatewayNodeEntity;
import cn.ce.platform_service.util.RandomUtil;



/**
 * 
 * @author makangwei
 * 2017-7-31
 *
 */
@Controller
public class GatewayUtils {

	private static Logger LOGGER = LoggerFactory.getLogger(GatewayUtils.class);
	
	@Autowired
	private GatewayManageDao gatewayManageDao;
	@Autowired
	private GatewayNodeManageDao gatewayNodeManageDao;
	
	private static GatewayUtils utils;
	
	@PostConstruct
	public void init(){
		utils = this;
		utils.gatewayManageDao = this.gatewayManageDao;
		utils.gatewayNodeManageDao = this.gatewayNodeManageDao;
	}
	
	/**
	 * 添加api到网关
	 * @param apiName api名称（必填）
	 * @param apiId api唯一编码，不输入将自动生成
	 * @param userId api上传者id（必填）
	 * @param listenPath api服务名称（必填）
	 * @param targetUrl api基础访问地址，基础访问地址和api服务名称共同组成api的真是访问路径（必填）
	 * @param status 是否开启 true/false
	 * @return
	 */
	public static Result<String> andApiToGateway(String apiName,String apiId,String userId,String listenPath,String targetUrl,boolean status) {
				
		Result<String> result = new Result<String>();
		
		ApiGatewayEntity apiGatewayEntity = new ApiGatewayEntity();
		
		//实体参数校验
		if(apiName == null || apiName.trim() == ""){
			result.setMessage("api名称不能为空");
			return result;
		}else if(listenPath == null || listenPath.trim() == ""){
			result.setMessage("listenPath不能为空");
			return result;
		}else if(targetUrl == null || targetUrl.trim() ==""){
			result.setMessage("targetUrl不能为空");
			return result;
		}else if(apiId == null || apiId.trim() == ""){
			apiGatewayEntity.setApi_id(RandomUtil.random16Number());
		}
		
		apiGatewayEntity.setName(apiName);
		apiGatewayEntity.setApi_id(apiId);
		apiGatewayEntity.setOrg_id(userId);
		apiGatewayEntity.setActive(status);
		
		apiGatewayEntity.getProxy().setListen_path(listenPath);
		apiGatewayEntity.getProxy().setTarget_url(targetUrl);
		
		ObjectMapper mapper = new ObjectMapper(); 
		
		JSONObject params = null;
		try {
			String str = mapper.writeValueAsString(apiGatewayEntity);
			
			params = new JSONObject(str);
			
			//将default属性转化为Default
			LOGGER.info("params:"+params.toString());
			JSONObject Default = params.getJSONObject("version_data").getJSONObject("versions").getJSONObject("default");
			params.getJSONObject("version_data").getJSONObject("versions").remove("default");
			params.getJSONObject("version_data").getJSONObject("versions").put("Default", Default);
			
		} catch (IOException e) {
			e.printStackTrace();
			return result;
		}
		//------------------------------以上是参数校验----------------------------------------------------------------------
		
		// TODO 这里添加api到节点。现在只有一个网关。所以忽略了根据网关来查询节点。如果后期有多个网关集群，则需要考虑多个集群多个节点上传api
		List<GatewayNodeEntity> nodeList = getAllGatwayNodes();
		
		List<String> urlList = new ArrayList<String>();
		
		//添加api到每一个网关节点，如果发生异常，立即撤销本次所有的添加
		for (GatewayNodeEntity gatewayNodeEntity : nodeList) {
			
			String uri = gatewayNodeEntity.getNodeUrl()+Constants.NETWORK_ADD_PATH;
			
			boolean bool = ApiCallUtils.postGwJson(uri, params);
			
			if(!bool){
				//回滚
				deleteApiFromGatewayNodes(urlList,apiId);
				result.setMessage("uri:"+gatewayNodeEntity.getNodeUrl()+"添加api失败,已回滚所有api");
				return result;
			}
			
			urlList.add(gatewayNodeEntity.getNodeUrl());
		}
		
		//reload所有节点
		List<String> urlList2 = new ArrayList<String>();
		
		for (GatewayNodeEntity gatewayNodeEntity : nodeList) {
			
			String uri = gatewayNodeEntity.getNodeUrl()+Constants.NETWORK_RELOAD_PATH;
			
			if(!ApiCallUtils.getGwReload(uri)){
				//回滚
				deleteApiFromGatewayNodes(urlList2,apiId);
				result.setMessage("uri:"+gatewayNodeEntity.getNodeUrl()+"reload失败，已回滚所有api");
				return result;
			}
			urlList2.add(gatewayNodeEntity.getNodeUrl());
		}

		result.setStatus(Status.SUCCESS);
		
		return result;
	}
	

	public static Result<String> postApiToGateway2(String apiName, String apiId, String listenPath,Map<String,String> versionUrl, boolean notVersioned){
		
		Result<String> result = new Result<String>();
		
		//实体参数校验
		if(apiName == null || apiName.trim() == ""){
			LOGGER.info("----->api名称不能为空");
			return result;
		}else if(listenPath == null || listenPath.trim() == ""){
			LOGGER.info("----->listenPath不能为空");
			return result;
		}else if(apiId == null || apiId.trim() == ""){
			LOGGER.info("----->apiId不能为空");
			return result;
		}
		
		JSONObject params = ApiGatewayEntity2.getApiGatewayEntity2(apiName, apiId, listenPath, versionUrl,notVersioned);
		

		//------------------------------以上是参数校验----------------------------------------------------------------------
		
		// TODO 这里添加api到节点。现在只有一个网关。所以忽略了根据网关来查询节点。如果后期有多个网关集群，则需要考虑多个集群多个节点上传api
		List<GatewayNodeEntity> nodeList = getAllGatwayNodes();
		
		List<String> urlList = new ArrayList<String>();
		
		//添加api到每一个网关节点，如果发生异常，立即撤销本次所有的添加
		for (GatewayNodeEntity gatewayNodeEntity : nodeList) {
			
			String uri = gatewayNodeEntity.getNodeUrl()+Constants.NETWORK_ADD_PATH;
			
			boolean bool = ApiCallUtils.postGwJson(uri, params);
			
			if(!bool){
				//回滚
				deleteApiFromGatewayNodes(urlList,apiId);
				result.setMessage("uri:"+gatewayNodeEntity.getNodeUrl()+"添加api失败,已回滚所有api");
				return result;
			}
			
			urlList.add(gatewayNodeEntity.getNodeUrl());
		}
		
		//reload所有节点
		List<String> urlList2 = new ArrayList<String>();
		
		for (GatewayNodeEntity gatewayNodeEntity : nodeList) {
			
			String uri = gatewayNodeEntity.getNodeUrl()+Constants.NETWORK_RELOAD_PATH;
			
			if(!ApiCallUtils.getGwReload(uri)){
				//回滚
				deleteApiFromGatewayNodes(urlList2,apiId);
				result.setMessage("uri:"+gatewayNodeEntity.getNodeUrl()+"reload失败，已回滚所有api");
				return result;
			}
			urlList2.add(gatewayNodeEntity.getNodeUrl());
		}

		result.setStatus(Status.SUCCESS);
		
		return result;
	}
	
	public static Result<String> postApiToGateway3(String apiName, String apiId, String listenPath,Map<String,String> versionUrl, boolean notVersioned){
		
		Result<String> result = new Result<String>();
		
		//JSONObject params = ApiGatewayEntity2.getApiGatewayEntity2(apiName, apiId, listenPath, versionUrl,notVersioned);
		JSONObject params = ApiGatewayEntity3.getApiGatewayEntity3(apiName, apiId, listenPath, versionUrl,notVersioned);

		//------------------------------以上是参数校验----------------------------------------------------------------------
		
		// TODO 这里添加api到节点。现在只有一个网关。所以忽略了根据网关来查询节点。如果后期有多个网关集群，则需要考虑多个集群多个节点上传api
		List<GatewayNodeEntity> nodeList = getAllGatwayNodes();
		
		List<String> urlList = new ArrayList<String>();
		
		//添加api到每一个网关节点，如果发生异常，立即撤销本次所有的添加
		for (GatewayNodeEntity gatewayNodeEntity : nodeList) {
			
			String uri = gatewayNodeEntity.getNodeUrl()+Constants.NETWORK_ADD_PATH;
			
			boolean bool = ApiCallUtils.postGwJson(uri, params);
			
			if(!bool){
				//回滚
				deleteApiFromGatewayNodes(urlList,apiId);
				result.setMessage("uri:"+gatewayNodeEntity.getNodeUrl()+"添加api失败,已回滚所有api");
				return result;
			}
			
			urlList.add(gatewayNodeEntity.getNodeUrl());
		}
		
		//reload所有节点
		List<String> urlList2 = new ArrayList<String>();
		
		for (GatewayNodeEntity gatewayNodeEntity : nodeList) {
			
			String uri = gatewayNodeEntity.getNodeUrl()+Constants.NETWORK_RELOAD_PATH;
			
			if(!ApiCallUtils.getGwReload(uri)){
				//回滚
				deleteApiFromGatewayNodes(urlList2,apiId);
				result.setMessage("uri:"+gatewayNodeEntity.getNodeUrl()+"reload失败，已回滚所有api");
				return result;
			}
			urlList2.add(gatewayNodeEntity.getNodeUrl());
		}

		result.setStatus(Status.SUCCESS);
		
		return result;
	}
	
	
	
	/**
	 * 根据id删除api
	 * @param apiId
	 * @return
	 */
	public static Result<String> deleteApiFromGateway(String apiId){
		
		Result<String> result = new Result<String>();
		
		List<GatewayNodeEntity> nodeList = getAllGatwayNodes();
		
		//已经删除api的节点集合
		List<String> deledList = new ArrayList<String>();
		
		// TODO 先拿到当前集群下旧的节点数据,现在是写死的，只拿到数据库中第一个集群中的旧数据
		String oldApiStr = getOldApiEntity(apiId);
		
		for (GatewayNodeEntity gatewayNodeEntity : nodeList) {
			
			String uri = gatewayNodeEntity.getNodeUrl();
			
			boolean flag = ApiCallUtils.deleteGwJson(uri+Constants.NETWORK_DEL_PATH+"/"+apiId);
			
			if(!flag){
				
				//发生异常时候，将原来已经删除的数据再恢复到节点
				recoverApiFromGatewayNodes(deledList,apiId,oldApiStr);
				
				result.setMessage("节点"+gatewayNodeEntity.getNodeId()+"，url为："+gatewayNodeEntity.getNodeUrl()+"删除api发生异常");
				
				return result;
			}
			
			deledList.add(gatewayNodeEntity.getNodeUrl());
		}
		
		result.setMessage("api在所有集群中删除成功");
		
		return result;
		
	}
	

	
	
	/**
	 * 根据apiId修改网关，如果当前apiId在网关中不存在则在网关注册新的api
	 * @return
	 */
	// TODO 目前暂不支持跟新操作。在网关上只能添加或者删除api
	public static Result<String> modifyApiFromGateway(String apiName,String apiId,String userId,String listenPath,String targetUrl,boolean status){
		
		Result<String> result = new Result<String>();
		
		ApiGatewayEntity apiGatewayEntity = new ApiGatewayEntity();
		
		
		//先从网关查询就的api，防止发生异常时候能够回滚
		//ApiGatewayEntity oldEntity = getOldApiEntity(apiId);
		
		
		//API参数校验
		if(apiName == null || apiName.trim() == ""){
			result.setMessage("api名称不能为空");
			return result;
		}else if(userId == null || userId.trim() == ""){
			result.setMessage("userId不能为空");
			return result;
		}else if(listenPath == null || listenPath.trim() == ""){
			result.setMessage("listenPath不能为空");
			return result;
		}else if(targetUrl == null || targetUrl.trim() ==""){
			result.setMessage("targetUrl不能为空");
			return result;
		}else if(apiId == null || apiId.trim() == ""){
			apiGatewayEntity.setApi_id(RandomUtil.random16Number());
		}
		
		apiGatewayEntity.setName(apiName);
		apiGatewayEntity.setApi_id(apiId);
		apiGatewayEntity.setOrg_id(userId);
		
		apiGatewayEntity.getProxy().setListen_path(listenPath);
		apiGatewayEntity.getProxy().setTarget_url(targetUrl);
		
		ObjectMapper mapper = new ObjectMapper(); 
		
		JSONObject params = null;
		try {
			String str = mapper.writeValueAsString(apiGatewayEntity);
			
			params = new JSONObject(str);
			
			//将default属性转化为Default
			LOGGER.info("params:"+params.toString());
			JSONObject Default = params.getJSONObject("version_data").getJSONObject("versions").getJSONObject("default");
			params.getJSONObject("version_data").getJSONObject("versions").remove("default");
			params.getJSONObject("version_data").getJSONObject("versions").put("Default", Default);
			
		} catch (IOException e) {
			e.printStackTrace();
			return result;
		}
		
		// TODO 这里修改节点api。现在只有一个网关。所以忽略了根据网关来查询节点。如果后期有多个网关集群，则需要考虑多个集群多个节点上传api
		List<GatewayNodeEntity> nodeList = getAllGatwayNodes();
		
		//修改每个网关上的api，如果发生异常，立即撤销本次所有的添加
		for (GatewayNodeEntity gatewayNodeEntity : nodeList) {
			
			String uri = gatewayNodeEntity.getNodeUrl()+Constants.NETWORK_UPDATE_PATH;
			
			boolean bool = ApiCallUtils.putGwJson(uri+"/"+apiId, params);
			
			if(!bool){
				// TODO 如果修改失败返回未修改状态
//				deleteApiFromGatewayNodes(nodeList,apiId);
				result.setMessage("uri:"+gatewayNodeEntity.getNodeUrl()+"修改api失败,已回滚所有api");
			}
			
		}
		
		//reload所有节点
		for (GatewayNodeEntity gatewayNodeEntity : nodeList) {
			
			String uri = gatewayNodeEntity.getNodeUrl()+Constants.NETWORK_RELOAD_PATH;
			
			if(!ApiCallUtils.getGwReload(uri)){
				// TODO 如果reload修改失败返回未修改状态
//				deleteApiFromGatewayNodes(nodeList,apiId);
				result.setMessage("uri:"+gatewayNodeEntity.getNodeUrl()+"reload失败，已回滚所有api");
				return result;
			}
		}

		result.setStatus(Status.SUCCESS);
		
		return result;
	}
	
	//添加key到网关
	public static Result<String> addKeyToGateway(Integer expires,Integer rate,Integer per,Integer quota_max,Integer quota_renewal_rate,
			String apiName,String apiId,String secretKey,String version){
		Result<String> result = new Result<String>();
		
		//拼接参数
		GatewayKeyEntity gkEntity = new GatewayKeyEntity();
		
		//expires默认为-1，永不过期
		gkEntity.setRate(rate);
		
		gkEntity.setPer(per);
		
		gkEntity.setQuota_max(quota_max);
		
		gkEntity.setQuota_renewal_rate(quota_renewal_rate);
		
		gkEntity.setQuota_remaining(quota_max);
		
		JSONObject api = new JSONObject();
		
		api.put("api_name", apiName);
		
		api.put("api_id", apiId);
		
		List<String> versions = new ArrayList<String>(); 
		
		versions.add(version);
		
		api.put("versions", versions);
		
		JSONObject job = new JSONObject();
		
		job.put(apiId, api);
		
		LOGGER.info(job.toString());
		
		gkEntity.setAccess_rights(job);
		
		//实体参数校验
		LOGGER.info("----------添加key到网关的参数:"+gkEntity.toString());
		
		
		
		ObjectMapper mapper = new ObjectMapper(); 
		
		JSONObject params = null;
		try {
			//String str = mapper.writeValueAsString(gkEntity);
			
			params = new JSONObject(gkEntity);
			
		} catch (Exception e) {
			e.printStackTrace();
			return result;
		}
		//------------------------------以上是参数校验----------------------------------------------------------------------
		
		// TODO 这里添加api到节点。现在只有一个网关。所以忽略了根据网关来查询节点。如果后期有多个网关集群，则需要考虑多个集群多个节点上传api
		List<GatewayNodeEntity> nodeList = getAllGatwayNodes();
		
		List<String> urlList = new ArrayList<String>();
		
		//添加api到每一个网关节点，如果发生异常，立即撤销本次所有的添加
		for (GatewayNodeEntity gatewayNodeEntity : nodeList) {
			
			String uri = gatewayNodeEntity.getNodeUrl()+Constants.NETWORK_ADD_KEY_PATH+"/"+secretKey;
			
			boolean bool = ApiCallUtils.postGwJson(uri, params);
			
			if(!bool){
				//回滚
				deleteKeyFromGatewayNodes(urlList,secretKey);
				result.setMessage("uri:"+gatewayNodeEntity.getNodeUrl()+"添加api失败,已回滚所有api");
				return result;
			}
			
			urlList.add(gatewayNodeEntity.getNodeUrl());
		}
		
		//reload所有节点
		List<String> urlList2 = new ArrayList<String>();
		
		for (GatewayNodeEntity gatewayNodeEntity : nodeList) {
			
			String uri = gatewayNodeEntity.getNodeUrl()+Constants.NETWORK_RELOAD_PATH;
			
			if(!ApiCallUtils.getGwReload(uri)){
				//回滚
				deleteKeyFromGatewayNodes(urlList2,secretKey);
				result.setMessage("uri:"+gatewayNodeEntity.getNodeUrl()+"reload失败，已回滚所有api");
				return result;
			}
			urlList2.add(gatewayNodeEntity.getNodeUrl());
		}

		result.setStatus(Status.SUCCESS);
		
		return result;
	}
	
	/**
	 * 删除key到网关
	 * @param apiId
	 * @return
	 */
	public static Result<String> deleteKeyFromGateway(String secretKey){
		
		Result<String> result = new Result<String>();
		
		List<GatewayNodeEntity> nodeList = getAllGatwayNodes();
		
		//已经删除api的节点集合
		List<String> deledList = new ArrayList<String>();
		
		// TODO 先拿到当前集群下旧的节点数据,现在是写死的，只拿到数据库中第一个集群中的旧数据
		String oldApiStr = getOldKeyEntity(secretKey);
		
		if(oldApiStr == Status.FAILED){
			LOGGER.info("-------------该key在网关中不存在----------------");
			result.setStatus(Status.SUCCESS);
			return result;
		}else if(oldApiStr == null){
			return result;
		}
		
		for (GatewayNodeEntity gatewayNodeEntity : nodeList) {
			
			String uri = gatewayNodeEntity.getNodeUrl();
			
			boolean flag = ApiCallUtils.deleteGwJson(uri+Constants.NETWORK_DEL_KEY_PATH+"/"+secretKey);
			
			if(!flag){
				
				//发生异常时候，将原来已经删除的数据再恢复到节点
				recoverApiFromGatewayNodes(deledList,secretKey,oldApiStr);
				
				LOGGER.error("节点"+gatewayNodeEntity.getNodeId()+"，url为："+gatewayNodeEntity.getNodeUrl()+"删除secretKey发生异常");
				result.setMessage("系统网关异常");
				
				return result;
			}
			
			deledList.add(gatewayNodeEntity.getNodeUrl());
		}
		
		result.setStatus(Status.SUCCESS);
		result.setMessage("密钥在所有集群中删除成功");
		
		return result;
		
	}


	//查询所有集群
	public static List<GatewayColonyEntity> getAllGatewayColony(){
		
		List<GatewayColonyEntity> colonyList = utils.gatewayManageDao.getAll(GatewayColonyEntity.class);
		
		return colonyList;
	}
	
	//根据集群id查询当前集群下的所有节点
	public static List<GatewayNodeEntity> getGatwayNodesByColonyId(Integer colId){
		
		@SuppressWarnings("unchecked")
		List<GatewayNodeEntity> nodeList = utils.gatewayNodeManageDao.findByField("colId", colId, GatewayNodeEntity.class);
		
		return nodeList;
		
	}
	
	//查询所有集群节点
	public static List<GatewayNodeEntity> getAllGatwayNodes(){
		
		List<GatewayNodeEntity> nodeList = utils.gatewayNodeManageDao.getAll(GatewayNodeEntity.class);
		
		return nodeList;
		
	}
	
	//批量删除网关节点上的api
	private static void deleteApiFromGatewayNodes(List<String> urlList, String apiId) {
		
		for (String uri : urlList) {
			ApiCallUtils.deleteGwJson(uri+Constants.NETWORK_DEL_PATH+"/"+apiId);
		}
		
	}
	
	//网关删除密钥
	private static void deleteKeyFromGatewayNodes(List<String> urlList, String keyId) {
		
		for(String uri : urlList){
			ApiCallUtils.deleteGwJson(uri+Constants.NETWORK_DEL_KEY_PATH+"/"+keyId);
		}
		
	}
	
	//批量恢复网关节点的api
	private static void recoverApiFromGatewayNodes(List<String> urlList,String apiId,String str){
		
		for (String uri : urlList) {
			
			JSONObject job = new JSONObject(str);
			
			boolean flag1 = ApiCallUtils.putGwJson(uri+Constants.NETWORK_UPDATE_PATH+"/"+apiId, job);
			
			if(!flag1){
				// TODO 如果api恢复回退的过程中也发生异常需要处理
			}
		}
	}
	
	//根据apiId查询旧的ApiGatewayEntity
	private static String getOldApiEntity(String apiId) {
		
		List<GatewayColonyEntity> colList = getAllGatewayColony();
		
		if(colList.size() < 1){
			LOGGER.error("当前数据库中集群为空！");
			return null;
		}
		
		// TODO　目前只有一个集群，只从第一个集群里拿数据。后期需要考虑多个集群中拿数据
		String jsonStr = null;
		try {
			jsonStr = ApiCallUtils.getGwJsonApi(colList.get(0).getColUrl()+Constants.NETWORK_GET_PATH+"/"+apiId);
		} catch (Exception e) {
			e.printStackTrace();
			jsonStr = null;
		} 
		
		if(jsonStr == null){
			LOGGER.error("--------根据apiId从当前集群中查询不到旧的api，集群可能有异常");
		}
		LOGGER.info("-----"+jsonStr+"-----");
		
		return jsonStr;
		
	}
	//根据密钥查询旧的密钥实体
	private static String getOldKeyEntity(String secretKey) {
		
		List<GatewayColonyEntity> colList = getAllGatewayColony();
		
		if(colList.size() < 1){
			LOGGER.error("当前数据库中集群为空！");
			return null;
		}
		
		// TODO　目前只有一个集群，只从第一个集群里拿数据。后期需要考虑多个集群中拿数据
		String jsonStr = null;
		try {
			jsonStr = ApiCallUtils.getGwJsonApi(colList.get(0).getColUrl()+Constants.NETWORK_DEL_KEY_PATH+"/"+secretKey);
		} catch (Exception e) {
			e.printStackTrace();
			jsonStr = null;
			return jsonStr;
		} 
		
		if(jsonStr == null){
			LOGGER.error("--------根据secretKey从当前集群中查询不到旧的secretKey，集群可能有异常");
			return Status.FAILED;
		}
		
		LOGGER.info("-----"+jsonStr+"-----");
		
		return jsonStr;
	}

}

