package cn.ce.platform_service.util;

import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;

public class ModuleClassLoader extends URLClassLoader {

	private static ModuleClassLoader instance;
	private static URLClassLoader classLoader = (URLClassLoader) ClassLoader.getSystemClassLoader();
	private static final Method ADD_URL = initAddMethod();

	static {
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
			addUrl.setAccessible(true);
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

	public static void main(String[] args) throws Exception {
		URL url1 = new URL("file:D:/lib/appservice-info-api-2.0.0-SNAPSHOT.jar");
		// URLClassLoader myClassLoader1 = new URLClassLoader(new URL[] { url1 },
		// Thread.currentThread().getContextClassLoader());
		// Class<?> myClass1 =
		// myClassLoader1.loadClass("cn.ce.ebiz.info.service.AppInfoCategoryService");
		// myClass1.getDeclaredMethods();
		// myClass1.newInstance();

		URL url2 = new URL("file:D:/lib/service-info-api-2.0.0-SNAPSHOT.jar");
		ModuleClassLoader moduleClassLoader = ModuleClassLoader.getInstance();
		moduleClassLoader.setDefaultAssertionStatus(false);
		moduleClassLoader.loadJar(url1.toURI().toURL());
		moduleClassLoader.loadJar(url2.toURI().toURL());
		System.out.println("加载成功");
		Class<?> spawnClass = Class.forName("cn.ce.ebiz.info.service.AppInfoCategoryService");
		spawnClass.getDeclaredMethods();
		CustomAnnotationUtils.initJsonServiceMap(spawnClass);
	}

}