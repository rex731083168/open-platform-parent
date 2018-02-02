package cn.ce.platform_service.zk.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import cn.ce.platform_service.zk.entity.DubboConfigurator;

/**
* @Description : mysql dubbo configurator dao
* @Author : makangwei
* @Date : 2018年1月29日
*/
public interface IDubboConfiguratorDao1 {

	int clearAll();

	int save(DubboConfigurator configurator);

	int findTotalPage(@Param("nodeId")String nodeId);

	List<DubboConfigurator> findPage(@Param("startIndex")int startIndex, @Param("pageSize")int pageSize, @Param("nodeId")String nodeId);

	DubboConfigurator findById(String id);

}
