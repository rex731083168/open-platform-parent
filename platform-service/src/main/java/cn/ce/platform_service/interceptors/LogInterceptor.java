package cn.ce.platform_service.interceptors;

import java.util.Date;
import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

/**
* @Description : 每次调用controller，debug记录当前调用信息，以及调用时间等
* @Author : makangwei
* @Date : 2017年9月14日
*/
public class LogInterceptor extends HandlerInterceptorAdapter{
	
	private static final Logger _LOGGER = LoggerFactory.getLogger(LogInterceptor.class);
	
	private static Long millLong= null;
	/**
	 * 调用controller，记录调用信息
	 */
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		
		millLong = System.currentTimeMillis();
		// TODO 是否将日志信息保存到数据库中
		_LOGGER.debug("_____________>request uri:"+request.getRequestURI());
		_LOGGER.debug("_____________>invoke time:"+new Date());
		_LOGGER.debug("_____________>request method:"+request.getMethod());
		
		// TODO 如何从request中多次读取流信息
		_LOGGER.debug("_____________>request parameters:");
		Enumeration<String> enu=request.getParameterNames();  
		while(enu.hasMoreElements()){  
			String paraName=(String)enu.nextElement();  
			_LOGGER.debug(paraName+": "+request.getParameter(paraName));
		}
		_LOGGER.debug("_____________>log interceptor finished");
		return true;
	}

	/**
	 * This implementation is empty.
	 */
	@Override
	public void postHandle(
			HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView)
			throws Exception {
	}

	/**
	 * This implementation is empty.
	 */
	@Override
	public void afterCompletion(
			HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		
		if(null != millLong){
			_LOGGER.debug("____________>method invoke finished times consumes:"+(System.currentTimeMillis() - millLong));
		}
	}

	/**
	 * This implementation is empty.
	 */
	@Override
	public void afterConcurrentHandlingStarted(
			HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
	}

}
