package cn.ce.platform_service.apis.service;

import cn.ce.platform_service.apis.entity.ApiEntity;
import cn.ce.platform_service.common.Result;
import cn.ce.platform_service.users.entity.User;

/**
* @Description : 说明
* @Author : makangwei
* @Date : 2017年10月12日
*/
public interface IConsoleApiService {

	Result<?> publishApi(User user, ApiEntity apiEntity);

	Result<?> submitApi(String[] apiIds);

	Result<?> modifyApi(ApiEntity apiEntity);

	Result<?> showApi(String apiId);

	Result<?> showApiList(String openApplyId, String userId, Integer checkState, String apiNameLike, int currentPage,
			int pageSize);

	Result<?> checkApiEnName(String apiEnName, String openApplyId);

	Result<?> checkApiChName(String apiChName, String openApplyId);

	Result<?> checkVersion(String apiId, String version);

}
