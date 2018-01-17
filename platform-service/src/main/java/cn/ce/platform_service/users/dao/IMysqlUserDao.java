package cn.ce.platform_service.users.dao;

import org.springframework.transaction.annotation.Transactional;

import cn.ce.platform_service.users.entity.User;

/**
* @Description : 说明
* @Author : makangwei
* @Date : 2018年1月17日
*/
@Transactional
public interface IMysqlUserDao {

	Integer save(User user);

}
