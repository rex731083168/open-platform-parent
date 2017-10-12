package cn.ce.platform_service.users.service;

import javax.servlet.http.HttpSession;

import cn.ce.platform_service.common.Result;
import cn.ce.platform_service.users.entity.User;

/**
* @Description : 说明
* @Author : makangwei
* @Date : 2017年10月11日
*/

public interface IConsoleUserService {

	Result<?> userRegister(User user);
	
	Result<?> login(HttpSession session, String userName, String password);
	
	Result<?> sendRegistSms(String telNumber, HttpSession session);

	Result<?> sendRePwdSms(String telNumber, HttpSession session);
	
	Result<?> checkTelNumber(String telNumber);

	Result<?> checkEmail(String email);
	
	Result<?> checkUserName(String userName);

	Result<?> modifyPassword(String telNumber, String newPassword);
	//用户认证信息
	Result<?> authenticate(String userId, String enterpriseName, String idCard, String userRealName);

}