package cn.ce.platform_service.admin.dao;

import cn.ce.platform_service.admin.entity.AdminEntity;


/**
 * 
 * @ClassName: IAdminDAO
 * @Description: 操作管理员表的数据接
 * @author dingjia@300.cn
 *
 */
public interface IAdminDao {
    AdminEntity findByUserName(String userName);
    void update(AdminEntity admin);
    void createNewDefaultAdmin();
	AdminEntity checkLogin(String username, String password);
}
