package cn.ce.platform_manage.gateway;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import cn.ce.platform_service.common.Result;
import cn.ce.platform_service.common.gateway.GatewayRouteUtils;
import cn.ce.platform_service.gateway.service.ISaasService;

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
@RestController
@RequestMapping("/route")
public class GatewayRouteController {
	
	@Resource
	private ISaasService saasService;
	
	Logger LOGGER = LoggerFactory.getLogger(GatewayRouteController.class);
	private static final String DEFAULT_RESTYPE_CODE = "XGW_SERVICE";//官网部门服务 默认值
	
	/***
	 * 
	 * @Title: getRouteBySaasId
	 * @Description: 根据saasId获取路由信息
	 * @param : @param saasId
	 * @param : @return
	 * @return: Result<String>
	 * @throws
	 */
	@RequestMapping(value="/saas/{saasId}/{resourceType}", method=RequestMethod.GET)
	public String getRouteBySaasId(HttpServletRequest request,@PathVariable("saasId") String saasId,
			@PathVariable("resourceType") String resourceType){
		Result<String> result = new Result<>();
		if(StringUtils.isBlank(saasId)){
			result.setErrorMessage("saasId不能为空!");
			return result.toString();
		}
		return saasService.getSaas(saasId,resourceType);
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
	@RequestMapping(value="/saas/{saasId}/{resourceType}", method = { RequestMethod.POST, RequestMethod.PUT },produces="application/json;charset=utf-8")
	public String saveRoute(HttpServletRequest request,@PathVariable("saasId") String saasId,@PathVariable("resourceType") String resourceType,
			@RequestParam(value = "target_url",required = true) String targetUrl){
		String routeBySaasId = GatewayRouteUtils.saveRoute(saasId, targetUrl, resourceType,request.getMethod());
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
	@RequestMapping(value="/saas/{saasId}/{resourceType}", method=RequestMethod.DELETE)
	public String deleteRoute(HttpServletRequest request,@PathVariable("saasId") String saasId,@PathVariable("resourceType") String resourceType){
		String routeBySaasId = GatewayRouteUtils.deleteRoute(saasId,resourceType,request.getMethod());
		return routeBySaasId;
	}
	
	
	
//=======================分割线，下列方法为兼容ResourceType不传递时默认兼容接口=======================
	
	
	
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
	public String getRouteBySaasId(HttpServletRequest request,@PathVariable("saasId") String saasId){
		return this.getRouteBySaasId(request,saasId,DEFAULT_RESTYPE_CODE);
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
	@RequestMapping(value="/saas/{saasId}", method = { RequestMethod.POST, RequestMethod.PUT },produces="application/json;charset=utf-8")
	public String saveRoute(HttpServletRequest request,@PathVariable("saasId") String saasId,@RequestParam(value = "target_url",required = true) String targetUrl){
		return this.saveRoute(request, saasId, DEFAULT_RESTYPE_CODE, targetUrl);
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
	@RequestMapping(value="/saas/{saasId}", method=RequestMethod.DELETE)
	public String deleteRoute(HttpServletRequest request,@PathVariable("saasId") String saasId){
		String routeBySaasId = GatewayRouteUtils.deleteRoute(saasId,DEFAULT_RESTYPE_CODE,request.getMethod());
		return routeBySaasId;
	}
	
}
