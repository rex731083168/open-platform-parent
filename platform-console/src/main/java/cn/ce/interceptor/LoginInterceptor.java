package cn.ce.interceptor;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.json.JSONObject;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import cn.ce.common.Constants;
import cn.ce.users.controller.LoginController;

/**
 * 
 * @author dingjia
 *
 */
public class LoginInterceptor extends HandlerInterceptorAdapter {

	
	   /** 日志对象 */
     private static Logger logger = Logger.getLogger(LoginController.class);
 
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
		response.setHeader("Access-Control-Allow-Origin", "http://172.23.150.24:8080");
        response.setHeader("Access-Control-Allow-Credentials", "true");
        
		String requestUri = request.getRequestURI();  
        String contextPath = request.getContextPath();  
        String url = requestUri.substring(contextPath.length());  
        
        logger.info("requestUri:"+requestUri);    
        logger.info("contextPath:"+contextPath);    
        logger.info("url:"+url);    
        
        String user =  (String)request.getSession().getAttribute(Constants.SES_LOGIN_USER);  
        if(user != null){
        	return true;
        }
        JSONObject json = new JSONObject();
        json.put("code", "0");
        json.put("message", "未登录");
        this.returnJson(response, json.toString());
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

}
