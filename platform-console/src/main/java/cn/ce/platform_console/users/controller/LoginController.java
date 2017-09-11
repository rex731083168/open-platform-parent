package cn.ce.users.controller;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.ce.common.Constants;
import cn.ce.common.Result;
import cn.ce.common.Status;
import cn.ce.users.entity.User;
import cn.ce.users.service.IUserService;
import cn.ce.util.Util;

/**
 * @author 作者 E -mail: dingjia@300.cn 创建时间：2017年7月17日 下午3:17:22
 * @version V1.0 类说明
 */
@Controller
public class LoginController {

	/** 日志对象 */
	private static Logger logger = Logger.getLogger(LoginController.class);

	@Autowired
	private IUserService userService;

	@RequestMapping(value = "user/login", method = RequestMethod.POST)
	@ResponseBody
	public String login(HttpServletRequest request, HttpServletResponse response, HttpSession session, Model model,
			String usertype, String username, String password) {

		username.trim();
		password.trim();

		JSONObject result = new JSONObject();
		User user = userService.findUserByUserNameAndPWD(username, password);

		if (user == null) {
			logger.info("---------->> not found user name");
			result.put("code", "0");
			result.put("message", "用户名称或密码错误！");
			return result.toString();
		} else if (user.getPassword().equals(password.toUpperCase())) {
			logger.info("---------->> not found user name");
			result.put("code", "0");
			result.put("message", "用户名称或密码错误！");
			return result.toString();
		} else {
			if (user.getState() == 1 && (2 == user.getUserType() || 1 == user.getUserType())
					&& 2 == user.getCheckState()) {
				result.put("code", "1");
				result.put("message", "OK");
				session.setAttribute(Constants.SES_LOGIN_USER, user);
				JSONObject object = new JSONObject(user);
				object.remove("password");
				result.put("data", object);
				return result.toString();
			}
			result.put("code", "0");
			result.put("message", "用户状态不可用！");
			return result.toString();

		}
	}
	@RequestMapping(value="/user/checkUserName",method=RequestMethod.GET,produces="application/json;charset=utf-8")
	@ResponseBody
	public Result<String> checkUserName(HttpServletRequest request,HttpServletResponse response,
			String username){
		
		Result<String> result = new Result<String>();
		
		if(StringUtils.isBlank(username)){
			result.setErrorMessage("当前用户名不能为空");
			return result;
		}
		
		boolean bool = userService.checkUserName(username);
		
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
		
		logger.info("---------->用户名："+userName);
		logger.info("---------->旧密码："+password);
		logger.info("---------->新密码："+newPassword);
		
		Result<String> result = new Result<String>();
		
		User user = userService.findUserByUserNameAndPWD(userName, password);
		if(user != null){
			user.setPassword(newPassword);
			int i = userService.modifyPasswordById(user.getId(), newPassword);
			logger.info("修改了"+i+"个用户");
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
	public String userRegister(HttpServletRequest request, HttpServletResponse response, String username,
			String password, String email, String tel,String userType) {
		logger.info("------->> ACTION!     Save regitry");
		logger.info(username);
		logger.info(email);
		logger.info(tel);
		
		JSONObject ret = new JSONObject();
		
		//校验当前用户名是否存在
		// TODO 校验用户名
		boolean bool = userService.checkUserName(username);
		if(bool){
			//当前用户名存在
			ret.put("0", "0");
			ret.put("message", "当前用户已存在");
			return ret.toString();
		}
		
		//校验邮箱
		boolean bool1 = userService.checkEmail(email);
		if(!bool1){
			ret.put("0", "0");
			ret.put("message", "当前邮箱已经被注册");
			return ret.toString();
		}
		
		//如果之前使用过当前邮箱注册，但是审核失败了，可以重新用该邮箱注册。将原来的数据修改
//		User preUser = userService.findByEmail(email);
		
		try {
			User user = new User();
			user.setUsername(username.trim());
			user.setPassword(password.trim());
			user.setEmail(email.trim());
			user.setTel(tel.trim());
			user.setUserType(Integer.parseInt(userType));
			user.setState(1);
			user.setRegtime(new Date());
			String appsecret = Util.getRandomStrs(Constants.SECRET_LENGTH);
			user.setAppSecret(appsecret);
			userService.addUser(user);
			ret.put("code", "1");
			ret.put("message", "OK");
			return ret.toString();
		} catch (Exception ex) {
			ret.put("code", "0");
			ret.put("message", "ERROR");
			ex.printStackTrace();
			return ret.toString();
		}
	}

	@RequestMapping(value = "user/logout", method = RequestMethod.POST)
	@ResponseBody
	public String logout(HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		logger.info("---------->> Action for logout");
		JSONObject result = new JSONObject();
		try {
			session.invalidate();
			result.put("code", Constants.SUCCESS);
			result.put("message", Constants.RESULT_OK);
			return result.toString();

		} catch (Exception e) {
			e.printStackTrace();
			result.put("code", Constants.SUCCESS);
			result.put("message", Constants.RESULT_ERROR);
			return result.toString();
		}
	}

	@RequestMapping(value = "user/checklogin", method = RequestMethod.POST)
	@ResponseBody
	public String checkLogin(HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		JSONObject result = new JSONObject();
		try {
			User user = (User) session.getAttribute(Constants.SES_LOGIN_USER);
			JSONObject object = new JSONObject(user);
			object.remove("password");
			result.put("data", object);
			result.put("code", Constants.SUCCESS);
			result.put("message", Constants.RESULT_OK);
			return result.toString();

		} catch (Exception e) {
			e.printStackTrace();
			result.put("code", Constants.SUCCESS);
			result.put("message", Constants.RESULT_ERROR);
			return result.toString();
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
}
