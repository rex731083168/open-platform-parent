package cn.ce.test;

import java.util.HashMap;
import java.util.Map;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import cn.ce.common.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

/**
* @Description : swagger测试
* @Author : makangwei
* @Date : 2017年9月6日
*/
@Api(value = "swagger-test")
@RestController
public class SwaggerTest {	

	@RequestMapping(method=RequestMethod.GET,value="/testGet1")
	@ApiOperation(value = "0905testGet1", httpMethod = "GET", notes = "测试get请求，带参数") 
	public Result<Map<String,String>> testTest1(
			@ApiParam(required = true, name = "userName", value = "用户名参数具体描述")String userName,
			@ApiParam(required = true, name = "password", value = "密码参数具体描述")String password
			){
		
		Map<String,String> map = new HashMap<String,String>();
		map.put("userName", userName);
		map.put("password", password);
		Result<Map<String,String>> result = new Result<Map<String,String>>();
		result.setData(map);
		result.setSuccessMessage("获取数据成功");
		return result;
	}
	
	@RequestMapping(method=RequestMethod.POST,value="/testPost1")
	@ApiOperation(value="0905testPost1", httpMethod = "POST", notes= "测试post请求，带 参数")
	public Result<Map<String,String>> testPost1(
			@ApiParam(required = true, name = "userName", value = "用户名参数具体描述")String userName,
			@ApiParam(required = true, name = "password", value = "密码参数具体描述")String password
			){
		
		Map<String,String> map = new HashMap<String,String>();
		map.put("userName", userName);
		map.put("password", password);
		Result<Map<String,String>> result = new Result<Map<String,String>>();
		result.setData(map);
		result.setSuccessMessage("获取数据成功");
		return result;
	}
}
