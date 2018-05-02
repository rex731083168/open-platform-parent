package cn.ce.platform_console.dubbo.controller;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import cn.ce.platform_service.common.Result;
import cn.ce.platform_service.dubbapply.service.IJarShowService;

/**
* @Description : jar包解析。下载。页面上列表展示等工作
* @Author : makangwei
* @Date : 2018年4月25日
*/
@RestController("dubboJar")
public class dubboJarShowController {

	@Resource
	private IJarShowService jarShowService;
	
	@RequestMapping(value="/downLoadJar", method=RequestMethod.GET)
	public Result<?> downLoadJar(@RequestParam(required=true)String mainJarId,
			HttpServletResponse response){
		
		return jarShowService.downLoadJar(mainJarId,response);
	}
	
	@RequestMapping(value="/parseMainJar",method=RequestMethod.POST)
	public Result<?> parseMainJar(@RequestParam(required=true)String mainJarId){
		
		return jarShowService.parseMainJar(mainJarId);
		
	}
	
	@RequestMapping(value="/getMainJarList", method=RequestMethod.GET)
	public Result<?> getMainJarList(String originalFileName,
			Integer currentPage, Integer pageSize){
		
		return jarShowService.getMainJarList(originalFileName,currentPage,pageSize);
	}
	
	@RequestMapping(value="/getDepJarList", method=RequestMethod.GET)
	public Result<?> getDepJarList(@RequestParam(required=true)String mainJarId,String originalFileName,
			Integer currentPage,Integer pageSize){
		
		return jarShowService.getDepJarList(mainJarId,originalFileName,currentPage,pageSize);
		
	}
	
	public Result<?> getMainJarParsedMethods(String mainJarId,
			String methodName,Integer currentPage,Integer pageSize){
		
		return jarShowService.getMainJarParsedMethods(mainJarId,
				methodName,currentPage,pageSize);
	}
	
}

