package cn.ce.platform_service.admin.service.impl;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Service;

import cn.ce.platform_service.admin.dao.IAdminDao;
import cn.ce.platform_service.admin.entity.AdminEntity;
import cn.ce.platform_service.admin.service.IAdminService;
import cn.ce.platform_service.common.Constants;
import cn.ce.platform_service.common.ErrorCodeNo;
import cn.ce.platform_service.common.Result;

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

    @Resource
    private IAdminDao adminDao;
    
	@Override //后台管理员登录功能
	public Result<Map<String, Object>> login(HttpSession session, String userName, String password) {
		
	   	Result<Map<String,Object>> result = new Result<Map<String,Object>>();
		AdminEntity admin = adminDao.checkLogin(userName,password);
		if (admin == null) {
			result.setErrorMessage("用户名或密码错误，请重新输入",ErrorCodeNo.SYS022);
			return result;
		} else {
			admin.setPassword("");
			session.setAttribute(Constants.SES_LOGIN_USER, admin);
			Map<String,Object> map = new HashMap<String,Object>();
			map.put("userName", admin.getUserName());
			map.put("userId", admin.getId());
			result.setSuccessData(map);
			return result;
		}
	}
}
