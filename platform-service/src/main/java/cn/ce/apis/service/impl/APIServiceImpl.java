package cn.ce.apis.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import com.mongodb.WriteResult;

import cn.ce.apis.dao.IApiDAO;
import cn.ce.apis.entity.APIEntity;
import cn.ce.apis.entity.ApiVersion;
import cn.ce.apis.service.IAPIService;
import cn.ce.common.Result;
import cn.ce.common.Status;
import cn.ce.oauth.dao.IOauthDao;
import cn.ce.page.Page;

/**
 * 
 * @ClassName: APIServiceImpl
 * @Description: 接口服务实现类型
 * @author dingjia@300.cn
 *
 */
@Service(value = "apiService")
public class APIServiceImpl implements IAPIService {
	
	@Autowired
	private MongoTemplate mongoTemplate;
	
	@Autowired
	private IApiDAO  apiDao;
	@Autowired
	private IOauthDao oauthDao;

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
			result.setMessage("当前api已被用户使用，暂时无法删除");
			return result;
		}else{
			apiDao.delApi(id);
			result.setStatus(Status.SUCCESS);
			result.setMessage("删除成功");
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

	@Override
	public List<APIEntity> getGroupAPIs(String groupId, int checkState) {
		return null;
	}

	@Override
	public Page<APIEntity> getGroupAPIsAsPage(String groupId, int checkState,
			int state, int currentPage, int pageSize) {
		return apiDao.getAPIsAsPage(groupId);
	}

	@Override
	public int reviewGroupAPIs(String groupId, int checkState, String checkmem) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Page<APIEntity> searchApis(String nameDesc, int currentPage,
			int pageSize) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<APIEntity> getCategoryAPIs(String categoryCode, int checkState) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Page<APIEntity> findApisByEntity(APIEntity entity, int currentPage, int pageSize) {
		return apiDao.findApisByEntity(entity, currentPage, pageSize);
	}
	
	@Override
	public ApiVersion findApiVersionByEntity(ApiVersion apiVersion) {
		ApiVersion aVersion = new ApiVersion();
		// TODO Auto-generated method stub
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
		Query query = new Query();
        query.addCriteria(Criteria.where("apiversion.apiId").is(apiid));
        WriteResult rt = mongoTemplate.updateMulti(query, Update.update("apiversion.newVersion", false), APIEntity.class);
        return rt.getN();
	}

	@Override
	public List<APIEntity> findByField(String key, String value) {
		
		return apiDao.findByField(key,value);
	}

	@Override
	public Result<String> checkVersion(String apiId, String version) {
		
		Result<String> result = new Result<String>();
		Criteria c = new Criteria();
		c.and("apiversion.apiId").is(apiId);
		c.and("apiversion.version").is(version);
		Query query = new Query(c);
		boolean bool = mongoTemplate.exists(query, APIEntity.class);
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

}
