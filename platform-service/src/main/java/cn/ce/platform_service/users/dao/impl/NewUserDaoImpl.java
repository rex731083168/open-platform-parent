package cn.ce.platform_service.users.dao.impl;


import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import cn.ce.platform_service.core.mongo.BaseMongoDaoImpl;
import cn.ce.platform_service.users.dao.INewUserDao;
import cn.ce.platform_service.users.entity.User;

/**
* @Description : 新的userDao实现
* @Author : makangwei
* @Date : 2017年10月11日
*/
@Service(value="newUserDao")
public class NewUserDaoImpl extends BaseMongoDaoImpl<User> implements INewUserDao{

	@Override
	public User findUserByTelNumber(String telNumber) {
		Query query = new Query();
		query.addCriteria(Criteria.where("telNumber").is(telNumber));
		return super.findOne(query);
	}

	public User save(User user) {
		return super.save(user);
	}

	@Override
	public User findUserByUsernameAndPwd(String userName, String password) {
		Criteria c = new Criteria();
		c.and("userName").is(userName);
		c.and("password").is(password);
		Query query = new Query().addCriteria(c);
		super.findOne(query);
		return null;
	}

}
