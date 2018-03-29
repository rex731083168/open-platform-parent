package cn.ce.platform_service.sandbox.dao;

import java.util.List;

import cn.ce.platform_service.sandbox.entity.QuerySandBox;
import cn.ce.platform_service.sandbox.entity.SandBox;

/**
* @Description : 说明
* @Author : makangwei
* @Date : 2018年3月23日
*/
public interface ISandBoxDao {

	SandBox findByName(String name);

	int saveBox(SandBox sandBox);

	SandBox findById(String boxId);

	int findListSize(QuerySandBox queryBox);

	List<SandBox> getPagedList(QuerySandBox queryBox);

	int updateBox(SandBox sandBox);

}

