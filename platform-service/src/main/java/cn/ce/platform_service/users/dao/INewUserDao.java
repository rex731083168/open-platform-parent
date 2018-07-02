//package cn.ce.platform_service.users.dao;
//
//import java.util.List;
//
//import cn.ce.platform_service.common.page.Page;
//import cn.ce.platform_service.users.entity.User;
//
///**
// * 
// * 
// * @ClassName:  IUserDao1   
// * @Description:新的userdao层 
// * @author: author 
// * @date:   2017年10月11日 上午9:58:39   
// * @Copyright: 2017 中企动力科技股份有限公司 © 1999-2017 300.cn All Rights Reserved
// *
// */
//public interface INewUserDao{
//
//	User findUserByTelNumber(String telNumber);
//
//	User save(User user);
//
//	User findUserByUsernameAndPwd(String userName, String password);
//
//	User findUserByEmail(String email);
//
//	User findUserByUserName(String userName);
//
//	User findUserById(String userId);
//
//	User findUserByName(String name);
//	
//	@Deprecated
//	/**
//	 *  
//	 * @Title: getUserList
//	 * @Description: mongodb 迁移到mysql会完全弃用该方法 推荐getUserList1
//	 * @author: makangwei 
//	 * @date:   2018年1月8日 下午3:13:47 
//	 */
//	Page<User> getUserList(Integer userType, String userName, String email, String telNumber, String enterpriseName,
//			Integer checkState, Integer state, int currentPage, int pageSize);
//	
//	Page<User> getUserList1(User user, int currentPage, int pageSize);
//
//	User findUserByIdCard(String idCard, Integer checkState);
//
//	List<User> findAll();
//
//	
//
//}
