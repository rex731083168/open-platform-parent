package cn.ce.platform_service.diyApply.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.regex.Pattern;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;

import cn.ce.platform_service.apis.service.IConsoleApiService;
import cn.ce.platform_service.common.AuditConstants;
import cn.ce.platform_service.common.ErrorCodeNo;
import cn.ce.platform_service.common.HttpClientUtil;
import cn.ce.platform_service.common.MongoFiledConstants;
import cn.ce.platform_service.common.RateConstants;
import cn.ce.platform_service.common.RateEnum;
import cn.ce.platform_service.common.Result;
import cn.ce.platform_service.common.Status;
import cn.ce.platform_service.common.page.Page;
import cn.ce.platform_service.diyApply.dao.IDiyApplyDao;
import cn.ce.platform_service.diyApply.entity.DiyApplyEntity;
import cn.ce.platform_service.diyApply.entity.interfaceMessageInfo.InterfaMessageInfoString;
import cn.ce.platform_service.diyApply.entity.tenantAppsEntity.AppList;
import cn.ce.platform_service.diyApply.entity.tenantAppsEntity.TenantApps;
import cn.ce.platform_service.diyApply.service.IConsoleDiyApplyService;
import cn.ce.platform_service.diyApply.service.IPlublicDiyApplyService;
import cn.ce.platform_service.util.PropertiesUtil;
import cn.ce.platform_service.util.SplitUtil;
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
public class ConsoleDiyApplyServiceImpl implements IConsoleDiyApplyService {

	/** 日志对象 */
	private static Logger _LOGGER = Logger.getLogger(ConsoleDiyApplyServiceImpl.class);

	@Resource
	private IDiyApplyDao diyApplyDao;
	// @Resource
	// private IApiOauthService apiOauthService;
	@Resource
	private IConsoleApiService consoleApiService;
	@Resource
	private IPlublicDiyApplyService plublicDiyApplyService;

	@Override
	public Result<String> saveApply(DiyApplyEntity entity) {
		Result<String> result = new Result<>();

		// 构建查询对象
		Criteria c = new Criteria();

		if (StringUtils.isNotBlank(entity.getUser().getId())) {
			c.and(MongoFiledConstants.BASIC_USERID).is(entity.getUser().getId());
		}

		if (StringUtils.isNotBlank(entity.getApplyName())) {
			c.and(MongoFiledConstants.DIY_APPLY_APPLYNAME).is(entity.getApplyName());
		}

		// 修改时排除当前修改应用
		if (StringUtils.isNotBlank(entity.getId())) {
			c.and(MongoFiledConstants.BASIC_ID).ne(entity.getId());
		}

		Query query = new Query(c).with(new Sort(Direction.DESC, MongoFiledConstants.BASIC_CREATEDATE));

		List<DiyApplyEntity> findPageByList = diyApplyDao.findListByEntity(query);

		if (null != findPageByList && findPageByList.size() > 0) {
			result.setErrorMessage("应用名称不可重复!", ErrorCodeNo.SYS010);
			return result;
		}

		// 验证产品码

		// 新增
		if (StringUtils.isBlank(entity.getId())) {
			entity.setUserId(entity.getUser().getId());
			entity.setUserName(entity.getUser().getUserName());
			entity.setEnterpriseName(entity.getUser().getEnterpriseName());
			entity.setCreateDate(new Date());
			entity.setCheckState(AuditConstants.DIY_APPLY_UNCHECKED);

			String key = entity.getProductAuthCode();
			String findTenantAppsByTenantKeyTenantId = null;
			String findTenantAppsByTenantKeyTenanName = null;

			// 产品信息
			TenantApps apps = new TenantApps();
			try {
				apps = plublicDiyApplyService.findTenantAppsByTenantKey(key).getData(); // 接入产品中心获取产品信息和开放应用信息

				if (apps.getStatus() == AuditConstants.INTERFACE_RETURNSATAS_FAILE
						|| apps.getData().getTenant() == null) {
					result.setErrorMessage("产品码不可用!", ErrorCodeNo.SYS015);
					return result;
				}

				int findTenantAppsByTenantKeyTenantIdtemp = apps.getData().getTenant().getId();
				findTenantAppsByTenantKeyTenantId = String.valueOf(findTenantAppsByTenantKeyTenantIdtemp);
				findTenantAppsByTenantKeyTenanName = apps.getData().getTenant().getName();
			} catch (Exception e) {
				// TODO: handle exception
				_LOGGER.error("get messaget from url faile resaon " + e.getMessage() + "");
			}

			entity.setProductInstanceId(findTenantAppsByTenantKeyTenantId);
			entity.setProductName(findTenantAppsByTenantKeyTenanName);

			/***************************************************************
			 * 
			 * TODO 定制应用和api以及频次绑定操作 将当前定制应用绑定的开放应用下的所有api推送到网关，并且给当前定制应用绑定频次限制设定
			 * 只在当前位置做了绑定关系，如果将来绑定关系和绑定位置发生变化需要修改这段代码
			 *************************************************************/

			_LOGGER.info("/********************创建定制应用绑定频次，推送网关开始***************************/");
			String policyId = UUID.randomUUID().toString().replaceAll("\\-", "");
			String clientId = UUID.randomUUID().toString().replaceAll("\\-", "");
			String secret = UUID.randomUUID().toString().replaceAll("\\-", "");
			List<String> appIdList = new ArrayList<String>();
			for (AppList appList : apps.getData().getAppList()) {
				appIdList.add(appList.getAppCode()); // TODO 这里绑定的是appId这个属性，添加api的时候绑定的开放应用的id也应该为appId
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
			_LOGGER.info("/********************创建定制应用绑定频次，推送网关结束***************************/");
			_LOGGER.info("insert apply begin : " + JSON.toJSONString(entity));
			diyApplyDao.saveOrUpdate(entity);
			_LOGGER.info("save end");
			result.setSuccessMessage("新增成功!");

		} else {
			// 修改
			DiyApplyEntity applyById = diyApplyDao.findById(entity.getId());

			if (null == applyById) {
				result.setErrorMessage("请求的应用信息不存在!");
				return result;
			} else {

				if (StringUtils.isNotBlank(entity.getApplyName())) {
					applyById.setApplyName(entity.getApplyName());
				}

				if (StringUtils.isNotBlank(entity.getApplyDesc())) {
					applyById.setApplyDesc(entity.getApplyDesc());
				}
				if (entity.getCheckState().equals(AuditConstants.DIY_APPLY_CHECKED_SUCCESS)) {
					result.setErrorMessage("应用审核成功,无法修改记录!");
					return result;
				}
			}

			_LOGGER.info("update apply begin : " + JSON.toJSONString(applyById));
			diyApplyDao.saveOrUpdate(applyById);
			_LOGGER.info("save end");
			result.setSuccessMessage("修改成功!");

		}

		return result;
	}

	@Override
	public Result<?> updateApply(DiyApplyEntity apply) {

		Result<String> result = new Result<String>();
		if (StringUtils.isBlank(apply.getId())) {
			result.setErrorMessage("当前id不能为空", ErrorCodeNo.SYS005);
		}

		DiyApplyEntity apply1 = diyApplyDao.findById(apply.getId());

		if (null == apply1) {
			result.setErrorMessage("查询结果不存在", ErrorCodeNo.SYS015);
			return result;
		}
		if (apply1.getProductAuthCode() != apply.getProductAuthCode()) {
			result.setErrorMessage("productAuthCode前后不一致", ErrorCodeNo.SYS016);
			return result;
		}
		diyApplyDao.saveOrUpdate(apply1);
		result.setSuccessMessage("修改成功");
		return result;
	}

	@Override
	public Result<String> deleteApplyByid(String id) {
		// TODO Auto-generated method stub
		Result<String> result = new Result<>();
		DiyApplyEntity apply = diyApplyDao.findById(id);
		if (null == apply) {
			result.setErrorMessage("请求删除的应用不存在!");
			return result;
		} else if (apply.getAuthIds() != null && apply.getAuthIds().size() > 0) {
			result.setErrorMessage("应用下存在api,删除失败!");
			return result;
		} else if (apply.getCheckState().equals(AuditConstants.DIY_APPLY_CHECKED_SUCCESS)
				|| apply.getCheckState().equals(AuditConstants.DIY_APPLY_CHECKED_COMMITED)) {
			result.setErrorMessage("该应用已审核,无法删除!");
			return result;
		} else {
			_LOGGER.info("delete apply begin applyId:" + id);
			diyApplyDao.delete(id);
			_LOGGER.info("delete apply end");
			result.setSuccessMessage("删除成功!");
			return result;
		}

	}

	@Override
	public Result<Page<DiyApplyEntity>> findApplyList(DiyApplyEntity entity, Page<DiyApplyEntity> page) {
		Result<Page<DiyApplyEntity>> result = new Result<>();
		Page<DiyApplyEntity> diyApplyPage = diyApplyDao.findApplyList(entity.getApplyName(),entity.getProductName(),entity.getCheckState(),page);
		//Page<DiyApplyEntity> findPageByEntity = diyApplyDao.findPageByEntity(generalApplyQuery(entity, null), page);
		result.setSuccessData(diyApplyPage);
		return result;
	}

	@Override
	public Result<List<DiyApplyEntity>> findApplyList(DiyApplyEntity entity) {
		return null;
	}

	// @Override
	// public Result<DiyApplyEntity> getApplyById(String id, int pageSize, int
	// currentPage) {
	// Result<DiyApplyEntity> result = new Result<>();
	// DiyApplyEntity apply = diyApplyDao.findById(id);
	//
	// if (null == apply) {
	// result.setErrorMessage("该应用不存在!");
	// } else if (null == apply.getAuthIds()) {
	// result.setErrorMessage("该应用下暂无api信息!");
	// } else {
	// List<String> authIds = apply.getAuthIds();
	// if (null != authIds) {
	// int begin = (currentPage - 1) * pageSize;
	//
	// int end = pageSize * currentPage;
	//
	// if (authIds.size() < end) {
	// end = authIds.size();
	// }
	//
	// authIds = authIds.subList(begin, end);
	//
	// List<ApiAuditEntity> apiAuditList =
	// apiOauthService.getApiAuditEntity(authIds);
	//
	// apply.setAuditList(apiAuditList);
	// result.setSuccessData(apply);
	// }
	// }
	// return result;
	// }

	/***
	 * 根据实体对象构建查询条件
	 * 
	 * @param entity
	 *            实体对象
	 * @author lida
	 * @return
	 */
	private Criteria generalApplyCriteria(DiyApplyEntity entity) {
		// 构建查询对象
		Criteria c = new Criteria();

		if (StringUtils.isNotBlank(entity.getId())) {
			c.and("id").is(entity.getId());
		}

		if (StringUtils.isNotBlank(entity.getUserId())) {
			c.and("userId").is(entity.getUserId());
		}

		if (StringUtils.isNotBlank(entity.getApplyName())) {
			c.and("applyName").regex(entity.getApplyName());
		}

		return c;
	}

//	private Query generalApplyQuery(DiyApplyEntity apply, Sort sort) {
//		if (sort == null) {
//			sort = new Sort(Direction.DESC, MongoFiledConstants.BASIC_CREATEDATE);
//		}
//		Query query = new Query(generalApplyCriteria(apply)).with(sort);
//		return query;
//	}

	@Override
	public DiyApplyEntity findById(String applyId) {
		return diyApplyDao.findById(applyId);
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

			/* 无接口时的测试方法 */
			// InterfaMessageInfoString messageInfo = (InterfaMessageInfoString)
			// testgetUrlReturnObject(
			// "generatorTenantKey", replacedurl, InterfaMessageInfoString.class, null);
			if (messageInfo.getStatus() == 200 || messageInfo.getStatus() == 110) {
				result.setData(messageInfo);
				result.setSuccessMessage("");
				return result;
			} else {
				_LOGGER.error("generatorTenantKey data http getfaile return code :" + messageInfo.getMsg() + " ");
				result.setErrorCode(ErrorCodeNo.SYS006);
				return result;
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			_LOGGER.error("generatorTenantKey http error " + e + "");
			result.setErrorCode(ErrorCodeNo.SYS001);
			result.setErrorMessage("请求失败");
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

			String sendGetRequest = HttpClientUtil.sendGetRequest(reqURL, "UTF-8");

			_LOGGER.debug("produMenuList return json:" + sendGetRequest);

			JSONObject jsonObject = JSONObject.fromObject(sendGetRequest);

			if (null != jsonObject && jsonObject.has("status") && jsonObject.has("msg") && jsonObject.has("data")) {

				String status = jsonObject.get("status") == null ? "" : jsonObject.get("status").toString();

				switch (status) {
				case "101":
					result.setStatus(Status.SUCCESS);

					result.setSuccessData(jsonObject.get("data").toString());

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
			String sendPostByJson = HttpClientUtil.sendPostRequestByJava(registerMenuURL, body);
			// String sendPostByJson = ApiCallUtils.putOrPostMethod(registerMenuURL, body,
			// null, HttpMethod.POST);

			// String sendPostByJson = HttpClientUtil.sendPostByJson(registerMenuURL,
			// body.toString());

			JSONObject jsonObject = JSONObject.fromObject(sendPostByJson);

			if (null != jsonObject && jsonObject.has("status") && jsonObject.has("msg")) {

				String status = jsonObject.get("status") == null ? "" : jsonObject.get("status").toString();

				switch (status) {
				case "101":
					result.setStatus(Status.SUCCESS);
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
	public Result<String> batchUpdate(String ids, int checkState, String checkMem) {
		// TODO Auto-generated method stub
		Result<String> result = new Result<>();
		try {
			String message = diyApplyDao.bathUpdateByid(SplitUtil.splitStringWithComma(ids), checkState, checkMem);
			_LOGGER.info("bachUpdate diyApply message " + message + " count");
			result.setSuccessMessage("审核成功:" + message + "条");
			return result;
		} catch (Exception e) {
			// TODO: handle exception
			_LOGGER.error("bachUpdate diyApply message error ", e);
			result.setErrorCode(ErrorCodeNo.SYS001);
			result.setErrorMessage("审核失败");
			return result;
		}
	}
}
