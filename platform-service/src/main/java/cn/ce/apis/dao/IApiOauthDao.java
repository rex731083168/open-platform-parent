package cn.ce.apis.dao;

import java.util.List;

import cn.ce.apis.entity.ApiAuditEntity;
import cn.ce.apply.entity.ApplyEntity;
import cn.ce.page.Page;

/**
* @Description : 说明
* @Author : makangwei
* @Date : 2017年8月18日
*/
public interface IApiOauthDao {

	void addAuditEntity(ApiAuditEntity auditEntity);

	ApiAuditEntity findById(String authId);

	void UpdateAuditEntity(ApiAuditEntity auditEntity);

	int updateClientIdAndSecret(String authId, String clientId, String secret);

	Page<ApiAuditEntity> findAsPage(String userId, int currentPage, int pageSize);

	Page<ApiAuditEntity> findAsPage1(String userId, int currentPage, int pageSize);

	List<ApplyEntity> findByApplyId(String applyId, String apiId);

	List<ApiAuditEntity> getListByApiId(List<String> apiIds);

	List<ApiAuditEntity> getApiAuditEntity(List<String> authIds);

	int deleteById(String auditId);
	
}
