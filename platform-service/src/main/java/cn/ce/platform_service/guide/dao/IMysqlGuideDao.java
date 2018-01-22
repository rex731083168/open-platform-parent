package cn.ce.platform_service.guide.dao;

import org.springframework.transaction.annotation.Transactional;

import cn.ce.platform_service.guide.entity.GuideEntity;

/**
* @Description : 说明
* @Author : makangwei
* @Date : 2018年1月18日
*/
@Transactional
public interface IMysqlGuideDao {

	int save(GuideEntity guideEntity);
	
}
