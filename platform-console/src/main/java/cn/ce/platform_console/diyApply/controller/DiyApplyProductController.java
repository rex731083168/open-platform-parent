package cn.ce.platform_console.diyApply.controller;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import cn.ce.platform_service.common.Result;
import cn.ce.platform_service.diyApply.entity.appsEntity.Apps;
import cn.ce.platform_service.diyApply.entity.inparameter.GeneratorTenantKeyInParameterEntity;
import cn.ce.platform_service.diyApply.entity.inparameter.RegisterBathAppInParameterEntity;
import cn.ce.platform_service.diyApply.entity.inparameter.SaveOrUpdateAppsInParameterEntity;
import cn.ce.platform_service.diyApply.entity.interfaceMessageInfo.InterfaMessageInfoJasonObject;
import cn.ce.platform_service.diyApply.entity.interfaceMessageInfo.InterfaMessageInfoString;
import cn.ce.platform_service.diyApply.entity.tenantAppsEntity.TenantApps;
import cn.ce.platform_service.diyApply.service.IConsoleDiyApplyService;
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
public class DiyApplyProductController {

	@Resource
	private IConsoleDiyApplyService consoleDiyApplyService;

	@RequestMapping(value = "findTenantAppsByTenantKey", method = RequestMethod.POST)
	public Result<TenantApps> findTenantAppsByTenantKey(@RequestParam(value = "key", required = true) String key) {
		return consoleDiyApplyService.findTenantAppsByTenantKey(key);
	}

	@RequestMapping(value = "findPagedApps", method = RequestMethod.POST)
	public Result<Apps> findPagedApps(@RequestParam(value = "owner", required = true) String owner,
			@RequestParam(value = "name", required = true) String name,
			@RequestParam(required = false, defaultValue = "10") int pageSize,
			@RequestParam(required = false, defaultValue = "1") int currentPage) {
		return consoleDiyApplyService.findPagedApps(owner, name, currentPage, pageSize);
	}

	@RequestMapping(value = "registerBathApp", method = RequestMethod.POST)
	@ResponseBody
	public Result<InterfaMessageInfoJasonObject> registerBathApp(@RequestParam(value = "tenantId", required = true) String tenantId,@RequestBody RegisterBathAppInParameterEntity[] queryVO,
			HttpServletRequest request, HttpServletResponse response) {
		return consoleDiyApplyService.registerBathApp(tenantId,JSONArray.fromObject(queryVO).toString());
	}

	@RequestMapping(value = "saveOrUpdateApps", method = RequestMethod.POST)
	@ResponseBody
	public Result<InterfaMessageInfoString> saveOrUpdateApps(@RequestBody SaveOrUpdateAppsInParameterEntity[] queryVO,
			HttpServletRequest request, HttpServletResponse response) {
		return consoleDiyApplyService.saveOrUpdateApps(JSONArray.fromObject(queryVO).toString());
	}

	@RequestMapping(value = "generatorTenantKey", method = RequestMethod.POST)
	public Result<InterfaMessageInfoString> generatorTenantKey(@RequestBody GeneratorTenantKeyInParameterEntity queryVO,
			HttpServletRequest request, HttpServletResponse response) {
		return consoleDiyApplyService.generatorTenantKey(queryVO.getId());
	}
}
