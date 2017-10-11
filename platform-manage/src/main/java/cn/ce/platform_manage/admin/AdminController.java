package cn.ce.platform_manage.admin;

import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import cn.ce.platform_manage.base.BaseController;
import cn.ce.platform_service.admin.service.IAdminService;
import cn.ce.platform_service.common.Result;

@RestController
@RequestMapping("/admin")
public class AdminController extends BaseController{

    @Resource
    private IAdminService adminService;
    
    @RequestMapping(value="/login", method=RequestMethod.POST)
	public Result<Map<String,Object>> login(HttpSession session,String userName, String password) {
    	
    	if(StringUtils.isBlank(userName) || StringUtils.isBlank(password)){
    		Result<Map<String,Object>> result = new Result<Map<String,Object>>();
    		result.setErrorMessage("用户名或密码不能为空");
    		return result;
    	}
    	return adminService.login(session,userName,password);
	}
	
    
    
	@RequestMapping(value = "/logOut", method = RequestMethod.POST)
	public Result<String> logOut(HttpSession session) {
		
		Result<String> result = new Result<String>();
		session.invalidate();
		result.setSuccessMessage("退出登录成功");
		return result;
	}
}
