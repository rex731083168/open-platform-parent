package cn.ce.apis.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.apache.commons.lang.StringUtils;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import cn.ce.apis.dao.IApiDAO;
import cn.ce.apis.entity.APIEntity;
import cn.ce.common.Constants;
import cn.ce.core.AbstractBaseMongoDao;
import cn.ce.page.Page;
import cn.ce.page.PageContext;

/**
 * 
 * @ClassName: IApiDAOIMpl
 * @Description: 接口数据库操作类型
 * @author dingjia@300.cn
 *
 */
@Repository(value ="apiDao")
public class ApiDaoImpl extends AbstractBaseMongoDao<APIEntity> implements IApiDAO {
    /** 定义 */
    private String appIdField = "appid";

    /** 定义 */
    private String categoryCodeField = "categorycode";

    /** 定义 */
    private String checkSta = "checkstate";

    public ApiDaoImpl() {
        PageContext.init(1, Constants.PAGE_SIZE);
    }

    @Override
    public List<APIEntity> getAPIs(String groupId) {
    	
        return super.findByField(appIdField, groupId, APIEntity.class);
    }

    @Override
    public List<APIEntity> getAPIs(String groupId, int checkState) {
        Map<String, Object> fields = new HashMap<String, Object>();
        fields.put(appIdField, groupId);
        fields.put(checkSta, checkState);
        return super.findByFieldsAnd(fields, APIEntity.class);
    }

    @Override
    public List<APIEntity> getCategoryAPIs(String categoryCode, int checkState) {
        Map<String, Object> fields = new HashMap<String, Object>();
        fields.put(categoryCodeField, categoryCode);
        fields.put(checkSta, checkState);
        return super.findByFieldsAnd(fields, APIEntity.class);
    }

    @Override
    public List<APIEntity> getAPIs(int checkState) {
        Map<String, Object> fields = new HashMap<String, Object>();
        fields.put(checkSta, checkState);
        return super.findByFieldsAnd(fields, APIEntity.class);
    }

    @Override
    public void addAPI(APIEntity api) {
        super.add(api);
    }

    @Override
    public APIEntity findOne(String apgrpId) {
        return super.findOneByField(appIdField, apgrpId, APIEntity.class);
    }

    @Override
    public APIEntity findOneByEnName(String enName) {
        return super.findOneByField("apienname", enName, APIEntity.class);
    }

    @Override
    public int delApi(String id) {
        return super.delById(id, APIEntity.class);
    }

    @Override
    public APIEntity findOneById(String id) {
        return super.findById(id, APIEntity.class);
    }

    @Override
    public int updateAPI(APIEntity api) {
        super.updateById(api.getId(), api);
        return 1;
    }

    @Override
    public Page<APIEntity> getAPIsAsPage(String groupId) {
        return super.findAsPage(appIdField, groupId, PageContext.getCuurentPage(), PageContext.getPageSize(),
                        APIEntity.class);

    }


    @PostConstruct
    public void init() {
//        boolean ext = mongoTemplate.collectionExists(APIEntity.class);
//        if (!ext) {
//            System.out.println("=========>> Init Create Collection : APIMG_APIS ....");
//            mongoTemplate.createCollection(APIEntity.class);
//            /** 配置分片 */
//            /* super.shardCollection("OPC_API", "_id"); */
//
//            /** 创建唯一索引 ，失败，分片后无法创建唯一索引*/
//            IndexOperations io = mongoTemplate.indexOps(APIEntity.class);
//            Index index = new Index();
//            // 为name属性加上 索引
//            index.on("apienname", Order.ASCENDING);
//            // 唯一索引
//            index.unique();
//            io.ensureIndex(index);
//        }
    }

    @Override
    public Page<APIEntity> searchApis(String nameDesc, int currentPage, int pageSize) {
        // 2 为降序排列
        return super.findLikeOrLikeAsPage("apichname", nameDesc, "desc", nameDesc, 2, "createdate", currentPage,
                        pageSize, APIEntity.class);
    }

    @Override
    public Page<APIEntity> getGroupAPIsAsPage(String groId, int checkState, int state, int currentPage, int pageSize) {
        Map<String, Object> fields = new HashMap<String, Object>();
        fields.put("groupid", groId);
        fields.put("checkstate", checkState);
        fields.put("state", state);
        return super.findByFieldsAsPage(fields, currentPage, pageSize, APIEntity.class);
    }

	@Override
	public APIEntity findByApiCnName(String cnname) {
		return super.findOneByField("apichname", cnname, APIEntity.class);
	}

	@Override
	public Page<APIEntity> findApisByEntity(APIEntity entity,int currentPage,int pageSize) {
		
		//构建查询对象
		Criteria c = new Criteria();
		
		if(StringUtils.isNotBlank(entity.getAppid())){
			c.and("appid").is(entity.getAppid());
		}
		
		if(null != entity.getCheckState()){
			c.and("checkstate").is(entity.getCheckState());
		}
		
		if(StringUtils.isNotBlank(entity.getApichname())){
			c.and("apichname").regex(entity.getApichname());
		}
		
		if(StringUtils.isNotBlank(entity.getUserid())){
			c.and("userid").is(entity.getUserid());
		}
		
		//构建排序对象
		Query query = new Query(c).with(new Sort(Direction.DESC, "createtime"));
		
		//分页
		Page<APIEntity> findAsPage = super.findAsPage(query, currentPage, pageSize, APIEntity.class);
		
		return findAsPage;
	}
	
	@Override
	public List<APIEntity> findApiListByQuery(Query query) {
		return super.find(query, APIEntity.class);
	}

	@Override
	public List<APIEntity> findByField(String key, String value) {
		Query query = new Query();
		query.addCriteria(Criteria.where(key).is(value));
		return super.find(query, APIEntity.class);
	}

	@Override
	public List<APIEntity> findApiListByIds(List<String> ids) {
		return super.findInAsList("id", ids, "createtime", APIEntity.class);
	}

	@Override
	public APIEntity findOneByFields(Map<String, Object> map) {
		
		List<APIEntity> list = super.findByFields(map, APIEntity.class);
		if(list.size()>0){
			return list.get(0);
		}
		return null;
	}

	@Override
	public boolean modifyApi(APIEntity apientity) {
		
		try{
			mongoTemplate.save(apientity);
			return true;
		}catch(Exception e){
			return false;
		}
	}

}
