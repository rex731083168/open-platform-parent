package cn.ce.platform_console.sandbox;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import cn.ce.platform_service.common.Constants;
import cn.ce.platform_service.common.EnvironmentHool;
import cn.ce.platform_service.common.ErrorCodeNo;
import cn.ce.platform_service.common.Result;
import cn.ce.platform_service.common.Status;
import cn.ce.platform_service.gateway.entity.QuerySaasEntity;
import cn.ce.platform_service.gateway.entity.SaasEntity;
import cn.ce.platform_service.gateway.service.ISaasService1;
import cn.ce.platform_service.sandbox.entity.QuerySandBox;
import cn.ce.platform_service.sandbox.entity.SandBox;
import cn.ce.platform_service.sandbox.service.ISandBoxService;
import cn.ce.platform_service.users.entity.User;

/**
* @Description : 沙箱功能
* @Author : makangwei
* @Date : 2018年3月23日
*/
@RestController
@RequestMapping("/sandBox")
public class SandBoxController {
	
	@Resource
	private ISandBoxService sandBoxService;
	@Resource
	private ISaasService1 saasService1;
	
	@RequestMapping(value="/getResourcePools", method=RequestMethod.GET)
	public Result<?> getResourcePool(){
		if(!EnvironmentHool.isSupportBox()){
			return Result.errorResult("环境不支持", ErrorCodeNo.SYS033, null, Status.FAILED);
		}else{
			return sandBoxService.getResourcePool();
		}
	}
	
	@RequestMapping(value="/andBox", method=RequestMethod.POST)
	public Result<?> andBox(HttpServletRequest request, @RequestBody SandBox sandBox){

		if(!EnvironmentHool.isSupportBox()){
			return Result.errorResult("环境不支持", ErrorCodeNo.SYS033, null, Status.FAILED);
		}else{
			User user = (User)request.getSession().getAttribute(Constants.SES_LOGIN_USER);
			sandBox.setUserId(user.getId());
			sandBox.setEnterpriseName(user.getEnterpriseName());
			return sandBoxService.andBox(sandBox);
		}
	}
	
	@RequestMapping(value="/getOne",method=RequestMethod.GET)
	public Result<?> boxList(@RequestParam(required=true)String boxId){
		
		return sandBoxService.getOne(boxId);
	}
	
	@RequestMapping(value="/deleteOne",method=RequestMethod.GET)
	public Result<?> deleteOne(String boxId){
		
		return sandBoxService.deleteOne(boxId);
	}
	
	@RequestMapping(value="/boxList",method=RequestMethod.POST)
	public Result<?> boxList(@RequestBody QuerySandBox queryBox){
		
		return sandBoxService.boxList(queryBox);
	}

	
	@RequestMapping(value="/saveOrUpdateBoxSaas", method=RequestMethod.POST)
	public Result<?> andBoxSaas(HttpServletRequest request,@RequestBody SaasEntity saasEntity){
	
		return saasService1.saveOrUpdateBoxSaas(saasEntity);
	}
	
	@RequestMapping(value="/deleteBoxSaas", method=RequestMethod.DELETE)
	public Result<?> updateRoute(HttpServletRequest request, @RequestParam String routeId){
		
		return saasService1.deleteBoxSaas(routeId);
		
	}
	
	@RequestMapping(value="/getBoxSaas", method=RequestMethod.GET)
	public Result<?> getRoute(HttpServletRequest request,@RequestParam String routeId){
		
		return saasService1.getBoxSaas(routeId);
		
	}
	
	@RequestMapping(value="/sandBoxList", method=RequestMethod.POST)
	public Result<?> routeList(HttpServletRequest request,@RequestBody QuerySaasEntity saas){
		
		return saasService1.boxSaasList(saas);
		
	}
	
}

