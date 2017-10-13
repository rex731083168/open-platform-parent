package cn.ce.platform_service.apis.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import cn.ce.platform_service.apis.dao.IApiDAO;
import cn.ce.platform_service.apis.entity.ApiEntity;
import cn.ce.platform_service.apis.entity.ApiVersion;
import cn.ce.platform_service.apis.service.IAPIService;
import cn.ce.platform_service.common.Result;
import cn.ce.platform_service.common.page.Page;
import cn.ce.platform_service.oauth.dao.IOauthDao;
import cn.ce.platform_service.openApply.service.IManageOpenApplyService;

/**
 * 
 * @ClassName: APIServiceImpl
 * @Description: 接口服务实现类型
 * @author dingjia@300.cn
 *
 */
@Service(value = "apiService")
public class APIServiceImpl implements IAPIService {
	
	@Resource
	private IManageOpenApplyService manageOpenApplyService;
	
	@Resource
	private IApiDAO  apiDao;
	@Resource
	private IOauthDao oauthDao;
	
	private static final Logger _LOGGER = LoggerFactory.getLogger(APIServiceImpl.class);
	
	@Override
	public void addAPI(ApiEntity api) {
		apiDao.addAPI(api);
	}

	@Override
	public boolean haveAPIs(String groupId) {
		return false;
	}

	@Override
	public Result<String> delById(String id) {
		//判断如果当前api已经被申请使用则不允许删除，否则允许删除
		Result<String> result = new Result<String>();
		Map<String,Object> queryMap = new HashMap<String,Object>();
		queryMap.put("api_id", id);
		queryMap.put("check_state", 2);
		
		int num = oauthDao.findByFields(queryMap);
		if(num > 0 ){
			result.setErrorMessage("当前api已被用户使用，暂时无法删除");
			return result;
		}else{
			apiDao.delApi(id);
			result.setSuccessMessage("删除成功");
			return result;
		}
	}

	@Override
	public ApiEntity findById(String id) {
		return apiDao.findOneById(id);
	}

	@Override
	public void updateAPI(ApiEntity api) {
		apiDao.updateAPI(api);
	}

	@Override
	public List<ApiEntity> getAPIs(String groupId) {
		return apiDao.getAPIs(groupId);
	}

	@Override
	public Page<ApiEntity> getAPIsAsPage(String groupId, int currentPage,
			int pageSize) {
		return apiDao.getAPIsAsPage(groupId);
	}

//	@Override
//	public List<APIEntity> getGroupAPIs(String groupId, int checkState) {
//		return null;
//	}

	@Override
	public Page<ApiEntity> getGroupAPIsAsPage(String groupId, int checkState,
			int state, int currentPage, int pageSize) {
		return apiDao.getAPIsAsPage(groupId);
	}

//	@Override
//	public int reviewGroupAPIs(String groupId, int checkState, String checkmem) {
//		// TODO Auto-generated method stub
//		return 0;
//	}

//	@Override
//	public Page<APIEntity> searchApis(String nameDesc, int currentPage,
//			int pageSize) {
//		// TODO Auto-generated method stub
//		return null;
//	}

//	@Override
//	public List<APIEntity> getCategoryAPIs(String categoryCode, int checkState) {
//		// TODO Auto-generated method stub
//		return null;
//	}

	@Override
	public Page<ApiEntity> findApisByEntity(ApiEntity entity, int currentPage, int pageSize) {
		return apiDao.findApisByEntity(entity, currentPage, pageSize);
	}
	
	@Override
	public ApiVersion findApiVersionByEntity(ApiVersion apiVersion) {
		ApiVersion aVersion = new ApiVersion();
		//构建查询对象
		Criteria c = new Criteria();
		
		if(StringUtils.isNotBlank(apiVersion.getVersionId())){
			c.and("apiversion.apiId").is(apiVersion.getVersionId());
		}
		
		if(null != apiVersion.getVersion()){
			c.and("apiversion.version").is(apiVersion.getVersion());
		}
		
		//构建排序对象
		Query query = new Query(c).with(new Sort(Direction.DESC, "apiversion.version"));
		
		List<ApiEntity> findApiListByQuery = apiDao.findApiListByQuery(query);
		if(!findApiListByQuery.isEmpty()){
			aVersion = findApiListByQuery.get(0).getApiVersion();
		}
		return aVersion;
	}

	@Override
	public int updApiVersionByApiid(String apiid) {
		return apiDao.updApiVersionByApiid(apiid);
	}

	@Override
	public List<ApiEntity> findByField(String key, String value) {
		
		return apiDao.findByField(key,value);
	}

//	@Override
//	public Result<String> checkVersion(String apiId, String version) {
//		
//		Result<String> result = new Result<String>();
//		boolean bool = apiDao.exitsVersion(apiId,version);
//
//		if(bool){
//			result.setMessage("当前version已经存在");
//			return result;
//		}else{
//			result.setMessage("当前version不存在，可以使用");
//			result.setStatus(Status.SUCCESS);
//			return result;
//		}
//	}

//	@Override
//	public Result<String> checkApiEnName(String apiEnName,String appId) {
//		
//		Result<String> result = new Result<String>();
//		if(StringUtils.isBlank(apiEnName)){
//			result.setMessage("apiEnName不能为空");
//			return result;
//		}
//		Map<String,Object> map =new HashMap<String,Object>();
//		map.put("appid", appId);
//		map.put("apienname", apiEnName);
//		ApiEntity entity = apiDao.findOneByFields(map);
//		
//		if(entity == null){
//			result.setStatus(Status.SUCCESS);
//			result.setMessage("当前名称可用");
//			return result;
//		}else{
//			result.setMessage("当前名称已经被占用");
//			return result;
//		}
//	}

	@Override
	public List<ApiEntity> getApiListByIds(List<String> ids) {
		return apiDao.findApiListByIds(ids);
	}

//	@Override
//	public Result<String> modifyApi(ApiEntity apientity) {
//		
//		Result<String> result = new Result<String>();
//		boolean bool = apiDao.modifyApi(apientity);
//		if(bool){
//			result.setSuccessMessage("修改成功");
//		}else{
//			result.setErrorMessage("修改失败");
//		}
//		return result;
//	}

//	@Override
//	public Result<String> checkApiChName(String apiChName, String appId) {
//		
//		Result<String> result = new Result<String>();
//		if(StringUtils.isBlank(apiChName)){
//			result.setMessage("apiEnName不能为空");
//			return result;
//		}
//		Map<String,Object> map =new HashMap<String,Object>();
//		map.put("appid", appId);
//		map.put("apichname", apiChName);
//		ApiEntity entity = apiDao.findOneByFields(map);
//		
//		if(entity == null){
//			result.setStatus(Status.SUCCESS);
//			result.setMessage("当前名称可用");
//			return result;
//		}else{
//			result.setMessage("当前名称已经被占用");
//			return result;
//		}
//	}

//	@Override
//	public Result<String> apiVerify(String apiId, String username, String password) {
//		
//		Result<String> result = new Result<String>();
//		try {
//			ApiEntity api = findById(apiId);
//			api.setCheckState(1);
//			updateAPI(api);
//			result.setSuccessMessage("修改成功");
//		} catch (Exception e) {
//			_LOGGER.error("_____________>error happens when excute apiVerify service :", e);
//			result.setErrorMessage("修改失败");
//		}
//		return result;
//	}

//	@Override
//	public Result<String> publishAPI(User user, APIEntity apientity) {
//		
//		Result<String> result = new Result<String>();
//		//如果apiId存在，则修改api
//		if(apientity.getId() != null ){
//			
//			updateAPI(apientity);
//			result.setSuccessMessage("修改成功");
//			return result;
//		}
//		
//		// 第一次添加接口,并且选择未开启版本控制
//		if (apientity.getApiVersion() == null || StringUtils.isBlank(apientity.getApiVersion().getVersion())) {
////			apientity.setId(UUID.randomUUID().toString().replace("-", ""));
//			apientity.setUserId(user.getId());
//			apientity.setUserName(user.getUserName());
//			apientity.setCreateTime(new Date());
//
//			ApiVersion version = new ApiVersion();
//			version.setApiId(apientity.getId());
//			apientity.setApiVersion(version);
//
//			// 过滤apienname不能以/开头和结尾
//			apientity.setApiEnName(apientity.getApiEnName().replaceAll("/", ""));
//
//			addAPI(apientity);
//
//		} else {
//			// 开启版本控制
//			
////			apientity.setId(UUID.randomUUID().toString().replace("-", ""));
//			apientity.setUserId(user.getId());
//			apientity.setUserName(user.getUserName());
//			apientity.setCreateTime(new Date());
//
//			
//			int num = updApiVersionByApiid(apientity.getApiVersion().getApiId());
//			_LOGGER.info("----->将原来其他版本的api的newVersion字段全部修改为false，一共修改了"+num+"条数据");
//			
//			// TODO 前端传入版本号和newVersion字段吗？
//			apientity.getApiVersion().setNewVersion(true);
//
//			//如果没有传入版本的apiId则重新生成apiId
//			if(StringUtils.isBlank(apientity.getApiVersion().getApiId())){
//				apientity.getApiVersion().setApiId(apientity.getId());
//			}
//			
//			addAPI(apientity);
//			
//			_LOGGER.info("------新添加的数据为："+new org.json.JSONObject(apientity).toString());
//		}
//		result.setSuccessData("添加成功");
//		return result;
//	}

//	@Override
//	public Result<?> show(String apiId) {
//		
//		Result<JSONObject> result = new Result<JSONObject>();
//		try {
//			ApiEntity api = findById(apiId);
//
//			if (api == null) {
//				result.setErrorMessage("当前id不存在");
//				return result;
//			}
//
//			String appId = api.getAppId();
//			OpenApplyEntity app = appService.findById(appId);
//
//			JSONObject jsonObject = JSONObject.parseObject(JSON.toJSONString(api));
//
//			// 添加网关访问地址
//			OpenApplyEntity appEntity = appService.findById(api.getAppId());
//			
//			List<GatewayColonyEntity> colList = GatewayUtils.getAllGatewayColony();
//			
//			List<String> gatewayUrlList = new ArrayList<String>();
//
//			for (GatewayColonyEntity gatewayColonyEntity : colList) {
//				
//				gatewayUrlList.add(gatewayColonyEntity.getColUrl() +"/"+appEntity.getApplyKey()+"/" + api.getApiEnName()+"/"+api.getApiVersion().getVersion()+"/");
//			}
//			
//			jsonObject.put("gatewayUrls", gatewayUrlList);
//			jsonObject.put("appName", app.getApplyName());
//			result.setSuccessData(jsonObject);
//		} catch (Exception e) {
//			e.printStackTrace();
//			result.setErrorMessage("");
//		}
//		return result;
//	}


	@Override
	public Result<Page<ApiEntity>> apiList(String apiId, String apiChName, String checkState, int currentPage, int pageSize) {
		
		Result<Page<ApiEntity>> result = new Result<Page<ApiEntity>>();
		try {

			ApiEntity apiParam = new ApiEntity();
			if (org.springframework.util.StringUtils.hasText(apiId)) {
				apiParam.setOpenApplyId(apiId);
			}
			if (org.springframework.util.StringUtils.hasText(apiChName)) {
				apiParam.setApiChName(apiChName);
			}
			if (org.springframework.util.StringUtils.hasText(checkState)) {
				int state = Integer.parseInt(checkState);
				apiParam.setCheckState(state);
			}

			Page<ApiEntity> ds = findApisByEntity(apiParam, currentPage, pageSize);
			@SuppressWarnings("unchecked")
			List<ApiEntity> items = (List<ApiEntity>) ds.getItems();
			List<String> apiIdList = new ArrayList<>(items.size());
			// 构建apiId集合
			for (ApiEntity apiEntity : items) {
				apiEntity.setOpenApplyEntity( manageOpenApplyService.findById(apiEntity.getOpenApplyId()));
				apiIdList.add(apiEntity.getId());
			}

			 result.setSuccessData(ds);
			 return result;
			 
		} catch (Exception e) {
			_LOGGER.info("error happens when execute apiList",e);
			result.setErrorMessage("");
			return result;
		}
	}
}
