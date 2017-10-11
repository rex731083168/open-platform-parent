package cn.ce.platform_service.admin.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.data.mongodb.core.IndexOperations;
import org.springframework.data.mongodb.core.index.Index;
import org.springframework.data.mongodb.core.query.Order;
import org.springframework.stereotype.Repository;

import com.mongodb.DBCollection;

import cn.ce.platform_service.admin.dao.IAdminDao;
import cn.ce.platform_service.admin.entity.AdminEntity;
import cn.ce.platform_service.common.Constants;
import cn.ce.platform_service.core.AbstractBaseMongoDao;



/**
 * 
 * @ClassName: AdminDAOImpl
 * @Description: 管理员数据库操作类型
 * @author dingjia@300.cn
 *
 */	
@Repository(value = "adminDao")
public class AdminDaoImpl extends AbstractBaseMongoDao<AdminEntity> implements IAdminDao {

    public AdminEntity findByUserName(String userName) {
        return findOneByField("userName", userName, AdminEntity.class);
    }

    public DBCollection getCol() {
        return mongoTemplate.getCollection(Constants.COL_APIMG_ADMIN);
    }

    public void update(AdminEntity admin) {
        super.updateById(admin.getId(), admin);
    }

    @Override
    public void createNewDefaultAdmin() {
//        DBCollection rex = mongoTemplate.getCollection("APIMG_ADMIN_XXX");
//        if (rex.count() == 0) {
//            System.out.println("========>> Create inital Admin User <<========");
//            BasicDBObject doc = new BasicDBObject();
//            doc.append("userName", "gly");
//            doc.append("password", "123456");
//            doc.append("mem", "admin");
//            rex.insert(doc);
//            System.out.println("=================================================");
//        }

    }
    
    @PostConstruct
    public void init() {
        boolean ext = mongoTemplate.collectionExists(AdminEntity.class);
        if (!ext) {
            System.out.println("=========>> Init Create Collection : OPC_ADMIN ....");
            mongoTemplate.createCollection(AdminEntity.class);
            /** 配置分片 */
            /* super.shardCollection("OPC_ADMIN", "_id"); */

            /** 创建唯一索引 ，失败，分片后无法创建唯一索引 */
//            IndexOperations io = mongoTemplate.indexOps(AdminEntity.class);
//            Index index = new Index();
//            // 为name属性加上 索引
//            index.on("username", Order.ASCENDING);
//            // 唯一索引
//            index.unique();
//            io.ensureIndex(index);

            /** 插入默认的一条记录 */
            AdminEntity admin = new AdminEntity();
            admin.setMem("管理员");
            admin.setUserName("admin");
            admin.setPassword("e10adc3949ba59abbe56e057f20f883e");//123456加密后的值
            mongoTemplate.save(admin);
        }
    }

	@Override
	public AdminEntity checkLogin(String username, String password) {
		
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("username", username);
		map.put("password", password);
		List<AdminEntity> list = super.findByFields(map, AdminEntity.class);
		
		if(list == null || list.size() < 1){
			return null;
		}
		
		return list.get(0);
	}
    
}
