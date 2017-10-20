package cn.ce.platform_service.diyApply.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import cn.ce.platform_service.apis.dao.INewApiDao;
import cn.ce.platform_service.apis.entity.ApiEntity;
import cn.ce.platform_service.apis.service.IConsoleApiService;
import cn.ce.platform_service.common.ErrorCodeNo;
import cn.ce.platform_service.common.HttpClientUtil;
import cn.ce.platform_service.common.Result;
import cn.ce.platform_service.common.page.Page;
import cn.ce.platform_service.diyApply.dao.IDiyApplyDao;
import cn.ce.platform_service.diyApply.entity.DiyApplyEntity;
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

	@Resource
	private INewApiDao newApiDao;
	@Resource
	private IDiyApplyDao diyApplyDao;
	@Resource
	private IConsoleApiService consoleApiService;
	
	@Override
	public Result<Apps> findPagedApps(String owner, String name, int pageNum, int pageSize) {
		Result<Apps> result = new Result<>();
		String url = PropertiesUtil.getInstance().getValue("findPagedApps");
		String o$ = Pattern.quote("${o}");// ${o} 所属企业 CE 为中企动力 不填为所有
		String n$ = Pattern.quote("${n}");// ${n} 名称 模糊搜索  不填为所有
		String p$ = Pattern.quote("${p}");// ${p} 当前第几页      
		String z$ = Pattern.quote("${z}");// ${z} 每页显示多少条
		if (StringUtils.isBlank(owner)) {
			owner = "";
		}
		if (StringUtils.isBlank(name)) {
			name = "";
		}

		String replacedurl = url.replaceAll(o$, owner).replaceAll(n$, name).replaceAll(p$, String.valueOf(pageNum))
				.replaceAll(z$, String.valueOf(pageSize));
		_LOGGER.info("请求产品中心Url:"+url);
		Map<String, Class<?>> classMap = new HashMap<String, Class<?>>();
		classMap.put("list", cn.ce.platform_service.diyApply.entity.appsEntity.AppList.class);
		classMap.put("appTypes", cn.ce.platform_service.diyApply.entity.appsEntity.AppTypes.class);

		try {
			/* get请求方法 */
			Apps apps = (Apps) HttpClientUtil.getUrlReturnObject(replacedurl, Apps.class, classMap);
			_LOGGER.info("调用产品Http请求发送成功");
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
			
			_LOGGER.error("findPagedApps http error " + e + "");
			result.setErrorCode(ErrorCodeNo.SYS018);
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

	@Override
	public Result<?> limitScope(String diyApplyId, String openApplyId, String apiName, Integer currentPage, Integer pageSize ) {

		Result<Page<ApiEntity>> result = new Result<Page<ApiEntity>>();
		DiyApplyEntity diyEntity = diyApplyDao.findById(diyApplyId);
		if (null == diyEntity) {
			result.setErrorMessage("当前id不存在", ErrorCodeNo.SYS015);
			return result;
		}
		Map<String, List<String>> limitMap = diyEntity.getLimitList();
		if (limitMap == null || limitMap.size() == 0) {
			result.setErrorMessage("当前定制应用暂时未绑定该开放应用", ErrorCodeNo.SYS017);
			return result;
		}

		Set<String> openApplySet = limitMap.keySet();
		if (openApplySet.contains(openApplyId)) {
			List<String> apiIds = limitMap.get(openApplyId);
			Page<ApiEntity> apiPage = newApiDao.findApiPageByIds(apiIds,new Page<ApiEntity>(currentPage,0,pageSize));
			result.setSuccessData(apiPage);
		} else {
			result.setErrorMessage("当前开放应用不可用", ErrorCodeNo.SYS017);
		}
		return result;
	}
}
