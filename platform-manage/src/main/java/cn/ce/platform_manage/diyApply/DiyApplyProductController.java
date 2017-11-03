package cn.ce.platform_manage.diyApply;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import cn.ce.platform_service.common.Result;
import cn.ce.platform_service.diyApply.entity.appsEntity.Apps;
import cn.ce.platform_service.diyApply.entity.inparameter.RegisterBathAppInParameterEntity;
import cn.ce.platform_service.diyApply.entity.interfaceMessageInfo.InterfaMessageInfoString;
import cn.ce.platform_service.diyApply.entity.tenantAppPage.TenantAppPage;
import cn.ce.platform_service.diyApply.service.IManageDiyApplyService;
import cn.ce.platform_service.diyApply.service.IPlublicDiyApplyService;
import cn.ce.platform_service.util.PageValidateUtil;
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
@Api("manage租户接口")
public class DiyApplyProductController {

	private static Logger _LOGGER = LoggerFactory.getLogger(DiyApplyProductController.class);
	@Resource
	private IManageDiyApplyService manageDiyApplyService;
	@Resource
	private IPlublicDiyApplyService plublicDiyApplyService;

	@RequestMapping(value = "/findPagedApps", method = RequestMethod.GET)
	@ApiOperation("获取所有应用列表")
	public Result<Apps> findPagedApps(@RequestParam(value = "owner", required = false) String owner,
			@RequestParam(required = false) String name,
			@RequestParam(required = true, defaultValue = "10") int pageSize,
			@RequestParam(required = true, defaultValue = "1") int currentPage) {
		
		_LOGGER.info("*************获取所有产品列表************");
		_LOGGER.info("查询条件：");
		_LOGGER.info("所属企业:"+owner);
		_LOGGER.info("名称模糊："+name);
		_LOGGER.info("传入页码："+currentPage);
		_LOGGER.info("页码大小："+pageSize);
		
		return plublicDiyApplyService.findPagedApps(owner, name, PageValidateUtil.checkCurrentPage(currentPage), 
				PageValidateUtil.checkPageSize(pageSize));
	}

	@RequestMapping(value = "findTenantAppsByTenantKey", method = RequestMethod.GET)
	@ApiOperation("获取产品实例 查询带分页")
	public Result<TenantAppPage> findTenantAppsByTenantKey(@RequestParam(value = "key", required = true) String key,
			@RequestParam(required = false)  String appName, @RequestParam(required = true, defaultValue = "10") int pageSize,
			@RequestParam(required = true, defaultValue = "1") int currentPage) {
		return plublicDiyApplyService.findTenantAppsByTenantKeyPage(key, appName, currentPage, pageSize);
	}

	@RequestMapping(value = "registerBathApp", method = RequestMethod.POST)
	@ApiOperation("开发者在开放平台发布应用审核")
	public Result<InterfaMessageInfoString> registerBathApp(
			@RequestParam(value = "tenantId", required = false) String tenantId,
			@RequestBody RegisterBathAppInParameterEntity[] queryVO, HttpServletRequest request,
			HttpServletResponse response) {
		return manageDiyApplyService.registerBathApp(tenantId, JSONArray.fromObject(queryVO).toString());
	}
	
	// 查看当前应用可以访问哪些开放应用下的哪些api
	@RequestMapping(value="/getLimitScope",method=RequestMethod.GET) //查看当前定制应用是否有权限访问某组api或者某个api
	public Result<?> getLimitScope(
			@RequestParam String diyApplyId,
			@RequestParam String openApplyId,
			@RequestParam(required=false) String apiName,
			@RequestParam(required=false, defaultValue="1") Integer currentPage,
			@RequestParam(required=false, defaultValue="10") Integer pageSize){
		
		return plublicDiyApplyService.limitScope(diyApplyId, openApplyId, apiName, PageValidateUtil.checkCurrentPage(currentPage), PageValidateUtil.checkPageSize(pageSize));
	}

}
