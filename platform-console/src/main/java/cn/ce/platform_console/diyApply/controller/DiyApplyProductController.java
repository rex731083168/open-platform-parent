package cn.ce.platform_console.diyApply.controller;

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
import cn.ce.platform_service.diyApply.entity.inparameter.GeneratorTenantKeyInParameterEntity;
import cn.ce.platform_service.diyApply.entity.interfaceMessageInfo.InterfaMessageInfoString;
import cn.ce.platform_service.diyApply.entity.tenantAppPage.TenantAppPage;
import cn.ce.platform_service.diyApply.service.IConsoleDiyApplyService;
import cn.ce.platform_service.diyApply.service.IPlublicDiyApplyService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

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
@Api("租户接口文档")
public class DiyApplyProductController {

	@Resource
	private IConsoleDiyApplyService consoleDiyApplyService;

	@Resource
	private IPlublicDiyApplyService plublicDiyApplyService;

	@RequestMapping(value = "findTenantAppsByTenantKey", method = RequestMethod.GET)
	@ApiOperation("获取产品实例 查询带分页")
	public Result<TenantAppPage> findTenantAppsByTenantKey(@RequestParam(value = "key", required = true) String key,
			@RequestParam(required = false) String appName, @RequestParam(required = true, defaultValue = "10") int pageSize,
			@RequestParam(required = true, defaultValue = "1") int currentPage) {
		return plublicDiyApplyService.findTenantAppsByTenantKeyPage(key, appName, currentPage, pageSize);
	}
	
	@RequestMapping(value = "/findPagedApps", method = RequestMethod.GET)
	@ApiOperation("获取所有应用列表")
	public Result<Apps> findPagedApps(@RequestParam(value = "owner", required = false) String owner,
			@RequestParam(value = "name", required = false) String name,
			@RequestParam(required = true, defaultValue = "10") int pageSize,
			@RequestParam(required = true, defaultValue = "1") int currentPage) {
		return plublicDiyApplyService.findPagedApps(owner, name, currentPage, pageSize);
	}

	@RequestMapping(value = "generatorTenantKey", method = RequestMethod.POST)
	@ApiOperation("获取网站")
	public Result<InterfaMessageInfoString> generatorTenantKey(@RequestBody GeneratorTenantKeyInParameterEntity queryVO,
			HttpServletRequest request, HttpServletResponse response) {
		return consoleDiyApplyService.generatorTenantKey(queryVO.getId());
	}

	@ApiOperation("产品菜单列表")
	@RequestMapping(value = "/productMenuList", method = RequestMethod.GET)
	public Result<String> productMenuList(
			@RequestParam(value = "bossInstanceCode", required = true) String bossInstanceCode) {
		return consoleDiyApplyService.productMenuList(bossInstanceCode);
	}

	@ApiOperation("注册菜单")
	@RequestMapping(value = "/registerMenu", method = RequestMethod.POST)
	public Result<String> registerMenu(@RequestParam(value = "appid", required = true) String appid,
			@RequestParam(value = "bossInstanceCode", required = true) String bossInstanceCode,String menuJson) {
		return consoleDiyApplyService.registerMenu(appid, bossInstanceCode, menuJson);
	}
}
