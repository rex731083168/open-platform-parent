package cn.ce.platform_service.apis.service;

import java.util.List;

import cn.ce.platform_service.apis.entity.ApiEntity;
import cn.ce.platform_service.apis.entity.QueryApiEntity;
import cn.ce.platform_service.common.Result;
import cn.ce.platform_service.common.page.Page;

/**
* @Description : 说明
* @Author : makangwei
* @Date : 2017年10月12日
*/
public interface IManageApiService {

	Result<?> auditApi(List<String> apiId, Integer checkState, String checkMem);

	Result<ApiEntity> showApi(String apiId);

	Result<Page<ApiEntity>> apiList(QueryApiEntity entity, int currentPage, int pageSize);

	
	
}
