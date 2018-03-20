package cn.ce.platform_service.zk.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import cn.ce.platform_service.zk.entity.DubboNode;

/**
* @Description : mysql dubbo node dao
* @Author : makangwei
* @Date : 2018年1月29日
*/
public interface IDubboNodeDao1 {

	int clearAll();

	int save(DubboNode dNode);

	int findTotalPage(@Param("rootId")String rootId);

	List<DubboNode> findPage(@Param("startIndex")int startIndex, @Param("pageSize")int pageSize, @Param("rootId")String rootId);

	DubboNode findById(String id);

}
