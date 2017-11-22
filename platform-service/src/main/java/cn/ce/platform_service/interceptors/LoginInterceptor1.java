//package cn.ce.platform_service.interceptors;
//
//import java.io.IOException;
//import java.io.PrintWriter;
//import java.util.Date;
//
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//
//import org.apache.log4j.Logger;
//import org.springframework.web.servlet.ModelAndView;
//import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
//
//import com.alibaba.fastjson.JSON;
//
//import cn.ce.platform_service.common.Constants;
//import cn.ce.platform_service.common.ErrorCodeNo;
//import cn.ce.platform_service.common.Result;
//import cn.ce.platform_service.users.entity.User;
//
///**
// * 
// * @author dingjia
// *
// */
//public class LoginInterceptor extends HandlerInterceptorAdapter {
//	
//	private static long validMilliSecond = 600000;
////	private static int validMilliSecond = 10000;
//	
//	   /** 日志对象 */
//     private static Logger _LOGGER = Logger.getLogger(LoginInterceptor.class);
// 
//	@Override
//	public void afterCompletion(HttpServletRequest request,
//			HttpServletResponse response, Object object, Exception exception)
//			throws Exception {
//		
//	}
//
//	@Override
//	
//	public void postHandle(HttpServletRequest request, HttpServletResponse response,
//			Object object, ModelAndView view) throws Exception {
//		// TODO Auto-generated method stub
//		
//	}
//
//	@Override
//	public boolean preHandle(HttpServletRequest request, HttpServletResponse response,
//			Object object) throws Exception {
//		
//        
//        User user =  (User)request.getSession().getAttribute(Constants.SES_LOGIN_USER);
//        Object t = request.getSession().getAttribute("loginTime");
//        if(t == null){ //如果session没有设置有效期。就设置一个有效期
//        	request.getSession().setAttribute("loginTime", System.currentTimeMillis());
//        }else{ //如果session中有有效期。那就判断有效期是否过期
//        	try{
//        		long loginTime = (long)request.getSession().getAttribute("loginTime");
//        		System.out.println(System.currentTimeMillis()-loginTime);
//        		System.out.println(new Date(loginTime));
//        		if(loginTime < (System.currentTimeMillis()-validMilliSecond)){
//        			_LOGGER.debug("session已经过期");
//                    Result<String> result = new Result<>();
//                    result.setErrorMessage("用户未登录",ErrorCodeNo.SYS003);
//                    this.returnJson(response, JSON.toJSONString(result));
//                    return false;
//        		}
//        	}catch(Exception e){
//        		_LOGGER.info("登录拦截获取日期信息错误");
//        		e.printStackTrace();
//                Result<String> result = new Result<>();
//                result.setErrorMessage("用户未登录",ErrorCodeNo.SYS003);
//                this.returnJson(response, JSON.toJSONString(result));
//                return false;
//        	}
//        }
//        
//        if(user != null){
//        	//设置新的有效时间
//        	request.getSession().setAttribute("loginTime", System.currentTimeMillis()-validMilliSecond);
//        	return true;
//        }else{
//            Result<String> result = new Result<>();
//            result.setErrorMessage("用户未登录",ErrorCodeNo.SYS003);
//            this.returnJson(response, JSON.toJSONString(result));
//    		return false;
//        }
//	}
//
//	private void returnJson(HttpServletResponse response, String json)throws Exception {
//		PrintWriter writer = null;
//		response.setCharacterEncoding("UTF-8");
//		response.setContentType("text/html; charset=utf-8");
//		try {
//			writer = response.getWriter();
//			writer.print(json);
//
//		} catch (IOException e) {
//			_LOGGER.error("response error", e);
//		} finally {
//			if (writer != null)
//				writer.close();
//		}
//	}
//
//}