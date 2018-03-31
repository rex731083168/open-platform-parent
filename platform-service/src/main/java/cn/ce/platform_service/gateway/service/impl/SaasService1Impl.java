package cn.ce.platform_service.gateway.service.impl;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import cn.ce.platform_service.common.Constants;
import cn.ce.platform_service.common.ErrorCodeNo;
import cn.ce.platform_service.common.Result;
import cn.ce.platform_service.common.Status;
import cn.ce.platform_service.common.gateway.ApiCallUtils;
import cn.ce.platform_service.common.gateway.GatewayUtils;
import cn.ce.platform_service.common.page.Page;
import cn.ce.platform_service.gateway.dao.IMysqlSaasDao;
import cn.ce.platform_service.gateway.entity.QuerySaasEntity;
import cn.ce.platform_service.gateway.entity.SaasEntity;
import cn.ce.platform_service.gateway.service.ISaasService1;
import cn.ce.platform_service.util.RandomUtil;

/**
* @Description : 开放平台内部使用的saas service
* @Author : makangwei
* @Date : 2018年3月31日
*/
@Service(value="saasService1")
public class SaasService1Impl implements ISaasService1{
	
	@Resource
	private IMysqlSaasDao saasDao;

	@Override
	public Result<?> saveOrUpdateBoxSaas(SaasEntity saasEntity) {
		
		//如果是跟新操作需要判断saasId,resourceType,sandboxId与原来一致
		if(StringUtils.isNotBlank(saasEntity.getRouteId())){
			SaasEntity saas1 = saasDao.getById(saasEntity.getRouteId());
			if(null == saas1){
				return new Result<String>("当前saasId不存在",ErrorCodeNo.SYS006,null,Status.FAILED);
			}
			boolean bool1 = (!saas1.getSaasId().equals(saasEntity.getSaasId())); //saasid是否不同
			boolean bool2 = (!saas1.getResourceType().equals(saasEntity.getResourceType()));//resourceType是否不同
			boolean bool3 = StringUtils.isNoneBlank(saas1.getSandboxId()) ? //sandboxId是否不为空？ 是：sandbodId是否不同：否：添加的sandboxId是否为空
					(!saas1.getSandboxId().equals(saasEntity.getSandboxId())) : StringUtils.isBlank(saasEntity.getSandboxId());
			if(bool1 || bool2 || bool3){
				return new Result<String>("跟新操作部分参数错误",ErrorCodeNo.SYS006,null,Status.FAILED);
			}
		}
		
		String url = GatewayUtils.getAllGatewayColony().get(0).getColUrl()+Constants.NETWORK_ROUTE_BOX;
		org.json.JSONObject params = new org.json.JSONObject();
		params.put("saas_id", saasEntity.getSaasId());
		params.put("resource_type",saasEntity.getResourceType());
		params.put("sandbox_id", saasEntity.getSandboxId());
		params.put("target_url", saasEntity.getTargetUrl());
		boolean postGwJson = ApiCallUtils.postGwJson(url, params);
		if(!postGwJson){
			return new Result<String>("调用网关异常",ErrorCodeNo.SYS014,null,Status.FAILED);
		}
		
		SaasEntity saas2 = saasDao.getBoxSaas(saasEntity.getSaasId(),saasEntity.getResourceType(),saasEntity.getSandboxId());
		if(null ==saas2){
			//新增
			saasEntity.setRouteId(RandomUtil.random32UUID());
			saasEntity.setCreateDate(new Date());
			saasEntity.setUpdateDate(saasEntity.getCreateDate());
			saasDao.save(saasEntity);
		}else{
			//修改
			saas2.setTargetUrl(saasEntity.getTargetUrl());
			saas2.setUpdateDate(new Date());
			saasDao.updateBoxSaas(saasEntity);
		}

		return new Result<SaasEntity>("添加或修改成功",ErrorCodeNo.SYS000,null,Status.SUCCESS);
	}

	@Override
	public Result<?> deleteBoxSaas(String routeId) {
		SaasEntity saas1 = saasDao.getById(routeId);
		if(null == saas1){
			return new Result<String>("当前id不存在",ErrorCodeNo.SYS006,null,Status.FAILED);
		}
		
		String url = GatewayUtils.getAllGatewayColony().get(0).getColUrl()+Constants.NETWORK_ROUTE_BOX;
		url.concat("/").concat(saas1.getSaasId()).concat("/").concat(saas1.getResourceType()).concat("/").concat(saas1.getSandboxId());
		
		boolean postGwJson = ApiCallUtils.deleteGwJson(url);
		
		if(postGwJson){
			saasDao.deleteById(routeId);
			return new Result<String>("删除成功",ErrorCodeNo.SYS000,null,Status.SUCCESS);
		}else{
			return new Result<String>("调用网关异常",ErrorCodeNo.SYS014,null,Status.FAILED);
		}
	}

	@Override
	public Result<?> getBoxSaas(String routeId) {
		SaasEntity saas1 = saasDao.getById(routeId);
		if(null == saas1){
			return new Result<String>("当前id不存在",ErrorCodeNo.SYS006,null,Status.FAILED);
		}else{
			return new Result<SaasEntity>("",ErrorCodeNo.SYS000,saas1,Status.SUCCESS);
		}
	}

	@Override
	public Result<?> boxSaasList(QuerySaasEntity saas) {
		//数据库中查询 box列表,地址，名称模糊，支持根据状态查询
		int totalNum = saasDao.findListSize(saas);
		
		List<SaasEntity> saasList = saasDao.getPagedList(saas);
		
		Page<SaasEntity> page = new Page<SaasEntity>(saas.getCurrentPage(),totalNum,saas.getPageSize());
		page.setItems(saasList);
		
		return new Result<Page<SaasEntity>>("",ErrorCodeNo.SYS000,page,Status.SUCCESS);
	}

	@Override
	public Result<?> migraSaas(SaasEntity saasEntity) {
		//如果是跟新操作需要判断saasId,resourceType,sandboxId与原来一致
		if(StringUtils.isNotBlank(saasEntity.getRouteId())){
			SaasEntity saas1 = saasDao.getById(saasEntity.getRouteId());
			if(null == saas1){
				return new Result<String>("当前saasId不存在",ErrorCodeNo.SYS006,null,Status.FAILED);
			}
			boolean bool1 = (!saas1.getSaasId().equals(saasEntity.getSaasId())); //saasid是否不同
			boolean bool2 = (!saas1.getResourceType().equals(saasEntity.getResourceType()));//resourceType是否不同
			boolean bool3 = StringUtils.isNoneBlank(saas1.getSandboxId()) ? //sandboxId是否不为空？ 是：sandbodId是否不同：否：添加的sandboxId是否为空
					(!saas1.getSandboxId().equals(saasEntity.getSandboxId())) : StringUtils.isBlank(saasEntity.getSandboxId());
			if(bool1 || bool2 || bool3){
				return new Result<String>("跟新操作部分参数错误",ErrorCodeNo.SYS006,null,Status.FAILED);
			}
		}
		
		SaasEntity saas2 = saasDao.getBoxSaas(saasEntity.getSaasId(),saasEntity.getResourceType(),saasEntity.getSandboxId());
		if(null ==saas2){
			//新增
			saasEntity.setRouteId(RandomUtil.random32UUID());
			saasEntity.setCreateDate(new Date());
			saasEntity.setUpdateDate(saasEntity.getCreateDate());
			saasDao.save(saasEntity);
		}else{
			//修改
			saas2.setTargetUrl(saasEntity.getTargetUrl());
			saas2.setUpdateDate(new Date());
			saasDao.updateBoxSaas(saasEntity);
		}

		return new Result<SaasEntity>("添加或修改成功",ErrorCodeNo.SYS000,null,Status.SUCCESS);
	}
	
//	@Override
//	public SaasEntity getBoxSaas(String saasId, String resourceType, String boxId, String method) {
//		return saasDao.getSandboxSaas(saasId, resourceType, boxId);
//	}
//	
//	
//	@Override
//	public int saveBoxSaas(String saasId, String resourceType, String targetUrl, String boxId, String method) {
//		SaasEntity saas = new SaasEntity();
//		saas.setSaasId(saasId);
//		saas.setResourceType(resourceType);
//		saas.setTargetUrl(targetUrl);
//		saas.setSandboxId(boxId);
//		return saasDao.save(saas); 
//		 
//	}
//
//	@Override
//	public int deleteBoxRoute(String saasId, String resourceType, String boxId, String method) {
//		return saasDao.deleteSandboxSaas(saasId, resourceType, boxId);
//	}
//
//	@Override
//	public List<SandBox> getSendBoxSaasList(SaasEntity saas) {
//		return saasDao.getSandBoxRouterList(saas);
//	}
//
//	@Override
//	public Page<SaasEntity> routeList(QuerySaasEntity saas) {
//		//数据库中查询 box列表,地址，名称模糊，支持根据状态查询
//		int totalNum = saasDao.findListSize(saas);
//		
//		List<SaasEntity> saasList = saasDao.getPagedList(saas);
//		
//		Page<SaasEntity> page = new Page<SaasEntity>(saas.getCurrentPage(),totalNum,saas.getPageSize());
//		page.setItems(saasList);
//		// TODO 查询回来后，查看构建状态.如果构建状态有变化。跟新构建状态
//		return page;
//	}
//		
}

