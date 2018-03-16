package cn.ce.platform_service.util;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import cn.ce.annotation.dubbodescription.InterfaceDescription;
import cn.ce.annotation.dubbodescription.InterfaceDescriptionEnty;

/**
 * 返回自定义标签接口描述的公共方法
 * 
 * @author lenovo
 *
 */
public class CustomAnnotationUtils {

	private static Log logger = LogFactory.getLog(CustomAnnotationUtils.class);

	public static Map<String, InterfaceDescriptionEnty> initJsonServiceMap(Class clazz) throws ClassNotFoundException {
		Map<String, InterfaceDescriptionEnty> ServiceMap = new HashMap<>();
		Method[] method = clazz.getDeclaredMethods();
		for (int i = 0; i < method.length; i++) {
			method[i].getDeclaredAnnotation(InterfaceDescription.class);
			InterfaceDescription service = (InterfaceDescription) method[i].getAnnotation(InterfaceDescription.class);
			InterfaceDescriptionEnty ide = new InterfaceDescriptionEnty();
			if (service != null) {
				ide.setName(service.name());
				ide.setDes(service.des());
				ide.setVersion(service.version());
				ServiceMap.put(method[i].getDeclaringClass().getName(), ide);
				logger.info(service.name() + ">>>" + service.des() + ">>>" + service.version() + "----->"
						+method[i].getDeclaringClass().getName());
				System.out.println(service.name() + ">>>" + service.des() + ">>>" + service.version() + "----->"
						+ method[i].getDeclaringClass().getName());
			}
		}
		return ServiceMap;
	}

	public static void main(String[] args) throws ClassNotFoundException {
		Class clzz = ClassLoader.getSystemClassLoader()
				.loadClass("cn.ce.platform_service.apis.service.IApiTransportService");
		CustomAnnotationUtils.initJsonServiceMap(clzz);
	}
}
