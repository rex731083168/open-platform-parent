package cn.ce.platform_service.app.dao.impl;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import cn.ce.platform_service.apisecret.entity.ApiSecretKey;
import cn.ce.platform_service.app.dao.IAppDAO;
import cn.ce.platform_service.app.entity.AppEntity;
import cn.ce.platform_service.common.Constants;
import cn.ce.platform_service.core.AbstractBaseMongoDao;
import cn.ce.platform_service.core.bean.MongoDBWhereEntity;
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
public class AppDaoImpl extends AbstractBaseMongoDao<AppEntity> implements IAppDAO {
    /** 定义 */
    private String clientName = "client_name";

    /** 定义 */
    private String createTime = "create_time";

     @Override
    public void addApp(AppEntity app) {
        this.add(app);
    }

     @Override
    public AppEntity findById(String id) {
        return super.findById(id, AppEntity.class);
    }

     @Override
    public int modifyById(String id, AppEntity app) {
        super.updateById(id, app);
        return 1;
    }

     @Override
    public List<AppEntity> getAll() {
        return super.getAll(AppEntity.class);
    }

     @Override
    public int delById(String id) {
        return super.delById(id, AppEntity.class);
    }

    @Override
    public Page<AppEntity> getAppList(String userid) {
        return super.findAsPage("userid", userid, PageContext.getCuurentPage(), PageContext.getPageSize(),
                        AppEntity.class);
    }


    @Override
    public AppEntity findByAppKey(String appKey) {
        return super.findOneByField("client_id", appKey, AppEntity.class);
    }

    @Override
    public Page<AppEntity> getAsPage(String appName, int currentPage, int pageSize) {
        return super.findLikeAsPage(clientName, appName, Constants.SORT_TYPE_DESC, createTime, currentPage,
                        pageSize, AppEntity.class);
    }

    @Override
    public Page<AppEntity> getAsPage(String appName, Integer appCheckstate, int currentPage, int pageSize) {
        return super.findLikeAndIsAsPage(clientName, appName, "checkState", appCheckstate, Constants.SORT_TYPE_DESC,
                        createTime, currentPage, pageSize, AppEntity.class);
    }

    @Override
    public List<?> getAppWithGroups(String appName, int groupCheckstate) {
        return super.findArrayElemAsPage("groups", "approvedstate", groupCheckstate, AppEntity.class);
    }

    @Override
    public Page<AppEntity> findInAsPage(String appname, Collection<?> ids, int currentPage, int pageSize) {
        return super.findLikeAndInAsPage(clientName, appname, "_id", ids, Constants.SORT_TYPE_DESC, createTime,
                        currentPage, pageSize, AppEntity.class);
    }

    @Override
    public Page<AppEntity> findNotInAsPage(String appname, Collection<?> ids, int currentPage, int pageSize) {
        return super.findLikeAndNotInAsPage(clientName, appname, "_id", ids, Constants.SORT_TYPE_DESC,
                        createTime, currentPage, pageSize, AppEntity.class);
    }

	@Override
	public void init() {
		// TODO Auto-generated method stub
		
	}
	
	public AppEntity findAppByAppName(String appName) {
		return super.findOneByField("appname", appName, AppEntity.class);
	}

	@Override
	public Page<AppEntity> findAppListPageByDBWhere(int currentPage, int pageSize,
			Map<String, MongoDBWhereEntity> condMap) {
		return super.findPageListByWhereEntity(currentPage, pageSize, condMap, AppEntity.class);
	}

	@Override
	public List<AppEntity> findAppListByDBWhere(Map<String, MongoDBWhereEntity> condMap) {
		return super.findListByWhereEntity(condMap, AppEntity.class);
	}
	
	@Override
	public Page<AppEntity> findAppListByMapCondition(Map<String,Object> condMap, int currentPage, int pageSize) {
        return super.findByFieldsAsPage(condMap, currentPage, pageSize, AppEntity.class);
	}

	@Override
	public Page<AppEntity> findAppsByEntity(AppEntity entity, int currentPage, int pageSize) {
		//分页
		Page<AppEntity> findAsPage = super.findAsPage(generalSecretQueryBean(entity).with(new Sort(Direction.DESC, "createdate")), currentPage, pageSize, AppEntity.class);
		
		return findAsPage;
	}
	
	private Query generalSecretQueryBean(AppEntity entity){
		//构建查询对象
		Criteria c = new Criteria();
		
		if(StringUtils.isNotBlank(entity.getUserid())){
			c.and("userid").is(entity.getUserid());
		}
		
		if(StringUtils.isNotBlank(entity.getAppname())){
			Criteria c1 = Criteria.where("appname").regex(".*?"+entity.getAppname()+".*","i");
			Criteria c2 = Criteria.where("appkey").regex(".*?"+entity.getAppname()+".*","i");
			c.orOperator(c1,c2);
		}
		
		if(StringUtils.isNotBlank(entity.getNeqId())){
			c.and("id").ne(entity.getNeqId());
		}
		
		if(StringUtils.isNotBlank(entity.getAppkey())){
			c.and("appkey").is(entity.getAppkey());
		}
		
		if(null != entity.getCheckState()){
			c.and("checkstate").is(entity.getCheckState());
		}
		
		//构建排序对象
		Query query = new Query(c).with(new Sort(Direction.DESC, "createdate"));
		
		return query;
	}

	@Override
	public List<AppEntity> findAppsByEntity(AppEntity entity) {
		List<AppEntity> listApp = super.find(generalSecretQueryBean(entity), AppEntity.class);
		return listApp;
	}

	
}
