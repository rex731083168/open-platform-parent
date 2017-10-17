package cn.ce.platform_console.diyApply.controller;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import cn.ce.platform_service.common.Result;
import cn.ce.platform_service.diyApply.entity.appsEntity.Apps;
import cn.ce.platform_service.diyApply.entity.inparameter.GeneratorTenantKeyInParameterEntity;
import cn.ce.platform_service.diyApply.entity.inparameter.RegisterBathAppInParameterEntity;
import cn.ce.platform_service.diyApply.entity.inparameter.SaveOrUpdateAppsInParameterEntity;
import cn.ce.platform_service.diyApply.entity.interfaceMessageInfo.InterfaMessageInfoString;
import cn.ce.platform_service.diyApply.entity.tenantAppsEntity.TenantApps;
import cn.ce.platform_service.diyApply.service.IConsoleDiyApplyService;
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
@Api("租户接口文档")
public class DiyApplyProductController {

	@Resource
	private IConsoleDiyApplyService consoleDiyApplyService;

	@RequestMapping(value = "findTenantAppsByTenantKey", method = RequestMethod.GET)
	@ApiOperation("获取产品实例")
	public Result<TenantApps> findTenantAppsByTenantKey(@RequestParam(value = "key", required = true) String key) {
		return consoleDiyApplyService.findTenantAppsByTenantKey(key);
	}

	@RequestMapping(value = "findPagedApps", method = RequestMethod.GET)
	@ApiOperation("获取所有应用列表")
	public Result<Apps> findPagedApps(@RequestParam(value = "owner", required = true) String owner,
			@RequestParam(value = "name", required = false) String name,
			@RequestParam(required = true, defaultValue = "10") int pageSize,
			@RequestParam(required = true, defaultValue = "1") int currentPage) {
		return consoleDiyApplyService.findPagedApps(owner, name, currentPage, pageSize);
	}

	@RequestMapping(value = "registerBathApp", method = RequestMethod.POST)
	@ApiOperation("开发者在开放平台发布应用审核")
	public Result<InterfaMessageInfoString> registerBathApp(
			@RequestParam(value = "tenantId", required = false) String tenantId,
			@RequestBody RegisterBathAppInParameterEntity[] queryVO, HttpServletRequest request,
			HttpServletResponse response) {
		return consoleDiyApplyService.registerBathApp(tenantId, JSONArray.fromObject(queryVO).toString());
	}

	@RequestMapping(value = "saveOrUpdateApps", method = RequestMethod.POST)
	@ApiOperation("注册应用")
	public Result<InterfaMessageInfoString> saveOrUpdateApps(@RequestBody SaveOrUpdateAppsInParameterEntity[] queryVO,
			HttpServletRequest request, HttpServletResponse response) {
		return consoleDiyApplyService.saveOrUpdateApps(JSONArray.fromObject(queryVO).toString());
	}

	@RequestMapping(value = "generatorTenantKey", method = RequestMethod.POST)
	@ApiOperation("获取网站")
	public Result<InterfaMessageInfoString> generatorTenantKey(@RequestBody GeneratorTenantKeyInParameterEntity queryVO,
			HttpServletRequest request, HttpServletResponse response) {
		return consoleDiyApplyService.generatorTenantKey(queryVO.getId());
	}
	
	@ApiOperation("产品菜单列表")
	@RequestMapping(value = "/productMenuList",method = RequestMethod.GET)
	public Result<String> productMenuList(@RequestParam(value = "bossInstanceCode",required = true)String bossInstanceCode){
		return consoleDiyApplyService.productMenuList(bossInstanceCode);
	}
	
	@ApiOperation("注册菜单")
	@RequestMapping(value = "/registerMenu",method = RequestMethod.POST)
	public Result<String> registerMenu(@RequestParam(value = "appid",required = true)String appid,
										@RequestParam(value = "bossInstanceCode",required = true)String bossInstanceCode,
										@RequestBody(required = true) String menuJson){
		return consoleDiyApplyService.registerMenu(appid, bossInstanceCode, menuJson);
	}	
}
