package cn.ce.platform_service.apis.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import cn.ce.platform_service.apis.entity.ApiEntity;
import cn.ce.platform_service.apis.entity.NewApiEntity;
import cn.ce.platform_service.apis.entity.QueryApiEntity;

/**
* @Description : 说明
* @Author : makangwei
* @Date : 2018年1月18日
*/
@Transactional(propagation=Propagation.REQUIRED)
public interface IMysqlApiDao {
	
	public int save(ApiEntity apiEntity);

	public void save1(NewApiEntity apiEntity);

	public List<NewApiEntity> findByListenPath(String listenPath);

	public NewApiEntity findById(String apiId);

	public int findCountByListenPath(String listenPath);

	public int saveOrUpdateEntity(NewApiEntity apiEntity);

	public int findVersionNum(@Param("versionId")String versionId, @Param("version")String version);

	public int updateVersionByVersionId(@Param("versionId")String versionId, @Param("newVersion")boolean b);

	public int updateCheckState(@Param("apiId")String apiId,@Param("checkState") Integer checkState);

	public NewApiEntity findTotalOneById(String apiId);

	/**
	 * 
	 * @Description: 根据条件查询条数
	 * @author: makangwei 
	 * @date:   2018年1月23日 下午3:23:50 
	 */
	public int findListSize(QueryApiEntity entity);

	public List<ApiEntity> getPagedList(QueryApiEntity entity);

	public int checkApiChName(@Param("apiChName")String apiChName, @Param("openApplyId")String openApplyId);
	
	public int checkVersion(@Param("versionId")String versionId, @Param("version")String version);

	public List<ApiEntity> findByOpenApply(String openApplyId);
}
