package cn.ce.platform_manage.gateway;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.ce.platform_service.common.Result;
import cn.ce.platform_service.common.page.Page;
import cn.ce.platform_service.gateway.entity.GatewayColonyEntity;
import cn.ce.platform_service.gateway.entity.GatewayNodeEntity;
import cn.ce.platform_service.gateway.service.impl.GatewayManageService;
import cn.ce.platform_service.util.PageValidateUtil;

/**
 *	
 * @author makangwei
 * 2017-8-4
 */
@Controller
@RequestMapping(value="/gateway")
public class GatewayController {

	Logger LOGGER = LoggerFactory.getLogger(GatewayController.class);
	
	@Autowired
	private GatewayManageService gatewayManageService;

	
	//添加集群
	@RequestMapping(value="/addGatewayColony", method=RequestMethod.POST,consumes="application/json",produces="application/json;charset=utf-8")
	@ResponseBody
	public Result<GatewayColonyEntity> addGatewayCol(HttpServletRequest request,HttpServletResponse response, @RequestBody GatewayColonyEntity colEntity){
		LOGGER.info("---------------------new gateway colony params----------------------------");
		LOGGER.info(colEntity.toString());

		return gatewayManageService.addGatewayCol(colEntity);
	}
	
	//修改集群
	@RequestMapping(value="/modifyGatewayColony", method=RequestMethod.POST,consumes="application/json",produces="application/json;charset=utf-8")
	@ResponseBody
	public Result<GatewayColonyEntity> modifyGatewayCol(HttpServletRequest request,HttpServletResponse response, @RequestBody GatewayColonyEntity colEntity){
		LOGGER.info("---------------------new gateway colony params----------------------------");
		LOGGER.info(colEntity.toString());

		return gatewayManageService.modifyGatewayCol(colEntity);
	}
	
	//获取所有集群
	@RequestMapping(value="/getAllGatewayColony",method=RequestMethod.GET)
	@ResponseBody
	public Result<Page<GatewayColonyEntity>> getAllGatewayCol(HttpServletRequest request, HttpServletResponse response,Integer currentPage,Integer pageSize){
		
		if(currentPage == null || currentPage < 1){
			currentPage = 1;
		} 
		if(pageSize == null || pageSize > 30){
			pageSize = 10;
		}
		
		return gatewayManageService.getAllGatewayCol(currentPage,pageSize);
	}
	
	//删除集群
	@RequestMapping(value="/deleteGatewayColonyById/{colId}", method=RequestMethod.GET, produces="application/json;charset=utf-8")
	@ResponseBody
	public Result<String> deleteGatewayColonyById(HttpServletRequest request,HttpServletResponse response,
			@PathVariable Integer colId){
		
		LOGGER.info("----------------根据ip删除集群--------------------");
		LOGGER.info("colId:"+colId);
		return gatewayManageService.deleteGatewayColonyById(colId);
	}

	//添加网关节点
	@RequestMapping(value="/addGatewayNode",method=RequestMethod.POST)
	@ResponseBody
	public Result<String> addGatewayNode(HttpServletRequest request,HttpServletResponse response,
			@RequestBody GatewayNodeEntity nodeEntity){
		
		LOGGER.info("---------------------new gateway colony params----------------------------");
		LOGGER.info(nodeEntity.toString());
		nodeEntity.setNodeId(null);
		return gatewayManageService.addGatewayNode(nodeEntity);
	}
	
	//根据id查询所有集群节点
	@RequestMapping(value="/getAllGatewayNode",method=RequestMethod.GET)
	@ResponseBody
	public Result<Page<GatewayNodeEntity>> getAllGatewayNode(HttpServletRequest request,HttpServletResponse response,Integer currentPage,Integer pageSize,
			@RequestParam(required=true)String colId){
		
		return gatewayManageService.getAllGatewayNode(PageValidateUtil.checkCurrentPage(currentPage),PageValidateUtil.checkPageSize(pageSize),colId);
	}
	
	//删除网关节点
	@RequestMapping(value="/deleteGatewayNodeById",method=RequestMethod.GET)
	@ResponseBody
	public Result<String> deleteGatewayNodeById(HttpServletRequest request,HttpServletResponse response,
			@RequestParam(required = true) String nodeId){
		
		LOGGER.info("----------------删除集群节点--------------------");
		LOGGER.info("colId:"+nodeId);
		
		return gatewayManageService.deleteGatewayNodeById(nodeId);
	}
	
	//修改网关节点
	@RequestMapping(value="/modifyGatewayNodeById",method=RequestMethod.POST)
	@ResponseBody
	public Result<String> modifyGatewayNodeById(HttpServletRequest request,HttpServletResponse response, 
			@RequestBody GatewayNodeEntity nodeEntity ){
		
		
		LOGGER.info("----------------根据id修改集群节点--------------------");
		LOGGER.info(nodeEntity.toString());
		
		return gatewayManageService.modifyGatewayNodeById(nodeEntity);
	}

}
