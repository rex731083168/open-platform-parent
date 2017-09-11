package cn.ce.app;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import cn.ce.admin.entity.AdminEntity;
import cn.ce.apis.service.IAPIService;
import cn.ce.app.entity.AppEntity;
import cn.ce.app.service.IAppService;
import cn.ce.base.BaseController;
import cn.ce.common.Constants;
import cn.ce.common.Result;
import cn.ce.common.Status;
import cn.ce.page.Page;

/***
 * 服务管理前端控制器
 * 
 * @author lida
 * @date 2017年8月7日21:37:42
 * @see jdk 1.7
 */
@Controller
@RequestMapping("/app")
public class AppsController extends BaseController {

	/** 日志对象 */
	private static Logger logger = Logger.getLogger(AppsController.class);

	@Autowired
	private IAPIService apiService;

	@Autowired
	private IAppService appService;
	
	/***
	 * 添加服务分类
	 * @param request
	 * @param response
	 * @param appname 服务分类名称
	 * @return
	 */
	@RequestMapping(value = "/addgroup", method = RequestMethod.POST)
	@ResponseBody
	public String addGroup(HttpServletRequest request, HttpServletResponse response, @RequestBody AppEntity app) {
		logger.info("---------------->> Action Add new Group! param: " + JSON.toJSONString(app));
		JSONObject result = new JSONObject();
		try {
			
			Result<AppEntity> checkAppIsHave = appService.checkAppIsHave(app);
			
			if (Status.SUCCESS == checkAppIsHave.getStatus()) {
				AdminEntity user = (AdminEntity) session.getAttribute(Constants.SES_LOGIN_USER);
				String uuid = UUID.randomUUID().toString().replace("-", "");
				app.setId(uuid);
				app.setCreateDate(new Date());
				app.setCheckState(2); //后台系统添加服务分类默认审核通过
				app.setUserid(user.getId());
				app.setUsername(user.getUserName());
				appService.addApp(app);
				result.put("code", "1");
				result.put("message", "OK");
				result.put("id", uuid);
			}else{
				result.put("code", "2");
				result.put("message", checkAppIsHave.getMessage());
				return result.toString();
			}
			return result.toString();
		} catch (Exception ex) {
			ex.printStackTrace();
			result.put("code", "0");
			result.put("message", "ERROR");
			return result.toString();
		}
	}

	
	/***
	 * 根据服务分类id删除
	 * @param request
	 * @param response
	 * @param id 服务分类id
	 * @return
	 */
	@RequestMapping(value = "/delgroup", method = RequestMethod.GET)
	@ResponseBody
	public String delGroup(HttpServletRequest request, HttpServletResponse response, String id) {
		logger.info("---------------->> Action del Group! GroupID: " + id);
		JSONObject result = new JSONObject();
		try {
			/** 删除前要求分组内无API存在！ */
			if (apiService.haveAPIs(id)) {
				result.put("code", "2");
				result.put("message", "当前服务分组中存在API接口定义!");
				return result.toString();
			}
			appService.delById(id);
			result.put("code", "1");
			result.put("message", "OK");
			return result.toString();
		} catch (Exception ex) {
			ex.printStackTrace();
			result.put("code", "0");
			result.put("message", "ERROR");
			return result.toString();
		}
	}

	/***
	 * 修改服务分类信息
	 * @param request
	 * @param response
	 * @param id 服务分类id
	 * @param appname 服务分类名称
	 * @return
	 */
	@RequestMapping(value = "/modifygroup", method = RequestMethod.POST)
	@ResponseBody
	public String modifyGroup(HttpServletRequest request, HttpServletResponse response, @RequestBody AppEntity app) {
		logger.info("---------------->> Action modify Group! Param: " + JSON.toJSONString(app));
		JSONObject result = new JSONObject();
		try {
			
			AppEntity appAfter = appService.findById(app.getId());
			if(null == appAfter){
				result.put("code", "2");
				result.put("message", "请求修改的分组不存在！");
			}else{
				app.setNeqId(app.getId());//查询非当前修改数据进行判断
				List<AppEntity> findAppsByEntity = appService.findAppsByEntity(app);
				if (findAppsByEntity.isEmpty()) {
					appAfter.setAppname(app.getAppname().trim());
					appAfter.setAppkey(app.getAppkey().trim());
					appService.modifyById(appAfter);
					result.put("code", "1");
					result.put("message", "OK");
				}else{
					result.put("code", "2");
					result.put("message", "分组名称或分组key不可重复！");
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			result.put("code", "0");
			result.put("message", "ERROR");
		}
		return result.toString();
	}
	
	
	/***
	 * 修改服务分类信息
	 * @param request
	 * @param response
	 * @param id 服务分类id
	 * @param appname 服务分类名称
	 * @return
	 */
	@RequestMapping(value = "/auditgroup", method = RequestMethod.POST)
	@ResponseBody
	public String auditGroupState(HttpServletRequest request, HttpServletResponse response, String id, int checkState,String remark) {
		logger.info("---------------->> Action audit Group! GroupID: " + id + " ; group state: " + checkState + "; remark:" + remark);
		JSONObject result = new JSONObject();
		try {
			AppEntity app = appService.findById(id);
			if(null == app){
				result.put("code", "2");
				result.put("message", "请求修改的分组不存在！");
			}else{
				app.setCheckState(checkState);
				if(checkState == 3 && StringUtils.isNotBlank(remark)){
					app.setCheckMem(remark);
				}
				appService.modifyById(app);
				result.put("code", "1");
				result.put("message", "OK");
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			result.put("code", "0");
			result.put("message", "ERROR");
		}
		return result.toString();
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
	@RequestMapping(value = "/grouplist", method = RequestMethod.POST)
	@ResponseBody
	public String groupList(HttpServletRequest request, HttpServletResponse response,String appname,String checkState,
			@RequestParam(required = false, defaultValue = "1") int currentPage,
			@RequestParam(required = false, defaultValue = "8") int pageSize) {
		logger.info("---------------->> Action Search GroupList! appname: " + appname + "; checkState:" + checkState);
		JSONObject result = new JSONObject();

		try {
//			Map<String,Object> condMap = new HashMap<String, Object>(2);
			AppEntity appParam = new AppEntity();
			
			if(StringUtils.isNotBlank(appname)){
				appParam.setAppname(appname);
			}
			
			if(StringUtils.isNotBlank(checkState)){
				appParam.setCheckState(Integer.valueOf(checkState));
			}
			
			Page<AppEntity> ds = appService.getAppListByDBWhere(appParam, currentPage, pageSize);
			JSONObject jsonObject = JSONObject.parseObject(JSON.toJSONString(ds));

			result.put("data", jsonObject);
			result.put("code", "1");
			result.put("message", "OK");
			return result.toString();
		} catch (Exception e) {
			e.printStackTrace();
			result.put("code", "0");
			result.put("message", "ERROR");
			return result.toString();
		}
	}
}
