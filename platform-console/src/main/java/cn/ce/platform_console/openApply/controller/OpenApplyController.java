package cn.ce.platform_console.openApply.controller;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import cn.ce.platform_service.common.AuditConstants;
import cn.ce.platform_service.common.Result;
import cn.ce.platform_service.common.page.Page;
import cn.ce.platform_service.openApply.entity.OpenApplyEntity;
import cn.ce.platform_service.openApply.service.IConsoleOpenApplyService;

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
	public Result<?> applyList(@RequestBody OpenApplyEntity entity,
			@RequestParam(required = false , defaultValue = "1") int currentPage,
			@RequestParam(required = false , defaultValue = "10") int pageSize) {
		Page<OpenApplyEntity> page = new Page<>(currentPage, 0, pageSize);
		return consoleOpenApplyService.applyList(entity,page);
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
	@RequestMapping(value = "/getApplyByid", method = RequestMethod.GET)
	public Result<?> getApplyByid(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "id", required = true) String id) {
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
	@ResponseBody
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
		
		if(StringUtils.isBlank(result.getMessage())){
			result = consoleOpenApplyService.addApply(session,apply);
		}
		
		return result;
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
	@ResponseBody
	public Result<?> modifyApply(@RequestBody OpenApplyEntity openApply) {
		return consoleOpenApplyService.modifyApply(openApply);
	}

	/***
	 * 
	 * @Title: submitVerify
	 * @Description: 提交审核
	 * @param : @param request
	 * @param : @param response
	 * @param : @param id
	 * @param : @return
	 * @return: Result<?>
	 * @throws
	 */
	@RequestMapping(value = "/submitVerify", method = RequestMethod.PUT)
	@ResponseBody
	public Result<?> submitVerify(@RequestParam(value = "id", required = true) String id) {
		return consoleOpenApplyService.submitVerify(id,AuditConstants.OPEN_APPLY_CHECKED_COMMITED);
	}
}
