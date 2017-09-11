package cn.ce.platform_service.oauth.dao.impl;

import java.util.List;
import java.util.Map;

import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import cn.ce.platform_service.apis.entity.ApiAuditEntity;
import cn.ce.platform_service.core.AbstractBaseMongoDao;
import cn.ce.platform_service.oauth.dao.IOauthDao;

/**
* @Description : 说明
* @Author : makangwei
* @Date : 2017年8月22日
*/
@Repository
public class OauthDaoImpl extends AbstractBaseMongoDao<ApiAuditEntity> implements IOauthDao{

	@Override
	public void init() {
		
	}

	public int findByFields(Map<String, Object> queryMap) {
		Query query = new Query();
		for (String key : queryMap.keySet()) {
			query.addCriteria(Criteria.where(key).is(queryMap.get(key)));
		}
		
		List<ApiAuditEntity> list = mongoTemplate.find(query, ApiAuditEntity.class);
		return list.size();
	}

}
