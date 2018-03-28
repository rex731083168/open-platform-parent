package cn.ce.platform_service.gateway.dao;

import org.apache.ibatis.annotations.Param;
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

	int deleteSaas(@Param("saas_id")String saas_id, @Param("resource_type")String resource_type);

	SaasEntity getSaas(@Param("saas_id")String saas_id, @Param("resource_type")String resource_type);

	int updateSaas(@Param("saas_id")String saas_id, @Param("resource_type")String resource_type, @Param("target_url")String target_url);

}

