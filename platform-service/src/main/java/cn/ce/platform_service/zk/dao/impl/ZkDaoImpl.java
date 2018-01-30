package cn.ce.platform_service.zk.dao.impl;

import org.springframework.stereotype.Repository;

import cn.ce.platform_service.core.mongo.BaseMongoDaoImpl;
import cn.ce.platform_service.zk.dao.IZkDao;
import cn.ce.platform_service.zk.entity.ZkNode;

/**
* @Description : 说明
* @Author : makangwei
* @Date : 2018年1月9日
*/
@Repository(value ="zkDao")
public class ZkDaoImpl extends BaseMongoDaoImpl<ZkNode> implements IZkDao {
	
}
