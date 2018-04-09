package cn.ce.platform_service.common;

import cn.ce.platform_service.util.PropertiesUtil;

/**
* @Description : 拦截不同的环境做控制
* @Author : makangwei
* @Date : 2018年3月28日
*/
public class EnvironmentHool {
	
	//支持沙箱创建的环境
	private static final EnvEnum[] boxSupport = new EnvEnum[]{EnvEnum.local,EnvEnum.test};
	
	private EnvironmentHool(){}
	
	//是否支持沙箱环境
	public static boolean isSupportBox(){
		String env = PropertiesUtil.getInstance().getValue("environment");
		for (EnvEnum envEnum : boxSupport) {
			if(envEnum.toString().equals(env)){
				return true;
			}
		}
		return false;
	}
	
}

