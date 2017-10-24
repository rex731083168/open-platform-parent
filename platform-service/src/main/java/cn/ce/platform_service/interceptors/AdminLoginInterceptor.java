package cn.ce.platform_service.interceptors;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.alibaba.fastjson.JSON;

import cn.ce.platform_service.admin.entity.AdminEntity;
import cn.ce.platform_service.common.Constants;
import cn.ce.platform_service.common.ErrorCodeNo;
import cn.ce.platform_service.common.Result;

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
		
//		String requestUri = request.getRequestURI();  
//     String contextPath = request.getContextPath();  
//     String url = requestUri.substring(contextPath.length());  
//     
//     //logger.info("requestUri:"+requestUri);    
//     //logger.info("contextPath:"+contextPath);  
//     //logger.info("url:"+url);    
		
		
		//TODO 后期修改
		if(StringUtils.contains(request.getRequestURI(), "/route/")){
			return true;
		}
     
		AdminEntity admin = (AdminEntity) request.getSession().getAttribute(Constants.SES_LOGIN_USER);
		if (admin != null) {
			return true;
		}
		Result<String> result = new Result<>();
		result.setErrorCode(ErrorCodeNo.SYS003);
		result.setErrorMessage("用户未登录");
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

}
