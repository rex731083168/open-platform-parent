package cn.ce.platform_service.admin.service;

import cn.ce.platform_service.admin.entity.AdminEntity;

/**
 * 
 * @ClassName: IAdminService
 * @Description: 管理员服务
 * @author dingjia@300.cn
 *
 */
public interface IAdminService {
	/**
	 * 根据帐号查询管理员
	 * 
	 * @param userName
	 *            : 管理员帐号
	 * @return 管理员对象
	 */
	AdminEntity findByUserName(String userName);

	/**
	 * 修改管理员密码
	 * 
	 * @param admin
	 *            : 修改后管理员对象
	 */
	void updatePassword(AdminEntity admin);

	AdminEntity checkLogin(String username, String password);
}
