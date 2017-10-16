package cn.ce.platform_manage.diyApply;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import cn.ce.platform_service.common.Result;
import cn.ce.platform_service.diyApply.service.IManageDiyApplyService;
import cn.ce.platform_service.util.SplitUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * @Description : 说明
 * @Author : makangwei
 * @Date : 2017年10月16日
 */
@RestController
@RequestMapping("/diyApplyManage")
@Api("定制应用")
public class DiyApplyController {

	//private static Logger _LOGGER = LoggerFactory.getLogger(DiyApplyController.class);

	@Resource
	IManageDiyApplyService manageDiyApplyService;

	@ApiOperation("定制应用列表")
	@RequestMapping(value = "/diyApplyList", method = RequestMethod.POST)
	public Result<?> diyApplyList(String productName, String userName, int checkState, String applyName,
			@RequestParam(required = false, defaultValue = "1") int currentPage,
			@RequestParam(required = false, defaultValue = "10") int pageSize) {
		return manageDiyApplyService.findPagedApps(productName, userName, checkState, applyName, currentPage, pageSize);
	}
	@ApiOperation("批量审核")
	@RequestMapping(value = "/batchUpdate", method = RequestMethod.POST)
	public Result<String> batchUpdate(@RequestBody String ids) {
		return manageDiyApplyService.batchUpdate(SplitUtil.splitStringWithComma(ids));
	}

	@ApiOperation("单个审核")
	@RequestMapping(value = "/auditUpdate", method = RequestMethod.POST)
	public Result<String> auditUpdate(@RequestParam String id, @RequestParam int checkState,
			@RequestParam String checkMem) {
		return manageDiyApplyService.auditUpdate(id, checkState, checkMem);
	}

}
