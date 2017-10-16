package cn.ce.platform_service.diyApply.service.impl;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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

import cn.ce.platform_service.apis.entity.ApiAuditEntity;
import cn.ce.platform_service.apis.service.IAPIService;
import cn.ce.platform_service.apis.service.IApiOauthService;
import cn.ce.platform_service.common.AuditConstants;
import cn.ce.platform_service.common.CRequest;
import cn.ce.platform_service.common.ErrorCodeNo;
import cn.ce.platform_service.common.HttpClientUtil;
import cn.ce.platform_service.common.MongoFiledConstants;
import cn.ce.platform_service.common.Result;
import cn.ce.platform_service.common.page.Page;
import cn.ce.platform_service.diyApply.dao.IDiyApplyDao;
import cn.ce.platform_service.diyApply.entity.DiyApplyEntity;
import cn.ce.platform_service.diyApply.entity.appsEntity.Apps;
import cn.ce.platform_service.diyApply.entity.interfaceMessageInfo.InterfaMessageInfoJasonObject;
import cn.ce.platform_service.diyApply.entity.interfaceMessageInfo.InterfaMessageInfoString;
import cn.ce.platform_service.diyApply.entity.tenantAppsEntity.TenantApps;
import cn.ce.platform_service.diyApply.service.IConsoleDiyApplyService;
import cn.ce.platform_service.util.PropertiesUtil;
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
	private static Logger logger = Logger.getLogger(ConsoleDiyApplyServiceImpl.class);

	@Resource
	private IAPIService apiService;
	@Resource
	private IDiyApplyDao diyApplyDao;
	@Resource
	private IApiOauthService apiOauthService;

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
				apps = this.findTenantAppsByTenantKey(key).getData(); //接入产品中心获取产品信息和开放应用信息
				
				int findTenantAppsByTenantKeyTenantIdtemp = apps.getData().getTenant().getId();
				findTenantAppsByTenantKeyTenantId = String.valueOf(findTenantAppsByTenantKeyTenantIdtemp);
				findTenantAppsByTenantKeyTenanName = apps.getData().getTenant().getName();
			} catch (Exception e) {
				// TODO: handle exception
				logger.error("get messaget from url faile resaon " + e.getMessage() + "");
			}

			entity.setProductInstanceId(findTenantAppsByTenantKeyTenantId);
			entity.setProductName(findTenantAppsByTenantKeyTenanName);

			logger.info("insert apply begin : " + JSON.toJSONString(entity));
			
			diyApplyDao.saveOrUpdate(entity);
			logger.info("save end");
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
			}

			logger.info("update apply begin : " + JSON.toJSONString(applyById));
			diyApplyDao.saveOrUpdate(applyById);
			logger.info("save end");
			result.setSuccessMessage("修改成功!");

		}

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
		} else if (apply.getCheckState().equals(AuditConstants.DIY_APPLY_CHECKED_SUCCESS)) {
			result.setErrorMessage("该应用已审,无法删除!");
			return result;
		} else {
			logger.info("delete apply begin applyId:" + id);
			diyApplyDao.delete(id);
			logger.info("delete apply end");
			result.setSuccessMessage("删除成功!");
			return result;
		}

	}

	@Override
	public Result<Page<DiyApplyEntity>> findApplyList(DiyApplyEntity entity, Page<DiyApplyEntity> page) {
		Result<Page<DiyApplyEntity>> result = new Result<>();
		Page<DiyApplyEntity> findPageByEntity = diyApplyDao.findPageByEntity(generalApplyQuery(entity, null), page);
		result.setData(findPageByEntity);
		return result;
	}

	@Override
	public Result<List<DiyApplyEntity>> findApplyList(DiyApplyEntity entity) {
		return null;
	}

	@Override
	public Result<DiyApplyEntity> getApplyById(String id, int pageSize, int currentPage) {
		Result<DiyApplyEntity> result = new Result<>();
		DiyApplyEntity apply = diyApplyDao.findById(id);

		if (null == apply) {
			result.setErrorMessage("该应用不存在!");
		} else if (null == apply.getAuthIds()) {
			result.setErrorMessage("该应用下暂无api信息!");
		} else {
			List<String> authIds = apply.getAuthIds();
			if (null != authIds) {
				int begin = (currentPage - 1) * pageSize;

				int end = pageSize * currentPage;

				if (authIds.size() < end) {
					end = authIds.size();
				}

				authIds = authIds.subList(begin, end);

				List<ApiAuditEntity> apiAuditList = apiOauthService.getApiAuditEntity(authIds);

				apply.setAuditList(apiAuditList);
				result.setSuccessData(apply);
			}
		}
		return result;
	}

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

	private Query generalApplyQuery(DiyApplyEntity apply, Sort sort) {
		if (sort == null) {
			sort = new Sort(Direction.DESC, MongoFiledConstants.BASIC_CREATEDATE);
		}
		Query query = new Query(generalApplyCriteria(apply)).with(sort);
		return query;
	}

	@Override
	public DiyApplyEntity findById(String applyId) {
		return diyApplyDao.findById(applyId);
	}

	@Override
	public Result<TenantApps> findTenantAppsByTenantKey(String key) {
		
		// TODO 需要把key查询出来的定制应用和多个开放应用的绑定关系存入数据库

		Result<TenantApps> result = new Result<>();
		String url = PropertiesUtil.getInstance().getValue("findTenantAppsByTenantKey");
		String key$ = Pattern.quote("${key}");
		String replacedurl = url.replaceAll(key$, key);
		Map<String, Class> classMap = new HashMap<String, Class>();
		classMap.put("appList", cn.ce.platform_service.diyApply.entity.tenantAppsEntity.AppList.class);
		classMap.put("tenant", cn.ce.platform_service.diyApply.entity.tenantAppsEntity.Tenant.class);

		try {
			/* get请求方法 */
			// TenantApps applyproduct =
			// (TenantApps)getUrlReturnObject(replacedurl,TenantApps.class,classMap);
			/* post请求方法 */
			// TenantApps applyproduct = (TenantApps) postUrlReturnObject(replacedurl,
			// TenantApps.class, classMap);
			/* 无接口时的测试方法 */
			TenantApps applyproduct = (TenantApps) testgetUrlReturnObject("findTenantAppsByTenantKey", replacedurl,
					TenantApps.class, classMap);
			if (applyproduct.getStatus() == 200) {
				result.setData(applyproduct);
				result.setSuccessMessage("");
				return result;
			} else {
				logger.error(
						"findTenantAppsByTenantKey data http getfaile return code :" + applyproduct.getMsg() + " ");
				result.setErrorCode(ErrorCodeNo.SYS006);
				return result;
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			logger.error("findTenantAppsByTenantKey http error " + e + "");
			result.setErrorCode(ErrorCodeNo.SYS001);
			result.setErrorMessage("请求失败");
			return result;
		}

	}

	@Override
	public Result<Apps> findPagedApps(String owner, String name, int pageNum, int pageSize) {
		// TODO Auto-generated method stub
		Result<Apps> result = new Result<>();
		String url = PropertiesUtil.getInstance().getValue("findPagedApps");
		String o$ = Pattern.quote("${o}");
		String n$ = Pattern.quote("${n}");
		String p$ = Pattern.quote("${p}");
		String z$ = Pattern.quote("${z}");

		String replacedurl = url.replaceAll(o$, owner).replaceAll(n$, name).replaceAll(p$, String.valueOf(pageNum))
				.replaceAll(z$, String.valueOf(pageSize));

		Map<String, Class> classMap = new HashMap<String, Class>();
		classMap.put("list", cn.ce.platform_service.diyApply.entity.appsEntity.AppList.class);
		classMap.put("appTypes", cn.ce.platform_service.diyApply.entity.appsEntity.AppTypes.class);

		try {
			/* get请求方法 */
			// Apps apps = (Apps) getUrlReturnObject(replacedurl, Apps.class,classMap);
			/* post请求方法 */
			// Apps apps = (Apps) postUrlReturnObject(replacedurl, Apps.class,classMap);
			/* 无接口时的测试方法 */
			Apps apps = (Apps) testgetUrlReturnObject("findPagedApps", replacedurl, Apps.class, classMap);
			if (apps.getStatus() == 200) {
				result.setData(apps);
				result.setSuccessMessage("");
				return result;
			} else {
				logger.error("findPagedApps data http getfaile return code :" + apps.getMsg() + " ");
				result.setErrorCode(ErrorCodeNo.SYS006);
				return result;
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			logger.error("findPagedApps http error " + e + "");
			result.setErrorCode(ErrorCodeNo.SYS001);
			result.setErrorMessage("请求失败");
			return result;
		}

	}

	@Override
	public Result<InterfaMessageInfoJasonObject> registerBathApp(String tenantId, String apps) {
		// TODO Auto-generated method stub
		Result<InterfaMessageInfoJasonObject> result = new Result<>();
		String url = PropertiesUtil.getInstance().getValue("registerBathApp");
		String tId$ = Pattern.quote("${tId}");
		String appList$ = Pattern.quote("${appList}");
		String replacedurl = url.replaceAll(tId$, tenantId).replaceAll(appList$, apps);

		try {
			/* get请求方法 */
			// InterfaMessageInfoJasonObject
			// messageInfo=(InterfaMessageInfoJasonObject)getUrlReturnObject(replacedurl,
			// Apps.class,null);
			/* post请求方法 */
			// InterfaMessageInfoJasonObject
			// messageInfo=(InterfaMessageInfoJasonObject)postUrlReturnObject(replacedurl,
			// Apps.class,null);
			/* 无接口时的测试方法 */
			InterfaMessageInfoJasonObject messageInfo = (InterfaMessageInfoJasonObject) testgetUrlReturnObject(
					"registerBathApp", replacedurl, InterfaMessageInfoJasonObject.class, null);
			if (messageInfo.getStatus() == 200) {
				result.setData(messageInfo);
				result.setSuccessMessage("");
				return result;
			} else {
				logger.error("registerBathApp data http getfaile return code :" + messageInfo.getMsg() + " ");
				result.setErrorCode(ErrorCodeNo.SYS006);
				return result;
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			logger.error("registerBathApp http error " + e + "");
			result.setErrorCode(ErrorCodeNo.SYS001);
			result.setErrorMessage("请求失败");
			return result;
		}

	}

	@Override
	public Result<InterfaMessageInfoString> saveOrUpdateApps(String apps) {
		// TODO Auto-generated method stub
		Result<InterfaMessageInfoString> result = new Result<>();
		String url = PropertiesUtil.getInstance().getValue("saveOrUpdateApps");
		String apps$ = Pattern.quote("${apps}");
		String replacedurl = url.replaceAll(apps$, apps);

		try {
			/* get请求方法 */
			// InterfaMessageInfoString messageInfo =
			// (InterfaMessageInfoString)getUrlReturnObject(replacedurl,
			// InterfaMessageInfo.class,null);
			/* post请求方法 */
			// InterfaMessageInfoString messageInfo =
			// (InterfaMessageInfoString)postUrlReturnObject(replacedurl,InterfaMessageInfo.class,null);
			/* 无接口时的测试方法 */
			InterfaMessageInfoString messageInfo = (InterfaMessageInfoString) testgetUrlReturnObject("saveOrUpdateApps",
					replacedurl, InterfaMessageInfoString.class, null);
			if (messageInfo.getStatus() == 200) {
				result.setData(messageInfo);
				result.setSuccessMessage("");
				return result;
			} else {
				logger.error("saveOrUpdateApps data http getfaile return code :" + messageInfo.getMsg() + " ");
				result.setErrorCode(ErrorCodeNo.SYS006);
				return result;
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			logger.error("saveOrUpdateApps http error " + e + "");
			result.setErrorCode(ErrorCodeNo.SYS001);
			result.setErrorMessage("请求失败");
			return result;
		}
	}

	@Override
	public Result<InterfaMessageInfoString> generatorTenantKey(String id) {
		// TODO Auto-generated method stub
		Result<InterfaMessageInfoString> result = new Result<>();
		String url = PropertiesUtil.getInstance().getValue("generatorTenantKey");
		String id$ = Pattern.quote("${id}");
		String replacedurl = url.replaceAll(id$, id);

		try {
			/* get请求方法 */
			// InterfaMessageInfo messageInfo =
			// (InterfaMessageInfo)getUrlReturnObject(replacedurl,
			// InterfaMessageInfo.class,null);
			/* post请求方法 */
			// InterfaMessageInfo messageInfo =
			// (InterfaMessageInfo)postUrlReturnObject(replacedurl,
			// InterfaMessageInfo.class,null);
			/* 无接口时的测试方法 */
			InterfaMessageInfoString messageInfo = (InterfaMessageInfoString) testgetUrlReturnObject(
					"generatorTenantKey", replacedurl, InterfaMessageInfoString.class, null);
			if (messageInfo.getStatus() == 200) {
				result.setData(messageInfo);
				result.setSuccessMessage("");
				return result;
			} else {
				logger.error("generatorTenantKey data http getfaile return code :" + messageInfo.getMsg() + " ");
				result.setErrorCode(ErrorCodeNo.SYS006);
				return result;
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			logger.error("generatorTenantKey http error " + e + "");
			result.setErrorCode(ErrorCodeNo.SYS001);
			result.setErrorMessage("请求失败");
			return result;
		}
	}

	public Object getUrlReturnObject(String url, Class<?> clazz, Map<String, Class> classMap) {
		String jasonResultHttpGet = HttpClientUtil.sendGetRequest(url, null);
		JSONObject jsonobject = JSONObject.fromObject(jasonResultHttpGet);
		Object object = JSONObject.toBean(jsonobject, clazz, classMap);
		return object;
	}

	public Object postUrlReturnObject(String url, Class<?> clazz, Map<String, Class> classMap) {
		// url="http://127.0.0.1:8080/platform-console/statistics/statisticsLineChartAndPie111111?key=11111&apps=[{1,2}]";
		String reqURL = CRequest.UrlPage(url);
		Map<String, String> params = CRequest.URLRequest(url);
		String strRequestKeyAndValues = "";
		for (String strRequestKey : params.keySet()) {
			String strRequestValue = params.get(strRequestKey);
			strRequestKeyAndValues += strRequestKey + "=" + strRequestValue + "";
		}
		String jasonResultHttpGet = HttpClientUtil.sendPostRequestByJava(reqURL, params);
		// String jasonResultHttpGet = HttpClientUtil.sendGetRequest(url, null);
		JSONObject jsonobject = JSONObject.fromObject(jasonResultHttpGet);
		Object object = JSONObject.toBean(jsonobject, clazz, classMap);
		return object;

	}

	public Object testgetUrlReturnObject(String method, String url, Class<?> clazz, Map<String, Class> classMap)
			throws Exception {

		BufferedReader br = null;

		if (method.equals("findTenantAppsByTenantKey")) {
			URL resourcesurl = this.getClass().getClassLoader().getResource("jason/findTenantAppsByTenantKey.json");

			br = new BufferedReader(new InputStreamReader(new FileInputStream(resourcesurl.getFile()), "UTF-8"));
		}
		if (method.equals("findPagedApps")) {

			URL resourcesurl = this.getClass().getClassLoader().getResource("jason/findPagedApps.json");

			br = new BufferedReader(new InputStreamReader(new FileInputStream(resourcesurl.getFile()), "UTF-8"));

		}
		if (method.equals("registerBathApp")) {
			URL resourcesurl = this.getClass().getClassLoader().getResource("jason/registerBathApp.json");

			br = new BufferedReader(new InputStreamReader(new FileInputStream(resourcesurl.getFile()), "UTF-8"));
		}
		if (method.equals("generatorTenantKey")) {
			URL resourcesurl = this.getClass().getClassLoader().getResource("jason/generatorTenantKey.json");

			br = new BufferedReader(new InputStreamReader(new FileInputStream(resourcesurl.getFile()), "UTF-8"));
		}
		if (method.equals("saveOrUpdateApps")) {
			URL resourcesurl = this.getClass().getClassLoader().getResource("jason/saveOrUpdateApps.json");

			br = new BufferedReader(new InputStreamReader(new FileInputStream(resourcesurl.getFile()), "UTF-8"));
		}

		String s = "";
		String tempString = null;
		while ((tempString = br.readLine()) != null) {
			s += tempString;

		}

		JSONObject jsonobject = JSONObject.fromObject(s);
		Object object = JSONObject.toBean(jsonobject, clazz, classMap);

		br.close();
		return object;

	}

	@Override
	public Result<String> auditUpdate(String id, int checkState, String checkMem) {
		// TODO Auto-generated method stub
		Result<String> result = new Result<String>();
		try {
			DiyApplyEntity dae = diyApplyDao.findById(id);
			if (dae != null) {
				dae.setCheckState(checkState);
				dae.setCheckMem(checkMem);
				diyApplyDao.saveOrUpdate(dae);
				result.setSuccessMessage("操作成功");
			}
			return result;

		} catch (Exception e) {
			// TODO: handle exception
			logger.error("auditUpdate failed reason " + e + "");
			result.setErrorMessage("提交失败");
			result.setErrorCode(ErrorCodeNo.SYS001);
			return result;
		}

	}

}
