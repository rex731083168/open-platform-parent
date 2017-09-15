package cn.ce.platform_console.test;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import cn.ce.platform_service.common.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

/**
* @Description : swagger测试用例
* @Author : makangwei
* @Date : 2017年9月6日
*/
@Api(value = "swagger-test",basePath="/swagger")
@RestController
@RequestMapping(value="/test")
public class SwaggerTest {	

	
	@RequestMapping(method=RequestMethod.GET,value="/swaggerGet1")
	@ApiOperation(value = "/testGet1", httpMethod = "GET", notes = "测试get请求，带参数") 
	public Result<Map<String,String>> testTest1(
			String userName,
			String password,
			Integer age,
			Date data,
			Double money
			){
		
		Map<String,String> map = new HashMap<String,String>();
		map.put("userName", userName);
		map.put("password", password);
		Result<Map<String,String>> result = new Result<Map<String,String>>();
		result.setData(map);
		result.setSuccessMessage("获取数据成功");
		return result;
	}
	
	@RequestMapping(method=RequestMethod.POST,value="/swaggerPost1",consumes="application/json",produces="application/json")
	@ApiOperation(value="0905testPost1", httpMethod = "POST", notes= "测试post请求，带 参数",consumes="application/json",produces="application/json")
	public Result<Map<String,String>> testPost1(
			@ApiParam(type="body")String userName,
			String password
			){
		
		Map<String,String> map = new HashMap<String,String>();
		map.put("userName", userName);
		map.put("password", password);
		Result<Map<String,String>> result = new Result<Map<String,String>>();
		result.setData(map);
		result.setSuccessMessage("获取数据成功");
		return result;
		
	}
	
	@RequestMapping(method=RequestMethod.POST,value="/swaggerPost2",consumes="application/json")
	@ApiOperation(value="0905testPost2", httpMethod = "POST", notes= "测试post请求，接受对象",consumes="application/json")
	public Result<SwaggerPostEntity1> testPost2(
			@ApiParam(required = true, name = "userName", value = "用户名参数具体描述")SwaggerPostEntity1 swaggerEntity
			){
		Result<SwaggerPostEntity1> result = new Result<SwaggerPostEntity1>();
		result.setData(swaggerEntity);
		result.setSuccessMessage("获取数据成功");
		return result;
	}
	
}
