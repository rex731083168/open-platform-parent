package cn.ce.platform_console.users.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.ce.platform_service.common.Constants;
import cn.ce.platform_service.common.Result;
import cn.ce.platform_service.common.Status;
import cn.ce.platform_service.users.entity.User;
import cn.ce.platform_service.users.service.IUserService;
import cn.ce.platform_service.util.SmsUtil;


/**
 * @author 作者 E -mail: dingjia@300.cn 创建时间：2017年7月17日 下午3:17:22
 * @version V1.0 类说明
 */
@Controller
public class LoginController {

	/** 日志对象 */
	private static Logger _LOGGER = Logger.getLogger(LoginController.class);

	@Autowired
	private IUserService userService;

	@RequestMapping(value = "user/login", method = RequestMethod.POST)
	@ResponseBody
	public Result<User> login(HttpSession session, @RequestParam String userName, 
			@RequestParam String password) {

		return userService.login(session, userName,password);
	}
	@RequestMapping(value="/user/checkUserName",method=RequestMethod.GET,produces="application/json;charset=utf-8")
	@ResponseBody
	public Result<String> checkUserName(HttpServletRequest request,HttpServletResponse response,
			String userName){
		
		Result<String> result = new Result<String>();
		
		if(StringUtils.isBlank(userName)){
			result.setErrorMessage("当前用户名不能为空");
			return result;
		}
		
		boolean bool = userService.checkUserName(userName);
		
		if(bool){
			result.setMessage("当前用户名已存在，请重新输入");
			return result;
		}else{
			result.setStatus(Status.SUCCESS);
			result.setMessage("当前用户名可以使用");
			return result;
		}
	}
	
	@RequestMapping(value="/user/resetPwd",method=RequestMethod.POST,consumes="application/json")
	@ResponseBody
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
	public Result<String> userRegister(HttpServletRequest request, HttpServletResponse response, String userName,
			String password, String email, String tel,String userType) {
		_LOGGER.info("------->> ACTION!     Save regitry");
		_LOGGER.info(userName);
		_LOGGER.info(email);
		_LOGGER.info(tel);
		
		return userService.userRegister(userName,password,email,tel,userType);
	}

	@RequestMapping(value = "user/logout", method = RequestMethod.POST)
	@ResponseBody
	public Result<String> logout(HttpServletRequest request, HttpServletResponse response, HttpSession session) {
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
		try {
			User user = (User) session.getAttribute(Constants.SES_LOGIN_USER);
			user.setPassword("");
			result.setSuccessData(user);
			return result;

		} catch (Exception e) {
			_LOGGER.info("error happens when execute user checklogin",e);
			result.setErrorMessage("");
			return result;
		}
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
	
	@RequestMapping(value="user/sendSms",method=RequestMethod.POST)
	@ResponseBody
	public Result<String> sendSms(HttpServletRequest request,HttpServletResponse response,
			String phone,String content){
		
		Result<String> result = new Result<String>();
		boolean bool = SmsUtil.messageSend(phone, content);
		
		if(bool){
			result.setStatus(Status.SUCCESS);
			result.setMessage("发送成功");
		}else{
			result.setErrorMessage("发送失败");
		}
		
		return result;
	}
	
}
