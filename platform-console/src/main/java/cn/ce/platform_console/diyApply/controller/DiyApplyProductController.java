package cn.ce.platform_console.diyApply.controller;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import cn.ce.platform_service.common.Result;
import cn.ce.platform_service.diyApply.entity.appsEntity.Apps;
import cn.ce.platform_service.diyApply.entity.tenantAppsEntity.TenantApps;
import cn.ce.platform_service.diyApply.service.IConsoleDiyApplyService;

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
	public Result<TenantApps> findTenantAppsByTenantKey(
			@RequestParam(value = "key", required = true) String key) {
		return consoleDiyApplyService.findTenantAppsByTenantKey(key);
	}

	@RequestMapping(value = "findPagedApps", method = RequestMethod.POST)
	public Result<Apps> findPagedApps(@RequestParam(value = "owner", required = true) String owner,
			@RequestParam(value = "name", required = true) String name,
			@RequestParam(required = false, defaultValue = "10") int pageSize,
			@RequestParam(required = false, defaultValue = "1") int currentPage) {

		return consoleDiyApplyService.findPagedApps(owner, name, currentPage, pageSize);
	}

}
