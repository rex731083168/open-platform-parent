package cn.ce.apis.service;

import java.util.List;

import cn.ce.apis.entity.ApiAuditEntity;
import cn.ce.apply.entity.ApplyEntity;
import cn.ce.common.Result;
import cn.ce.page.Page;

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

	List<ApplyEntity> findByApplyId(String applyId, String apiId);

	List<ApiAuditEntity> getApiAuditEntity(List<String> apiIds);

	Result<String> deleteById(String auditId);

}
