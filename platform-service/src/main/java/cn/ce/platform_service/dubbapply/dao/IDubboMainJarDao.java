package cn.ce.platform_service.dubbapply.dao;

import cn.ce.platform_service.dubbapply.entity.MainJar;

/**
* @Description : dubbo 上传下载jar包 记录保存数据库
* @Author : makangwei
* @Date : 2018年4月10日
*/
public interface IDubboMainJarDao {

	int saveMainJar(MainJar jarEntity);

	MainJar findMainJarById(String mainJarId);

	int updateMainJar(MainJar main);

}

