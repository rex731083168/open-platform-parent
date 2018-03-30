package cn.ce.platform_service.gateway.service;

import java.util.List;

import cn.ce.platform_service.gateway.entity.SaasEntity;
import cn.ce.platform_service.sandbox.entity.SandBox;

/**
* @Description : 说明
* @Author : makangwei
* @Date : 2018年3月22日
*/
public interface ISaasService {

//	int save(SaasEntity saas);

	void clearAll();

	String getSaas(String saasId, String resourceType, String method);
	
	String saveSaas(String saasId, String resourceType, String targetUrl, String method);

	String deleteRoute(String saasId, String resourceType, String method);
	
	
	
	SaasEntity getBoxSaas(String saasId, String resourceType, String boxId, String method);
	
	int saveBoxSaas(String saasId, String resourceType, String targetUrl, String boxId, String method);
	
	int deleteBoxRoute(String saasId, String resourceType, String boxId, String method);
	
	List<SandBox> getSendBoxSaasList(SaasEntity saas);


}

