package cn.ce.platform_service.users.dao;

import cn.ce.platform_service.users.entity.User;

/**
 * 
 * 
 * @ClassName:  IUserDao1   
 * @Description:新的userdao层 
 * @author: author 
 * @date:   2017年10月11日 上午9:58:39   
 * @Copyright: 2017 中企动力科技股份有限公司 © 1999-2017 300.cn All Rights Reserved
 *
 */
public interface INewUserDao{

	User findUserByTelNumber(String telNumber);

	User save(User user);

	User findUserByUsernameAndPwd(String userName, String password);
	

}
