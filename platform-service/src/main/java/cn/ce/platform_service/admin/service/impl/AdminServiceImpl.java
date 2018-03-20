package cn.ce.platform_service.admin.service.impl;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import cn.ce.platform_service.admin.service.IAdminService;
import cn.ce.platform_service.common.Constants;
import cn.ce.platform_service.common.ErrorCodeNo;
import cn.ce.platform_service.common.Result;
import cn.ce.platform_service.common.Status;
import cn.ce.platform_service.users.dao.IMysqlUserDao;
import cn.ce.platform_service.users.entity.User;

/**
 * 
 * 
 * @ClassName:  AdminServiceImpl   
 * @Description:TODO(这里用一句话描述这个类的作用)   
 * @author: makangwei
 * @date:   2017年9月29日 上午9:52:33   
 * @Copyright: 2017 中企动力科技股份有限公司 © 1999-2017 300.cn All Rights Reserved
 *
 */
@Service(value = "adminService")
@Transactional(propagation=Propagation.REQUIRED)
public class AdminServiceImpl implements IAdminService {

    @Resource
    private IMysqlUserDao mysqlAdminDao;
    
	@Override //后台管理员登录功能
	public Result<?> login(HttpSession session, String userName, String password) {
		
		//User admin = adminDao.checkLogin(userName,password);
	   	User admin = mysqlAdminDao.findByUserName(userName);
		if (admin == null) {
			return new Result<String>("您输入的账号不存在", ErrorCodeNo.SYS020,null,Status.FAILED);
		} else if(!userName.equals(admin.getUserName())){
			return new Result<String>("密码错误", ErrorCodeNo.SYS021,null,Status.FAILED);
		}else {
			admin.setPassword("");
			session.setAttribute(Constants.SES_LOGIN_USER, admin);
			Map<String,Object> map = new HashMap<String,Object>();
			map.put("userName", admin.getUserName());
			map.put("userId", admin.getId());
			return new Result<Map<String,Object>>("",ErrorCodeNo.SYS000,map,Status.SUCCESS);
		}
	}
}
