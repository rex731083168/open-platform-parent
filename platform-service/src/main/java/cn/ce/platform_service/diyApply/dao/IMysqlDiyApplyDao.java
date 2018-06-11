package cn.ce.platform_service.diyApply.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.transaction.annotation.Transactional;

import cn.ce.platform_service.diyApply.entity.DiyApplyEntity;
import cn.ce.platform_service.diyApply.entity.QueryDiyApplyEntity;
import cn.ce.platform_service.open.entity.BiDiyApply;
import cn.ce.platform_service.diyApply.entity.DiyBoundApi;

/**
* @Description : 说明
* @Author : makangwei
* @Date : 2018年1月18日
*/
@Transactional
public interface IMysqlDiyApplyDao {

	int save(DiyApplyEntity diyApplyEntity);

	int saveBoundOpenApply(@Param("boundId")String random32uuid
			, @Param("diyApplyId")String diyApplyId, @Param("openApplyId")String openApplyId);

	int saveBoundApi(@Param("boundId")String boundId, @Param("diyApplyId")String diyApplyId,
			@Param("openApplyId")String openApplyId,@Param("apiId") String apiId, @Param("boundOpenId")String boundOpenId);

	int findListSize(QueryDiyApplyEntity queryApply);

	List<DiyApplyEntity> getPagedList(QueryDiyApplyEntity queryApply);

	DiyApplyEntity findById(String applyId);

	int deleteById(String applyId);

	int checkApplyName(@Param("userId")String userId, @Param("applyName")String applyName);

	int update(DiyApplyEntity diyApplyEntity);

	int bathUpdateCheckState(@Param("diyIds")List<String> diyIds, @Param("checkState")int checkState, @Param("checkMem")String checkMem);

	List<DiyBoundApi> findBoundApi(@Param("diyApplyId")String diyApplyId, @Param("openApplyId")String openApplyId);

	List<DiyApplyEntity> findByIds(@Param("diyIds")List<String> diyIds);

	int deleteAll();

	int auditSuccess(@Param("applyId")String applyId, @Param("appId")String appId, @Param("checkState")Integer checkState, @Param("checkMem")String checkMem);

	List<BiDiyApply> findBiDiyByClientIds(@Param("diyClientIds") List<String> diyClientIds);
	
}
