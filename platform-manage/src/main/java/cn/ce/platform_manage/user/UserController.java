package cn.ce.platform_manage.user;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import cn.ce.platform_service.common.Result;
import cn.ce.platform_service.common.page.Page;
import cn.ce.platform_service.users.entity.User;
import cn.ce.platform_service.users.service.IManageUserService;

/**
 * @Description : 说明
 * @Author : makangwei
 * @Date : 2017年8月22日
 */
@RestController
@RequestMapping("/user")
public class UserController {

	@Resource
	private IManageUserService manageUserService;
	
	// 后台获取使用者列表
	@RequestMapping(value = "/userList", method = RequestMethod.POST)
	public Result<Page<User>> userList(
			Integer userType, //用户类型，必传
			String userName, //用户名，支持模糊查询
			String email, //email支持模糊查询
			String telNumber,	//手机号,支持模糊查询
			String enterpriseName, //企业名称，支持模糊查询
			Integer checkState, //审核状态
			Integer state, //启用禁用状态
			@RequestParam(required=false,defaultValue = "1")int currentPage, 
			@RequestParam(required=false,defaultValue = "10")int pageSize) {

		return manageUserService.userList(userType, userName, email, telNumber, enterpriseName, checkState, state,
				currentPage, pageSize);
	}


	@RequestMapping(value = "/auditUsers", method = RequestMethod.POST)
	public Result<?> approveUsers(String userIds, String checkMem, Integer checkState) {

		String[] userIdArray = userIds.split(",");
		return manageUserService.auditUsers(userIdArray, checkMem, checkState);
	}

	@RequestMapping(value = "/activeOrForbidUsers", method = RequestMethod.POST)
	public Result<String> activeOrForbidUsers(
			@RequestParam String userId, 
			@RequestParam Integer state) {

		return manageUserService.activeOrForbidUsers(userId, state);
	}
}
