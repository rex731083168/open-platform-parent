//package cn.ce.platform_service.zk.dao.impl;
//
//import org.springframework.data.mongodb.core.query.Query;
//import org.springframework.stereotype.Repository;
//
//import cn.ce.platform_service.common.page.Page;
//import cn.ce.platform_service.core.mongo.BaseMongoDaoImpl;
//import cn.ce.platform_service.zk.dao.IDubboRootDao;
//import cn.ce.platform_service.zk.entity.DubboRoot;
//
///**
// * @Description : 说明
// * @Author : makangwei
// * @Date : 2018年1月11日
// */
//@Repository("dubboRootDao")
//public class DubboRootDaoImpl extends BaseMongoDaoImpl<DubboRoot> implements IDubboRootDao {
//
//	@Override
//	public Page<DubboRoot> findPage(Integer currentPage, Integer pageSize) {
//
//		Query query = new Query();
//		return super.findPage(new Page<DubboRoot>(currentPage, 0, pageSize), query);
//	}
//
//	@Override
//	public boolean clearAll() {
//		Query query = new Query();
//		super.remove(query);
//		return true;
//	}
//
//}
