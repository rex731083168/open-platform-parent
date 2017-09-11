package cn.ce.platform_console.test;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.ce.platform_service.common.ErrorCodeNo;
import cn.ce.platform_service.common.Result;
import cn.ce.platform_service.common.Status;

/**
 *
 * @author makangwei
 * 2017-8-2
 */
@Controller
public class VisitControlTest {

	//post/json/无参数
	@RequestMapping(value="/test/testPost1", method=RequestMethod.POST)
	@ResponseBody
	public Result<Map<String,String>> testPost1(){
		
		Result<Map<String,String>> result = new Result<Map<String,String>>();
		

		Map<String,String> map = new HashMap<String,String>();
		map.put("post1", "aaa");
		map.put("post2", "bbb");
		
		result.setData(map);
		result.setStatus(Status.SUCCESS);
		
		return result;
	}
	
	//post/json/有参数
	@RequestMapping(value="/test/testPost2", method=RequestMethod.POST,consumes="application/json")
	@ResponseBody
	public Result<Map<String,String>> testPost2(HttpServletRequest request,String userName,String password){
		
		Result<Map<String,String>> result = new Result<Map<String,String>>();
		

		Map<String,String> map = new HashMap<String,String>();
		map.put("username", userName);
		map.put("password", password);
		
		result.setData(map);
		result.setErrorCode(ErrorCodeNo.SYS000);
		result.setMessage("正常返回");
		result.setStatus(Status.SUCCESS);
		
		return result;
	}
	
	@RequestMapping(value="/test/testPost3", method=RequestMethod.POST,consumes="application/json")
	@ResponseBody
	public Result<Map<String,String>> testPost3(HttpServletRequest request,String userName,String password){
		
		
		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		Result<Map<String,String>> result = new Result<Map<String,String>>();

		Map<String,String> map = new HashMap<String,String>();
		map.put("username", userName);
		map.put("password", password);
		
		result.setData(map);
		result.setErrorCode(ErrorCodeNo.SYS000);
		result.setMessage("正常返回");
		result.setStatus(Status.SUCCESS);
		
		return result;
	}
	
	@RequestMapping(value="/test/testPost4", method=RequestMethod.POST,consumes="application/json")
	@ResponseBody
	public Result<Map<String,String>> testPost4(HttpServletRequest request,String userName,String password){
		
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		Result<Map<String,String>> result = new Result<Map<String,String>>();

		Map<String,String> map = new HashMap<String,String>();
		map.put("username", userName);
		map.put("password", password);
		
		result.setData(map);
		result.setErrorCode(ErrorCodeNo.SYS000);
		result.setMessage("正常返回");
		result.setStatus(Status.SUCCESS);
		
		return result;
	}
	
	@RequestMapping(value="/test1", method=RequestMethod.GET)
	@ResponseBody
	public Map<String,String> test1(){
		
		Map<String,String> map = new HashMap<String,String>();
		map.put("username", "changxuezheng");
		map.put("truename", "常学征");

		return map;
	}
	//get/无参数
	@RequestMapping(value="/test/testGet1", method=RequestMethod.GET)
	@ResponseBody
	public Map<String,String> testGet1(){
		
		Map<String,String> map = new HashMap<String,String>();
		map.put("username", "changxuezheng");
		map.put("truename", "常学征");

		return map;
	}
	
	@RequestMapping(value="/test/testGet2", method=RequestMethod.GET)
	@ResponseBody
	public Map<String,String> testGet2(){
		
		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		Map<String,String> map = new HashMap<String,String>();
		map.put("username", "changxuezheng");
		map.put("truename", "常学征");

		return map;
	}
	
	@RequestMapping(value="/test/testGet3", method=RequestMethod.GET)
	@ResponseBody
	public Map<String,String> testGet3(){
		
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		Map<String,String> map = new HashMap<String,String>();
		map.put("username", "changxuezheng");
		map.put("truename", "常学征");

		return map;
	}
	
	
	//get/有参数
	@RequestMapping(value="/test/testGet4", method=RequestMethod.GET)
	@ResponseBody
	public Map<String,String> testGet4(HttpServletRequest request,String userName,String password){
		
		Map<String,String> map = new HashMap<String,String>();
		map.put("username", userName);
		map.put("truename", password);

		return map;
	}
	
	@RequestMapping(value="/test/testGet5", method=RequestMethod.GET)
	@ResponseBody
	public Map<String,String> testGet5(HttpServletRequest request,String userName,String password){
		
		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		Map<String,String> map = new HashMap<String,String>();
		map.put("username", userName);
		map.put("truename", password);

		return map;
	}
	
	@RequestMapping(value="/test/testGet6", method=RequestMethod.GET)
	@ResponseBody
	public Map<String,String> testGet6(HttpServletRequest request,String userName,String password){
		
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		Map<String,String> map = new HashMap<String,String>();
		map.put("username", userName);
		map.put("truename", password);

		return map;
	}
}
