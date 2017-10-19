package cn.ce.platform_service.diyApply.service;

import cn.ce.platform_service.common.Result;
import cn.ce.platform_service.common.page.Page;
import cn.ce.platform_service.diyApply.entity.appsEntity.Apps;
import cn.ce.platform_service.diyApply.entity.tenantAppPage.TenantAppPage;
import cn.ce.platform_service.diyApply.entity.tenantAppsEntity.AppList;
import cn.ce.platform_service.diyApply.entity.tenantAppsEntity.TenantApps;

/**
 *
 * @Title: IPlublicDiyApplyService.java
 * @Description
 * @version V1.0
 * @author yang.yu@300.cn
 * @date dat2017年10月18日 time下午7:50:17
 *
 **/
public interface IPlublicDiyApplyService {

	public Result<Apps> findPagedApps(String owner, String name, int pageNum, int pageSize);

	/**
	 * @Description 根据key获取产品实例
	 * @param key
	 * @return 产品实例
	 */
	public Result<TenantApps> findTenantAppsByTenantKey(String key);

	public Result<TenantAppPage> findTenantAppsByTenantKeyPage(
			String key, String appName, int pageNum, int pageSize);

}
