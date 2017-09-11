package cn.ce.platform_service.app.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.ce.platform_service.app.dao.IAppDAO;
import cn.ce.platform_service.app.entity.AppEntity;
import cn.ce.platform_service.app.service.IAppService;
import cn.ce.platform_service.common.Result;
import cn.ce.platform_service.common.Status;
import cn.ce.platform_service.oauth.service.IOauthService;
import cn.ce.platform_service.page.Page;
import cn.ce.platform_service.page.PageContext;


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
    private static Logger logger = Logger.getLogger(AppServiceImpl.class);
    
    /** 功能分组数据库操作对象 */
    @Autowired
    private IAppDAO appDao;
    @Autowired
    private IOauthService oauthService;
    
//    @Autowired
//    private MongoTemplate mongoTemplate;
    

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
		AppEntity findAppByAppName = this.findAppByAppName(entity.getAppname());
		if(null == findAppByAppName){
			AppEntity app = new AppEntity();
			app.setAppkey(entity.getAppkey());
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
			logger.info("删除了"+i+"条app分组数据");
			result.setStatus(Status.SUCCESS);
			result.setMessage("删除成功");
			return result;
		}
	}
	


}
