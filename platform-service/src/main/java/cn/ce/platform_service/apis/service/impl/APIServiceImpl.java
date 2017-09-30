package cn.ce.platform_service.apis.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import cn.ce.platform_service.apis.dao.IApiDAO;
import cn.ce.platform_service.apis.entity.APIEntity;
import cn.ce.platform_service.apis.entity.ApiVersion;
import cn.ce.platform_service.apis.service.IAPIService;
import cn.ce.platform_service.app.entity.AppEntity;
import cn.ce.platform_service.app.service.IAppService;
import cn.ce.platform_service.common.Result;
import cn.ce.platform_service.common.Status;
import cn.ce.platform_service.common.gateway.GatewayUtils;
import cn.ce.platform_service.gateway.entity.GatewayColonyEntity;
import cn.ce.platform_service.oauth.dao.IOauthDao;
import cn.ce.platform_service.page.Page;
import cn.ce.platform_service.users.entity.User;

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
	private IAppService appService;
	@Resource
	private IApiDAO  apiDao;
	@Resource
	private IOauthDao oauthDao;
	
	private static final Logger _LOGGER = LoggerFactory.getLogger(APIServiceImpl.class);
	
	@Override
	public void addAPI(APIEntity api) {
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
	public APIEntity findById(String id) {
		return apiDao.findOneById(id);
	}

	@Override
	public void updateAPI(APIEntity api) {
		apiDao.updateAPI(api);
	}

	@Override
	public List<APIEntity> getAPIs(String groupId) {
		return apiDao.getAPIs(groupId);
	}

	@Override
	public Page<APIEntity> getAPIsAsPage(String groupId, int currentPage,
			int pageSize) {
		return apiDao.getAPIsAsPage(groupId);
	}

//	@Override
//	public List<APIEntity> getGroupAPIs(String groupId, int checkState) {
//		return null;
//	}

	@Override
	public Page<APIEntity> getGroupAPIsAsPage(String groupId, int checkState,
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
	public Page<APIEntity> findApisByEntity(APIEntity entity, int currentPage, int pageSize) {
		return apiDao.findApisByEntity(entity, currentPage, pageSize);
	}
	
	@Override
	public ApiVersion findApiVersionByEntity(ApiVersion apiVersion) {
		ApiVersion aVersion = new ApiVersion();
		//构建查询对象
		Criteria c = new Criteria();
		
		if(StringUtils.isNotBlank(apiVersion.getApiId())){
			c.and("apiversion.apiId").is(apiVersion.getApiId());
		}
		
		if(null != apiVersion.getVersion()){
			c.and("apiversion.version").is(apiVersion.getVersion());
		}
		
		//构建排序对象
		Query query = new Query(c).with(new Sort(Direction.DESC, "apiversion.version"));
		
		List<APIEntity> findApiListByQuery = apiDao.findApiListByQuery(query);
		if(!findApiListByQuery.isEmpty()){
			aVersion = findApiListByQuery.get(0).getApiversion();
		}
		return aVersion;
	}

	@Override
	public int updApiVersionByApiid(String apiid) {
		return apiDao.updApiVersionByApiid(apiid);
	}

	@Override
	public List<APIEntity> findByField(String key, String value) {
		
		return apiDao.findByField(key,value);
	}

	@Override
	public Result<String> checkVersion(String apiId, String version) {
		
		Result<String> result = new Result<String>();
		boolean bool = apiDao.exitsVersion(apiId,version);

		if(bool){
			result.setMessage("当前version已经存在");
			return result;
		}else{
			result.setMessage("当前version不存在，可以使用");
			result.setStatus(Status.SUCCESS);
			return result;
		}
	}

	@Override
	public Result<String> checkApiEnName(String apiEnName,String appId) {
		
		Result<String> result = new Result<String>();
		if(StringUtils.isBlank(apiEnName)){
			result.setMessage("apiEnName不能为空");
			return result;
		}
		Map<String,Object> map =new HashMap<String,Object>();
		map.put("appid", appId);
		map.put("apienname", apiEnName);
		APIEntity entity = apiDao.findOneByFields(map);
		
		if(entity == null){
			result.setStatus(Status.SUCCESS);
			result.setMessage("当前名称可用");
			return result;
		}else{
			result.setMessage("当前名称已经被占用");
			return result;
		}
	}

	@Override
	public List<APIEntity> getApiListByIds(List<String> ids) {
		return apiDao.findApiListByIds(ids);
	}

	@Override
	public Result<String> modifyApi(APIEntity apientity) {
		
		Result<String> result = new Result<String>();
		boolean bool = apiDao.modifyApi(apientity);
		if(bool){
			result.setSuccessMessage("修改成功");
		}else{
			result.setErrorMessage("修改失败");
		}
		return result;
	}

	@Override
	public Result<String> checkApiChName(String apiChName, String appId) {
		
		Result<String> result = new Result<String>();
		if(StringUtils.isBlank(apiChName)){
			result.setMessage("apiEnName不能为空");
			return result;
		}
		Map<String,Object> map =new HashMap<String,Object>();
		map.put("appid", appId);
		map.put("apichname", apiChName);
		APIEntity entity = apiDao.findOneByFields(map);
		
		if(entity == null){
			result.setStatus(Status.SUCCESS);
			result.setMessage("当前名称可用");
			return result;
		}else{
			result.setMessage("当前名称已经被占用");
			return result;
		}
	}

	@Override
	public Result<String> apiVerify(String apiid, String username, String password) {
		
		Result<String> result = new Result<String>();
		try {
			APIEntity api = findById(apiid);
			api.setCheckState(1);
			updateAPI(api);
			result.setSuccessMessage("修改成功");;
		} catch (Exception e) {
			_LOGGER.error("_____________>error happens when excute apiVerify service :", e);
			result.setErrorMessage("修改失败");
		}
		return result;
	}

	@Override
	public Result<String> publishAPI(User user, APIEntity apientity) {
		
		Result<String> result = new Result<String>();
		//如果apiId存在，则修改api
		if(apientity.getId() != null ){
			
			updateAPI(apientity);
			result.setSuccessMessage("修改成功");
			return result;
		}
		
		// 第一次添加接口,并且选择未开启版本控制
		if (apientity.getApiversion() == null || StringUtils.isBlank(apientity.getApiversion().getVersion())) {
			apientity.setId(UUID.randomUUID().toString().replace("-", ""));
			apientity.setUserid(user.getId());
			apientity.setUsername(user.getUsername());
			apientity.setCreatetime(new Date());

			ApiVersion version = new ApiVersion();
			version.setApiId(apientity.getId());
			apientity.setApiversion(version);

			// 过滤apienname不能以/开头和结尾
			apientity.setApienname(apientity.getApienname().replaceAll("/", ""));

			addAPI(apientity);

		} else {
			// 开启版本控制
			
			apientity.setId(UUID.randomUUID().toString().replace("-", ""));
			apientity.setUserid(user.getId());
			apientity.setUsername(user.getUsername());
			apientity.setCreatetime(new Date());

			
			int num = updApiVersionByApiid(apientity.getApiversion().getApiId());
			_LOGGER.info("----->将原来其他版本的api的newVersion字段全部修改为false，一共修改了"+num+"条数据");
			
			// TODO 前端传入版本号和newVersion字段吗？
			apientity.getApiversion().setNewVersion(true);

			//如果没有传入版本的apiId则重新生成apiId
			if(StringUtils.isBlank(apientity.getApiversion().getApiId())){
				apientity.getApiversion().setApiId(apientity.getId());
			}
			
			addAPI(apientity);
			
			_LOGGER.info("------新添加的数据为："+new org.json.JSONObject(apientity).toString());
		}
		result.setSuccessData("添加成功");
		return result;
	}

	@Override
	public Result<String> show(String apiId) {
		
		Result<String> result = new Result<String>();
		try {
			APIEntity api = findById(apiId);

			if (api == null) {
				result.setErrorMessage("当前id不存在");
				return result;
			}

			String appId = api.getAppid();
			AppEntity app = appService.findById(appId);

			JSONObject jsonObject = JSONObject.parseObject(JSON.toJSONString(api));

			// 添加网关访问地址
			AppEntity appEntity = appService.findById(api.getAppid());
			
			List<GatewayColonyEntity> colList = GatewayUtils.getAllGatewayColony();
			List<String> gatewayUrlList = new ArrayList<String>();

			for (GatewayColonyEntity gatewayColonyEntity : colList) {
				
				gatewayUrlList.add(gatewayColonyEntity.getColUrl() +"/"+appEntity.getAppkey()+"/" + api.getApienname()+"/"+api.getApiversion().getVersion()+"/");
			}
			jsonObject.put("gatewayUrls", gatewayUrlList);

			jsonObject.put("appname", app.getAppname());
			result.setSuccessMessage("");
		} catch (Exception e) {
			e.printStackTrace();
			result.setErrorMessage("ERROR");;
		}
		return result;
	}


	@Override
	public Result<Page<APIEntity>> apiList(String apiId, String apiChName, String checkState, int currentPage, int pageSize) {
		
		Result<Page<APIEntity>> result = new Result<Page<APIEntity>>();
		try {

			APIEntity apiParam = new APIEntity();
			if (org.springframework.util.StringUtils.hasText(apiId)) {
				apiParam.setAppid(apiId);
			}
			if (org.springframework.util.StringUtils.hasText(apiChName)) {
				apiParam.setApichname(apiChName);
			}
			if (org.springframework.util.StringUtils.hasText(checkState)) {
				int state = Integer.parseInt(checkState);
				apiParam.setCheckState(state);
			}

			Page<APIEntity> ds = findApisByEntity(apiParam, currentPage, pageSize);
			List<APIEntity> items = (List<APIEntity>) ds.getItems();
			List<String> apiIdList = new ArrayList<>(items.size());
			// 构建apiId集合
			for (APIEntity apiEntity : items) {
				apiEntity.setApp(appService.findById(apiEntity.getAppid()));
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
