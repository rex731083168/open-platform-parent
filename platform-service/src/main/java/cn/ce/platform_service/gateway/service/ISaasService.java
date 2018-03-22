package cn.ce.platform_service.gateway.service;

import cn.ce.platform_service.gateway.entity.SaasEntity;

/**
* @Description : 说明
* @Author : makangwei
* @Date : 2018年3月22日
*/
public interface ISaasService {

	int save(SaasEntity saas);

	void clearAll();

	String getSaas(String saasId, String resourceType);


}

