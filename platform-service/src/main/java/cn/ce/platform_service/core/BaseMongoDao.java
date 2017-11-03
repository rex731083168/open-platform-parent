package cn.ce.platform_service.core;

import java.util.List;
import java.util.Map;

import cn.ce.platform_service.common.page.Page;
import cn.ce.platform_service.core.bean.MongoDBWhereEntity;

/**
 * 
 * @ClassName: BaseMongoDAO
 * @Description: 数据库基本操作接口定义
 * @author dingjia@300.cn
 *
 * @param <T> 实例
 */
public interface BaseMongoDao<T> {
	
    public void add(T t);

    public int delById(String id, Class<?> entityclass);

    public int delByField(String field, String value, Class<?> entityclass);

    public void updateById(String id, T t);

    public int updateField(String id, String field, Object value, Class<?> entityclass);

    public T findById(String id, Class<?> entityclass);

    public T findOneByField(String field, String value, Class<?> entityclass);

    public T findOneByFieldsAnd(Map<String, Object> fields, Class<?> entityclass);

    public List findByField(String field, String value, Class<?> entityclass);
    
    public List findByField(String field, Object value, Class<?> entityclass);
    
    public List findByFields(Map<String, Object> fieldAndValues, Class<?> entityclass);

    public Page<T> findAsPage(int currentPage, int pageSize, Class<?> entityclass);

    public Page<T> findAsPageWithSort(String field, int currentPage, int pageSize, Class<?> entityclass);

    public Page<T> findAsPage(String field, String value, int currentPage, int pageSize, Class<?> entityclass);

    public int updateArrayElemById(String id, String field, String findItemName, Object findItemValue,
            String updateItemName, Object updateItemValue, Class<?> entityclass);

    public List<T> findInAsList(String field, List<?> list, String sort, Class<?> entityclass);
    
    
    
    public List<T> findListByWhereEntity(Map<String,MongoDBWhereEntity> whereMap , Class<?> entityclass);
    
    public Page<T> findPageListByWhereEntity(int currentPage, int pageSize,Map<String,MongoDBWhereEntity> whereMap , Class<?> entityclass);
    
}
