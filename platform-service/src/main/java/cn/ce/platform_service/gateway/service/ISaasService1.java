package cn.ce.platform_service.gateway.service;

import java.util.List;

import cn.ce.platform_service.common.Result;
import cn.ce.platform_service.common.page.Page;
import cn.ce.platform_service.gateway.entity.QuerySaasEntity;
import cn.ce.platform_service.gateway.entity.SaasEntity;
import cn.ce.platform_service.sandbox.entity.SandBox;

/**
* @Description : 开放平台内部使用的saas service
* @Author : makangwei
* @Date : 2018年3月31日
*/
public interface ISaasService1 {

	Result<?> saveOrUpdateBoxSaas(SaasEntity saasEntity);

	Result<?> deleteBoxSaas(String routeId);

	Result<?> getBoxSaas(String routeId);

	Result<?> boxSaasList(QuerySaasEntity saas);

	Result<?> migraSaas(SaasEntity saas);

//	SaasEntity getBoxSaas(String saasId, String resourceType, String boxId, String method);
//	
//	int saveBoxSaas(String saasId, String resourceType, String targetUrl, String boxId, String method);
//	
//	int deleteBoxRoute(String saasId, String resourceType, String boxId, String method);
//	
//	List<SandBox> getSendBoxSaasList(SaasEntity saas);
//
//	Page<SaasEntity> routeList(QuerySaasEntity saas);
	
}

