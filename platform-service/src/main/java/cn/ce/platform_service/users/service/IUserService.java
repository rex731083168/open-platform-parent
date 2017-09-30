package cn.ce.platform_service.users.service;

import javax.servlet.http.HttpSession;

import cn.ce.platform_service.common.Result;
import cn.ce.platform_service.page.Page;
import cn.ce.platform_service.users.entity.User;

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

	User findByEmail(String email);

	Result<User> login(HttpSession session, String userName, String password);

	Result<String> userRegister(String userName, String password, String email, String tel, String userType);

	Result<Page<User>> approveUsers(String roleType, String userName, String checkState, String state, int currentPage,
			int pageSize);

	Result<String> approve(String id, String checkMem, String checkState);

	Result<String> forbid(String userId, String state);

}
