package cn.ce.platform_service.apis.dao;

import org.springframework.transaction.annotation.Transactional;

import cn.ce.platform_service.apis.entity.ApiEntity;

/**
* @Description : 说明
* @Author : makangwei
* @Date : 2018年1月18日
*/
@Transactional
public interface IMysqlApiDao {
	
	public int save(ApiEntity apiEntity);

}
