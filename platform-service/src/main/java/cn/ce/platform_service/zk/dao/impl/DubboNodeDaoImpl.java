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
//import cn.ce.platform_service.zk.dao.IDubboNodeDao;
//import cn.ce.platform_service.zk.entity.DubboNode;
//
///**
//* @Description : 说明
//* @Author : makangwei
//* @Date : 2018年1月10日
//*/
//@Repository("dubboNodeDao")
//public class DubboNodeDaoImpl extends BaseMongoDaoImpl<DubboNode> implements IDubboNodeDao{
//
//	@Override
//	public boolean clearAll() {
//		Query query = new Query();
//		super.remove(query);
//		return true;
//	}
//
//	@Override
//	public Page<DubboNode> findPage(int currentPage, int pageSize, String rootId) {
//		Criteria c = new Criteria();
//		if(StringUtils.isNotBlank(rootId)){
//			c.and(DBFieldsConstants.DUBBO_ROOT_ID).is(rootId);
//		}
//		return super.findPage(new Page<DubboNode>(currentPage,0,pageSize), new Query(c));
//	}
//
//}
