package cn.ce.platform_console.apis.controller;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import cn.ce.platform_service.apis.entity.ApiEntity;
import cn.ce.platform_service.apis.entity.QueryApiEntity;
import cn.ce.platform_service.apis.service.IConsoleApiService;
import cn.ce.platform_service.common.AuditConstants;
import cn.ce.platform_service.common.Constants;
import cn.ce.platform_service.common.ErrorCodeNo;
import cn.ce.platform_service.common.Result;
import cn.ce.platform_service.common.Status;
import cn.ce.platform_service.users.entity.User;
import cn.ce.platform_service.util.PageValidateUtil;
import cn.ce.platform_service.util.SplitUtil;

/**
 * @ClassName:  ApisController   
 * @Description:前台api 控制类
 * @author: makangwei
 * @date:   2017年10月10日 下午8:15:17   
 * @Copyright: 2017 中企动力科技股份有限公司 © 1999-2017 300.cn All Rights Reserved
 *
 */
@RestController
@RequestMapping("/api")
public class ApiController {

	/** 日志对象 */
	private static Logger _LOGGER = Logger.getLogger(ApiController.class);

	@Resource
	private IConsoleApiService consoleApiService;

	/**
	 * 
	 * @Title: publishApi
	 * @Description: 提供者发布一个api，这时候还未审核
	 * @author: makangwei
	 * @date:   2017年10月10日 下午8:17:41  
	 */
	@RequestMapping(value = "/publishApi", method = RequestMethod.POST)
	public Result<?> publishApi(HttpSession session, @RequestBody ApiEntity apiEntity) {
			
		User user = (User) session.getAttribute(Constants.SES_LOGIN_USER);
		
		_LOGGER.info("publish api Entity:"+apiEntity.toString());
		
		if(apiEntity.getCheckState() > AuditConstants.API_CHECK_STATE_UNAUDITED
				|| apiEntity.getCheckState() < AuditConstants.API_CHECK_STATE_UNCOMMITED){
			Result<String> result = new Result<String>();
			result.setErrorMessage("api审核状态不可用", ErrorCodeNo.SYS012);
			return result;
		}
		return consoleApiService.publishApi(user, apiEntity);
	}
	
	@RequestMapping(value="/checkListenPath", method= RequestMethod.GET)
	public Result<?> checkListenPath(String listenPath){
		
		if(StringUtils.isBlank(listenPath)){
			return Result.errorResult("listenPath不能为空", ErrorCodeNo.SYS005, null, Status.FAILED);
		}
		
		if(!listenPath.startsWith("/")){
			listenPath = "/"+listenPath;
		}
		if(!listenPath.endsWith("/")){
			listenPath = listenPath+"/";
		}
		return consoleApiService.checkListenPath(listenPath);
	}
	
	/**
	 * 
	 * @Title: submitApi
	 * @Description: 提供者提交审核，修改api状态为待审核
	 * @author: makangwei
	 * @date:   2017年10月12日 上午11:23:58  
	 */
	@RequestMapping(value = "/submitApi", method = RequestMethod.POST)
	public Result<?> submitApi(@RequestParam String apiIds) {

		_LOGGER.info("多个api提交审核："+apiIds);
		// DOTO 多个参数将参数用逗号隔开传入
		List<String> apiId = SplitUtil.splitStringWithComma(apiIds);
		
		return consoleApiService.submitApi(apiId);
	}
	
	/**
	 * @Title: modifyApi
	 * @Description: 修改api，由前端控制，未发布到网关才可修改
	 * @author: makangwei
	 * @date:   2017年10月10日 下午8:19:15  
	 */
	@RequestMapping(value="/modifyApi",method=RequestMethod.POST)
	public Result<?> modifyApi(@RequestBody ApiEntity apiEntity){
		
		if(apiEntity.getCheckState() > AuditConstants.API_CHECK_STATE_UNCOMMITED){
			Result<String> result = new Result<String>();
			result.setErrorMessage("当前状态不支持修改", ErrorCodeNo.SYS012);
			return result;
		}
		
		return consoleApiService.modifyApi(apiEntity);
	}
	
	/**
	 * @Title: showApi
	 * @Description: 单个api的查询
	 * @author: makangwei
	 * @date:   2017年10月12日 下午1:17:55  
	 */
	@RequestMapping(value = "/showApi", method = RequestMethod.POST)
	public Result<?> showApi(String apiId) {
		_LOGGER.info("显示当前api："+apiId);
		return consoleApiService.showApi(apiId);
	}

	/**
	 * @Title: showApiList
	 * @Description: 提供者查看api列表
	 * @author: makangwei
	 * @date:   2017年10月12日 下午1:42:41  
	 */
	@RequestMapping(value="/showApiList",method=RequestMethod.POST)
	public Result<?> showApiList(
			HttpSession session,
			@RequestBody QueryApiEntity apiEntity,
			@RequestParam(required=false,defaultValue= "1") int currentPage, 
			@RequestParam(required=false,defaultValue= "10")int pageSize){
		
		if(apiEntity.getUserType() != null && 
				apiEntity.getUserType() == AuditConstants.USER__CHECKED_SUCCESS){//如果当前是管理员查询，那么管理员的userIdd不能为空
			
			// 如果是提供者，从session中获取用户信息
			Object userObj = session.getAttribute(Constants.SES_LOGIN_USER);
			if(null == userObj){
				return Result.errorResult("当前用户位登录", ErrorCodeNo.SYS003, null, Status.FAILED);
			}else{
				User user = (User)userObj;
				apiEntity.setUserId(user.getId());
			}
			// TODO 支持根据不同的checkState查询出不同的结果
			
		}else{//如果当前是开发者登录，只能查看审核成功的api
			apiEntity.setUserId(null);
			apiEntity.setCheckState(AuditConstants.API_CHECK_STATE_SUCCESS);
		}
		
		return consoleApiService.showApiList(apiEntity, PageValidateUtil.checkCurrentPage(currentPage), 
				PageValidateUtil.checkPageSize(pageSize));
	}
	
	
	@RequestMapping(value="/checkApiEnName",method=RequestMethod.GET)
	public Result<?> checkApiEnName(HttpServletRequest request,HttpServletResponse response,
			String appId,
			String apiEnName){
		
		return consoleApiService.checkApiEnName(apiEnName,appId);
	}
	
	@RequestMapping(value="/checkApiChName",method=RequestMethod.POST)
	public Result<?> checkApiChName(HttpServletRequest request,HttpServletResponse response,
			@RequestParam String appId,
			@RequestParam String apiChName){
		
		return consoleApiService.checkApiChName(apiChName,appId);
	}
	
	@RequestMapping(value="/checkVersion",method=RequestMethod.GET,produces="application/json;charset=utf-8")
	public Result<?> checkVersion(String versionId, String version){
		
		if(StringUtils.isBlank(versionId)){
			return Result.errorResult("apiId不能为空", ErrorCodeNo.SYS005, null, null);
		}if(StringUtils.isBlank(version)){
			return Result.errorResult("version不能为空", ErrorCodeNo.SYS005, null, null);
		}
		
		return consoleApiService.checkVersion(versionId,version);
	}
		
}
