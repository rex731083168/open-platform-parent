package cn.ce.platform_manage.guide;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import cn.ce.platform_service.common.Result;
import cn.ce.platform_service.common.page.Page;
import cn.ce.platform_service.guide.entity.GuideEntity;
import cn.ce.platform_service.guide.service.IManageGuideService;
import cn.ce.platform_service.util.SplitUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 *
 * @Title: GuideManageController.java
 * @Description
 * @version V1.0
 * @author yang.yu@300.cn
 * @date dat2017年10月13日 time上午9:34:37
 *
 **/

@RestController
@RequestMapping("/guideManage")
@Api("指南管理")
public class GuideManageController {

	/** 日志对象 */
	private static Logger _LOGGER = Logger.getLogger(GuideManageController.class);
	@Resource
	private IManageGuideService iManageGuideService;

	@ApiOperation("指南列表")
	@RequestMapping(value = "/guideList", method = RequestMethod.POST)
	public Result<?> guideList(String guideName, String creatUserName, String applyId,
			@RequestParam(required = false, defaultValue = "1") int currentPage,
			@RequestParam(required = false, defaultValue = "10") int pageSize) {
		Result<Page<GuideEntity>> result = new Result<Page<GuideEntity>>();
		result = iManageGuideService.guideList(guideName, creatUserName, applyId, currentPage, pageSize);
		return result;
	}

	@ApiOperation("指南详情")
	@RequestMapping(value = "/getGuideByid", method = RequestMethod.GET)
	public Result<GuideEntity> getGuideByid(@RequestParam(value = "id", required = true) String id) {
		return iManageGuideService.getByid(id);

	}

	@ApiOperation("批量审核")
	@RequestMapping(value = "/batchUpdate", method = RequestMethod.POST)
	public Result<String> batchUpdate(@RequestParam String ids, @RequestParam Integer checkState,
			@RequestParam String checkMem) {
		return iManageGuideService.batchUpdate(SplitUtil.splitStringWithComma(ids), checkState, checkMem);
	}

}
