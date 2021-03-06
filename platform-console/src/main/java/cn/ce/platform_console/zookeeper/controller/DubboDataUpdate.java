package cn.ce.platform_console.zookeeper.controller;

import javax.annotation.PostConstruct;
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

	@RequestMapping(value="/manualUpdataData", method=RequestMethod.GET)
	public Result<?> manualUpdataData(){
		boolean flag1 = zkDubboService.clearAll();
		boolean flag2 = zkDubboService.updateData(zkconnectioninfo, datakey);
		if(flag1 && flag2){
			return new Result<String>("批量更新数据库成功", ErrorCodeNo.SYS000, null, Status.SUCCESS);
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
		
		return zkDubboService.findConsumerPage(PageValidateUtil.checkCurrentPage(currentPage),
				PageValidateUtil.checkPageSize(pageSize),nodeId);
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
		
		return zkDubboService.findRouterPage(PageValidateUtil.checkCurrentPage(currentPage),
				PageValidateUtil.checkPageSize(pageSize),nodeId);
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
		
		return zkDubboService.findConfiguratorPage(PageValidateUtil.checkCurrentPage(currentPage),
				PageValidateUtil.checkPageSize(pageSize),nodeId);
	}
	
	@RequestMapping(value="/findConfiguratorById", method=RequestMethod.GET)
	public Result<?> findNodeConfiguratorById(@RequestParam(required=true) String id){
		
		return zkDubboService.findConfiguratorById(id);
	}
		
}
