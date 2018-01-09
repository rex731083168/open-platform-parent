//package cn.ce.platform_manage.test;
//
//import java.util.List;
//
//import javax.annotation.Resource;
//
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestMethod;
//import org.springframework.web.bind.annotation.RestController;
//
//import cn.ce.platform_service.common.Result;
//
///**
//* @Description : 说明
//* @Author : makangwei
//* @Date : 2018年1月8日
//*/
//@RestController
//@RequestMapping(value="/test")
//public class MysqlTest {
//
//	@Resource
//	private MysqlTestDao mysqlTestDao;
//	
//	@RequestMapping(value="/testMultiParam", method=RequestMethod.POST)
//	public Result<?> testMultiParam(@RequestBody Student student){
//		
//		Integer currentPage = 1;
//		Integer pageSize = 15;
//		List<Student> list =  mysqlTestDao.findByPage(student);
//		
//		Result<List<Student>> result = new Result<List<Student>>();
//		
//		result.setSuccessData(list);
//		
//		return result;
//	}
//}
