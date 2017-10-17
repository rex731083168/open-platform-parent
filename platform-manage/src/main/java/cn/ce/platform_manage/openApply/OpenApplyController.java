package cn.ce.platform_manage.openApply;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import cn.ce.platform_manage.base.BaseController;
import cn.ce.platform_service.apis.service.IAPIService;
import cn.ce.platform_service.common.Result;
import cn.ce.platform_service.common.page.Page;
import cn.ce.platform_service.openApply.entity.OpenApplyEntity;
import cn.ce.platform_service.openApply.entity.QueryOpenApplyEntity;
import cn.ce.platform_service.openApply.service.IManageOpenApplyService;
import cn.ce.platform_service.util.SplitUtil;

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

	private static Logger _LOGGER = Logger.getLogger(OpenApplyController.class);

	@Resource
	private IAPIService apiService;

	@Resource
	private IManageOpenApplyService openApplyService;

	/**
	 * 
	 * @param ids
	 *            jsondemo["1","2","3"]
	 * @return
	 */
	@RequestMapping(value = "/batchUpdate", method = RequestMethod.POST)
	public Result<String> batchUpdate(@RequestBody String ids,Integer checkState,String checkMem) {
		return openApplyService.batchUpdate(SplitUtil.splitStringWithComma(ids),checkState,checkMem);
	}

	/**
	 * 
	 * @Title: auditGroupState @Description: TODO(这里用一句话描述这个方法的作用) @param : @param
	 *         id @param : @param checkState @param : @param remark @throws
	 */
	@RequestMapping(value = "/auditGroup", method = RequestMethod.POST)
	public Result<String> auditGroupState(String id, int checkState, String remark) {
		return openApplyService.auditGroup(id, checkState, remark);
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
	@ResponseBody
	public Result<Page<OpenApplyEntity>> openApplyList(
			@RequestBody QueryOpenApplyEntity queryEntity,
			@RequestParam(required = false, defaultValue = "1") int currentPage,
			@RequestParam(required = false, defaultValue = "10") int pageSize) {
		return openApplyService.findOpenApplyList(queryEntity, currentPage, pageSize);
	}
	
	
}
