package cn.ce.test;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import cn.ce.common.Result;

/**
* @Description : 测试代码发生异常是否能够进行拦截并跳转
* @Author : makangwei
* @Date : 2017年9月11日
*/

@RestController
@RequestMapping(value="/test")
public class ExceptionInterceptorTest {

	@RequestMapping(value="/testExceptionInterceptor",method=RequestMethod.GET)
	@ResponseBody
	public Result<String> testExceptionInterceptor(String userName,String password){
		
		System.out.println("Hello world");
		userName = userName.toLowerCase();
		int i = 1/0;
		return new Result<String>();
	}
}
