package cn.ce.platform_service.interceptors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import cn.ce.platform_service.common.DataSourceEnum;
import cn.ce.platform_service.dbSwitch.DataSourceTypeManager;

/**
 * @Description : 每次调用controller，debug记录当前调用信息，以及调用时间等
 * @Author : makangwei
 * @Date : 2017年9月14日
 */
public class DbInterceptor extends HandlerInterceptorAdapter {

	private static final Logger _LOGGER = LoggerFactory
			.getLogger(DbInterceptor.class);

	/**
	 * 调用controller，记录调用信息
	 */
	@Override
	public boolean preHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler) throws Exception {

		_LOGGER.debug("_____________>db interceptor Started");
		String flag = request.getParameter("flag");
		_LOGGER.debug("_____________>db flag :" + "========" + flag
				+ "========");
		if ("0".equals(flag)) {
			DataSourceTypeManager.set(DataSourceEnum.SANDBOX);
		} else {
			DataSourceTypeManager.set(DataSourceEnum.PROD);

		}
		_LOGGER.debug("_____________>db interceptor finished");
		return true;
	}

	/**
	 * This implementation is empty.
	 */
	@Override
	public void postHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		DataSourceTypeManager.set(DataSourceEnum.PROD);
	}

	/**
	 * This implementation is empty.
	 */
	@Override
	public void afterCompletion(HttpServletRequest request,
			HttpServletResponse response, Object handler, Exception ex)
			throws Exception {

	}

	/**
	 * This implementation is empty.
	 */
	@Override
	public void afterConcurrentHandlingStarted(HttpServletRequest request,
			HttpServletResponse response, Object handler) throws Exception {
	}

}
