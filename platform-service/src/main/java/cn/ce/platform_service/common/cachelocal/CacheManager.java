package cn.ce.platform_service.common.cachelocal;

import java.util.concurrent.ConcurrentHashMap;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

import cn.ce.platform_service.common.cachelocal.service.ICachThreadService;

/**
 *
 * @Title: CacheManager.java
 * @Description
 * @version V1.0
 * @author yang.yu@300.cn
 * @date dat2017年10月18日 time下午8:27:09
 *
 **/
@Service("cacheManager")
public class CacheManager {
	public static ConcurrentHashMap<String, Object> cacheMap = new ConcurrentHashMap<String, Object>();
	@Autowired
	private ThreadPoolTaskExecutor taskExecutor;
	@Resource
	private ICachThreadService cachThreadServiceImpl;

	// 得到缓存。同步静态方法
	public Object getCache(String key) {
		return (Object) cacheMap.get(key);
	}

	// 载入json缓存
	public void putCache(String key, Object obj) {
		cacheMap.put(key, obj);
	}

	// 判断是否存在一个缓存
	public synchronized boolean hasCache(String key) {
		return cacheMap.containsKey(key);
	}

	// 清除所有缓存
	public synchronized void clearAll() {
		cacheMap.clear();
	}

	// 清除指定的缓存
	public synchronized void clearOnly(String key) {
		cacheMap.remove(key);
	}

	// 获取缓存中的大小
	public int getCacheSize() {
		return cacheMap.size();
	}

	@PostConstruct
	public void exectueStar() {
		taskExecutor.execute(cachThreadServiceImpl);
	}

}
