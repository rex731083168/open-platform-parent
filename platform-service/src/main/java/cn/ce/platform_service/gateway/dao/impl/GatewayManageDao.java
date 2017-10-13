package cn.ce.platform_service.gateway.dao.impl;




import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import com.mongodb.WriteResult;

import cn.ce.platform_service.common.page.Page;
import cn.ce.platform_service.core.AbstractBaseMongoDao;
import cn.ce.platform_service.gateway.dao.IGatewayManageDao;
import cn.ce.platform_service.gateway.entity.GatewayColonyEntity;

/**
 *
 * @author makangwei
 * 2017-8-4
 */
@Repository
public class GatewayManageDao extends AbstractBaseMongoDao<GatewayColonyEntity> implements IGatewayManageDao {

	Logger LOGGER = LoggerFactory.getLogger(GatewayManageDao.class);
	
	@Override
	public void init() {
		
		boolean isHasCollection = mongoTemplate.collectionExists(GatewayColonyEntity.class);

		if(!isHasCollection){
			LOGGER.info("------------init create Collection GatewayColonyEntity:----------------");
			mongoTemplate.createCollection(GatewayColonyEntity.class);
		}
		
	}
	
	/**
	 * 添加集群到mongodb数据库
	 * @param colEntity
	 * @return
	 */
	public boolean addGatewayCol(GatewayColonyEntity colEntity) {
		
		try{
			add(colEntity);
			return true;
		}catch(Exception e){
			e.printStackTrace();
			return false;
		}

	}

    /**
     * 查询所有网关集群
     * @return
     */
	public Page<GatewayColonyEntity> getAllGatewayCol(Integer currentPage,Integer pageSize) {
		
		Page<GatewayColonyEntity> page = findAsPage(currentPage,pageSize,GatewayColonyEntity.class);
	
		if(page != null){
			return page;
		}
		
		return null;
	}

	public boolean deleteGatewayColonyById(Integer colId) {
		
		Query query =new Query();
		query.addCriteria(Criteria.where("_id").is(colId));
		WriteResult rt = mongoTemplate.remove(query, GatewayColonyEntity.class);
		
		LOGGER.info("-------数据是否已删除:"+rt.getN());
		
		if(rt.getN() > 0 ){
			return true;
		}
		return false;
	}

	public GatewayColonyEntity findById(Integer colId, Class<GatewayColonyEntity> entityclass) {
		
		Query query = new Query();
		query.addCriteria(Criteria.where("_id").is(colId));
		GatewayColonyEntity entity = mongoTemplate.findOne(query, GatewayColonyEntity.class);
		return entity;
		
	}

	public void updateById(String colId, GatewayColonyEntity entity) {
		entity.setColId(colId);
		mongoTemplate.save(entity);
		
	}

	
	
}
