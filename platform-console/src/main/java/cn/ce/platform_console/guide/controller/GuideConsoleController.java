package cn.ce.platform_console.guide.controller;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import cn.ce.platform_service.common.Result;
import cn.ce.platform_service.common.page.Page;
import cn.ce.platform_service.guide.entity.GuideEntity;
import cn.ce.platform_service.guide.service.IConsoleGuideService;

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
public class GuideConsoleController {

	@Resource
	private IConsoleGuideService iConsoleGuideService;

	@RequestMapping(value = "/guideAddOrAudit", method = RequestMethod.POST)
	public Result<?> guideAdd(HttpServletRequest request, HttpServletResponse response, HttpSession session,
			@RequestBody GuideEntity g) {
		return iConsoleGuideService.add(session, g);
	}

	@RequestMapping(value = "/guideList", method = RequestMethod.POST)
	public Result<?> guideList(@RequestBody GuideEntity entity, String guideName, String creatUserName,
			@RequestParam(required = false, defaultValue = "1") int currentPage,
			@RequestParam(required = false, defaultValue = "10") int pageSize) {
		Result<Page<GuideEntity>> result = new Result<Page<GuideEntity>>();
		result = iConsoleGuideService.guideList(guideName, creatUserName, currentPage, pageSize);
		return result;
	}

	@RequestMapping(value = "/guideDelete", method = RequestMethod.POST)
	public Result<?> guideDelete(HttpServletRequest request, HttpServletResponse response, HttpSession session,
			@RequestBody GuideEntity g) {
		return iConsoleGuideService.delete(g);
	}

	@RequestMapping(value = "/getGuideByid", method = RequestMethod.GET)
	public Result<GuideEntity> getGuideByid(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "id", required = true) String id) {

		Result<GuideEntity> result = new Result<>();
		result = iConsoleGuideService.getByid(id);

		return result;

	}

}
