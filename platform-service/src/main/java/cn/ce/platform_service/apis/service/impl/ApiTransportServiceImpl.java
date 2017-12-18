package cn.ce.platform_service.apis.service.impl;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import cn.ce.platform_service.apis.dao.IDApiRecordDao;
import cn.ce.platform_service.apis.dao.INewApiDao;
import cn.ce.platform_service.apis.dao.IUApiRecordDao;
import cn.ce.platform_service.apis.entity.ApiEntity;
import cn.ce.platform_service.apis.entity.ApiExportParamEntity;
import cn.ce.platform_service.apis.entity.DApiRecordEntity;
import cn.ce.platform_service.apis.entity.UApiRecordEntity;
import cn.ce.platform_service.apis.entity.UApiRecordList;
import cn.ce.platform_service.apis.service.IApiTransportService;
import cn.ce.platform_service.common.AuditConstants;
import cn.ce.platform_service.common.DBFieldsConstants;
import cn.ce.platform_service.common.ErrorCodeNo;
import cn.ce.platform_service.common.Result;
import cn.ce.platform_service.common.Status;
import cn.ce.platform_service.common.gateway.ApiCallUtils;
import cn.ce.platform_service.diyApply.entity.appsEntity.AppList;
import cn.ce.platform_service.users.entity.User;
import cn.ce.platform_service.util.PropertiesUtil;
import io.netty.handler.codec.http.HttpMethod;

/**
* @Description : api导入导出service
* @Author : makangwei
* @Date : 2017年12月14日
*/
@Service(value="apiTransportService")
public class ApiTransportServiceImpl implements IApiTransportService{
	
	@Resource
	private INewApiDao newApiDao;
	@Resource
	private IDApiRecordDao dApiRecordDao;
	@Resource
	private IUApiRecordDao uApiRecordDao;
	
	private static Logger _LOGGER = LoggerFactory.getLogger(ApiTransportServiceImpl.class);
	@Override
	public Result<?> generalExportList(ApiExportParamEntity exportParam, User user) {

		List<ApiEntity> apiList = null;
		if(exportParam.getAllFlag() != null && exportParam.getAllFlag() == 1){//导出全部
			apiList = newApiDao.findApiByCheckState(AuditConstants.API_CHECK_STATE_SUCCESS);
		}else{
			apiList = newApiDao.findByIdsOrAppIds(exportParam.getApiIds(), exportParam.getAppIds());
		}

		List<String> apiIds = new ArrayList<String>();
		if (apiList.size() > 0) {
			// 往数据库中保存记录
			for (ApiEntity apiEntity : apiList) {
				apiIds.add(apiEntity.getId());
			}

			DApiRecordEntity recordEntity = new DApiRecordEntity(apiIds, new Date(), apiList.size(), user.getUserName(),
					user.getId());

			dApiRecordDao.save(recordEntity);

			return Result.errorResult("", ErrorCodeNo.SYS000, recordEntity.getId(), Status.SUCCESS);

		} else {
			return Result.errorResult("", ErrorCodeNo.SYS008, "", Status.FAILED);
		}
	}

	@Override
	public String exportApis(String recordId, HttpServletResponse response) {

		if (StringUtils.isBlank(recordId)) {
			return returnErrorJson("", ErrorCodeNo.SYS005, response);
		}

		List<ApiEntity> successApiList = new ArrayList<ApiEntity>();
		String url = PropertiesUtil.getInstance().getValue("findAppsByIds");
		DApiRecordEntity recordEntity = dApiRecordDao.findById(recordId);

		if (recordEntity == null) {
			return returnErrorJson("", ErrorCodeNo.SYS006, response);
		}

		List<String> apiIds = recordEntity.getApiIds();
		List<ApiEntity> apiList = newApiDao.findApiByIds(apiIds, AuditConstants.API_CHECK_STATE_SUCCESS);
		for (ApiEntity apiEntity : apiList) {
			// 获取产品中心applist
			String tempUrl = null;
			try {
				tempUrl = url + "?appIds=" + URLEncoder.encode("[" + apiEntity.getOpenApplyId() + "]", "utf-8");
			} catch (UnsupportedEncodingException e1) {
			}
			String resultStr = ApiCallUtils.getOrDelMethod(tempUrl, null, HttpMethod.GET);

			AppList appList = null;
			try {
				com.alibaba.fastjson.JSONArray jsonArray = com.alibaba.fastjson.JSONArray.parseObject(resultStr)
						.getJSONArray("data");

				appList = jsonArray.getJSONObject(0).toJavaObject(AppList.class);
			} catch (Exception e) {// 如果从产品中心拿不到开放应用
				_LOGGER.info("获取开放应用失败：" + apiEntity.getOpenApplyId());
				return returnErrorJson("开放应用(id:" + apiEntity.getOpenApplyId() + ")不存在", ErrorCodeNo.DOWNLOAD002,
						response);
			}

			// 封装：文档参数和api记录列表
			apiEntity.setAppCode(appList.getAppCode());
			apiEntity.setId(null);
			apiEntity.setUserId(null);
			apiEntity.setUserName(null);
			successApiList.add(apiEntity);
			
		}
		// TODO 20171211 mkw 这里的操作人是admin写死的。将来用户模块抽离出来的时候，这里再修改绑定用户id和用户名等

		return returnSuccessFile(successApiList, response);
	}
	
	@Override
	public Result<?> importApis(String upStr, User user) {

		List<ApiEntity> apiEntityList = null;
		try{
			apiEntityList = com.alibaba.fastjson.JSONArray.parseArray(upStr, ApiEntity.class);// 将文档中的api集合导出
		}catch(Exception e){
			return Result.errorResult("文件内容读取错误", ErrorCodeNo.UPLOAD001, null, Status.FAILED);
		}
		List<String> successApiIds = new ArrayList<String>(); // 用于发生错误时回滚数据
		List<UApiRecordList> records = new ArrayList<UApiRecordList>(); // 用户记录操作日志
		int successNum = 0;
		for (ApiEntity entity : apiEntityList) {
			String importLog = null; //用于记录操作详情，记录到操作日志中
			boolean flag = true;
			AppList appList = validateApiAndApp(entity, user);// 校验并获取appList
			
			if (appList == null || appList.getAppCode() == null || appList.getAppId() == null) {
				// roback
				_LOGGER.info("导入失败。文档格式不正确，回滚数据");
				newApiDao.deleteApis(successApiIds);
				_LOGGER.info("回滚数据成功");
				return Result.errorResult("", ErrorCodeNo.UPLOAD001, null, Status.FAILED);
			}
			
			/**校验listenPath*/
			List<ApiEntity> checkApiList = newApiDao.findByField(DBFieldsConstants.APIS_LISTEN_PATH,
					entity.getListenPath());
			if(checkApiList != null && checkApiList.size() > 0){
				if(entity.getApiVersion().getVersionId().equals(checkApiList.get(0).getApiVersion().getVersionId())){
					for (ApiEntity apiEntity : checkApiList) {
						String version = apiEntity.getApiVersion().getVersion();
						if(version.equals(entity.getApiVersion().getVersion())){
							flag=false;
							importLog = "版本号重复";
							break;
						}
					}
				}else{
					flag = false;
					importLog="listenPath重复.";
				}
			}
			
			if (flag) {// 当前监听路径不存在
				entity.setId(null);
				entity.setCheckState(1);
				entity.setCreateTime(new Date());
				entity.setAppCode(appList.getAppCode());
				entity.setOpenApplyId(appList.getAppId());
				entity.setUserName(user.getUserName());
				entity.setUserId(user.getId());
				entity.setApiSource(1);
				entity.setEnterpriseName(user.getEnterpriseName());
				newApiDao.save(entity);
				successApiIds.add(entity.getId());
				successNum++;
			} 

			records.add(new UApiRecordList(entity.getId(), entity.getApiChName(), entity.getListenPath(),
					entity.getApiType(), entity.getOpenApplyId(), entity.getAppCode(), appList.getAppName(),
					entity.getApiVersion(), flag ,importLog));
			
		}

		// 全部导出成功 后记录到日志中
		UApiRecordEntity recordEntity = new UApiRecordEntity(records, new Date(), apiEntityList.size(), successNum,
				user.getUserName(), user.getId());
		uApiRecordDao.save(recordEntity);
		return Result.errorResult("导入成功", ErrorCodeNo.SYS000, recordEntity, Status.SUCCESS);
	}
	
	private AppList validateApiAndApp(ApiEntity apiEntity, User user) {

		if (StringUtils.isBlank(apiEntity.getApiChName())) {// 接口名称不能为空
			_LOGGER.info("接口名称不能为空");
			return null;
		}
		if (StringUtils.isBlank(apiEntity.getAppCode())) {
			_LOGGER.info("接口所属开放应用分组不能为空");
			return null;
		}
		if (StringUtils.isBlank(apiEntity.getListenPath())) {
			_LOGGER.info("监听路径不能为空");
			return null;
		}
		if (apiEntity.getApiVersion() == null) {
			_LOGGER.info("版本不能为空");
			return null;
		} else {
			if (StringUtils.isBlank(apiEntity.getApiVersion().getVersion())) {
				_LOGGER.info("版本信息中的版本号不能为空");
				return null;
			}
			if (StringUtils.isBlank(apiEntity.getApiVersion().getVersionId())) {
				_LOGGER.info("版本信息中的版本id不能为空");
				return null;
			}
		}
		if (apiEntity.getApiType() == null) {
			_LOGGER.info("apiType不能为空");
			return null;
		}

		String url = PropertiesUtil.getInstance().getValue("findAppsByCodes");
		String tempUrl = null;
		try {
			tempUrl = url + "?codes=" + URLEncoder.encode("[\"" + apiEntity.getAppCode() + "\"]", "utf-8");
		} catch (UnsupportedEncodingException e1) {
		}
		String resultStr = ApiCallUtils.getOrDelMethod(tempUrl, null, HttpMethod.GET);

		AppList appList = null;
		try {
			com.alibaba.fastjson.JSONArray jsonArray = com.alibaba.fastjson.JSONArray.parseObject(resultStr)
					.getJSONArray("data");

			appList = jsonArray.getJSONObject(0).toJavaObject(AppList.class);
		} catch (Exception e) {// 如果从产品中心拿不到开放应用
			_LOGGER.info("获取开放应用失败：" + apiEntity.getOpenApplyId());
			return null;
		}
		if (appList != null) {
			_LOGGER.info("获取appList成功：" + appList);
			return appList;
		}
		_LOGGER.info("获取appList失败");
		return null;
	}
	
	private String returnSuccessFile(List<ApiEntity> successApiList, HttpServletResponse response) {
		response.setCharacterEncoding("utf-8");
		response.setContentType("multipart/form-data");
		String dateStr = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
		String fileName = "copy_" + dateStr + ".json";
		response.setHeader("Content-Disposition", "attachment; filename=" + fileName);
		try {
			response.getOutputStream()
					.write(com.alibaba.fastjson.JSONObject.toJSONString(successApiList).getBytes("utf-8"));
			response.getOutputStream().flush();
			response.getOutputStream().close();
		} catch (IOException e) {
			_LOGGER.info("返回下载文件时发生异常", e);
		}
		return null;
	}

	private String returnErrorJson(String errorMessage, ErrorCodeNo errorCode, HttpServletResponse response) {
		response.setCharacterEncoding("utf-8");
		response.setContentType("application/json");
		try {
			response.getOutputStream().write(com.alibaba.fastjson.JSONObject
					.toJSONString(Result.errorResult(errorMessage, errorCode, null, Status.FAILED)).getBytes("utf-8"));
			response.getOutputStream().flush();
			response.getOutputStream().close();
		} catch (IOException e) {
			_LOGGER.info("回显错误信息发生异常", e);
		}
		return null;
	}

	@Override
	public Result<?> getExportRecords(Integer checkCurrentPage, Integer checkPageSize) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Result<?> getExportRecordById(String recordId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Result<?> getImportRecords(Integer checkCurrentPage, Integer checkPageSize) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Result<?> getImportRecordById(String recordId) {
		// TODO Auto-generated method stub
		return null;
	}

}
