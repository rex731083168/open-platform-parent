package cn.ce.apis.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

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

import cn.ce.apis.entity.APIEntity;
import cn.ce.apis.entity.ApiVersion;
import cn.ce.apis.service.IAPIService;
import cn.ce.apis.service.IApiOauthService;
import cn.ce.apisecret.entity.ApiSecretKey;
import cn.ce.apisecret.service.IApiSecretKeyService;
import cn.ce.app.entity.AppEntity;
import cn.ce.app.service.IAppService;
import cn.ce.common.Constants;
import cn.ce.common.Result;
import cn.ce.common.Status;
import cn.ce.common.gateway.GatewayUtils;
import cn.ce.gateway.entity.GatewayColonyEntity;
import cn.ce.page.Page;
import cn.ce.users.entity.User;

@Controller
@RequestMapping("/apis")
public class ApisController {

	/** 日志对象 */
	private static Logger logger = Logger.getLogger(ApisController.class);

	@Autowired
	private IAPIService apiService;
	@Autowired
	private IAppService appService;
	@Autowired
	private IApiSecretKeyService secretKeyService;
	@Autowired
	private IApiOauthService oauthService;

	@RequestMapping(value = "/apiVerify", method = RequestMethod.POST)
	@ResponseBody
	public String apiVerify(HttpServletRequest request, HttpServletResponse response, String apiid) {

		JSONObject obj = new JSONObject();
		try {
			APIEntity api = apiService.findById(apiid);
			api.setCheckState(1);

			apiService.updateAPI(api);
			obj.put("code", "1");
			obj.put("message", "OK");
			return obj.toString();
		} catch (Exception e) {
			e.printStackTrace();
			obj.put("code", "0");
			obj.put("message", "ERROR");
			return obj.toString();
		}
	}

	@RequestMapping(value = "/publish", method = RequestMethod.POST)
	@ResponseBody
	public String publishAPI(HttpServletRequest request, HttpServletResponse response, @RequestBody APIEntity apientity) {

		JSONObject ret = new JSONObject();

//		try {
			
			HttpSession session = request.getSession();
			User user = (User) session.getAttribute(Constants.SES_LOGIN_USER);
			String str = JSONObject.toJSONString(apientity);
			logger.info(str);
			
			//如果apiId存在，则修改api
			if(apientity.getId() != null ){
				apiService.updateAPI(apientity);
				ret.put("code", 1);
				ret.put("message", "修改成功");
				return ret.toString();
			}
			
			// 第一次添加接口,并且选择未开启版本控制
			if (apientity.getApiversion() == null || StringUtils.isBlank(apientity.getApiversion().getVersion())) {
				apientity.setId(UUID.randomUUID().toString().replace("-", ""));
				apientity.setUserid(user.getId());
				apientity.setUsername(user.getUsername());
				apientity.setCreatetime(new Date());

				ApiVersion version = new ApiVersion();
				version.setApiId(apientity.getId());
				apientity.setApiversion(version);

				// 过滤apienname不能以/开头和结尾
				apientity.setApienname(apientity.getApienname().replaceAll("/", ""));

				apiService.addAPI(apientity);

			} else {
				// 开启版本控制
				
				apientity.setId(UUID.randomUUID().toString().replace("-", ""));
				apientity.setUserid(user.getId());
				apientity.setUsername(user.getUsername());
				apientity.setCreatetime(new Date());

				
				int num = apiService.updApiVersionByApiid(apientity.getApiversion().getApiId());
				logger.info("----->将原来其他版本的api的newVersion字段全部修改为false，一共修改了"+num+"条数据");
				
				// TODO 前端传入版本号和newVersion字段吗？
				apientity.getApiversion().setNewVersion(true);

				//如果没有传入版本的apiId则重新生成apiId
				if(StringUtils.isBlank(apientity.getApiversion().getApiId())){
					apientity.getApiversion().setApiId(apientity.getId());
				}
				
				apiService.addAPI(apientity);
				
				logger.info("------新添加的数据为："+new org.json.JSONObject(apientity).toString());
			}
			ret.put("code", "1");
			ret.put("message", "OK");
			return ret.toString();
//		} 
//		catch (Exception ex) {
//			ex.printStackTrace();
//			ret.put("code", "0");
//			ret.put("message", "添加发生异常，异常原因为："+ex.getMessage());
//			return ret.toString();
//		}
	}

	@RequestMapping(value="/modifyApi",method=RequestMethod.POST)
	@ResponseBody
	public Result<String> modifyApi(@RequestBody APIEntity apientity){
		
		//校验参数
		return apiService.modifyApi(apientity);
	}
	@RequestMapping(value = "/showapi", method = RequestMethod.POST)
	@ResponseBody
	public String show(HttpServletRequest request, HttpServletResponse response, String apiid) {

		JSONObject obj = new JSONObject();
		try {
			APIEntity api = apiService.findById(apiid);

			if (api == null) {
				obj.put("code", "0");
				obj.put("message", "您访问的api不存在!");
				return obj.toString();
			}

			String appId = api.getAppid();
			AppEntity app = appService.findById(appId);

			JSONObject jsonObject = JSONObject.parseObject(JSON.toJSONString(api));

			// 添加网关访问地址
			AppEntity appEntity = appService.findById(api.getAppid());
			
			List<GatewayColonyEntity> colList = GatewayUtils.getAllGatewayColony();
			List<String> gatewayUrlList = new ArrayList<String>();

			for (GatewayColonyEntity gatewayColonyEntity : colList) {
				
				gatewayUrlList.add(gatewayColonyEntity.getColUrl() +"/"+appEntity.getAppkey()+"/" + api.getApienname()+"/"+api.getApiversion().getVersion()+"/");
			}
			jsonObject.put("gatewayUrls", gatewayUrlList);

			jsonObject.put("appname", app.getAppname());
			obj.put("data", jsonObject);
			obj.put("code", "1");
			obj.put("message", "OK");
			return obj.toString();
		} catch (Exception e) {
			e.printStackTrace();
			obj.put("code", "0");
			obj.put("message", "ERROR");
			return obj.toString();
		}
	}

	@RequestMapping(value = "/delapi", method = RequestMethod.GET)
	@ResponseBody
	public String delAPI(HttpServletRequest request, HttpServletResponse response, String apiid) {
		logger.info("--------------->> Action!  del api. ID: " + apiid);
		JSONObject object = new JSONObject();
		try {
			apiService.delById(apiid.trim());
			// secretKeyService.delApi(apiid.trim());
			object.put("code", "1");
			object.put("message", "OK");
			return object.toString();
		} catch (Exception ex) {
			object.put("code", "1");
			object.put("message", "ERROR");

			return object.toString();
		}
	}

	@RequestMapping(value="/checkApiEnName",method=RequestMethod.GET)
	@ResponseBody
	public Result<String> checkApiEnName(HttpServletRequest request,HttpServletResponse response,
			String appId,
			String apiEnName){
		
		return apiService.checkApiEnName(apiEnName,appId);
	}
	
	@RequestMapping(value="/checkApiChName",method=RequestMethod.POST)
	@ResponseBody
	public Result<String> checkApiChName(HttpServletRequest request,HttpServletResponse response,
			String appId,
			String apiChName){
		
		return apiService.checkApiChName(apiChName,appId);
	}
	@RequestMapping(value="/checkVersion",method=RequestMethod.GET,produces="application/json;charset=utf-8")
	@ResponseBody
	public Result<String> checkVersion(HttpServletRequest request, HttpServletResponse response,
			String apiId, String version){
		
		Result<String> result = new Result<String>();
		if(apiId == null || apiId.trim()==""){
			result.setMessage("apiId不能为空");
			return result;
		}if(version == null || version.trim() ==""){
			result.setMessage("version不能为空");
			return result;
		}
		
		return apiService.checkVersion(apiId,version);
	}

	@RequestMapping(value="/list2",method=RequestMethod.GET)
	@ResponseBody
	public Result<String> showApis2(String appid,String userid,String applyId,int currentPage,int pageSize){
		
		APIEntity apiParam = new APIEntity();
		apiParam.setAppid(appid);
		apiParam.setUserid(userid);
		Result<String> result = new Result<String>();

		try{
			Page<APIEntity> ds = apiService.findApisByEntity(apiParam, currentPage, pageSize);
			
			List<APIEntity> list = (List<APIEntity>) ds.getItems();
			
			Page<org.json.JSONObject> page = new Page<org.json.JSONObject>(ds.getCurrentPage(), ds.getTotalNumber(),ds.getPageSize());
			
			List<org.json.JSONObject> list1 = new ArrayList<org.json.JSONObject>();
			
			for (APIEntity apiEntity : list) {
				//将正式的url地址设置为空，不能让用户看到
				apiEntity.setTestendpoint("http://************");
				
				org.json.JSONObject tempJson = new org.json.JSONObject(apiEntity);
				
				int size = oauthService.findByApplyId(applyId,apiEntity.getId()).size();
				if(size > 0){
					tempJson.put("isUseful", false);//可申请
				}else{
					tempJson.put("isUseful", true);//不可申请
				}
				logger.info(tempJson.get("isUseful"));
				list1.add(tempJson);
			}
			page.setItems(list1);
			
			result.setStatus(Status.SUCCESS);
			result.setMessage("获取数据成功");
			result.setData(new org.json.JSONObject(page).toString());
			return result;
		}catch(Exception e){
			result.setMessage("系统发生异常");
			return result;
		}
	}
	@Deprecated //使用密钥授权，已过期
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	@ResponseBody
	public String showAPIs(HttpServletRequest request, HttpServletResponse response, String appid, String userid,
			@RequestParam(required = false, defaultValue = "1") int currentPage,
			@RequestParam(required = false, defaultValue = "8") int pageSize) {
		logger.info("-------------->> Action!  show apis.  ======>> appid: " + appid + " userid:" + userid);
		logger.info("-------------->> Action!  show apis. currentPage: " + currentPage + "  pageSize:" + pageSize);
		JSONObject obj = new JSONObject();
		try {
			APIEntity apiParam = new APIEntity();
			apiParam.setAppid(appid);
			apiParam.setUserid(userid);

			Page<APIEntity> ds = apiService.findApisByEntity(apiParam, currentPage, pageSize);
			List<APIEntity> items = (List<APIEntity>) ds.getItems();
			List<String> apiIdList = new ArrayList<>(items.size());
			// 构建apiId集合
			for (APIEntity apiEntity : items) {
				apiIdList.add(apiEntity.getId());
			}

			// 根据apiId加载秘钥信息
			List<ApiSecretKey> findSecretKeyByApiIds = secretKeyService.findSecretKeyByApiIds(apiIdList);
			List<ApiSecretKey> secretKeyList;

			// 循环Api集合查找秘钥apiSecretKeyList进行组装Api信息
			for (APIEntity apiEntity : items) {
				secretKeyList = new ArrayList<>();
				for (ApiSecretKey apiSecretKey : findSecretKeyByApiIds) {
					if (apiSecretKey.getApiId().equals(apiEntity.getId())) {
						secretKeyList.add(apiSecretKey);
					}
				}
				apiEntity.setApp(appService.findById(apiEntity.getAppid()));
				apiEntity.setApiSecret(secretKeyList);
			}

			JSONObject jsonObject = JSONObject.parseObject(JSON.toJSONString(ds));

			obj.put("code", "1");
			obj.put("message", "OK");
			obj.put("data", jsonObject);
			return obj.toString();
		} catch (Exception e) {
			e.printStackTrace();
			obj.put("code", "0");
			obj.put("message", "ERROR");
			return obj.toString();
		}
	}

    @Deprecated //使用密钥授权已过期。使用者查询api列表
	@RequestMapping(value = "/uselist", method = RequestMethod.GET)
	@ResponseBody
	public String showUseAPIs(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(required = true) String userid,
			@RequestParam(required = false, defaultValue = "1") int currentPage,
			@RequestParam(required = false, defaultValue = "8") int pageSize) {
		logger.info("-------------->> Action!  show use apis.  ======>> userid:" + userid);
		logger.info("-------------->> Action!  show use. currentPage: " + currentPage + "  pageSize:" + pageSize);
		JSONObject obj = new JSONObject();
		try {
			ApiSecretKey param = new ApiSecretKey();
			param.setUserId(userid);
			Page<ApiSecretKey> ds = secretKeyService.findSecretKeyEntityPage(param, currentPage, pageSize);
			APIEntity api = new APIEntity();
			// 构建apiId集合
			for (ApiSecretKey apiSecret : (List<ApiSecretKey>) ds.getItems()) {
				api = apiService.findById(apiSecret.getApiId());
				api.setApp(appService.findById(api.getAppid()));
				apiSecret.setApi(api);
			}
			JSONObject jsonObject = JSONObject.parseObject(JSON.toJSONString(ds));
			obj.put("code", "1");
			obj.put("message", "OK");
			obj.put("data", jsonObject);
		} catch (Exception e) {
			e.printStackTrace();
			obj.put("code", "0");
			obj.put("message", "ERROR");
		}
		return obj.toString();
	}

}
