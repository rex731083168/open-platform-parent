package cn.ce.platform_console.test;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import cn.ce.platform_service.common.Result;
import io.swagger.models.Swagger;
import io.swagger.parser.SwaggerParser;

/**
* @Description : swagger 解析器
* @Author : makangwei
* @Date : 2017年9月25日
*/
@RestController
@RequestMapping(value= "/test" )
public class SwaggerParseTest {

	@RequestMapping(value= "/testSwaggerParser" )
	public Result<?> testSwaggerParser(HttpServletRequest request,HttpServletResponse response,
			@RequestParam String str){

		Swagger swagger = null;
		try{
			swagger = new SwaggerParser().parse(str);
		}catch(Exception e){
//			swagger = new Swagger
		}
		
		
		return null;
	}
	
}
