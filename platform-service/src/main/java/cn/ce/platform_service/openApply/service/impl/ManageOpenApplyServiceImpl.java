package cn.ce.platform_service.openApply.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.regex.Pattern;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import cn.ce.platform_service.admin.entity.AdminEntity;
import cn.ce.platform_service.apis.service.IAPIService;
import cn.ce.platform_service.common.Constants;
import cn.ce.platform_service.common.ErrorCodeNo;
import cn.ce.platform_service.common.HttpClientUtil;
import cn.ce.platform_service.common.Result;
import cn.ce.platform_service.common.Status;
import cn.ce.platform_service.common.page.Page;
import cn.ce.platform_service.common.page.PageContext;
import cn.ce.platform_service.diyApply.entity.interfaceMessageInfo.InterfaMessageInfoString;
import cn.ce.platform_service.oauth.service.IOauthService;
import cn.ce.platform_service.openApply.dao.IOpenApplyDao;
import cn.ce.platform_service.openApply.entity.OpenApplyEntity;
import cn.ce.platform_service.openApply.service.IManageOpenApplyService;
import cn.ce.platform_service.util.PropertiesUtil;

/**
 * @ClassName: openApplyServiceImpl
 * @Description:TODO(这里用一句话描述这个类的作用)
 * @author: author
 * @date: 2017年10月11日 下午5:24:58
 * @Copyright: 2017 中企动力科技股份有限公司 © 1999-2017 300.cn All Rights Reserved
 */
@Service(value = "manageOpenApplyService")
public class ManageOpenApplyServiceImpl implements IManageOpenApplyService {

	/** 日志对象 */
	private static Logger _LOGGER = Logger.getLogger(ManageOpenApplyServiceImpl.class);

	/** 功能分组数据库操作对象 */
	@Resource
	private IOpenApplyDao openApplyDao;
	@Resource
	private IOauthService oauthService;
	@Resource
	private IAPIService apiService;

	public void addApp(OpenApplyEntity app) {
		openApplyDao.addApp(app);
	}

	@Override
	public void modifyById(OpenApplyEntity app) {
		openApplyDao.modifyById(app.getId(), app);
	}

	@Override
	public OpenApplyEntity findById(String id) {
		return openApplyDao.findById(id);
	}

	@Override
	public List<OpenApplyEntity> getAll() {
		return openApplyDao.getAll();
	}

	@Override
	public Page<OpenApplyEntity> getAppList(String userId, int currentPage, int pageSize) {
		PageContext.setCuurentPage(currentPage);
		PageContext.setPageSize(pageSize);
		return openApplyDao.getAppList(userId);
	}

	@Override
	public void delById(String id) {
		openApplyDao.delById(id);

	}

	@Override
	public OpenApplyEntity findAppByAppName(String appName) {
		return openApplyDao.findAppByAppName(appName);
	}

	@Override
	public Page<OpenApplyEntity> getAppListByDBWhere(OpenApplyEntity appentity, int currentPage, int pageSize) {
		return openApplyDao.findAppsByEntity(appentity, currentPage, pageSize);
	}

	@Override
	public Page<OpenApplyEntity> findAppsByEntity(OpenApplyEntity entity, int currentPage, int pageSize) {
		return openApplyDao.findAppsByEntity(entity, currentPage, pageSize);
	}

	@Override
	public List<OpenApplyEntity> findAppsByEntity(OpenApplyEntity entity) {
		return openApplyDao.findAppsByEntity(entity);
	}

	@Override
	public Result<OpenApplyEntity> checkAppIsHave(OpenApplyEntity entity) {
		boolean isTrue = false;
		Result<OpenApplyEntity> result = new Result<>();
		OpenApplyEntity findAppByAppName = this.findAppByAppName(entity.getApplyName());
		if (null == findAppByAppName) {
			OpenApplyEntity app = new OpenApplyEntity();
			app.setApplyKey(entity.getApplyKey());
			List<OpenApplyEntity> findAppsByEntity = findAppsByEntity(app);
			if (findAppsByEntity.isEmpty()) {
				isTrue = true;
			} else {
				result.setStatus(Status.FAILED);
				result.setMessage("服务分类标识已存在!");
			}
		} else {
			result.setStatus(Status.FAILED);
			result.setMessage("服务分类名称已存在!");
		}

		if (isTrue) {
			result.setStatus(Status.SUCCESS);
		}
		return result;
	}

	@Override
	public Result<String> deleteById(String appId) {

		Result<String> result = new Result<String>();

		if (StringUtils.isBlank(appId)) {
			result.setMessage("appId不能为空");
			return result;
		}
		// 查看当前app中是否有api已经被使用，如果已经被使用，就不能删除
		Map<String, Object> queryMap = new HashMap<String, Object>();
		queryMap.put("app_id", appId);
		queryMap.put("check_state", 2);
		int num = oauthService.findByFields(queryMap);

		if (num > 0) {
			result.setMessage("当前分组已经有api被使用，无法删除该分组");
			return result;
		} else {
			// 删除
			int i = openApplyDao.delById(appId);
			_LOGGER.info("删除了" + i + "条app分组数据");
			result.setStatus(Status.SUCCESS);
			result.setMessage("删除成功");
			return result;
		}
	}

	@Override
	public Result<JSONObject> appList(HttpServletRequest request, HttpServletResponse response) {

		Result<JSONObject> result = new Result<JSONObject>();

		try {
			List<OpenApplyEntity> list = getAll();

			JSONArray jsonArray = new JSONArray();
			JSONObject data = new JSONObject();
			for (int i = 0; i < list.size(); i++) {
				JSONObject obj = new JSONObject();
				OpenApplyEntity app = list.get(i);
				obj.put("id", app.getId());
				obj.put("appname", app.getApplyName());
				jsonArray.put(obj);
			}
			data.put("items", jsonArray);
			result.setSuccessData(data);
			return result;
		} catch (Exception e) {
			_LOGGER.info("error happens when get app list", e);
			result.setErrorMessage("");
			return result;
		}
	}

	// @Override
	// public Result<?> addApply(HttpSession session,OpenApplyEntity apply) {
	//
	// Result<String> result = new Result<String>();
	// try {
	// Result<OpenApplyEntity> checkAppIsHave = checkAppIsHave(apply);
	//
	// if (Status.SUCCESS == checkAppIsHave.getStatus()) {
	// User user = (User) session. getAttribute(Constants.SES_LOGIN_USER);
	// String uuid = UUID.randomUUID().toString().replace("-", "");
	// apply.setId(uuid);
	// apply.setCreateDate(new Date());
	// if (user.getUserType() == 0) {
	// apply.setCheckState(2); // 后台系统添加服务分类默认审核通过
	// } else {
	// apply.setCheckState(0);
	// }
	// apply.setUserId(user.getId());
	// apply.setUserName(user.getUserName());
	//
	// // appKey不能以/开头和结尾
	// if (apply.getApplyKey().startsWith("/") || apply.getApplyKey().endsWith("/"))
	// {
	// result.setErrorMessage("appKey不能以/开头和/结尾");
	// return result;
	// }
	// addApp(apply);
	// result.setSuccessMessage("");
	// return result;
	// } else{
	// // TODO
	// result.setErrorMessage(checkAppIsHave.getMessage());
	// }
	// return result;
	// } catch (Exception e) {
	// // TODO
	// _LOGGER.error("error happens when add group", e);
	// result.setErrorMessage("");
	// return result;
	// }
	// }

	@Override
	public Result<String> delGroup(String appId) {
		Result<String> result = new Result<String>();
		try {
			/** 删除前要求分组内无API存在！ */
			// 这是丁佳一个大坑
			if (apiService.haveAPIs(appId)) {
				result.setErrorMessage("当前服务分组中存在API接口定义!", ErrorCodeNo.SYS007);
			}
			delById(appId);
			result.setSuccessData("");
		} catch (Exception e) {

			// TODO
			_LOGGER.error("error happens when del group", e);
			result.setErrorMessage("");
		}
		return result;
	}

	// @Override
	// public Result<String> modifyGroup(OpenApplyEntity app) {
	// Result<String> result = new Result<String>();
	// try {
	// OpenApplyEntity appAfter = findById(app.getId());
	// if (null == appAfter) {
	// result.setErrorMessage("当前分组不存在", ErrorCodeNo.SYS006);
	// } else {
	// app.setNeqId(app.getId());// 查询非当前修改数据进行判断
	// List<OpenApplyEntity> findAppsByEntity = findAppsByEntity(app);
	// if (findAppsByEntity.isEmpty()) {
	// appAfter.setApplyName(app.getApplyName().trim());
	// appAfter.setApplyKey(app.getApplyKey().trim());
	// modifyById(appAfter);
	// result.setSuccessMessage("");
	// } else {
	// result.setErrorMessage("");
	// }
	// }
	// return result;
	// } catch (Exception e) {
	// _LOGGER.error("error happeen when execute app service modify group",e);
	// result.setErrorMessage("");
	// return result;
	// }
	// }

	@Override
	public Result<Page<OpenApplyEntity>> groupList(String userId, int currentPage, int pageSize) {
		Result<Page<OpenApplyEntity>> result = new Result<Page<OpenApplyEntity>>();
		try {
			OpenApplyEntity entity = new OpenApplyEntity();
			entity.setUserId(userId);
			Page<OpenApplyEntity> ds = findAppsByEntity(entity, currentPage, pageSize);
			result.setSuccessData(ds);

		} catch (Exception e) {
			_LOGGER.info("error happens when group list");
			result.setErrorMessage("");
		}

		return result;
	}

	@Override
	public Result<String> submitVerify(String id) {
		Result<String> result = new Result<String>();
		try {
			OpenApplyEntity app = findById(id);
			app.setCheckState(1);
			modifyById(app);

			result.setSuccessMessage("");
		} catch (Exception e) {
			_LOGGER.error("error happens when submit verify", e);

			result.setErrorMessage("");

		}
		return result;
	}

	@Override
	public Result<String> addGroup1(HttpSession session, OpenApplyEntity app) {
		Result<String> result = new Result<String>();
		try {

			Result<OpenApplyEntity> checkAppIsHave = checkAppIsHave(app);

			if (Status.SUCCESS == checkAppIsHave.getStatus()) {
				AdminEntity user = (AdminEntity) session.getAttribute(Constants.SES_LOGIN_USER);
				String uuid = UUID.randomUUID().toString().replace("-", "");
				app.setId(uuid);
				app.setCreateDate(new Date());
				app.setCheckState(2); // 后台系统添加服务分类默认审核通过
				app.setUserId(user.getId());
				app.setUserName(user.getUserName());
				addApp(app);
				result.setSuccessData(uuid);
			} else {
				// TODO
				result.setErrorMessage("");
			}
		} catch (Exception e) {
			_LOGGER.info("error happens when execute add group", e);
			result.setErrorMessage("");
		}
		return result;
	}

	@Override
	public Result<String> deleteGroup(String id) {

		Result<String> result = new Result<String>();

		try {
			/** 删除前要求分组内无API存在！ */
			// TODO 这是丁佳一个大坑
			if (apiService.haveAPIs(id)) {
				result.setErrorMessage("当前服务分组中存在API接口定义!", ErrorCodeNo.SYS009);
			}
			delById(id);
			result.setSuccessMessage("");
			return result;
		} catch (Exception e) {
			_LOGGER.info("error happens when execute admin log out", e);
			result.setErrorMessage("");
			return result;
		}
	}

	// @Override
	// public Result<String> modifyGroup1(OpenApplyEntity app) {
	// Result<String> result = new Result<String>();
	// try {
	//
	// OpenApplyEntity appAfter = findById(app.getId());
	// if(null == appAfter){
	// result.setErrorMessage("当前分组不存在", ErrorCodeNo.SYS006);
	// }else{
	// app.setNeqId(app.getId());//查询非当前修改数据进行判断
	// List<OpenApplyEntity> findAppsByEntity = findAppsByEntity(app);
	// if (findAppsByEntity.isEmpty()) {
	// appAfter.setApplyName(app.getApplyName().trim());
	// appAfter.setApplyKey(app.getApplyKey().trim());
	// modifyById(appAfter);
	// result.setSuccessMessage("");
	// }else{
	// result.setErrorMessage("分组名称或分组key不可重复！", ErrorCodeNo.SYS010);
	// }
	// }
	// } catch (Exception e) {
	// _LOGGER.info("error happens when execute modify app group ",e);
	// result.setErrorMessage("");
	// }
	// return result;
	// }

	@Override
	public Result<Page<OpenApplyEntity>> groupList1(String appName, String checkState, int currentPage, int pageSize) {
		Result<Page<OpenApplyEntity>> result = new Result<Page<OpenApplyEntity>>();

		try {
			// Map<String,Object> condMap = new HashMap<String, Object>(2);
			OpenApplyEntity appParam = new OpenApplyEntity();

			if (StringUtils.isNotBlank(appName)) {
				appParam.setApplyName(appName);
			}

			if (StringUtils.isNotBlank(checkState)) {
				appParam.setCheckState(Integer.valueOf(checkState));
			}

			Page<OpenApplyEntity> ds = getAppListByDBWhere(appParam, currentPage, pageSize);

			result.setSuccessData(ds);
		} catch (Exception e) {
			_LOGGER.info("error happens when execute group list1", e);
			result.setErrorMessage("");
		}
		return result;
	}

	@Override
	public Result<String> modifyGroup1(OpenApplyEntity app) {
		Result<String> result = new Result<String>();
		try {

			OpenApplyEntity appAfter = findById(app.getId());
			if (null == appAfter) {
				result.setErrorMessage("当前分组不存在", ErrorCodeNo.SYS006);
			} else {
				app.setNeqId(app.getId());// 查询非当前修改数据进行判断
				List<OpenApplyEntity> findAppsByEntity = findAppsByEntity(app);
				if (findAppsByEntity.isEmpty()) {
					appAfter.setApplyName(app.getApplyName().trim());
					appAfter.setApplyKey(app.getApplyKey().trim());
					modifyById(appAfter);
					result.setSuccessMessage("");
				} else {
					result.setErrorMessage("分组名称或分组key不可重复！", ErrorCodeNo.SYS010);
				}
			}
		} catch (Exception e) {
			_LOGGER.info("error happens when execute modify app group ", e);
			result.setErrorMessage("");
		}
		return result;
	}

	@Override
	public Result<Page<OpenApplyEntity>> groupList1(String appName, String userName, String enterpriseName,
			String checkState, int currentPage, int pageSize) {
		Result<Page<OpenApplyEntity>> result = new Result<Page<OpenApplyEntity>>();

		try {
			// Map<String,Object> condMap = new HashMap<String, Object>(2);
			OpenApplyEntity appParam = new OpenApplyEntity();

			if (StringUtils.isNotBlank(appName)) {
				appParam.setApplyName(appName);
			}

			if (StringUtils.isNotBlank(userName)) {
				appParam.setUserName(userName);
			}
			if (StringUtils.isNotBlank(enterpriseName)) {
				appParam.setEnterpriseName(enterpriseName);
			}

			if (StringUtils.isNotBlank(checkState)) {
				appParam.setCheckState(Integer.valueOf(checkState));
			}

			Page<OpenApplyEntity> ds = getAppListByDBWhere(appParam, currentPage, pageSize);

			result.setSuccessData(ds);
		} catch (Exception e) {
			_LOGGER.info("error happens when execute group list1", e);
			result.setErrorMessage("");
		}
		return result;
	}

	@Override
	public Result<String> auditGroup(String id, int checkState, String remark) {
		Result<String> result = new Result<String>();
		try {
			OpenApplyEntity app = findById(id);
			if (null == app) {
				result.setErrorMessage("当前id不存在", ErrorCodeNo.SYS006);
			} else {
				app.setCheckState(checkState);
				if (checkState == 3 && StringUtils.isNotBlank(remark)) {
					app.setCheckMem(remark);
				}
				modifyById(app);
				result.setSuccessMessage("");
			}
		} catch (Exception e) {
			_LOGGER.info("error happens when execute audit group", e);
			result.setErrorMessage("");
		}
		return result;
	}

	@Override
	public Result<String> batchUpdate(List<String> ids) {
		// TODO Auto-generated method stub
		Result<String> result = new Result<String>();
		String message = openApplyDao.bathUpdateByid(ids);
		_LOGGER.info("bachUpdate message " + message + " count");
		result.setSuccessMessage("成功审核:" + message + "条");
		return result;
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
			InterfaMessageInfoString messageInfo = (InterfaMessageInfoString) HttpClientUtil.getUrlReturnObject(replacedurl,
					InterfaMessageInfoString.class, null);

			/* 无接口时的测试方法 */
			// InterfaMessageInfoString messageInfo = (InterfaMessageInfoString)
			// testgetUrlReturnObject("saveOrUpdateApps",
			// replacedurl, InterfaMessageInfoString.class, null);
			if (messageInfo.getStatus() == 200 || messageInfo.getStatus() == 110) {
				result.setData(messageInfo);
				result.setSuccessMessage("");
				return result;
			} else {
				_LOGGER.error("saveOrUpdateApps data http getfaile return code :" + messageInfo.getMsg() + " ");
				result.setErrorCode(ErrorCodeNo.SYS006);
				return result;
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			_LOGGER.error("saveOrUpdateApps http error " + e + "");
			result.setErrorCode(ErrorCodeNo.SYS001);
			result.setErrorMessage("请求失败");
			return result;
		}
	}
	
	
	
}
