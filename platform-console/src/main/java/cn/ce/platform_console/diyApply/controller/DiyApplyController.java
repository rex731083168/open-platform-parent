package cn.ce.platform_console.diyApply.controller;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
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
import cn.ce.platform_service.common.page.Page;
import cn.ce.platform_service.diyApply.entity.DiyApplyEntity;
import cn.ce.platform_service.diyApply.service.IConsoleDiyApplyService;
import cn.ce.platform_service.users.entity.User;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/***
 * 
 * 
 * @ClassName: DiyApplyController
 * @Description:定制应用控制类
 * @author: lida
 * @date: 2017年10月14日 下午3:41:37
 * @Copyright: 2017 中企动力科技股份有限公司 © 1999-2017 300.cn All Rights Reserved
 *
 */
@RestController
@RequestMapping("/diyApply")
@Api("定制应用控制类")
public class DiyApplyController {

	/** 日志对象 */
	// private static Logger logger =
	// LogManager.getLogger(DiyApplyController.class);

	@Resource
	private IConsoleDiyApplyService consoleDiyApplyService;

//	@Resource
//	private IAPIService apiService;

	@RequestMapping(value = "/findApplyList", method = RequestMethod.POST)
	@ApiOperation(value = "根据条件查询应用列表", httpMethod = "POST", response = Result.class, notes = "根据条件查询应用列表")
	public Result<Page<DiyApplyEntity>> findApplyList(@RequestBody DiyApplyEntity apply,
			@RequestParam(required = false, defaultValue = "10") int pageSize,
			@RequestParam(required = false, defaultValue = "1") int currentPage) {

		Page<DiyApplyEntity> page = new Page<>(currentPage, 0, pageSize);

		Result<Page<DiyApplyEntity>> result = consoleDiyApplyService.findApplyList(apply, page);

		return result;
	}

	@RequestMapping(value = "/deleteApplyByid", method = RequestMethod.DELETE)
	@ApiOperation("根据标识删除应用")
	public Result<String> deleteApplyByid(HttpServletResponse response,
			@RequestParam(value = "id", required = true) String id) {

		Result<String> result = new Result<>();

		result = consoleDiyApplyService.deleteApplyByid(id);

		return result;
	}

//	@RequestMapping(value = "/getApplyApisById", method = RequestMethod.GET)
//	@ApiOperation("查询应用及api分页集合")
//	public Result<DiyApplyEntity> getApplyByid(HttpServletRequest request, HttpServletResponse response,
//			@RequestParam(value = "id", required = true) String id,
//			@RequestParam(required = false, defaultValue = "10") int pageSize,
//			@RequestParam(required = false, defaultValue = "1") int currentPage) {
//
//		Result<DiyApplyEntity> result = consoleDiyApplyService.getApplyById(id, pageSize, currentPage);
//
//		return result;
//	}

	@RequestMapping(value = "/getApplyById", method = RequestMethod.GET)
	@ApiOperation("根据应用标识查询应用")
	public Result<DiyApplyEntity> getApplyById(@RequestParam(value = "applyId", required = true) String applyId) {
		Result<DiyApplyEntity> result = new Result<>();
		DiyApplyEntity findById = consoleDiyApplyService.findById(applyId);
		if (null == findById) {
			result.setErrorMessage("应用不存在!");
		} else {
			result.setSuccessData(findById);
		}
		return result;
	}

	@RequestMapping(value = "/saveApply", method = RequestMethod.POST)
	@ApiOperation("新增应用")
	public Result<String> saveApply(HttpSession session, @RequestBody DiyApplyEntity apply) {

		Result<String> result = new Result<>();

		if (StringUtils.isBlank(apply.getApplyName())) {
			result.setErrorMessage("应用名称不能为空!");
			return result;
		}

		User user = (User) session.getAttribute(Constants.SES_LOGIN_USER);

		apply.setUser(user);

		result = consoleDiyApplyService.saveApply(apply);

		return result;
	}

	@RequestMapping(value = "updateApply", method = RequestMethod.POST)
	@ApiOperation("修改应用，不能修改产品密钥")
	public Result<?> updateApply(HttpSession session, @RequestBody DiyApplyEntity apply) {

		return consoleDiyApplyService.updateApply(apply);
	}

	@ApiOperation("批量发布应用 更改checkState为1 ")
	@RequestMapping(value = "/batchUpdate", method = RequestMethod.POST)
	public Result<String> batchUpdate(@RequestParam String ids) {
		return consoleDiyApplyService.batchUpdate(ids, AuditConstants.DIY_APPLY_CHECKED_COMMITED, null);
	}

}
