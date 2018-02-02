package cn.ce.platform_manage.openApply;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import cn.ce.platform_manage.base.BaseController;
import cn.ce.platform_service.common.AuditConstants;
import cn.ce.platform_service.common.ErrorCodeNo;
import cn.ce.platform_service.common.Result;
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
	 * @param ids
	 *            jsondemo["1","2","3"]
	 * @return
	 */
	@RequestMapping(value = "/batchUpdate", method = RequestMethod.POST)
	public Result<String> batchUpdate(@RequestParam(value = "ids", required = true) String ids,
			@RequestParam(value = "checkState",required = true) Integer checkState, 
			@RequestParam(value = "checkMem", required = false) String checkMem) {
		
		if(checkState == null){
			checkState = AuditConstants.OPEN_APPLY_CHECKED_SUCCESS;
		}else if(checkState != AuditConstants.OPEN_APPLY_CHECKED_FAILED && checkState != AuditConstants.OPEN_APPLY_CHECKED_SUCCESS){
			Result<String> result = new Result<String>();
			result.setErrorMessage("当前审核状态不正确", ErrorCodeNo.SYS023);
		}
		
		return openApplyService.batchUpdate(SplitUtil.splitStringWithComma(ids), checkState, checkMem);
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
	@ApiOperation("###获取应用详情")
	public Result<OpenApplyEntity> getApplyByid(@RequestParam(value = "id", required = true) String id) {
		return openApplyService.findById(id);
	}
	
	
	/***
	 * 按照条件查询服务分类列表方法
	 * 
	 * @author lida
	 * @date 2017年8月8日15:11:11
	 * @param request
	 * @param response
	 * @param appName
	 *            服务分类名称
	 * @param checkState
	 *            服务分类状态
	 * @param currentPage
	 *            当前页
	 * @param pageSize
	 *            分页数
	 * @return
	 */
	@RequestMapping(value = "/openApplyList", method = RequestMethod.POST)
	public Result<Page<OpenApplyEntity>> openApplyList(
			@RequestBody QueryOpenApplyEntity queryEntity) {
		return openApplyService.findOpenApplyList(queryEntity);
	}
	
	
}
