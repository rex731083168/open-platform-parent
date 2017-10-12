package cn.ce.platform_console.diyApply.controller;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;

import cn.ce.platform_service.apis.service.IAPIService;
import cn.ce.platform_service.common.Constants;
import cn.ce.platform_service.common.Result;
import cn.ce.platform_service.common.page.Page;
import cn.ce.platform_service.diyApply.entity.DiyApplyEntity;
import cn.ce.platform_service.diyApply.service.IConsoleDiyApplyService;
import cn.ce.platform_service.users.entity.User;
import io.swagger.annotations.ApiOperation;

/***
 * 
 * 应用集合
 * 
 * @author lida
 * @date 2017年8月23日14:41:46
 *
 */
@RestController
@RequestMapping("/diyApply")
public class DiyApplyController{

	/** 日志对象 */
	private static Logger logger = LogManager.getLogger(DiyApplyController.class);
	
	@Resource
	private IConsoleDiyApplyService consoleDiyApplyService;
	
	@Resource
	private IAPIService apiService;

	@RequestMapping(value = "/findApplyList", method = RequestMethod.POST)
	@ApiOperation(value = "根据条件查询应用列表", httpMethod = "POST", response = Result.class, notes = "根据条件查询应用列表")
	public Result<Page<DiyApplyEntity>> findApplyList(@RequestBody DiyApplyEntity apply,
			@RequestParam(required=false,defaultValue="10") int pageSize,
			@RequestParam(required=false,defaultValue="1") int currentPage) {
		Page<DiyApplyEntity> page = new Page<>(currentPage, 0, pageSize);
		logger.info("findApplyList start,parameter:{},{}",pageSize,currentPage);
		Result<Page<DiyApplyEntity>> result = consoleDiyApplyService.findApplyList(apply, page);
		return result;
	}

	@RequestMapping(value = "/deleteApplyByid", method = RequestMethod.DELETE)
	public Result<String> deleteApplyByid(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "id", required = true) String id) {
		
		logger.info(">>>>> Action by deleteApplyByid start,applyId:" + id);
		
		Result<String> result = new Result<>();
		
		try {
			result = consoleDiyApplyService.deleteApplyByid(id);
		} catch (Exception e) {
			logger.error("saveApply system error,e:" + e.toString());
			result.setErrorMessage("系统错误!");
		} finally {
			logger.info("<<<<< Action by deleteApplyByid end,result:" + JSON.toJSONString(result));
		}
		
		return result;
	}
	
	@RequestMapping(value = "/getApplyByid", method = RequestMethod.GET)
	public Result<DiyApplyEntity> getApplyByid(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "id", required = true) String id,
			@RequestParam(required=false,defaultValue="10") int pageSize,
			@RequestParam(required=false,defaultValue="1") int currentPage) {
		
		logger.info(">>>>> Action by getApplyByid start,applyId:" + id);
		
		Result<DiyApplyEntity> result = new Result<>();
		
		try {
			result = consoleDiyApplyService.getApplyById(id,pageSize,currentPage);
		} catch (Exception e) {
			logger.error("saveApply system error,e:" + e.toString());
			result.setErrorMessage("系统错误!");
		} finally {
			logger.info("<<<<< Action by getApplyByid end,result:" + JSON.toJSONString(result));
		}
		
		return result;
	}

	@RequestMapping(value = "saveApply" , method = RequestMethod.POST)
	public Result<String> saveApply(HttpServletRequest request, HttpServletResponse response,HttpSession session, @RequestBody DiyApplyEntity apply) {
		logger.info(">>>>> Action by saveApply start,parameter:" + JSON.toJSONString(apply));
		Result<String> result = new Result<>();
		
		try {
			User user = (User) session.getAttribute(Constants.SES_LOGIN_USER);
			apply.setUser(user);
			result = consoleDiyApplyService.saveApply(apply);
		} catch (Exception e) {
			logger.error("saveApply system error,e:" + e.toString());
			result.setErrorMessage("系统错误!");
			return result;
		} finally {
			logger.info("<<<<< Action by saveApply end,result:" + JSON.toJSONString(result));
		}
		return result;
	}

}
