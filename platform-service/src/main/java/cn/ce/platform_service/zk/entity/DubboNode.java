package cn.ce.platform_service.zk.entity;

import org.springframework.data.mongodb.core.mapping.Document;

/**
* @Description : dubbo node 实体
* @Author : makangwei
* @Date : 2018年1月9日
*/
@Document(collection= "DUBBO_NODE")
public class DubboNode {

	private String id;
	
	private String nodeName;

	private String rootId;
	
	public DubboNode(){
	}
	public DubboNode(String nodeName){
		this.nodeName = nodeName;
	}
	public DubboNode(String nodeName, String rootId){
		this.nodeName = nodeName;
		this.rootId = rootId;
	}
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getNodeName() {
		return nodeName;
	}
	public void setNodeName(String nodeName) {
		this.nodeName = nodeName;
	}
	public String getRootId() {
		return rootId;
	}
	public void setRootId(String rootId) {
		this.rootId = rootId;
	}
	
	@Override
	public String toString() {
		return "DubboNode [id=" + id + ", nodeName=" + nodeName + ", rootId=" + rootId + "]";
	}
	
}
