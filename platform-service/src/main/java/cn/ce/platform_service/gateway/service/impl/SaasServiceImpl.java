package cn.ce.platform_service.gateway.service.impl;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import cn.ce.platform_service.common.gateway.GatewayRouteUtils;
import cn.ce.platform_service.gateway.dao.IMysqlSaasDao;
import cn.ce.platform_service.gateway.entity.SaasEntity;
import cn.ce.platform_service.gateway.service.ISaasService;
import cn.ce.platform_service.util.RandomUtil;
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
	
	@Override
	public int save(SaasEntity saas) {
		if(null ==saas){
			return 0;
		}else if(StringUtils.isBlank(saas.getResourceType()) ||
				StringUtils.isBlank(saas.getSaasId()) ||
				StringUtils.isBlank(saas.getTargetUrl())){
			return 0;
		}
		return saasDao.save(saas);
	}

	@Override
	public void clearAll() {
		saasDao.clearAll();
	}

	@Override
	public String getSaas(String saasId, String resourceType) {
		SaasEntity saas = saasDao.getSaas(saasId,resourceType);
		if(null == saas){
			String routeBySaasId = GatewayRouteUtils.getRouteBySaasId(saasId,resourceType,HttpMethod.GET.toString());
			return routeBySaasId;
		}
		
		return null;
	}
}

