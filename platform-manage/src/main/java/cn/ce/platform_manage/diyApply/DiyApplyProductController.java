package cn.ce.platform_manage.diyApply;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import cn.ce.platform_service.common.Result;
import cn.ce.platform_service.diyApply.entity.appsEntity.Apps;
import cn.ce.platform_service.diyApply.entity.inparameter.RegisterBathAppInParameterEntity;
import cn.ce.platform_service.diyApply.entity.inparameter.SaveOrUpdateAppsInParameterEntity;
import cn.ce.platform_service.diyApply.entity.interfaceMessageInfo.InterfaMessageInfoString;
import cn.ce.platform_service.diyApply.service.IManageDiyApplyService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import net.sf.json.JSONArray;

/**
 *
 * @Title: DiyApplyProductController.java
 * @Description
 * @version V1.0
 * @author yang.yu@300.cn
 * @date dat2017年10月13日 time上午11:02:16
 *
 **/
@RestController
@RequestMapping("/diyapplyProduct")
@Api("manage租户接口")
public class DiyApplyProductController {

	@Resource
	private IManageDiyApplyService manageDiyApplyService;

	@RequestMapping(value = "findPagedApps", method = RequestMethod.GET)
	@ApiOperation("获取所有应用列表")
	public Result<Apps> findPagedApps(@RequestParam(value = "owner", required = false) String owner,
			@RequestParam(value = "name", required = false) String name,
			@RequestParam(required = true, defaultValue = "10") int pageSize,
			@RequestParam(required = true, defaultValue = "1") int currentPage) {
		return manageDiyApplyService.findPagedApps(owner, name, currentPage, pageSize);
	}

	@RequestMapping(value = "registerBathApp", method = RequestMethod.POST)
	@ApiOperation("开发者在开放平台发布应用审核")
	public Result<InterfaMessageInfoString> registerBathApp(
			@RequestParam(value = "tenantId", required = false) String tenantId,
			@RequestBody RegisterBathAppInParameterEntity[] queryVO, HttpServletRequest request,
			HttpServletResponse response) {
		return manageDiyApplyService.registerBathApp(tenantId, JSONArray.fromObject(queryVO).toString());
	}

}
