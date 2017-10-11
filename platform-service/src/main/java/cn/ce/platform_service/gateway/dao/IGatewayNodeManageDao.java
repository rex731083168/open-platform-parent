package cn.ce.platform_service.gateway.dao;

import java.util.List;

import cn.ce.platform_service.gateway.entity.GatewayNodeEntity;
import cn.ce.platform_service.page.Page;

/**
* @Description : 说明
* @Author : makangwei
* @Date : 2017年10月10日
*/
public interface IGatewayNodeManageDao {

	boolean addGatewayNode(GatewayNodeEntity nodeEntity);
	
	Page<GatewayNodeEntity> getAllGatewayNode(Integer currentPage, Integer pageSize, Integer colId);
	
	boolean deleteGatewayNodeById(String nodeId);

	List<GatewayNodeEntity> findByField(String string, String nodeId, Class<?> class1);

	void updateById(String nodeId, GatewayNodeEntity nodeEntity);

	List<GatewayNodeEntity> findByField(String string, Object colId, Class<?> class1);

	List<GatewayNodeEntity> getAll(Class<?> class1);
}
