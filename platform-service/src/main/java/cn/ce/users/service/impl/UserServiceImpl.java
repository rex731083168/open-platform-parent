package cn.ce.users.service.impl;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.ce.common.Result;
import cn.ce.common.Status;
import cn.ce.page.Page;
import cn.ce.users.dao.IUserDAO;
import cn.ce.users.entity.User;
import cn.ce.users.service.IUserService;

/**
 * 
 * @ClassName: SupplierServiceImpl
 * @Description: 提供者服务实现类型
 * @author dingjia@300.cn
 *
 */
@Service
public class UserServiceImpl implements IUserService {

    
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
		
		if(user != null){
			return false;
		}
		return true;
	}

}
