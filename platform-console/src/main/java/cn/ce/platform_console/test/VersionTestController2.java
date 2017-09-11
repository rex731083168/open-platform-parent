package cn.ce.platform_console.test;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import cn.ce.platform_service.common.Result;
import cn.ce.platform_service.common.Status;

/**
* @Description : 说明
* @Author : makangwei
* @Date : 2017年8月20日
*/
@RestController
@RequestMapping("/20170821test")
public class VersionTestController2 {

	@RequestMapping(value="/testGet/v1",method=RequestMethod.GET)
	@ResponseBody
	public Result<String> testGet1(){
		
		Result<String> result = new Result<String>();
		result.setStatus(Status.SUCCESS);
		result.setData("get请求，这是版本一，获取成功");
		
		return result;
	}
	
	@RequestMapping(value="/testGet/v2",method=RequestMethod.GET)
	@ResponseBody
	public Result<String> testGet2(HttpServletRequest request,HttpServletResponse response,
			String userName,String password){
		
		Result<String> result = new Result<String>();
		result.setStatus(Status.SUCCESS);
		result.setData("get请求，这是版本二，获取成功,用户名是："+userName+",密码是："+password+"。");
		return result;
	}
	
	@RequestMapping(value="/testPost/v1",method=RequestMethod.POST)
	@ResponseBody
	public Result<String> testPost1(){
		
		Result<String> result = new Result<String>();
		result.setStatus(Status.SUCCESS);
		result.setData("post请求，这是版本一，获取成功");
		
		return result;
	}
	
	@RequestMapping(value="/testPost/v2",method=RequestMethod.POST)
	@ResponseBody
	public Result<String> testPost2(HttpServletRequest request,HttpServletResponse response,
			String userName,String password){
		
		Result<String> result = new Result<String>();
		result.setStatus(Status.SUCCESS);
		result.setData("post请求，这是版本二，获取成功,用户名是："+userName+",密码是："+password+"。");
		return result;
	}
	
}
