package cn.ce.users.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import com.mongodb.WriteResult;

import cn.ce.common.Constants;
import cn.ce.core.AbstractBaseMongoDao;
import cn.ce.page.Page;
import cn.ce.page.PageContext;
import cn.ce.users.dao.IUserDAO;
import cn.ce.users.entity.User;

/**
 * 
 * @ClassName: UserDAOImpl
 * @Description: 提供者数据库操作类型
 * @author dingjia@300.cn
 *
 */
@Repository
public class UserDAOImpl extends AbstractBaseMongoDao<User> implements IUserDAO {

	@Override
	public void addUser(User user) {
		this.add(user);
	}

	@Override
	public User findOne(String id) {
		return super.findById(id, User.class);
	}

	@Override
	public int modifyById(String id, User user) {
		try{
			mongoTemplate.save(user);
			return 1;
		}catch(Exception e){
			return 0;
		}
	}

	@Override
	public List<User> getAll() {
		return super.getAll(User.class);
	}

	@Override
	public int delOne(String id) {
		return super.delById(id, User.class);
	}

	@Override
	public Page<User> getUsers() {
		return super.findAsPage(PageContext.getCuurentPage(), PageContext.getPageSize(), User.class);
	}

	public User findUserByUserName(String username) {
		return super.findOneByField("username", username, User.class);
	}

	public User findUserByUserNameAndPWD(String username, String password) {
		Map map = new HashMap<String, String>();
		map.put("username", username);
		map.put("password", password);
		return super.findOneByFieldsAnd(map, User.class);
	}

	@Override
	public void init() {
		// TODO Auto-generated method stub

	}

	@Override
		public Page<User> getUsers(int roleType, String uname,
				String checkstate, int currentPage, int pageSize) {
			//构建查询对象
			Criteria c = new Criteria();
			
			if(0 != roleType){
				c.and("userType").is(roleType);
			}
			
			if(StringUtils.isNotBlank(checkstate)){
				c.and("checkstate").is(Integer.valueOf(checkstate));
			}
			
			if(StringUtils.isNotBlank(uname)){
				c.and("username").regex(uname);
			}
			
	        Query query = new Query(c).with(new Sort(new Order(Direction.DESC,"regtime")));
	        
	        return this.findAsPage(query, currentPage, pageSize, User.class);
		}

	private String getUserRoleField(int userRole) {
		String field = "developerRole";
		if (userRole == Constants.USER_ROLE_SUPPLIER) {
			field = "supplierRole";
		}
		return field;
	}

	@Override
	public int modifyPasswordById(String id, String newPassword) {
		Query query = new Query(Criteria.where("id").is(id));
		WriteResult wr = mongoTemplate.updateFirst(query, Update.update("password", newPassword), User.class);
		return wr.getN();
	}

	@Override
	public int updateById(String userId, Map<String,Object> map) {
		
		Query query = new Query(Criteria.where("id").is(userId));
		Update update = new Update();
		for (String key : map.keySet()) {
			update.set(key, map.get(key));
		}
		WriteResult wr = mongoTemplate.updateMulti(query, update, User.class);
		
		return wr.getN();
	}

	@Override
	public User findUserByEmail(String email) {
		
		return super.findOneByField("email", email, User.class);
	}

}
