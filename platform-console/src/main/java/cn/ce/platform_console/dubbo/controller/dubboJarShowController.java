package cn.ce.platform_console.dubbo.controller;

import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import cn.ce.ebiz.framework.result.Result;

/**
* @Description : jar包解析。下载。页面上列表展示等工作
* @Author : makangwei
* @Date : 2018年4月25日
*/
@RestController("dubboJar")
public class dubboJarShowController {

	public Result<?> downLoadJar(@RequestParam(required=true)String mainJarId){
		
		return null;
	}
}

