package cn.ce.platform_service.zk.dao;

import cn.ce.platform_service.common.page.Page;
import cn.ce.platform_service.zk.entity.DubboNode;

/**
* @Description : 说明
* @Author : makangwei
* @Date : 2018年1月10日
*/
public interface IDubboNodeDao {

	DubboNode save(DubboNode dubboNode);

	boolean clearAll();

	Page<DubboNode> findPage(int currentPage, int pageSize, String rootId);

	DubboNode findById(String id);

}
