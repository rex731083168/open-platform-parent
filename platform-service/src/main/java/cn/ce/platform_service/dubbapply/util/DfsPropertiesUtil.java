//package cn.ce.platform_service.dubbapply.util;
//
//import java.io.InputStream;
//import java.util.Enumeration;
//import java.util.Hashtable;
//import java.util.Properties;
//
//import org.apache.commons.logging.Log;
//import org.apache.commons.logging.LogFactory;
//import org.springframework.core.io.ClassPathResource;
//import org.springframework.core.io.Resource;
//
//
///**
//* @Description : 说明
//* @Author : makangwei
//* @Date : 2018年4月19日
//*/
//public class DfsPropertiesUtil {
//	private static Log log = LogFactory.getLog(DfsPropertiesUtil.class);
//	private static DfsPropertiesUtil m_instance = null;
//	private static Hashtable<String, String> m_config = null;
//
//	private DfsPropertiesUtil() throws Exception {
//		m_config = new Hashtable<String, String>();
//		init();
//	}
//
//	/**
//	 * initResource
//	 */
//	private static void init() throws Exception {
//		try {
//			Resource res = new ClassPathResource("dfs.zk.properties");
//			log.info("-->load application.properties :"+res.exists());
//			InputStream iss = res.getInputStream();
//			Properties prop = new Properties();
//			prop.load(iss);
//			Enumeration<Object> enKey = prop.keys();// getKeys();
//			while (enKey.hasMoreElements()) {
//				String key = (String) enKey.nextElement();
//				String val = (String) prop.get(key);
//				m_config.put(key, val);
//			}
//
//		} catch (Exception e) {
//			e.printStackTrace();
//			throw new Exception(e);
//		}
//	}
//
////	/**
////	 * findResource
////	 */
////	private static File findResource(String resourceName) {
////		File resFile = null;
////		try {
////
////			Enumeration e = DfsPropertiesUtil.class.getClassLoader().getResources(resourceName);
////			while (e.hasMoreElements()) {
////
////				URL u = (URL) e.nextElement();
////				resFile = new File(URI.create(u.toString()));
////				break;
////			}
////		} catch (IOException e) {
////			e.printStackTrace();
////		}
////		return resFile;
////	}
//
//	/**
//	 * getInstance
//	 */
//	public synchronized static DfsPropertiesUtil getInstance() {
//		if (m_instance != null) {
//			return m_instance;
//		}
//
//		try {
//			m_instance = new DfsPropertiesUtil();
//		} catch (Exception e) {
//			log.error(e.getMessage());
//			log.error("PropertiesUtil instance error!");
//			return null;
//		}
//
//		return m_instance;
//	}
//
//	public String getValue(String key) {
//		String ret = null;
//		ret = (String) m_config.get(key);
//		return ret;
//	}
//
//}
//
