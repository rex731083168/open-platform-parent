package cn.ce.app.controller;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;

import cn.ce.apis.service.IAPIService;
import cn.ce.app.entity.AppEntity;
import cn.ce.app.service.IAppService;
import cn.ce.common.Constants;
import cn.ce.common.Result;
import cn.ce.common.Status;
import cn.ce.page.Page;
import cn.ce.users.entity.User;

@Controller
@RequestMapping("/app")
public class AppController {

	/** 日志对象 */
	private static Logger logger = Logger.getLogger(AppController.class);

	@Autowired
	private IAppService appService;
    @Autowired
    private IAPIService apiService;

	@RequestMapping(value = "/applist", method = RequestMethod.POST)
	@ResponseBody
	public String appList(HttpServletRequest request,
			HttpServletResponse response) {
		JSONObject result = new JSONObject();

		try {
			List<AppEntity> list = appService.getAll();

			JSONArray jsonArray = new JSONArray();
			JSONObject data = new JSONObject();
			for (int i = 0; i < list.size(); i++) {
				JSONObject obj = new JSONObject();
				AppEntity app = list.get(i);
				obj.put("id", app.getId());
				obj.put("appname", app.getAppname());
				jsonArray.put(obj);
			}
			data.put("items", jsonArray);
			result.put("data", data);
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
	
	@RequestMapping(value="deleteById",method=RequestMethod.POST)
	@ResponseBody
	public Result<String> deleteById(HttpServletRequest request,HttpServletResponse response,
			String appId){
		
		return appService.deleteById(appId);
	}
	
	@RequestMapping(value = "/addgroup", method = RequestMethod.POST)
	@ResponseBody
	public String addGroup(HttpServletRequest request, HttpServletResponse response, HttpSession session,
			@RequestBody AppEntity app) {
		logger.info("---------------->> Action Add new Group! app details: " + JSON.toJSONString(app));
		JSONObject result = new JSONObject();
		try {
			
			Result<AppEntity> checkAppIsHave = appService.checkAppIsHave(app);
			
			if (Status.SUCCESS == checkAppIsHave.getStatus()) {
				User user = (User) session.getAttribute(Constants.SES_LOGIN_USER);
				String uuid = UUID.randomUUID().toString().replace("-", "");
				app.setId(uuid);
				app.setCreateDate(new Date());
				if (user.getUserType() == 0) {
					app.setCheckState(2); // 后台系统添加服务分类默认审核通过
				} else {
					app.setCheckState(0);
				}
				app.setUserid(user.getId());
				app.setUsername(user.getUsername());

				// appKey不能以/开头和结尾
				if (app.getAppkey().startsWith("/") || app.getAppkey().endsWith("/")) {
					result.put("message", "appKey不能以/开头和/结尾");
					return result.toString();
				}
				appService.addApp(app);
				result.put("code", "1");
				result.put("message", "OK");
				result.put("id", uuid);
			} else{
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
	
	@RequestMapping(value = "/modifygroup", method = RequestMethod.POST)
	@ResponseBody
	public String modifyGroup(HttpServletRequest request, HttpServletResponse response, @RequestBody AppEntity app) {
		logger.info("---------------->> Action modify Group! Param: " + JSON.toJSONString(app));
		JSONObject result = new JSONObject();
		try {
			AppEntity appAfter = appService.findById(app.getId());
			if (null == appAfter) {
				result.put("code", "2");
				result.put("message", "请求修改的分组不存在！");
			} else {
				app.setNeqId(app.getId());// 查询非当前修改数据进行判断
				List<AppEntity> findAppsByEntity = appService.findAppsByEntity(app);
				if (findAppsByEntity.isEmpty()) {
					appAfter.setAppname(app.getAppname().trim());
					appAfter.setAppkey(app.getAppkey().trim());
					appService.modifyById(appAfter);
					result.put("code", "1");
					result.put("message", "OK");
				} else {
					result.put("code", "2");
					result.put("message", "分组名称或分组key不可重复！");
				}
			}
			return result.toString();
		} catch (Exception ex) {
			ex.printStackTrace();
			result.put("code", "0");
			result.put("message", "ERROR");
			return result.toString();
		}
	}
	
	@RequestMapping(value = "/grouplist", method = RequestMethod.POST)
	@ResponseBody
	public String groupList(HttpServletRequest request, HttpServletResponse response, String userid,
			@RequestParam(required = false, defaultValue = "1") int currentPage,
			@RequestParam(required = false, defaultValue = "8") int pageSize) {
		logger.info("---------------->> Action Add new Group! userid: " + userid);
		JSONObject result = new JSONObject();
		try {
			AppEntity entity = new AppEntity();
			entity.setUserid(userid);
			Page<AppEntity> ds = appService.findAppsByEntity(entity, currentPage, pageSize);
			com.alibaba.fastjson.JSONObject jsonObject = com.alibaba.fastjson.JSONObject.parseObject(JSON.toJSONString(ds));
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

	@RequestMapping(value = "/submitVerify", method = RequestMethod.POST)
	@ResponseBody
	public String submitVerify(HttpServletRequest request, HttpServletResponse response, String id) {
		logger.info("---------------->> Action modify Group! GroupID: " + id + "  + id");
		JSONObject result = new JSONObject();
		try {
			AppEntity app = appService.findById(id);
			app.setCheckState(1);
			appService.modifyById(app);
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

}
