package cn.ce.platform_manage.user;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import cn.ce.platform_service.common.AuditConstants;
import cn.ce.platform_service.common.ErrorCodeNo;
import cn.ce.platform_service.common.Result;
import cn.ce.platform_service.common.page.Page;
import cn.ce.platform_service.users.entity.QueryUserEntity;
import cn.ce.platform_service.users.entity.User;
import cn.ce.platform_service.users.service.IManageUserService;
import cn.ce.platform_service.util.SplitUtil;

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

	public Result<Page<User>> userList(@RequestBody QueryUserEntity userEntity) {
//		Integer userType, //用户类型，必传
//		String userName, //用户名，支持模糊查询
//		String email, //email支持模糊查询
//		String telNumber,	//手机号,支持模糊查询
//		String enterpriseName, //企业名称，支持模糊查询
//		Integer checkState, //审核状态
//		Integer state, //启用禁用状态
//		return manageUserService.userList(userType, userName, email, telNumber, enterpriseName, checkState, state,
//				currentPage, pageSize);
		return manageUserService.userList(userEntity);
	}


	@RequestMapping(value = "/auditUsers", method = RequestMethod.POST)
	public Result<?> approveUsers(
			@RequestParam(required=true)String userIds, 
			@RequestParam(required=true)Integer checkState,
			@RequestParam(required=false)String checkMem) {

		List<String> userIdArray = SplitUtil.splitStringWithComma(userIds);
		
		if(checkState != AuditConstants.API_CHECK_STATE_SUCCESS &&
				checkState != AuditConstants.API_CHECK_STATE_DENY){
			Result<String> result = new Result<String>();
			result.setErrorMessage("审核状态不正确", ErrorCodeNo.SYS012);
		}
		
		return manageUserService.auditUsers(userIdArray, checkMem, checkState);
	}

	/**
	 * 
	 * @Title: activeOrForbidUsers
	 * @Description: 启用或禁用用户
	 * @author: makangwei 
	 * @date:   2017年11月21日 下午6:15:51 
	 */
	@RequestMapping(value = "/activeOrForbidUsers", method = RequestMethod.POST)
	public Result<String> activeOrForbidUsers(
			@RequestParam String userId, 
			@RequestParam Integer state) {
		if(AuditConstants.USER_STATE_OFF != state && AuditConstants.USER_STATE_ON != state){
			Result<String> result = new Result<String>();
			result.setErrorMessage("状态不可用", ErrorCodeNo.SYS012);
			return result;
		}
		return manageUserService.activeOrForbidUsers(userId, state);
	}
}
