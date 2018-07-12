package cn.ce.platform_manage.openApply;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import cn.ce.platform_manage.base.BaseController;
import cn.ce.platform_service.common.AuditConstants;
import cn.ce.platform_service.common.ErrorCodeNo;
import cn.ce.platform_service.common.Result;
import cn.ce.platform_service.common.Status;
import cn.ce.platform_service.common.page.Page;
import cn.ce.platform_service.openApply.entity.OpenApplyEntity;
import cn.ce.platform_service.openApply.entity.QueryOpenApplyEntity;
import cn.ce.platform_service.openApply.service.IManageOpenApplyService;
import cn.ce.platform_service.util.SplitUtil;
import io.swagger.annotations.ApiOperation;

/***
 * 服务管理前端控制器
 * 
 * @author lida
 * @date 2017年8月7日21:37:42
 * @see jdk 1.8
 */
@RestController
@RequestMapping("/openApply")
public class OpenApplyController extends BaseController {

//	private static Logger _LOGGER = Logger.getLogger(OpenApplyController.class);

	@Resource
	private IManageOpenApplyService openApplyService;

	/**
	 * 
	 * @Title: batchUpdate
	 * @Description: 批量审核开放应用
	 * @author: makangwei 
	 * @date:   2018年2月5日 下午2:41:21 
	 * @throws
	 */
//	@RequestMapping(value = "/batchCommit", method = RequestMethod.POST)
	@RequestMapping(value = "/batchUpdate", method = RequestMethod.POST)
	@ApiOperation("批量更新_TODO")
	public Result<?> batchUpdate(
			HttpServletRequest request,
			@RequestParam(value = "ids", required = true) String ids,
			@RequestParam(value = "checkState",required = true) Integer checkState, 
			@RequestParam(value = "checkMem", required = false) String checkMem) {
		
		if(checkState != AuditConstants.OPEN_APPLY_CHECKED_FAILED 
				&& checkState != AuditConstants.OPEN_APPLY_CHECKED_SUCCESS){
			return new Result<String>("当前审核状态不正确", ErrorCodeNo.SYS023,null,Status.FAILED);
		}
		String sourceConfig = request.getParameter("sourceConfig");

		return openApplyService.batchUpdate(sourceConfig,SplitUtil.splitStringWithComma(ids), checkState, checkMem);
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
//	@RequestMapping(value = "/getApplyById", method = RequestMethod.GET)
	@RequestMapping(value = "/getApplyByid", method = RequestMethod.GET)
	@ApiOperation("获取应用详情_TODO")
	public Result<OpenApplyEntity> getApplyByid(@RequestParam(value = "id", required = true) String id) {
		return openApplyService.findById(id);
	}
	
	
	/**
	 * 
	 * @Title: openApplyList
	 * @Description: 获取开放应用列表
	 * @author: makangwei 
	 * @date:   2018年2月5日 下午2:43:12 
	 */
	@RequestMapping(value = "/openApplyList", method = RequestMethod.POST)
	@ApiOperation("开放应用列表_TODO")
//	public Result<Page<OpenApplyEntity>> openApplyList(
//			@RequestBody QueryOpenApplyEntity queryEntity) {
	public Result<Page<OpenApplyEntity>> openApplyList(
			@RequestBody QueryOpenApplyEntity queryEntity,
			@RequestParam(required = false, defaultValue = "1") int currentPage,
			@RequestParam(required = false, defaultValue = "10") int pageSize) {
		
		queryEntity.setCurrentPage(currentPage);
		queryEntity.setPageSize(pageSize);
		return openApplyService.findOpenApplyList(queryEntity);
	}
	
	
}
