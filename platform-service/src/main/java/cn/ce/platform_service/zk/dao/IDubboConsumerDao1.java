package cn.ce.platform_service.zk.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import cn.ce.platform_service.zk.entity.DubboConsumer;

/**
* @Description : mysql dubbo consumer dao
* @Author : makangwei
* @Date : 2018年1月29日
*/
public interface IDubboConsumerDao1 {

	int clearAll();

	int save(DubboConsumer consumer);

	int findTotalPage(@Param("nodeId")String nodeId);

	List<DubboConsumer> findPage(@Param("startIndex")int startIndex, @Param("pageSize")int pageSize, @Param("nodeId")String nodeId);

	DubboConsumer findById(String id);

}
