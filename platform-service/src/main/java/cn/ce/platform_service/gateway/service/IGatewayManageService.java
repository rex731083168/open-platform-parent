package cn.ce.platform_service.gateway.service;

import cn.ce.platform_service.common.Result;
import cn.ce.platform_service.common.page.Page;
import cn.ce.platform_service.gateway.entity.GatewayColonyEntity;
import cn.ce.platform_service.gateway.entity.GatewayNodeEntity;
import cn.ce.platform_service.gateway.entity.QueryGwColonyEntity;
import cn.ce.platform_service.gateway.entity.QueryGwNodeEntity;

/**
* @Description : 说明
* @Author : makangwei
* @Date : 2017年10月10日
*/
public interface IGatewayManageService {

	Result<GatewayColonyEntity> addGatewayCol(GatewayColonyEntity colEntity);
	
	Result<GatewayColonyEntity> modifyGatewayCol(GatewayColonyEntity colEntity);
	
	Result<Page<GatewayColonyEntity>> getAllGatewayCol(QueryGwColonyEntity queryEntity);
	
	Result<String> deleteGatewayColonyById(String colId);
	
	Result<String> addGatewayNode(GatewayNodeEntity nodeEntity);
	
	Result<Page<GatewayNodeEntity>> getAllGatewayNode(QueryGwNodeEntity queryEntity);
	
	Result<String> deleteGatewayNodeById(String nodeId);
	
	Result<String> modifyGatewayNodeById(GatewayNodeEntity nodeEntity);

//	Result<?> migraGateway();
}
