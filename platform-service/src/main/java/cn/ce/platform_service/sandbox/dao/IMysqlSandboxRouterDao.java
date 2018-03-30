package cn.ce.platform_service.sandbox.dao;

import org.apache.ibatis.annotations.Param;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import cn.ce.platform_service.sandbox.entity.SandboxRouterEntity;

@Transactional(propagation = Propagation.REQUIRED)
public interface IMysqlSandboxRouterDao {

	int save(SandboxRouterEntity s);

	void clearAll();

	int deleteSandboxRouter(@Param("saas_id") String saas_id, @Param("resource_type") String resource_type,
			@Param("sandbox_id") String sandboxId);

	SandboxRouterEntity getSandboxRouter(@Param("saas_id") String saas_id, @Param("resource_type") String resource_type,
			@Param("sandbox_id") String sandboxId);

	int updateSandboxRouter(@Param("saas_id") String saas_id, @Param("resource_type") String resource_type,
			@Param("sandbox_id") String sandboxId, @Param("target_url") String target_url);

}
