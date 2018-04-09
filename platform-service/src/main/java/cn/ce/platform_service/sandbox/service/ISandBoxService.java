package cn.ce.platform_service.sandbox.service;

import cn.ce.platform_service.common.Result;
import cn.ce.platform_service.gateway.entity.QuerySaasEntity;
import cn.ce.platform_service.sandbox.entity.QuerySandBox;
import cn.ce.platform_service.sandbox.entity.SandBox;

/**
* @Description : 说明
* @Author : makangwei
* @Date : 2018年3月23日
*/
public interface ISandBoxService {

	Result<?> getResourcePool();
	
	Result<?> andBox(SandBox sandBox);

	Result<?> boxList(QuerySandBox queryBox);

	Result<?> getOne(String boxId);

	Result<?> deleteOne(String boxId);

//	Result<?> routeList(QuerySaasEntity saas);
//
//	Result<?> getRoute(String saasId, String resourceType, String boxId);
//
//	Result<?> andRoute(String saasId, String resourceType, String targetUrl , String boxId);
//
//	Result<?> updateRoute(String saasId, String resourceType, String targetUrl , String boxId);
//
//	Result<?> deleteRoute(String saasId, String resourceType, String boxId);
}

