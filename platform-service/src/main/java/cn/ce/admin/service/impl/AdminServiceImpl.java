package cn.ce.admin.service.impl;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.ce.admin.dao.IAdminDao;
import cn.ce.admin.entity.AdminEntity;
import cn.ce.admin.service.IAdminService;

/**
 * 
 * @ClassName: AdminServiceImpl
 * @Description: 管理员服务实现类型
 * @author dingjia@300.cn
 *
 */
@Service(value = "adminService")
public class AdminServiceImpl implements IAdminService {

    /** 日志对象 */
    private static Logger logger = Logger.getLogger(AdminServiceImpl.class);
    
    /** 管理员数据库操作接口  */
    @Autowired
    private IAdminDao adminDao;
    
    @Override
    public AdminEntity findByUserName(String userName) {
        return adminDao.findByUserName(userName);
    }

    @Override
    public void updatePassword(AdminEntity admin) {
        adminDao.update(admin);
    }

	@Override
	public AdminEntity checkLogin(String username, String password) {
		
		return adminDao.checkLogin(username,password);
	}

}
