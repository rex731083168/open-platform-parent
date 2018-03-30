package cn.ce.platform_service.gateway.service;

import cn.ce.platform_service.sandbox.entity.SandboxRouterEntity;

/**
* @Description : 说明
* @Author : makangwei
* @Date : 2018年3月22日
*/
public interface ISaasService {

//	int save(SaasEntity saas);

	void clearAll();

	String getSaas(String saasId, String resourceType, String method);
	
	SandboxRouterEntity getBoxSaas(String saasId, String resourceType, String boxId, String method);
	
	String saveSaas(String saasId, String resourceType, String targetUrl, String method);
	
	String saveBoxSaas(String saasId, String resourceType, String targetUrl, String boxId, String method);

	String deleteRoute(String saasId, String resourceType, String method);
	
	String deleteBoxRoute(String saasId, String resourceType, String boxId, String method);


}

