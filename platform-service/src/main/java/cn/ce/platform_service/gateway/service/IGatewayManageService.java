package cn.ce.platform_service.gateway.service;

import cn.ce.platform_service.common.Result;
import cn.ce.platform_service.common.page.Page;
import cn.ce.platform_service.gateway.entity.GatewayColonyEntity;
import cn.ce.platform_service.gateway.entity.GatewayNodeEntity;

/**
* @Description : 说明
* @Author : makangwei
* @Date : 2017年10月10日
*/
public interface IGatewayManageService {

	Result<GatewayColonyEntity> addGatewayCol(GatewayColonyEntity colEntity);
	
	Result<GatewayColonyEntity> modifyGatewayCol(GatewayColonyEntity colEntity);
	
	Result<Page<GatewayColonyEntity>> getAllGatewayCol(Integer currentPage, Integer pageSize);
	
	Result<String> deleteGatewayColonyById(Integer colId);
	
	Result<String> addGatewayNode(GatewayNodeEntity nodeEntity);
	
	Result<Page<GatewayNodeEntity>> getAllGatewayNode(Integer currentPage, Integer pageSize, Integer colId);
	
	Result<String> deleteGatewayNodeById(String nodeId);
	
	Result<String> modifyGatewayNodeById(GatewayNodeEntity nodeEntity);
}
