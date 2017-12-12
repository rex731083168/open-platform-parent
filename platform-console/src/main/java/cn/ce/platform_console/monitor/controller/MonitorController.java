package cn.ce.platform_console.monitor.controller;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import cn.ce.platform_service.common.Result;

/**
* @Description : 监控程序。监控服务的正常运行
* @Author : makangwei
* @Date : 2017年12月8日
*/
@Component
public class MonitorController {

	
	public Result<?> gateway(){
		
		return null;
	}
	
	public Result<?> productCentor(){
		
		return null;
	}
	
	public Result<?> menuCentor(){
		
		return null;
	}
	
	public Result<?> mongoColony(){
		
		return null;
	}
	
	public Result<?> elasticSearch(){
		
		return null;
	}
	
	public Result<?> redisColony(){
		
		return null;
	}
	
	public static void main(String[] args) {
		Runnable runnable = new Runnable() {
			public void run() {
				System.out.println("hello Rex");
			}
		};
		
		ScheduledExecutorService service = Executors.newSingleThreadScheduledExecutor();
		service.scheduleAtFixedRate(runnable, 10, 1, TimeUnit.SECONDS);
		
	}
}
