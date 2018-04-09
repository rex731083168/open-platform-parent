package cn.ce.platform_console.users.controller;

import java.util.UUID;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import cn.ce.platform_service.common.Constants;
import cn.ce.platform_service.common.ErrorCodeNo;
import cn.ce.platform_service.common.Result;
import cn.ce.platform_service.common.Status;
import cn.ce.platform_service.users.entity.User;
import cn.ce.platform_service.users.service.IConsoleUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;


/**
 * @ClassName:  UserController   
 * @Description:Console用户控制类
 * @author: makangwei
 * @date:   2017年10月11日 下午2:27:28   
 * @Copyright: 2017 中企动力科技股份有限公司 © 1999-2017 300.cn All Rights Reserved
 */
@RestController
@RequestMapping("/user")
@Api("用户管理")
public class UserController {

	/** 日志对象 */
	private static Logger _LOGGER = Logger.getLogger(UserController.class);

	@Resource
	private IConsoleUserService consoleUserService;
	
	/**
	 * 
	 * 用户中心迁移后，该类已经不再使用。
	 * 新的迁移mysql未经过联调测试，如果重新使用该模块，需要重新联调测试
	 * @date:   2018年01月29日 上午11:10:18 
	 */
	
	@RequestMapping(value = "/register", method = RequestMethod.POST)
	@ApiOperation("用户注册")
	public Result<?> userRegister(HttpServletRequest request, HttpServletResponse response, HttpSession session,
			@RequestBody User user) {
		
		_LOGGER.info("userName:"+user.getUserName());
		_LOGGER.info("email:"+user.getEmail());
		
		
		_LOGGER.info("校验验证码是否正确");
		Integer checkCode1 = (Integer)session.getAttribute(user.getEmail());
		
		
		//邮箱验证码校验
		if(checkCode1 == null){
			return new Result<String>("session数据不存在", ErrorCodeNo.SYS019,null,Status.FAILED);
		}
		if(!user.getCheckCode().equals(checkCode1)){
			return new Result<String>("验证码错误", ErrorCodeNo.SYS008,null,Status.FAILED);
		}
		 
		return consoleUserService.userRegister(user);
	}

	@RequestMapping(value="/authenticate", method=RequestMethod.POST)
	@ApiOperation("用户登陆后认证信息")
	public Result<?> userAuthenticate(
			HttpSession session,
			@RequestParam String userId,
			@RequestParam String enterpriseName, //企业名称 
			@RequestParam String idCard, //用户身份证号码
			@RequestParam String userRealName //用户真实姓名
			){
			
		return consoleUserService.authenticate(userId,enterpriseName,idCard,userRealName,session);
	}
	
	@RequestMapping(value="/checkIdCard",method=RequestMethod.GET)
	@ApiOperation("校验身份证唯一性")
	public Result<?> checkIdCard(@RequestParam(required=true) String idCard){
		
		if(StringUtils.isBlank(idCard)){
			return new Result<String>("idCard不能为空", ErrorCodeNo.SYS005, null, Status.FAILED);
		}
		
		return consoleUserService.checkIdCard(idCard);
	}
	
	@RequestMapping(value = "/login", method = RequestMethod.POST)
	@ApiOperation("登录")
	public Result<?> login(HttpSession session, 
			@RequestParam String userName, 
			@RequestParam String password) {

		return consoleUserService.login(session, userName,password);
	}
	
	@RequestMapping(value = "/checkLogin", method = RequestMethod.POST)
	@ApiOperation("检查登录")
	public Result<?> checkLogin(HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		
		Object userObj = session.getAttribute(Constants.SES_LOGIN_USER);
		if(userObj == null){
			return Result.errorResult("session已过期", ErrorCodeNo.SYS019, null, Status.FAILED);
		}
		
		User user = (User) session.getAttribute(Constants.SES_LOGIN_USER);
		
		_LOGGER.info("*********checkLogin 当前session仍然存在user数据");
		User userNew = consoleUserService.findUserById(user.getId());
		
		if(userNew != null){
			userNew.setPassword("");
		}
		_LOGGER.info("********checkLogin 获取新的用户数据：");
		session.setAttribute(Constants.SES_LOGIN_USER, userNew);
		
		return new Result<User>(null,null,userNew,Status.SUCCESS);
		
	}

	@RequestMapping(value = "/logOut", method = RequestMethod.POST)
	@ApiOperation("退出登录")
	public Result<?> logOut(HttpSession session) {
		
		_LOGGER.info("---------->> Action for logout");
		session.invalidate();
		return new Result<String>("ok",ErrorCodeNo.SYS000,null,Status.SUCCESS);
	}
	
	@RequestMapping(value="modifyPassword",method=RequestMethod.POST)
	@ApiOperation("忘记密码后重置密码(需先经过邮箱验证)")
	public Result<?> modifyPassword(HttpSession session,
			@RequestParam String email,
			@RequestParam String newPassword,
			@RequestParam String uuid){
		
		String uuid1 = (String)session.getAttribute("uuid");
		if(!uuid.equals(uuid1)){
			return new Result<String>("uuid错误",ErrorCodeNo.SYS016,null,Status.FAILED);
		}
		
		return consoleUserService.modifyPassword(email,newPassword);
	}
	
	@RequestMapping(value="/sendRegistEmail", method=RequestMethod.POST)
	@ApiOperation("注册时发送邮箱验证码")
	public Result<?> sendRegistEmail(HttpSession session, @RequestParam String email){
		
		return consoleUserService.sendRegistEmail(email,session);
	}
	
	@RequestMapping(value="/sendRePwdEmail", method=RequestMethod.POST)
	@ApiOperation("忘记密码时发送邮箱验证码")
	public Result<?> sendRePwdEmail(HttpSession session, 
			@RequestParam(required=true) String email){
		
		return consoleUserService.sendRePwdEmail(email,session);
	}
	
	@RequestMapping(value="checkTelVerifyCode",method=RequestMethod.POST)
	@ApiOperation("校验忘记密码时发送的验证码")
	public Result<?> checkTelVerifyCode(HttpSession session,
			@RequestParam String email,
			@RequestParam String telVerifyCode){
		
		String verifyCode = session.getAttribute(email) == null ? "" : session.getAttribute(email).toString();
		Long transTime = (Long) session.getAttribute(email+"TransTime");
		
		if(StringUtils.isBlank(verifyCode) || transTime == null){
			return new Result<String>("当前是新的session",ErrorCodeNo.SYS019,null,Status.FAILED);
		}
		if(!telVerifyCode.equals(verifyCode)){
			return new Result<String>("验证码错误",ErrorCodeNo.SYS008,null,Status.FAILED);
		}
		if((transTime + Constants.TEL_VALIDITY) < System.currentTimeMillis()){
			return new Result<String>("验证码过期",ErrorCodeNo.SYS011,null,Status.FAILED);
		}
		
		String uuid = UUID.randomUUID().toString();
		session.setAttribute("uuid", uuid);
		return new Result<String>(uuid,ErrorCodeNo.SYS000,null,Status.SUCCESS);
	}
	
	//校验邮箱
	@RequestMapping(value="/checkEmail",method=RequestMethod.GET)
	@ApiOperation("校验邮箱是否可用")
	public Result<?> checkEmail(HttpServletRequest request,HttpServletResponse response,
			String email){
	
		if(StringUtils.isBlank(email)){
			return new Result<String>("邮箱不能为空",ErrorCodeNo.SYS005,null,Status.FAILED);
		}
		return consoleUserService.checkEmail(email);
	}
	
	@RequestMapping(value="/checkUserName",method=RequestMethod.GET)
	@ApiOperation("校验用户名是否可用")
	public Result<?> checkUserName(String userName){
		
		if(StringUtils.isBlank(userName)){
			return new Result<String>("当前用户名不能为空",ErrorCodeNo.SYS005,null,Status.FAILED);
		}
		
		return consoleUserService.checkUserName(userName);
	}
	
	@RequestMapping(value="/checkTelNumber",method=RequestMethod.GET)
	@ApiOperation("校验手机号是否可用")
	public Result<?> checkTelNumber(String telNumber){
		
		if(StringUtils.isBlank(telNumber)){
			return new Result<String>("手机号不能为空",ErrorCodeNo.SYS005,null,Status.FAILED);
		}
		return consoleUserService.checkTelNumber(telNumber);
	}
}
