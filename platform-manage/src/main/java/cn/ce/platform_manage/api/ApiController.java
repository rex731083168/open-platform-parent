package cn.ce.platform_manage.api;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;

import cn.ce.platform_service.apis.entity.APIEntity;
import cn.ce.platform_service.apis.service.IAPIService;
import cn.ce.platform_service.app.entity.AppEntity;
import cn.ce.platform_service.app.service.IAppService;
import cn.ce.platform_service.common.Result;
import cn.ce.platform_service.common.gateway.GatewayUtils;
import cn.ce.platform_service.gateway.entity.GatewayColonyEntity;
import cn.ce.platform_service.gateway.service.GatewayApiKeyService;
import cn.ce.platform_service.page.Page;

/**
* @Description : 后台api管理
* @Author : makangwei
* @Date : 2017年8月18日
*/
@Controller
@RequestMapping("/api")
public class ApiController {
	

	private static Logger logger = LoggerFactory.getLogger(ApiController.class);
	
	@Autowired
	private GatewayApiKeyService gatewayApiKeyService;
    @Autowired
    private IAPIService apiService;
    @Autowired
    private IAppService appService;
	/**
	 * 管理员审核通过api
	 * 
	 * @Description : 说明
	 * @Author : makangwei
	 * @Date : 2017年8月14日
	 * @param request
	 * @param response
	 * @param apiId	api唯一表示
	 * @param checkState
	 *            审核状态 2：通过，3：拒绝
	 * @param checkMem
	 *            如果审核失败，此字段输入审核失败原因
	 * @return
	 */
	@RequestMapping(value = "/auditApi", method = RequestMethod.GET, produces = "application/json;charset=utf-8")
	@ResponseBody
	public Result<String> auditApi(HttpServletRequest request, HttpServletResponse response, String apiId,
			Integer checkState, String checkmem) {

		logger.info("----------apiId:" + apiId);
		logger.info("---------checkState:" + checkState);

		Result<String> result = new Result<String>();
		if (apiId == null || apiId.trim() == "") {
			result.setMessage("apiId不能为空");
			return result;
		}
		if (checkState == null || checkState > 3 || checkState < 0) {
			result.setMessage("审核状态不存在，请输入正确的审核状态");
			return result;
		}

		return gatewayApiKeyService.auditApi(apiId, checkState, checkmem);

	}

	/**
	 * @Description : 管理员审核通过api，使用oauth添加api到网关
	 * @Author : makangwei
	 * @Date : 2017年8月21日
	 * @param request
	 * @param response
	 * @param apiId
	 *            api唯一表示
	 * @param checkState
	 *            审核状态 2：通过，3：拒绝
	 * @param checkMem
	 *            如果审核失败，此字段输入审核失败原因
	 * @return
	 */
	@RequestMapping(value = "/auditApi2", method = RequestMethod.POST, produces = "application/json;charset=utf-8")
	@ResponseBody
	public Result<String> auditApi2(HttpServletRequest request, HttpServletResponse response, String apiId,
			Integer checkState, String checkMem) { // checkMem如过审核不通过，将审核失败的原因通过该字段传入

		logger.info("----------apiId:" + apiId);
		logger.info("----------checkState:" + checkState);
		logger.info("----------checkMem" + checkMem);

		Result<String> result = new Result<String>();
		if (apiId == null || apiId.trim() == "") {
			result.setMessage("apiId不能为空");
			return result;
		}
		if (checkState == null || checkState > 3 || checkState < 0) {
			result.setMessage("审核状态不存在，请输入正确的审核状态");
			return result;
		}

		return gatewayApiKeyService.auditApi2(apiId, checkState, checkMem);
	}

	@RequestMapping(value = "/showapi", method = RequestMethod.POST)
	@ResponseBody
	public String show(HttpServletRequest request, HttpServletResponse response, String apiid) {

		JSONObject obj = new JSONObject();
		try {
			APIEntity api = apiService.findById(apiid);

			com.alibaba.fastjson.JSONObject jsonObject = com.alibaba.fastjson.JSONObject
					.parseObject(JSON.toJSONString(api));

			// 添加封装信息
			AppEntity appEntity = appService.findById(api.getAppid());

			List<String> gatewayUrlList = new ArrayList<String>();
			for (GatewayColonyEntity gatewayColonyEntity : GatewayUtils.getAllGatewayColony()) {
				gatewayUrlList
						.add(gatewayColonyEntity.getColUrl() + "/" + appEntity.getAppkey() + "/" + api.getApienname());
			}
			jsonObject.put("appname", appEntity.getAppname());
			jsonObject.put("gatewayUrls", gatewayUrlList);

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

	@RequestMapping(value = "/apilist", method = RequestMethod.POST)
	@ResponseBody
	public String showAPIs(HttpServletRequest request, HttpServletResponse response, String appid,
			String apichname, String checkState, @RequestParam(required = false, defaultValue = "1") int currentPage,
			@RequestParam(required = false, defaultValue = "8") int pageSize) {

		logger.info("-------------->appid:" + appid);
		logger.info("-------------->apichname:" + apichname);
		logger.info("-------------->checkState:" + checkState);
		logger.info("-------------->currentPage:" + currentPage);
		logger.info("-------------->pageSize:" + pageSize);
		 JSONObject obj = new JSONObject();
		try {

			APIEntity apiParam = new APIEntity();
			if (StringUtils.hasText(appid)) {

				apiParam.setAppid(appid);
			}
			if (StringUtils.hasText(apichname)) {
				apiParam.setApichname(apichname);
			}
			if (StringUtils.hasText(checkState)) {
				int state = Integer.parseInt(checkState);
				apiParam.setCheckState(state);
			}

			Page<APIEntity> ds = apiService.findApisByEntity(apiParam, currentPage, pageSize);
			List<APIEntity> items = (List<APIEntity>) ds.getItems();
			List<String> apiIdList = new ArrayList<>(items.size());
			// 构建apiId集合
			for (APIEntity apiEntity : items) {
				apiEntity.setApp(appService.findById(apiEntity.getAppid()));
				apiIdList.add(apiEntity.getId());
			}

			 com.alibaba.fastjson.JSONObject jsonObject = com.alibaba.fastjson.JSONObject.parseObject(JSON.toJSONString(ds));

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

	@RequestMapping(value = "/delapi", method = RequestMethod.GET)
	@ResponseBody
	public Result<String> delAPI(HttpServletRequest request, HttpServletResponse response, String apiid) {
		logger.info("--------------->> Action!  del api. ID: " + apiid);

		return apiService.delById(apiid);
	}
}
