package cn.ce.platform_service.zk.service;

import cn.ce.platform_service.common.Result;

/**
* @Description : 说明
* @Author : makangwei
* @Date : 2018年1月9日
*/
public interface IZkDubboService {

	boolean clearAll();

	boolean updateData(String zkconnectioninfo, String datakey);

	Result<?> findRootPage(Integer checkCurrentPage, Integer checkPageSize);
	
	Result<?> findNodePage(int currentPage, int pageSize, String rootId);

	Result<?> findNodeById(String id);

	Result<?> findProviderPage(int currentPage, int pageSize, String nodeId);

	Result<?> findProviderById(String id);

	Result<?> findConsumerPage(int currentPage, int pageSize, String nodeId);

	Result<?> findConsumerById(String id);

	Result<?> findRouterPage(int currentPage, int pageSize, String nodeId);

	Result<?> findRouterById(String id);

	Result<?> findConfiguratorPage(int currentPage, int pageSize, String nodeId);

	Result<?> findConfiguratorById(String id);

}
