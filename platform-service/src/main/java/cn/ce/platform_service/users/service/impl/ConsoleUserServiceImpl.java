package cn.ce.platform_service.users.service.impl;

import java.util.Date;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import cn.ce.platform_service.common.Constants;
import cn.ce.platform_service.common.ErrorCodeNo;
import cn.ce.platform_service.common.Result;
import cn.ce.platform_service.users.dao.INewUserDao;
import cn.ce.platform_service.users.dao.IUserDAO;
import cn.ce.platform_service.users.entity.User;
import cn.ce.platform_service.users.service.IConsoleUserService;
import cn.ce.platform_service.util.Util;

/**
* @Description : 说明
* @Author : makangwei
* @Date : 2017年10月11日
*/
@Service(value="consoleUserService")
public class ConsoleUserServiceImpl implements IConsoleUserService{

	private static final Logger _LOGGER = LoggerFactory.getLogger(ConsoleUserServiceImpl.class);
	
    @Resource
    private IUserDAO userDao;
    @Resource
    private INewUserDao newUserDao;
	@Override
	public Result<String> userRegister(User user) {
		
		Result<String> result = new Result<String>();
		
		//校验用户名
		User user1= userDao.findUserByUserName(user.getUserName());
		if(user1 != null){
			result.setErrorMessage("当前用户名已经存在", ErrorCodeNo.SYS009);
			return result;
		}
		//校验邮箱
		User user2 = userDao.findUserByEmail(user.getEmail());
		if(user2 != null){
			result.setErrorMessage("当前邮箱已经被注册", ErrorCodeNo.SYS009);
			return result;
		}
		//校验手机号
		User user3 = newUserDao.findUserByTelNumber(user.getTelNumber());
		if(user3 != null){
			result.setErrorMessage("当前手机号已经被使用", ErrorCodeNo.SYS009);
			return result;
		}
		
		try {
			// TODO appSecret使用Util生成的，这里有什么作用？
			// TODO userType为int类型 ；/** 用户类型  0:管理员，1:普通用户，2:提供者 */
			// state 保留字段
			user.setState(1);
			user.setRegTime(new Date());
			user.setCheckState(0);
			user.setAppSecret(Util.getRandomStrs(Constants.SECRET_LENGTH));
			newUserDao.save(user);
			result.setSuccessMessage("添加成功");
		} catch (Exception e) {
			_LOGGER.error("error happens when execute save user to db",e);
			result.setErrorMessage("");
		}
		return result;
	}


	@Override
	public Result<?> authenticate(String userId, String enterpriseName, String idCard, String userRealName) {
		Result<String> result = new Result<String>();
		User user = newUserDao.findUserById(userId);
		
		if(user == null){
			result.setErrorMessage("当前用户不存在", ErrorCodeNo.SYS006);
			return result;
		}
		
		user.setEnterpriseName(enterpriseName);
		user.setIdCard(idCard);
		user.setUserRealName(userRealName);
		user.setCheckState(1);
		newUserDao.save(user);
		result.setSuccessMessage("");;
		return result;
	}
	@Override
	public Result<User> login(HttpSession session,String userName, String password) {

		Result<User> result = new Result<User>();
		User user = newUserDao.findUserByUsernameAndPwd(userName, password);
		if (user == null) {
			result.setErrorMessage("用户名或者密码错误", ErrorCodeNo.SYS008);
			return result;
		}
		
		_LOGGER.info("账号密码正确");
		user.setPassword("");
		session.setAttribute(Constants.SES_LOGIN_USER, user);
		result.setSuccessData(user);
		return result;
	}

	
	
	@Override
	public Result<?> sendRegistSms(String telNumber, HttpSession session) {
		
		User user = newUserDao.findUserByTelNumber(telNumber);
		Result<String> result = new Result<String>();
		if(user != null) {
			result.setErrorMessage("当前手机号已经被注册");
		}
		
		//Integer checkCode = RandomUtil.random6Number();
		// TODO 发送短信
		
		session.setAttribute(telNumber, 123456);
		session.setAttribute(telNumber+"TransTime", System.currentTimeMillis()); //验证码发送时间
		result.setSuccessMessage("发送成功");
		
		return result;
	}

	
	
	@Override
	public Result<?> sendRePwdSms(String telNumber, HttpSession session) {
		
		Result<String> result = new Result<String>();
		
		User user = newUserDao.findUserByTelNumber(telNumber);
		if(user == null){
			result.setErrorMessage("当前用户不存在",ErrorCodeNo.SYS006);
			return result;
		}
		
		// TODO 发送短信
		//Integer checkCode = RandomUtil.random6Number();
		session.setAttribute(telNumber, 123456);
		session.setAttribute(telNumber+"TransTime", System.currentTimeMillis()); //验证码发送时间
		result.setSuccessMessage("发送成功");
		
		return result;
	}

	
	
	@Override
	public Result<?> checkTelNumber(String telNumber) {
		User user = newUserDao.findUserByTelNumber(telNumber);
		Result<String> result = new Result<String>();
		if(user != null) {
			result.setErrorMessage("当前手机号已经被注册");
		}else{
			result.setSuccessMessage("");
		}
		return result;
	}
	

	@Override
	public Result<?> checkEmail(String email) {
		User user = newUserDao.findUserByEmail(email);
		Result<String> result = new Result<String>();
		if(user != null) {
			result.setErrorMessage("当前邮箱已经被注册");
		}else{
			result.setSuccessMessage("");
		}
		return result;
	}
	

	@Override
	public Result<?> checkUserName(String userName) {
		User user = newUserDao.findUserByUserName(userName);
		Result<String> result = new Result<String>();
		if(user != null){
			result.setErrorMessage("当前用户名已存在，请重新输入");
		}else{
			result.setSuccessMessage("当前用户名可以使用");
		}
		return result;
	}

	
	@Override
	public Result<?> modifyPassword(String telNumber, String newPassword) {
		
		Result<String> result = new Result<String>();
		User user = newUserDao.findUserByTelNumber(telNumber);
		if(user == null){
			result.setErrorMessage("当前用户不存在",ErrorCodeNo.SYS006);
			return result;
		}
		
		user.setPassword(newPassword);
		newUserDao.save(user);
		result.setSuccessMessage("修改成功");
		return result;
	}
	
}
