package cn.ce.platform_service.gateway.dao;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import cn.ce.platform_service.gateway.entity.SaasEntity;

/**
* @Description : 说明
* @Author : makangwei
* @Date : 2018年3月22日
*/
@Transactional(propagation=Propagation.REQUIRED)
public interface IMysqlSaasDao {

	int save(SaasEntity s);

	void clearAll();
	
}

