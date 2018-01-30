package cn.ce.platform_service.zk.dao;

import cn.ce.platform_service.common.page.Page;
import cn.ce.platform_service.zk.entity.DubboRouter;

/**
* @Description : 说明
* @Author : makangwei
* @Date : 2018年1月10日
*/
public interface IDubboRouterDao {

	DubboRouter save(DubboRouter consumer);

	boolean clearAll();

	Page<DubboRouter> findPage(int currentPage, int pageSize, String nodeId);

	DubboRouter findById(String id);

}
