package cn.ce.platform_console.apis.controller;

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
import cn.ce.platform_service.apis.service.IConsoleApiService;
import cn.ce.platform_service.common.AuditConstants;
import cn.ce.platform_service.common.Constants;
import cn.ce.platform_service.common.ErrorCodeNo;
import cn.ce.platform_service.common.Result;
import cn.ce.platform_service.users.entity.User;

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
public class ApisController {

	/** 日志对象 */
	private static Logger _LOGGER = Logger.getLogger(ApisController.class);

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
		
		_LOGGER.info(apiEntity.toString());
		
		return consoleApiService.publishApi(user, apiEntity);
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

		// DOTO 多个参数将参数用逗号隔开传入
		String[] apiId = apiIds.split(",");
		
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
		
		return consoleApiService.showApi(apiId);
	}

	/**
	 * @Title: showApiList
	 * @Description: api列表
	 * @author: makangwei
	 * @date:   2017年10月12日 下午1:42:41  
	 */
	@RequestMapping(value="/showApiList",method=RequestMethod.GET)
	public Result<?> showApiList(
			@RequestParam String openApplyId, 
			@RequestParam Integer userType,
			@RequestParam(required=false) String userId,
			@RequestParam(required=false) Integer checkState,
			@RequestParam(required=false) String apiNameLike,
			@RequestParam(required=false,defaultValue= "1") int currentPage, 
			@RequestParam(required=false,defaultValue= "10")int pageSize){
		
		_LOGGER.info("当前开放应用："+openApplyId);
		_LOGGER.info("当前用户类型："+userType);
		_LOGGER.info("当前用户："+userId);
		_LOGGER.info("审核状态："+checkState);
		_LOGGER.info("模糊名称"+apiNameLike);
		
		if(pageSize == 0){
			pageSize = Constants.PAGE_COMMON_SIZE;
		}else if(pageSize > 30){
			pageSize = Constants.PAGE_MAX_SIZE; //最大超过三十
		}
		
		return consoleApiService.showApiList(openApplyId,userId,checkState,apiNameLike,currentPage,pageSize);
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
	public Result<?> checkVersion(String apiId, String version){
		
		Result<String> result = new Result<String>();
		if(StringUtils.isBlank(apiId)){
			result.setMessage("apiId不能为空");
			return result;
		}if(StringUtils.isBlank(version)){
			result.setMessage("version不能为空");
			return result;
		}
		
		return consoleApiService.checkVersion(apiId,version);
	}
		
}
