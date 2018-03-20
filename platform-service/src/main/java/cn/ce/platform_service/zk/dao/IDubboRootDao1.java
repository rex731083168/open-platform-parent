package cn.ce.platform_service.zk.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import cn.ce.platform_service.zk.entity.DubboRoot;

/**
* @Description : mysql dubbo root dao
* @Author : makangwei
* @Date : 2018年1月29日
*/
public interface IDubboRootDao1 {

	int clearAll();

	int save(DubboRoot dubboRoot);

	int findTotalPage();

	List<DubboRoot> findPage(@Param("startIndex")Integer startIndex, @Param("pageSize")Integer pageSize);

}
