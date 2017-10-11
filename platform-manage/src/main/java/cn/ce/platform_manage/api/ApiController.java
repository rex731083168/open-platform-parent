package cn.ce.platform_manage.api;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import cn.ce.platform_service.apis.entity.APIEntity;
import cn.ce.platform_service.apis.service.IAPIService;
import cn.ce.platform_service.common.Result;
import cn.ce.platform_service.common.gateway.GatewayUtils;
import cn.ce.platform_service.gateway.entity.GatewayColonyEntity;
import cn.ce.platform_service.gateway.service.impl.GatewayApiService;
import cn.ce.platform_service.openApply.entity.DevApplyEntity;
import cn.ce.platform_service.openApply.service.IAppService;
import cn.ce.platform_service.page.Page;

/**
* @Description : 后台api管理
* @Author : makangwei
* @Date : 2017年8月18日
*/
@RestController
@RequestMapping("/api")
public class ApiController {
	

	private static Logger _LOGGER = LoggerFactory.getLogger(ApiController.class);
	
	@Resource
	private GatewayApiService gatewayApiService;
    @Resource
    private IAPIService apiService;
    @Resource
    private IAppService appService;
    
	/**
	 * @Description : 审核后推送网关是多版本+密钥授权
	 * @Author : makangwei
	 * @Date : 2017年8月14日
	 * @param request
	 * @param response
	 * @param apiId 
	 * @param checkState 审核状态 2：通过，3：拒绝
	 * @param checkMem  如果审核失败，此字段输入审核失败原因
	 * @return
	 */
	@RequestMapping(value = "/auditApi", method = RequestMethod.POST, produces = "application/json;charset=utf-8")
	public Result<String> auditApi(HttpServletRequest request, HttpServletResponse response, 
			@RequestParam String apiId,
			@RequestParam Integer checkState, 
			@RequestParam(required=false) String checkmem) {

		_LOGGER.info("----------apiId:" + apiId);
		_LOGGER.info("---------checkState:" + checkState);

		if (StringUtils.isBlank(apiId)) {
			Result<String> result = new Result<String>();
			result.setMessage("apiId不能为空");
			return result;
		}
		if (checkState == null || checkState > 3 || checkState < 0) {
			Result<String> result = new Result<String>();
			result.setMessage("审核状态不存在，请输入正确的审核状态");
			return result;
		}

		return gatewayApiService.auditApi(apiId, checkState, checkmem);

	}

	/**
	 * @Description : 审核后推送网关是多版本+oauth
	 * @Author : makangwei
	 * @Date : 2017年8月21日
	 * @param request
	 * @param response
	 * @param apiId 
	 * @param checkState 审核状态 2：通过，3：拒绝
	 * @param checkMem  如果审核失败，此字段输入审核失败原因
	 * @return
	 */
	@RequestMapping(value = "/auditApi2", method = RequestMethod.POST, produces = "application/json;charset=utf-8")
	public Result<String> auditApi2(HttpServletRequest request, HttpServletResponse response, 
			@RequestParam String apiId,
			@RequestParam Integer checkState, 
			@RequestParam(required=false) String checkMem) { // checkMem如过审核不通过，将审核失败的原因通过该字段传入

		_LOGGER.info("----------apiId:" + apiId);
		_LOGGER.info("----------checkState:" + checkState);
		_LOGGER.info("----------checkMem" + checkMem);

		if (apiId == null || apiId.trim() == "") {
			Result<String> result = new Result<String>();
			result.setMessage("apiId不能为空");
			return result;
		}
		if (checkState == null || checkState > 3 || checkState < 0) {
			Result<String> result = new Result<String>();
			result.setMessage("审核状态不存在，请输入正确的审核状态");
			return result;
		}

		return gatewayApiService.auditApi2(apiId, checkState, checkMem);
	}

	@RequestMapping(value = "/showApi", method = RequestMethod.POST)
	public Result<?> show(HttpServletRequest request, HttpServletResponse response, String apiid) {

		Result<JSONObject> result= new Result<JSONObject>();
		try {
			APIEntity api = apiService.findById(apiid);

//			com.alibaba.fastjson.JSONObject jsonObject = com.alibaba.fastjson.JSONObject
//					.parseObject(JSON.toJSONString(api));
			org.json.JSONObject jsonObject = new org.json.JSONObject(api);

			// 添加封装信息
			DevApplyEntity appEntity = appService.findById(api.getAppId());

			List<String> gatewayUrlList = new ArrayList<String>();
			for (GatewayColonyEntity gatewayColonyEntity : GatewayUtils.getAllGatewayColony()) {
				gatewayUrlList
						.add(gatewayColonyEntity.getColUrl() + "/" + appEntity.getApplyKey() + "/" + api.getApiEnName());
			}
			jsonObject.put("appName", appEntity.getApplyName());
			jsonObject.put("gatewayUrls", gatewayUrlList);
			
			result.setSuccessData(jsonObject);
		} catch (Exception e) {
			_LOGGER.info("error happens when execute showapi",e);
			result.setErrorMessage("");
		}
		return result;
	}

	@RequestMapping(value = "/apiList", method = RequestMethod.POST)
	@ResponseBody
	public Result<Page<APIEntity>> showAPIs(HttpServletRequest request, HttpServletResponse response, String apiId,
			String apiChName, String checkState, @RequestParam(required = false, defaultValue = "1") int currentPage,
			@RequestParam(required = false, defaultValue = "8") int pageSize) {

		_LOGGER.info("-------------->appid:" + apiId);
		_LOGGER.info("-------------->apichname:" + apiChName);
		_LOGGER.info("-------------->checkState:" + checkState);
		_LOGGER.info("-------------->currentPage:" + currentPage);
		_LOGGER.info("-------------->pageSize:" + pageSize);

		return apiService.apiList(apiId,apiChName,checkState,currentPage,pageSize);
	}

	@RequestMapping(value = "/delApi", method = RequestMethod.GET)
	@ResponseBody
	public Result<String> delAPI(HttpServletRequest request, HttpServletResponse response, String apIid) {
		_LOGGER.info("--------------->> Action!  del api. ID: " + apIid);

		return apiService.delById(apIid);
	}
}
