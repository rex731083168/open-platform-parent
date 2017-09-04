package cn.ce.users.dao;

import java.util.List;
import java.util.Map;

import cn.ce.page.Page;
import cn.ce.users.entity.User;

/**
 * 
 * @ClassName: IUserDAO
 * @Description: 提供者操作数据库接口
 * @author dingjia@300.cn
 *
 */
public interface IUserDAO {
	void addUser(User user);

	User findOne(String id);

	int modifyById(String id, User user);

	List<User> getAll();

	Page<User> getUsers();

	int delOne(String id);

	User findUserByUserName(String username);

	User findUserByUserNameAndPWD(String username, String password);
	
	Page<User> getUsers(int roleType, String uname, String checkstate, int currentPage, int pageSize);

	int modifyPasswordById(String id, String newPassword);

	int updateById(String userId,Map<String,Object> map);

	User findUserByEmail(String email);

}
