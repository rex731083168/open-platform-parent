package cn.ce.platform_service.dubbapply.dao;

import java.util.List;

import cn.ce.platform_service.dubbapply.entity.DepJar;
import cn.ce.platform_service.dubbapply.entity.QueryDepJar;

/**
* @Description : 说明
* @Author : makangwei
* @Date : 2018年4月24日
*/
public interface IDubboDepJarDao {

	int saveDepJar(DepJar dep);

	DepJar findDepJarById(String depJarId);

	int updateDepJar(DepJar dep);

	List<DepJar> findDepJarByIds(List<String> depJarIds);

	List<DepJar> findDepJarsByMainId(String mainJarId);

	int findListSize(QueryDepJar queryDepJar);

	List<DepJar> getPagedList(QueryDepJar queryDepJar);

}

