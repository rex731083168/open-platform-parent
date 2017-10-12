package cn.ce.platform_service.apis.service;

import java.util.List;

import cn.ce.platform_service.apis.entity.ApiAuditEntity;
import cn.ce.platform_service.common.Result;
import cn.ce.platform_service.common.page.Page;
import cn.ce.platform_service.diyApply.entity.DiyApplyEntity;

/**
* @Description : 说明
* @Author : makangwei
* @Date : 2017年8月18日
*/
public interface IApiOauthService {

	Result<String> applyApi(String userId, String apiId , String applyId);
	
	ApiAuditEntity findApiByClientId(String clientId);

	Result<String> allowApi(String authId,Integer checkState,String failedReason);

	Result<Page<ApiAuditEntity>> getUseList(String userId, int currentPage, int pageSize);

	Result<Page<ApiAuditEntity>> getsupplyList(String userId, int currentPage, int pageSize);

	List<DiyApplyEntity> findByApplyId(String applyId, String apiId);

	List<ApiAuditEntity> getApiAuditEntity(List<String> apiIds);

	Result<String> deleteById(String auditId);

}
