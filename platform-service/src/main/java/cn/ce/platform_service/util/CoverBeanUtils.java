package cn.ce.platform_service.util;

import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.beanutils.converters.BigDecimalConverter;
import org.apache.commons.beanutils.converters.DoubleConverter;
import org.apache.commons.beanutils.converters.IntegerConverter;
import org.apache.commons.beanutils.converters.LongConverter;
import org.apache.commons.beanutils.converters.ShortConverter;

/**
* @Description : BeanUtils默认会将基本数据类型的包装类的null赋值为0，该类能够解决将null赋值给null
* @Author : makangwei
* @Date : 2018年2月8日
*/
public class CoverBeanUtils {

	static {
		ConvertUtils.register(new LongConverter(null), Long.class);
		ConvertUtils.register(new ShortConverter(null), Short.class);
		ConvertUtils.register(new IntegerConverter(null), Integer.class);
		ConvertUtils.register(new DoubleConverter(null), Double.class);
		ConvertUtils.register(new BigDecimalConverter(null), BigDecimal.class);
		}
	
	
	public static void copyProperties(Object dest, Object orig) 
			throws IllegalAccessException, InvocationTargetException{
		
		BeanUtils.copyProperties(dest, orig);
	}
}
