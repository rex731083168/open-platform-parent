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
import cn.ce.platform_service.common.Status;
import cn.ce.platform_service.common.page.Page;
import cn.ce.platform_service.users.entity.QueryUserEntity;
import cn.ce.platform_service.users.entity.User;
import cn.ce.platform_service.users.service.IManageUserService;
import cn.ce.platform_service.util.SplitUtil;
import io.swagger.annotations.ApiOperation;

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
	@ApiOperation("用户列表_TODO")
//	public Result<Page<User>> userList(@RequestBody QueryUserEntity userEntity) {
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

//		return manageUserService.userList(userType, userName, email, telNumber, enterpriseName, checkState, state,
//				currentPage, pageSize);
		QueryUserEntity userEntity  = new QueryUserEntity();
		userEntity.setUserType(userType);
		userEntity.setUserName(userName);
		userEntity.setEmail(email);
		userEntity.setEnterpriseName(enterpriseName);
		userEntity.setCheckState(checkState);
		userEntity.setState(state);
		userEntity.setCurrentPage(currentPage);
		userEntity.setPageSize(pageSize);
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
			return new Result<String>("审核状态不正确", ErrorCodeNo.SYS012,null,Status.FAILED);
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
			@RequestParam(required=true) String userId, 
			@RequestParam(required=true) Integer state) {
		if(AuditConstants.USER_STATE_OFF != state && AuditConstants.USER_STATE_ON != state){
			return new Result<String>("状态不可用", ErrorCodeNo.SYS012,null,Status.SUCCESS);
		}
		
		return manageUserService.activeOrForbidUsers(userId, state);
	}
}
