package cn.ce.platform_service.diyApply.service.impl;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

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
import cn.ce.platform_service.common.MongoFiledConstants;
import cn.ce.platform_service.common.Result;
import cn.ce.platform_service.common.page.Page;
import cn.ce.platform_service.diyApply.dao.IDiyApplyDao;
import cn.ce.platform_service.diyApply.entity.DiyApplyEntity;
import cn.ce.platform_service.diyApply.entity.inparameter.RegisterBathAppInParameterEntity;
import cn.ce.platform_service.diyApply.entity.interfaceMessageInfo.InterfaMessageInfoString;
import cn.ce.platform_service.diyApply.service.IConsoleDiyApplyService;
import cn.ce.platform_service.diyApply.service.IManageDiyApplyService;
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
	@Resource
	private IConsoleDiyApplyService consoleDiyApplyService;

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
	public Result<String> batchUpdate(List<String> ids) {
		// TODO Auto-generated method stub
		Result<String> result = new Result<String>();
		try {
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
			InterfaMessageInfoString interfaMessageInfoJasonObjectResult = consoleDiyApplyService
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

			if (interfaMessageInfoJasonObjectResult.getStatus() == AuditConstants.INTERFACE_RETURNSATAS_SUCCESS) {
				String message = String
						.valueOf(diyApplyDao.bathUpdateByid(ids, AuditConstants.DIY_APPLY_CHECKED_SUCCESS));
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * cn.ce.platform_service.diyApply.service.IManageDiyApplyService#auditUpdate(
	 * java.lang.String, int, java.lang.String)
	 */
	@Override
	public Result<String> auditUpdate(String id, int checkState, String checkMem) {
		// TODO Auto-generated method stub
		Result<String> result = new Result<String>();

		DiyApplyEntity dae = diyApplyDao.findById(id);
		if (dae == null) {
			result.setErrorMessage("当前id不存在", ErrorCodeNo.SYS006);
			return result;
		}

		if (checkState > AuditConstants.DIY_APPLY_CHECKED_FAILED
				|| checkState < AuditConstants.DIY_APPLY_CHECKED_SUCCESS) {
			result.setErrorMessage("审核状态不存在", ErrorCodeNo.SYS012);
			return result;
		} else if (checkState == AuditConstants.DIY_APPLY_CHECKED_FAILED) { // 审核不通过
			dae.setCheckState(3);
			dae.setCheckMem(checkMem);
			diyApplyDao.saveOrUpdate(dae);
			result.setSuccessMessage("");
			return result;
		} else { // 审核通过

			// TODO 推送网关：定制应用申请clientId,并且将频次绑定在clientId,并且将定制应用绑定多个api

			getClientIdWithApis(dae);

			RegisterBathAppInParameterEntity[] queryVO = null;
			queryVO[0].setAppName(dae.getApplyName());
			queryVO[0].setAppUrl(dae.getDomainUrl());
			queryVO[0].setAppDesc(dae.getApplyDesc());
			queryVO[0].setAppCode(dae.getId());
			queryVO[0].setAppType("2");
			queryVO[0].setOwner(dae.getEnterpriseName());
			/* 调用接口推送信息 */
			InterfaMessageInfoString interfaMessageInfoJasonObjectResult = consoleDiyApplyService
					.registerBathApp(dae.getProductInstanceId(), JSONArray.fromObject(queryVO).toString()).getData();
			/* 接收返回信息存储本地 */
			JSONObject jsonObjecttest = JSONObject.fromObject(interfaMessageInfoJasonObjectResult.getData());
			Iterator<String> keys = jsonObjecttest.keys();
			String key = null;
			while (keys.hasNext()) {
				key = keys.next();
				dae.setAppId(jsonObjecttest.get(key).toString());
			}

			dae.setCheckState(checkState);
			diyApplyDao.saveOrUpdate(dae);
			result.setSuccessMessage("");
			return result;
		}
	}

	private void getClientIdWithApis(DiyApplyEntity dae) {
		// 1、创建policy，绑定多个api，推送网关

		// 2、创建clientId绑定policyId

	}

}
