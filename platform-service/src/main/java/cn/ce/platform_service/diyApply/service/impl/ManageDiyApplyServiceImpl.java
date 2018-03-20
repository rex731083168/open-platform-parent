package cn.ce.platform_service.diyApply.service.impl;

import java.util.List;
import java.util.regex.Pattern;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;

import cn.ce.platform_service.common.AuditConstants;
import cn.ce.platform_service.common.ErrorCodeNo;
import cn.ce.platform_service.common.HttpClientUtil;
import cn.ce.platform_service.common.Result;
import cn.ce.platform_service.common.page.Page;
import cn.ce.platform_service.diyApply.dao.IMysqlDiyApplyDao;
import cn.ce.platform_service.diyApply.entity.DiyApplyEntity;
import cn.ce.platform_service.diyApply.entity.QueryDiyApplyEntity;
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
@Transactional(propagation=Propagation.REQUIRED)
public class ManageDiyApplyServiceImpl implements IManageDiyApplyService {
	private static Logger _LOGGER = Logger.getLogger(ManageDiyApplyServiceImpl.class);
//	@Resource
//	private IDiyApplyDao diyApplyDao;
	@Resource
	private IMysqlDiyApplyDao mysqlDiyApplyDao;

	@Override
	public Result<Page<DiyApplyEntity>> findPagedApps(QueryDiyApplyEntity queryApply) {
		// TODO Auto-generated method stub
		Result<Page<DiyApplyEntity>> result = new Result<>();
		
//		Page<DiyApplyEntity> page = diyApplyDao.findPageByParam(productName, userName, checkState, applyName,  currentPage, pageSize);
		int totalNum = mysqlDiyApplyDao.findListSize(queryApply);
		List<DiyApplyEntity> list= mysqlDiyApplyDao.getPagedList(queryApply);
		Page<DiyApplyEntity> page= new Page<DiyApplyEntity>(queryApply.getCurrentPage(),totalNum,queryApply.getPageSize(),list);
		
		result.setSuccessData(page);
		
		return result;
	}

	@Override
	public Result<String> batchUpdate(List<String> ids, Integer checkState, String checkMem) {
		// TODO Auto-generated method stub
		Result<String> result = new Result<String>();
		try {
			/* 审核失败返回 */
			if (checkState == AuditConstants.DIY_APPLY_CHECKED_FAILED) {
				//String message = diyApplyDao.bathUpdateByid(ids, checkState, checkMem);
				int num = mysqlDiyApplyDao.bathUpdateCheckState(ids, checkState, checkMem);
				_LOGGER.info("bachUpdate diyApply message " + num + " count");
				result.setSuccessMessage("审核成功:" + num + "条");
				return result;
			}
			
			//Query query = new Query(Criteria.where("id").in(ids));
			//List<DiyApplyEntity> diyApply = diyApplyDao.findListByIds(ids);
			List<DiyApplyEntity> diyApply = mysqlDiyApplyDao.findByIds(ids);
			if(null == diyApply || diyApply.size() == 0){
				_LOGGER.info("diyApply List is Null,ids:" + JSON.toJSONString(ids));
				result.setMessage("应用不存在!");
				result.setErrorCode(ErrorCodeNo.SYS015);
				return result;
			}
			
			RegisterBathAppInParameterEntity[] queryVO = new RegisterBathAppInParameterEntity[diyApply.size()];
			for (int i = 0; i < diyApply.size(); i++) {
				RegisterBathAppInParameterEntity rapentity = new RegisterBathAppInParameterEntity();
				rapentity.setAppName(diyApply.get(i).getApplyName());
				rapentity.setAppUrl(diyApply.get(i).getDomainUrl());
				rapentity.setAppDesc(diyApply.get(i).getApplyDesc());
				rapentity.setAppCode(diyApply.get(i).getId());
				rapentity.setAppType("2");
				rapentity.setOwner(diyApply.get(i).getEnterpriseName());
				queryVO[i] = rapentity;
			}
			/* 开发者在开放平台发布应用审核 */

			_LOGGER.info("registerBathApp to interface satar");
			InterfaMessageInfoString interfaMessageInfoJasonObjectResult = this
					.registerBathApp(diyApply.get(0).getProductInstanceId(), JSONArray.fromObject(queryVO).toString())
					//
					//new org.json.JSONArray(queryVO)
					.getData();
			_LOGGER.info("registerBathApp to interface states" + interfaMessageInfoJasonObjectResult.getStatus() + "");
//			JSONObject jsonObjecttest = JSONObject.fromObject(interfaMessageInfoJasonObjectResult.getData());
//			Iterator<String> keys = jsonObjecttest.keys();
//			Map<String, Object> map = new HashMap<String, Object>();
//			String key = null;
//			Object value = null;
//			while (keys.hasNext()) {
//				key = keys.next();
//				value = jsonObjecttest.get(key).toString();
//				map.put(key, value);
//
//			}
			/* 审核成功 */
			if (interfaMessageInfoJasonObjectResult.getStatus() == AuditConstants.INTERFACE_RETURNSATAS_SUCCESS) {
				//String message = String.valueOf(diyApplyDao.bathUpdateByidAndPush(ids, map, checkState, checkMem));
				int totalNum = mysqlDiyApplyDao.bathUpdateCheckState(ids, checkState, checkMem);
				_LOGGER.info("bachUpdate diyApply message " + totalNum + " count");
				result.setSuccessMessage("审核成功:" + totalNum + "条");
				return result;
			} else {
				result.setErrorMessage("审核失败", ErrorCodeNo.SYS030);
				_LOGGER.info("bachUpdate diyApply message faile");
				return result;
			}
		} catch (Exception e) {
			// TODO: handle exception
			_LOGGER.error("bachUpdate diyApply message faile ", e);
			result.setErrorCode(ErrorCodeNo.SYS001);
			result.setErrorMessage("系统错误");
			return result;
		}
	}

	@Override
	public Result<InterfaMessageInfoString> registerBathApp(String tenantId, String app) {
		
		Result<InterfaMessageInfoString> result = new Result<>();
		String url = PropertiesUtil.getInstance().getValue("registerBathApp");
		String tId$ = Pattern.quote("${tId}");
		String appList$ = Pattern.quote("${appList}");
		String replacedurl = url.replaceAll(tId$, tenantId).replaceAll(appList$, app);
		//String replacedurl = url.replaceAll(tId$, tenantId);
		
		try {
			/* get请求方法 */
			InterfaMessageInfoString messageInfo = new InterfaMessageInfoString();

			JSONObject jsonObject = (JSONObject) HttpClientUtil.getUrlReturnJsonObject(replacedurl);
				//ApiCallUtils.putOrPostMethod(replacedurl, params, headers, method);
			messageInfo.setData(jsonObject.getString("data"));
			messageInfo.setMsg(jsonObject.getString("msg"));
			messageInfo.setStatus(Integer.valueOf(jsonObject.getString("status")));

			if (messageInfo.getStatus() == 200 || messageInfo.getStatus() == 110) {
				result.setSuccessData(messageInfo);
				return result;
			} else {
				_LOGGER.error("registerBathApp data http getfaile return code :" + messageInfo.getMsg() + " ");
				result.setErrorMessage("请求失败");
				result.setErrorCode(ErrorCodeNo.SYS006);
				return result;
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			_LOGGER.error("registerBathApp http error " + e + "");
			result.setErrorCode(ErrorCodeNo.SYS001);
			result.setErrorMessage("系统错误,请求失败");
			return result;
		}

	}
	
	@Override
	public Result<DiyApplyEntity> findById(String applyId) {
		Result<DiyApplyEntity> result = new Result<>();
		//DiyApplyEntity findById = diyApplyDao.findById(applyId);
		DiyApplyEntity findById = mysqlDiyApplyDao.findById(applyId);
		if (null == findById) {
			result.setErrorMessage("该应用不存在!");
			result.setErrorCode(ErrorCodeNo.SYS009);
		} else {
			result.setSuccessData(findById);
		}
		return result;
	}
	
}
