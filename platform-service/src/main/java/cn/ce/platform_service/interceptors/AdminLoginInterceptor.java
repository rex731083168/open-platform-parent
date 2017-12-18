package cn.ce.platform_service.interceptors;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import cn.ce.platform_service.common.Constants;
import cn.ce.platform_service.common.ErrorCodeNo;
import cn.ce.platform_service.common.Result;
import cn.ce.platform_service.common.gateway.ApiCallUtils;
import cn.ce.platform_service.users.entity.User;
import cn.ce.platform_service.util.PropertiesUtil;
import io.netty.handler.codec.http.HttpMethod;

/**
 * @ClassName:  AdminLoginInterceptor   
 * @Description:后台管理员登录拦截
 * @author: makangwei 
 * @date:   2017年10月20日 上午10:09:27   
 * @Copyright: 2017 中企动力科技股份有限公司 © 1999-2017 300.cn All Rights Reserved
 */
public class AdminLoginInterceptor extends HandlerInterceptorAdapter {

	
	   /** 日志对象 */
  private static Logger logger = Logger.getLogger(LoginInterceptor.class);

	@Override
	public void afterCompletion(HttpServletRequest request,
			HttpServletResponse response, Object object, Exception exception)
			throws Exception {
		
	}

	@Override
	
	public void postHandle(HttpServletRequest request, HttpServletResponse response,
			Object object, ModelAndView view) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response,
			Object object) throws Exception {
		
        User user = null;
        try{
        	user = (User)request.getSession().getAttribute(Constants.SES_LOGIN_USER);  
        }catch(Exception e){
        	//session中没有用户数据
        }
        
        if(user != null){
        	return true;
        }
        
    	//用户数据拿不到。就到用户中心去拿
		String ticket = request.getHeader("ticket");
		String url = PropertiesUtil.getInstance().getValue("checkTicket");
		Map<String,String> headers = new HashMap<String,String>();
		headers.put("ticket", ticket);
		String responseStr = ApiCallUtils.getOrDelMethod(url, headers, HttpMethod.GET);
		try{
			User user1 = JSONObject.parseObject(responseStr)
					.getJSONObject("data")
					.toJavaObject(User.class);
			request.getSession().setAttribute(Constants.SES_LOGIN_USER, user1);
			user = (User)request.getSession().getAttribute(Constants.SES_LOGIN_USER);
		}catch(Exception e){
			logger.info("用户中心session已经过期");
		}
		
    	if(user != null){
    		return true;
    	}
        
        Result<String> result = new Result<>();
        result.setErrorMessage("用户未登录",ErrorCodeNo.SYS003);
        this.returnJson(response, JSON.toJSONString(result));
		return false;
	}

	private void returnJson(HttpServletResponse response, String json)throws Exception {
		PrintWriter writer = null;
		response.setCharacterEncoding("UTF-8");
		response.setContentType("text/html; charset=utf-8");
		try {
			writer = response.getWriter();
			writer.print(json);

		} catch (IOException e) {
			logger.error("response error", e);
		} finally {
			if (writer != null)
				writer.close();
		}
	}
	
	public boolean userNotLogged(HttpServletResponse response) throws Exception{
        Result<String> result = new Result<>();
        result.setErrorMessage("用户未登录",ErrorCodeNo.SYS003);
        this.returnJson(response, JSON.toJSONString(result));
		return false;
	}
	
}
