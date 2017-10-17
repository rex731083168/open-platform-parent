package cn.ce.platform_service.diyApply.service.impl;

import java.util.HashMap;
import java.util.Iterator;
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

import cn.ce.platform_service.common.AuditConstants;
import cn.ce.platform_service.common.ErrorCodeNo;
import cn.ce.platform_service.common.HttpClientUtil;
import cn.ce.platform_service.common.MongoFiledConstants;
import cn.ce.platform_service.common.Result;
import cn.ce.platform_service.common.page.Page;
import cn.ce.platform_service.diyApply.dao.IDiyApplyDao;
import cn.ce.platform_service.diyApply.entity.DiyApplyEntity;
import cn.ce.platform_service.diyApply.entity.appsEntity.Apps;
import cn.ce.platform_service.diyApply.entity.inparameter.RegisterBathAppInParameterEntity;
import cn.ce.platform_service.diyApply.entity.interfaceMessageInfo.InterfaMessageInfoString;
import cn.ce.platform_service.diyApply.service.IManageDiyApplyService;
import cn.ce.platform_service.util.PropertiesUtil;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * @Description : 说明
 * @Author : makangwei
 * @Date : 2017年10月16日
 */
@Service("manageDiyApplyService")
public class ManageDiyApplyServiceImpl implements IManageDiyApplyService {
	private static Logger _LOGGER = Logger.getLogger(ManageDiyApplyServiceImpl.class);
	@Resource
	private IDiyApplyDao diyApplyDao;

	@Override
	public Result<Page<DiyApplyEntity>> findPagedApps(String productName, String userName, int checkState,
			String applyName, int currentPage, int pageSize) {
		// TODO Auto-generated method stub
		Result<Page<DiyApplyEntity>> result = new Result<>();
		Page<DiyApplyEntity> page = new Page<>(currentPage, 0, pageSize);

		Criteria c = new Criteria();
		if (StringUtils.isNotBlank(productName)) {
			c.and("productName").regex(productName);
		}
		if (StringUtils.isNotBlank(userName)) {
			c.and("userName").regex(userName);
		}
		if (StringUtils.isNotBlank(String.valueOf(checkState))) {
			c.and("checkState").is(checkState);
		}
		if (StringUtils.isNotBlank(applyName)) {
			c.and("applyName").regex(applyName);
		}
		Query query = new Query(c).with(new Sort(Direction.DESC, MongoFiledConstants.BASIC_CREATEDATE));
		page = diyApplyDao.findPageByEntity(query, page);
		result.setData(page);

		return result;
	}

	@Override
	public Result<String> batchUpdate(List<String> ids, Integer checkState, String checkMem) {
		// TODO Auto-generated method stub
		Result<String> result = new Result<String>();
		try {
			/* 审核失败返回 */
			if (checkState == AuditConstants.DIY_APPLY_CHECKED_FAILED) {
				String message = String.valueOf(diyApplyDao.bathUpdateByid(ids, checkState, checkMem));
				_LOGGER.info("bachUpdate diyApply message " + message + " count");
				result.setSuccessMessage("审核成功:" + message + "条");
				return result;
			}

			Query query = new Query(Criteria.where("id").is(ids.get(0)));
			List<DiyApplyEntity> diyApply = diyApplyDao.findListByEntity(query);

			RegisterBathAppInParameterEntity[] queryVO = null;
			for (int i = 0; i < diyApply.size(); i++) {
				queryVO[i].setAppName(diyApply.get(i).getApplyName());
				queryVO[i].setAppUrl(diyApply.get(i).getDomainUrl());
				queryVO[i].setAppDesc(diyApply.get(i).getApplyDesc());
				queryVO[i].setAppCode(diyApply.get(i).getId());
				queryVO[i].setAppType("2");
				queryVO[i].setOwner(diyApply.get(i).getEnterpriseName());
			}
			/* 开发者在开放平台发布应用审核 */
			InterfaMessageInfoString interfaMessageInfoJasonObjectResult = this
					.registerBathApp(diyApply.get(0).getProductInstanceId(), JSONArray.fromObject(queryVO).toString())
					.getData();
			JSONObject jsonObjecttest = JSONObject.fromObject(interfaMessageInfoJasonObjectResult.getData());
			Iterator<String> keys = jsonObjecttest.keys();
			Map<String, Object> map = new HashMap<String, Object>();
			String key = null;
			Object value = null;
			while (keys.hasNext()) {
				key = keys.next();
				value = jsonObjecttest.get(key).toString();
				map.put(key, value);

			}
			/* 审核成功 */
			if (interfaMessageInfoJasonObjectResult.getStatus() == AuditConstants.INTERFACE_RETURNSATAS_SUCCESS) {
				String message = String.valueOf(diyApplyDao.bathUpdateByidAndPush(ids, map, checkState, checkMem));
				_LOGGER.info("bachUpdate diyApply message " + message + " count");
				result.setSuccessMessage("审核成功:" + message + "条");
				return result;
			} else {
				result.setErrorCode(ErrorCodeNo.SYS001);
				result.setErrorMessage("发布失败");
				return result;
			}
		} catch (Exception e) {
			// TODO: handle exception
			_LOGGER.info("bachUpdate diyApply message faile " + e + " ");
			result.setErrorCode(ErrorCodeNo.SYS001);
			result.setErrorMessage("审核失败");
			return result;
		}
	}

	@Override
	public Result<InterfaMessageInfoString> registerBathApp(String tenantId, String apps) {
		// TODO Auto-generated method stub
		Result<InterfaMessageInfoString> result = new Result<>();
		String url = PropertiesUtil.getInstance().getValue("registerBathApp");
		String tId$ = Pattern.quote("${tId}");
		String appList$ = Pattern.quote("${appList}");
		String replacedurl = url.replaceAll(tId$, tenantId).replaceAll(appList$, apps);

		try {
			/* get请求方法 */
			InterfaMessageInfoString messageInfo = new InterfaMessageInfoString();

			JSONObject jsonObject = (JSONObject) HttpClientUtil.getUrlReturnJsonObject(replacedurl);

			messageInfo.setData(jsonObject.getString("data"));
			messageInfo.setMsg(jsonObject.getString("msg"));
			messageInfo.setStatus(Integer.valueOf(jsonObject.getString("status")));

			/* 无接口时的测试方法 */
			// InterfaMessageInfoJasonObject messageInfo = (InterfaMessageInfoJasonObject)
			// testgetUrlReturnObject(
			// "registerBathApp", replacedurl, InterfaMessageInfoJasonObject.class, null);
			if (messageInfo.getStatus() == 200 || messageInfo.getStatus() == 110) {
				result.setData(messageInfo);
				result.setSuccessMessage("");
				return result;
			} else {
				_LOGGER.error("registerBathApp data http getfaile return code :" + messageInfo.getMsg() + " ");
				result.setErrorMessage("接口请求成功,");
				result.setErrorCode(ErrorCodeNo.SYS006);
				return result;
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			_LOGGER.error("registerBathApp http error " + e + "");
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
		if (StringUtils.isBlank(owner)) {
			owner = "";
		}
		if (StringUtils.isBlank(name)) {
			name = "";
		}

		String replacedurl = url.replaceAll(o$, owner).replaceAll(n$, name).replaceAll(p$, String.valueOf(pageNum))
				.replaceAll(z$, String.valueOf(pageSize));

		Map<String, Class> classMap = new HashMap<String, Class>();
		classMap.put("list", cn.ce.platform_service.diyApply.entity.appsEntity.AppList.class);
		classMap.put("appTypes", cn.ce.platform_service.diyApply.entity.appsEntity.AppTypes.class);

		try {
			/* get请求方法 */
			Apps apps = (Apps) HttpClientUtil.getUrlReturnObject(replacedurl, Apps.class, classMap);

			/* 无接口时的测试方法 */
			// Apps apps = (Apps) testgetUrlReturnObject("findPagedApps", replacedurl,
			// Apps.class, classMap);
			if (apps.getStatus() == 200 || apps.getStatus() == 110) {
				result.setData(apps);
				result.setSuccessMessage("");
				return result;
			} else {
				_LOGGER.error("findPagedApps data http getfaile return code :" + apps.getMsg() + " ");
				result.setErrorCode(ErrorCodeNo.SYS006);
				return result;
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			_LOGGER.error("findPagedApps http error " + e + "");
			result.setErrorCode(ErrorCodeNo.SYS001);
			result.setErrorMessage("请求失败");
			return result;
		}

	}

}
