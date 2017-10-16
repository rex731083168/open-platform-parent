package cn.ce.platform_console.guide.controller;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import cn.ce.platform_service.common.ErrorCodeNo;
import cn.ce.platform_service.common.Result;
import cn.ce.platform_service.common.page.Page;
import cn.ce.platform_service.guide.entity.GuideEntity;
import cn.ce.platform_service.guide.service.IConsoleGuideService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 *
 * @Title: GuideController.java
 * @Description
 * @version V1.0
 * @author yang.yu@300.cn
 * @date dat2017年10月12日 time下午8:06:46
 *
 **/
@RestController
@RequestMapping("/guideConsole")
@Api("应用指南")
public class GuideConsoleController {

	@Resource
	private IConsoleGuideService iConsoleGuideService;

	@RequestMapping(value = "/guide", method = RequestMethod.POST)
	@ApiOperation("添加指南")
	public Result<?> guideAdd(HttpSession session,@RequestBody GuideEntity g) {
		Result<?> result = new Result<>();
		if(StringUtils.isBlank(g.getGuideName())){
			result.setErrorCode(ErrorCodeNo.SYS005);
			result.setMessage("指南名称不能为空!");
			return result;
		}
		return iConsoleGuideService.add(session, g);
	}
	
	@RequestMapping(value = "/guide/{gid}", method = RequestMethod.PUT)
	@ApiOperation("修改指南")
	public Result<?> guideUpdate(@RequestBody GuideEntity g , @PathVariable("gid") String gid) {
		g.setId(gid);
		return iConsoleGuideService.update(g);
	}

	@RequestMapping(value = "/guideList", method = RequestMethod.GET)
	@ApiOperation("查询指南列表")
	public Result<?> guideList(String guideName, String creatUserName,
			@RequestParam(required = false, defaultValue = "1") int currentPage,
			@RequestParam(required = false, defaultValue = "10") int pageSize) {
		Result<Page<GuideEntity>> result = new Result<Page<GuideEntity>>();
		result = iConsoleGuideService.guideList(guideName, creatUserName, currentPage, pageSize);
		return result;
	}

	@RequestMapping(value = "/guide/{gid}", method = RequestMethod.DELETE)
	@ApiOperation("删除指南")
	public Result<?> guideDelete(HttpServletRequest request, HttpServletResponse response, HttpSession session,@PathVariable("gid") String id) {
		return iConsoleGuideService.delete(id);
	}

	@RequestMapping(value = "/guide/{gid}", method = RequestMethod.GET)
	@ApiOperation("获取指南详情")
	public Result<GuideEntity> getGuideByid(HttpServletRequest request, HttpServletResponse response,
			@PathVariable("gid") String id) {
		
		Result<GuideEntity> result = new Result<>();
		result = iConsoleGuideService.getByid(id);

		return result;

	}

}
