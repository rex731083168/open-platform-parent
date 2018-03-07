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
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import cn.ce.platform_service.apis.dao.IMysqlApiDao;
import cn.ce.platform_service.apis.dao.IMysqlDApiRecordDao;
import cn.ce.platform_service.apis.dao.IMysqlUApiRecordDao;
import cn.ce.platform_service.apis.entity.ApiExportParamEntity;
import cn.ce.platform_service.apis.entity.DApiRecordEntity;
import cn.ce.platform_service.apis.entity.NewApiEntity;
import cn.ce.platform_service.apis.entity.UApiRecordEntity;
import cn.ce.platform_service.apis.entity.UApiRecordList;
import cn.ce.platform_service.apis.service.IApiTransportService;
import cn.ce.platform_service.common.AuditConstants;
import cn.ce.platform_service.common.ErrorCodeNo;
import cn.ce.platform_service.common.Result;
import cn.ce.platform_service.common.Status;
import cn.ce.platform_service.common.gateway.ApiCallUtils;
import cn.ce.platform_service.diyApply.entity.appsEntity.AppList;
import cn.ce.platform_service.users.entity.User;
import cn.ce.platform_service.util.PropertiesUtil;
import cn.ce.platform_service.util.RandomUtil;
import io.netty.handler.codec.http.HttpMethod;

/**
* @Description : api导入导出service
* @Author : makangwei
* @Date : 2017年12月14日
*/
@Service(value="apiTransportService")
@Transactional(propagation=Propagation.REQUIRED)
public class ApiTransportServiceImpl implements IApiTransportService{
	
//	@Resource
//	private INewApiDao newApiDao;
	@Resource
	private IMysqlApiDao mysqlApiDao;
//	@Resource
//	private IDApiRecordDao dApiRecordDao;
	@Resource
	private IMysqlDApiRecordDao mysqlDapiRecord;
//	@Resource
//	private IUApiRecordDao uApiRecordDao;
	@Resource
	private IMysqlUApiRecordDao mysqlUApiRecord;
	
	private static Logger _LOGGER = LoggerFactory.getLogger(ApiTransportServiceImpl.class);
	@Override
	public Result<?> generalExportList(ApiExportParamEntity exportParam, User user) {

//		List<ApiEntity> apiList = null;
		List<String> exApiIds = null;
		if(exportParam.getAllFlag() != null && exportParam.getAllFlag() == 1){//导出全部
//			apiList = newApiDao.findApiByCheckState(AuditConstants.API_CHECK_STATE_SUCCESS);
			exApiIds = mysqlApiDao.findIdByCheckState(AuditConstants.API_CHECK_STATE_SUCCESS);
		}else{
//			apiList = newApiDao.findByIdsOrAppIds(exportParam.getApiIds(), exportParam.getAppIds());
			exApiIds = mysqlApiDao.findIdByIdsOrOpenApplys(exportParam.getApiIds(), exportParam.getAppIds()
					,AuditConstants.API_CHECK_STATE_SUCCESS);
		}

//		List<String> apiIds = new ArrayList<String>();
		if (null !=exApiIds && exApiIds.size() > 0) {
//			// 往数据库中保存记录
//			for (ApiEntity apiEntity : apiList) {
//				apiIds.add(apiEntity.getId());
//			}

			DApiRecordEntity recordEntity = new DApiRecordEntity(new Date(), exApiIds.size(), user.getUserName(),
					user.getId());
			
			recordEntity.setId(RandomUtil.random32UUID());
			mysqlDapiRecord.save(recordEntity);
			mysqlDapiRecord.saveBoundApis(recordEntity.getId(), exApiIds);

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

		List<NewApiEntity> successApiList = new ArrayList<NewApiEntity>();
		String url = PropertiesUtil.getInstance().getValue("findAppsByIds");
		DApiRecordEntity recordEntity = mysqlDapiRecord.findTotalOneById(recordId);

		if (recordEntity == null) {
			return returnErrorJson("", ErrorCodeNo.SYS006, response);
		}

		List<String> apiIds = recordEntity.getApiIds();
//		List<ApiEntity> apiList = newApiDao.findApiByIds(apiIds, AuditConstants.API_CHECK_STATE_SUCCESS);
		List<NewApiEntity> apiList = mysqlApiDao.findTotalOnesByIdsAndCheckState(apiIds, AuditConstants.API_CHECK_STATE_SUCCESS);
		for (NewApiEntity apiEntity : apiList) {
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

		List<NewApiEntity> apiEntityList = null;
		try{
			apiEntityList = com.alibaba.fastjson.JSONArray.parseArray(upStr, NewApiEntity.class);// 将文档中的api集合导出
		}catch(Exception e){
			_LOGGER.info("文件导入时json数据解析错误。");
			return Result.errorResult("文件内容读取错误", ErrorCodeNo.UPLOAD001, null, Status.FAILED);
		}
		List<String> successApiIds = new ArrayList<String>(); // 用于发生错误时回滚数据
		List<UApiRecordList> records = new ArrayList<UApiRecordList>(); // 用户记录操作日志
		int successNum = 0;
		for (NewApiEntity entity : apiEntityList) {
			String importLog = null; //用于记录操作详情，记录到操作日志中
			boolean flag = true;
			AppList appList = validateApiAndApp(entity, user);// 校验并获取appList
			
			if (null==appList || null == appList.getAppCode() || null == appList.getAppId() ) {
				// roback
				importLog= "文档解析错误";
				flag = false;
			}else{
				/**校验listenPath*/
//			List<ApiEntity> checkApiList = newApiDao.findByField(DBFieldsConstants.APIS_LISTEN_PATH,
//					entity.getListenPath());
				List<NewApiEntity> checkApiList = mysqlApiDao.findByListenPath(entity.getListenPath());
				if(null != checkApiList && checkApiList.size() > 0){
					if(entity.getVersionId().equals(checkApiList.get(0).getVersionId())){
						for (NewApiEntity apiEntity : checkApiList) {
							String version = apiEntity.getVersion();
							if(version.equals(entity.getVersion())){
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
				
				if (flag) {// 当前监听路径可以使用
					entity.setId(null);
					entity.setCheckState(1);
					entity.setCreateTime(new Date());
					entity.setAppCode(appList.getAppCode());
					entity.setOpenApplyId(appList.getAppId());
					entity.setUserName(user.getUserName());
					entity.setUserId(user.getId());
					entity.setApiSource(1);
					entity.setEnterpriseName(user.getEnterpriseName());
//				newApiDao.save(entity);
					entity.setId(RandomUtil.random32UUID());
					mysqlApiDao.save1(entity);
					successApiIds.add(entity.getId());
					successNum++;
				} 
			}
			
			records.add(new UApiRecordList(entity.getId(), 
					entity.getApiChName(), 
					entity.getListenPath(),
					entity.getApiType(), 
					entity.getOpenApplyId(), 
					entity.getAppCode(), 
					null == appList? null : appList.getAppName(),
					entity.getVersionId(), 
					entity.getVersion(), 
					flag ,
					importLog));
			
		}

		// 全部导出成功 后记录到日志中
		UApiRecordEntity recordEntity = new UApiRecordEntity(records, new Date(), apiEntityList.size(), successNum,
				user.getUserName(), user.getId());
		
//		uApiRecordDao.save(recordEntity);
		recordEntity.setId(RandomUtil.random32UUID());
		mysqlUApiRecord.save(recordEntity);
		mysqlUApiRecord.saveBoundApi(recordEntity.getId(),records);
		return Result.errorResult("导入成功", ErrorCodeNo.SYS000, recordEntity, Status.SUCCESS);
	}
	
	private AppList validateApiAndApp(NewApiEntity apiEntity, User user) {

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
		if (StringUtils.isBlank(apiEntity.getVersion())) {
			_LOGGER.info("版本信息中的版本号不能为空");
			return null;
		}
		if (StringUtils.isBlank(apiEntity.getVersionId())) {
			_LOGGER.info("版本信息中的版本id不能为空");
			return null;
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
	
	private String returnSuccessFile(List<NewApiEntity> successApiList, HttpServletResponse response) {
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
