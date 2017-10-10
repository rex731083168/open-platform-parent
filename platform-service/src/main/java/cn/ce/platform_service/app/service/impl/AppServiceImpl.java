package cn.ce.platform_service.app.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

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
import cn.ce.platform_service.app.dao.IAppDAO;
import cn.ce.platform_service.app.entity.AppEntity;
import cn.ce.platform_service.app.service.IAppService;
import cn.ce.platform_service.common.Constants;
import cn.ce.platform_service.common.ErrorCodeNo;
import cn.ce.platform_service.common.Result;
import cn.ce.platform_service.common.Status;
import cn.ce.platform_service.oauth.service.IOauthService;
import cn.ce.platform_service.page.Page;
import cn.ce.platform_service.page.PageContext;
import cn.ce.platform_service.users.entity.User;


/**
 * 
 * @ClassName: AppServiceImpl
 * @Description: 服务分组实现类型
 * @author dingjia@300.cn
 *
 */
@Service(value = "appService")
public class AppServiceImpl implements IAppService {

    /** 日志对象 */
    private static Logger _LOGGER = Logger.getLogger(AppServiceImpl.class);
    
    /** 功能分组数据库操作对象 */
    @Resource
    private IAppDAO appDao;
    @Resource
    private IOauthService oauthService;
    @Resource
    private IAPIService apiService;
    

    @Override
    public void addApp(AppEntity app) {
        appDao.addApp(app);
    }

    @Override
    public void modifyById(AppEntity app) {
        appDao.modifyById(app.getId(), app);
    }

    @Override
    public AppEntity findById(String id) {
        return appDao.findById(id);
    }

    @Override
    public List<AppEntity> getAll() {
        return appDao.getAll();
    }

  
    @Override
    public Page<AppEntity> getAppList(String userId, int currentPage, int pageSize) {
        PageContext.setCuurentPage(currentPage);
        PageContext.setPageSize(pageSize);
        return appDao.getAppList(userId);
    }

	@Override
	public void delById(String id) {
		appDao.delById(id);
		
	}

	@Override
	public AppEntity findAppByAppName(String appName) {
		return appDao.findAppByAppName(appName);
	}


	@Override
	public Page<AppEntity> getAppListByDBWhere(AppEntity appentity, int currentPage,
			int pageSize) {
		return appDao.findAppsByEntity(appentity, currentPage, pageSize);
	}

	@Override
	public Page<AppEntity> findAppsByEntity(AppEntity entity, int currentPage, int pageSize) {
		return appDao.findAppsByEntity(entity, currentPage, pageSize);
	}
	
	@Override
	public List<AppEntity> findAppsByEntity(AppEntity entity) {
		return appDao.findAppsByEntity(entity);
	}

	@Override
	public Result<AppEntity> checkAppIsHave(AppEntity entity) {
		boolean isTrue = false;
		Result<AppEntity> result = new Result<>();
		AppEntity findAppByAppName = this.findAppByAppName(entity.getAppName());
		if(null == findAppByAppName){
			AppEntity app = new AppEntity();
			app.setAppKey(entity.getAppKey());
			List<AppEntity> findAppsByEntity = findAppsByEntity(app);
			if(findAppsByEntity.isEmpty()){
				isTrue = true;
			}else{
				result.setStatus(Status.FAILED);
				result.setMessage("服务分类标识已存在!");
			}
		}else{
			result.setStatus(Status.FAILED);
			result.setMessage("服务分类名称已存在!");
		}
		
		if(isTrue){
			result.setStatus(Status.SUCCESS);
		}
		return result;
	}

	@Override
	public Result<String> deleteById(String appId) {
		
		Result<String> result = new Result<String>();
		
		if(StringUtils.isBlank(appId)){
			result.setMessage("appId不能为空");
			return result;
		}
		//查看当前app中是否有api已经被使用，如果已经被使用，就不能删除
		Map<String,Object> queryMap = new HashMap<String,Object>();
		queryMap.put("app_id", appId);
		queryMap.put("check_state", 2);
		int num = oauthService.findByFields(queryMap);
		
		if(num > 0){
			result.setMessage("当前分组已经有api被使用，无法删除该分组");
			return result;
		}else{
			//删除
			int i = appDao.delById(appId);
			_LOGGER.info("删除了"+i+"条app分组数据");
			result.setStatus(Status.SUCCESS);
			result.setMessage("删除成功");
			return result;
		}
	}

	@Override
	public Result<JSONObject> appList(HttpServletRequest request, HttpServletResponse response) {
		
		Result<JSONObject> result = new Result<JSONObject>();
		
		try {
			List<AppEntity> list = getAll();

			JSONArray jsonArray = new JSONArray();
			JSONObject data = new JSONObject();
			for (int i = 0; i < list.size(); i++) {
				JSONObject obj = new JSONObject();
				AppEntity app = list.get(i);
				obj.put("id", app.getId());
				obj.put("appname", app.getAppName());
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

	@Override
	public Result<String> addGroup(HttpSession session,AppEntity app) {
		
		Result<String> result = new Result<String>();
		try {
			Result<AppEntity> checkAppIsHave = checkAppIsHave(app);
			
			if (Status.SUCCESS == checkAppIsHave.getStatus()) {
				User user = (User) session.getAttribute(Constants.SES_LOGIN_USER);
				String uuid = UUID.randomUUID().toString().replace("-", "");
				app.setId(uuid);
				app.setCreateDate(new Date());
				if (user.getUserType() == 0) {
					app.setCheckState(2); // 后台系统添加服务分类默认审核通过
				} else {
					app.setCheckState(0);
				}
				app.setUserId(user.getId());
				app.setUserName(user.getUserName());

				// appKey不能以/开头和结尾
				if (app.getAppKey().startsWith("/") || app.getAppKey().endsWith("/")) {
					result.setErrorMessage("appKey不能以/开头和/结尾");
					return result;
				}
				addApp(app);
				result.setSuccessMessage("");
				return result;
			} else{
				// TODO
				result.setErrorMessage(checkAppIsHave.getMessage());
			}
			return result;
		} catch (Exception e) {
			// TODO
			_LOGGER.error("error happens when add group", e);
			result.setErrorMessage("");
			return result;
		}
	}

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

	@Override
	public Result<String> modifyGroup(AppEntity app) {
		Result<String> result = new Result<String>();
		try {
			AppEntity appAfter = findById(app.getId());
			if (null == appAfter) {
				result.setErrorMessage("当前分组不存在", ErrorCodeNo.SYS006);
			} else {
				app.setNeqId(app.getId());// 查询非当前修改数据进行判断
				List<AppEntity> findAppsByEntity = findAppsByEntity(app);
				if (findAppsByEntity.isEmpty()) {
					appAfter.setAppName(app.getAppName().trim());
					appAfter.setAppKey(app.getAppKey().trim());
					modifyById(appAfter);
					result.setSuccessMessage("");
				} else {
					result.setErrorMessage("");
				}
			}
			return result;
		} catch (Exception e) {
			_LOGGER.error("error happeen when execute app service modify group",e);
			result.setErrorMessage("");
			return result;
		}
	}

	@Override
	public Result<Page<AppEntity>> groupList(String userId, int currentPage, int pageSize) {
		Result<Page<AppEntity>> result = new Result<Page<AppEntity>>();
		try {
			AppEntity entity = new AppEntity();
			entity.setUserId(userId);
			Page<AppEntity> ds = findAppsByEntity(entity, currentPage, pageSize);
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
			AppEntity app = findById(id);
			app.setCheckState(1);
			modifyById(app);
			
			result.setSuccessMessage("");
		} catch (Exception e) {
			_LOGGER.error("error happens when submit verify",e);
			
			result.setErrorMessage("");
			
		}
		return result;
	}

	@Override
	public Result<String> addGroup1(HttpSession session,AppEntity app) {
		Result<String> result = new Result<String>();
		try {
			
			Result<AppEntity> checkAppIsHave = checkAppIsHave(app);
			
			if (Status.SUCCESS == checkAppIsHave.getStatus()) {
				AdminEntity user = (AdminEntity) session.getAttribute(Constants.SES_LOGIN_USER);
				String uuid = UUID.randomUUID().toString().replace("-", "");
				app.setId(uuid);
				app.setCreateDate(new Date());
				app.setCheckState(2); //后台系统添加服务分类默认审核通过
				app.setUserId(user.getId());
				app.setUserName(user.getUserName());
				addApp(app);
				result.setSuccessData(uuid);
			}else{
				// TODO
				result.setErrorMessage("");
			}
		} catch (Exception e) {
			_LOGGER.info("error happens when execute add group",e);
			result.setErrorMessage("");
		}
		return result;
	}

	@Override
	public Result<String> deleteGroup(String id) {
		
		Result<String> result =new Result<String>();
		
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
			_LOGGER.info("error happens when execute admin logout",e);
			result.setErrorMessage("");
			return result;
		}
	}

	@Override
	public Result<String> modifyGroup1(AppEntity app) {
		Result<String> result = new Result<String>();
		try {
			
			AppEntity appAfter = findById(app.getId());
			if(null == appAfter){
				result.setErrorMessage("当前分组不存在", ErrorCodeNo.SYS006);
			}else{
				app.setNeqId(app.getId());//查询非当前修改数据进行判断
				List<AppEntity> findAppsByEntity = findAppsByEntity(app);
				if (findAppsByEntity.isEmpty()) {
					appAfter.setAppName(app.getAppName().trim());
					appAfter.setAppKey(app.getAppKey().trim());
					modifyById(appAfter);
					result.setSuccessMessage("");
				}else{
					result.setErrorMessage("分组名称或分组key不可重复！", ErrorCodeNo.SYS010);
				}
			}
		} catch (Exception e) {
			_LOGGER.info("error happens when execute modify app group ",e);
			result.setErrorMessage("");
		}
		return result;
	}

	@Override
	public Result<Page<AppEntity>> groupList1(String appName, String checkState, int currentPage, int pageSize) {
		Result<Page<AppEntity>> result = new Result<Page<AppEntity>>();

		try {
//			Map<String,Object> condMap = new HashMap<String, Object>(2);
			AppEntity appParam = new AppEntity();
			
			if(StringUtils.isNotBlank(appName)){
				appParam.setAppName(appName);
			}
			
			if(StringUtils.isNotBlank(checkState)){
				appParam.setCheckState(Integer.valueOf(checkState));
			}
			
			Page<AppEntity> ds = getAppListByDBWhere(appParam, currentPage, pageSize);

			result.setSuccessData(ds);
		} catch (Exception e) {
			_LOGGER.info("error happens when execute group list1",e);
			result.setErrorMessage("");
		}
		return result;
	}

	@Override
	public Result<String> auditGroup(String id, int checkState, String remark) {
		Result<String> result = new Result<String>();
		try {
			AppEntity app = findById(id);
			if(null == app){
				result.setErrorMessage("当前id不存在", ErrorCodeNo.SYS006);
			}else{
				app.setCheckState(checkState);
				if(checkState == 3 && StringUtils.isNotBlank(remark)){
					app.setCheckMem(remark);
				}
				modifyById(app);
				result.setSuccessMessage("");
			}
		} catch (Exception e) {
			_LOGGER.info("error happens when execute audit group",e);
			result.setErrorMessage("");
		}
		return result;
	}
}
