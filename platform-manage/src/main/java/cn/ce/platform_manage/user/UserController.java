package cn.ce.platform_manage.user;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import cn.ce.platform_service.common.Result;
import cn.ce.platform_service.page.Page;
import cn.ce.platform_service.users.entity.User;
import cn.ce.platform_service.users.service.IUserService;

/**
 * @Description : 说明
 * @Author : makangwei
 * @Date : 2017年8月22日
 */
@RestController
@RequestMapping("/user")
public class UserController {

	@Resource
	private IUserService userService;

	// 后台获取使用者列表
	@RequestMapping(value = "/approveUsers", method = RequestMethod.POST)
	public Result<Page<User>> approveUsers(String roleType, String userName, String email, String telNumber,
			String enterpriseName, String checkState, String state, 
			@RequestParam(required=false,defaultValue = "1")int currentPage, 
			@RequestParam(required=false,defaultValue = "10")int pageSize) {

		return userService.approveUsers(roleType, userName, email, telNumber, enterpriseName, checkState, state,
				currentPage, pageSize);
	}

	@RequestMapping(value = "deleteUserById", method = RequestMethod.GET)
	public Result<String> deleteUserById(String userId) {

		return userService.deleteById(userId);
	}

	@RequestMapping(value = "/resetUserPassword", method = RequestMethod.POST)
	public Result<String> resetUserPassword(HttpServletRequest request, HttpServletResponse response, String userId,
			String newPassword) {

		return userService.resetUserPassword(userId, newPassword);
	}

	@RequestMapping(value = "/approve", method = RequestMethod.POST)
	public Result<String> approve(String id, String checkMem, String checkState) {

		return userService.approve(id, checkMem, checkState);
	}

	@RequestMapping(value = "/forbid", method = RequestMethod.POST)
	public Result<String> forbid(String userId, String state) {

		return userService.forbid(userId, state);
	}
}
