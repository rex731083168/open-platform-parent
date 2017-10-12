package cn.ce.platform_manage.openapp;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.elasticsearch.action.admin.cluster.snapshots.status.TransportNodesSnapshotsStatus.Request;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;

import cn.ce.platform_manage.base.BaseController;
import cn.ce.platform_service.apis.service.IAPIService;
import cn.ce.platform_service.common.ErrorCodeNo;
import cn.ce.platform_service.common.Result;
import cn.ce.platform_service.openApply.entity.OpenApplyEntity;
import cn.ce.platform_service.openApply.service.IManageOpenApplyService;
import cn.ce.platform_service.page.Page;
import net.sf.json.JSONArray;

/***
 * 服务管理前端控制器
 * 
 * @author lida
 * @date 2017年8月7日21:37:42
 * @see jdk 1.8
 */
@RestController
@RequestMapping("/openapp")
public class OpenAppsController extends BaseController {

	private static Logger _LOGGER = Logger.getLogger(OpenAppsController.class);

	@Resource
	private IAPIService apiService;

	@Resource
	private IManageOpenApplyService openApplyService;

	/**
	 * 
	 * @param ids
	 *            jsondemo["1","2","3"]
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/batchUpdate", method = RequestMethod.POST)
	public Result<String> batchUpdate(@RequestBody String ids) {
		JSONArray jsonArray = JSONArray.fromObject(ids);
		List<String> lisids = jsonArray.toList(jsonArray);
		return openApplyService.batchUpdate(lisids);
	}

	/**
	 * 
	 * @Title: auditGroupState @Description: TODO(这里用一句话描述这个方法的作用) @param : @param
	 *         id @param : @param checkState @param : @param remark @throws
	 */
	@RequestMapping(value = "/auditGroup", method = RequestMethod.POST)
	public Result<String> auditGroupState(String id, int checkState, String remark) {
		_LOGGER.info("---------------->> Action audit Group! GroupID: " + id + " ; group state: " + checkState
				+ "; remark:" + remark);

		return openApplyService.auditGroup(id, checkState, remark);
	}

	/***
	 * 按照条件查询服务分类列表方法
	 * 
	 * @author lida
	 * @date 2017年8月8日15:11:11
	 * @param request
	 * @param response
	 * @param appName
	 *            服务分类名称
	 * @param checkState
	 *            服务分类状态
	 * @param currentPage
	 *            当前页
	 * @param pageSize
	 *            分页数
	 * @return
	 */
	@RequestMapping(value = "/groupList", method = RequestMethod.POST)
	@ResponseBody
	public Result<Page<OpenApplyEntity>> groupList(HttpServletRequest request, HttpServletResponse response,
			String appName, String userName, String enterpriseName, String checkState,
			@RequestParam(required = false, defaultValue = "1") int currentPage,
			@RequestParam(required = false, defaultValue = "10") int pageSize) {
		_LOGGER.info("---------------->> Action Search GroupList! appname: " + appName + ";  appName:" + appName
				+ ";userName:" + userName + "checkState:" + checkState);

		return openApplyService.groupList1(appName, userName, enterpriseName, checkState, currentPage, pageSize);
	}
}
