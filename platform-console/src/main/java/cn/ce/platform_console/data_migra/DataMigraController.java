package cn.ce.platform_console.data_migra;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import cn.ce.platform_service.common.Result;
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
	
	@RequestMapping(value="/user", method=RequestMethod.GET)
	public Result<String> migraUser(){
		
		return consoleUserService.migraUser();
	}
}
