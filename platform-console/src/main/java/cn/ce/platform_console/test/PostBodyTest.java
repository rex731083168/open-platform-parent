package cn.ce.platform_console.test;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import cn.ce.platform_service.apis.entity.ApiVersion;

/**
* @Description : 说明
* @Author : makangwei
* @Date : 2017年9月22日
*/
@RestController
public class PostBodyTest {

	private static final Logger _LOGGER = LoggerFactory.getLogger(PostBodyTest.class);
	
	@RequestMapping(value="/testPostBody",method=RequestMethod.POST)
	public String testPostBody(@RequestBody String str){
		
		_LOGGER.info(str);
		
		return "aaa";
	}
	
	@RequestMapping(value="/testPostBody1",method=RequestMethod.POST)
	public String testPostBody1(@RequestBody ApiVersion apiVersion){
		
		_LOGGER.info("get in");
		_LOGGER.info(apiVersion.toString());
		
		return "aaa";
	}
	
	@RequestMapping(value="/getGetBody1",method=RequestMethod.GET)
	public String testGetBody(String userName, String password){
		
		_LOGGER.info("get in");
		
		return "aaa";
	}
	
}
