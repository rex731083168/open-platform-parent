package cn.ce.platform_service.apis.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
import cn.ce.platform_service.apis.entity.ApiVersion;
import cn.ce.platform_service.apis.entity.QueryApiEntity;
import cn.ce.platform_service.apis.service.IConsoleApiService;
import cn.ce.platform_service.common.AuditConstants;
import cn.ce.platform_service.common.DBFieldsConstants;
import cn.ce.platform_service.common.ErrorCodeNo;
import cn.ce.platform_service.common.Result;
import cn.ce.platform_service.common.gateway.GatewayUtils;
import cn.ce.platform_service.common.page.Page;
import cn.ce.platform_service.gateway.entity.GatewayColonyEntity;
import cn.ce.platform_service.openApply.entity.OpenApplyEntity;
import cn.ce.platform_service.openApply.service.IManageOpenApplyService;
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
	private IManageOpenApplyService manageOpenApplyService;
	
	
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
		
		// 第一次添加接口,并且选择未开启版本控制
		if (apiEntity.getApiVersion() == null || 
				StringUtils.isBlank(apiEntity.getApiVersion().getVersion())) {
			//api添加者信息和添加时间
			apiEntity.setUserId(user.getId());
			apiEntity.setUserName(user.getUserName());
			apiEntity.setCreateTime(new Date());
			
			//未开启版本控制的version信息
			ApiVersion version = new ApiVersion();
			version.setVersionId(apiEntity.getId());
			apiEntity.setApiVersion(version);
			
			// 过滤apienname不能以/开头和结尾
			apiEntity.setApiEnName(apiEntity.getApiEnName().replaceAll("/", ""));

			newApiDao.save(apiEntity);

		} else {
			
			// 开启版本控制
			//apiEntity.setUserId(user.getId());
			//apiEntity.setUserName(user.getUserName());
			apiEntity.setCreateTime(new Date());
			
			int num = newApiDao.updApiVersionByApiId(apiEntity.getApiVersion().getVersionId(),false);
			_LOGGER.info("----->将原来其他版本的api的newVersion字段全部修改为false，一共修改了"+num+"条数据");
			
			// TODO 前端传入版本号和newVersion字段吗？
			apiEntity.getApiVersion().setNewVersion(true);
			//如果前端没有传入版本的版本的apiId则重新生成版本versionId
			if(StringUtils.isBlank(apiEntity.getApiVersion().getVersionId())){
				// TODO versionId是java生成
				String versionId = UUID.randomUUID().toString().replace("-", "");
				apiEntity.getApiVersion().setVersionId(versionId);
			}
			
			newApiDao.save(apiEntity);
			
			_LOGGER.info("------新添加的数据为："+apiEntity.toString());
		}
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
			api.setCheckState(1);
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
		}else if (apiEntity.getCheckState() == AuditConstants.API_CHECK_STATE_SUCCESS){
			result.setErrorMessage("当前状态不可修改", ErrorCodeNo.SYS012);
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
		String openApplyId = api.getOpenApplyId();
		OpenApplyEntity openApplyEntity = manageOpenApplyService.findById(openApplyId);
		List<GatewayColonyEntity> colList = GatewayUtils.getAllGatewayColony();
		List<String> gatewayUrlList = new ArrayList<String>();
		for (GatewayColonyEntity gatewayColonyEntity : colList) {
			// TODO 这里的路径是否正确。网关是否修改这里
			gatewayUrlList.add(gatewayColonyEntity.getColUrl() +"/"+openApplyEntity.getApplyKey()+"/" + api.getApiEnName()+"/"+api.getApiVersion().getVersion()+"/");
		}
		
		JSONObject jsonObject = JSONObject.parseObject(JSON.toJSONString(api));
		jsonObject.put("gatewayUrls", gatewayUrlList);
		jsonObject.put("applyName", openApplyEntity.getApplyName());
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
		
		//隐藏真实地址
//		List<ApiEntity> list = page.getItems();
//		for (ApiEntity apiEntity : list) {
//			//将正式的url地址设置为空，不能让用户看到
//			apiEntity.setTestEndPoint("");
//		}
			
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
	public Result<String> checkVersion(String apiId, String version) {
		Result<String> result = new Result<String>();
		
		Map<String,Object> map =new HashMap<String,Object>();
		map.put(DBFieldsConstants.APIS_VERSION, version);
		map.put(DBFieldsConstants.APIS_ID, apiId);
		ApiEntity entity = newApiDao.findOneByFields(map);

		if(entity == null){
			result.setSuccessMessage("当前version不存在，可以使用");
		}else{
			result.setErrorMessage("当前version已经存在",ErrorCodeNo.SYS010);
		}
		return result;
	}
	
}
