package cn.ce.platform_service.util;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import cn.ce.annotation.dubbodescription.InterfaceDescription;
import cn.ce.annotation.dubbodescription.InterfaceDescriptionFullEnty;

/**
 * 返回自定义标签接口描述的公共方法
 * 
 * @author lenovo
 *
 */
public class CustomAnnotationUtils {

	private static Log logger = LogFactory.getLog(CustomAnnotationUtils.class);

	//从class中解析注解
	public static Map<String, InterfaceDescriptionFullEnty> initJsonServiceMap(Class<?> clazz) throws ClassNotFoundException {
		
		Map<String, InterfaceDescriptionFullEnty> serviceMap = new HashMap<>();
		Method[] method = clazz.getDeclaredMethods();
		for (int i = 0; i < method.length; i++) {
			InterfaceDescription service = (InterfaceDescription) method[i].getAnnotation(InterfaceDescription.class);
			InterfaceDescriptionFullEnty ide = new InterfaceDescriptionFullEnty();
			if (service != null) {
				ide.setName(service.name());
				ide.setDes(service.des());
				ide.setVersion(service.version());
				ide.setClassname(method[i].getDeclaringClass().getName());
				ide.setMethod(method[i].getName());
				int index = method[i].getDeclaringClass().getName().lastIndexOf(".");
				ide.setPackagename(method[i].getDeclaringClass().getName().substring(0, index));
				
				String key = method[i].getDeclaringClass().getName() + "." + method[i].getName();
				serviceMap.put(key, ide);
				logger.info("method annotation parser key:"+key+",value="+ide);
			}
		}
		return serviceMap;
	}

}
