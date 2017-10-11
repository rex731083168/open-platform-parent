package cn.ce.platform_service.openApply.dao.impl;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import cn.ce.platform_service.common.Constants;
import cn.ce.platform_service.core.AbstractBaseMongoDao;
import cn.ce.platform_service.core.bean.MongoDBWhereEntity;
import cn.ce.platform_service.openApply.dao.IAppDAO;
import cn.ce.platform_service.openApply.entity.DevApplyEntity;
import cn.ce.platform_service.page.Page;
import cn.ce.platform_service.page.PageContext;

/**
 * 
 * @ClassName: AppDAOImpl
 * @Description: 开发者创建的应用操作类型
 * @author dingjia@300.cn
 *
 */
@Repository(value ="appDao")
public class AppDaoImpl extends AbstractBaseMongoDao<DevApplyEntity> implements IAppDAO {
    /** 定义 */
    private String clientName = "client_name";

    /** 定义 */
    private String createTime = "create_time";

     @Override
    public void addApp(DevApplyEntity app) {
        this.add(app);
    }

     @Override
    public DevApplyEntity findById(String id) {
        return super.findById(id, DevApplyEntity.class);
    }

     @Override
    public int modifyById(String id, DevApplyEntity app) {
        super.updateById(id, app);
        return 1;
    }

     @Override
    public List<DevApplyEntity> getAll() {
        return super.getAll(DevApplyEntity.class);
    }

     @Override
    public int delById(String id) {
        return super.delById(id, DevApplyEntity.class);
    }

    @Override
    public Page<DevApplyEntity> getAppList(String userid) {
        return super.findAsPage("userid", userid, PageContext.getCuurentPage(), PageContext.getPageSize(),
                        DevApplyEntity.class);
    }


    @Override
    public DevApplyEntity findByAppKey(String appKey) {
        return super.findOneByField("client_id", appKey, DevApplyEntity.class);
    }

    @Override
    public Page<DevApplyEntity> getAsPage(String appName, int currentPage, int pageSize) {
        return super.findLikeAsPage(clientName, appName, Constants.SORT_TYPE_DESC, createTime, currentPage,
                        pageSize, DevApplyEntity.class);
    }

    @Override
    public Page<DevApplyEntity> getAsPage(String appName, Integer appCheckstate, int currentPage, int pageSize) {
        return super.findLikeAndIsAsPage(clientName, appName, "checkState", appCheckstate, Constants.SORT_TYPE_DESC,
                        createTime, currentPage, pageSize, DevApplyEntity.class);
    }

    @Override
    public List<?> getAppWithGroups(String appName, int groupCheckstate) {
        return super.findArrayElemAsPage("groups", "approvedstate", groupCheckstate, DevApplyEntity.class);
    }

    @Override
    public Page<DevApplyEntity> findInAsPage(String appname, Collection<?> ids, int currentPage, int pageSize) {
        return super.findLikeAndInAsPage(clientName, appname, "_id", ids, Constants.SORT_TYPE_DESC, createTime,
                        currentPage, pageSize, DevApplyEntity.class);
    }

    @Override
    public Page<DevApplyEntity> findNotInAsPage(String appname, Collection<?> ids, int currentPage, int pageSize) {
        return super.findLikeAndNotInAsPage(clientName, appname, "_id", ids, Constants.SORT_TYPE_DESC,
                        createTime, currentPage, pageSize, DevApplyEntity.class);
    }

	@Override
	public void init() {
		// TODO Auto-generated method stub
		
	}
	
	public DevApplyEntity findAppByAppName(String appName) {
		return super.findOneByField("appname", appName, DevApplyEntity.class);
	}

	@Override
	public Page<DevApplyEntity> findAppListPageByDBWhere(int currentPage, int pageSize,
			Map<String, MongoDBWhereEntity> condMap) {
		return super.findPageListByWhereEntity(currentPage, pageSize, condMap, DevApplyEntity.class);
	}

	@Override
	public List<DevApplyEntity> findAppListByDBWhere(Map<String, MongoDBWhereEntity> condMap) {
		return super.findListByWhereEntity(condMap, DevApplyEntity.class);
	}
	
	@Override
	public Page<DevApplyEntity> findAppListByMapCondition(Map<String,Object> condMap, int currentPage, int pageSize) {
        return super.findByFieldsAsPage(condMap, currentPage, pageSize, DevApplyEntity.class);
	}

	@Override
	public Page<DevApplyEntity> findAppsByEntity(DevApplyEntity entity, int currentPage, int pageSize) {
		//分页
		Page<DevApplyEntity> findAsPage = super.findAsPage(generalSecretQueryBean(entity).with(new Sort(Direction.DESC, "createdate")), currentPage, pageSize, DevApplyEntity.class);
		
		return findAsPage;
	}
	
	private Query generalSecretQueryBean(DevApplyEntity entity){
		//构建查询对象
		Criteria c = new Criteria();
		
		if(StringUtils.isNotBlank(entity.getUserId())){
			c.and("userid").is(entity.getUserId());
		}
		
		if(StringUtils.isNotBlank(entity.getApplyName())){
			Criteria c1 = Criteria.where("appname").regex(".*?"+entity.getApplyName()+".*","i");
			Criteria c2 = Criteria.where("appkey").regex(".*?"+entity.getApplyName()+".*","i");
			c.orOperator(c1,c2);
		}
		
		if(StringUtils.isNotBlank(entity.getNeqId())){
			c.and("id").ne(entity.getNeqId());
		}
		
		if(StringUtils.isNotBlank(entity.getApplyKey())){
			c.and("appkey").is(entity.getApplyKey());
		}
		
		if(null != entity.getCheckState()){
			c.and("checkstate").is(entity.getCheckState());
		}
		
		//构建排序对象
		Query query = new Query(c).with(new Sort(Direction.DESC, "createdate"));
		
		return query;
	}

	@Override
	public List<DevApplyEntity> findAppsByEntity(DevApplyEntity entity) {
		List<DevApplyEntity> listApp = super.find(generalSecretQueryBean(entity), DevApplyEntity.class);
		return listApp;
	}

	
}
