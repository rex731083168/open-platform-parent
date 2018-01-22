package cn.ce.platform_console.data_migra;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import cn.ce.platform_service.apis.service.IConsoleApiService;
import cn.ce.platform_service.common.Result;
import cn.ce.platform_service.diyApply.service.IConsoleDiyApplyService;
import cn.ce.platform_service.guide.service.IConsoleGuideService;
import cn.ce.platform_service.openApply.service.IConsoleOpenApplyService;
import cn.ce.platform_service.users.service.IConsoleUserService;

/**
* @Description : 说明
* @Author : makangwei
* @Date : 2018年1月17日
*/
@RestController
@RequestMapping("/data/migra")
public class DataMigraController {

	@Resource
	private IConsoleUserService consoleUserService;
	@Resource
	private IConsoleApiService consoleApiService;
	@Resource
	private IConsoleGuideService consoleGuideGuide;
	@Resource
	private IConsoleDiyApplyService consoleDiyApplyService;
	@Resource
	private IConsoleOpenApplyService consoleOpenApplyService;
	
	@RequestMapping(value="/user", method=RequestMethod.GET)
	public Result<?> migraUser(){
		
		return consoleUserService.migraUser();
	}
	
	@RequestMapping(value="/api", method=RequestMethod.GET)
	public Result<?> migraApi(){
		
		return consoleApiService.migraApi();
	}
	
	@RequestMapping(value="/guide", method=RequestMethod.GET)
	public Result<?> migraGuide(){
		
		return consoleGuideGuide.migraGuide();
	}
	
	@RequestMapping(value="/diyApply", method=RequestMethod.GET)
	public Result<?> migraDiyApply(){
		
		return consoleDiyApplyService.migraDiyApply();
	}
	
	@RequestMapping(value="/openApply", method=RequestMethod.GET)
	public Result<?> migraOpenApply(){
		
		return consoleOpenApplyService.migraOpenApply();
	}
	
}
