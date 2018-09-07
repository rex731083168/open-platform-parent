package cn.ce.platform_service.apis.service;

import java.util.List;

import cn.ce.platform_service.apis.entity.QueryApiEntity;
import cn.ce.platform_service.common.Result;

/**
* @Description : 说明
* @Author : makangwei
* @Date : 2017年10月12日
*/
public interface IManageApiService {

	Result<?> auditApi(List<String> apiId, Integer checkState, String checkMem, boolean isMocked);

	Result<?> showApi(String apiId);

	Result<?> apiList(QueryApiEntity apiEntity);

	Result<?> apiAllList(QueryApiEntity apiEntity);


}
