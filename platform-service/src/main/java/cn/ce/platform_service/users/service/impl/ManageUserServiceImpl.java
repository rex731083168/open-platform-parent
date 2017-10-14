package cn.ce.platform_service.users.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import cn.ce.platform_service.common.Result;
import cn.ce.platform_service.common.mail.MailInfo;
import cn.ce.platform_service.common.mail.MailUtil;
import cn.ce.platform_service.common.page.Page;
import cn.ce.platform_service.users.dao.INewUserDao;
import cn.ce.platform_service.users.entity.User;
import cn.ce.platform_service.users.service.IManageUserService;

/**
* @Description : 说明
* @Author : makangwei
* @Date : 2017年10月12日
*/
@Service(value="manageUserService")
public class ManageUserServiceImpl implements IManageUserService{

	private static final Logger _LOGGER = LoggerFactory.getLogger(ManageUserServiceImpl.class);
	
	@Resource 
	private INewUserDao newUserDao;
	
	
	@Override
	public Result<Page<User>> userList(Integer userType, String userName, String email, String telNumber,
			String enterpriseName, Integer checkState, Integer state, int currentPage, int pageSize) {

		Result<Page<User>> result = new Result<Page<User>>();
		Page<User> userList = newUserDao.getUserList(userType, userName, email,telNumber,enterpriseName, checkState,state,currentPage, pageSize);
		List<User> items = (List<User>) userList.getItems();
		for (User user : items) {
			user.setPassword("");
		}
		result.setSuccessData(userList);
		return result;
	}


	@Override
	public Result<String> auditUsers(List<String> userIdArray, String checkMem, Integer checkState) {
		Result<String> result = new Result<String>();
	
		for (String userId : userIdArray) {
			User user = newUserDao.findUserById(userId);
			user.setCheckState(checkState);
			user.setCheckMem(checkMem);
			newUserDao.save(user);
		
			_LOGGER.info("------->邮件通知用户，帐号审核结果");
			if(2 == checkState) {
				MailInfo mailInfo = new MailInfo();
				mailInfo.setContent("您申请注册的开放平台帐号已经审核通过。欢迎使用中企云Pass开放平台！");
				mailInfo.setSubject("中企云Paas开放平台账户审核结果通知");
				mailInfo.setToOne(user.getEmail());
				MailUtil.send(mailInfo, false);
			}else if(3 == checkState){
				MailInfo mailInfo = new MailInfo();
				mailInfo.setContent("很抱歉通知您，您申请的开放平台账户未审核通过，原因是："+checkMem+",请重新申请");
				mailInfo.setSubject("中企云Paas开放平台账户审核结果通知");
				mailInfo.setToOne(user.getEmail());
				MailUtil.send(mailInfo, false);
			}
		} 
		result.setSuccessMessage("");
		return result;
	}

	
	@Override
	public Result<String> activeOrForbidUsers(String userId, Integer state) {
		
		Result<String> result = new Result<String>();
		User user = newUserDao.findUserById(userId);
		user.setState(state);
		newUserDao.save(user);
		result.setSuccessMessage("");
		return result;
	}

}
