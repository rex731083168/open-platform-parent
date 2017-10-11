package cn.ce.platform_console.openApply.controller;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;

import cn.ce.platform_service.apis.service.IAPIService;
import cn.ce.platform_service.common.Result;
import cn.ce.platform_service.openApply.entity.DevApplyEntity;
import cn.ce.platform_service.openApply.service.IAppService;
import cn.ce.platform_service.page.Page;

@Controller
@RequestMapping("/openApply")
public class openApplyController {

	/** 日志对象 */
	private static Logger logger = Logger.getLogger(openApplyController.class);

	@Resource
	private IAppService appService;
    @Resource
    private IAPIService apiService;

	@RequestMapping(value = "/appList", method = RequestMethod.POST)
	@ResponseBody
	public Result<JSONObject> appList(HttpServletRequest request,
			HttpServletResponse response) {

		return appService.appList(request,response);
	}
	
	@RequestMapping(value="deleteById",method=RequestMethod.POST)
	@ResponseBody
	public Result<String> deleteById(HttpServletRequest request,HttpServletResponse response,
			String appId){
		
		return appService.deleteById(appId);
	}
	
	@RequestMapping(value = "/addGroup", method = RequestMethod.POST)
	@ResponseBody
	public Result<String> addGroup(HttpSession session,
			@RequestBody DevApplyEntity app) {
		logger.info("---------------->> Action Add new Group! app details: " + JSON.toJSONString(app));
		
		return appService.addGroup(session,app);
	}
	
	@RequestMapping(value = "/delGroup", method = RequestMethod.GET)
	@ResponseBody
	public Result<String> delGroup(HttpServletRequest request, HttpServletResponse response, String appId) {
		logger.info("---------------->> Action del Group! GroupID: " + appId);
		
		return appService.delGroup(appId);
	}
	
	@RequestMapping(value = "/modifyGroup", method = RequestMethod.POST)
	@ResponseBody
	public Result<String> modifyGroup(@RequestBody DevApplyEntity app) {
		logger.info("---------------->> Action modify Group! Param: " + JSON.toJSONString(app));
		
		return appService.modifyGroup(app);
	}
	
	@RequestMapping(value = "/groupList", method = RequestMethod.POST)
	@ResponseBody
	public Result<Page<DevApplyEntity>> groupList(HttpServletRequest request, HttpServletResponse response, String userId,
			@RequestParam(required = false, defaultValue = "1") int currentPage,
			@RequestParam(required = false, defaultValue = "8") int pageSize) {
		logger.info("---------------->> Action Add new Group! userid: " + userId);
	
		return appService.groupList(userId,currentPage,pageSize);
	}

	@RequestMapping(value = "/submitVerify", method = RequestMethod.POST)
	@ResponseBody
	public Result<String> submitVerify(HttpServletRequest request, HttpServletResponse response, String id) {
		logger.info("---------------->> Action modify Group! GroupID: " + id + "  + id");

		return appService.submitVerify(id);
	}
}
