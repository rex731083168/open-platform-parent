package cn.ce.platform_service.dubbapply.service;

import cn.ce.platform_service.common.Result;
import cn.ce.platform_service.dubbapply.entity.Interfaceapplyentity.DubboApps;

public interface IGetAppListSercice {

	/**
	 * 获取物理应用列表
	 * 
	 * @param unit
	 * @return
	 */
	public Result<DubboApps> findAppsByUnit(String unit);
}
