package cn.ce.tyk_oauth_service.controller;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.jasig.cas.client.util.AbstractCasFilter;
import org.jasig.cas.client.validation.Assertion;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.alibaba.fastjson.JSON;

import cn.ce.common.Result;
import cn.ce.oauth.service.IOauthService;

@Controller
@RequestMapping("/tykOauth")
public class TykOauthController {

	/** 日志对象 */
	private static Logger logger = Logger.getLogger(TykOauthController.class);

	@Autowired private IOauthService authService;
	
	/***
	 * 生成clientId
	 * @return
	 
	@RequestMapping(value = "/generalAuthClientId", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	@ResponseBody
	public String generalClientId(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value="apiId",required=true)String apiId,
			@RequestParam(value="redirectUri",required=true)String redirectUri){
		logger.info("general client begin >> ");
		Object clientIdAndSecret = oauthService.getClientIdAndSecret(apiId,redirectUri);
		logger.info("general client end >> ");
		return JSON.toJSONString(clientIdAndSecret);
	}*/
	
	
	/**
	 * 生成code
	 * @return
	 * @throws IOException 
	 */
	@RequestMapping(value = "/generalAuthCode", method = RequestMethod.GET)
	public String generalAuthCode(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(required=true) String clientId,
			@RequestParam(required=true) String response_type,
			@RequestParam(required=true) String redirect_uri){
		logger.info("general authcode begin >> ");
		Assertion assertion = (Assertion) request.getSession().getAttribute(AbstractCasFilter.CONST_CAS_ASSERTION);
		//JSONObject result = new JSONObject();
		
		Result<String> result = new Result<String>();
        logger.info("current user detail:" + JSON.toJSONString(assertion));
		
		if(null != assertion){
			try {
				//result = authService.generalAuthCode(redirect_uri+"",clientId, response_type, "/");
				result = authService.generalAuthCode("",clientId, response_type, "/");
			} catch (Exception e) {
				logger.error("generate AuthCode error:" + e.toString());
				result.setMessage("error happens when generate authcode,please try again!");
			}
		}else{
			result.setMessage("未获取当前登录用户，请重新登录");
		}
		logger.info(result.getData());
		logger.info("general authcode end >> ");
		
		//TODO 这里需要拼接地址，不用返回结果
		logger.info(">>>>result.getData():"+result.getData());
		logger.info(">>>>result.getStatus()"+result.getStatus());
		logger.info(">>>>result.getMessage()"+result.getMessage());
		
		//对redirect_uri进行判断
		if(!(redirect_uri.startsWith("http://") || redirect_uri.startsWith("https://"))){
			redirect_uri = "http://"+redirect_uri;
		}
		if(!redirect_uri.contains("?")){
			redirect_uri = redirect_uri+"?1=1";
		}
		if(!redirect_uri.endsWith("&")){
			redirect_uri = redirect_uri+"&";
		}
		
		if("200".equals(result.getStatus())){
			JSONObject job = new JSONObject(result.getData());
			String code = job.getString("code");
			redirect_uri+="code="+code+"&status="+result.getStatus();
			return "redirect:"+redirect_uri;
		}else{
			redirect_uri+="status="+result.getStatus()+"&message="+result.getMessage()+"&code=";
			return "redirect:"+redirect_uri;
		}
	}
}
