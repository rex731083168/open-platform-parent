package cn.ce.platform_service.apis.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import cn.ce.platform_service.apis.dao.INewApiDao;
import cn.ce.platform_service.apis.entity.ApiEntity;
import cn.ce.platform_service.apis.entity.ApiType;
import cn.ce.platform_service.apis.entity.QueryApiEntity;
import cn.ce.platform_service.apis.service.IConsoleApiService;
import cn.ce.platform_service.common.AuditConstants;
import cn.ce.platform_service.common.DBFieldsConstants;
import cn.ce.platform_service.common.ErrorCodeNo;
import cn.ce.platform_service.common.Result;
import cn.ce.platform_service.common.Status;
import cn.ce.platform_service.common.gateway.GatewayUtils;
import cn.ce.platform_service.common.page.Page;
import cn.ce.platform_service.gateway.entity.GatewayColonyEntity;
import cn.ce.platform_service.gateway.service.IGatewayApiService;
import cn.ce.platform_service.users.entity.User;

/**
* @Description : 说明
* @Author : makangwei
* @Date : 2017年10月12日
*/
@Service(value="consoleApiService")
public class ConsoleApiServiceImpl implements IConsoleApiService{

	private static Logger _LOGGER = LoggerFactory.getLogger(ConsoleApiServiceImpl.class);
	
	@Resource
	private INewApiDao newApiDao;
	@Resource
	private IGatewayApiService gatewayApiService;
	
	
	/**
	 * 
	 * @Title: publishApi
	 * @Description: api发布
	 * @author: makangwei 
	 * @date:   2017年10月12日 上午10:58:11 
	 */
	@Override
	public Result<?> publishApi(User user, ApiEntity apiEntity) {
		
		Result<String> result = new Result<String>();
		
		
		if(StringUtils.isBlank(apiEntity.getListenPath())){
			result.setErrorMessage("listenPath不能为空", ErrorCodeNo.SYS005);
			return result;
		}
		
		apiEntity.setListenPath(checkListenPathFormat(apiEntity.getListenPath()));
		
		_LOGGER.info("************************** listenPath = " + apiEntity.getListenPath());
		
		//设置默认值，否则会后面审核api会报错
		if(apiEntity.getCheckState() == null){
			apiEntity.setCheckState(0);
		}
		
		//如果apiId存在，则修改api
		if(apiEntity.getId() != null ){
			newApiDao.save(apiEntity);
			result.setSuccessMessage("修改成功");
			return result;
		}
		
		
		if(StringUtils.isBlank(apiEntity.getApiVersion().getVersion())){
			result.setErrorMessage("版本名称不能为空", ErrorCodeNo.SYS005);
			return result;
		}
		
		ApiEntity vEntity = newApiDao.findByVersion(apiEntity.getApiVersion().getVersionId(),apiEntity.getApiVersion().getVersion());
		
		if(vEntity != null){
			return Result.errorResult("当前版本已经存在", ErrorCodeNo.SYS025, null, Status.FAILED);
		}
		// 第一次添加接口,并且选择未开启版本控制
//		if (apiEntity.getApiVersion() == null || 
//				StringUtils.isBlank(apiEntity.getApiVersion().getVersion())) {
//			//api添加者信息和添加时间
//			apiEntity.setUserId(user.getId());
//			apiEntity.setUserName(user.getUserName());
//			apiEntity.setCreateTime(new Date());
//			
//			//未开启版本控制的version信息
//			ApiVersion version = new ApiVersion();
//			version.setVersionId(UUID.randomUUID().toString().replace("-", ""));
//			apiEntity.setApiVersion(version);
//			
//			// 过滤apienname不能以/开头和结尾
//			// TODO 2期无法获取开放应用的enName，所以添加时不拼接和处理apiEnName lida 2017年10月18日17:10:19
//			if(StringUtils.isNotBlank(apiEntity.getApiEnName())){
//				apiEntity.setApiEnName(apiEntity.getApiEnName().replaceAll("/", ""));
//			}
//
//			newApiDao.save(apiEntity);
//
//		} else {
			
			// 开启版本控制
			apiEntity.setUserId(user.getId());
			apiEntity.setUserName(user.getUserName());
			apiEntity.setCreateTime(new Date());
			
			int num = newApiDao.updApiVersionByApiId(apiEntity.getApiVersion().getVersionId(),false);
			_LOGGER.info("----->将原来其他版本的api的newVersion字段全部修改为false，一共修改了"+num+"条数据");
			
			// TODO 前端传入版本号和newVersion字段吗？
			apiEntity.getApiVersion().setNewVersion(true);
			//如果前端没有传入版本的版本的apiId则重新生成版本versionId
			if(StringUtils.isBlank(apiEntity.getApiVersion().getVersionId())){
				_LOGGER.info("当前添加的api是新的api不存在旧的版本，生成新的versionId");
				String versionId = UUID.randomUUID().toString().replace("-", "");
				apiEntity.getApiVersion().setVersionId(versionId);
			}
			
			newApiDao.save(apiEntity);
			
			_LOGGER.info("------新添加的数据为："+apiEntity.toString());
//		}
		result.setSuccessData("添加成功");
		return result;
	}
	
	



	/**
	 * @Title: apiVerify
	 * @Description: 从未审核状态改为待审核状态,批量提交审核
	 * @author: makangwei 
	 * @date:   2017年10月12日 上午11:05:13 
	 */
	@Override
	public Result<?> submitApi(List<String> apiIds) {
		
		Result<String> result = new Result<String>();
		for (String apiId : apiIds) {
			ApiEntity api = newApiDao.findApiById(apiId);
			if(null == api){
				_LOGGER.info("当前api:"+apiId+"在数据库中不存在");
				continue;
			}
			api.setCheckState(AuditConstants.API_CHECK_STATE_UNAUDITED);
			newApiDao.save(api);
		}
		result.setSuccessMessage("修改成功");
		return result;
	}

	
	/**
	 * 
	 * @Title: modifyApi
	 * @Description: api修改，审核成功后无法再修改
	 * @author: makangwei 
	 * @date:   2017年10月12日 上午11:18:39 
	 */
	@Override
	public Result<?> modifyApi(ApiEntity apiEntity) {
		
		Result<String> result = new Result<String>();
		if(null == newApiDao.findApiById(apiEntity.getId())){
			result.setErrorMessage("当前id不可用", ErrorCodeNo.SYS006);
			return result;
		}
		newApiDao.save(apiEntity);
		result.setSuccessData("");
		return result;
	}

	
	/**
	 * @Title: showApi
	 * @Description: 单个api信息显示
	 * @author: makangwei 
	 * @date:   2017年10月12日 下午1:04:19 
	 */
	@Override
	public Result<?> showApi(String apiId) {
		
		Result<JSONObject> result = new Result<JSONObject>();
		
		ApiEntity api = newApiDao.findApiById(apiId);
		if (api == null) {
			result.setErrorMessage("当前id不存在",ErrorCodeNo.SYS006);
			return result;
		}

		// 添加网关访问地址
		List<GatewayColonyEntity> colList = GatewayUtils.getAllGatewayColony();
		List<String> gatewayUrlList = new ArrayList<String>();
		List<String> wGatewayUrlList = new ArrayList<String>();
		for (GatewayColonyEntity gatewayColonyEntity : colList) {
//			gatewayUrlList.add(gatewayColonyEntity.getColUrl() +api.getListenPath()+api.getApiVersion().getVersion()+"/");
			gatewayUrlList.add(gatewayColonyEntity.getColUrl()+api.getListenPath());
			wGatewayUrlList.add(gatewayColonyEntity.getwColUrl()+api.getListenPath());//外网访问地址
		}
		
		JSONObject jsonObject = JSONObject.parseObject(JSON.toJSONString(api));
		jsonObject.put("gatewayUrls", gatewayUrlList);
		jsonObject.put("wGatewayUrls", wGatewayUrlList);
		result.setSuccessData(jsonObject);
		return result;
	}

	
	/**
	 * @Title: showApiList
	 * @Description: 提供者查看api列表
	 * @author: makangwei 
	 * @date:   2017年10月12日 下午2:10:36 
	 */
	@Override
	public Result<?> showApiList(QueryApiEntity entity, int currentPage,
			int pageSize) {
		
		Result<Page<ApiEntity>> result = new Result<Page<ApiEntity>>();

		//提供者查看api列表
		Page<ApiEntity> page = newApiDao.findSupplierApis(entity,currentPage, pageSize);
			
		result.setSuccessData(page);
		return result;
	}


	/**
	 * @Title: checkApiEnName
	 * @Description: 校验当前开放应用中是否存在这个英文名称
	 * @author: makangwei 
	 * @date:   2017年10月12日 下午2:49:22 
	 */
	@Override
	public Result<String> checkApiEnName(String apiEnName, String openApplyId) {
		
		Result<String> result = new Result<String>();
		if(StringUtils.isBlank(apiEnName)){
			result.setErrorMessage("apiEnName不能为空",ErrorCodeNo.SYS005);
			return result;
		}
		Map<String,Object> map =new HashMap<String,Object>();
		map.put(DBFieldsConstants.APIS_OPENAPPLY_ID, openApplyId);
		map.put(DBFieldsConstants.APIS_APIENNAME, apiEnName);
		ApiEntity entity = newApiDao.findOneByFields(map);
		
		if(entity == null){
			result.setSuccessMessage("当前名称可以使用");
		}else{
			result.setErrorMessage("当前名称已经被占用", ErrorCodeNo.SYS010);
		}
		return result;
	}


	/**
	 * @Title: checkApiChName
	 * @Description: 校验当前开放应用中是否存在这个中文名称
	 * @author: makangwei 
	 * @date:   2017年10月12日 下午2:49:55 
	 */
	@Override
	public Result<String> checkApiChName(String apiChName, String openApplyId) {
		
		Result<String> result = new Result<String>();
		if(StringUtils.isBlank(apiChName)){
			result.setErrorMessage("apiEnName不能为空",ErrorCodeNo.SYS005);
			return result;
		}
		Map<String,Object> map =new HashMap<String,Object>();
		map.put(DBFieldsConstants.APIS_OPENAPPLY_ID, openApplyId);
		map.put(DBFieldsConstants.APIS_APICHNAME, apiChName);
		ApiEntity entity = newApiDao.findOneByFields(map);
		
		if(entity == null){
			result.setSuccessMessage("当前名称可用");
		}else{
			result.setErrorMessage("当前名称已经被占用",ErrorCodeNo.SYS010);
		}
		return result;
	}

	/**
	 * @Title: checkVersion
	 * @Description: 查看当前api是否存在该版本
	 * @author: makangwei 
	 * @date:   2017年10月12日 下午2:50:36 
	 */
	@Override
	public Result<String> checkVersion(String versionId, String version) {
		Result<String> result = new Result<String>();
		
		ApiEntity entity = newApiDao.findByVersion(versionId, version);

		if(entity == null){
			result.setSuccessMessage("当前version不存在，可以使用");
		}else{
			result.setErrorMessage("当前version已经存在",ErrorCodeNo.SYS010);
		}
		return result;
	}
	
	/**
	 * @Title: boundDiyApplyWithApi
	 * @Description: 将定制应用和api发生绑定，将绑定关系推送到网关 。
	 * @author: makangwei 
	 * @date:   2017年10月17日 下午5:55:34 
	 */
	public boolean boundDiyApplyWithApi(
			String policyId,
			String clientId,
			String secret,
			Integer rate,
			Integer per,
			Integer quotaMax,
			Integer quotaRenewRate,
			List<String> openApplyIds){
		
		//获取versionId集合以及用逗号隔开的长数组
//		List<ApiEntity> apiEntityList = newApiDao.findApiByApplyIdsAndCheckState(openApplyIds,AuditConstants.API_CHECK_STATE_SUCCESS);
		List<ApiEntity> apiEntityList = newApiDao.findApiByApplyIdsAndCheckState(openApplyIds,AuditConstants.API_CHECK_STATE_SUCCESS, ApiType.OPEN);
		_LOGGER.info("根据开放应用Id查询的即将绑定的api数量"+apiEntityList.size());
		StringBuffer versionIdsBuf = new StringBuffer(); // versionId用逗号分隔的长字符串
		Set<String> versionIdList = new HashSet<String>(); //versionId的集合
		for (ApiEntity apiEntity : apiEntityList) { //装参数
			if(StringUtils.isNotBlank(apiEntity.getApiVersion().getVersionId())){
				versionIdList.add(apiEntity.getApiVersion().getVersionId());
				versionIdsBuf.append(","+apiEntity.getApiVersion().getVersionId());
			}
		}
		if(!(versionIdsBuf.length() > 0)){
			_LOGGER.info("versionidBuf的长度为0,查询不到api");
			_LOGGER.info("不推送网关，直接分配clientId和secret");
			// TODO 紧急
			return true;
		}
		
		versionIdsBuf.deleteCharAt(0); //versionId用逗号分隔的长字符串
		Map<String, List<String>> apiInfos = new HashMap<String,List<String>>();
		for (String versionId : versionIdList) {
			List<ApiEntity> apiList = newApiDao.findByField("apiVersion.versionId", versionId);
			List<String> versionList = new ArrayList<String>();
			for (ApiEntity apiEntity : apiList) {
				versionList.add(apiEntity.getApiVersion().getVersion());
			}
			apiInfos.put(versionId, versionList);
		}
		//推送policy
		String policyResult =  gatewayApiService.pushPolicy(policyId, rate, per, quotaMax, quotaRenewRate, apiInfos);
		
		if(StringUtils.isBlank(policyResult)){ //推送policy失败
			_LOGGER.error("____________>error happens when execute push policy to gateway");
			return false;
		}
		
		//推送clientId;
		String clientResult = gatewayApiService.pushClient(clientId, secret, versionIdsBuf, policyId);
		
		if(StringUtils.isBlank(clientResult)){
			_LOGGER.error("____________>error happens when execute push client to gateway");
			return false;
		}
		
		return true;
	}


	@Override
	public Result<?> checkListenPath(String listenPath) {
		
		List<ApiEntity> list = newApiDao.findByField(DBFieldsConstants.APIS_LISTEN_PATH, listenPath);
		Result<String> result = new Result<String>();
		if(list == null || list.size() < 1){
			result.setSuccessMessage("当前监听路径可用");
		}else{
			result.setErrorMessage("当前监听路径不可用",ErrorCodeNo.SYS009);
		}
		return result;
	}
	
	private String checkListenPathFormat(String listenPath) {
		if(!listenPath.startsWith("/")){
			listenPath = "/"+listenPath;
		}
//		if(!listenPath.endsWith("/")){
//			listenPath = listenPath+"/";
//		}
		return listenPath;
	}
}
