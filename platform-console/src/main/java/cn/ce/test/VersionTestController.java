package cn.ce.test;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.ce.common.Result;
import cn.ce.common.Status;

/**
* @Description : 测试版本的访问
* @Author : makangwei
* @Date : 2017年8月17日
*/
@Controller
public class VersionTestController {


	@RequestMapping(value="/testVersion/v1",method=RequestMethod.GET,produces="application/json;charset=utf-8")
	@ResponseBody
	public Result<String> testVersion(HttpServletRequest request,HttpServletResponse response){
		
		Result<String> result = new Result<String>();
		
		result.setStatus(Status.SUCCESS);
		result.setData("版本一测试成功");
		
		return result;
	}
	
	@RequestMapping(value="testVersion/v2",method=RequestMethod.GET,produces="application/json;charset=utf-8")
	@ResponseBody
	public Result<String> testVersion2(HttpServletRequest request,HttpServletResponse response){
		
		Result<String> result = new Result<String>();
		result.setStatus(Status.SUCCESS);
		result.setData("版本二测试成功");
		return result;
	}

}
