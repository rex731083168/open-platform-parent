package cn.ce.apply.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;

import cn.ce.apis.service.IAPIService;
import cn.ce.apply.entity.ApplyEntity;
import cn.ce.apply.service.IApplyService;
import cn.ce.common.Constants;
import cn.ce.common.Result;
import cn.ce.page.Page;
import cn.ce.users.entity.User;
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
@RequestMapping("/apply")
public class ApplyController{

	/** 日志对象 */
	private static Logger logger = Logger.getLogger(ApplyController.class);

	@Autowired @Qualifier("applyService") private IApplyService applyService;
	
	@Autowired @Qualifier("apiService") private IAPIService apiService;

	@RequestMapping(value = "/findApplyList", method = RequestMethod.POST)
	@ApiOperation(value = "根据条件查询应用列表", httpMethod = "POST", response = Result.class, notes = "根据条件查询应用列表")
	public Result<Page<ApplyEntity>> findApplyList(HttpServletRequest request, HttpServletResponse response,
			@RequestBody ApplyEntity apply,
			@RequestParam(required=false,defaultValue="10") int pageSize,
			@RequestParam(required=false,defaultValue="1") int currentPage) {
		logger.info(">>>>> Action by fidApplyList start,parameter:" + JSON.toJSONString(apply));
		Result<Page<ApplyEntity>> result = new Result<>();
		try {
			result = applyService.findApplyList(apply, currentPage, pageSize);
		} catch (Exception e) {
			logger.error("saveApply system error,e:" + e.toString());
			result.setErrorMessage("系统错误!");
		} finally {
			logger.info("<<<<< Action by findApplyList end,result:" + JSON.toJSONString(result));
		}
		return result;
	}

	@RequestMapping(value = "/deleteApplyByid", method = RequestMethod.DELETE)
	public Result<String> deleteApplyByid(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "id", required = true) String id) {
		
		logger.info(">>>>> Action by deleteApplyByid start,applyId:" + id);
		
		Result<String> result = new Result<>();
		
		try {
			result = applyService.deleteApplyByid(id);
		} catch (Exception e) {
			logger.error("saveApply system error,e:" + e.toString());
			result.setErrorMessage("系统错误!");
		} finally {
			logger.info("<<<<< Action by deleteApplyByid end,result:" + JSON.toJSONString(result));
		}
		
		return result;
	}
	
	@RequestMapping(value = "/getApplyByid", method = RequestMethod.GET)
	public Result<ApplyEntity> getApplyByid(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "id", required = true) String id,
			@RequestParam(required=false,defaultValue="10") int pageSize,
			@RequestParam(required=false,defaultValue="1") int currentPage) {
		
		logger.info(">>>>> Action by getApplyByid start,applyId:" + id);
		
		Result<ApplyEntity> result = new Result<>();
		
		try {
			result = applyService.getApplyById(id,pageSize,currentPage);
		} catch (Exception e) {
			logger.error("saveApply system error,e:" + e.toString());
			result.setErrorMessage("系统错误!");
		} finally {
			logger.info("<<<<< Action by getApplyByid end,result:" + JSON.toJSONString(result));
		}
		
		return result;
	}

	@RequestMapping(value = "saveApply" , method = RequestMethod.POST)
	public Result<String> saveApply(HttpServletRequest request, HttpServletResponse response,HttpSession session, @RequestBody ApplyEntity apply) {
		logger.info(">>>>> Action by saveApply start,parameter:" + JSON.toJSONString(apply));
		Result<String> result = new Result<>();
		
		try {
			User user = (User) session.getAttribute(Constants.SES_LOGIN_USER);
			apply.setUser(user);
			result = applyService.saveApply(apply);
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
