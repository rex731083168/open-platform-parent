package cn.ce.platform_manage.app;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;

import cn.ce.platform_manage.base.BaseController;
import cn.ce.platform_service.apis.service.IAPIService;
import cn.ce.platform_service.app.entity.AppEntity;
import cn.ce.platform_service.app.service.IAppService;
import cn.ce.platform_service.common.ErrorCodeNo;
import cn.ce.platform_service.common.Result;
import cn.ce.platform_service.page.Page;

/***
 * 服务管理前端控制器
 * 
 * @author lida
 * @date 2017年8月7日21:37:42
 * @see jdk 1.8
 */
@RestController
@RequestMapping("/app")
public class AppsController extends BaseController {

	private static Logger _LOGGER = Logger.getLogger(AppsController.class);

	@Resource
	private IAPIService apiService;

	@Resource
	private IAppService appService;
	
	/***
	 * 添加服务分类
	 * @param request
	 * @param response
	 * @param appname 服务分类名称
	 * @return
	 */
	@RequestMapping(value = "/addGroup", method = RequestMethod.POST)
	public Result<String> addGroup(@RequestBody AppEntity app) {
		_LOGGER.info("---------------->> Action Add new Group! param: " + JSON.toJSONString(app));
		
		return appService.addGroup1(session,app);
	}

	
	/***
	 * 根据服务分类id删除
	 * @param request
	 * @param response
	 * @param id 服务分类id
	 * @return
	 */
	@RequestMapping(value = "/delGroup", method = RequestMethod.GET)
	public Result<String> delGroup(@RequestParam String id) {
		_LOGGER.info("---------------->> Action del Group! GroupID: " + id);
		
		return appService.deleteGroup(id);
	}

	/***
	 * 修改服务分类信息
	 * @param request
	 * @param response
	 * @param id 服务分类id
	 * @param appname 服务分类名称
	 * @return
	 */
	@RequestMapping(value = "/modifyGroup", method = RequestMethod.POST)
	public Result<String> modifyGroup(@RequestBody AppEntity app) {
		_LOGGER.info("---------------->> Action modify Group! Param: " + JSON.toJSONString(app));

		return appService.modifyGroup1(app);
	}
	
	
	/***
	 * 修改服务分类信息
	 * @param request
	 * @param response
	 * @param id 服务分类id
	 * @param appname 服务分类名称
	 * @return
	 */
	@RequestMapping(value = "/auditGroup", method = RequestMethod.POST)
	public Result<String> auditGroupState(String id, int checkState,String remark) {
		_LOGGER.info("---------------->> Action audit Group! GroupID: " + id + " ; group state: " + checkState + "; remark:" + remark);
		
		return appService.auditGroup(id,checkState,remark);
	}

	/***
	 * 按照条件查询服务分类列表方法
	 * @author lida
	 * @date 2017年8月8日15:11:11
	 * @param request
	 * @param response
	 * @param appName 服务分类名称
	 * @param checkState 服务分类状态
	 * @param currentPage 当前页
	 * @param pageSize 分页数
	 * @return
	 */
	@RequestMapping(value = "/groupList", method = RequestMethod.POST)
	@ResponseBody
	public Result<Page<AppEntity>> groupList(HttpServletRequest request, HttpServletResponse response,String appName,String checkState,
			@RequestParam(required = false, defaultValue = "1") int currentPage,
			@RequestParam(required = false, defaultValue = "8") int pageSize) {
		_LOGGER.info("---------------->> Action Search GroupList! appname: " + appName + "; checkState:" + checkState);

		return appService.groupList1(appName,checkState,currentPage,pageSize);
	}
}
