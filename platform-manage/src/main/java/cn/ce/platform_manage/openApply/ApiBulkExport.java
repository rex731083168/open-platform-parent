package cn.ce.platform_manage.openApply;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.codehaus.plexus.util.StringInputStream;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONArray;
import com.mongodb.MongoClient;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;

import cn.ce.platform_service.apis.entity.ApiEntity;
import cn.ce.platform_service.common.Result;

/**
* @Description : api在不同环境的批量导出功能
* @Author : makangwei
* @Date : 2017年12月6日
*/
@Controller("/apiBulkExport")
public class ApiBulkExport {
	
	@RequestMapping(value="/checkOpenApply", method=RequestMethod.POST)
	@ResponseBody
	public Result<?> checkOpenApply(){
		
		return null;
	}
	
	@RequestMapping(value="checkApiExists", method=RequestMethod.POST)
	@ResponseBody
	public Result<?> checkApiExists(){
		
		return null;
	}
	
	@RequestMapping(value="/bulkExport", method=RequestMethod.POST)
	@ResponseBody
	public Result<?> bulkExport(){
		
		return null;
	}
	
	@RequestMapping(value="/testExport", method=RequestMethod.GET)
	public void testExport(HttpServletResponse response) throws IOException{
		List<ServerAddress> list1 = new ArrayList<ServerAddress>();
		list1.add(new ServerAddress("10.12.40.83",23000));
		list1.add(new ServerAddress("10.12.40.84",23000));
		list1.add(new ServerAddress("10.12.40.86",23000));
		List<MongoCredential> list2 = new ArrayList<MongoCredential>();
		list2.add(MongoCredential.createCredential("makangwei", "admin","123456".toCharArray()));
		MongoClient client1 = new MongoClient(list1,list2);
		MongoOperations mongoOps = new MongoTemplate(client1,"openplatform");
		List<ApiEntity> list = mongoOps.findAll(ApiEntity.class);
		System.out.println(list.size());
		
		response.setCharacterEncoding("utf-8");
		response.setContentType("multipart/form-data");
		response.setHeader("Content-Disposition", "attachment:filename="+"filename1");
		
		ObjectOutputStream outputStream = new ObjectOutputStream(response.getOutputStream());
		outputStream.writeObject(list);
		outputStream.flush();
		outputStream.close();
	}
	
}
