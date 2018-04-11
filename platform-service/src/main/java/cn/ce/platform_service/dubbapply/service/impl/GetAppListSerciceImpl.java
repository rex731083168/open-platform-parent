package cn.ce.platform_service.dubbapply.service.impl;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import cn.ce.platform_service.common.ErrorCodeNo;
import cn.ce.platform_service.common.HttpClientUtil;
import cn.ce.platform_service.common.Result;
import cn.ce.platform_service.dubbapply.entity.Interfaceapplyentity.DubboApps;
import cn.ce.platform_service.dubbapply.service.IGetAppListSercice;
import cn.ce.platform_service.util.PropertiesUtil;

@Service("getAppListSercice")
public class GetAppListSerciceImpl implements IGetAppListSercice {
	/** 日志对象 */
	private static Logger _LOGGER = Logger.getLogger(GetAppListSerciceImpl.class);

	@Override
	public Result<DubboApps> findAppsByUnit(String unit, String appName, Integer currentPage, Integer pageSize) {
		Result<DubboApps> result = new Result<>();
		String url = PropertiesUtil.getInstance().getValue("dubbo_app_interfaceurl");
		String unit$ = Pattern.quote("${unit}");// ${o} 所属企业 CE 为中企动力 不填为所有
		String replacedurl = url.replaceAll(unit$, unit);
		_LOGGER.info("请求产品中心Url:" + url);
		Map<String, Class<?>> classMap = new HashMap<String, Class<?>>();
		classMap.put("list", cn.ce.platform_service.diyApply.entity.appsEntity.AppList.class);
		classMap.put("appTypes", cn.ce.platform_service.diyApply.entity.appsEntity.AppTypes.class);

		try {
			/* get请求方法 */
			DubboApps apps = (DubboApps) HttpClientUtil.getUrlReturnObject(replacedurl, DubboApps.class, classMap);
			_LOGGER.info("调用产品Http请求发送成功");
			if (apps.getStatus().equals("200") || apps.getStatus().equals("110")) {
				result.setSuccessData(apps);
				return result;
			} else {
				_LOGGER.error("dubbo_app_interfaceurl data http getfaile return code :" + apps.getMsg() + " ");
				result.setErrorCode(ErrorCodeNo.SYS006);
				return result;
			}
		} catch (Exception e) {

			_LOGGER.error("dubbo_app_interfaceurl http error " + e + "");
			result.setErrorCode(ErrorCodeNo.SYS018);
			result.setErrorMessage("请求失败");
			return result;
		}
	}

}
