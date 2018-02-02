package cn.ce.platform_console.openApply.controller;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import cn.ce.platform_service.common.AuditConstants;
import cn.ce.platform_service.common.Constants;
import cn.ce.platform_service.common.Result;
import cn.ce.platform_service.openApply.entity.OpenApplyEntity;
import cn.ce.platform_service.openApply.entity.QueryOpenApplyEntity;
import cn.ce.platform_service.openApply.service.IConsoleOpenApplyService;
import cn.ce.platform_service.users.entity.User;
import io.swagger.annotations.ApiOperation;

/***
 * 
 * 
 * @ClassName:  OpenApplyController   
 * @Description: 提供者 开放应用管理   
 * @author: lida 
 * @date:   2017年10月12日 下午3:13:46   
 * @Copyright: 2017 中企动力科技股份有限公司 © 1999-2017 300.cn All Rights Reserved
 *
 */
@RestController
@RequestMapping("/openApply") 
public class OpenApplyController {

	/** 日志对象 */
//	private static Logger logger = Logger.getLogger(OpenApplyController.class);

	@Resource
	private IConsoleOpenApplyService consoleOpenApplyService;

	/***
	 * 
	 * @Title: appList
	 * @Description: 查询应用列表
	 * @param : @param entity
	 * @param : @param currentPage
	 * @param : @param pageSize
	 * @param : @return
	 * @return: Result<?>
	 * @throws
	 */
	@RequestMapping(value = "/applyList", method = RequestMethod.POST)
	public Result<?> applyList(@RequestBody QueryOpenApplyEntity entity) {
		
		return consoleOpenApplyService.applyList(entity);
	}
	
	/***
	 * 
	 * @Title: getApplyByid
	 * @Description: 根据应用id获取应用详情
	 * @param : @param request
	 * @param : @param response
	 * @param : @param id
	 * @param : @return
	 * @return: Result<?>
	 * @throws
	 */
	@RequestMapping(value = "/getApplyById", method = RequestMethod.GET)
	@ApiOperation("###根据id查询应用")
	public Result<?> getApplyByid(@RequestParam(value = "id", required = true) String id) {
		return consoleOpenApplyService.getApplyById(id);
	}
	
	/***
	 * 
	 * @Title: addApply
	 * @Description: 新增应用
	 * @param : @param session
	 * @param : @param apply
	 * @param : @return
	 * @return: Result<?>
	 * @throws
	 */
	@RequestMapping(value = "/addApply", method = RequestMethod.POST)
	@ApiOperation("###添加开放应用")
	public Result<?> addApply(HttpSession session,
			@RequestBody OpenApplyEntity apply) {
		Result<?> result = new Result<>();
		
		if(StringUtils.isBlank(apply.getApplyName())){
			result.setErrorMessage("服务名称不能为空!");
			return result;
		}
		
		if(StringUtils.isBlank(apply.getApplyKey())){
			result.setErrorMessage("服务key不能为空!");
			return result;
		}
		User user = (User) session.getAttribute(Constants.SES_LOGIN_USER);
		return	result = consoleOpenApplyService.addApply(user, apply);
		
	}
	
	@RequestMapping(value="/checkApplyName",method=RequestMethod.GET)
	@ApiOperation("###校验应用名称")
	public Result<?> checkApplyName(
			@RequestParam(required=true) String applyName){
		
		return consoleOpenApplyService.checkApplyName(applyName);
	}
	
	/***
	 * 
	 * @Title: modifyApply
	 * @Description: 修改应用
	 * @param : @param openApply
	 * @param : @return
	 * @return: Result<?>
	 * @throws
	 */
	@RequestMapping(value = "/modifyApply", method = RequestMethod.PUT)
	@ApiOperation("修改api")
	public Result<?> modifyApply(@RequestBody OpenApplyEntity openApply) {
		
		return consoleOpenApplyService.modifyApply(openApply);
	}

	/***
	 * 
	 * @Title: submitVerify
	 * @Description: 提交审核
	 */
	@RequestMapping(value = "/submitVerify", method = RequestMethod.PUT)
	@ApiOperation("批量提交")
	public Result<?> submitVerify(@RequestParam(value = "ids", required = true) String ids) {
		return consoleOpenApplyService.submitVerify(ids,AuditConstants.OPEN_APPLY_CHECKED_COMMITED);
	}
	
}
