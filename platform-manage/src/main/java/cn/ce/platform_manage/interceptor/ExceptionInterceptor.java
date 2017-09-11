package cn.ce.platform_manage.interceptor;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSON;

import cn.ce.platform_service.common.ErrorCodeNo;
import cn.ce.platform_service.common.Result;

/**
* @Description : 在所有结果返回之前对返回结果进行判断，进行异常拦截和错误拦截
* @Author : makangwei
* @Date : 2017年9月11日
*/
public class ExceptionInterceptor implements HandlerInterceptor{

	private static Logger _LOGGER = LoggerFactory.getLogger(ExceptionInterceptor.class);
	
	/**
	 * controller执行前调用此方法
	 * 返回true表示继续执行，返回false中止执行
	 */
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		// TODO Auto-generated method stub
		return true;
	}

	/**
	 * controller执行后但未返回视图前调用此方法
	 * 这里可在返回用户前对模型数据进行加工处理，比如这里加入公用信息以便页面显示
	 * 前后端分离用不到渲染视图
	 */

	@Deprecated
	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		// TODO Auto-generated method stub
		
	}

	/**
	 * controller执行后且视图返回后调用此方法
	 * 这里可得到执行controller时的异常信息
	 * 这里可记录操作日志，资源清理等
	 */
	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex){
		
		//异常处理
		if(null != ex){
			
			_LOGGER.error("error happens in location "+request.getRequestURI()); //这里不打印日志堆栈是因为springmvc还会打印一次
			
			try {
				Result<String> result = new Result<String>();
				result.setErrorMessage("程序内部异常", ErrorCodeNo.SYS004);
				this.returnJson(response, JSON.toJSONString(result));	
			} catch (Exception e) {
				
				_LOGGER.error("error happens when handle errors occured in return "+","+e.getMessage(), e);
			}
		}
	}
	
	//返回响应和数据设置
	private void returnJson(HttpServletResponse response, String jsonStr)throws Exception {
		PrintWriter writer = null;
		response.setCharacterEncoding("UTF-8");
		response.setContentType("application/json; charset=utf-8");
		try {
			writer = response.getWriter();
			writer.print(jsonStr);

		} catch (IOException e) {
			_LOGGER.error("response error", e);
		} finally {
			if (writer != null)
				writer.close();
		}
	}
}
