package cn.ce.admin.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.ce.admin.entity.AdminEntity;
import cn.ce.admin.service.IAdminService;
import cn.ce.base.BaseController;
import cn.ce.common.Constants;
import cn.ce.common.Result;

@Controller
public class AdminController extends BaseController{

	   /** 日志操作对象 */
    private static Logger logger = Logger.getLogger(AdminController.class);

    /** 管理员服务 */
    @Autowired
    private IAdminService adminService;
    
    @RequestMapping(value="admin/login", method=RequestMethod.POST)
	 @ResponseBody
	public Result<Map<String,Object>> login(HttpServletRequest request, HttpServletResponse response,
			Model model, String usertype, String username, String password) {
		
    	Result<Map<String,Object>> result = new Result<Map<String,Object>>();
    	
    	if(StringUtils.isBlank(username) || StringUtils.isBlank(password)){
    		result.setErrorMessage("用户名或密码不能为空");
    		return result;
    	}
    	
		AdminEntity admin = adminService.checkLogin(username,password);

		if (admin == null) {
			result.setErrorMessage("用户名或密码错误，请重新输入");
			return result;
		} else {
			HttpSession session = request.getSession();
			session.setAttribute(Constants.SES_LOGIN_USER, admin);
			result.setSuccessMessage("登录成功");
			
			Map<String,Object> map = new HashMap<String,Object>();
			map.put("userName", admin.getUserName());
			map.put("userId", admin.getId());
			result.setData(map);
			return result;
		}
	}
	
	@RequestMapping(value = "admin/logout", method = RequestMethod.POST)
	@ResponseBody
	public String logout(HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		logger.info("---------->> Action for logout");
		JSONObject result = new JSONObject();
		try {
			session.invalidate();
			result.put("code", Constants.SUCCESS);
			result.put("message", Constants.RESULT_OK);
			return result.toString();

		} catch (Exception e) {
			e.printStackTrace();
			result.put("code", Constants.SUCCESS);
			result.put("message", Constants.RESULT_ERROR);
			return result.toString();
		}
	}
}
