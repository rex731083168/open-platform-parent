package cn.ce.platform_manage.test;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.web.multipart.MultipartFile;

/**
* @Description : 说明
* @Author : makangwei
* @Date : 2017年9月30日
*/
@Document(collection="API_INSTRUCTIONS")
public class FileEntity {

	@Id
	private String fileId;
	@Field("name")
	private String name;
	@Field("type")
	private String type;
	@Field("size")
	private Integer size;     
	@Field("instruction")     
	private MultipartFile file;
	
	
	public String getFileId() {
		return fileId;
	}
	public void setFileId(String fileId) {
		this.fileId = fileId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public Integer getSize() {
		return size;
	}
	public void setSize(Integer size) {
		this.size = size;
	}
	public MultipartFile getFile() {
		return file;
	}
	public void setFile(MultipartFile file) {
		this.file = file;
	}
     
}
