package cn.ce.platform_service.zk.entity;

import org.springframework.data.mongodb.core.mapping.Document;

/**
* @Description : 说明
* @Author : makangwei
* @Date : 2018年1月11日
*/
@Document(collection= "DUBBO_ROOT")
public class DubboRoot {

	private String id;
	
	private String rootName;

	public DubboRoot(){
		
	}
	
	public DubboRoot(String rootName) {
		this.rootName = rootName;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getRootName() {
		return rootName;
	}

	public void setRootName(String rootName) {
		this.rootName = rootName;
	}

	@Override
	public String toString() {
		return "DubboRoot [id=" + id + ", rootName=" + rootName + ", getId()=" + getId() + ", getRootName()="
				+ getRootName() + ", getClass()=" + getClass() + ", hashCode()=" + hashCode() + ", toString()="
				+ super.toString() + "]";
	}
	
}
