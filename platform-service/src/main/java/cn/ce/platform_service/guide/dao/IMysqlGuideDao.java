package cn.ce.platform_service.guide.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.transaction.annotation.Transactional;

import cn.ce.platform_service.guide.entity.GuideEntity;
import cn.ce.platform_service.guide.entity.QueryGuideEntity;

/**
* @Description : 说明
* @Author : makangwei
* @Date : 2018年1月18日
*/
@Transactional
public interface IMysqlGuideDao {

	int save(GuideEntity guideEntity);

	int checkGuideName(@Param("guideName")String guideName, @Param("openApplyId")String openApplyId);

	int updateGuide(GuideEntity g);

	GuideEntity findById(String guideId);

	int findTotalNum(QueryGuideEntity entity);

	List<GuideEntity> getList(QueryGuideEntity entity);

	int deleteById(String guideId);

	//int batchSubmit(@Param("guideIds")List<String> guideIds, @Param("checkState")int checkState);

	int bathUpdateCheckState(@Param("guideIds")List<String> guideIds, @Param("checkState")int checkState, @Param("checkMem")String checkMem);
	
}
