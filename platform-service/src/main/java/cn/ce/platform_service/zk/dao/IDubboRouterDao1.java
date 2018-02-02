package cn.ce.platform_service.zk.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import cn.ce.platform_service.zk.entity.DubboRouter;

/**
* @Description : mysql dubbo router dao
* @Author : makangwei
* @Date : 2018年1月29日
*/
public interface IDubboRouterDao1 {

	int clearAll();

	int save(DubboRouter router);

	int findTotalPage(@Param("nodeId")String nodeId);

	List<DubboRouter> findPage(@Param("startIndex")int startIndex, @Param("pageSize")int pageSize, @Param("nodeId")String nodeId);

	DubboRouter findById(String id);

}
