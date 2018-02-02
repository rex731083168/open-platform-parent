package cn.ce.platform_service.admin.service.impl;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Service;

import cn.ce.platform_service.admin.service.IAdminService;
import cn.ce.platform_service.common.Constants;
import cn.ce.platform_service.common.ErrorCodeNo;
import cn.ce.platform_service.common.Result;
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
public class AdminServiceImpl implements IAdminService {

//    @Resource
//    private IAdminDao adminDao;
    @Resource
    private IMysqlUserDao mysqlAdminDao;
    
	@Override //后台管理员登录功能
	public Result<Map<String, Object>> login(HttpSession session, String userName, String password) {
		
	   	Result<Map<String,Object>> result = new Result<Map<String,Object>>();
		//User admin = adminDao.checkLogin(userName,password);
	   	User admin = mysqlAdminDao.findByUserName(userName);
		if (admin == null) {
			result.setErrorMessage("您输入的账号不存在", ErrorCodeNo.SYS020);
		} else if(!userName.equals(admin.getUserName())){
			result.setErrorMessage("密码错误", ErrorCodeNo.SYS021);
		}else {
			admin.setPassword("");
			session.setAttribute(Constants.SES_LOGIN_USER, admin);
			Map<String,Object> map = new HashMap<String,Object>();
			map.put("userName", admin.getUserName());
			map.put("userId", admin.getId());
			result.setSuccessData(map);
		}
		return result;
	}
}
