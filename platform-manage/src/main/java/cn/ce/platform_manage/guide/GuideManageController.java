package cn.ce.platform_manage.guide;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
public class GuideManageController {

	/** 日志对象 */
	private static Logger _LOGGER = Logger.getLogger(GuideManageController.class);
	@Resource
	private IManageGuideService iManageGuideService;

	@RequestMapping(value = "/guideList", method = RequestMethod.POST)
	public Result<?> guideList(@RequestBody GuideEntity entity, String guideName, String creatUserName,
			@RequestParam(required = false, defaultValue = "1") int currentPage,
			@RequestParam(required = false, defaultValue = "10") int pageSize) {
		Result<Page<GuideEntity>> result = new Result<Page<GuideEntity>>();
		result = iManageGuideService.guideList(guideName, creatUserName, currentPage, pageSize);
		return result;
	}

	@RequestMapping(value = "/getGuideByid", method = RequestMethod.GET)
	public Result<GuideEntity> getGuideByid(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "id", required = true) String id) {
		Result<GuideEntity> result = new Result<GuideEntity>();
		result = iManageGuideService.getByid(id);
		return result;

	}

	/**
	 * 
	 * @param ids
	 *            jsondemo["1","2","3"]
	 * @return
	 */
	@RequestMapping(value = "/batchUpdate", method = RequestMethod.POST)
	public Result<String> batchUpdate(@RequestBody String ids) {

		Result<String> result = new Result<String>();
		result = iManageGuideService.batchUpdate(ids);
		return result;

	}

}
