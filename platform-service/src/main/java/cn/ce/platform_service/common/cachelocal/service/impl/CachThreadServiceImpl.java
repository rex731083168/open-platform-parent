package cn.ce.platform_service.common.cachelocal.service.impl;

import org.springframework.stereotype.Service;

import cn.ce.platform_service.common.cachelocal.CacheManager;
import cn.ce.platform_service.common.cachelocal.service.ICachThreadService;

/**
 *
 * @Title: Cache.java
 * @Description
 * @version V1.0
 * @author yang.yu@300.cn
 * @date dat2017年10月18日 time下午8:16:21
 *
 **/
@Service(value="cachThreadServiceImpl")
public class CachThreadServiceImpl implements ICachThreadService {

	@Override
	public void run() {
		// TODO Auto-generated method stub
		
		
		CacheManager.cacheMap.clear();//这个就是调用封装类里面的清理缓存的方法  
//		try {
//			Thread.sleep(5*60*1000);
//			//设置时间，这里是设置5分钟，格式是：24*60*60*1000（时*分*秒*毫秒）  
//			
//		} catch (InterruptedException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}

	}

}
