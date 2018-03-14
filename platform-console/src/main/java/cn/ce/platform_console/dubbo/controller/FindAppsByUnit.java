package cn.ce.platform_console.dubbo.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import cn.ce.platform_service.common.Result;
import cn.ce.platform_service.dubbapply.entity.Interfaceapplyentity.DubboApps;
import cn.ce.platform_service.dubbapply.service.IGetAppListSercice;

@RestController
@RequestMapping("findAppsByUnit")
public class FindAppsByUnit {

	private static final Logger _LOGGER = LoggerFactory.getLogger(FindAppsByUnit.class);
	@Autowired
	private IGetAppListSercice getAppListSercice;

	@RequestMapping(value = "findAppsByUnit", method = RequestMethod.GET)
	public Result<DubboApps> findAppsByUnit(@RequestParam(required = true) String unit) {

		return getAppListSercice.findAppsByUnit(unit);
	}
}
