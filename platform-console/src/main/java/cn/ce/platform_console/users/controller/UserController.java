package cn.ce.platform_console.users.controller;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import cn.ce.platform_service.common.Constants;
import cn.ce.platform_service.common.Result;
import cn.ce.platform_service.common.Status;
import cn.ce.platform_service.users.entity.User;
import cn.ce.platform_service.users.service.IUserService;


/**
 * @author 作者 E -mail: dingjia@300.cn 创建时间：2017年7月17日 下午3:17:22
 * @version V1.0 类说明
 */
@RestController
public class UserController {

	/** 日志对象 */
	private static Logger _LOGGER = Logger.getLogger(UserController.class);

	@Resource
	private IUserService userService;

	
	@RequestMapping(value = "/user/login", method = RequestMethod.POST)
	public Result<?> login(HttpSession session, 
			@RequestParam String userName, 
			@RequestParam String password) {

		return userService.login(session, userName,password);
	}
	
	
	@RequestMapping(value="/user/checkUserName",method=RequestMethod.GET)
	public Result<?> checkUserName(String userName){
		
		if(StringUtils.isBlank(userName)){
			Result<String> result = new Result<String>();
			result.setErrorMessage("当前用户名不能为空");
			return result;
		}
		
		return userService.checkUserName(userName);
	}
	
	@RequestMapping(value="/user/resetPwd",method=RequestMethod.POST)
	public Result<String> resetPwd(HttpServletRequest request,HttpServletResponse response,
			@RequestParam(required=true)String userName,
			@RequestParam(required=true)String password,
			@RequestParam(required=true)String newPassword
			){
		
		_LOGGER.info("---------->用户名："+userName);
		_LOGGER.info("---------->旧密码："+password);
		_LOGGER.info("---------->新密码："+newPassword);
		
		Result<String> result = new Result<String>();
		
		User user = userService.findUserByUserNameAndPWD(userName, password);
		if(user != null){
			user.setPassword(newPassword);
			int i = userService.modifyPasswordById(user.getId(), newPassword);
			_LOGGER.info("修改了"+i+"个用户");
			result.setStatus(Status.SUCCESS);
			result.setMessage("修改成功");
			return result;
		}else{
			result.setMessage("当前用户名或密码错误");
			return result;
		}
	}
	
	@RequestMapping(value = "user/register", method = RequestMethod.POST)
	@ResponseBody
	public Result<String> userRegister(HttpServletRequest request, HttpServletResponse response, 
			@RequestParam String userName,
			@RequestParam String password, 
			@RequestParam String email, 
			@RequestParam String telNumber,
			@RequestParam Integer userType) {
		
		_LOGGER.info("userName:"+userName);
		_LOGGER.info("email:"+email);
		_LOGGER.info("tel:"+telNumber);
		
		return userService.userRegister(userName,password,email,telNumber,userType);
	}

	@RequestMapping(value = "user/logOut", method = RequestMethod.POST)
	@ResponseBody
	public Result<String> logOut(HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		_LOGGER.info("---------->> Action for logout");
		Result<String> result = new Result<String>();
		try {
			session.invalidate();
			result.setSuccessMessage("");
			return result;
		} catch (Exception e) {
			_LOGGER.info("error happens when execute user logout",e);
			result.setErrorMessage("");
			return result;
		}
	}

	@RequestMapping(value = "user/checkLogin", method = RequestMethod.POST)
	@ResponseBody
	public Result<User> checkLogin(HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		
		Result<User> result = new Result<User>();
		User user = null;
		
		try{
			user = (User) session.getAttribute(Constants.SES_LOGIN_USER);
		}catch(Exception e){
			_LOGGER.error("error happens when get user info from session",e);
		}
		
		if(user != null){
			user.setPassword("");
			result.setSuccessData(user);
		}else{
			result.setErrorMessage("");
		}
		return result;
	}
	
	@RequestMapping(value="user/checkEmail",method=RequestMethod.GET)
	@ResponseBody
	public Result<String> checkEmail(HttpServletRequest request,HttpServletResponse response,
			String email){
		
		Result<String> result = new Result<String>();
		
		boolean bool = userService.checkEmail(email);
		
		if(bool){
			result.setStatus(Status.SUCCESS);
			result.setMessage("当前邮箱可用");
		}else{
			result.setMessage("当前邮箱已经被注册");
		}
		return result;
	}

}
