package cn.ce.platform_manage.test;

import java.util.List;

import org.springframework.transaction.annotation.Transactional;

/**
* @Description : 说明
* @Author : makangwei
* @Date : 2018年1月8日
*/
@Transactional
public interface MysqlTestDao {

	List<Student> findByPage(Student student);

}
