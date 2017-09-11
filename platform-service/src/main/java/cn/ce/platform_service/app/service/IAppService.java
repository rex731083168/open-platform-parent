package cn.ce.platform_service.app.service;

import java.util.List;
import java.util.Map;

import cn.ce.platform_service.app.entity.AppEntity;
import cn.ce.platform_service.common.Result;
import cn.ce.platform_service.page.Page;

/**
 * 
 * @ClassName: IAppService
 * @Description: 服务分组
 * @author dingjia@300.cn
 *
 */
public interface IAppService {
	/**
     * @Description: 添加应用
     * @param  app: 应用信息
     * @throws
     */
    public void addApp(AppEntity app);

    /**
     * @Description: 根据应用Id查询应用信息
     * @param  id: 应用id
     * @return     返回应用对象
     * @throws
     */
    public AppEntity findById(String id);

    /**
     * @Description: 修改应用信息
     * @param  app: 应用对象
     * @throws
     */
    public void modifyById(AppEntity app);

    /**
     * @Description: 查询所有应用
     * @return     返回应用集合
     * @throws
     */
    public List<AppEntity> getAll();

    /**
     * @Description: 根据用户查询应用列表（翻页）
     * @param  userId: 分组ID
     * @param  currentPage: 当前页
     * @param  pageSize: 每页显示数量
     * @return     返回应用集合
     * @throws
     */
    public Page<AppEntity> getAppList(String userId, int currentPage, int pageSize);
    
    
    /**
     * @Description: 根据App实体对象查询应用列表（翻页）
     * @param  appEntity: 服务分类实体
     * @param  currentPage: 当前页
     * @param  pageSize: 每页显示数量
     * @return     返回应用集合
     * @throws
     */
//    public Page<AppEntity> getAppListByDBWhere(Map<String,MongoDBWhereEntity> condition, int currentPage, int pageSize);
    public Page<AppEntity> getAppListByDBWhere(AppEntity appentity, int currentPage, int pageSize);
    

    /**
     * @Description: 删除应用
     * @param  id: 应用id
     * @throws
     */
    public void delById(String id);
    
    
    public AppEntity findAppByAppName(String appName);
    
    /***
     * 根据实体对象分页查询App集合
     * @param entity 查询的实体对象
     * @param currentPage 
     * @param pageSize
     * @return
     */
    Page<AppEntity> findAppsByEntity(AppEntity entity,int currentPage,int pageSize);
    
    /***
     * 根据app实体中appName及appKey检测是否已经存在
     * @param entity App实体
     * @return
     */
    Result<AppEntity> checkAppIsHave(AppEntity entity);
    
    List<AppEntity> findAppsByEntity(AppEntity entity);

	public Result<String> deleteById(String appId);

}
