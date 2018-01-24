package cn.ce.platform_service.diyApply.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.transaction.annotation.Transactional;

import cn.ce.platform_service.diyApply.entity.DiyApplyEntity;
import cn.ce.platform_service.diyApply.entity.DiyApplyQueryEntity;

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
			@Param("openApplyId")String openApplyId,@Param("apiId") String apiId);

	int findListSize(DiyApplyQueryEntity queryApply);

	List<DiyApplyEntity> getPagedList(DiyApplyQueryEntity queryApply);

	DiyApplyEntity findById(String applyId);

	int deleteById(String applyId);

	int checkApplyName(@Param("userId")String userId, @Param("applyName")String applyName);

	int update(DiyApplyEntity diyApplyEntity);
	
}
