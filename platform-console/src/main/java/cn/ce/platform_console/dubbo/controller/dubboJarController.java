package cn.ce.platform_console.dubbo.controller;

import java.io.File;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import cn.ce.platform_service.common.Result;
import cn.ce.platform_service.dubbapply.entity.Interfaceapplyentity.DubboApps;
import cn.ce.platform_service.dubbapply.service.IDubboApplyService;
import cn.ce.platform_service.dubbapply.service.IGetAppListSercice;
import cn.ce.platform_service.util.PageValidateUtil;

@RestController
@RequestMapping("findAppsByUnit")
public class dubboJarController {

	private static final Logger _LOGGER = LoggerFactory.getLogger(dubboJarController.class);
	@Autowired
	private IGetAppListSercice getAppListSercice;
	@Resource
	private IDubboApplyService dubboApplyService;

	/**
	 * @Title: findPhysicsApps
	 * @Description: 获取物理应用列表
	 * @author: makangwei 
	 * @date:   2018年4月10日 下午5:54:21 
	 */
	@RequestMapping(value="/findPhysicApps", method=RequestMethod.POST)
	public Result<?> findLogicalApps(){
		// TODO 获取逻辑应用。需要王佳接口支持
		return null;
	}
	
	@RequestMapping(value = "/findAppsByUnit", method = RequestMethod.GET)
	public Result<DubboApps> findAppsByUnit(@RequestParam(required = true) String unit,
		@RequestParam(required=false)String appName,
		@RequestParam(required=false, defaultValue="1")Integer currentPage,
		@RequestParam(required=false, defaultValue="10")Integer pageSize) {

		return getAppListSercice.findAppsByUnit(unit,appName
				,PageValidateUtil.checkCurrentPage(currentPage),PageValidateUtil.checkPageSize(pageSize));
	}
	
	/**
	 * 
	 * @Title: uploadMainJar
	 * @Description: 上传主jar
	 * @author: makangwei 
	 * @date:   2018年4月11日 上午10:06:54 
	 */
	@RequestMapping(value="/uploadMainJar", method=RequestMethod.POST)
	public Result<?> uploadMainJar(@RequestParam("file") MultipartFile mainJar){
		
		return dubboApplyService.uploadMainJar(mainJar);
	}
	
	@RequestMapping(value="/uploadDepJarJar", method=RequestMethod.POST)
	public Result<?> uploadDepJar(@RequestParam(required=true) String mainJarId,
			@RequestParam(required=true) MultipartFile[] depJar ){
		
		return dubboApplyService.uploadDepJar(mainJarId,depJar);
	}

}
