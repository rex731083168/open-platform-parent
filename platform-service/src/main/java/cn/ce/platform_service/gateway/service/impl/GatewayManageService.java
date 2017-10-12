package cn.ce.platform_service.gateway.service.impl;


import java.util.List;
import java.util.UUID;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import cn.ce.platform_service.common.Constants;
import cn.ce.platform_service.common.Result;
import cn.ce.platform_service.common.Status;
import cn.ce.platform_service.common.gateway.ApiCallUtils;
import cn.ce.platform_service.common.page.Page;
import cn.ce.platform_service.gateway.dao.IGatewayManageDao;
import cn.ce.platform_service.gateway.dao.IGatewayNodeManageDao;
import cn.ce.platform_service.gateway.entity.GatewayColonyEntity;
import cn.ce.platform_service.gateway.entity.GatewayNodeEntity;
import cn.ce.platform_service.gateway.service.IGatewayManageService;

/**
 *
 * @author makangwei
 * 2017-8-4
 */
@Service(value="gatewayManageService")
public class GatewayManageService implements IGatewayManageService{

	
	@Resource
	private IGatewayManageDao gatewayManageDao;
	@Resource
	private IGatewayNodeManageDao gatewayNodeManageDao;
	
	Logger _LOGGER = LoggerFactory.getLogger(GatewayManageService.class);
	/**
	 * 添加网关集群
	 * @param colEntity
	 * @return
	 */
	public Result<GatewayColonyEntity> addGatewayCol(GatewayColonyEntity colEntity) {
		
		Result<GatewayColonyEntity> result = new Result<GatewayColonyEntity>();
		
		//判断参数是否可用
		if(colEntity.getColUrl() == null || colEntity.getColUrl().trim() == ""){
			result.setMessage("代理域名不能为空！");
			return result;
		}
		if(colEntity.getColName() == null || colEntity.getColName().trim() == ""){
			result.setMessage("集群名称不能为空");
			return result;
		}
		if(colEntity.getColStatus() == null ){
			colEntity.setColStatus(0); //默认集群是关闭的
		}
		
		List<GatewayColonyEntity> urlList = gatewayManageDao.findByField("colUrl", colEntity.getColUrl(), GatewayColonyEntity.class);
		if(urlList.size()>0){
			result.setMessage("当前集群url已经存在，无需重复添加");
			return result;
		}
		
		//查询当前网关id是否存在
		List<GatewayColonyEntity> isExits = null;
		do{
			isExits = gatewayManageDao.findByField("_id", colEntity.getColId(), GatewayColonyEntity.class);
		}while(isExits.size() >0);
		
		
		//对集群的测试
		boolean reloadBool = ApiCallUtils.getGwReload(colEntity.getColUrl()+Constants.NETWORK_RELOAD_PATH);
		
		if(!reloadBool){
			result.setMessage("该集群无法在网络中查找到，请填写正确的ip和端口号码");
			return result;
		}
		
		boolean bool = gatewayManageDao.addGatewayCol(colEntity);
		
		if(bool){
			result.setMessage("集群添加成功");
			result.setData(colEntity);
			result.setStatus(Status.SUCCESS);
		}
		
		return result;
	}

	/**
	 * 
	 * @Description : 根据id修改集群信息
	 * @Author : makangwei
	 * @Date : 2017-8-7
	 * @param colEntity
	 * @return
	 */
	public Result<GatewayColonyEntity> modifyGatewayCol(GatewayColonyEntity colEntity) {
		
		Result<GatewayColonyEntity> result = new Result<GatewayColonyEntity>();
		
		//判断参数是否可用
		if(StringUtils.isBlank(colEntity.getColId())){
			result.setMessage("当前集群id不能为空");
			return result;
		}
		if(colEntity.getColUrl() == null || colEntity.getColUrl().trim() == ""){
			result.setMessage("代理域名不能为空");
			return result;
		}
		if(colEntity.getColName() == null || colEntity.getColName().trim() == ""){
			result.setMessage("集群名称不能为空");
			return result;
		}
		if(colEntity.getColStatus() == null ){
			colEntity.setColStatus(0); //默认集群是关闭的
		}
		List<GatewayColonyEntity> urlList = gatewayManageDao.findByField("colUrl", colEntity.getColUrl(), GatewayColonyEntity.class);
		if(urlList.size()>0){
			result.setMessage("当前集群url已经存在，无需重复添加");
			return result;
		}
		
		//查询当前网关id是否存在
		List<GatewayColonyEntity> isExits = gatewayManageDao.findByField("_id", colEntity.getColId(), GatewayColonyEntity.class);
		if(isExits.size() <1){
			result.setMessage("当前集群不存在，请传入正确的集群id");
			return result;
		}
		
		//对集群的测试
		boolean reloadBool = ApiCallUtils.getGwReload(colEntity.getColUrl()+Constants.NETWORK_RELOAD_PATH);
		
		if(!reloadBool){
			result.setMessage("该集群无法在网络中查找到，请填写正确的ip和端口号码");
			return result;
		}
		
		//boolean bool = gatewayManageDao.addGatewayCol(colEntity);
		gatewayManageDao.updateById(colEntity.getColId(), colEntity );
		result.setSuccessData(colEntity);
		return result;
	}
	
	/**
	 * 查询网关集群
	 * @param pageSize 
	 * @param currentPage 
	 * @return
	 */
	public Result<Page<GatewayColonyEntity>> getAllGatewayCol(Integer currentPage, Integer pageSize) {
		
		Result<Page<GatewayColonyEntity>> result = new Result<Page<GatewayColonyEntity>>();
		
		Page<GatewayColonyEntity> page = gatewayManageDao.getAllGatewayCol(currentPage,pageSize);
		
	
		
		if(page != null){
			result.setSuccessData(page);
		}else{
			result.setErrorMessage("查不到数据");
		}
		
		return result;
	}

	/**
	 * 删除集群
	 * @param colId
	 * @return
	 */
	public Result<String> deleteGatewayColonyById(Integer colId) {
		
		Result<String> result = new Result<String>();
		
		boolean bool= gatewayManageDao.deleteGatewayColonyById(colId);
		
		if(bool){
			result.setStatus(Status.SUCCESS);
			result.setMessage("删除成功");
			return result;
		}else{
			result.setMessage("删除失败，查看当前集群是否存在");
			return result;
		}
		
	}

	/**
	 * 添加集群节点
	 * @param nodeEntity
	 * @return
	 */
	public Result<String> addGatewayNode(GatewayNodeEntity nodeEntity) {
		
		Result<String> result = new Result<String>();
		result.setErrorMessage("");
		
		if(nodeEntity.getNodeUrl() == null || nodeEntity.getNodeUrl().trim() == ""){
			result.setMessage("请求url不能为空");
			return result;
		}
		if(nodeEntity.getNodeName() == null || nodeEntity.getNodeName().trim() == ""){
			result.setMessage("请求节点名称不能为空");
			return result;
		}
		if(StringUtils.isBlank(nodeEntity.getColId())){
			result.setMessage("所属集群id不能为空");
			return result;
		}
		
		GatewayColonyEntity gatewayColonyEntity = gatewayManageDao.findById(nodeEntity.getColId(), GatewayColonyEntity.class);
		
		if(gatewayColonyEntity == null || gatewayColonyEntity.getColDesc() == null){
			result.setMessage("当前集群不存在");
			return result;
		}
		
		List<GatewayNodeEntity> urlList = gatewayNodeManageDao.findByField("nodeUrl", nodeEntity.getNodeUrl(), GatewayNodeEntity.class);
		if(urlList.size()>0){
			result.setMessage("当前节点url已经存在，无需重复添加");
			return result;
		}
		
		boolean reloadBool = ApiCallUtils.getGwReload(nodeEntity.getNodeUrl()+Constants.NETWORK_RELOAD_PATH);
		
		if(!reloadBool){
			result.setMessage("该节点无法在网络中查找到，请填写正确的ip和端口号码");
			return result;
		}
		
		String uuid = UUID.randomUUID().toString().replace("-", "");
		nodeEntity.setNodeId(uuid);
		
		boolean bool = gatewayNodeManageDao.addGatewayNode(nodeEntity);
		
		if(bool){
			result.setMessage("添加成功");
			
			//TODO 向新的网关中加入已经添加到别的网关的接口
			result.setStatus(Status.SUCCESS);
		}else{
			result.setMessage("添加失败");
		}
		
		return result;
	}

	public Result<Page<GatewayNodeEntity>> getAllGatewayNode(Integer currentPage, Integer pageSize,
			Integer colId) {
		
		
		Result<Page<GatewayNodeEntity>> result = new Result<Page<GatewayNodeEntity>>();
		
		Page<GatewayNodeEntity> page = gatewayNodeManageDao.getAllGatewayNode(currentPage,pageSize,colId);
		
		if(page != null){
			result.setSuccessData(page);
		}else{
			result.setErrorMessage("查不到数据");
		}
		return result;
	}

	public Result<String> deleteGatewayNodeById(String nodeId) {
		Result<String> result = new Result<String>();
		
		boolean bool= gatewayNodeManageDao.deleteGatewayNodeById(nodeId);
		
		if(bool){
			result.setStatus(Status.SUCCESS);
			result.setMessage("删除成功");
			return result;
		}else{
			result.setMessage("删除失败，查看当前集群是否存在");
			return result;
		}
	}

	public Result<String> modifyGatewayNodeById(GatewayNodeEntity nodeEntity) {
		
		Result<String> result = new Result<String>();
		
		if(nodeEntity.getNodeUrl() == null || nodeEntity.getNodeUrl().trim() == ""){
			result.setMessage("请求url不能为空");
			return result;
		}
		if(nodeEntity.getNodeName() == null || nodeEntity.getNodeName().trim() == ""){
			result.setMessage("请求节点名称不能为空");
			return result;
		}
		if(StringUtils.isBlank(nodeEntity.getColId())){
			result.setMessage("所属集群id不能为空");
			return result;
		}
		
		if(nodeEntity.getNodeId() == null || nodeEntity.getNodeId().trim() == ""){
			result.setMessage("节点id不能为空");
			return result;
		}
	
		List<GatewayNodeEntity> nodeEntitys = gatewayNodeManageDao.findByField("_id", nodeEntity.getNodeId(), GatewayNodeEntity.class);
	
		if(nodeEntitys.size() < 1){
			result.setMessage("当前节点Id不存在");
			return result;
		}
		
		GatewayColonyEntity gatewayColonyEntity = gatewayManageDao.findById(nodeEntity.getColId(), GatewayColonyEntity.class);
		
		if(gatewayColonyEntity == null || gatewayColonyEntity.getColDesc() == null){
			result.setMessage("当前集群不存在");
			return result;
		}
		
		List<GatewayNodeEntity> urlList = gatewayNodeManageDao.findByField("nodeUrl", nodeEntity.getNodeUrl(), GatewayNodeEntity.class);
		if(urlList.size()>0){
			result.setMessage("当前节点url已经存在，无需重复添加");
			return result;
		}
		
		boolean reloadBool = ApiCallUtils.getGwReload(nodeEntity.getNodeUrl()+Constants.NETWORK_RELOAD_PATH);
		
		if(!reloadBool){
			result.setMessage("该节点无法在网络中查找到，请填写正确的ip和端口号码");
			return result;
		}
		
		try{
			
			gatewayNodeManageDao.updateById(nodeEntity.getNodeId(), nodeEntity);
			result.setMessage("修该成功");
			result.setStatus(Status.SUCCESS);
			return result;
		
		}catch(Exception e){
			
			result.setMessage("修改失败");
		
		return result;
		}
		
	}



}
