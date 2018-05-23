package cn.ce.platform_service.util;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import org.apache.log4j.Logger;

import cn.ce.annotation.dubbodescription.InterfaceDescriptionFullEnty;

public class ModuleClassLoader extends URLClassLoader {
	/** 日志对象 */
	private static Logger _LOGGER = Logger.getLogger(ModuleClassLoader.class);
	private static ModuleClassLoader instance;
	private static URLClassLoader classLoader = (URLClassLoader) ClassLoader.getSystemClassLoader();
	private static final Method ADD_URL = initAddMethod();

	static {
		// 加载，装配jar包过程
		ClassLoader.registerAsParallelCapable();
	}

	private ModuleClassLoader(URL[] urls) {
		super(urls);
	}

	public static ModuleClassLoader getInstance() {
		if (instance == null) {
			instance = new ModuleClassLoader(new URL[] {});
		}
		return instance;
	}

	private static Method initAddMethod() {
		try {
			Method addUrl = URLClassLoader.class.getDeclaredMethod("addURL", URL.class);
			addUrl.setAccessible(true); //初始化jar包加载的方法
			return addUrl;
		} catch (NoSuchMethodException e) {
			throw new RuntimeException(e);
		}
	}

	public void loadJar(URL url) {
		try {
			ADD_URL.invoke(classLoader, url);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * @throws URISyntaxException 
	 * @throws MalformedURLException 
	 * @Title: loadJars
	 * @Description: 将jar包加载到jvm
	 * @author: makangwei 
	 * @date:   2018年4月10日 下午2:30:30 
	 * @param : @param jarFilePath
	 * @param : @return
	 * @return: boolean
	 * @throws
	 */
	public boolean loadJars(String[] jarFilePath) throws MalformedURLException, URISyntaxException{
		ModuleClassLoader moduleClassLoader = ModuleClassLoader.getInstance();
		for (String path : jarFilePath) {
			File f = new File(path);
			URL url = new URL("file:"+f.getPath());
			moduleClassLoader.setDefaultAssertionStatus(false);
			moduleClassLoader.loadJar(url.toURI().toURL());
		}
		return true;
	}
    /**
     * 根椐jar路径解析注解信息 
     * @param mainJar 
     * @return key :jarName
     * @param jarFilePath
     * @throws URISyntaxException
     * @throws IOException
     * @throws ClassNotFoundException
     */
	public  Map<String, Map<String, InterfaceDescriptionFullEnty>>  parseJars(String marJar, String[] jarFilePath) throws URISyntaxException, IOException, ClassNotFoundException {
		
		loadJars(jarFilePath); //加载jar包
		
		Map<String, Map<String, InterfaceDescriptionFullEnty>> resultMap = new HashMap<String, Map<String, InterfaceDescriptionFullEnty>>();
		JarFile localJarFile = new JarFile(marJar);
		Enumeration<JarEntry> entries = localJarFile.entries();
		while (entries.hasMoreElements()) {
			JarEntry jarEntry = entries.nextElement();
			if (jarEntry.isDirectory() || !jarEntry.getName().endsWith(".class"))
				continue;
			String clazz = jarEntry.getName().substring(0, jarEntry.getName().length() - 6).replace("/", ".");
			Class<?> spawnClass = Class.forName(clazz);
			Map<String, InterfaceDescriptionFullEnty> methodAnnotationMap = CustomAnnotationUtils.initJsonServiceMap(spawnClass);
			resultMap.put(new File(marJar).getName(), methodAnnotationMap);
		}
		localJarFile.close();
		return resultMap;
	}


	public static void main(String[] args) throws Exception {
		ModuleClassLoader moduleClassLoader = ModuleClassLoader.getInstance();
		String[] path = new String[]{"D:/lib/framework-pbase-2.0.0-20180328.074441-7.jar"
				,"D:/lib/service-info-api-2.0.0-SNAPSHOT.jar"
				,"D:/lib/appservice-info-api-2.0.0-SNAPSHOT.jar"};
		String mainJar = "D:/lib/appservice-info-api-2.0.0-SNAPSHOT.jar";
		Map<String, Map<String, InterfaceDescriptionFullEnty>> map = moduleClassLoader.parseJars(mainJar,path);
		for (String string : map.keySet()) {
			System.out.println("key:"+string+",value"+map.get(string));
			Map<String, InterfaceDescriptionFullEnty> map2 = map.get(string);
			for (String string2 : map2.keySet()) {
				System.out.println("key :"+string2+",value:"+map2.get(string2));
			}
		}
	}

}