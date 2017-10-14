package cn.ce.platform_service.diyApply.service.impl;

import java.net.URLEncoder;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.PoolingClientConnectionManager;
import org.apache.http.util.EntityUtils;
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
import cn.ce.platform_service.common.ErrorCodeNo;
import cn.ce.platform_service.common.MongoFiledConstants;
import cn.ce.platform_service.common.Result;
import cn.ce.platform_service.common.page.Page;
import cn.ce.platform_service.diyApply.dao.IDiyApplyDao;
import cn.ce.platform_service.diyApply.entity.DiyApplyEntity;
import cn.ce.platform_service.diyApply.entity.appsEntity.Apps;
import cn.ce.platform_service.diyApply.entity.interfaceMessageInfo.InterfaMessageInfo;
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
		
		//修改时排除当前修改应用
		if(StringUtils.isNotBlank(entity.getId())){
			c.and(MongoFiledConstants.BASIC_ID).ne(entity.getId());
		}

		Query query = new Query(c).with(new Sort(Direction.DESC, MongoFiledConstants.BASIC_CREATEDATE));

		List<DiyApplyEntity> findPageByList = diyApplyDao.findListByEntity(query);

		if(null != findPageByList && findPageByList.size() > 0){
			result.setMessage("应用名称不可重复!");
			result.setErrorCode(ErrorCodeNo.SYS010);
			return result;
		}

		// 新增
		if (StringUtils.isBlank(entity.getId())) {
			entity.setUserId(entity.getUser().getId());
			entity.setUserName(entity.getUser().getUserName());
			entity.setEnterpriseName(entity.getUser().getEnterpriseName());
			entity.setCreateDate(new Date());

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
		} else {
			logger.info("delete apply begin applyId:" + id);
			diyApplyDao.delete(id);
			logger.info("delete apply end");
			result.setSuccessMessage("删除成功!");
		}
		return result;
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
		// TODO Auto-generated method stub
		Result<TenantApps> result = new Result<>();
		String url = PropertiesUtil.getInstance().getValue("findTenantAppsByTenantKey");
		String key$ = Pattern.quote("${key}");
		String replacedurl = url.replaceAll(key$, key);
		try {

			TenantApps applyproduct = (TenantApps) getUrlReturnObject(replacedurl, TenantApps.class);
			if (applyproduct.getMsg().equals("200")) {
				result.setData(applyproduct);
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

		try {
			Apps apps = (Apps) getUrlReturnObject(replacedurl, Apps.class);
			if (apps.getMsg().equals("200")) {
				result.setData(apps);
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
	public Result<InterfaMessageInfo> registerBathApp(String tenantId, String apps) {
		// TODO Auto-generated method stub
		Result<InterfaMessageInfo> result = new Result<>();
		String url = PropertiesUtil.getInstance().getValue("registerBathApp");
		String tId$ = Pattern.quote("${tId}");
		String appList$ = Pattern.quote("${appList}");
		String replacedurl = url.replaceAll(tId$, tenantId).replaceAll(appList$, apps);

		try {
			InterfaMessageInfo messageInfo = (InterfaMessageInfo) getUrlReturnObject(replacedurl, Apps.class);
			if (messageInfo.getMsg().equals("200")) {
				result.setData(messageInfo);
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
	public Result<InterfaMessageInfo> saveOrUpdateApps(String apps) {
		// TODO Auto-generated method stub
		Result<InterfaMessageInfo> result = new Result<>();
		String url = PropertiesUtil.getInstance().getValue("saveOrUpdateApps");
		String apps$ = Pattern.quote("${apps}");
		String replacedurl = url.replaceAll(apps$, apps);

		try {
			InterfaMessageInfo messageInfo = (InterfaMessageInfo) getUrlReturnObject(replacedurl, Apps.class);
			if (messageInfo.getMsg().equals("200")) {
				result.setData(messageInfo);
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
	public Result<InterfaMessageInfo> generatorTenantKey(String id) {
		// TODO Auto-generated method stub
		Result<InterfaMessageInfo> result = new Result<>();
		String url = PropertiesUtil.getInstance().getValue("generatorTenantKey");
		String id$ = Pattern.quote("${id}");
		String replacedurl = url.replaceAll(id$, id);

		try {
			InterfaMessageInfo messageInfo = (InterfaMessageInfo) getUrlReturnObject(replacedurl, Apps.class);
			if (messageInfo.getMsg().equals("200")) {
				result.setData(messageInfo);
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

	public Object getUrlReturnObject(String url, Class<?> clazz) throws Exception {
		url = "http://localhost:8080/platform-console/statistics/statisticsLineChartAndPie?Tenankey=111111";

		String jasonResultHttpGet = null;
		Object object;
		ClientConnectionManager connManager = new PoolingClientConnectionManager();
		DefaultHttpClient client = new DefaultHttpClient(connManager);
		HttpGet request = new HttpGet(URLEncoder.encode(url));// 这里发送get请求
		HttpResponse response = client.execute(request);
		if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
			jasonResultHttpGet = EntityUtils.toString(response.getEntity(), "utf-8");
			logger.info("http request" + url + " success ");
			logger.info("http jasonResultHttpGet " + jasonResultHttpGet + "");
		}
		JSONObject jsonobject = JSONObject.fromObject(jasonResultHttpGet);
		object = JSONObject.toBean(jsonobject, clazz);
		return object;
	}

}
