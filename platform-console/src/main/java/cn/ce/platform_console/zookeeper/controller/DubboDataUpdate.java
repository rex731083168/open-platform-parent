package cn.ce.platform_console.zookeeper.controller;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import cn.ce.platform_service.common.ErrorCodeNo;
import cn.ce.platform_service.common.Result;
import cn.ce.platform_service.common.Status;
import cn.ce.platform_service.util.PageValidateUtil;
import cn.ce.platform_service.zk.service.IZkDubboService;

/**
* @Description : 定时更新dubbo数据
* @Author : makangwei
* @Date : 2018年1月9日
*/
@RestController
@RequestMapping("/dubboDateController")
public class DubboDataUpdate {
	
	@Value("#{redis['zookeeper.connection']}")
	private String zkconnectioninfo;
	@Value("#{redis['dubbo.node']}")
	private String datakey;
	
	
	@Resource
	private IZkDubboService zkDubboService;

	/**
	 * 
	 * @Description: 定时更新dubbo数据
	 * @author: makangwei
	 * @date:   2018年1月9日 下午3:58:24  
	 */
//	@Scheduled(cron = "0 0 1 * * *")
//	public void updataDubboData() {
//		if(zkDubboService.clearAll()){ //先清除数据成功
//			zkDubboService.updateData(zkconnectioninfo, datakey); //然后执行更新数据
//		}
//	}
	
	//@PostConstruct
	//每次容器一启动就会调用该方法。注册到陈金龙的调度中心,但是调度中心没有删除操作。所以每次上线前需要陈金龙手动删除原来的调度动作
//	public void scheduled(){
//		
//		manualUpdataData();	// 往数据库中添加初始数据
//		invokeScheduled();
//	}
	
//	private void invokeScheduled() {
//		String scheduledUrl = PropertiesUtil.getInstance().getValue("scheduled.task");
//		
//	}
		
	@RequestMapping(value="/	", method=RequestMethod.GET)
	public Result<?> manualUpdataData(){
		boolean flag1 = zkDubboService.clearAll();
		boolean flag2 = zkDubboService.updateData(zkconnectioninfo, datakey);
		if(flag1 && flag2){
			return Result.errorResult("批量更新数据库成功", ErrorCodeNo.SYS000, null, Status.SUCCESS);
		}
		return Result.errorResult("批量更新数据失败", ErrorCodeNo.SYS031, null, Status.FAILED);
	}

	@RequestMapping(value="/findRootPage", method=RequestMethod.GET)
	public Result<?> findRootPage(@RequestParam(defaultValue = "1") int currentPage,
			@RequestParam(defaultValue="10") int pageSize){
		
		return zkDubboService.findRootPage(PageValidateUtil.checkCurrentPage(currentPage),
				PageValidateUtil.checkPageSize(pageSize));
	}
	
	@RequestMapping(value="/findNodePage", method=RequestMethod.GET)
	public Result<?> findNodePage(
			@RequestParam(defaultValue = "1") int currentPage,
			@RequestParam(defaultValue="10") int pageSize,
			@RequestParam(required=false) String rootId){
		
		return zkDubboService.findNodePage(PageValidateUtil.checkCurrentPage(currentPage),
				PageValidateUtil.checkPageSize(pageSize), rootId);
	}
	
	@RequestMapping(value="/findNodeById", method=RequestMethod.GET)
	public Result<?> findNodeById(@RequestParam(required=true) String id){
		
		return zkDubboService.findNodeById(id);
	}	
	
	@RequestMapping(value="findNodeProviderPage", method=RequestMethod.GET)
	public Result<?> findNodeProviderPage(
			@RequestParam(defaultValue = "1") int currentPage,
			@RequestParam(defaultValue="10") int pageSize,
			@RequestParam(required=false)String nodeId){
		
		return zkDubboService.findProviderPage(PageValidateUtil.checkCurrentPage(currentPage),
				PageValidateUtil.checkPageSize(pageSize),nodeId);
	}
	
	@RequestMapping(value="/findProviderById", method=RequestMethod.GET)
	public Result<?> findNodeProviderById(@RequestParam(required=true) String id){
		
		return zkDubboService.findProviderById(id);
	}
	
	@RequestMapping(value="findNodeConsumerPage", method=RequestMethod.GET)
	public Result<?> findNodeConsumerPage(
			@RequestParam(defaultValue="1") int currentPage,
			@RequestParam(defaultValue="10") int pageSize,
			@RequestParam(required=false) String nodeId){
		
		return zkDubboService.findConsumerPage(currentPage,pageSize,nodeId);
	}
	
	@RequestMapping(value="/findConsumerById", method=RequestMethod.GET)
	public Result<?> findNodeConsumerById(@RequestParam(required=true) String id){
		
		return zkDubboService.findConsumerById(id);
	}
	
	@RequestMapping(value="findNodeRouterPage", method=RequestMethod.GET)
	public Result<?> findNodeRouterPage(
			@RequestParam(defaultValue = "1") int currentPage,
			@RequestParam(defaultValue="10") int pageSize,
			@RequestParam(required=false) String nodeId){
		
		return zkDubboService.findRouterPage(currentPage,pageSize,nodeId);
	}
	
	@RequestMapping(value="/findRouterById", method=RequestMethod.GET)
	public Result<?> findNodeRouterById(@RequestParam(required=true) String id){
		
		return zkDubboService.findRouterById(id);
	}
	
	@RequestMapping(value="findNodeConfigatorPage", method=RequestMethod.GET)
	public Result<?> findNodeConfiguratorPage(
			@RequestParam(defaultValue="1") int currentPage,
			@RequestParam(defaultValue="10") int pageSize,
			@RequestParam(required=false)String nodeId){
		
		return zkDubboService.findConfiguratorPage(currentPage,pageSize,nodeId);
	}
	
	@RequestMapping(value="/findConfiguratorById", method=RequestMethod.GET)
	public Result<?> findNodeConfiguratorById(@RequestParam(required=true) String id){
		
		return zkDubboService.findConfiguratorById(id);
	}
		
}
