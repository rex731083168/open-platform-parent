package cn.ce.platform_service.users.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.ce.platform_service.common.Constants;
import cn.ce.platform_service.common.ErrorCodeNo;
import cn.ce.platform_service.common.Result;
import cn.ce.platform_service.common.Status;
import cn.ce.platform_service.common.mail.MailInfo;
import cn.ce.platform_service.common.mail.MailUtil;
import cn.ce.platform_service.page.Page;
import cn.ce.platform_service.users.dao.IUserDAO;
import cn.ce.platform_service.users.entity.User;
import cn.ce.platform_service.users.service.IUserService;
import cn.ce.platform_service.util.Util;

/**
 * 
 * @ClassName: SupplierServiceImpl
 * @Description: 提供者服务实现类型
 * @author dingjia@300.cn
 *
 */
@Service("userService")
public class UserServiceImpl implements IUserService {

    
	private static final Logger _LOGGER = LoggerFactory.getLogger(UserServiceImpl.class);
    /** 提供者数据库操作类型 */
    @Autowired
    private IUserDAO userDAO;

	@Override
	public void addUser(User user) {
		userDAO.addUser(user);
		
	}

	@Override
	public User findOne(String id) {
		return userDAO.findOne(id);
	}

	@Override
	public int modifyById(String id, User user) {
		return userDAO.modifyById(id, user);
	}

	@Override
	public User findByUserName(String username) {
		return userDAO.findUserByUserName(username);
	}

	@Override
	public User findUserByUserNameAndPWD(String username, String password) {
		return userDAO.findUserByUserNameAndPWD(username, password);
	}

	@Override
	public Page<User> getUsers(int roleType, String uname, String checkstate,String state,
			int currentPage, int pageSize) {
		return userDAO.getUsers(roleType, uname, checkstate, state, currentPage, pageSize);
	}



	@Override
	public Result<String> deleteById(String userId) {
		return null;
	}

	@Override
	public int modifyPasswordById(String id, String newPassword) {
		
		return userDAO.modifyPasswordById(id,newPassword);
	}

	/**
	 * @Description : 管理员后台重置密码
	 * @Author : makangwei
	 * @Date : 2017年8月23日
	 */
	public Result<String> resetUserPassword(String userId,String newPassword) {
		
		Result<String> result = new Result<String>();
		
		if(StringUtils.isBlank(userId)){
			result.setMessage("userId不能为空");
			return result;
		}
		
		if(StringUtils.isBlank(newPassword)){
			result.setMessage("新密码不能为空");
			return result;
		}
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("password", newPassword);
		
		int i = userDAO.updateById(userId,map);
		
		if(i > 0){
			result.setMessage("修改成功");
			result.setStatus(Status.SUCCESS);
			return result;
		}else{
			result.setMessage("当前用户不存在，请输入正确的id");
			return result;
		}
	}
	@Override
	public boolean checkUserName(String username) {
		
		User user = userDAO.findUserByUserName(username);
		
		if(user != null){
			return true;
		}
		return false;
	}
	
	@Override
	public boolean checkEmail(String email) {
		
		User user = userDAO.findUserByEmail(email);
		
		//if(user != null && user.getCheckState() != 3){ // 当前email已经存在，且审核状态为审核失败，当前email仍然可以使用
		if(user != null) {
			return false;
		}
		return true;
	}

	@Override
	public User findByEmail(String email) {
		
		return userDAO.findUserByEmail(email);
	}

	@Override
	public Result<User> login(HttpSession session,String userName, String password) {

		Result<User> result = new Result<User>();
		User user = findUserByUserNameAndPWD(userName, password);

		if (user == null) {
			_LOGGER.info("---------->> not found user name");
			result.setErrorMessage("用户名或者密码错误", ErrorCodeNo.SYS008);
			return result;
		} else if (user.getPassword().equals(password.toUpperCase())) {
			_LOGGER.info("---------->> not found user name");
			result.setErrorMessage("用户名或者密码错误", ErrorCodeNo.SYS008);
			return result;
		} else {
			if (user.getState() == 1 && (2 == user.getUserType() || 1 == user.getUserType())
					&& 2 == user.getCheckState()) {
				result.setSuccessMessage("登录成功");
				session.setAttribute(Constants.SES_LOGIN_USER, user);
				user.setPassword("");
				result.setSuccessData(user);
				return result;
			}
			result.setErrorMessage("用户状态不可用");
			return result;
		}
	}

	@Override
	public Result<String> userRegister(String userName, String password, String email, String tel, String userType) {
		Result<String> result = new Result<String>();
		
		//校验当前用户名是否存在
		// TODO 校验用户名
		boolean bool = checkUserName(userName);
		if(bool){
			//当前用户名存在
			result.setErrorMessage("当前用户名已经存在", ErrorCodeNo.SYS009);
			return result;
		}
		
		//校验邮箱
		boolean bool1 = checkEmail(email);
		if(!bool1){
			result.setErrorMessage("当前邮箱已经被注册", ErrorCodeNo.SYS009);
			return result;
		}
		
		//如果之前使用过当前邮箱注册，但是审核失败了，可以重新用该邮箱注册。将原来的数据修改
//		User preUser = userService.findByEmail(email);
		
		try {
			User user = new User();
			user.setUsername(userName.trim());
			user.setPassword(password.trim());
			user.setEmail(email.trim());
			user.setTel(tel.trim());
			user.setUserType(Integer.parseInt(userType));
			user.setState(1);
			user.setRegtime(new Date());
			String appsecret = Util.getRandomStrs(Constants.SECRET_LENGTH);
			user.setAppSecret(appsecret);
			addUser(user);
			result.setSuccessMessage("");
		} catch (Exception ex) {
			result.setErrorMessage("");
		}
		return result;
	}


	@Override
	public Result<Page<User>> approveUsers(String roleType, String userName, String checkState, String state,
			int currentPage, int pageSize) {
		// TODO 解决异常处理
		int role = Integer.parseInt(roleType.trim());

		Result<Page<User>> result = new Result<Page<User>>();
		Page<User> userList = getUsers(role, userName, checkState,state,currentPage, pageSize);
		List<User> items = (List<User>) userList.getItems();
		for (User user : items) {
			user.setPassword("");
		}
		result.setSuccessData(userList);
		return result;
	}

	@Override
	public Result<String> approve(String id, String checkMem, String checkState) {
		id = id.trim();
		Result<String> result = new Result<String>();
		
		try {
			
			User user = findOne(id);
			user.setCheckState(Integer.valueOf(checkState));
			user.setCheckMem(checkMem);

			int i = modifyById(id, user);
			
			
			_LOGGER.info("------->邮件通知用户，帐号审核结果");
			if(i > 0 && "2".equals(checkState)) {
				MailInfo mailInfo = new MailInfo();
				mailInfo.setContent("您申请注册的开放平台帐号已经审核通过。欢迎使用中企云Pass开放平台！");
				mailInfo.setSubject("中企云Pass开放平台账户审核结果通知");
				mailInfo.setToOne(user.getEmail());
				MailUtil.send(mailInfo, false);
			}else if(i > 0 && "3".equals(checkState)){
				MailInfo mailInfo = new MailInfo();
				mailInfo.setContent("很抱歉通知您，您申请的开放平台账户未审核通过，原因是："+checkMem+",请重新申请");
				mailInfo.setSubject("中企云Pass开放平台账户审核结果通知");
				mailInfo.setToOne(user.getEmail());
				MailUtil.send(mailInfo, false);
			}

			result.setSuccessMessage("");
		} catch (Exception e) {
			_LOGGER.info("error happens when execute approve",e);
			result.setErrorMessage("");
		}
		return result;
	}

	@Override
	public Result<String> forbid(String userId, String state) {
		Result<String> result = new Result<String>();
		
		try {
			
			int st = Integer.parseInt(state);
			User user = findOne(userId);
			user.setState(st);
			modifyById(userId, user);
			result.setSuccessMessage("");
		} catch (Exception e) {
			_LOGGER.info("error happens when execute forbid",e);
			result.setErrorMessage("");
		}
		return result;
	}
}
