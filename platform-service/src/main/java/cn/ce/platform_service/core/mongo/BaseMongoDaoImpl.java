package cn.ce.platform_service.core.mongo;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import com.mongodb.BasicDBObject;
import com.mongodb.CommandResult;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.WriteResult;

import cn.ce.platform_service.core.BathUpdateOptions;
import cn.ce.platform_service.core.bean.ConditionEnum;
import cn.ce.platform_service.core.bean.MongoDBWhereEntity;
import cn.ce.platform_service.page.Page;
import cn.ce.platform_service.util.ReflectionUtils;

public class BaseMongoDaoImpl<T> implements BaseMongoDao<T> {
	/**
	 * spring mongodb 集成操作类
	 */
	@Resource
	protected MongoTemplate mongoTemplate;

	/**
	 * 注入mongodbTemplate
	 * 
	 * @param mongoTemplate
	 */
	protected void setMongoTemplate(MongoTemplate mongoTemplate) {
		this.mongoTemplate = mongoTemplate;
	}

	public T save(T entity) {
		mongoTemplate.insert(entity);
		return entity;
	}

	public T findById(String id) {
		return mongoTemplate.findById(id, this.getEntityClass());
	}

	public T findById(String id, String collectionName) {
		return mongoTemplate.findById(id, this.getEntityClass(), collectionName);
	}

	public List<T> findAll() {
		return mongoTemplate.findAll(this.getEntityClass());
	}

	public List<T> findAll(String collectionName) {
		return mongoTemplate.findAll(this.getEntityClass(), collectionName);
	}

	public List<T> find(Query query) {
		return mongoTemplate.find(query, this.getEntityClass());
	}

	public T findOne(Query query) {
		return mongoTemplate.findOne(query, this.getEntityClass());
	}

	public Page<T> findPage(Page<T> page, Query query) {
		// 如果没有条件 则所有全部
		query = query == null ? new Query(Criteria.where("_id").exists(true)) : query;
		long count = this.count(query);
		// 总数
		page.setTotalNumber((int) count);
		int currentPage = page.getCurrentPage();
		int pageSize = page.getPageSize();
		query.skip((currentPage - 1) * pageSize).limit(pageSize);
		List<T> rows = this.find(query);
		page.build(rows);
		return page;
	}

	public long count(Query query) {
		return mongoTemplate.count(query, this.getEntityClass());
	}

	public WriteResult update(Query query, Update update) {
		if (update == null) {
			return null;
		}
		return mongoTemplate.updateMulti(query, update, this.getEntityClass());
	}

	public T updateOne(Query query, Update update) {
		if (update == null) {
			return null;
		}
		return mongoTemplate.findAndModify(query, update, this.getEntityClass());
	}

	public WriteResult update(T entity) {
		Field[] fields = this.getEntityClass().getDeclaredFields();
		if (fields == null || fields.length <= 0) {
			return null;
		}
		Field idField = null;
		// 查找ID的field
		for (Field field : fields) {
			if (field.getName() != null && "id".equals(field.getName().toLowerCase())) {
				idField = field;
				break;
			}
		}
		if (idField == null) {
			return null;
		}
		idField.setAccessible(true);
		String id = null;
		try {
			id = (String) idField.get(entity);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		if (id == null || "".equals(id.trim()))
			return null;
		// 根据ID更新
		Query query = new Query(Criteria.where("_id").is(id));
		Update update = ReflectionUtils.getUpdateObj(entity);
		if (update == null) {
			return null;
		}
		return mongoTemplate.updateFirst(query, update, getEntityClass());
	}

	public void remove(Query query) {
		mongoTemplate.remove(query, this.getEntityClass());
	}

	/**
	 * 获得泛型类
	 */
	private Class<T> getEntityClass() {
		return ReflectionUtils.getSuperClassGenricType(getClass());
	}

	@Override
	public void removeById(String id) {
		Query query = new Query();
		query.addCriteria(Criteria.where("_id").is(id));
		remove(query);
	}

	/***
	 * 
	 * 根据多个字段值构建查询条件器
	 * 
	 * @author lida
	 * @param condMap
	 *            查询条件Map key=字段 value=值 method=查询方式
	 * @see {@link MongoDBWhereEntity } {@link ConditionEnum}
	 * @return
	 * 
	 */
	protected Criteria getCriteriaByWhereEntity(final Map<String, MongoDBWhereEntity> condMap) {
		Criteria criteria = new Criteria();
		if (!condMap.isEmpty()) {
			Iterator<String> iterator = condMap.keySet().iterator();
			MongoDBWhereEntity entity = null;
			String k = "";
			while (iterator.hasNext()) {
				k = iterator.next();
				entity = condMap.get(k);
				if (entity.getMethod() == ConditionEnum.LIKE) {
					criteria.and(k).regex(entity.getValue().toString());
				} else if (entity.getMethod() == ConditionEnum.EQ) {
					criteria.and(k).is(entity.getValue());
				} else if (entity.getMethod() == ConditionEnum.NEQ) {
					criteria.and(k).ne(entity.getValue());
				} else if (entity.getMethod() == ConditionEnum.IN) {
					criteria.and(k).in(entity.getValue());
				} else if (entity.getMethod() == ConditionEnum.NOTIN) {
					criteria.and(k).nin(entity.getValue());
				}
			}
		}
		return criteria;
	}

	/**** bathupdate code begain ****/
	/**
	 * 
	 * @param dbCollection
	 * @param collName
	 * @param options  BathUpdateOptions
	 * @param ordered
	 * @return 受影响的行数
	 */
	private static int doBathUpdate(DBCollection dbCollection, String collName, List<BathUpdateOptions> options,
			boolean ordered) {
		DBObject command = new BasicDBObject();
		command.put("update", collName);
		List<BasicDBObject> updateList = new ArrayList<BasicDBObject>();
		for (BathUpdateOptions option : options) {
			BasicDBObject update = new BasicDBObject();
			update.put("q", option.getQuery().getQueryObject());
			update.put("u", option.getUpdate().getUpdateObject());
			update.put("upsert", option.isUpsert());
			update.put("multi", option.isMulti());
			updateList.add(update);
		}
		command.put("updates", updateList);
		command.put("ordered", ordered);
		CommandResult commandResult = dbCollection.getDB().command(command);
		return Integer.parseInt(commandResult.get("n").toString());
	}

	/**
	 * 
	 * @param entityClass
	 * @return collectionName
	 */
	private static String determineCollectionName(Class<?> entityClass) {
		if (entityClass == null) {
			throw new InvalidDataAccessApiUsageException(
					"No class parameter provided, entity collection can't be determined!");
		}
		String collName = entityClass.getSimpleName();
		if (entityClass.isAnnotationPresent(Document.class)) {
			Document document = entityClass.getAnnotation(Document.class);
			collName = document.collection();
		} else {
			collName = collName.replaceFirst(collName.substring(0, 1), collName.substring(0, 1).toLowerCase());
		}
		return collName;
	}

	/**
	 * 
	 * @param mongoTemplate
	 * @param entityClass
	 * @param options
	 * @return 受影响的行數
	 * 
	 * how to use: 
	 *         List<BathUpdateOptions> list = new
	 *         ArrayList<BathUpdateOptions>(); list.add(new
	 *         BathUpdateOptions(Query.query(Criteria.where("author").is("yinjihuan")),
	 *         Update.update("title", "批量更新"), true, true)); list.add(new
	 *         BathUpdateOptions(Query.query(Criteria.where("author").is("jason")),
	 *         Update.update("title", "批量更新"), true, true)); int n =
	 *         MongoBaseDao.bathUpdate(mongoTemplate, Article.class, list);
	 *         System.out.println("受影响的行数："+n);
	 */
	public static int bathUpdate(MongoTemplate mongoTemplate, Class<?> entityClass, List<BathUpdateOptions> options) {
		String collectionName = determineCollectionName(entityClass);
		return doBathUpdate(mongoTemplate.getCollection(collectionName), collectionName, options, true);
	}
	/**
	 * 
	 * @param dbCollection
	 * @param entityClass
	 * @param options  BathUpdateOptions
	 * @return 受影响的行數
	 * 
	 *  how to use: 
	 *         List<BathUpdateOptions> list = new
	 *         ArrayList<BathUpdateOptions>(); list.add(new
	 *         BathUpdateOptions(Query.query(Criteria.where("author").is("yinjihuan")),
	 *         Update.update("title", "批量更新"), true, true)); list.add(new
	 *         BathUpdateOptions(Query.query(Criteria.where("author").is("jason")),
	 *         Update.update("title", "批量更新"), true, true)); int n =
	 *         MongoBaseDao.bathUpdate(mongoTemplate, Article.class, list);
	 *         System.out.println("受影响的行数："+n);
	 */
	public static int bathUpdate(DBCollection dbCollection, Class<?> entityClass, List<BathUpdateOptions> options) {
		return doBathUpdate(dbCollection, determineCollectionName(entityClass), options, true);
	}

	/**** bathupdate code begain ****/
}
