package cn.ce.platform_manage.test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import cn.ce.platform_service.apis.dao.INewApiDao;
import cn.ce.platform_service.apis.entity.ApiEntity;
import cn.ce.platform_service.gateway.service.impl.GatewayApiServiceImpl;
import cn.ce.platform_service.util.SplitUtil;

/**
* @Description : 说明
* @Author : makangwei
* @Date : 2017年10月16日
*/
@RestController
@RequestMapping("/test")
public class GatewayTest {
	
	@Resource
	GatewayApiServiceImpl gatewayApiService;
	@Resource
	INewApiDao newApiDao;
	
	@RequestMapping(value="/testPushPolicy",method=RequestMethod.GET)
	public String testPushPolicy(
			String policyId,
			Integer rate,
			Integer per,
			Integer quotaMax,
			Integer quotaRenewRate,
			String versionIds){
		
		List<String> versionIdList = SplitUtil.splitStringWithComma(versionIds);
		
		Map<String,List<String>> map = new HashMap<String,List<String>>();
		
		for (String versionId : versionIdList) {
			List<ApiEntity> apiList = newApiDao.findByField("apiVersion.versionId", versionId);
			List<String> versionList = new ArrayList<String>();
			for (ApiEntity apiEntity : apiList) {
				versionList.add(apiEntity.getApiVersion().getVersion());
			}
			map.put(versionId, versionList);
		}
		
		String str = gatewayApiService.pushPolicy(policyId, rate, per, quotaMax, quotaRenewRate, map);
		return str;
	}
	
	@RequestMapping(value="/testPushClient",method=RequestMethod.GET)
	public String testPushClient(
			String clientId,
			String secret,
			String apiIds,
			String policyId){
		
		String str = gatewayApiService.pushClient(clientId, secret, apiIds, policyId);
		return str;
	}
	
	
	

}
