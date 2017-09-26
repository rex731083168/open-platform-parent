package cn.ce.platform_service.apis.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;

import cn.ce.platform_service.apis.dao.IApiOauthDao;
import cn.ce.platform_service.apis.entity.APIEntity;
import cn.ce.platform_service.apis.entity.ApiAuditEntity;
import cn.ce.platform_service.apis.service.IAPIService;
import cn.ce.platform_service.apis.service.IApiOauthService;
import cn.ce.platform_service.app.entity.AppEntity;
import cn.ce.platform_service.app.service.IAppService;
import cn.ce.platform_service.apply.dao.IApplyDao;
import cn.ce.platform_service.apply.entity.ApplyEntity;
import cn.ce.platform_service.common.Constants;
import cn.ce.platform_service.common.Result;
import cn.ce.platform_service.common.Status;
import cn.ce.platform_service.common.gateway.GatewayUtils;
import cn.ce.platform_service.page.Page;
import cn.ce.platform_service.users.entity.User;
import cn.ce.platform_service.users.service.IUserService;
import cn.ce.platform_service.util.HttpUtils;

/**
* @Description : 说明
* @Author : makangwei
* @Date : 2017年8月18日
*/
@Service(value = "apiOauthService")
public class ApiOauthServiceImpl implements IApiOauthService{

	@Autowired
	private IAPIService apiService;
	@Autowired
	private IAppService appService;
	@Autowired @Qualifier("applyDao")
	private IApplyDao applyDao;
	@Autowired
	private IApiOauthDao apiOauthDao;
	@Autowired
	private IUserService userService;
	@Autowired
	private MongoTemplate mongoTemplate;
	
	private static Logger logger = LoggerFactory.getLogger(ApiOauthServiceImpl.class);
	
	public Result<String> applyApi(String userId, String apiId , String applyId) {
		
		Result<String> result = new Result<String>();
		
		if(StringUtils.isBlank(userId)){
			result.setMessage("userId不能为空");
			return result;
		}else if(StringUtils.isBlank(apiId)){
			result.setMessage("apiId不能为空");
			return result;
		}

		User user = userService.findOne(userId);
		
		if(user == null){
			result.setMessage("当前用户不存在");
			return result;
		}
		
		APIEntity apiEntity = apiService.findById(apiId);
		
		if(apiEntity == null){
			result.setMessage("当前api不存在");
			return result;
		}
		
		ApplyEntity applyEntity = applyDao.findById(applyId);
		
		if(null == applyEntity){
			result.setErrorMessage("应用信息不存在!");
			return result;
		}
		
		
		AppEntity appEntity = appService.findById(apiEntity.getAppid());

		//拼接对象
		ApiAuditEntity auditEntity = new ApiAuditEntity();
		//auditEntity.setId(UUID.randomUUID().toString().replace("-", ""));
		//auditEntity.setApiId(apiEntity.getApiversion().getApiId()); // TODO 这里的apiId是apiVersion里面的apiId，这是错误的
		auditEntity.setApiId(apiEntity.getId());
		auditEntity.setVersionApiId(apiEntity.getApiversion().getApiId());
		auditEntity.setApiChName(apiEntity.getApichname());
		auditEntity.setApiEnName(apiEntity.getApienname());
		auditEntity.setAppId(appEntity.getId());
		auditEntity.setAppName(appEntity.getAppname());
		auditEntity.setAppKey(appEntity.getAppkey());
		auditEntity.setCheckState(1);
		auditEntity.setUserId(user.getId());
		auditEntity.setSupplier_id(apiEntity.getUserid());
		auditEntity.setUserName(user.getUsername());
		auditEntity.setPer(apiEntity.getPer());
		auditEntity.setRate(apiEntity.getRate());
		auditEntity.setQuotaMax(apiEntity.getQuota_max());
		auditEntity.setQuotaRenewalRate(apiEntity.getQuota_renewal_rate());
		auditEntity.setApplyId(applyEntity.getId());
		auditEntity.setApplyTime(new Date());
		auditEntity.setApplyName(applyEntity.getApplyName());
		
		if(apiEntity.getApiversion() != null && StringUtils.isNotBlank(apiEntity.getApiversion().getVersion())){
			auditEntity.setVersion(apiEntity.getApiversion().getVersion());
		}
		auditEntity.setClientId("");
		auditEntity.setSecret("");
		
		apiOauthDao.addAuditEntity(auditEntity);
		
		
		/***
		 * 申请成功后将api与应用Apply建立关系
		 * @author lida
		 * @date 2017年8月23日16:47:40
		 */
		List<String> authIds = applyEntity.getAuthIds();
		if(authIds == null){
			authIds = new ArrayList<>();
		}
		
		if(!authIds.contains(auditEntity.getId())){
			logger.info(">>>> apiService add apply api begin,applyId:" + applyId + ";apiId:" + apiId);
			authIds.add(auditEntity.getId());
			applyEntity.setAuthIds(authIds);
			applyDao.saveOrUpdate(applyEntity);
			logger.info("<<< apiService update apply end");
		}
		
		
		result.setStatus(Status.SUCCESS);
		return result;
	}

	@Override
	public ApiAuditEntity findApiByClientId(String clientId) {
		ApiAuditEntity apiAuditEntity = null;
		List<ApiAuditEntity> find = mongoTemplate.find(new Query(Criteria.where("client_id").is(clientId)), ApiAuditEntity.class);
		if(!find.isEmpty()){
			apiAuditEntity = find.get(0);
			if(StringUtils.isNotBlank(apiAuditEntity.getApiId())){
				apiAuditEntity.setApiEntity(apiService.findById(apiAuditEntity.getApiId()));
			}
		}
		// TODO Auto-generated method stub
		return apiAuditEntity;
	}

	public Result<String> allowApi(String authId,Integer checkState,String checkmem) {
		
		Result<String> result = new Result<String>();
		
		ApiAuditEntity auditEntity = apiOauthDao.findById(authId);
		if(auditEntity == null){
			result.setMessage("当前数据不存在，请重新输入！");
			return result;
		}
		
		String applyId = auditEntity.getApplyId();
		
		ApplyEntity applyEntity = applyDao.findById(applyId);
		
		if(applyEntity == null){
			//当前应用已经删除，删除审核记录
			apiOauthDao.deleteById(auditEntity.getId());
			result.setErrorMessage("使用者已删除应用，无需审核");
			return result;
		}
		
		//api提供者拒绝分配clientId和secret
		if(checkState == 3){
			auditEntity.setCheckState(3);
			auditEntity.setCheckMem(checkmem);
			apiOauthDao.UpdateAuditEntity(auditEntity);
			result.setStatus(Status.SUCCESS);
			result.setMessage("审核成功，审核状态为3");
			return result;
		}
		
		
		APIEntity apiEntity = apiService.findById(auditEntity.getApiId());
		try{
			JSONObject job = new JSONObject();
			//job.put("api_id", auditEntity.getApiId());
			job.put("api_id", apiEntity.getApiversion().getApiId()); // TODO 这里申请client_id需要输入版本的api_id
			
			// TODO 这里的值是写死的，需要后期修改
			//job.put("redirect_uri", URLEncoder.encode("/","UTF-8"));
			job.put("redirect_uri", "/");
			
			String params = job.toString();
			
			Map<String,String> headers = new HashMap<String,String>();
			headers.put(Constants.HEADER_KEY, Constants.HEADER_VALUE);
			headers.put(Constants.HEADER_CONTENT_TYPE, Constants.APPLICATION_JSON);
			// DOTO 目前是单集群，将来多集群，需要修改这里
			String str = HttpUtils.postJson(GatewayUtils.getAllGatewayColony().get(0).getColUrl()+Constants.NETWORK_CREATE_CLIENT, params, headers);
			
			logger.info("------------------调用网关获取client_id和secret的返回结果是："+str+"---------------");
			//解析参数
			org.json.JSONObject responseJson = new org.json.JSONObject(str);
			String clientId = responseJson.getString("client_id");
			String secret = responseJson.getString("secret");
			if(StringUtils.isBlank(clientId) || StringUtils.isBlank(secret)){
				throw new Exception();
			}
			
			//修改数据库
			auditEntity.setClientId(clientId);
			auditEntity.setSecret(secret);
			auditEntity.setCheckState(2);
			apiOauthDao.UpdateAuditEntity(auditEntity);
			result.setStatus(Status.SUCCESS);
			result.setMessage("修改成功，修改状态为2");
			return result;
		}catch(Exception e){
			result.setMessage("system error,check params if it's correct");
			return result;
		}
	}

	@Override
	public Result<Page<ApiAuditEntity>> getUseList(String userId, int currentPage, int pageSize) {
		
		Page<ApiAuditEntity> page = apiOauthDao.findAsPage(userId,currentPage,pageSize);
		
		Result<Page<ApiAuditEntity>> result = new Result<Page<ApiAuditEntity>>();
		if(page != null && page.getPageSize()>0){
			result.setData(page);
			result.setStatus(Status.SUCCESS);
			return result;
		}
		
		result.setMessage("获取数据错误");
		return result;
	}

	@Override
	public Result<Page<ApiAuditEntity>> getsupplyList(String userId, int currentPage, int pageSize) {
		
		Page<ApiAuditEntity> page = apiOauthDao.findAsPage1(userId,currentPage,pageSize);
		
		List<ApiAuditEntity> list = (List<ApiAuditEntity>)page.getItems();
		for (ApiAuditEntity apiAuditEntity : list) {
			String applyId = apiAuditEntity.getApplyId();
			ApplyEntity applyEntity = applyDao.findById(applyId);
			if(applyEntity != null){
				apiAuditEntity.setApplyName(applyEntity.getApplyName());
			}
		}
		Result<Page<ApiAuditEntity>> result = new Result<Page<ApiAuditEntity>>();
		if(page != null && page.getPageSize()>0){
			result.setData(page);
			result.setStatus(Status.SUCCESS);
			return result;
		}
		
		result.setMessage("获取数据错误");
		return result;
	}

	@Override
	public List<ApplyEntity> findByApplyId(String applyId,String apiId) {
		
		return apiOauthDao.findByApplyId(applyId,apiId);
	}

	@Override
	public List<ApiAuditEntity> getApiAuditEntity(List<String> apiIds) {
		return apiOauthDao.getApiAuditEntity(apiIds);
	}

	@Override
	public Result<String> deleteById(String auditId) {
		Result<String> result = new Result<String>();
		
		if(StringUtils.isBlank(auditId)){
			result.setMessage("auditId不能为空");
			return result;
		}
		
		ApiAuditEntity auditEntity = apiOauthDao.findById(auditId);
		
		if(null == auditEntity){
			result.setErrorMessage("当前申请记录查询不到,请检查id");
			return result;
		}
		
		ApplyEntity applyEntity = applyDao.findById(auditEntity.getApplyId());
		
		/***
		 * 删除将api与应用Apply建立关系
		 * @author makangwei
		 * @date 2017年9月1日 16:47:40
		 */
		List<String> authIds = applyEntity.getAuthIds();
		if(authIds == null){
			authIds = new ArrayList<>();
		}
		
		if(authIds.contains(auditEntity.getId())){
			logger.info(">>>> apiService delete apply api begin,applyId:" 
					+ auditEntity.getApplyId() + ";apiId:" + auditEntity.getApiId());
			
			authIds.remove(auditEntity.getId());
			applyEntity.setAuthIds(authIds);
			applyDao.saveOrUpdate(applyEntity);
			logger.info("<<< apiService update apply end");
		}
		
		//int i = apiOauthDao.deleteById(auditId);
		auditEntity.setCheckState(4); //删除并不是真的删除，而是将状态改为4
		apiOauthDao.UpdateAuditEntity(auditEntity);
		
		result.setStatus(Status.SUCCESS);
		result.setMessage("删除成功");
		return result;
		
	}

}
