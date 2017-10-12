package cn.ce.platform_service.gateway.dao;

import cn.ce.platform_service.gateway.entity.GatewayColonyEntity;

/***
 * 
 * 
 * @ClassName:  IGatewayColonyManageDao   
 * @Description:网关集群表Dao   
 * @author: lida 
 * @date:   2017年10月11日 下午6:05:24   
 * @Copyright: 2017 中企动力科技股份有限公司 © 1999-2017 300.cn All Rights Reserved
 *
 */
public interface IGatewayColonyManageDao {
	
	GatewayColonyEntity getColonySingle();
	
}
