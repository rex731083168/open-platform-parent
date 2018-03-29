package cn.ce.platform_service.gateway.service.impl;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;

import cn.ce.platform_service.common.SaasConstants;
import cn.ce.platform_service.common.gateway.GatewayRouteUtils;
import cn.ce.platform_service.gateway.dao.IMysqlSaasDao;
import cn.ce.platform_service.gateway.entity.SaasEntity;
import cn.ce.platform_service.gateway.service.ISaasService;
import io.netty.handler.codec.http.HttpMethod;

/**
* @Description : 说明
* @Author : makangwei
* @Date : 2018年3月22日
*/
@Service(value="saasService")
public class SaasServiceImpl implements ISaasService{

	@Resource
	private IMysqlSaasDao saasDao;
	
	/**
	 * 
	 * @Title: save
	 * @Description: 只能添加不能新增
	 * @author: makangwei 
	 * @date:   2018年3月28日 下午7:45:32 
	 */
	private int save(SaasEntity saas) {
		if(null ==saas){
			return 0;
		}else if(StringUtils.isBlank(saas.getResource_type()) ||
				StringUtils.isBlank(saas.getSaas_id()) ||
				StringUtils.isBlank(saas.getTarget_url())){
			return 0;
		}
		return saasDao.save(saas);
	}

	@Override
	public void clearAll() {
		saasDao.clearAll();
	}

	@Override
	public String getSaas(String saasId, String resourceType,String method) {
		SaasEntity saas = saasDao.getSaas(saasId,resourceType);
		if(null == saas){
			String routeBySaasId = GatewayRouteUtils.getRouteBySaasId(saasId,resourceType,HttpMethod.GET.toString());
			int num = this.save(JSON.parseObject(routeBySaasId, SaasEntity.class));
			return routeBySaasId;
		}else{
			return JSON.toJSONString(saas); 
		}
	}

	@Override
	public String saveSaas(String saasId, String resourceType, String targetUrl,String method) {
		
		String routeBySaasId = GatewayRouteUtils.saveRoute(saasId, targetUrl, resourceType,method);
		
		JSONObject job = new JSONObject(routeBySaasId);
		if(SaasConstants.STATUS_OK.equals(job.getString(SaasConstants.STATUS))){
				SaasEntity saas1 = saasDao.getSaas(saasId, resourceType);
				if(null != saas1 && StringUtils.isNotBlank(saas1.getResource_type()) && StringUtils.isNotBlank(saas1.getSaas_id())){
					saasDao.updateSaas(saasId, resourceType, targetUrl);
				}else{
					this.save(new SaasEntity(saasId, resourceType, targetUrl));
				}
		}
		return routeBySaasId;
	}

	@Override
	public String deleteRoute(String saasId, String resourceType, String method) {
		
		String routeBySaasId = GatewayRouteUtils.deleteRoute(saasId,resourceType,method);
		JSONObject job = new JSONObject(routeBySaasId);
		if(SaasConstants.STATUS_OK.equals(job.getString(SaasConstants.STATUS)) && 
				SaasConstants.ACTION_DELETED.equals(job.getString(SaasConstants.ACTION))){
			saasDao.deleteSaas(saasId, resourceType);
		}
		return routeBySaasId;
	}
	
}

