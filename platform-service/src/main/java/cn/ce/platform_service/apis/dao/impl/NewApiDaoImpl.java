package cn.ce.platform_service.apis.dao.impl;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import com.mongodb.WriteResult;

import cn.ce.platform_service.apis.dao.INewApiDao;
import cn.ce.platform_service.apis.entity.ApiEntity;
import cn.ce.platform_service.common.DBFieldsConstants;
import cn.ce.platform_service.common.page.Page;
import cn.ce.platform_service.core.mongo.BaseMongoDaoImpl;

/**
* @Description : 说明
* @Author : makangwei
* @Date : 2017年10月12日
*/
@Repository(value="newApiDao")
public class NewApiDaoImpl extends BaseMongoDaoImpl<ApiEntity> implements INewApiDao{

	@Override
	public ApiEntity save(ApiEntity entity) {
		
		return super.save(entity);
	}

	//修改旧的版本的newVersion字段为false
	@Override 
	public int updApiVersionByApiId(String apiId,boolean flag) {
		Query query = new Query().addCriteria(Criteria.where(DBFieldsConstants.APIS_APIVERSION_VERSIONID).is(apiId));
		Update update = Update.update(DBFieldsConstants.APIS_APIVERSION_NEWVERSION, flag);
        WriteResult wr = super.update(query, update);
        return wr.getN();
	}

	@Override
	public ApiEntity findApiById(String apiId) {
		
		return super.findById(apiId);
	}

	@Override
	public Page<ApiEntity> findSupplierApis(String openApplyId, String userId, String apiNameLike, Integer checkState,
			int currentPage, int pageSize) {
		
		Criteria c = new Criteria();
		c.and(DBFieldsConstants.APIS_OPENAPPLY_ID).is(openApplyId);
		if(!StringUtils.isBlank(userId)){
			c.and(DBFieldsConstants.APIS_USERID);
		}
		if(!StringUtils.isBlank(apiNameLike)){
			c.and(DBFieldsConstants.APIS_APICHNAME).regex(apiNameLike);
		}
		if(checkState != null){
			c.and(DBFieldsConstants.APIS_CHECKSTATE).is(checkState);
		}
		Query query = new Query(c).with(new Sort(Direction.DESC, DBFieldsConstants.APIS_CREATE_TIME));
		
		Page<ApiEntity> page = new Page<ApiEntity>(currentPage,0,pageSize);
		
		return super.findPage(page, query);
	}

	@Override
	public ApiEntity findOneByFields(Map<String, Object> map) {
		Criteria c = new Criteria();
		for (String key : map.keySet()) {
			c.and(key).is(map.get(key));
		}
		Query query = new Query(c);
		return super.findOne(query);
	}

	@Override
	public List<ApiEntity> findApiByIds(List<String> apiId) {
		Query query = new Query().addCriteria(Criteria.where(DBFieldsConstants.APIS_ID).in(apiId));
		return super.find(query);
	}

	@Override
	public List<ApiEntity> findByField(String key, String value) {
		Query query = new Query().addCriteria(Criteria.where(key).is(value));
		return super.find(query);
	}

}
