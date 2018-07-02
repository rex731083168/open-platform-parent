//package cn.ce.platform_service.gateway.dao.impl;
//
//import java.util.List;
//
//import org.springframework.stereotype.Repository;
//
//import cn.ce.platform_service.core.mongo.BaseMongoDaoImpl;
//import cn.ce.platform_service.gateway.dao.IGatewayColonyManageDao;
//import cn.ce.platform_service.gateway.entity.GatewayColonyEntity;
//
//@Repository(value="gatewayColonyManageDao")
//public class GatewayColonyManageDao extends BaseMongoDaoImpl<GatewayColonyEntity> implements IGatewayColonyManageDao{
//
//	@Override
//	public GatewayColonyEntity getColonySingle() {
//		List<GatewayColonyEntity> findAll = super.findAll();
//		GatewayColonyEntity gce = new GatewayColonyEntity();
//		if(null != findAll && findAll.size() > 0){
//			gce = findAll.get(0);
//		}
//		return gce;
//	}
//	
//	@Override
//	public GatewayColonyEntity findById(String id){
//		return super.findById(id);
//	}
//
//}
