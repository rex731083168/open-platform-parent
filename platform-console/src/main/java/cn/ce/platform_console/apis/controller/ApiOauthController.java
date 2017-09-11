package cn.ce.platform_console.apis.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import cn.ce.platform_service.apis.entity.ApiAuditEntity;
import cn.ce.platform_service.apis.service.IApiOauthService;
import cn.ce.platform_service.common.Result;
import cn.ce.platform_service.page.Page;

/**
* @Description : api审核管理
* @Author : makangwei
* @Date : 2017年8月18日
*/

@RestController
@RequestMapping("/api")
public class ApiOauthController {

	@Autowired
	private IApiOauthService apiOauthService;
	
	private static Logger logger  = LoggerFactory.getLogger(ApiOauthController.class);
	
	//api使用者申请api
	@RequestMapping(value="/applyApi", method=RequestMethod.POST)
	public Result<String> applyApi(HttpServletRequest request,HttpServletResponse response,
			String userId, String apiId,String applyId){
		
		logger.info("------------------api使用者申请api------------------------------");
		logger.info("userId:"+userId);
		logger.info("apiId:"+apiId);
		return apiOauthService.applyApi(userId,apiId,applyId);
		
	}
	
	//api提供者同意api使用者的申请
	@RequestMapping(value="/allowApi",method=RequestMethod.POST)
	public Result<String> allowApi(HttpServletRequest resquest, HttpServletResponse response,
			String authId,Integer checkState,String checkMem){
		logger.info("------------------api提供者同意api使用者的申请----------------------");
		
		return apiOauthService.allowApi(authId,checkState,checkMem);
	}
	
	//api使用者申请的api的列表
	@RequestMapping(value="/useList1",method=RequestMethod.GET)
	public Result<Page<ApiAuditEntity>> getUseList(HttpServletRequest request,
			HttpServletResponse response,
			@RequestParam(required = true) String userId,
			@RequestParam(required = false, defaultValue = "1") int currentPage,
			@RequestParam(required = false, defaultValue = "10") int pageSize){
		
		return apiOauthService.getUseList(userId,currentPage,pageSize);
	}
		
	//api提供者查看api的列表
	@RequestMapping(value="/supplyList1",method=RequestMethod.GET)
	public Result<Page<ApiAuditEntity>> getsupplyList1(HttpServletRequest request,
			HttpServletResponse response,
			@RequestParam(required = true) String userId,
			@RequestParam(required = false, defaultValue = "1") int currentPage,
			@RequestParam(required = false, defaultValue = "10") int pageSize){
			
		return apiOauthService.getsupplyList(userId,currentPage,pageSize);
		
	}
		
	//api使用者删除api使用
	@RequestMapping(value="/deleteAuditEntity", method=RequestMethod.GET)
	@ResponseBody
	public Result<String> deleteById(HttpServletRequest request,HttpServletResponse response,
			@RequestParam(required = true)String auditId){
			
		return apiOauthService.deleteById(auditId);
	}
}
