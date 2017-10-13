package cn.ce.platform_service.gateway.dao;

import java.util.List;

import cn.ce.platform_service.common.page.Page;
import cn.ce.platform_service.gateway.entity.GatewayColonyEntity;

/**
* @Description : 说明
* @Author : makangwei
* @Date : 2017年10月10日
*/
public interface IGatewayManageDao {

	boolean addGatewayCol(GatewayColonyEntity colEntity);
	
	Page<GatewayColonyEntity> getAllGatewayCol(Integer currentPage,Integer pageSize);
	
	boolean deleteGatewayColonyById(Integer colId);
	
	GatewayColonyEntity findById(Integer colId, Class<GatewayColonyEntity> entityclass);
	
	void updateById(String colId, GatewayColonyEntity entity);
	
	List<GatewayColonyEntity> findByField(String field, Object value, Class<?> entityClass);
	
	GatewayColonyEntity findById(String id, Class<?> entityclass);

	List<GatewayColonyEntity> getAll(Class<?> class1);
}
