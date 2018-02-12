package cn.ce.platform_service.gateway.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import cn.ce.platform_service.gateway.entity.GatewayColonyEntity;
import cn.ce.platform_service.gateway.entity.QueryGwColonyEntity;

/**
* @Description : 说明
* @Author : makangwei
* @Date : 2018年1月30日
*/
public interface IMysqlGwColonyDao {

	int checkByUrl(String colUrl);

	int checkByUrlExp(@Param("colUrl")String colUrl, @Param("colId")String colId);
	
	int addGatewayCol(GatewayColonyEntity colEntity);

	GatewayColonyEntity findById(String colId);

	int update(GatewayColonyEntity colEntity);
	
	int findListSize(QueryGwColonyEntity colEntity);

	List<GatewayColonyEntity> getPagedList(QueryGwColonyEntity queryEntity);

	int deleteById(String colId);

	List<GatewayColonyEntity> getAll();

	int deleteAll();


}
