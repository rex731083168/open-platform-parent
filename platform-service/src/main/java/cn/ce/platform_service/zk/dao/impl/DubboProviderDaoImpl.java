//package cn.ce.platform_service.zk.dao.impl;
//
//import org.apache.commons.lang3.StringUtils;
//import org.springframework.data.mongodb.core.query.Criteria;
//import org.springframework.data.mongodb.core.query.Query;
//import org.springframework.stereotype.Repository;
//
//import cn.ce.platform_service.common.DBFieldsConstants;
//import cn.ce.platform_service.common.page.Page;
//import cn.ce.platform_service.core.mongo.BaseMongoDaoImpl;
//import cn.ce.platform_service.zk.dao.IDubboProviderDao;
//import cn.ce.platform_service.zk.entity.DubboProvider;
//
///**
//* @Description : 说明
//* @Author : makangwei
//* @Date : 2018年1月10日
//*/
//@Repository("dubboProviderDao")
//public class DubboProviderDaoImpl extends BaseMongoDaoImpl<DubboProvider> implements IDubboProviderDao{
//
//	@Override
//	public boolean clearAll() {
//		Query query = new Query();
//		super.remove(query);
//		return true;
//	}
//
//	@Override
//	public Page<DubboProvider> findPage(int currentPage, int pageSize, String nodeId) {
//		Criteria c = new Criteria();
//		if(StringUtils.isNotBlank(nodeId)){
//			c.and(DBFieldsConstants.DUBBO_NODE_ID).is(nodeId);
//		}
//		return super.findPage(new Page<DubboProvider>(currentPage, 0, pageSize), new Query(c));
//	}
//	
//
//}
