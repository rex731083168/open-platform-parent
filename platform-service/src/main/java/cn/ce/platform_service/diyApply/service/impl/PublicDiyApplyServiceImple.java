package cn.ce.platform_service.diyApply.service.impl;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import cn.ce.platform_service.common.ErrorCodeNo;
import cn.ce.platform_service.common.HttpClientUtil;
import cn.ce.platform_service.common.Result;
import cn.ce.platform_service.diyApply.entity.appsEntity.Apps;
import cn.ce.platform_service.diyApply.entity.tenantAppPage.TenantAppPage;
import cn.ce.platform_service.diyApply.entity.tenantAppsEntity.TenantApps;
import cn.ce.platform_service.diyApply.service.IPlublicDiyApplyService;
import cn.ce.platform_service.util.PropertiesUtil;

/**
 *
 * @Title: PublicDiyApplyServiceImple.java
 * @Description
 * @version V1.0
 * @author yang.yu@300.cn
 * @date dat2017年10月18日 time下午7:52:40
 *
 **/
@Service("publicDiyApplyServiceImple")
public class PublicDiyApplyServiceImple implements IPlublicDiyApplyService {
	private static Logger _LOGGER = Logger.getLogger(PublicDiyApplyServiceImple.class);

	@Override
	public Result<Apps> findPagedApps(String owner, String name, int pageNum, int pageSize) {
		Result<Apps> result = new Result<>();
		String url = PropertiesUtil.getInstance().getValue("findPagedApps");
		String o$ = Pattern.quote("${o}");
		String n$ = Pattern.quote("${n}");
		String p$ = Pattern.quote("${p}");
		String z$ = Pattern.quote("${z}");
		if (StringUtils.isBlank(owner)) {
			owner = "";
		}
		if (StringUtils.isBlank(name)) {
			name = "";
		}

		String replacedurl = url.replaceAll(o$, owner).replaceAll(n$, name).replaceAll(p$, String.valueOf(pageNum))
				.replaceAll(z$, String.valueOf(pageSize));

		Map<String, Class<?>> classMap = new HashMap<String, Class<?>>();
		classMap.put("list", cn.ce.platform_service.diyApply.entity.appsEntity.AppList.class);
		classMap.put("appTypes", cn.ce.platform_service.diyApply.entity.appsEntity.AppTypes.class);

		try {
			/* get请求方法 */
			Apps apps = (Apps) HttpClientUtil.getUrlReturnObject(replacedurl, Apps.class, classMap);

			/* 无接口时的测试方法 */
			// Apps apps = (Apps) testgetUrlReturnObject("findPagedApps", replacedurl,
			// Apps.class, classMap);
			if (apps.getStatus() == 200 || apps.getStatus() == 110) {
				result.setData(apps);
				result.setSuccessMessage("");
				return result;
			} else {
				_LOGGER.error("findPagedApps data http getfaile return code :" + apps.getMsg() + " ");
				result.setErrorCode(ErrorCodeNo.SYS006);
				return result;
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			_LOGGER.error("findPagedApps http error " + e + "");
			result.setErrorCode(ErrorCodeNo.SYS001);
			result.setErrorMessage("请求失败");
			return result;
		}

	}

	@Override
	public Result<TenantApps> findTenantAppsByTenantKey(String key) {

		// TODO 需要把key查询出来的定制应用和多个开放应用的绑定关系存入数据库

		Result<TenantApps> result = new Result<>();
		String url = PropertiesUtil.getInstance().getValue("findTenantAppsByTenantKey");
		String key$ = Pattern.quote("${key}");
		String replacedurl = url.replaceAll(key$, key);
		try {
			/* get请求方法 */
			TenantApps applyproduct = (TenantApps) HttpClientUtil.specialTenantRetrunObject(replacedurl);

			if (applyproduct.getStatus() == 200) {
				result.setData(applyproduct);
				result.setSuccessMessage("");
				return result;
			} else {
				_LOGGER.error(
						"findTenantAppsByTenantKey data http getfaile return code :" + applyproduct.getMsg() + " ");
				result.setErrorCode(ErrorCodeNo.SYS006);
				return result;
			}
		} catch (Exception e) {
			_LOGGER.error("findTenantAppsByTenantKey http error " + e + "");
			result.setErrorCode(ErrorCodeNo.SYS001);
			result.setErrorMessage("请求失败");
			return result;
		}

	}

	@Override
	public Result<TenantAppPage> findTenantAppsByTenantKeyPage(String key, String appName, int pageNum, int pageSize) {

		Result<TenantAppPage> result = new Result<>();
		String url = PropertiesUtil.getInstance().getValue("findTenantAppsByTenantKeypage");
		String key$ = Pattern.quote("${key}");
		String n$ = Pattern.quote("${n}");
		String p$ = Pattern.quote("${p}");
		String z$ = Pattern.quote("${z}");
		if (StringUtils.isBlank(appName)) {
			appName = "";
		}
		Map<String, Class<?>> classMap = new HashMap<String, Class<?>>();

		classMap.put("tenant", cn.ce.platform_service.diyApply.entity.tenantAppPage.Tenant.class);
		classMap.put("pages", cn.ce.platform_service.diyApply.entity.tenantAppPage.Pages.class);
		classMap.put("list", cn.ce.platform_service.diyApply.entity.tenantAppPage.List.class);
		classMap.put("attr", cn.ce.platform_service.diyApply.entity.tenantAppPage.Attr.class);
		classMap.put("params", cn.ce.platform_service.diyApply.entity.tenantAppPage.Params.class);
		classMap.put("instanceList", cn.ce.platform_service.diyApply.entity.tenantAppPage.InstanceList.class);

		String replacedurl = url.replaceAll(key$, key).replaceAll(n$, appName).replaceAll(z$, String.valueOf(pageSize))
				.replaceAll(p$, String.valueOf(pageNum));
		try {
			TenantAppPage applyproduct = (TenantAppPage) HttpClientUtil.getUrlReturnObject(replacedurl,
					TenantAppPage.class, classMap);

			if (applyproduct.getStatus() == 200) {
				result.setData(applyproduct);
				result.setSuccessMessage("");
				return result;
			} else {
				_LOGGER.error(
						"findTenantAppsByTenantKey data http getfaile return code :" + applyproduct.getMsg() + " ");
				result.setErrorCode(ErrorCodeNo.SYS006);
				return result;
			}
		} catch (Exception e) {
			_LOGGER.error("findTenantAppsByTenantKey http error " + e + "");
			result.setErrorCode(ErrorCodeNo.SYS001);
			result.setErrorMessage("请求失败");
			return result;
		}

	}

}
