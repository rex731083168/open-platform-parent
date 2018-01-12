package cn.ce.platform_service.zk.dao;

import cn.ce.platform_service.common.page.Page;
import cn.ce.platform_service.zk.entity.DubboConfigurator;

/**
* @Description : 说明
* @Author : makangwei
* @Date : 2018年1月10日
*/
public interface IDubboConfiguratorDao {

	DubboConfigurator save(DubboConfigurator configurator);

	boolean clearAll();

	DubboConfigurator findById(String id);

	Page<DubboConfigurator> findPage(int currentPage, int pageSize, String nodeId);



}
