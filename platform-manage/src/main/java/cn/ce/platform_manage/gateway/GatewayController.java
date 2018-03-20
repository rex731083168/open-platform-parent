package cn.ce.platform_manage.gateway;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import cn.ce.platform_service.common.ErrorCodeNo;
import cn.ce.platform_service.common.Result;
import cn.ce.platform_service.common.Status;
import cn.ce.platform_service.common.page.Page;
import cn.ce.platform_service.gateway.entity.GatewayColonyEntity;
import cn.ce.platform_service.gateway.entity.GatewayNodeEntity;
import cn.ce.platform_service.gateway.entity.QueryGwColonyEntity;
import cn.ce.platform_service.gateway.entity.QueryGwNodeEntity;
import cn.ce.platform_service.gateway.service.IGatewayManageService;
import io.swagger.annotations.ApiOperation;

/**
 *	
 * @author makangwei
 * 2017-8-4
 */
@RestController
@RequestMapping(value="/gateway")
public class GatewayController {

	Logger LOGGER = LoggerFactory.getLogger(GatewayController.class);
	
	@Resource
	private IGatewayManageService gatewayManageService;

	
	//添加集群
	@RequestMapping(value="/addGatewayColony", method=RequestMethod.POST)
	public Result<GatewayColonyEntity> addGatewayCol(@RequestBody GatewayColonyEntity colEntity){
		LOGGER.info("---------------------new gateway colony params----------------------------");
		LOGGER.info(colEntity.toString());

		return gatewayManageService.addGatewayCol(colEntity);
	}
	
	//修改集群
	@RequestMapping(value="/modifyGatewayColony", method=RequestMethod.POST)
	public Result<GatewayColonyEntity> modifyGatewayCol(@RequestBody GatewayColonyEntity colEntity){
		LOGGER.info("---------------------modify gateway colony params----------------------------");
		LOGGER.info(colEntity.toString());

		return gatewayManageService.modifyGatewayCol(colEntity);
	}
	
	//获取所有集群
	@RequestMapping(value="/getAllGatewayColony",method=RequestMethod.GET)
	@ApiOperation("获取分页集群列表_TODO")
//	public Result<Page<GatewayColonyEntity>> getAllGatewayCol(@RequestBody QueryGwColonyEntity colEntity){
	public Result<Page<GatewayColonyEntity>> getAllGatewayCol(Integer currentPage,Integer pageSize){
		
		QueryGwColonyEntity colEntity = new QueryGwColonyEntity();
		colEntity.setCurrentPage(currentPage);
		colEntity.setPageSize(pageSize);
		return gatewayManageService.getAllGatewayCol(colEntity);
	}
	
	//删除集群
	@RequestMapping(value="/deleteGatewayColonyById/{colId}", method=RequestMethod.GET)
	public Result<String> deleteGatewayColonyById(@PathVariable String colId){
		
		LOGGER.info("----------------根据ip删除集群--------------------");
		LOGGER.info("colId:"+colId);
		return gatewayManageService.deleteGatewayColonyById(colId);
	}

	//添加网关节点
	@RequestMapping(value="/addGatewayNode",method=RequestMethod.POST)
	public Result<String> addGatewayNode(@RequestBody GatewayNodeEntity nodeEntity){
		
		LOGGER.info("---------------------new gateway colony params----------------------------");
		LOGGER.info(nodeEntity.toString());
		nodeEntity.setNodeId(null);
		return gatewayManageService.addGatewayNode(nodeEntity);
	}
	
	//根据id查询所有集群节点
	@RequestMapping(value="/getAllGatewayNode",method=RequestMethod.GET)
	@ApiOperation("获取分页网关节点")
//	public Result<Page<GatewayNodeEntity>> getAllGatewayNode(@RequestBody QueryGwNodeEntity queryEntity){
	public Result<Page<GatewayNodeEntity>> getAllGatewayNode(Integer currentPage,Integer pageSize,
			@RequestParam(required=true)String colId){
		QueryGwNodeEntity queryEntity = new QueryGwNodeEntity();
		queryEntity.setColId(colId);
		queryEntity.setCurrentPage(currentPage);
		queryEntity.setPageSize(pageSize);
		return gatewayManageService.getAllGatewayNode(queryEntity);
	}
	
	//删除网关节点
	@RequestMapping(value="/deleteGatewayNodeById/{nodeId}",method=RequestMethod.GET)
	public Result<String> deleteGatewayNodeById(@PathVariable String nodeId){
		
		LOGGER.info("----------------删除集群节点--------------------");
		LOGGER.info("colId:"+nodeId);
		
		return gatewayManageService.deleteGatewayNodeById(nodeId);
	}
	
	//修改网关节点
	@RequestMapping(value="/modifyGatewayNodeById",method=RequestMethod.POST)
	public Result<String> modifyGatewayNodeById(@RequestBody GatewayNodeEntity nodeEntity ){
		
		
		LOGGER.info("----------------根据id修改集群节点--------------------");
		LOGGER.info(nodeEntity.toString());
		
		if(StringUtils.isBlank(nodeEntity.getNodeUrl())){
			return new Result<String>("请求url不能为空",ErrorCodeNo.SYS005,null,Status.FAILED);
		}
		if(StringUtils.isBlank(nodeEntity.getNodeName())){
			return new Result<String>("请求节点名称不能为空",ErrorCodeNo.SYS005,null,Status.FAILED);
		}
		if(StringUtils.isBlank(nodeEntity.getColId())){
			return new Result<String>("所属集群id不能为空",ErrorCodeNo.SYS005,null,Status.FAILED);
		}
		if(StringUtils.isBlank(nodeEntity.getNodeId())){
			return new Result<String>("节点id不能为空",ErrorCodeNo.SYS005,null,Status.FAILED);
		}
		return gatewayManageService.modifyGatewayNodeById(nodeEntity);
	}
	
}
