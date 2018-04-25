package cn.ce.platform_service.dubbapply.util;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import cn.ce.dfs.DfsFolderClient;
import cn.ce.dfs.thrift.DfsException;
import cn.ce.dfs.thrift.HandleType;
import cn.ce.platform_service.exception.DfsClientException;

/**
* @Description : 上传和下载jar包工具类
* @Author : makangwei
* @Date : 2018年4月20日
*/
public class JarDfsUtil {

	private static final Logger _LOGGER = LoggerFactory.getLogger(JarDfsUtil.class);	
	
	private static DfsFolderClient dfsFolderClient;
	
	private JarDfsUtil() throws Exception {
		Resource res1 = new ClassPathResource("dfs.zk.properties");
		String dfsZkLocation = res1.getURL().getPath();
		_LOGGER.info("fastdfs zookeeper location:" + dfsZkLocation);
		dfsFolderClient = new DfsFolderClient();
		boolean bool = dfsFolderClient.init(dfsZkLocation);
		if(!bool){
			throw new DfsClientException();
		}
		
	}
	
	private static JarDfsUtil jarDfs = null;
	
	public static JarDfsUtil getInstance() throws Exception{
		if(null != jarDfs){
			return jarDfs;
		}
		return new JarDfsUtil();
	}
	
    /**
     * @throws Exception 
     * 
     * @Title: saveFile
     * @Description: 保存文件到服务器
     * @param fileName 文件的逻辑全路径名称
     * @param arrData 文件的字节数组
     * @param type 如果目标存在，则指定是覆盖、抛出异常还是重命名
     * @throws Exception 异常信息
     * @return List<String> 返回重命名后的新名称
     * @throws
     */
	public String saveFile(String fileName, byte[] arrData){
		
		if(StringUtils.isBlank(fileName)){
			throw new DfsClientException ("保存文件的文件名称不能为空");
		}
		List<String> saveList = null;
		try {
			saveList = dfsFolderClient.saveFile(fileName, arrData, HandleType.COVER_OLD);
		} catch (Exception e) {
			_LOGGER.error("调用fastdfs server 发生异常，异常信息如下：");
			e.printStackTrace();
			throw new DfsClientException ("保存文件到fastdfs案发生异常。请校验fastdfs服务是否可用");
		}
		/**
		    fastdfs 添加成功后的返回示例如下：
		    [
		    "hello.txt", //文件名。传入fileName返回fileName
		    "group1/M00/00/06/CgwzUlrZOlyAclD6AAAACwP9M4k439.txt" //文件id。后缀和文件名的后缀一致
  			]
		 */
		if(null != saveList){
			for (String string : saveList) {
				if(!fileName.equals(string)){
					return string; //只返回id。不再返回文件名
				}
			}
		}
		return null;
	}
	
	/**
	 * 
	 * @Title: updateFile
	 * @Description: 跟新jar包
	 * @author: makangwei 
	 * @date:   2018年4月23日 下午2:51:12 
	 * @param : @param fid 原来的jar包id
	 * @param : @param fileName 新jar包的文件名称
	 * @param : @param newData 新jar包的数据内容
	 * @param : @return
	 * @return: String
	 * @throws
	 */
	public String updateFile(String fid, String fileName, byte[] newData){
		
		if(StringUtils.isBlank(fid)){
			throw new DfsClientException ("文件id不能为空");
		}
		
		try {
			byte[] fileBytes = dfsFolderClient.readFileId(fid);
			if(null == fileBytes || fileBytes.length < 1){
				_LOGGER.info("当前fastdfs的文件id不存在");
				return null;
			}
			
			boolean bool = dfsFolderClient.deleteFileId(fid);
			if(bool){
				return this.saveFile(fileName, newData);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
    /**
     * 
     * @Title: readFileId
     * @Description: 根据文件的标识读取文件的内容
     * @param fid 文件的标识
     * @throws Exception 异常信息
     * @return byte[] 文件的字节数组内容
     * @throws
     */
	public byte[] readFile(String fid) throws Exception{
		
		if(StringUtils.isBlank(fid)){
			throw new DfsClientException ("保存文件的文件名称不能为空");
		}
		
		return dfsFolderClient.readFileId(fid);
	}
	
	public boolean deleteFile(String fid) throws Exception{
		
		if(StringUtils.isBlank(fid)){
			throw new DfsClientException ("保存文件的文件名称不能为空");
		}
		
		boolean bool = false;
		try{
			bool = dfsFolderClient.deleteFileId(fid);
		}catch(DfsException e){
			_LOGGER.info("当前id不存在");
		}
		if(bool){
			_LOGGER.info(new String(this.readFile(fid)));
		}
		return bool;
	}
	
}

