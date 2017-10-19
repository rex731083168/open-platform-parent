package cn.ce.platform_service.users.dao.impl;

import org.apache.commons.lang.StringUtils;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import cn.ce.platform_service.common.DBFieldsConstants;
import cn.ce.platform_service.common.page.Page;
import cn.ce.platform_service.core.mongo.BaseMongoDaoImpl;
import cn.ce.platform_service.users.dao.INewUserDao;
import cn.ce.platform_service.users.entity.User;

/**
 * @Description : 新的userDao实现
 * @Author : makangwei
 * @Date : 2017年10月11日
 */
@Service(value = "newUserDao")
public class NewUserDaoImpl extends BaseMongoDaoImpl<User> implements INewUserDao {

	public User save(User user) {
		if(StringUtils.isNotBlank(user.getId())){
			super.update(user);
		} else {
			super.save(user);
		}
		return user;
	}

	@Override
	public User findUserByUsernameAndPwd(String userName, String password) {
		Criteria c = new Criteria();
		c.and("userName").is(userName);
		c.and("password").is(password);
		Criteria c1 = new Criteria();
		c1.and("telNumber").is(userName);
		c1.and("password").is(password);
		
		
		Query query = new Query().addCriteria(c.orOperator(c,c1));
		return super.findOne(query);
	}

	@Override
	public User findUserByEmail(String email) {
		Query query = new Query();
		query.addCriteria(Criteria.where("email").is(email));
		return super.findOne(query);
	}

	@Override
	public User findUserByUserName(String userName) {
		Query query = new Query();
		query.addCriteria(Criteria.where("userName").is(userName));
		return super.findOne(query);
	}

	@Override
	public User findUserByTelNumber(String telNumber) {
		Query query = new Query();
		query.addCriteria(Criteria.where("telNumber").is(telNumber));
		return super.findOne(query);
	}

	@Override
	public User findUserById(String userId) {

		return super.findById(userId);
	}

	@Override
	public Page<User> getUserList(Integer userType, String userName, String email, String telNumber,
			String enterpriseName, Integer checkState, Integer state, int currentPage, int pageSize) {
		//构建查询对象
		Criteria c = new Criteria();
		
		if(null != userType && 0 != userType){
			c.and(DBFieldsConstants.USER_USERTYPE).is(userType);
		}
		if(null != checkState){
			c.and(DBFieldsConstants.USER_CHECKSTATE).is(Integer.valueOf(checkState));
		}
		if(null != state){
			c.and(DBFieldsConstants.USER_STATE).is(Integer.valueOf(state));
		}
		if(StringUtils.isNotBlank(userName)){
			c.and(DBFieldsConstants.USER_USERNAME).regex(userName);
		}
		if(StringUtils.isNotBlank(email)){
			c.and(DBFieldsConstants.USER_EMAIL).regex(email);
		}
		if(StringUtils.isNotBlank(telNumber)){
			c.and(DBFieldsConstants.USER_TELNUMBER).regex(telNumber);
		}
		if(StringUtils.isNotBlank(enterpriseName)){
			c.and(DBFieldsConstants.USER_ENTERPRISE_NAME).regex(enterpriseName);
		}
        Query query = new Query(c).with(new Sort(new Order(Direction.DESC,DBFieldsConstants.USER_REGTIME)));
        
        return super.findPage(new Page<User>(currentPage, 0,pageSize),query);
	}

}
