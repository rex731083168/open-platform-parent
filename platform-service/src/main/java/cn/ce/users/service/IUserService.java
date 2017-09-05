package cn.ce.users.service;

import cn.ce.common.Result;
import cn.ce.page.Page;
import cn.ce.users.entity.User;

/**
 * 
 * @ClassName: IUserService
 * @Description: 提供者服务
 * @author dingjia@300.cn
 *
 */
public interface IUserService {

	void addUser(User user);

	User findOne(String id);

	int modifyById(String id, User user);
	
	User findByUserName(String username);
	
	User findUserByUserNameAndPWD(String username,String password);
	
	Page<User> getUsers(int roleType, String uname, String checkstate, String state, int currentPage, int pageSize);



	Result<String> deleteById(String userId);

	int modifyPasswordById(String id, String newPassword);


	Result<String> resetUserPassword(String userId,String newpassword);

	boolean checkEmail(String email);
	
	boolean checkUserName(String username);

}
