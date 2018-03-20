package cn.ce.platform_manage.admin;

import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import cn.ce.platform_manage.base.BaseController;
import cn.ce.platform_service.admin.service.IAdminService;
import cn.ce.platform_service.common.ErrorCodeNo;
import cn.ce.platform_service.common.Result;
import cn.ce.platform_service.common.Status;

@RestController
@RequestMapping("/admin")
public class AdminController extends BaseController{

    @Resource
    private IAdminService adminService;
    
    @RequestMapping(value="/login", method=RequestMethod.POST)
	public Result<?> login(HttpSession session,
			@RequestParam(required=true)String userName, 
			@RequestParam(required=true)String password) {
    	
    	if(StringUtils.isBlank(userName) || StringUtils.isBlank(password)){
    		return new Result<String>("用户名或密码不能为空",ErrorCodeNo.SYS005,null,Status.FAILED);
    	}
    	
    	return adminService.login(session,userName,password);
	}
	
    
    
	@RequestMapping(value = "/logOut", method = RequestMethod.POST)
	public Result<String> logOut(HttpSession session) {
		
		session.invalidate();
		return new Result<String>("退出登录成功",ErrorCodeNo.SYS000,null,Status.SUCCESS);
	}
}
