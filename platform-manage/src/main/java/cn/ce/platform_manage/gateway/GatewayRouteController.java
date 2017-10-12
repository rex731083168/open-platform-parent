package cn.ce.platform_manage.gateway;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.ce.platform_service.common.Result;
import cn.ce.platform_service.common.gateway.GatewayUtils;

/***
 * 
 * 
 * @ClassName:  GatewayRouteController   
 * @Description:网关路由控制类   
 * @author: author 
 * @date:   2017年10月11日 下午5:53:11   
 * @Copyright: 2017 中企动力科技股份有限公司 © 1999-2017 300.cn All Rights Reserved
 *
 */
@Controller
@RequestMapping("/route")
public class GatewayRouteController {
	
	Logger LOGGER = LoggerFactory.getLogger(GatewayRouteController.class);
	
	
	/***
	 * 
	 * @Title: getRouteBySaasId
	 * @Description: 根据saasId获取路由信息
	 * @param : @param saasId
	 * @param : @return
	 * @return: Result<String>
	 * @throws
	 */
	@RequestMapping(value="/saas/{saasId}", method=RequestMethod.GET)
	@ResponseBody
	public String getRouteBySaasId(HttpServletRequest request,@PathVariable("saasId") String saasId){
		Result<String> result = new Result<>();
		if(StringUtils.isBlank(saasId)){
			result.setErrorMessage("saasId不能为空!");
			return result.toString();
		}
		String routeBySaasId = GatewayUtils.getRouteBySaasId(saasId);
		return routeBySaasId;
	}
	
	/***
	 * 
	 * @Title: getRouteBySaasId
	 * @Description: 新增路由信息
	 * @param : @param saasId
	 * @param : @return
	 * @return: Result<String>
	 * @throws
	 */
	@RequestMapping(value="/saas/{saasId}", method = { RequestMethod.POST, RequestMethod.PUT }, consumes="application/json",produces="application/json;charset=utf-8")
	public String saveRoute(HttpServletRequest request,@PathVariable("saasId") String saasId){
		Result<String> result = new Result<>();
		String targetUrl = request.getParameter("target_url");
		if(StringUtils.isBlank(targetUrl)){
			result.setErrorMessage("targetUrl不能为空!");
			return result.toString();
		}
		
		if(StringUtils.isBlank(saasId)){
			result.setErrorMessage("saasId不能为空!");
			return result.toString();
		}
		
		String routeBySaasId = GatewayUtils.saveRoute(saasId, targetUrl,request.getMethod());
		return routeBySaasId;
	}
	
	
	/***
	 * 
	 * @Title: deleteRoute
	 * @Description: 根据saasId删除路由信息
	 * @param : @param request
	 * @param : @param saasId
	 * @param : @return
	 * @return: Result<String>
	 * @throws
	 */
	@RequestMapping(value="/saas/{saasId}", method=RequestMethod.DELETE,consumes="application/json",produces="application/json;charset=utf-8")
	public String deleteRoute(HttpServletRequest request,@PathVariable("saasId") String saasId){
		Result<String> result = new Result<>();
		if(StringUtils.isBlank(saasId)){
			result.setErrorMessage("saasId不能为空!");
			return result.toString();
		}
		String routeBySaasId = GatewayUtils.deleteRoute(saasId);
		return routeBySaasId;
	}
	
	
}
