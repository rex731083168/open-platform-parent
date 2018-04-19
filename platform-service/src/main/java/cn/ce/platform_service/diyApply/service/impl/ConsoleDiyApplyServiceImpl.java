package cn.ce.platform_service.diyApply.service.impl;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.regex.Pattern;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMethod;

import com.alibaba.fastjson.JSON;

import cn.ce.platform_service.apis.dao.IMysqlApiDao;
import cn.ce.platform_service.apis.entity.NewApiEntity;
import cn.ce.platform_service.apis.service.IConsoleApiService;
import cn.ce.platform_service.common.AuditConstants;
import cn.ce.platform_service.common.DBFieldsConstants;
import cn.ce.platform_service.common.ErrorCodeNo;
import cn.ce.platform_service.common.HttpClientUtil;
import cn.ce.platform_service.common.HttpClientUtilsNew;
import cn.ce.platform_service.common.RateConstants;
import cn.ce.platform_service.common.RateEnum;
import cn.ce.platform_service.common.Result;
import cn.ce.platform_service.common.Status;
import cn.ce.platform_service.common.gateway.ApiCallUtils;
import cn.ce.platform_service.common.gateway.GatewayRouteUtils;
import cn.ce.platform_service.common.page.Page;
import cn.ce.platform_service.diyApply.dao.IDiyApplyDao;
import cn.ce.platform_service.diyApply.dao.IMysqlDiyApplyDao;
import cn.ce.platform_service.diyApply.entity.DiyApplyEntity;
import cn.ce.platform_service.diyApply.entity.Menu;
import cn.ce.platform_service.diyApply.entity.QueryDiyApplyEntity;
import cn.ce.platform_service.diyApply.entity.RetMenu;
import cn.ce.platform_service.diyApply.entity.interfaceMessageInfo.InterfaMessageInfoString;
import cn.ce.platform_service.diyApply.entity.tenantAppsEntity.AppList;
import cn.ce.platform_service.diyApply.entity.tenantAppsEntity.TenantApps;
import cn.ce.platform_service.diyApply.service.IConsoleDiyApplyService;
import cn.ce.platform_service.diyApply.service.IPlublicDiyApplyService;
import cn.ce.platform_service.util.PropertiesUtil;
import cn.ce.platform_service.util.RandomUtil;
import cn.ce.platform_service.util.SplitUtil;
import io.netty.handler.codec.http.HttpMethod;
import net.sf.json.JSONObject;

/***
 * 
 * 应用服务接口实现类
 * 
 * @author lida
 * @date 2017年8月23日14:32:48
 *
 */
@Service("consoleDiyApplyService")
@Transactional(propagation=Propagation.REQUIRED)
public class ConsoleDiyApplyServiceImpl implements IConsoleDiyApplyService {

	/** 日志对象 */
	private static Logger _LOGGER = Logger.getLogger(ConsoleDiyApplyServiceImpl.class);
	
//	@Resource
//	private INewApiDao newApiDao;
	@Resource
	private IMysqlApiDao mysqlApiDao;
	@Resource
	private IDiyApplyDao diyApplyDao;
	@Resource
	private IMysqlDiyApplyDao mysqlDiyApplyDao;
	@Resource
	private IConsoleApiService consoleApiService;
	@Resource
	private IPlublicDiyApplyService plublicDiyApplyService;

	@Override
	public Result<?> saveApply(DiyApplyEntity entity) {
		Result<String> result = new Result<>();

		int applyNum = mysqlDiyApplyDao.checkApplyName(entity.getUser().getId(),entity.getApplyName());
		
//		if (null != findPageByList && findPageByList.size() > 0) {
		if(applyNum > 0){
			result.setErrorMessage("应用名称不可重复!", ErrorCodeNo.SYS010);
			return result;
		}

		// 新增
		if (StringUtils.isBlank(entity.getId())) {
			entity.setUserId(entity.getUser().getId());
			entity.setUserName(entity.getUser().getUserName());
			entity.setEnterpriseName(entity.getUser().getEnterpriseName());
			entity.setCreateDate(new Date());
			entity.setCheckState(AuditConstants.DIY_APPLY_UNCHECKED);

			String key = entity.getProductAuthCode();
			String findTenantAppsByTenantKeyTenanName = null;

			// 产品信息
			TenantApps apps = new TenantApps();
			try {
				_LOGGER.info("get message from interface satar");
				apps = plublicDiyApplyService.findTenantAppsByTenantKey(key).getData(); // 接入产品中心获取产品信息和开放应用信息
				if (apps == null || !apps.getStatus().equals("200")) {
					return Result.errorResult("产品码错误", ErrorCodeNo.SYS024, null, Status.FAILED);
				}
				_LOGGER.info("get message from interface states " + apps.getStatus() + "");

				if (apps.getStatus() == String.valueOf(AuditConstants.INTERFACE_RETURNSATAS_FAILE)
						|| apps.getData().getTenant() == null) {
					result.setErrorMessage("产品码不可用!", ErrorCodeNo.SYS024);
					return result;
				}
				for (int i = 0; i < apps.getData().getAppList().size(); i++) {
					if (apps.getData().getAppList().get(i).getAppName() != null
							&& apps.getData().getAppList().get(i).getAppName().equals(entity.getApplyName())) {
						result.setErrorMessage("应用名称不可重复!", ErrorCodeNo.SYS010);
						return result;
					}
				}

				String findTenantAppsByTenantKeyTenantIdtemp = apps.getData().getTenant().getId();

				if (null == findTenantAppsByTenantKeyTenantIdtemp) {
					result.setErrorMessage("网站实例tenantId不存在!");
					result.setErrorCode(ErrorCodeNo.SYS015);
					return result;
				}

				// findTenantAppsByTenantKeyTenantId =
				// String.valueOf(findTenantAppsByTenantKeyTenantIdtemp);
				findTenantAppsByTenantKeyTenanName = apps.getData().getTenant().getName();

				entity.setProductInstanceId(findTenantAppsByTenantKeyTenantIdtemp);
				entity.setProductName(findTenantAppsByTenantKeyTenanName);

				_LOGGER.info(">>>>>>>>>>>>>>>>>>> tenantId:" + findTenantAppsByTenantKeyTenantIdtemp);

				// TODO {"status":"error","message":"saasId or resourceType not found"}
				// 根据网站实例id查询是否配置资源IP
				String routeBySaasId = GatewayRouteUtils.getRouteBySaasId(findTenantAppsByTenantKeyTenantIdtemp,
						entity.getResourceType(), RequestMethod.GET.toString());
				// TODO mkw2010411 http://10.12.40.161:8000/tyk/router/saas/1600002358/null
				//{"status":"error","message":"saasKey not found"}
				if (StringUtils.isBlank(routeBySaasId)) {
					result.setErrorMessage("未配置定制应用资源池,请联系管理员!");
					result.setErrorCode(ErrorCodeNo.SYS015);
					return result;
				}

				_LOGGER.info(">>>>>>>>>>>>>>>>>>> tenantIdBindIp:" + routeBySaasId);

			} catch (Exception e) {
				_LOGGER.error("get messaget from url faile resaon " + e.getMessage() + "");
			}

			if (StringUtils.isNotBlank(apps.getData().getTenant().getProductInstance().getBossProductInstance())) {
				entity.setBossProductInstance(apps.getData().getTenant().getProductInstance().getBossProductInstance());
			} else {
				result.setErrorMessage("该产品为旧版产品,无法发布菜单", ErrorCodeNo.SYS025);
				return result;
			}

			/***************************************************************
			 * 
			 * TODO 定制应用和api以及频次绑定操作
			 * 将当前定制应用绑定的开放应用下的所有api推送到网关，并且给当前定制应用绑定频次限制设定
			 * 只在当前位置做了绑定关系，如果将来绑定关系和绑定位置发生变化需要修改这段代码
			 *************************************************************/

			_LOGGER.info("/********************创建定制应用绑定频次，推送网关开始***************************/");
			String policyId = UUID.randomUUID().toString().replaceAll("\\-", "");
			String clientId = UUID.randomUUID().toString().replaceAll("\\-", "");
			String secret = UUID.randomUUID().toString().replaceAll("\\-", "");

			_LOGGER.info("**********调用保存appkey与tenantId开始********* appkey:" + clientId + ";tenantId:"
					+ entity.getProductInstanceId());
			String saveAppkeyResult = GatewayRouteUtils.saveAppkey(clientId, entity.getProductInstanceId(),
					HttpMethod.POST.toString());
			_LOGGER.info("**********调用保存appkey与tenantId结束 返回值:*********" + saveAppkeyResult);

			List<String> appIdList = new ArrayList<String>();
			for (AppList appList : apps.getData().getAppList()) {
				appIdList.add(appList.getAppId() + ""); // TODO
														// 这里绑定的是appId这个属性，添加api的时候绑定的开放应用的id也应该为appId
			}

			_LOGGER.info("insert apply begin : " + JSON.toJSONString(entity));

			// 绑定频次，推送网关
			Integer frequencyType = entity.getFrequencyType();
			boolean flag = false;
			if (frequencyType == RateEnum.RATE1.toValue()) {
				flag = consoleApiService.boundDiyApplyWithApi(policyId, clientId, secret, RateConstants.TYPE_1_RATE,
						RateConstants.TYPE_1_PER, RateConstants.TYPE_1_QUOTA_MAX, RateConstants.TYPE_1_QUOTA_RENEW_RATE,
						appIdList);
				entity.setPer(RateConstants.TYPE_1_PER);
				entity.setRate(RateConstants.TYPE_1_RATE);
				entity.setQuotaMax(RateConstants.TYPE_1_QUOTA_MAX);
				entity.setQuotaRenewRate(RateConstants.TYPE_1_QUOTA_RENEW_RATE);
			} else if (frequencyType == RateEnum.RATE2.toValue()) {
				flag = consoleApiService.boundDiyApplyWithApi(policyId, clientId, secret, RateConstants.TYPE_2_RATE,
						RateConstants.TYPE_2_PER, RateConstants.TYPE_2_QUOTA_MAX, RateConstants.TYPE_1_QUOTA_RENEW_RATE,
						appIdList);
				entity.setPer(RateConstants.TYPE_2_PER);
				entity.setRate(RateConstants.TYPE_2_RATE);
				entity.setQuotaMax(RateConstants.TYPE_2_QUOTA_MAX);
				entity.setQuotaRenewRate(RateConstants.TYPE_2_QUOTA_RENEW_RATE);
			} else if (frequencyType == RateEnum.RATE3.toValue()) {
				flag = consoleApiService.boundDiyApplyWithApi(policyId, clientId, secret, RateConstants.TYPE_3_RATE,
						RateConstants.TYPE_3_PER, RateConstants.TYPE_3_QUOTA_MAX, RateConstants.TYPE_1_QUOTA_RENEW_RATE,
						appIdList);
				entity.setPer(RateConstants.TYPE_3_PER);
				entity.setRate(RateConstants.TYPE_3_RATE);
				entity.setQuotaMax(RateConstants.TYPE_3_QUOTA_MAX);
				entity.setQuotaRenewRate(RateConstants.TYPE_3_QUOTA_RENEW_RATE);
			} else {
				result.setErrorMessage("当前频次档位不存在", ErrorCodeNo.SYS012);
				return result;
			}

			if (flag == false) {
				_LOGGER.error("添加定制应用，推送网关发送异常");
				result.setErrorMessage("", ErrorCodeNo.SYS014);
				return result;
			}
			entity.setPolicyId(policyId);
			entity.setClientId(clientId);
			entity.setSecret(secret);
			entity.setId(RandomUtil.random32UUID());
			mysqlDiyApplyDao.save(entity);
			_LOGGER.info("****************将绑定关系保存到mysql其它表中****************");
			int i = 1;
			for (String appId : appIdList) {
				List<NewApiEntity> apiList = mysqlApiDao.findByOpenApply(appId);
				if (apiList.size() > 0) {
					String boundOpenId = RandomUtil.random32UUID();
					mysqlDiyApplyDao.saveBoundOpenApply(boundOpenId, entity.getId(), appId);
					_LOGGER.info("当前定制应用和第" + i++ + "个开放应用" + appId + "下绑定的api个数是：" + apiList.size());
					for (NewApiEntity apiEntity : apiList) {
						// version2.1改为只有api类型为开放的才绑定
						if (DBFieldsConstants.API_TYPE_OPEN.equals(apiEntity.getApiType())
								&& apiEntity.getCheckState() == AuditConstants.API_CHECK_STATE_SUCCESS) {
//							apiIds.add(apiEntity.getId());
							mysqlDiyApplyDao.saveBoundApi(RandomUtil.random32UUID(), entity.getId(), appId, apiEntity.getId(),boundOpenId);
						}
					}
				}
			}
			_LOGGER.info("****************绑定关系实体保存完成****************");
			_LOGGER.info("/********************创建定制应用绑定频次，推送网关结束***************************/");
			_LOGGER.info("insert apply begin : " + JSON.toJSONString(entity));
			_LOGGER.info("save end");
			result.setSuccessMessage("新增成功!");

		} else {
			// 修改
//			DiyApplyEntity applyById = diyApplyDao.findById(entity.getId());
			DiyApplyEntity applyById = mysqlDiyApplyDao.findById(entity.getId());

			if (null == applyById) {
				result.setErrorMessage("请求的应用信息不存在!");
				return result;
			} else {

				if (entity.getCheckState().equals(AuditConstants.DIY_APPLY_CHECKED_SUCCESS)) {
					result.setErrorMessage("应用审核成功,无法修改记录!");
					return result;
				}
				if (StringUtils.isNotBlank(entity.getApplyName())) {
					applyById.setApplyName(entity.getApplyName());
				}
				if (StringUtils.isNotBlank(entity.getApplyDesc())) {
					applyById.setApplyDesc(entity.getApplyDesc());
				}
				if(StringUtils.isNotBlank(entity.getDomainUrl())){
					applyById.setDomainUrl(entity.getDomainUrl());
				}
			}

			_LOGGER.info("update apply begin : " + applyById);
			mysqlDiyApplyDao.update(applyById);
			_LOGGER.info("save end");
			result.setSuccessMessage("修改成功!");

		}

		return result;
	}

	@Override
	public Result<?> updateApply(DiyApplyEntity apply) {
		Result<String> result = new Result<String>();
		try {
			_LOGGER.info("updateApply");
			if (StringUtils.isBlank(apply.getId())) {
				result.setErrorMessage("当前id不能为空", ErrorCodeNo.SYS005);
				return result;
			}
			//DiyApplyEntity apply1 = diyApplyDao.findById(apply.getId());
			DiyApplyEntity apply1 = mysqlDiyApplyDao.findById(apply.getId());
			if (null == apply1) {
				result.setErrorMessage("查询结果不存在", ErrorCodeNo.SYS015);
				return result;
			}
			if (!apply1.getProductAuthCode().equals(apply.getProductAuthCode())) {
				result.setErrorMessage("productAuthCode前后不一致", ErrorCodeNo.SYS016);
				return result;
			}
			
			if(!apply1.getApplyName().equals(apply.getApplyName())){
				//校验applyName
				int applyNum = mysqlDiyApplyDao.checkApplyName(apply.getUserId(),apply.getApplyName());
				if(applyNum > 0){
					result.setErrorMessage("应用名称不可重复!", ErrorCodeNo.SYS010);
					return result;
				}
				
			}
			mysqlDiyApplyDao.update(apply);
			_LOGGER.info("updateApply success");
			result.setSuccessMessage("修改成功");
			return result;
		} catch (Exception e) {
			// TODO: handle exception
			result.setErrorMessage("修改失败");
			result.setErrorCode(ErrorCodeNo.SYS001);
			_LOGGER.error("updateApply faile", e);
			return result;
		}
	}

	@Override
	public Result<String> deleteApplyByid(String id) {
		// TODO Auto-generated method stub
		Result<String> result = new Result<>();
//		DiyApplyEntity apply = diyApplyDao.findById(id);
		DiyApplyEntity apply = mysqlDiyApplyDao.findById(id);
		if (null == apply) {
			result.setErrorMessage("请求删除的应用不存在!");
			return result;
		} else if (apply.getCheckState().equals(AuditConstants.DIY_APPLY_CHECKED_SUCCESS)
				|| apply.getCheckState().equals(AuditConstants.DIY_APPLY_CHECKED_COMMITED)) {
			result.setErrorMessage("该应用已审核,无法删除!");
			return result;
		} else {
			_LOGGER.info("delete apply begin applyId:" + id);
			mysqlDiyApplyDao.deleteById(id);
			_LOGGER.info("delete apply end");
			result.setSuccessMessage("删除成功!");
			return result;
		}

	}

	@Override
	public Result<Page<DiyApplyEntity>> findApplyList(QueryDiyApplyEntity queryApply) {
		
		Result<Page<DiyApplyEntity>> result = new Result<Page<DiyApplyEntity>>();
		
		int totalNum = mysqlDiyApplyDao.findListSize(queryApply);
		List<DiyApplyEntity> diyList = mysqlDiyApplyDao.getPagedList(queryApply);
//		Page<DiyApplyEntity> diyApplyPage = diyApplyDao.findApplyList(entity.getApplyName(), entity.getProductName(),
//				entity.getCheckState(), entity.getUserId(), page);
		Page<DiyApplyEntity> page = new Page<DiyApplyEntity>(queryApply.getCurrentPage(), totalNum, queryApply.getPageSize());
		page.setItems(diyList);
		result.setSuccessData(page);
		return result;
	}

	@Override
	public Result<DiyApplyEntity> findById(String applyId) {
//		return diyApplyDao.findById(applyId);
		Result<DiyApplyEntity> result = new Result<>();
		DiyApplyEntity findById = mysqlDiyApplyDao.findById(applyId);
		if (null == findById) {
			result.setErrorMessage("应用不存在!");
		} else {
			result.setSuccessData(findById);
		}
		return result;
	}

	@Override
	public Result<InterfaMessageInfoString> generatorTenantKey(String id) {
		// TODO Auto-generated method stub
		Result<InterfaMessageInfoString> result = new Result<>();
		String url = PropertiesUtil.getInstance().getValue("generatorTenantKey");
		String id$ = Pattern.quote("${TenantKeyid}");
		String replacedurl = url.replaceAll(id$, id);

		try {
			/* get请求方法 */
			InterfaMessageInfoString messageInfo = (InterfaMessageInfoString) HttpClientUtil
					.getUrlReturnObject(replacedurl, InterfaMessageInfoString.class, null);

			if (messageInfo.getStatus() == 200 || messageInfo.getStatus() == 110) {
				result.setSuccessData(messageInfo);
				return result;
			} else {
				_LOGGER.error("generatorTenantKey data http getfaile return code :" + messageInfo.getMsg() + " ");
				result.setErrorCode(ErrorCodeNo.SYS006);
				result.setErrorMessage("请求失败");
				return result;
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			_LOGGER.error("generatorTenantKey http error " + e + "");
			result.setErrorCode(ErrorCodeNo.SYS001);
			result.setErrorMessage("系统错误，接口请求失败");
			return result;
		}
	}

	public Result<String> productMenuList(String bossInstanceCode) {
		Result<String> result = new Result<>();

		String productMenuListURL = PropertiesUtil.getInstance().getValue("productMenuList");

		if (StringUtils.isBlank(productMenuListURL)) {
			_LOGGER.error("productMenuListURL is null !");
			result.setErrorMessage("获取产品菜单列表错误,请联系管理员!");
			return result;
		}

		String reqURL = productMenuListURL.replace("{bossInstanceCode}", bossInstanceCode);

		_LOGGER.info("send productMenuList URL is " + reqURL);

		try {

			StringBuffer sendGetRequest = HttpClientUtil.sendGetRequest(reqURL, "UTF-8");

			_LOGGER.debug("produMenuList return json:" + sendGetRequest);

			JSONObject jsonObject = JSONObject.fromObject(sendGetRequest.toString());

			if (null != jsonObject && jsonObject.has("status") && jsonObject.has("msg") && jsonObject.has("data")) {

				String status = jsonObject.get("status") == null ? "" : jsonObject.get("status").toString();

				switch (status) {
				case "101":
					result.setStatus(Status.SUCCESS);

					result.setSuccessData(jsonObject.get("data").toString());

					break;
				case "201":
					result.setErrorCode(ErrorCodeNo.SYS029);
					result.setStatus(Status.FAILED);
					break;
				case "301":
					result.setErrorCode(ErrorCodeNo.SYS029);
					result.setStatus(Status.FAILED);
					break;
				default:
					result.setErrorCode(ErrorCodeNo.SYS029);
					result.setStatus(Status.SYSTEMERROR);
					break;
				}

				result.setMessage(jsonObject.get("msg").toString());

			} else {
				_LOGGER.error("获取产品菜单列表时,缺失返回值:" + jsonObject);

				result.setErrorMessage("获取产品菜单列表错误!");
			}

		} catch (Exception e) {
			_LOGGER.error("send productMenuList error e:" + e.toString());
			result.setErrorMessage("获取产品菜单列表错误!");
		}
		// TODO Auto-generated method stub
		return result;
	}

	@Override
	public Result<String> registerMenu(String appid, String bossInstanceCode, String menuJson) {

		String registerMenuURL = PropertiesUtil.getInstance().getValue("registerMenu");

		Result<String> result = new Result<>();
		if (StringUtils.isBlank(registerMenuURL)) {
			_LOGGER.error("registerMenuURL is null !");
			result.setErrorMessage("发布菜单错误,请联系管理员");
			return result;
		}
		Map<String, String> body = new HashMap<>();
		body.put("appid", appid);
		body.put("bossInstanceCode", bossInstanceCode);
		body.put("menuJson", menuJson);

		try {
			String sendPostByJson = HttpClientUtilsNew.getResponseString(registerMenuURL, body);

			_LOGGER.info("registerMenuURL return senPostByJson : " + sendPostByJson);

			JSONObject jsonObject = JSONObject.fromObject(sendPostByJson);

			if (null != jsonObject && jsonObject.has("status") && jsonObject.has("msg")) {

				String status = null == jsonObject.get("status") ? "" : jsonObject.get("status").toString();

				switch (status) {
				case "101":
					result.setStatus(Status.SUCCESS);
					result.setErrorCode(ErrorCodeNo.SYS000);
					break;
				case "201":
					result.setStatus(Status.FAILED);
					break;
				case "301":
					result.setStatus(Status.FAILED);
					break;
				default:
					result.setStatus(Status.SYSTEMERROR);
					break;
				}
				result.setMessage(jsonObject.get("msg").toString());
			} else {
				_LOGGER.error("发布菜单时,缺失返回值:" + jsonObject);

				result.setErrorMessage("发布菜单出现错误!");
			}
		} catch (Exception e) {
			_LOGGER.error("send registerMenuURL error,e:" + e.toString());
			result.setErrorMessage("发布菜单错误!");
		}
		return result;
	}

	@Override
	public Result<String> batchUpdateCheckState(String ids, Integer checkState, String checkMem) {
		Result<String> result = new Result<>();
		try {
//			String message = diyApplyDao.bathUpdateByid(SplitUtil.splitStringWithComma(ids), checkState, checkMem);
			int changeNum = mysqlDiyApplyDao.bathUpdateCheckState(SplitUtil.splitStringWithComma(ids), checkState, checkMem);
			_LOGGER.info("bachUpdate diyApply message " + changeNum + " count");
			result.setSuccessMessage("审核成功:" + changeNum + "条");
			return result;
		} catch (Exception e) {
			// TODO: handle exception
			_LOGGER.error("bachUpdate diyApply message error ", e);
			result.setErrorCode(ErrorCodeNo.SYS001);
			result.setErrorMessage("审核失败");
			return result;
		}
	}

	@Override
	public Result<?> migraDiyApply() {
		int i = 0;
		List<DiyApplyEntity> diyList = diyApplyDao.findAll();
		mysqlDiyApplyDao.deleteAll();
		for (DiyApplyEntity diyApplyEntity : diyList) {
			Map<String,List<String>> map = diyApplyEntity.getLimitList();
			for (String openId : map.keySet()) {
				String boundId = RandomUtil.random32UUID();
				mysqlDiyApplyDao.saveBoundOpenApply(boundId,diyApplyEntity.getId(),openId);
				List<String> apiIds = map.get(openId);
				for (String apiId : apiIds) {
					mysqlDiyApplyDao.saveBoundApi(RandomUtil.random32UUID()
							, diyApplyEntity.getId(), openId, apiId,boundId);
				}
			}
			i+=mysqlDiyApplyDao.save(diyApplyEntity);
			
		}
		Result<String> result = new Result<String>();
		result.setSuccessMessage("一共"+diyList.size()+"条数据，成功插入mysql数据库"+i+"条");
		return result;
	}

	@Override
	public Result<?> productMenuList1(String tenantId) {
		Result<List<RetMenu>> result = new Result<>();

		String productMenuListURL = PropertiesUtil.getInstance().getValue("productMenuList1");

		if (StringUtils.isBlank(productMenuListURL)) {
			_LOGGER.error("productMenuListURL is null !");
			result.setErrorMessage("获取产品菜单列表错误,请联系管理员!");
			return result;
		}

		String reqURL = productMenuListURL.replace("{tenantId}", tenantId);

		_LOGGER.info("send productMenuList URL is " + reqURL);

		try {

			StringBuffer sendGetRequest = HttpClientUtil.sendGetRequest(reqURL, "UTF-8");

			_LOGGER.debug("produMenuList return json:" + sendGetRequest);

			JSONObject jsonObject = JSONObject.fromObject(sendGetRequest.toString());

			if (null != jsonObject && jsonObject.has("status") && jsonObject.has("msg") && jsonObject.has("data")) {

				String status = jsonObject.get("status") == null ? "" : jsonObject.get("status").toString();

				switch (status) {
				case "200":
					result.setStatus(Status.SUCCESS);

					result.setSuccessData(com.alibaba.fastjson.JSONArray.parseArray((jsonObject.getString("data")), RetMenu.class));

					break;
				
				// TODO other status
//				case "201":
//					result.setErrorCode(ErrorCodeNo.SYS029);
//					result.setStatus(Status.FAILED);
//					break;
//				case "301":
//					result.setErrorCode(ErrorCodeNo.SYS029);
//					result.setStatus(Status.FAILED);
//					break;
				default:
					result.setErrorCode(ErrorCodeNo.SYS029);
					result.setStatus(Status.SYSTEMERROR);
					break;
				}

				result.setMessage(jsonObject.get("msg").toString());

			} else {
				_LOGGER.error("获取产品菜单列表时,缺失返回值:" + jsonObject);

				result.setErrorMessage("获取产品菜单列表错误!");
			}
			
		} catch (Exception e) {
			_LOGGER.error("send productMenuList error e:" + e.toString());
			result.setErrorMessage("获取产品菜单列表错误!");
		}
		// TODO Auto-generated method stub
		return result;
	}

	@Override
	public Result<?> registerMenu1(String tenantId, List<Menu> menus) {
		String registerMenuURL = PropertiesUtil.getInstance().getValue("registerMenu1");

		Result<String> result = new Result<>();
		if (StringUtils.isBlank(registerMenuURL)) {
			_LOGGER.error("registerMenuURL is null !");
			result.setErrorMessage("发布菜单错误,请联系管理员");
			return result;
		}
		
		//如果id不为空校验menus
		if(!validateMenu(menus)){
			result.setErrorMessage("发布菜单错误,请联系管理员",ErrorCodeNo.SYS032); 
			return result;
		}
		for (Menu menu : menus) {
			menu.setCode(RandomUtil.random32UUID());
			menu.setFrame(1);
		}
		
		String reqURL = registerMenuURL.replace("{tenantId}", tenantId);
		String menusStr = com.alibaba.fastjson.JSONObject.toJSONString(menus);
		_LOGGER.info("修改或注册的菜单为"+menusStr);
		List<NameValuePair> list = new ArrayList<NameValuePair>();
		list.add(new BasicNameValuePair("customerMenus", menusStr));
		try {
			String sentPostByForm = ApiCallUtils.putOrPostForm(reqURL, list, null, HttpMethod.POST);
//			String sendPostByJson = HttpClientUtilsNew.getResponseString(registerMenuURL, body);

			_LOGGER.info("registerMenuURL return sentPostByForm : " + sentPostByForm);

			JSONObject jsonObject = JSONObject.fromObject(sentPostByForm);

			if (null != jsonObject && jsonObject.has("status") && jsonObject.has("msg")) {

				String status = null == jsonObject.get("status") ? "" : jsonObject.get("status").toString();

				switch (status) {
				case "200":
					result.setStatus(Status.SUCCESS);
					result.setErrorCode(ErrorCodeNo.SYS000);
					break;
				case "110":
					result.setStatus(Status.FAILED);
					break;
				// TODO other status
//				case "301":
//					result.setStatus(Status.FAILED);
//					break;
				default:
					result.setStatus(Status.SYSTEMERROR);
					break;
				}
				result.setMessage(jsonObject.get("msg").toString());
			} else {
				_LOGGER.error("发布菜单时,缺失返回值:" + jsonObject);

				result.setErrorMessage("发布菜单出现错误!");
			}
		} catch (Exception e) {
			_LOGGER.error("send registerMenuURL error,e:" + e.toString());
			result.setErrorMessage("发布菜单错误!");
		}
		return result;
	}

	@Override
	public Result<?> deleteMenu1(ArrayList<String> ids) {
		String deleteMenuURL = PropertiesUtil.getInstance().getValue("deleteMenu1");

		Result<String> result = new Result<>();
		if (StringUtils.isBlank(deleteMenuURL)) {
			_LOGGER.error("registerMenuURL is null !");
			result.setErrorMessage("发布菜单错误,请联系管理员");
			return result;
		}
		
		String reqURL = null;
		try {
			reqURL = deleteMenuURL.replace("{ids}", URLEncoder.encode(ids.toString(), "utf-8"));
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}
		try {
			String sendGet = ApiCallUtils.getOrDelMethod(reqURL, null, HttpMethod.GET);

			_LOGGER.info("deleteMenuURL return setGet : " + sendGet);

			JSONObject jsonObject = JSONObject.fromObject(sendGet);

			if (null != jsonObject && jsonObject.has("status") && jsonObject.has("msg")) {

				String status = null == jsonObject.get("status") ? "" : jsonObject.get("status").toString();

				switch (status) {
				case "200":
					result.setStatus(Status.SUCCESS);
					result.setErrorCode(ErrorCodeNo.SYS000);
					break;
				// TODO other status
//				case "201":
//					result.setStatus(Status.FAILED);
//					break;
//				case "301":
//					result.setStatus(Status.FAILED);
//					break;
				default:
					result.setStatus(Status.SYSTEMERROR);
					break;
				}
				result.setMessage(jsonObject.get("msg").toString());
			} else {
				_LOGGER.error("删除菜单时,缺失返回值:" + jsonObject);

				result.setErrorMessage("删除菜单出现错误!");
			}
		} catch (Exception e) {
			_LOGGER.error("send deleteMenuURL error,e:" + e.toString());
			result.setErrorMessage("删除菜单错误!");
		}
		return result;
	}
	
	private boolean validateMenu(List<Menu> menus) {
		
		if(menus.isEmpty()){
			return false;
		}
		for (Menu menu : menus) {
			//如果id为空做添加叫校验。否则做修改校验
			if(null != menu.getId() && menu.getId() > 0){
				//修改校验
				if(StringUtils.isBlank(menu.getUrl())){
					_LOGGER.info("菜单url错误");
					return false;
				}
			}else{
				//添加校验
				if(null == menu.getLevel() || menu.getLevel() < 1 || menu.getLevel() > 3){
					_LOGGER.info("菜单level错误");
					return false;
				}else if(StringUtils.isBlank(menu.getUrl())){
					_LOGGER.info("菜单url错误");
					return false;
				}else if(null == menu.getParentId() && null == menu.getBeforeMenuId()
						&& null == menu.getBehandMenuId()){
					_LOGGER.info("菜单相对定位坐标错误");
					return false;
				}else if(null == menu.getPoint() || menu.getPoint() < 1){
					_LOGGER.info("菜单相对定位位移错误");
					return false;
				}else if(1 != menu.getLeaf() && 0 != menu.getLeaf()){
					_LOGGER.info("菜单是否为叶子错误");
					return false;
				}
			}
		}
		return true;
	}
}
