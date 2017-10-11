package cn.ce.platform_service.gateway.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import cn.ce.platform_service.apis.entity.APIEntity;
import cn.ce.platform_service.apis.service.IAPIService;
import cn.ce.platform_service.apisecret.dao.IApiSecretKeyDao;
import cn.ce.platform_service.apisecret.entity.ApiSecretKey;
import cn.ce.platform_service.app.entity.AppEntity;
import cn.ce.platform_service.app.service.IAppService;
import cn.ce.platform_service.apply.dao.IApplyDao;
import cn.ce.platform_service.apply.entity.ApplyEntity;
import cn.ce.platform_service.common.Result;
import cn.ce.platform_service.common.Status;
import cn.ce.platform_service.common.gateway.GatewayUtils;
import cn.ce.platform_service.gateway.service.IGatewayApiService;

/**
* @Description : 网关api和密钥的管理
* @Author : makangwei
* @Date : 2017年8月14日
*/
@Service("gatewayApiService")
public class GatewayApiService implements IGatewayApiService{

	@Autowired
	private IAPIService apiService;
	@Autowired
	private IAppService appService;
	@Autowired @Qualifier("applyDao")
	private IApplyDao applyDao;
	@Autowired
	private IApiSecretKeyDao apiSecretKeyDao;
	
	private static Logger logger = LoggerFactory.getLogger(GatewayApiService.class);
	/**
	 * 
	 * @Description : 添加版本后的api审
	 * @Author : makangwei
	 * @Date : 2017年8月17日
	 * @param apiId
	 * @param checkState
	 * @param checkMem
	 * @return
	 */
	public Result<String> auditApi(String apiId, Integer checkState, String checkMem) {
		
		Result<String> result = new Result<String>();

		APIEntity tempEntity = apiService.findById(apiId);
		
		if(tempEntity == null){
			result.setMessage("当前api不存在，请检查apiId");
			return result;
		}
		
		if(checkState == 3){
			tempEntity.setCheckState(3);
			tempEntity.setCheckMem(checkMem);
			apiService.updateAPI(tempEntity);
		}else if(checkState == 2){
	
			List<APIEntity> apiList = apiService.findByField("apiversion.apiId", tempEntity.getApiVersion().getApiId());

			String apiName = apiList.get(0).getApiEnName();
			
			//这里的apiId已经和传进来的参数apiId有了不用的有意义，传进来的apiId是具体到某一个版本的apiId
			//而这里的apiId已经转化成为不同版本的api所共同拥有的apiId,在这里转换了一下意义
			apiId = apiList.get(0).getApiVersion().getApiId();
			
			AppEntity appEntity = appService.findById(apiList.get(0).getAppId());
			
			// TODO 监听路径是否需要加上appKey
			String listenPath = "/"+appEntity.getAppKey()+"/"+apiName+"/";
			
			Map<String,String> map = new HashMap<String,String>();
			
			for (APIEntity entity : apiList) {
				if(entity.getCheckState() == 2 ){
					map.put(entity.getApiVersion().getVersion()+"", entity.getTestEndPoint());
				}
			}
			map.put(tempEntity.getApiVersion().getVersion()+"", tempEntity.getTestEndPoint());
			
			
			for (String key : map.keySet()) {
				logger.info("版本键："+key+","+"请求地址是："+map.get(key));
			}
			
			Result<String> res1;
			//如果未开启版本控制
//			if(tempEntity.getApiversion() == null || tempEntity.getApiversion().getVersion() == null || tempEntity.getApiversion().getVersion() == ""){
//				res1 = GatewayUtils.postApiToGateway2(apiName,apiId,listenPath,map,true);
//			}else{
				res1 = GatewayUtils.postApiToGateway2(apiName,apiId,listenPath,map,false);
//			}
				
			
			
			//1、修改网关,发生异常回滚
			if(res1.getStatus() == Status.SUCCESS){
				// 修改数据库状态
				tempEntity.setCheckState(2);
				apiService.updateAPI(tempEntity);
			}else{
				result.setMessage("网关发生异常。请稍后添加");
				return result;
			}
				
		}else{
			result.setMessage("修改状态不正确。请输入正确的状态"); // TODO 重复的判断
			return result;
		}
		
		result.setStatus(Status.SUCCESS);;
		return result;
	}

	public Result<String> auditKey(String secretKey, Integer checkState) {
		
		Result<String> result = new Result<String>();
		//1、检查当前key是否存在
		ApiSecretKey secretEntity = apiSecretKeyDao.findOneByField("secretKey",secretKey);
		
		if(secretEntity == null){
			result.setMessage("当前密钥不存在，请检查密钥是否正确");
			return result;
		}
		
		APIEntity apiEntity = apiService.findById(secretEntity.getApiId());
		
		if(apiEntity == null){
			result.setMessage("当前secretKey没有和api绑定。请重新申请");
			return result;
		}
		
		if(checkState == 3){
			secretEntity.setCheckState(3);
			apiSecretKeyDao.updateApiSecretKey(secretEntity);
			result.setStatus(Status.SUCCESS);
			result.setMessage("修改不可用成功！");
			return result;
		}else if(checkState == 2){
			//2、如果存在先在网关中添加key，否则返回错误结果
			
			Result<String> res1 = GatewayUtils.addKeyToGateway(
					null,apiEntity.getRate(),apiEntity.getPer(),apiEntity.getQuotaMax(),apiEntity.getQuotaRenewalRate(),
					apiEntity.getApiEnName(),apiEntity.getId(),secretKey,apiEntity.getApiVersion().getVersion());
			
			if(res1.getStatus() ==Status.FAILED){
				result.setMessage("当前网关集群不可用。请稍后再试");
				return result;
			}
			
			secretEntity.setCheckState(2);
			apiSecretKeyDao.updateApiSecretKey(secretEntity);
			result.setStatus(Status.SUCCESS);
			result.setMessage("修改可用成功");
			return result;
			
		}else {
			result.setMessage("审核状态有误，请重新审核");
			return result;
		}
	}

	public Result<String> deleKey(String secretKey , String applyId) {
		
		Result<String> result = new Result<String>();
		//1、从数据库中查找key
		ApiSecretKey secretEntity = apiSecretKeyDao.findOneByField("secretKey",secretKey);
		
		if(secretEntity == null){
			result.setMessage("当前密钥不存在");
			return result;
		}
		
		ApplyEntity applyById = applyDao.findById(applyId);
		if(null == applyById){
			result.setErrorMessage("应用信息不存在!");
			return result;
		}
		
		//2、根据不同的状态选择不同的删除key策略
		if(secretEntity.getCheckState() == 2){
			//删除网关
			Result<String> res1 = GatewayUtils.deleteKeyFromGateway(secretEntity.getSecretKey());
			
			if(res1.getStatus() != Status.SUCCESS){
				result.setMessage("当前网关暂不可连接,请稍后再试");
				
				// TODO 网关删除成功后方可删除数据库中的key,否则不能删除
				return result;
			}
			
		}
		//删除数据库
		int i = apiSecretKeyDao.delApi(secretEntity.getId());
		
		logger.info("删除了"+i+"个密钥");
		
		/***
		 * 删除成功后将api与应用Apply关系移除
		 * @author lida
		 * @date 2017年8月23日17:15:11
		 */
		List<String> apiIds = applyById.getAuthIds();
		if(apiIds == null){
			apiIds = new ArrayList<>();
		}
		
		if(apiIds.contains(secretEntity.getApiId())){
			logger.info(">>>> apiService remove apply api begin,applyId:" + applyId + ";apiId:" + secretEntity.getApiId());
			apiIds.remove(secretEntity.getApiId());
			applyById.setAuthIds(apiIds);
			applyDao.saveOrUpdate(applyById);
			logger.info("<<< apiService update apply end");
		}
		
		
		
		result.setStatus(Status.SUCCESS);
		result.setMessage("删除成功");
		return result;
	}

	/**
	 * 审核后推送网关是多版本+oauth
	 * <p>Title: auditApi2</p>   
	 * <p>Description: </p>   
	 * @param apiId
	 * @param checkState
	 * @param checkMem
	 * @return   
	 * @see cn.ce.platform_service.gateway.service.IGatewayApiService#auditApi2(java.lang.String, java.lang.Integer, java.lang.String)
	 */
	public Result<String> auditApi2(String apiId, Integer checkState, String checkMem) {

		Result<String> result = new Result<String>();

		APIEntity tempEntity = apiService.findById(apiId);
		
		if(tempEntity == null){
			result.setMessage("当前api不存在，请检查apiId");
			return result;
		}
		
		if(checkState == 3){
			//修改数据库状态
			tempEntity.setCheckState(3);
			tempEntity.setCheckMem(checkMem);//审核失败，添加审核失败原因
			apiService.updateAPI(tempEntity);
			
		}else if(checkState == 2){ //添加api到网关后分配client和secret，发生异常回滚
	
			List<APIEntity> apiList = apiService.findByField("apiversion.apiId", tempEntity.getApiVersion().getApiId());

			String apiName = apiList.get(0).getApiEnName();
			
			//这里的apiId已经和传进来的参数apiId有了不用的有意义，传进来的apiId是具体到某一个版本的apiId
			//而这里的apiId已经转化成为不同版本的api所共同拥有的apiId,在这里转换了一下意义
			apiId = apiList.get(0).getApiVersion().getApiId();
			
			AppEntity appEntity = appService.findById(apiList.get(0).getAppId());
			// TODO 监听路径是否需要加上appKey
			String listenPath = "/"+appEntity.getAppKey()+"/"+apiName+"/";
			
			Map<String,String> map = new HashMap<String,String>();
			
			for (APIEntity entity : apiList) {
				if(entity.getCheckState() == 2 ){
					map.put(entity.getApiVersion().getVersion()+"", entity.getTestEndPoint());
				}
			}
			map.put(tempEntity.getApiVersion().getVersion()+"", tempEntity.getTestEndPoint());
			
			
			for (String key : map.keySet()) {
				logger.info("版本键："+key+","+"请求地址是："+map.get(key));
			}
			
			Result<String> res1;
			//如果未开启版本控制
//			if(tempEntity.getApiversion() == null || tempEntity.getApiversion().getVersion() == null || tempEntity.getApiversion().getVersion() == ""){
//				res1 = GatewayUtils.postApiToGateway2(apiName,apiId,listenPath,map,true);
//			}else{
				//res1 = GatewayUtils.postApiToGateway2(apiName,apiId,listenPath,map,false);
//			}
				res1 = GatewayUtils.postApiToGateway3(tempEntity.getApiEnName(), apiId, listenPath, map,false);
			
			
			//1、修改网关,发生异常回滚
			if(res1.getStatus() == Status.SUCCESS){
				// 修改数据库状态
				tempEntity.setCheckState(2);
				apiService.updateAPI(tempEntity);
			}else{
				result.setMessage("网关发生异常。请稍后添加");
				return result;
			}
				
		}else{
			result.setMessage("修改状态不正确。请输入正确的状态"); // TODO 重复的判断
			return result;
		}
		
		
		result.setStatus(Status.SUCCESS);;
		return result;
	}
}
