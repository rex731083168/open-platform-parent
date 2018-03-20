package cn.ce.platform_service.zk.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import cn.ce.platform_service.zk.entity.DubboProvider;

/**
* @Description : mysql dubbo provider dao
* @Author : makangwei
* @Date : 2018年1月29日
*/
public interface IDubboProviderDao1 {

	int clearAll();

	int save(DubboProvider provider);

	int findTotalPage(@Param("nodeId")String nodeId);

	List<DubboProvider> findPage(@Param("startIndex")int startIndex, @Param("pageSize")int pageSize, @Param("nodeId")String nodeId);

	DubboProvider findById(String id);


}
