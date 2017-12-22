package cn.ce.platform_service.interceptors;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import cn.ce.platform_service.common.Constants;
import cn.ce.platform_service.common.ErrorCodeNo;
import cn.ce.platform_service.common.Result;
import cn.ce.platform_service.common.Status;
import cn.ce.platform_service.common.gateway.ApiCallUtils;
import cn.ce.platform_service.users.entity.User;
import cn.ce.platform_service.util.PropertiesUtil;
import io.netty.handler.codec.http.HttpMethod;

/**
 * 
 * 
 * @ClassName:  LoginInterceptor   
 * @Description:用户登录拦截
 * @author: makangwei 
 * @date:   2017年11月21日 下午9:29:06   
 * @Copyright: 2017 中企动力科技股份有限公司 © 1999-2017 300.cn All Rights Reserved
 *
 */
public class AdminLoginInterceptor extends HandlerInterceptorAdapter {

	
	   /** 日志对象 */
     private static Logger _LOGGER = Logger.getLogger(AdminLoginInterceptor.class);
 
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
        
    	//用户数据拿不到。就到用户中心去拿
		String ticket = request.getHeader("ticket");
		if(StringUtils.isBlank(ticket)){
	       return userNotLogged(response);
		}
//		String url = "http://10.12.40.161:8088/passport/checkTicket";
		String url = PropertiesUtil.getInstance().getValue("checkTicket");
		Map<String,String> headers = new HashMap<String,String>();
		headers.put("ticket", ticket);
		String responseStr = ApiCallUtils.getOrDelMethod(url, headers, HttpMethod.GET);
		try{
			JSONObject job = JSONObject.parseObject(responseStr).getJSONObject("data");
			String uid = job.getString("uid");
			if(0 != job.getIntValue("userType")){
				return userInfoError(response, ErrorCodeNo.SYS013, "用户角色权限不足");
			}
			if(StringUtils.isBlank(uid)){
				return userNotLogged(response);
			}
			if(StringUtils.isBlank(job.getString("enterpriseName"))){
				return userInfoError(response, ErrorCodeNo.SYS028, "用户信息错误");
			}
			job.put("id", uid);
			request.getSession().setAttribute(Constants.SES_LOGIN_USER, 
					job.toJavaObject(User.class));
			return true;
		}catch(Exception e){
			_LOGGER.info("用户中心session已经过期");
			return false;
		}
	}

	private void returnJson(HttpServletResponse response, String json)throws Exception {
		PrintWriter writer = null;
		response.setCharacterEncoding("UTF-8");
		response.setContentType("text/html; charset=utf-8");
		try {
			writer = response.getWriter();
			writer.print(json);

		} catch (IOException e) {
			_LOGGER.error("response error", e);
		} finally {
			if (writer != null)
				writer.close();
		}
	}
	

	public boolean userNotLogged(HttpServletResponse response) throws Exception{
        this.returnJson(response, 
        		JSON.toJSONString(Result.errorResult("用户未登录", ErrorCodeNo.SYS003, null, Status.FAILED)));
        return false;
	}
	
	public boolean userInfoError(HttpServletResponse response, ErrorCodeNo errorCodeNo, String message) throws Exception{
        this.returnJson(response, 
        		JSON.toJSONString(Result.errorResult(message, errorCodeNo, null, Status.FAILED)));
		return false;
	}
	
}
