package cn.ce.platform_console.apis.controller;

import java.util.ArrayList;
import java.util.List;

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

import cn.ce.platform_service.apis.entity.APIEntity;
import cn.ce.platform_service.apis.service.IAPIService;
import cn.ce.platform_service.apis.service.IApiOauthService;
import cn.ce.platform_service.apisecret.entity.ApiSecretKey;
import cn.ce.platform_service.apisecret.service.IApiSecretKeyService;
import cn.ce.platform_service.app.service.IAppService;
import cn.ce.platform_service.common.Constants;
import cn.ce.platform_service.common.Result;
import cn.ce.platform_service.common.Status;
import cn.ce.platform_service.page.Page;
import cn.ce.platform_service.users.entity.User;

@Controller
@RequestMapping("/apis")
public class ApisController {

	/** 日志对象 */
	private static Logger _LOGGER = Logger.getLogger(ApisController.class);

	@Autowired
	private IAPIService apiService;
	@Autowired
	private IAppService appService;
	@Autowired
	private IApiSecretKeyService apiSecretKeyService;
	@Autowired
	private IApiOauthService oauthService;

	@RequestMapping(value = "/apiVerify", method = RequestMethod.POST)
	@ResponseBody
	public Result<String> apiVerify(
			@RequestParam String apiid,
			@RequestParam(required=false) String userName,
			@RequestParam(required=false) String password
			) {

		return apiService.apiVerify(apiid,userName,password);
	}

	@RequestMapping(value = "/publish", method = RequestMethod.POST)
	@ResponseBody
	public Result<String> publishAPI(HttpSession session, @RequestBody APIEntity apiEntity) {
			
		User user = (User) session.getAttribute(Constants.SES_LOGIN_USER);
		
		_LOGGER.info(JSONObject.toJSONString(apiEntity));
		
		return apiService.publishAPI(user, apiEntity);
	}

	@RequestMapping(value="/modifyApi",method=RequestMethod.POST)
	@ResponseBody
	public Result<String> modifyApi(@RequestBody APIEntity apiEntity){
		
		//校验参数
		return apiService.modifyApi(apiEntity);
	}
	@RequestMapping(value = "/showApi", method = RequestMethod.POST)
	@ResponseBody
	public Result<String> show(String apiId) {
		
		return apiService.show(apiId);
	}

	@RequestMapping(value = "/delApi", method = RequestMethod.GET)
	@ResponseBody
	public Result<String> delAPI(String apiId) {

		return apiService.delById(apiId);
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
			@RequestParam String appId,
			@RequestParam String apiChName){
		
		return apiService.checkApiChName(apiChName,appId);
	}
	@RequestMapping(value="/checkVersion",method=RequestMethod.GET,produces="application/json;charset=utf-8")
	@ResponseBody
	public Result<String> checkVersion(String apiId, String version){
		
		Result<String> result = new Result<String>();
		if(StringUtils.isBlank(apiId)){
			result.setMessage("apiId不能为空");
			return result;
		}if(StringUtils.isBlank(version)){
			result.setMessage("version不能为空");
			return result;
		}
		
		return apiService.checkVersion(apiId,version);
	}

	@RequestMapping(value="/list2",method=RequestMethod.GET)
	@ResponseBody
	public Result<String> showApis2(String appId,String userId,String applyId,int currentPage,int pageSize){
		
		APIEntity apiParam = new APIEntity();
		apiParam.setAppId(appId);
		apiParam.setUserId(userId);
		Result<String> result = new Result<String>();

		try{
			Page<APIEntity> ds = apiService.findApisByEntity(apiParam, currentPage, pageSize);
			
			@SuppressWarnings("unchecked")
			List<APIEntity> list = (List<APIEntity>) ds.getItems();
			
			Page<org.json.JSONObject> page = new Page<org.json.JSONObject>(ds.getCurrentPage(), ds.getTotalNumber(),ds.getPageSize());
			
			List<org.json.JSONObject> list1 = new ArrayList<org.json.JSONObject>();
			
			for (APIEntity apiEntity : list) {
				//将正式的url地址设置为空，不能让用户看到
				apiEntity.setTestEndPoint("http://************");
				
				org.json.JSONObject tempJson = new org.json.JSONObject(apiEntity);
				
				int size = oauthService.findByApplyId(applyId,apiEntity.getId()).size();
				if(size > 0){
					tempJson.put("isUseful", false);//可申请
				}else{
					tempJson.put("isUseful", true);//不可申请
				}
				_LOGGER.info(tempJson.get("isUseful"));
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
		_LOGGER.info("-------------->> Action!  show apis.  ======>> appid: " + appid + " userid:" + userid);
		_LOGGER.info("-------------->> Action!  show apis. currentPage: " + currentPage + "  pageSize:" + pageSize);
		JSONObject obj = new JSONObject();
		try {
			APIEntity apiParam = new APIEntity();
			apiParam.setAppId(appid);
			apiParam.setUserId(userid);

			Page<APIEntity> ds = apiService.findApisByEntity(apiParam, currentPage, pageSize);
			@SuppressWarnings("unchecked")
			List<APIEntity> items = (List<APIEntity>) ds.getItems();
			List<String> apiIdList = new ArrayList<>(items.size());
			// 构建apiId集合
			for (APIEntity apiEntity : items) {
				apiIdList.add(apiEntity.getId());
			}

			// 根据apiId加载秘钥信息
			List<ApiSecretKey> findSecretKeyByApiIds = apiSecretKeyService.findSecretKeyByApiIds(apiIdList);
			List<ApiSecretKey> secretKeyList;

			// 循环Api集合查找秘钥apiSecretKeyList进行组装Api信息
			for (APIEntity apiEntity : items) {
				secretKeyList = new ArrayList<>();
				for (ApiSecretKey apiSecretKey : findSecretKeyByApiIds) {
					if (apiSecretKey.getApiId().equals(apiEntity.getId())) {
						secretKeyList.add(apiSecretKey);
					}
				}
				apiEntity.setAppEntity((appService.findById(apiEntity.getAppId())));
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

    @SuppressWarnings("unchecked")
	@Deprecated //使用密钥授权已过期。使用者查询api列表
	@RequestMapping(value = "/uselist", method = RequestMethod.GET)
	@ResponseBody
	public String showUseAPIs(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(required = true) String userid,
			@RequestParam(required = false, defaultValue = "1") int currentPage,
			@RequestParam(required = false, defaultValue = "8") int pageSize) {
		_LOGGER.info("-------------->> Action!  show use apis.  ======>> userid:" + userid);
		_LOGGER.info("-------------->> Action!  show use. currentPage: " + currentPage + "  pageSize:" + pageSize);
		JSONObject obj = new JSONObject();
		try {
			ApiSecretKey param = new ApiSecretKey();
			param.setUserId(userid);
			Page<ApiSecretKey> ds = apiSecretKeyService.findSecretKeyEntityPage(param, currentPage, pageSize);
			APIEntity api = new APIEntity();
			// 构建apiId集合
			for (ApiSecretKey apiSecret : (List<ApiSecretKey>) ds.getItems()) {
				api = apiService.findById(apiSecret.getApiId());
				api.setAppEntity(appService.findById(api.getAppId()));
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
