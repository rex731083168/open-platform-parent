package cn.ce.platform_service.openApply.dao.impl;

import java.util.List;

import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import cn.ce.platform_service.core.mongo.BaseMongoDaoImpl;
import cn.ce.platform_service.openApply.dao.INewOpenApplyDao;
import cn.ce.platform_service.openApply.entity.OpenApplyEntity;

/**
* @Description : 说明
* @Author : makangwei
* @Date : 2017年10月11日
*/

@Repository(value = "newOpenApplyDao")
public class NewOpenApplyDaoImpl extends  BaseMongoDaoImpl<OpenApplyEntity> implements INewOpenApplyDao{

	@Override
	public OpenApplyEntity save(OpenApplyEntity openApplyEntity) {
		return super.save(openApplyEntity);
	}

	@Override
	public OpenApplyEntity findOpenApplyById(String id) {
		return super.findById(id);
	}

	@Override
	public List<OpenApplyEntity> findOpenApplyByNameOrKey(String applyName, String applyKey) {
		
		Criteria c = new Criteria();
		Criteria c1 = Criteria.where("applyName").is(applyName);
		Criteria c2 = Criteria.where("applyKey").is(applyKey);
		c.orOperator(c1,c2);
		Query query = new Query(c);
		return super.find(query);
	}


}
