package cn.ce.platform_service.apis.service;

import java.util.List;

import cn.ce.platform_service.apis.entity.APIEntity;
import cn.ce.platform_service.apis.entity.ApiVersion;
import cn.ce.platform_service.common.Result;
import cn.ce.platform_service.page.Page;
import cn.ce.platform_service.users.entity.User;

/**
 * 
 * @ClassName: IAPIService
 * @Description: 接口服务 
 * @author dingjia@300.cn
 *
 */
public interface IAPIService {
	 /**
     * 新增API
     * @param api: 新增API对象
     */
    void addAPI(APIEntity api);
    
//    /**
//     * 判断某功能分组下是否有API
//     * @param groupId: 功能分组ID
//     * @return 功能分组下是否有API
//     */
    boolean haveAPIs(String groupId);
    
    /**
     * 删除API
     * @param id: 待删除的API ID
     * @return 
     */
    Result<String> delById(String id);
    
    /**
     * 根据ID查询API
     * @param id: API ID
     * @return
     */
    APIEntity findById(String id);
    
    /**
     * 修改API
     * @param api: 修改后的API对象
     */
    void updateAPI(APIEntity api);
    
    /**
     * 查询某功能分组的API集合，不分页
     * @param groupId: 功能分组ID
     * @return API集合
     */
    List<APIEntity>  getAPIs(String groupId);
    
    List<APIEntity> getApiListByIds(List<String> ids);
    
    /**
     * 查询某功能分组的API集合，分页
     * @param groupId: 功能分组id
     * @param currentPage: 当前页
     * @param pageSize: 每页记录数
     * @return
     */
    Page<APIEntity>  getAPIsAsPage(String groupId, int currentPage, int pageSize);
    
    /**
     * @Description: 根据分组和审核状态查询API
     * @param  groupId: 分组ID
     * @param  checkState: 审核状态
     * @return     返回审核API集合
     * @throws
     */
//    List<APIEntity> getGroupAPIs(String groupId, int checkState);
    
    /**
     * @Description: 根据分组和审核状态查询API
     * @param  groupId: 分组ID
     * @param  checkState: 审核状态
     * @param  state : 发布状态
     * @return     返回审核API集合Page
     * @throws
     */
    Page<APIEntity> getGroupAPIsAsPage(String groupId, int checkState, int state, int currentPage, int pageSize);
    
    
    /**
     * @Description: 审核分组的API
     * @param  groupId: 分组ID
     * @param  checkState: 审核状态
     * @param  checkmem: 审核备注
     * @return     返回审核API数量
     * @throws
     */
//    int reviewGroupAPIs(String groupId, int checkState, String checkmem);
    
    /**
     * @Description: 根据中文名称或描述进行模糊匹配（大小写不区分）
     * @return List<APIEntity>    返回类型
     * @throws
     */
//    Page<APIEntity> searchApis(String nameDesc, int currentPage, int pageSize);
    
    /**
     * @Description: 根据业务线和审核状态查询API
     * @param  categoryCode: 业务线编码
     * @param  checkState: 审核状态
     * @return     返回审核API集合
     * @throws
     */
//    List<APIEntity> getCategoryAPIs(String categoryCode, int checkState);
    
    /***
     * 根据对象及分页信息查询API分页集合
     * @param entity 实体查询对象
     * @param currentPage 
     * @param pageSize
     * @return
     */
    Page<APIEntity> findApisByEntity(APIEntity entity,int currentPage,int pageSize);

	List<APIEntity> findByField(String key, String value);
    
    /***
     * 根据版本信息查询api版本信息
     * @param apiVersion api版本
     * @return
     */
    ApiVersion findApiVersionByEntity(ApiVersion apiVersion);
    
    /***
     * 根据apiId更新所有版本为false
     * 
     */
    int updApiVersionByApiid(String apiid);

	Result<String> checkVersion(String apiId, String version);

	Result<String> checkApiEnName(String apiEnName, String appId);

	Result<String> modifyApi(APIEntity apientity);

	Result<String> checkApiChName(String apiChName, String appId);

	Result<String> apiVerify(String apiid, String username, String password);

	Result<String> publishAPI(User user, APIEntity apientity);

	Result<?> show(String apiId);

	Result<Page<APIEntity>> apiList(String apiId, String apiChName, String checkState, int currentPage, int pageSize);
    
}
