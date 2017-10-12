package cn.ce.platform_console.openApply.controller;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;

import cn.ce.platform_service.common.Result;
import cn.ce.platform_service.openApply.entity.OpenApplyEntity;
import cn.ce.platform_service.openApply.service.IConsoleOpenApplyService;

@RestController
@RequestMapping("/openApply")
public class OpenApplyController {

	/** 日志对象 */
	private static Logger logger = Logger.getLogger(OpenApplyController.class);

	@Resource
	private IConsoleOpenApplyService consoleOpenApplyService;

	@RequestMapping(value = "/applyList", method = RequestMethod.POST)
	public Result<?> appList(HttpServletRequest request,
			HttpServletResponse response) {

		return consoleOpenApplyService.applyList(request,response);
	}
	
	@RequestMapping(value="deleteApplyById",method=RequestMethod.POST)
	@ResponseBody
	public Result<?> deleteById(HttpServletRequest request,HttpServletResponse response,
			String appId){
		
		return consoleOpenApplyService.deleteApplyById(appId);
	}
	
	@RequestMapping(value = "/addApply", method = RequestMethod.POST)
	@ResponseBody
	public Result<?> addGroup(HttpSession session,
			@RequestBody OpenApplyEntity apply) {
		logger.info("---------------->> Action Add new Group! openApply details: " + JSON.toJSONString(apply));
		
		return consoleOpenApplyService.addApply(session,apply);
	}
	
	
	@RequestMapping(value = "/modifyApply", method = RequestMethod.POST)
	@ResponseBody
	public Result<?> modifyGroup(@RequestBody OpenApplyEntity openApply) {
		logger.info("---------------->> Action modify Group! Param: " + JSON.toJSONString(openApply));
		
		return consoleOpenApplyService.modifyApply(openApply);
	}
	
	@RequestMapping(value = "/applyList", method = RequestMethod.POST)
	@ResponseBody
	public Result<?> groupList(HttpServletRequest request, HttpServletResponse response, String userId,
			@RequestParam(required = false, defaultValue = "1") int currentPage,
			@RequestParam(required = false, defaultValue = "8") int pageSize) {
		logger.info("---------------->> Action Add new Group! userid: " + userId);
	
		return consoleOpenApplyService.groupList(userId,currentPage,pageSize);
	}

	@RequestMapping(value = "/submitVerify", method = RequestMethod.POST)
	@ResponseBody
	public Result<?> submitVerify(HttpServletRequest request, HttpServletResponse response, String id) {
		logger.info("---------------->> Action modify Group! GroupID: " + id + "  + id");

		return consoleOpenApplyService.submitVerify(id);
	}
}
