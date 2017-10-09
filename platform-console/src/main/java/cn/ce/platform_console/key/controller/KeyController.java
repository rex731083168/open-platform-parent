package cn.ce.platform_console.key.controller;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;

import cn.ce.platform_console.apis.controller.ApisController;
import cn.ce.platform_service.apis.entity.APIEntity;
import cn.ce.platform_service.apis.service.IAPIService;
import cn.ce.platform_service.apisecret.entity.ApiSecretKey;
import cn.ce.platform_service.apisecret.service.IApiSecretKeyService;
import cn.ce.platform_service.common.Constants;
import cn.ce.platform_service.common.Result;
import cn.ce.platform_service.gateway.service.impl.GatewayApiService;
import cn.ce.platform_service.users.entity.User;

/**
* @Description : 说明
* @Author : makangwei
* @Date : 2017年8月23日
*/
@Controller
@RequestMapping("/key")
public class KeyController {
	
	private static Logger logger = Logger.getLogger(ApisController.class);
	@Autowired
	private GatewayApiService gatewayApiService;
	@Autowired
	private IApiSecretKeyService secretKeyService;
	@Autowired
	private IAPIService apiService;
	/**
	 * @Description : 审核通过key
	 * @Author : makangwei
	 * @Date : 2017年8月17日
	 */
	@RequestMapping(value = "/auditKey", method = RequestMethod.GET, produces = "application/json;charset=utf-8")
	@ResponseBody
	public Result<String> allowKey(HttpServletRequest request, HttpServletResponse response, String secretKey,
			Integer checkState) {

		Result<String> result = new Result<String>();
		if (secretKey == null || secretKey.trim() == "") {
			result.setMessage("密钥不能为空");
			return result;
		}
		if (checkState == null || checkState > 3 || checkState < 0) {
			result.setMessage("审核状态不存在，请输入正确的审核状态");
			return result;
		}

		return gatewayApiService.auditKey(secretKey, checkState);
	}

	/**
	 * 
	 * @Description : 删除密钥
	 * @Author : makangwei
	 * @Date : 2017年8月14日
	 * @param request
	 * @param response
	 * @param secretKey
	 * @return
	 */
	@RequestMapping(value = "/deleteKey", method = RequestMethod.GET, produces = "application/json;charset=utf-8")
	@ResponseBody
	public Result<String> deleteKey(HttpServletRequest request, HttpServletResponse response, String secretKey , String applyId) {

		if (secretKey == null || secretKey.trim() == "") {
			Result<String> result = new Result<String>();
			result.setMessage("secretKey不能为空");
			return result;
		}

		return gatewayApiService.deleKey(secretKey , applyId);
	}
	
	/***
	 * 用户申请api使用权 生成秘钥
	 * @param request
	 * @param response
	 * @param secretKey
	 * @return
	 */
	@RequestMapping(value = "/generalSecretKey", method = RequestMethod.POST)
	@ResponseBody
	private String generalSecretKey(HttpServletRequest request, HttpServletResponse response, HttpSession session,
			@RequestParam(required = true, value = "apiId") String apiId) {
		logger.info("---------------->> Action general secret key! apiId: " + apiId);

		JSONObject result = new JSONObject();
		String errorStr = "";
		try {

			// 检测用户是否登录
			User user = (User) session.getAttribute(Constants.SES_LOGIN_USER);
			if (user == null) {
				errorStr = "用户未登录!";
				result.put("code", "0");
				result.put("message", errorStr);
				return result.toString();
			}

			// 检测Key是否已经生成过
			ApiSecretKey secKey = new ApiSecretKey();
			secKey.setApiId(apiId);
			secKey.setUserId(user.getId());
			List<ApiSecretKey> findSecretKeyEntity = secretKeyService.findSecretKeyEntity(secKey);
			if (!findSecretKeyEntity.isEmpty()) {
				errorStr = "您已申请过此应用!";
				result.put("code", "0");
				result.put("message", errorStr);
				return result.toString();
			}

			// 检查api是否存在
			APIEntity findById = apiService.findById(apiId);
			if (null == findById) {
				errorStr = "申请的api不存在!";
				result.put("code", "0");
				result.put("message", errorStr);
				return result.toString();
			}

			ApiSecretKey secretKey = new ApiSecretKey();

			String key = UUID.randomUUID().toString();
			secretKey.setSecretKey(key);
			secretKey.setUserId(user.getId());
			secretKey.setUserName(user.getUserName());
			secretKey.setCreateDate(new Date());
			secretKey.setApiId(apiId);
			secretKey.setCheckState(1);

			secretKeyService.addApiSecretKey(secretKey);
			result.put("code", "1");
			result.put("message", "OK");

		} catch (Exception e) {
			e.printStackTrace();
			errorStr = "系统错误!";
			result.put("code", "0");
			result.put("message", errorStr);
		}
		return result.toString();
	}

	
}
