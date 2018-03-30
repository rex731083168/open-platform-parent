package cn.ce.platform_service.sandbox.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

import javax.annotation.Resource;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;

import cn.ce.platform_service.common.Constants;
import cn.ce.platform_service.common.ErrorCodeNo;
import cn.ce.platform_service.common.Result;
import cn.ce.platform_service.common.Status;
import cn.ce.platform_service.common.gateway.ApiCallUtils;
import cn.ce.platform_service.common.gateway.GatewayUtils;
import cn.ce.platform_service.common.page.Page;
import cn.ce.platform_service.gateway.entity.BoxPool;
import cn.ce.platform_service.gateway.service.ISaasService;
import cn.ce.platform_service.sandbox.dao.ISandBoxDao;
import cn.ce.platform_service.sandbox.entity.QuerySandBox;
import cn.ce.platform_service.sandbox.entity.SandBox;
import cn.ce.platform_service.sandbox.service.ISandBoxService;
import cn.ce.platform_service.util.PropertiesUtil;
import cn.ce.platform_service.util.RandomUtil;
import io.netty.handler.codec.http.HttpMethod;
import io.swagger.models.Method;
import net.sf.json.JSONObject;

/**
* @Description : 说明
* @Author : makangwei
* @Date : 2018年3月23日
*/
@Service(value="sandBoxService")
@Transactional(propagation=Propagation.REQUIRED)
public class SandBoxServiceImpl implements ISandBoxService{

	@Resource
	private ISandBoxDao sandBoxDao;
	@Resource
	private ISaasService saasService;
	
	private static final Logger _LOGGER = LoggerFactory.getLogger(SandBoxServiceImpl.class);
	private static final int resultSuccess = 1;
	private static final String domainSuffix  = "design.yun300.cn"; //域名后缀
	private static final String TEMPLATE_NAME = "templatename";
	private static final String RESOURCE_POOL = "resourcePool";
	private static final String SANDBOX_NAME = "sandboxname";
	private static final String boxStateUrl = PropertiesUtil.getInstance().getValue("box_state");
	private static final String boxPoolUrl = PropertiesUtil.getInstance().getValue("box_getPool");
	private static final String boxDeleteUrl = PropertiesUtil.getInstance().getValue("box_delete");
	private static final String boxAddUrl = PropertiesUtil.getInstance().getValue("box_add");
	@Override
	public Result<?> getResourcePool() {
		String reStr = ApiCallUtils.getOrDelMethod(boxPoolUrl, null, HttpMethod.GET);
		_LOGGER.info("获取沙箱resourcePool："+reStr);
		BoxPool pool = JSON.parseObject(reStr, BoxPool.class);
		BoxPool.Pool[] pools = null;
		if(null != pool && 1 == pool.getResult()){
			pools = pool.getData();
		}
		return new Result<BoxPool.Pool[]>("",ErrorCodeNo.SYS000,pools,Status.SUCCESS);
	}
	
	@Override
	public Result<?> andBox(SandBox sandBox) {
		
		if(null != sandBoxDao.findByName(sandBox.getName())){
			return Result.errorResult("沙箱已存在", ErrorCodeNo.SYS009, null, Status.FAILED);
		}
		
		if(null == sandBox.getTemplateName()){
			return Result.errorResult("模板名称不能为空", ErrorCodeNo.SYS005, null, Status.FAILED);
		}
		if(null == sandBox.getResourcePool()){
			return Result.errorResult("沙箱资源池不能为空", ErrorCodeNo.SYS005, null, Status.FAILED);
		}
		
		sandBox.setBoxId(RandomUtil.random32UUID());
		//调用paas接口
		
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair(SANDBOX_NAME,sandBox.getBoxId())); //将沙箱id作为paas的sandboxname
		params.add(new BasicNameValuePair(TEMPLATE_NAME,sandBox.getTemplateName().toString()));
		params.add(new BasicNameValuePair(RESOURCE_POOL,sandBox.getResourcePool().toString()));
		String str = ApiCallUtils.putOrPostForm(boxAddUrl,params,null,HttpMethod.POST);
		_LOGGER.info("添加沙箱返回结果:"+str);
		JSONObject jsonRet = JSONObject.fromString(str);
		if(!(resultSuccess==jsonRet.getInt("result"))){
			return Result.errorResult("创建失败请稍后再试", ErrorCodeNo.SYS005, null, Status.FAILED);	
		}
		
		sandBox.setBoxUrl(sandBox.getBoxId()+"."+domainSuffix);
		sandBox.setCreateState(SandBox.CreateState.BUILDDING);
		sandBox.setState(1);
		sandBox.setDeleted(false);
		sandBox.setCreateDate(new Date());
		
		sandBoxDao.saveBox(sandBox);
		//如果paas返回成功修改添加到数据库
		
		//返回创建成功，否则返回创建失败
		return new Result<String>("创建成功",ErrorCodeNo.SYS000,null,Status.SUCCESS);
	}

	@Override
	public Result<?> getOne(String boxId) {
		
		// TODO 查询回来后，查看构建状态.如果构建状态有变化。跟新构建状态
		SandBox sandBox = sandBoxDao.findById(boxId);
		if(null == sandBox || null == sandBox.getBoxId()){
			return Result.errorResult("查询结果不存在", ErrorCodeNo.SYS015, null, Status.FAILED);
		}
		
		sandBox = validateAllState(sandBox);
		
		return new Result<SandBox>("",ErrorCodeNo.SYS000,sandBox,Status.SUCCESS);
	}


	@Override
	public Result<?> boxList(QuerySandBox queryBox) {
		//数据库中查询 box列表,地址，名称模糊，支持根据状态查询
		int totalNum = sandBoxDao.findListSize(queryBox);
		
		List<SandBox> boxList = sandBoxDao.getPagedList(queryBox);
		List<SandBox> boxList1 = new ArrayList<SandBox>();
		for (SandBox sandBox : boxList) {
			boxList1.add(validateAllState(sandBox));
		}
		
		Page<SandBox> page = new Page<SandBox>(queryBox.getCurrentPage(),totalNum,queryBox.getPageSize());
		page.setItems(boxList1);
		// TODO 查询回来后，查看构建状态.如果构建状态有变化。跟新构建状态
		return new Result<Page<SandBox>>("",ErrorCodeNo.SYS000,page,Status.SUCCESS);
	}

	@Override
	public Result<?> deleteOne(String boxId) {

		SandBox sandBox = sandBoxDao.findById(boxId);
		if(null == sandBox || null == sandBox.getBoxId()){
			return Result.errorResult("查询结果不存在", ErrorCodeNo.SYS015, null, Status.FAILED);
		}
		
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair(SANDBOX_NAME,sandBox.getBoxId())); //将沙箱id作为paas的sandboxname
		params.add(new BasicNameValuePair(RESOURCE_POOL,sandBox.getResourcePool().toString()));
		String str = ApiCallUtils.putOrPostForm(boxDeleteUrl,params,null,HttpMethod.POST);
		
		JSONObject jsonRet = JSONObject.fromString(str);
		if(resultSuccess == jsonRet.getInt("result")){
			sandBox.setDeleted(true);
			sandBoxDao.updateBox(sandBox);
			return new Result<String>("",ErrorCodeNo.SYS000,"",Status.SUCCESS);
		}else{
			return new Result<String>("删除失败",ErrorCodeNo.SYS034,"",Status.FAILED);
		}
	}

	
	@Override
	public Result<?> andRoute(String saasId, String resourceType, String targetUrl , String boxId) {
		Result<?> result = new Result<>();
		
		String url = GatewayUtils.getAllGatewayColony().get(0).getColUrl()+Constants.NETWORK_ROUTE_BOX;
		
		org.json.JSONObject params = new org.json.JSONObject();
		params.put("saas_id", saasId);
		params.put("resource_type",resourceType);
		params.put("sandbox_id", boxId);
		params.put("target_url", targetUrl);
		
		boolean postGwJson = ApiCallUtils.postGwJson(url, params);
		
		if(postGwJson){
			saasService.saveBoxSaas(saasId, resourceType, targetUrl, boxId,Method.POST.toString());
			result.setSuccessMessage("add router Success!");
		} else {
			result.setErrorMessage("add router fail!");
		}
		
		return result;
	}

	@Override
	public Result<?> updateRoute(String saasId, String resourceType, String targetUrl , String boxId) {
		
		Result<?> result = new Result<>();
		
		String url = GatewayUtils.getAllGatewayColony().get(0).getColUrl()+Constants.NETWORK_ROUTE_BOX;
		
		org.json.JSONObject params = new org.json.JSONObject();
		params.put("saas_id", saasId);
		params.put("resource_type",resourceType);
		params.put("sandbox_id", boxId);
		params.put("target_url", targetUrl);
		
		boolean postGwJson = ApiCallUtils.postGwJson(url, params);
		
		if(postGwJson){
			saasService.saveBoxSaas(saasId, resourceType, targetUrl, boxId,Method.PUT.toString());
			result.setSuccessMessage("update router Success!");
		} else {
			result.setErrorMessage("update router fail!");
		}

		return result;
	}

	@Override
	public Result<?> deleteRoute(String saasId, String resourceType , String boxId) {
		
		Result<?> result = new Result<>();
		
		String url = GatewayUtils.getAllGatewayColony().get(0).getColUrl()+Constants.NETWORK_ROUTE_BOX;
		url.concat("/").concat(saasId).concat("-").concat(resourceType).concat("-").concat(boxId);
		
		boolean postGwJson = ApiCallUtils.deleteGwJson(url);
		
		if(postGwJson){
			saasService.deleteBoxRoute(saasId, resourceType, boxId, Method.DELETE.toString());
			result.setSuccessMessage("update router Success!");
		} else {
			result.setErrorMessage("update router fail!");
		}
		
		return result;
	}
	
	private SandBox validateAllState(SandBox sandBox) {
		String stateUrl = boxStateUrl;
		String sandboxname$ = Pattern.quote("${sandboxname}");
		String resourcePool$ = Pattern.quote("${resourcePool}");
		String replaceUrl = stateUrl.replaceAll(sandboxname$, sandBox.getBoxId())
			.replaceAll(resourcePool$, sandBox.getResourcePool());;
		
		String resultStr = ApiCallUtils.getOrDelMethod(replaceUrl, null, HttpMethod.GET);
		JSONObject job = JSONObject.fromString(resultStr);
		JSONObject paasState = job.getJSONObject("data");
		if(null == paasState){
			//说明paas平台的沙箱根本就不存在。所以将数据库中的沙箱状态改为删除
			sandBox.setDeleted(true);
			sandBoxDao.updateBox(sandBox);
			return sandBox;
		}
		if(null != sandBox.getCreateState() 
				&& sandBox.getCreateState().equals(paasState.getString("status"))
				&& sandBox.isDeleted() == paasState.getBoolean("deleted")){
			return sandBox;
		}else{
			sandBox.setDeleted(paasState.getBoolean("deleted"));
			sandBox.setCreateState(SandBox.CreateState.valueOf(paasState.getString("status")));
			sandBoxDao.updateBox(sandBox);
			return sandBox;
		}
	}

}

