package cn.ce.platform_manage.test;

import java.io.IOException;
import java.time.ZoneId;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.http.HttpMethod;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import cn.ce.platform_service.common.IOUtils;
import cn.ce.platform_service.common.Result;


/**
* @Description : 测试参数接收
* @Author : makangwei
* @Date : 2017年9月14日
*/
@RestController
@RequestMapping("/test")
public class ParameterAccessTest {

	@RequestMapping(value="/testRequestParam",method=RequestMethod.POST)
	public Result<String> getPost(HttpServletRequest request, HttpServletResponse response,
			HttpSession session,
			ZoneId zoneId,
			HttpMethod method,
			@RequestParam(required=false) String userName,
			@RequestParam(required=false) String password){
	
		try {
			ServletInputStream ins = request.getInputStream();
			
			String str = IOUtils.convertStreamToString(ins);
			
			System.out.println(str);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		Result<String> result = new Result<String>();
		
		result.setMessage("OK");
		return result;
	}
	
	@RequestMapping(value="/testPostObjectWithArray",method=RequestMethod.POST)
	public Result<String> getObjectWithArrayParam(@RequestBody Person persion){
		
		Result<String> result = new Result<String>();
		
		return result;
	}
}
