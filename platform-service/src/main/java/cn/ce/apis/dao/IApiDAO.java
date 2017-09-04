package cn.ce.apis.dao;

import java.util.List;
import java.util.Map;

import org.springframework.data.mongodb.core.query.Query;

import cn.ce.apis.entity.APIEntity;
import cn.ce.apis.entity.ApiVersion;
import cn.ce.common.Result;
import cn.ce.page.Page;

/**
 * 
 * @ClassName: IApiDAO
 * @Description: 接口表操作数据库接口
 * @author dingjia@300.cn
 *
 */
public interface IApiDAO {
	
	 /**
     * 查询某功能分组的API集合，不分页
     * @param groupId: 功能分组ID
     * @return API集合
     */
    List<APIEntity> getAPIs(String groupId);
    
    /**
     * 查询某功能分组的API集合，分页
     * @param groupId: 功能分组ID
     * @return API分页数据
     */
    Page<APIEntity> getAPIsAsPage(String groupId);
    
    /**
     * @Description: 根据分组和审核状态查询API
     * @param  groupId: 分组ID
     * @param  checkState: 审核状态
     * @return     返回审核API集合
     * @throws
     */
    List<APIEntity> getAPIs(String groupId, int checkState);
    
    /**
     * @Description: 根据业务线和审核状态查询API
     * @param  categoryId: 业务线ID
     * @param  checkState: 审核状态
     * @return     返回审核API集合
     * @throws
     */
    List<APIEntity> getCategoryAPIs(String categoryCode, int checkState);
    
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
     * @Description: 根据审核状态查询API
     * @param  checkState: 审核状态
     * @return     返回审核API集合
     * @throws
     */
    List<APIEntity> getAPIs(int checkState);
    
    /**
     * 新增API
     * @param api 新增的API对象
     */
    void addAPI(APIEntity api);
    
    /**
     * 查询功能分组下唯一API
     * @param groupId: 功能分组ID
     * @return API
     */
    APIEntity findOne(String groupId);
    
    /**
     * 根据英文名查询API
     * @param enName: API英文名
     * @return API对象
     */
    APIEntity findOneByEnName(String enName);
    
    /**
     * 根据ID查询API
     * @param id: API ID
     * @return API对象
     */
    APIEntity findOneById(String id);
    
    /**
     * 删除API
     * @param id: 待删除的API对象
     * @return 操作成功记录数
     */
    int delApi(String id);
    
    /**
     * 修改API
     * @param api: 修改后的API对象
     * @return 操作成功记录数
     */
    int updateAPI(APIEntity api);
    
    /**
     * @Description: 根据中文名称或描述进行模糊匹配(大小写不区分)
     * @return List<APIEntity>    返回类型
     * @throws
     */
    Page<APIEntity> searchApis(String nameDesc, int currentPage, int pageSize);
    
    APIEntity findByApiCnName(String cnname);
    
    /***
     * 根据实体对象分页查询API集合
     * @param entity 查询的实体对象
     * @param currentPage 
     * @param pageSize
     * @return
     */
    Page<APIEntity> findApisByEntity(APIEntity entity,int currentPage,int pageSize);

	List<APIEntity> findByField(String key, String value);
	
	List<APIEntity> findApiListByIds(List<String> ids);
    
    public List<APIEntity> findApiListByQuery(Query query);

	APIEntity findOneByFields(Map<String, Object> map);

	boolean modifyApi(APIEntity apientity);
    
}  
