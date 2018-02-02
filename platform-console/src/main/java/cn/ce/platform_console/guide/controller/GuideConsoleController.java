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
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import cn.ce.platform_service.common.Constants;
import cn.ce.platform_service.common.ErrorCodeNo;
import cn.ce.platform_service.common.Result;
import cn.ce.platform_service.guide.entity.GuideEntity;
import cn.ce.platform_service.guide.entity.QueryGuideEntity;
import cn.ce.platform_service.guide.service.IConsoleGuideService;
import cn.ce.platform_service.users.entity.User;
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
	private IConsoleGuideService consoleGuideService;

	@RequestMapping(value = "/guide", method = RequestMethod.POST)
	@ApiOperation("添加指南")
	public Result<?> guideAdd(HttpSession session, @RequestBody GuideEntity g) {
		Result<?> result = new Result<>();
		if (StringUtils.isBlank(g.getGuideName())) {
			result.setErrorCode(ErrorCodeNo.SYS005);
			result.setMessage("指南名称不能为空!");
			return result;
		}
		User user = (User) session.getAttribute(Constants.SES_LOGIN_USER);
		return consoleGuideService.add(user, g);
	}

	@RequestMapping(value = "/updateGuide", method = RequestMethod.PUT)
	@ApiOperation("修改指南")
	public Result<?> guideUpdate(@RequestBody GuideEntity guideEntity) {
		
		if(StringUtils.isBlank(guideEntity.getId())){
			Result<String> result = new Result<>();
			result.setErrorMessage("指南不存在!", ErrorCodeNo.SYS015);
			return result;
		}
		 
		return consoleGuideService.update(guideEntity);
	}

	@RequestMapping(value = "/guideList", method = RequestMethod.POST)
	@ApiOperation("###查询指南列表")
	public Result<?> guideList(@RequestBody QueryGuideEntity entity) {
		
		return consoleGuideService.guideList(entity);
	}

	@RequestMapping(value = "/guide/{gid}", method = RequestMethod.DELETE)
	@ApiOperation("##删除指南")
	public Result<?> guideDelete(HttpServletRequest request, HttpServletResponse response, HttpSession session,
			@PathVariable("gid") String id) {
		return consoleGuideService.delete(id);
	}

	@RequestMapping(value = "/guide/{gid}", method = RequestMethod.GET)
	@ApiOperation("获取指南详情")
	public Result<GuideEntity> getGuideByid(HttpServletRequest request, HttpServletResponse response,
			@PathVariable("gid") String id) {

		return consoleGuideService.getByid(id);
	}

	/***
	 * 
	 * @Title: submitVerify @Description: 提交审核 @param : @param request @param
	 *         : @param response @param : @param id @param : @return @return:
	 *         Result<?> @throws
	 */
	@RequestMapping(value = "/submitVerify", method = RequestMethod.PUT)
	@ResponseBody
	public Result<?> submitVerify(@RequestParam String ids) {
		return consoleGuideService.submitVerify(ids);
	}

}
