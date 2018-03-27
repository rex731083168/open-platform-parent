package cn.ce.platform_console.apis.controller;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import cn.ce.platform_service.apis.entity.ApiEntity;
import cn.ce.platform_service.apis.entity.NewApiEntity;
import cn.ce.platform_service.apis.entity.QueryApiEntity;
import cn.ce.platform_service.apis.service.IConsoleApiService;
import cn.ce.platform_service.apis.util.ApiTransform;
import cn.ce.platform_service.common.AuditConstants;
import cn.ce.platform_service.common.Constants;
import cn.ce.platform_service.common.DBFieldsConstants;
import cn.ce.platform_service.common.ErrorCodeNo;
import cn.ce.platform_service.common.Result;
import cn.ce.platform_service.common.Status;
import cn.ce.platform_service.users.entity.User;
import cn.ce.platform_service.util.SplitUtil;
import io.swagger.annotations.ApiOperation;

/**
 * @ClassName:  ApisController   
 * @Description:前台api 控制类
 * @author: makangwei
 * @date:   2017年10月10日 下午8:15:17   
 * @Copyright: 2017 中企动力科技股份有限公司 © 1999-2017 300.cn All Rights Reserved
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
	 * @Description: 提供者发布一个api，这时候还未审核
	 * @author: makangwei
	 * @date:   2017年10月10日 下午8:17:41  
	 */
	@RequestMapping(value = "/publishApi", method = RequestMethod.POST)
	@ApiOperation("发布api_TODO")
//	public Result<?> publishApi(HttpSession session, @RequestBody NewApiEntity apiEntity) {
	public Result<?> publishApi(HttpSession session, @RequestBody ApiEntity entity) {
	
		/**
		 * TODO 下一期改动api定义和api参数
		 */
		User user = (User) session.getAttribute(Constants.SES_LOGIN_USER);
		
		NewApiEntity newApiEntity = ApiTransform.transToTotalNewApi(entity);
		
		_LOGGER.info("publish api Entity:"+newApiEntity.toString());
		
		if(newApiEntity.getCheckState() > AuditConstants.API_CHECK_STATE_UNAUDITED
				|| newApiEntity.getCheckState() < AuditConstants.API_CHECK_STATE_UNCOMMITED){
			return Result.errorResult("api审核状态不可用", ErrorCodeNo.SYS012, null, Status.FAILED);
		}
		
		if(newApiEntity.getApiType() == null || StringUtils.isBlank(newApiEntity.getApiType().toString())){
			return Result.errorResult("api类型必须指定", ErrorCodeNo.SYS008, null, Status.FAILED);
		}
		
		if(StringUtils.isBlank(newApiEntity.getResourceType())){
			return Result.errorResult("api资源类型必须指定", ErrorCodeNo.SYS005, null, Status.FAILED);
		}
		
		if(StringUtils.isBlank(newApiEntity.getResourceTypeName())){
			return Result.errorResult("api资源类型名称必须指定", ErrorCodeNo.SYS005, null, Status.FAILED);
		}		
		
		// TODO 如果请求body不为空校验请求body为可选值
		// TODO 如果返回body不为空校验返回body为固定值
		// TODO 校验protocol 协议必须是http或者https
		
		return consoleApiService.publishApi(user, newApiEntity);
	}

	@RequestMapping(value="/checkListenPath", method= RequestMethod.GET)
	@ApiOperation("校验listenPath")
	public Result<?> checkListenPath(@RequestParam(required=true)String listenPath){
		
		if(StringUtils.isBlank(listenPath)){
			return new Result<String>("listenPath不能为空", ErrorCodeNo.SYS005, null, Status.FAILED);
		}
		if(listenPath.endsWith("?")){
			return new Result<String>("listenPath不能以问号结尾", ErrorCodeNo.SYS005, null, Status.FAILED);
		}
		if(!listenPath.startsWith("/")){
			listenPath = "/"+listenPath;
		}
		
		return consoleApiService.checkListenPath(listenPath);
	}
	
	/**
	 * 
	 * @Description: 提供者提交审核，修改api状态为待审核
	 * @author: makangwei
	 * @date:   2017年10月12日 上午11:23:58  
	 */
	@RequestMapping(value = "/submitApi", method = RequestMethod.POST)
	@ApiOperation("提供者提交审核")
	public Result<?> submitApi(@RequestParam(required=true)String apiIds) {

		_LOGGER.info("多个api提交审核："+apiIds);
		
		List<String> apiId = SplitUtil.splitStringWithComma(apiIds);
		
		return consoleApiService.submitApi(apiId);
	}
	
	/**
	 * @Description: 修改api，由前端控制，未发布到网关才可修改
	 * @author: makangwei
	 * @date:   2017年10月10日 下午8:19:15  
	 */
	@RequestMapping(value="/modifyApi",method=RequestMethod.POST)
	@ApiOperation("api更新_TODO")
//	public Result<?> modifyApi(@RequestBody NewApiEntity apiEntity){
	public Result<?> modifyApi(@RequestBody ApiEntity entity){
		
		NewApiEntity apiEntity = ApiTransform.transToTotalNewApi(entity);
		
//		if(apiEntity.getCheckState() == AuditConstants.API_CHECK_STATE_SUCCESS){	
//			return Result.errorResult("当前状态不支持修改", ErrorCodeNo.SYS012, null, Status.FAILED);
//		}
		
		return consoleApiService.modifyApi(apiEntity);
	}
	
	/**
	 * @Description: 单个api的查询
	 * @author: makangwei
	 * @date:   2017年10月12日 下午1:17:55  
	 */
	@RequestMapping(value = "/showApi", method = RequestMethod.POST)
	@ApiOperation("显示完整的api详情_TODO")
	public Result<?> showApi(@RequestParam(required=true)String apiId) {
		_LOGGER.info("显示当前api："+apiId);
		return consoleApiService.showApi(apiId);
	}

	/**
	 * @Description: 提供者查看api列表
	 * @author: makangwei
	 * @date:   2017年10月12日 下午1:42:41  
	 */
	@RequestMapping(value="/showApiList",method=RequestMethod.POST)
	@ApiOperation("api列表_TODO")
//	public Result<?> showApiList(@RequestBody QueryApiEntity apiEntity){
	public Result<?> showApiList(
			HttpSession session,
			@RequestBody QueryApiEntity apiEntity,
			@RequestParam(required=false,defaultValue= "1") int currentPage, 
			@RequestParam(required=false,defaultValue= "10")int pageSize){
		
		apiEntity.setCurrentPage(currentPage);
		apiEntity.setPageSize(pageSize);
		
		if(apiEntity.getUserType() != null && 
				apiEntity.getUserType() == AuditConstants.USER_PROVIDER){
			//20171214改为如果是提供者，那么提供者的组织不能为空。根据组织来查询api,而不是根据用户id来查询了
			//User user = (User)session.getAttribute(Constants.SES_LOGIN_USER);
			//apiEntity.setUserId(user.getId());
			
		}else{//如果当前是开发者登录，只能查看审核成功的api
			apiEntity.setUserId(null);
			apiEntity.setCheckState(AuditConstants.API_CHECK_STATE_SUCCESS);
		}
		
//		apiEntity.setCurrentPage(PageValidateUtil.checkCurrentPage(apiEntity.getCurrentPage()));
//		apiEntity.setPageSize(PageValidateUtil.checkPageSize(apiEntity.getPageSize()));
//		apiEntity.setStartNum((apiEntity.getCurrentPage()-1)*apiEntity.getPageSize());
		return consoleApiService.showApiList(apiEntity);
	}
	
	/**
	 * @Description: 文档中心api列表
	 * @author: makangwei
	 * @date:   2017年11月14日 下午2:14:13  
	 */
	@RequestMapping(value="/showDocApiList",method=RequestMethod.POST)
	@ApiOperation("api文档中心列表_TODO")
//	public Result<?> showDocApiList(@RequestBody QueryApiEntity apiEntity){
	public Result<?> showDocApiList(
			HttpSession session,
			@RequestBody QueryApiEntity apiEntity,
			@RequestParam(required=false,defaultValue= "1") int currentPage, 
			@RequestParam(required=false,defaultValue= "10")int pageSize){		

		apiEntity.setCurrentPage(currentPage);
		apiEntity.setPageSize(pageSize);
		
		apiEntity.setUserId(null);
		apiEntity.setCheckState(AuditConstants.API_CHECK_STATE_SUCCESS);
		apiEntity.setApiType(DBFieldsConstants.API_TYPE_OPEN);
		
//		apiEntity.setCurrentPage(PageValidateUtil.checkCurrentPage(apiEntity.getCurrentPage()));
//		apiEntity.setPageSize(PageValidateUtil.checkPageSize(apiEntity.getPageSize()));
//		apiEntity.setStartNum((apiEntity.getCurrentPage()-1)*apiEntity.getPageSize());
		return consoleApiService.showDocApiList(apiEntity);
	}
	
	/**
	 * 
	 * @Title: checkApiChName
	 * @Description: 校验api中文名称，当前开放应用内不重复
	 * @author: makangwei
	 * @date:   2018年2月5日 上午11:24:18  
	 */
	@RequestMapping(value="/checkApiChName", method=RequestMethod.POST)
	public Result<?> checkApiChName(@RequestParam String appId,
			@RequestParam String apiChName){
		
		return consoleApiService.checkApiChName(apiChName,appId);
	}
	
	/**
	 * 
	 * @Title: checkVersion
	 * @Description: TODO(这里用一句话描述这个方法的作用)
	 * @author: makangwei
	 * @date:   2018年2月5日 上午11:25:29  
	 */
	@RequestMapping(value="/checkVersion", method=RequestMethod.GET)
	@ApiOperation("校验版本号")
	public Result<?> checkVersion(String versionId, String version){
		
		if(StringUtils.isBlank(versionId)){
			return new Result<String>("apiId不能为空", ErrorCodeNo.SYS005, null, Status.FAILED);
		}if(StringUtils.isBlank(version)){
			return new Result<String>("version不能为空", ErrorCodeNo.SYS005, null, Status.FAILED);
		}
		
		return consoleApiService.checkVersion(versionId,version);
	}
	
	@RequestMapping(value="/getResourceType", method=RequestMethod.GET)
	public Result<?> getResourceType(){
		
		return consoleApiService.getResourceType();
	}
}
