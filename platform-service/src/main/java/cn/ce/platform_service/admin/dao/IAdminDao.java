package cn.ce.platform_service.admin.dao;

import cn.ce.platform_service.admin.entity.AdminEntity;


/**
 * 
 * 
 * @ClassName:  IAdminDao   
 * @Description:TODO(这里用一句话描述这个类的作用)   
 * @author: makangwei
 * @date:   2017年9月29日 上午9:47:56   
 * @Copyright: 2017 中企动力科技股份有限公司 © 1999-2017 300.cn All Rights Reserved
 *
 */
public interface IAdminDao {
	
	AdminEntity checkLogin(String username, String password);
	
    AdminEntity findByUserName(String userName);
    
    void update(AdminEntity admin);
    
    void createNewDefaultAdmin();
    
}
