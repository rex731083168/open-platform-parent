package cn.ce.platform_console.monitor.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import cn.ce.platform_service.common.Result;
import cn.ce.platform_service.common.gateway.ApiCallUtils;
import cn.ce.platform_service.common.gateway.GatewayUtils;
import cn.ce.platform_service.diyApply.entity.appsEntity.Apps;
import cn.ce.platform_service.diyApply.service.IPlublicDiyApplyService;
import cn.ce.platform_service.gateway.entity.GatewayColonyEntity;
import cn.ce.platform_service.util.PropertiesUtil;
import io.netty.handler.codec.http.HttpMethod;

/**
* @Description : 监控程序。监控服务的正常运行
* @Author : makangwei
* @Date : 2017年12月8日
*/
@RestController
@RequestMapping(value="/monitor")
public class MonitorController {

	@Resource
	private IPlublicDiyApplyService plublicDiyApplyService;
	
	private static final String tyk_header_key = "X-Tyk-Authorization";
	private static final String tyk_header_value = "352d20ee67be67f6340b4c0605b044b7";
	private static String menuMonitor;
	private static Integer currentPage = 1;
	private static Integer pageSize = 10;
	
	static{
		menuMonitor = PropertiesUtil.getInstance().getValue("menuMonitor");
	}
	
	@RequestMapping(value="/gateway", method=RequestMethod.GET)
	public String gateway(){
		
		Map<String, String> map = new HashMap<String, String>();
		map.put(tyk_header_key, tyk_header_value);
		List<GatewayColonyEntity> gwCols = GatewayUtils.getAllGatewayColony();
		// TODO 20171218 mkw gateway修改地址
		GatewayColonyEntity gatewayColonyEntity = gwCols.get(0);
		String resultStr = ApiCallUtils.getOrDelMethod(gatewayColonyEntity.getColUrl()+"/hello", map, HttpMethod.GET);
		
		if(StringUtils.isNotBlank(resultStr)){
			return "ok";
		}else{
			return "fail";
		}
	}
	
	@RequestMapping(value="/productCentre", method=RequestMethod.GET)
	public String productCentre(){
		
		Result<Apps> result = plublicDiyApplyService.findPagedApps(null, null, currentPage, pageSize);
		
		System.out.println(result.toString());
		if(result != null && result.getData() != null){
			return "ok";
		}
		return "fail";
	}
	
	@RequestMapping(value="/menuCentre", method=RequestMethod.GET)
	public String menuCentre(){
		String resultStr = ApiCallUtils.getOrDelMethod(menuMonitor, null, HttpMethod.GET);
		if(resultStr != null){
			return "ok";
		}
		return "fail";
	}
	
	/**
	 * @Description: TODO 20171218 mkw  已经提供mongo的监控接口,后期提供代码中的监控
	 * @author: makangwei
	 * @date:   2017年12月18日 上午10:57:04  
	 */
	@RequestMapping(value="mongoColony", method=RequestMethod.GET)
	public String mongoColony(){
		
		return null;
	}
	
	/**
	 * @Description: TODO 20171218 mkw 已经提供es的监控接口，后期提供代码中的监控
	 * @author: makangwei
	 * @date:   2017年12月18日 上午10:57:41  
	 */
	@RequestMapping(value="es", method=RequestMethod.GET)
	public Result<?> elasticSearch(){
		
		return null;
	}
	
	/**
	 * @Description: TODO 20171218 mkw 已经提供redis接口，后期提供代码的监控
	 * @author: makangwei
	 * @date:   2017年12月18日 上午10:58:38  
	 */
	public Result<?> redisColony(){
		
		return null;
	}


}
