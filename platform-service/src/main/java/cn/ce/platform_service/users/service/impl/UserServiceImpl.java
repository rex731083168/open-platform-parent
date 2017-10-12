package cn.ce.platform_service.users.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.stereotype.Service;

import cn.ce.platform_service.common.Result;
import cn.ce.platform_service.common.Status;
import cn.ce.platform_service.common.mail.MailInfo;
import cn.ce.platform_service.common.mail.MailUtil;
import cn.ce.platform_service.page.Page;
import cn.ce.platform_service.users.dao.INewUserDao;
import cn.ce.platform_service.users.dao.IUserDAO;
import cn.ce.platform_service.users.entity.User;
import cn.ce.platform_service.users.service.IUserService;

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
    @Resource
    private IUserDAO userDao;
    @Resource
    private INewUserDao newUserDao;
    
	@Override
	public void addUser(User user) {
		userDao.addUser(user);
		
	}

	@Override
	public User findOne(String id) {
		return userDao.findOne(id);
	}

	@Override
	public int modifyById(String id, User user) {
		return userDao.modifyById(id, user);
	}

	@Override
	public User findByUserName(String username) {
		return userDao.findUserByUserName(username);
	}

	@Override
	public User findUserByUserNameAndPWD(String username, String password) {
		return userDao.findUserByUserNameAndPWD(username, password);
	}

	@Override
	public Page<User> getUsers(int roleType, String uname,  String email,String telNumber,String enterpriseName, String checkstate,String state,
			int currentPage, int pageSize) {
		return userDao.getUsers(roleType, uname, email,telNumber,enterpriseName,checkstate, state, currentPage, pageSize);
	}



	@Override
	public Result<String> deleteById(String userId) {
		return null;
	}

	@Override
	public int modifyPasswordById(String id, String newPassword) {
		
		return userDao.modifyPasswordById(id,newPassword);
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
		
		int i = userDao.updateById(userId,map);
		
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
	public User findByEmail(String email) {
		
		return userDao.findUserByEmail(email);
	}


	@Override
	public Result<Page<User>> approveUsers(String roleType, String userName, String email,String telNumber,String enterpriseName,String checkState, String state,
			int currentPage, int pageSize) {
		
		
		// TODO 解决异常处理
		int role = Integer.parseInt(roleType.trim());

		Result<Page<User>> result = new Result<Page<User>>();
		Page<User> userList = getUsers(role, userName, email,telNumber,enterpriseName, checkState,state,currentPage, pageSize);
		@SuppressWarnings("unchecked")
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