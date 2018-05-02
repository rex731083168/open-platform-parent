package cn.ce.platform_console.dubbo.controller;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import cn.ce.platform_service.common.Constants;
import cn.ce.platform_service.common.Result;
import cn.ce.platform_service.dubbapply.service.IDubboApplyService;
import cn.ce.platform_service.dubbapply.service.IJarService;
import cn.ce.platform_service.users.entity.User;
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
