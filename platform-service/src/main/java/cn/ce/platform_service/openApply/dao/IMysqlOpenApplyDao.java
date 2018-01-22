package cn.ce.platform_service.openApply.dao;

import org.springframework.transaction.annotation.Transactional;

import cn.ce.platform_service.openApply.entity.OpenApplyEntity;

/**
* @Description : 说明
* @Author : makangwei
* @Date : 2018年1月18日
*/
@Transactional
public interface IMysqlOpenApplyDao {
	
	int save(OpenApplyEntity openApplyEntity);
}
