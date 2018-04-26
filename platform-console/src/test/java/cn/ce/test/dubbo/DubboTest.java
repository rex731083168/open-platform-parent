//package cn.ce.platform_console.dubbo.controller;
//
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestMethod;
//import org.springframework.web.bind.annotation.RestController;
//import org.springframework.web.multipart.MultipartFile;
//
//import cn.ce.platform_service.common.Result;
//
///**
//* @Description : 说明
//* @Author : makangwei
//* @Date : 2018年3月12日
//*/
//@RestController
//@RequestMapping("test")
//public class DubboTest {
//
//	private static final Logger _LOGGER = LoggerFactory.getLogger(DubboTest.class);
//	
//	@RequestMapping(value="uploadJar", method=RequestMethod.POST)
//	public Result<?> testUploadJar(MultipartFile[] files, HttpServletRequest request, HttpServletResponse response){  
//		
//		for (MultipartFile upFile : files) {
//			// TODO 同一批上传的jar包有一个共同的字段值
//			String fileName = upFile.getOriginalFilename();
//			_LOGGER.info(fileName);
//		}
//		
//		return null;
//	}
//}
//
