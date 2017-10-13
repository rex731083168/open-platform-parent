package cn.ce.platform_service.users.service;

import cn.ce.platform_service.common.Result;
import cn.ce.platform_service.common.page.Page;
import cn.ce.platform_service.users.entity.User;

/**
* @Description : 说明
* @Author : makangwei
* @Date : 2017年10月12日
*/
public interface IManageUserService {

	Result<Page<User>> userList(Integer userType, String userName, String email, String telNumber,
			String enterpriseName, Integer checkState, Integer state, int currentPage, int pageSize);

	Result<String> auditUsers(String[] userIdArray, String checkMem, Integer checkState);

	Result<String> activeOrForbidUsers(String userId, Integer state);

}
