package cn.ce.platform_console.dubbo.controller;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import cn.ce.annotation.dubbodescription.InterfaceDescriptionFullEnty;
import cn.ce.platform_service.common.Constants;
import cn.ce.platform_service.common.Result;
import cn.ce.platform_service.dubbapply.service.IDubboApplyService;
import cn.ce.platform_service.dubbapply.service.IJarService;
import cn.ce.platform_service.dubbapply.util.JarDfsUtil;
import cn.ce.platform_service.users.entity.User;
import cn.ce.platform_service.util.ModuleClassLoader;
import cn.ce.platform_service.util.PageValidateUtil;
import cn.ce.platform_service.util.SplitUtil;

@RestController
@RequestMapping("/dubboJar")
public class dubboJarController {

	private static final Logger _LOGGER = LoggerFactory.getLogger(dubboJarController.class);
//	@Autowired
//	private IGetAppListSercice getAppListSercice;
	@Resource
	private IDubboApplyService dubboApplyService;
	@Resource
	private IJarService jarService;

	/**
	 * @Title: findPhysicsApps
	 * @Description: 获取物理应用列表
	 * @author: makangwei 
	 * @date:   2018年4月10日 下午5:54:21 
	 */
	@RequestMapping(value="/findPhysicApps", method=RequestMethod.POST)
	public Result<?> findLogicalApps(){
		// TODO 获取逻辑应用。需要王佳接口支持
		return null;
	}
	
	/**
	 * 
	 * @Title: findAppsByUnit
	 * @Description: 根据逻辑单元标识获取物理应用
	 * @author: makangwei 
	 * @date:   2018年4月19日 下午1:39:36 
	 * @param : @param unit
	 * @param : @param appName
	 * @param : @param currentPage
	 * @param : @param pageSize
	 * @param : @return
	 * @return: Result<DubboApps>
	 * @throws
	 */
	@RequestMapping(value = "/findAppsByUnit", method = RequestMethod.GET)
	public Result<?> findAppsByUnit(@RequestParam(required = true, defaultValue="166666") String unit,
		@RequestParam(required=false)String appName,
		@RequestParam(required=false, defaultValue="1")Integer currentPage,
		@RequestParam(required=false, defaultValue="10")Integer pageSize) {
		// TODO 优化分页，模糊查询
		return dubboApplyService.findAppsByUnit(unit,appName
				,PageValidateUtil.checkCurrentPage(currentPage),PageValidateUtil.checkPageSize(pageSize));
	}
	
	/**
	 * 
	 * @Title: uploadMainJar
	 * @Description: 上传主jar
	 * @author: makangwei 
	 * @date:   2018年4月11日 上午10:06:54 
	 */
	@RequestMapping(value="/uploadMainJar", method=RequestMethod.POST)
	public Result<?> uploadMainJar(HttpServletRequest request,
			@RequestParam("file") MultipartFile mainJar){
		
		User user = (User)request.getSession().getAttribute(Constants.SES_LOGIN_USER);

		 // TODO 和物理应用绑定
		return jarService.uploadMainJar(mainJar,user);
	}

	/**
	 * 
	 * @Title: updateMainJar
	 * @Description: 跟新主包文件
	 * @author: makangwei 
	 * @date:   2018年4月19日 下午3:39:07 
	 * @param : @param mainJarId 主包id
	 * @param : @param mainJar 新的主包
	 * @param : @return
	 * @return: Result<?>
	 * @throws
	 */
	@RequestMapping(value="/updateMainJar", method=RequestMethod.PUT)
	public Result<?> updateMainJar(@RequestParam String mainJarId,
			@RequestParam("file") MultipartFile mainJar){
		
		return jarService.updateMainJar(mainJarId, mainJar);
	}
	
	/**
	 * 
	 * @Title: deletedMainJar
	 * @Description: 删除主jar包。同时删除依赖jar包
	 * @author: makangwei 
	 * @date:   2018年4月19日 下午3:23:06 
	 * @param : @param mainJarId 主包id
	 * @param : @return
	 * @return: Result<?>
	 * @throws
	 */
	@RequestMapping(value="/deleteMainJar", method=RequestMethod.DELETE)
	public Result<?> deletedMainJar(@RequestParam String mainJarId){
		
		return jarService.deletedMainJar(mainJarId);
	}
	
	/**
	 * 
	 * @Title: uploadDepJar
	 * @Description: 根据主包批量上传依赖包
	 * @author: makangwei 
	 * @date:   2018年4月19日 下午3:38:16 
	 * @param : @param mainJarId 主包id
	 * @param : @param depJar 依赖包file
	 * @param : @return
	 * @return: Result<?>
	 * @throws
	 */
	@RequestMapping(value="/uploadDepJarJar", method=RequestMethod.POST)
	public Result<?> uploadDepJar(HttpServletRequest request,
			@RequestParam(required=true) String mainJarId,
			@RequestParam(required=true) MultipartFile[] depJar ){
		User user = (User)request.getSession().getAttribute(Constants.SES_LOGIN_USER);

		return jarService.uploadDepJar(mainJarId, depJar, user);
	}
	
	public Result<?> updateSingleDepJar(HttpServletRequest request,
			@RequestParam String depJarId,
			@RequestParam("file")MultipartFile file){
		User user = (User)request.getSession().getAttribute(Constants.SES_LOGIN_USER);
		return jarService.updateSingleDepJar(depJarId,file);
	}
	
	public Result<?> deleteSingleDepJar(@RequestParam String depJarId){
		
		return jarService.deleteSingleDepJar(depJarId);
	}
	
	public Result<?> deleteDepJars(@RequestParam String depJars){
		
		List<String> depJarIds = SplitUtil.splitStringWithComma(depJars);
		
		return jarService.deleteDepJars(depJarIds);
	}

    /**    
    * @Description: ${todo}(用一句话描述该文件做什么)  
    * @author ${user}  
    * @date ${date}  
    * @update date/author/content ${date}	${date}/${user}/(说明。)....多次修改添加多次update   
    */  
	@RequestMapping(value="/testParse",method=RequestMethod.GET)
	public Result<?> testParse() throws Exception{
////		File file = new File("D:/lib/framework-pbase-2.0.0-20180328.074441-7.jar");
////		File file2 = new File("D:/lib/service-info-api-2.0.0-SNAPSHOT.jar");
////		File file3 = new File("D:/lib/appservice-info-api-2.0.0-SNAPSHOT.jars");
//		File[] files = new File[]{
//				new File("D:\\lib\\framework-pbase-2.0.0-20180328.074441-7.jar")
//				,new File("D:\\lib\\service-info-api-2.0.0-SNAPSHOT.jar")
//				,new File("D:\\lib\\appservice-info-api-2.0.0-SNAPSHOT.jar")};
//		Map<String,String> map = new HashMap<String,String>();
//		for (File fi : files) {
//			System.out.println(fi.exists());
//			FileInputStream f = new FileInputStream(fi);
//			byte b[] =new byte[(int) fi.length()];
//			f.read(b);
//			System.out.println(fi.getName());
//			System.out.println(new String(b));
//			String path = JarDfsUtil.getInstance().saveFile(fi.getName(), b);
//			System.out.println(fi.getName()+":"+path);
//			map.put(fi.getName(), path);
//		}
//		List<String> fList = new ArrayList<String>();
//		for (String key : map.keySet()) {
//			byte[] b = JarDfsUtil.getInstance().readFile(map.get(key));
//			File f = new File(this.getClass().getResource("/").getPath()+"fastdfsTemp",key);
//			if(!f.exists()){
//				f.createNewFile();
//			}
//			FileOutputStream fis = new FileOutputStream(f);
//			BufferedOutputStream bis = new BufferedOutputStream(fis);
//			bis.write(b);
//			bis.close();
//			fis.close();
//			System.out.println(f.getAbsolutePath());
//			fList.add(f.getAbsolutePath());
//		}
//		
//		String mainJarPath = null;
//		for (String string : fList) {
//			if(string.indexOf("appservice-info-api-2.0.0-SNAPSHOT.jar")> 0){
//				mainJarPath = string;
//				break;
//			}
//		}
//		String[] strs = new String[3];
//		for (int i = 0; i < strs.length; i++) {
//			strs[i] =  fList.get(i);
//		}
//		System.out.println(mainJarPath);
//		System.out.println(strs);
////		Map<String, Map<String, InterfaceDescriptionFullEnty>> m = ModuleClassLoader.getInstance().parseJars(mainJarPath,strs);
////		for (String string : m.keySet()) {
////			System.out.println("key:"+string+",value"+m.get(string));
////			Map<String, InterfaceDescriptionFullEnty> map2 = m.get(string);
////			for (String string2 : map2.keySet()) {
////				System.out.println("key :"+string2+",value:"+map2.get(string2));
////			}
////		}
//		String[] path = new String[]{"D:/lib/framework-pbase-2.0.0-20180328.074441-7.jar"
//				,"D:/lib/service-info-api-2.0.0-SNAPSHOT.jar"
//				,"D:/lib/appservice-info-api-2.0.0-SNAPSHOT.jar"};
//		String mainJar = "D:/lib/appservice-info-api-2.0.0-SNAPSHOT.jar";
//		Map<String, Map<String, InterfaceDescriptionFullEnty>> map1 = ModuleClassLoader.getInstance().parseJars(mainJar,path);
//		for (String string : map1.keySet()) {
//			System.out.println("key:"+string+",value"+map1.get(string));
//			Map<String, InterfaceDescriptionFullEnty> map2 = map1.get(string);
//			for (String string2 : map2.keySet()) {
//				System.out.println("key :"+string2+",value:"+map2.get(string2));
//			}
//		}
		String[] path = new String[]{"D:/lib/framework-pbase-2.0.0-20180328.074441-7.jar"
				,"D:/lib/service-info-api-2.0.0-SNAPSHOT.jar"
				,"D:/lib/appservice-info-api-2.0.0-SNAPSHOT.jar"};
		String mainJar = "D:/lib/appservice-info-api-2.0.0-SNAPSHOT.jar";
		Map<String, Map<String, InterfaceDescriptionFullEnty>> map1 = ModuleClassLoader.getInstance().parseJars(mainJar,path);
		for (String string : map1.keySet()) {
			System.out.println("key:"+string+",value"+map1.get(string));
			Map<String, InterfaceDescriptionFullEnty> map2 = map1.get(string);
			for (String string2 : map2.keySet()) {
				System.out.println("key :"+string2+",value:"+map2.get(string2));
			}
		}
		return null;
	}
	
	public static void main(String[] args) throws ClassNotFoundException, URISyntaxException, IOException {
		String[] path = new String[]{"D:/lib/framework-pbase-2.0.0-20180328.074441-7.jar"
				,"D:/lib/service-info-api-2.0.0-SNAPSHOT.jar"
				,"D:/lib/appservice-info-api-2.0.0-SNAPSHOT.jar"};
		String mainJar = "D:/lib/appservice-info-api-2.0.0-SNAPSHOT.jar";
		Map<String, Map<String, InterfaceDescriptionFullEnty>> map1 = ModuleClassLoader.getInstance().parseJars(mainJar,path);
		for (String string : map1.keySet()) {
			System.out.println("key:"+string+",value"+map1.get(string));
			Map<String, InterfaceDescriptionFullEnty> map2 = map1.get(string);
			for (String string2 : map2.keySet()) {
				System.out.println("key :"+string2+",value:"+map2.get(string2));
			}
		}
	}
//	@RequestMapping(value = "/testUpload", method=RequestMethod.POST)
//	public Result<?> testUpload(MultipartFile file) throws Exception {
//	
//	System.out.println(file.getOriginalFilename());
//	String saveList = JarDfsUtil.getInstance().saveFile(file.getOriginalFilename(), file.getBytes());
//	System.out.println(saveList);
//	return new Result<String>("messageaaa",ErrorCodeNo.SYS000,saveList,Status.SUCCESS);
//		
//	}
//	
//	@RequestMapping(value = "/testGet", method=RequestMethod.GET)
//	public Result<?> testGet(@RequestParam String fid) throws Exception {
//		
//	byte[] saveList = JarDfsUtil.getInstance().readFile(fid);
//	return new Result<String>("messageaaa",ErrorCodeNo.SYS000,new String(saveList),Status.SUCCESS);
//		
//	}
//	
//	@RequestMapping(value = "/testDelete", method=RequestMethod.DELETE)
//	public Result<?> testDelete(@RequestParam String fid) throws Exception{
//		
//	boolean bool = JarDfsUtil.getInstance().deleteFile(fid);
//	
//	return new Result<Boolean>("messageaaa",ErrorCodeNo.SYS000,bool,Status.SUCCESS);
//		
//	}
//	
//	@RequestMapping(value = "/testUpdate", method=RequestMethod.POST)
//	public Result<?> testUpdate(@RequestParam String fid,
//			MultipartFile file) throws Exception {
//		
//	String str = JarDfsUtil.getInstance().updateFile(fid, file.getOriginalFilename(), file.getBytes());
//	return new Result<String>("messageaaa",ErrorCodeNo.SYS000,str,Status.SUCCESS);
//		
//	}
	 
}
