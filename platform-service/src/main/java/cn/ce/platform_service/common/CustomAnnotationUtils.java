package cn.ce.platform_service.common;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import cn.ce.annotation.dubbodescription.InterfaceDescription;

/**
 * 返回自定义标签接口描述的公共方法
 * 
 * @author lenovo
 *
 */
public class CustomAnnotationUtils {

	private static Log logger = LogFactory.getLog(CustomAnnotationUtils.class);

	public static Map initJsonServiceMap(Class<?> clazz) {
		Map<String, String> ServiceMap = new HashMap<>();
		Method[] method = clazz.getDeclaredMethods();
		for (int i = 0; i < method.length; i++) {
			InterfaceDescription service = (InterfaceDescription) method[i].getAnnotation(InterfaceDescription.class);

			if (service != null) {
				String desc = service.des();
				logger.info(desc + ">>>>>" + clazz.getName());
				ServiceMap.put(desc, method[i].getName());
			}
		}
		return ServiceMap;
	}

}
