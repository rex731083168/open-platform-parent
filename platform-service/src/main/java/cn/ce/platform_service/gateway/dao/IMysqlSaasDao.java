package cn.ce.platform_service.gateway.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import cn.ce.platform_service.gateway.entity.SaasEntity;
import cn.ce.platform_service.sandbox.entity.SandBox;

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
	
	int deleteSandboxSaas(@Param("saas_id")String saas_id, @Param("resource_type")String resource_type,@Param("sandbox_id")String sandbox_id);

	SaasEntity getSaas(@Param("saas_id")String saas_id, @Param("resource_type")String resource_type);
	
	SaasEntity getSandboxSaas(@Param("saas_id")String saas_id, @Param("resource_type")String resource_type , @Param("sandbox_id")String sandbox_id);

	int updateSaas(@Param("saas_id")String saas_id, @Param("resource_type")String resource_type, @Param("target_url")String target_url);
	
	int updateSandboxSaas(@Param("saas_id")String saas_id, @Param("resource_type")String resource_type, @Param("target_url")String target_url,@Param("sandbox_id")String sandbox_id);

	List<SandBox> getSandBoxRouterList(SaasEntity s);
	
}

