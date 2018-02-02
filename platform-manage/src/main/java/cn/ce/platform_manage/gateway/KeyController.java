//package cn.ce.platform_manage.gateway;
//
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.stereotype.Controller;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestMethod;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.bind.annotation.ResponseBody;
//
//import cn.ce.platform_service.common.Result;
//
///**
// * @Description : 说明
// * @Author : makangwei
// * @Date : 2017-8-9
// */
//@Controller
//@RequestMapping(value="/key")
//public class KeyController {
//
//	Logger LOGGER = LoggerFactory.getLogger(KeyController.class);
//	
//	
//	@RequestMapping(value="/allowKey",method=RequestMethod.GET,produces="application/json;charset=utf-8")
//	@ResponseBody
//	@Deprecated //"这种授权方式已经不再使用了"
//	public Result<String> allowKey(HttpServletRequest request,HttpServletResponse response,
//			@RequestParam(required=true)String secretKey){
//	
//		LOGGER.info("-----------------api使用者申请api----------------------");
//		
//		//修改数据库，添加到网关
//		//1、修改数据库
//		//2、保存到网关
//		return null;
//	}
//	
//}
