package cn.ce.platform_service.core;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import com.mongodb.BasicDBObject;
import com.mongodb.CommandResult;
import com.mongodb.DB;
import com.mongodb.Mongo;
import com.mongodb.WriteResult;

import cn.ce.platform_service.common.Constants;
import cn.ce.platform_service.core.bean.ConditionEnum;
import cn.ce.platform_service.core.bean.MongoDBWhereEntity;
import cn.ce.platform_service.page.Page;

/**
 * 
 * @ClassName: AbstractBaseMongoDAO
 * @Description: 基类定义
 * @author dingjia@300.cn
 * 
 * @param <T> 具体实例 
 */
public abstract class AbstractBaseMongoDao<T> implements BaseMongoDao<T> {

    /** mongodb操作对象 */
    @Autowired
    protected MongoTemplate mongoTemplate;

    public void add(T t) {
        mongoTemplate.insert(t);
    }

    public T findById(String id, Class<?> entityclass) {
        Query query = new Query();
        query.addCriteria(Criteria.where("id").is(id));
        return (T) mongoTemplate.findOne(query, entityclass);
    }

    public void updateById(String id, T t) {
        mongoTemplate.save(t);
    }

    public int updateField(String id, String field, Object value, Class<?> entityclass) {
        Query query = new Query();
        query.addCriteria(Criteria.where("id").is(id));
        WriteResult rt = mongoTemplate.updateFirst(query, Update.update(field, value), entityclass);
        return rt.getN();
    }

    public int updateField(Map<String, Object> queries, Map<String, Object> updates, Class<?> entityclass) {
        Update update = this.getUpdate(null, updates);
        if (update == null) {
            return 0;
        }

        Criteria c = this.getIsCriteria(queries);
        Query query = new Query(c);

        WriteResult rt = mongoTemplate.updateMulti(query, update, entityclass);
        return rt.getN();
    }

    public int delById(String id, Class<?> entityclass) {
        Query query = new Query();
        query.addCriteria(Criteria.where("id").is(id));
        WriteResult rt = mongoTemplate.remove(query, entityclass);
        return rt.getN();
    }

    public Page<T> findAsPage(int currentPage, int pageSize, Class<?> entityclass) {
        Query query = new Query();
        return this.findAsPage(query, currentPage, pageSize, entityclass);
    }

    public Page<T> findAsPageWithSort(String field, int currentPage, int pageSize, Class<?> entityclass) {
        Query query = new Query();
        long totalnumber = mongoTemplate.count(query, entityclass);
        int skip = (currentPage - 1) * pageSize;
        query.skip(skip).limit(pageSize);
        query.with(new Sort(Direction.DESC, field));
        List<?> datas = mongoTemplate.find(query, entityclass);
        Page<T> page = new Page<T>(currentPage, (int) totalnumber, pageSize);
        page.setTotalNumber((int) totalnumber);
        page.setCurrentPage(currentPage);
        page.build(datas);
        return page;
    }

    public Page<T> findAsPage(String field, String value, int currentPage, int pageSize, Class<?> entityclass) {
        Query query = new Query();
        query.addCriteria(Criteria.where(field).is(value));
        return this.findAsPage(query, currentPage, pageSize, entityclass);
    }
    /**
     * @Description: 根据多个属性查找数据
     * @return Page<T>    返回类型
     * @throws
     */
    public Page<T> findByFieldsAsPage(Map<String, Object> fieldAndValues, int currentPage, int pageSize, Class<?> entityclass) {
        Criteria criteria = this.getAndCriteria(fieldAndValues);
        /*List<Criteria> criteriaLst = new ArrayList<Criteria>();
        Iterator<String> ite = fieldAndValues.keySet().iterator();
        while (ite.hasNext()) {
            String field = ite.next();
            Object val = fieldAndValues.get(field);
            if (criteria == null) {
                criteria = Criteria.where(field).is(val);
            } else {
                criteriaLst.add(Criteria.where(field).is(val));
            }
        }
        if (criteria != null && !criteriaLst.isEmpty()) {
            Criteria[] criterias = new Criteria[criteriaLst.size()];
            for (int i = 0; i < criteriaLst.size(); i++) {
            	criterias[i] = criteriaLst.get(i);
            }
            criteria.andOperator(criterias);
        }*/
        if(criteria == null){
        	criteria = new Criteria();
        }
        Query query = new Query(criteria);
        return this.findAsPage(query, currentPage, pageSize, entityclass);
    }
    
    public Page<T> findLikeAsPage(String field, String value, Integer sortType, String sortField, 
    		int currentPage, int pageSize, Class<?> entityclass) {
        Query query = new Query();
        if (StringUtils.isNotBlank(value)) {
            query.addCriteria(Criteria.where(field).regex(value));
        }
        this.querySort(query, sortType, sortField);
        return this.findAsPage(query, currentPage, pageSize, entityclass);
    }

    public Page<T> findNeAsPage(String field, String neVal, int currentPage, int pageSize, Class<?> entityclass) {
        Query query = new Query();
        query.addCriteria(Criteria.where(field).ne(neVal));
        return this.findAsPage(query, currentPage, pageSize, entityclass);
    }

    public Page<T> findLikeAndIsAsPage(String field, String value, String isField, Object isVal,
    		Integer sortType, String sortField, 
    		int currentPage, int pageSize, Class<?> entityclass) {
        Criteria c = Criteria.where(isField).in(isVal);
        if (StringUtils.isNotBlank(value)) {
            c.andOperator(Criteria.where(field).regex(value));
        }
        Query query = new Query(c);
        this.querySort(query, sortType, sortField);
        return this.findAsPage(query, currentPage, pageSize, entityclass);
    }
    
    /**用于按名字或内容搜索 api 接口*/
    public Page<T> findLikeOrLikeAsPage(String field, String value, String secondField, String secondVal,
            Integer sortType, String sortField, int currentPage, int pageSize, Class<?> entityclass) {
        //模糊匹配，不区分大小写
        Pattern valuePat = Pattern.compile("^.*" + value + ".*$", Pattern.CASE_INSENSITIVE);
        Criteria c = new Criteria();
        Query query = new Query();
        query.addCriteria(c.orOperator(Criteria.where(field).regex(valuePat), 
                             Criteria.where(secondField).regex(valuePat)));
        
        this.querySort(query, sortType, sortField);
        return this.findAsPage(query, currentPage, pageSize, entityclass);
    }

    public Page<T> findLikeAndInAsPage(String field, String value, String inField, Collection<?> inVal,
    		Integer sortType, String sortField,
    		int currentPage, int pageSize, Class<?> entityclass) {
        if (inVal == null) {
            Page<T> page = new Page<T>(currentPage, 0, pageSize);
            page.setItems(new ArrayList<T>());
            page.setTotalPage(1);
            return page;
        }
        Criteria c = Criteria.where(inField).in(inVal);
        if (StringUtils.isNotBlank(value)) {
            c.andOperator(Criteria.where(field).regex(value));
        }
        Query query = new Query(c);
        this.querySort(query, sortType, sortField);
        return this.findAsPage(query, currentPage, pageSize, entityclass);
    }

    public Page<T> findLikeAndNotInAsPage(String field, String value, 
    		String ninField, Collection<?> ninVal, Integer sortType, String sortField,
    		int currentPage, int pageSize, Class<?> entityclass) {
        Criteria c = null;
        if (ninVal != null) {
            c = Criteria.where(ninField).nin(ninVal);
        }
        if (StringUtils.isNotBlank(value)) {
            Criteria likeC = Criteria.where(field).regex(value);
            if (c != null) {
                c.andOperator(likeC);
            } else {
                c = likeC;
            }
        }
        
        Query query = null;
        if (c == null) {
            query = new Query();
        } else {
            query = new Query(c);
        }
        this.querySort(query, sortType, sortField);
        return this.findAsPage(query, currentPage, pageSize, entityclass);
    }

    public Page<T> findEqAndInAsPage(Map<String, Object> eqFields, Map<String, Collection<?>> inFields, int currentPage, int pageSize, Class<?> entityclass) {
        Criteria c = this.getIsCriteria(eqFields);
        c = this.getInCriteria(c, inFields);
        if (c == null) {
            Page<T> page = new Page<T>(currentPage, 0, pageSize);
            page.setTotalPage(1);
            page.setItems(new ArrayList<T>());
            return page;
        }
        Query query = new Query();
        query.addCriteria(c);
        return this.findAsPage(query, currentPage, pageSize, entityclass);
    }

    protected Page<T> findAsPage(Query query, int currentPage, int pageSize, Class<?> entityclass) {
        long totalnumber = mongoTemplate.count(query, entityclass);
        int skip = (currentPage - 1) * pageSize;
        query.skip(skip).limit(pageSize);
        List<?> datas = mongoTemplate.find(query, entityclass);
        Page<T> page = new Page<T>(currentPage, (int) totalnumber, pageSize);
        page.setTotalNumber((int) totalnumber);
        page.setCurrentPage(currentPage);
        page.build(datas);
        return page;
    }

    protected List<T> find(Query query, Class<T> entityclass) {
        List<T> datas = mongoTemplate.find(query, entityclass);
        return datas;
    }

    public List<?> findArrayElemAsPage(String field, String itemName, Object itemValue, Class<?> entityclass) {
        Query query = Query.query(Criteria.where(field + "." + itemName).is(itemValue));
        return mongoTemplate.find(query, entityclass);
    }

    public List findByField(String field, String value, Class<?> entityclass) {
        Query query = new Query();
        query.addCriteria(Criteria.where(field).is(value));
        return mongoTemplate.find(query, entityclass);
    }

    public List findByField(String field, Object value, Class<?> entityclass) {
        Query query = new Query();
        query.addCriteria(Criteria.where(field).is(value));
        return mongoTemplate.find(query, entityclass);
    }

    public List findEqAndInField(Map<String, Object> eqFields, Map<String, Collection<?>> inFields, Class<?> entityClass) {
        Criteria c = this.getIsCriteria(eqFields);
        c = this.getInCriteria(c, inFields);
        if (c == null) {
            return new ArrayList();
        }
        Query query = new Query();
        query.addCriteria(c);
        return mongoTemplate.find(query, entityClass);
    }

    public List findEqAndNotInField(Map<String, Object> eqFields, Map<String, Collection<?>> inFields, Class<?> entityClass) {
        Criteria c = this.getIsCriteria(eqFields);
        c = this.getNotInCriteria(c, inFields);
        Query query = new Query();
        query.addCriteria(c);
        return mongoTemplate.find(query, entityClass);
    }
    
    public List findByFields(Map<String, Object> fieldAndValues, Class<?> entityclass) {
        Criteria a = null;
        Iterator<String> ite = fieldAndValues.keySet().iterator();
        while (ite.hasNext()) {
            String field = ite.next();
            Object val = fieldAndValues.get(field);
            if (a == null) {
                a = Criteria.where(field).is(val);
            } else {
                a.andOperator(Criteria.where(field).is(val));
            }
        }
        Query query = new Query(a);
        return mongoTemplate.find(query, entityclass);
    }
    
    public T findOneByField(String field, String value, Class<?> entityclass) {
        Query query = new Query();
        query.addCriteria(Criteria.where(field).is(value));
        return (T) mongoTemplate.findOne(query, entityclass);
    }

    public T findOneByFieldsAnd(Map<String, Object> fields, Class<?> entityclass) {
        if (fields == null || fields.size() == 0) {
            return null;
        }

        Criteria a = this.getAndCriteria(fields);
        /*Iterator<String> ite = fields.keySet().iterator();
        while (ite.hasNext()) {
            String field = ite.next();
            String val = fields.get(field);
            if (a == null) {
                a = Criteria.where(field).is(val);
            } else {
                a.andOperator(Criteria.where(field).is(val));
            }
        }*/

        Query query = new Query(a);
        return (T) mongoTemplate.findOne(query, entityclass);
    }
    
    public List findByFieldsAndList(Map<String, String> fields, Class<?> entityclass) {
        if (fields == null || fields.size() == 0) {
            return null;
        }

        Criteria a = null;
        Iterator<String> ite = fields.keySet().iterator();
        while (ite.hasNext()) {
            String field = ite.next();
            String val = fields.get(field);
            if (a == null) {
                a = Criteria.where(field).is(val);
            } else {
                a.andOperator(Criteria.where(field).is(val));
            }
        }

        Query query = new Query(a);
        return mongoTemplate.find(query, entityclass);
    }

    public List<T> findByFieldsAnd(Map<String, Object> fields, Class<T> entityclass) {
        if (fields == null || fields.isEmpty()) {
            return null;
        }

        Criteria c = this.getIsCriteria(fields);

        Query query = new Query(c);
        return mongoTemplate.find(query, entityclass);
    }

    public abstract void init();

    public void shardCollection(String collectionName, String field) {
        BasicDBObject cmd = new BasicDBObject();
        String colName = "opencenterx." + collectionName;
        cmd.append("shardcollection", colName).append("key", new BasicDBObject(field, 1));

        DB db = mongoTemplate.getDb();
        Mongo mg = db.getMongo();
        DB ad = mg.getDB("admin");
        CommandResult r = ad.command(cmd);
        System.out.println("Collection: " + collectionName + " ShardingResult: " + r.toJson());
    }

    public List<T> getAll(Class<?> entityclass) {
        return (List<T>) mongoTemplate.find(new Query(), entityclass);
    }
    
    @Override
    public List<T> findListByWhereEntity(Map<String, MongoDBWhereEntity> whereMap , Class<?> entityclass) {
    	Query query = new Query();
    	Criteria criteriaByWhereEntity = getCriteriaByWhereEntity(whereMap);
    	query.addCriteria(criteriaByWhereEntity);
    	return (List<T>) mongoTemplate.find(query, entityclass);
    }
    
    @Override
    public Page<T> findPageListByWhereEntity(int currentPage, int pageSize,Map<String, MongoDBWhereEntity> whereMap , Class<?> entityclass) {
    	Query query = new Query();
    	Criteria criteriaByWhereEntity = getCriteriaByWhereEntity(whereMap);
    	query.addCriteria(criteriaByWhereEntity);
    	return this.findAsPage(query, currentPage, pageSize, entityclass);
    }

    public int delByField(String field, String value, Class<?> entityclass) {
        Query query = new Query();
        query.addCriteria(Criteria.where(field).is(value));
        WriteResult rt = mongoTemplate.remove(query, entityclass);
        return rt.getN();
    }

    public int updateArrayElemById(String id, String field, String findItemName, Object findItemValue,
            String updateItemName, Object updateItemValue, Class<?> entityclass) {
        Query query = Query.query(Criteria.where("_id").is(id).and(field + "." + findItemName).is(findItemValue));
        Update update = new Update();
        update.set(field + ".$." + updateItemName, updateItemValue);

        WriteResult ret = mongoTemplate.updateMulti(query, update, entityclass);
        // WriteResult ret = mongoTemplate.updateFirst(query, update, Worker.class);
        return ret.getN();
    }

    public int updateArrayElemById(String id, String field, String findItemName, Object findItemValue,
            Map<String, Object> updateItems, Class<T> entityclass) {
        Query query = Query.query(Criteria.where("_id").is(id).and(field + "." + findItemName).is(findItemValue));
        Update update = this.getUpdate(field, updateItems);

        WriteResult ret = mongoTemplate.updateMulti(query, update, entityclass);
        return ret.getN();
    }

    public int updateElemById(String id, String field, Map<String, Object> updateItems, Class<T> entityclass) {
        if (updateItems == null || updateItems.isEmpty()) {
            return 0;
        }
        Query query = Query.query(Criteria.where("_id").is(id));
        Update update = this.getUpdate(field, updateItems);

        WriteResult ret = mongoTemplate.updateMulti(query, update, entityclass);
        return ret.getN();
    }

    private Criteria getIsCriteria(Map<String, Object> fields) {
        if (fields == null || fields.size() == 0) {
            return null;
        }

        Criteria c = null;
        Iterator<String> ite = fields.keySet().iterator();
        while (ite.hasNext()) {
            String field = ite.next();
            Object val = fields.get(field);
            if (c == null) {
                c = Criteria.where(field).is(val);
            } else {
                c.andOperator(Criteria.where(field).is(val));
            }
        }
        return c;
    }

    private Criteria getInCriteria(Criteria c, Map<String, Collection<?>> fields) {
        if (fields == null || fields.size() == 0) {
            return c;
        }

        Iterator<String> ite = fields.keySet().iterator();
        while (ite.hasNext()) {
            String field = ite.next();
            Collection<?> val = fields.get(field);
            if (val == null || val.isEmpty()) {
                return null;
            }
            if (c == null) {
                c = Criteria.where(field).in(val);
            } else {
                c.andOperator(Criteria.where(field).in(val));
            }
        }
        return c;
    }

    private Criteria getNotInCriteria(Criteria c, Map<String, Collection<?>> fields) {
        if (fields == null || fields.size() == 0) {
            return c;
        }

        Iterator<String> ite = fields.keySet().iterator();
        while (ite.hasNext()) {
            String field = ite.next();
            Collection<?> val = fields.get(field);
            if (val == null || val.isEmpty()) {
                continue;
            }
            if (c == null) {
                c = Criteria.where(field).nin(val);
            } else {
                c.andOperator(Criteria.where(field).nin(val));
            }
        }
        return c;
    }

    private Update getUpdate(String field, Map<String, Object> updates) {
        if (updates == null || updates.size() == 0) {
            return null;
        }

        String keyPrefix = "";
        if (field != null && !field.trim().isEmpty()) {
            keyPrefix = field + ".$.";
        }

        Update update = new Update();

        Iterator<String> keyIt = updates.keySet().iterator();
        String key = null;
        while (keyIt.hasNext()) {
            key = keyIt.next();
            update.set(keyPrefix + key, updates.get(key));
        }

        return update;
    }

    public List<T> findInAsList(String field, List<?> list, String sort,
            Class<?> entityclass) {
        Query query = new Query();
        query.addCriteria(Criteria.where(field).in(list));
        query.with(new Sort(Direction.DESC, sort));
        return (List<T>)mongoTemplate.find(query, entityclass);
    }
    
    private void querySort(Query query, Integer sortType, String sortField) {
        if (StringUtils.isNotBlank(sortField)) {
        	Sort.Direction direction = Sort.Direction.DESC;
        	if (sortType != null && sortType.intValue() == Constants.SORT_TYPE_ASC) {
        		direction = Sort.Direction.ASC;
        	}
        	query.with(new Sort(direction, sortField));
        }
    }
    
    private Criteria getAndCriteria(Map<String, Object> fieldValueMap) {
        Criteria criteria = null;
        List<Criteria> criteriaLst = new ArrayList<Criteria>();
        Iterator<String> ite = fieldValueMap.keySet().iterator();
        while (ite.hasNext()) {
            String field = ite.next();
            Object val = fieldValueMap.get(field);
            if (criteria == null) {
                criteria = Criteria.where(field).is(val);
            } else {
                criteriaLst.add(Criteria.where(field).is(val));
            }
        }
        if (criteria != null && !criteriaLst.isEmpty()) {
            Criteria[] criterias = new Criteria[criteriaLst.size()];
            for (int i = 0; i < criteriaLst.size(); i++) {
                criterias[i] = criteriaLst.get(i);
            }
            criteria.andOperator(criterias);
        }
        return criteria;
    }
    
    /***
     * 
     * 根据多个字段值构建查询条件器
     * @author lida
     * @param condMap 查询条件Map key=字段 value=值 method=查询方式
     * @see {@link MongoDBWhereEntity } {@link ConditionEnum}
     * @return
     * 
     */
    private Criteria getCriteriaByWhereEntity(Map<String,MongoDBWhereEntity> condMap){
    	Criteria criteria = new Criteria();
    	if(!condMap.isEmpty()){
    		Iterator<String> iterator = condMap.keySet().iterator();
    		MongoDBWhereEntity entity = null;
    		String k = "";
    		while(iterator.hasNext()){
    			k = iterator.next();
    			entity = condMap.get(k);
    			if(entity.getMethod() == ConditionEnum.LIKE){
    				criteria.and(k).regex(entity.getValue().toString());
    			}else if(entity.getMethod() == ConditionEnum.EQ){
    				criteria.and(k).is(entity.getValue());
    			}else if(entity.getMethod() == ConditionEnum.OR){
    				criteria.orOperator(Criteria.where(k).is(entity.getValue()));
    			}
    		}
    	}
    	return criteria;
    }
    
}
