package cn.ce.platform_service.zk.entity;

import org.springframework.data.mongodb.core.mapping.Document;

/**
* @Description : 说明
* @Author : makangwei
* @Date : 2018年1月9日
*/
@Document(collection= "DUBBO_CONSUMER")
public class DubboConsumer {

	private String id;
	
	private String application; //应用名称
	
	private String category; //类型-消费者
	
	private String thradPoolCoreSize; //线程池大小
	
	private String interfaceName; //接口名称
	
	private String methods; //调用方法

	private String uri;	//consumer的ip
	
	private String nodeId; 
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getApplication() {
		return application;
	}

	public void setApplication(String application) {
		this.application = application;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getThradPoolCoreSize() {
		return thradPoolCoreSize;
	}

	public void setThradPoolCoreSize(String thradPoolCoreSize) {
		this.thradPoolCoreSize = thradPoolCoreSize;
	}

	public String getInterfaceName() {
		return interfaceName;
	}

	public void setInterfaceName(String interfaceName) {
		this.interfaceName = interfaceName;
	}

	public String getMethods() {
		return methods;
	}

	public void setMethods(String methods) {
		this.methods = methods;
	}

	public String getUri() {
		return uri;
	}

	public void setUri(String uri) {
		this.uri = uri;
	}
	
	public String getNodeId() {
		return nodeId;
	}

	public void setNodeId(String nodeId) {
		this.nodeId = nodeId;
	}

	@Override
	public String toString() {
		return "DubboConsumer [id=" + id + ", application=" + application + ", catetory=" + category
				+ ", thradPoolCoreSize=" + thradPoolCoreSize + ", interfaceName=" + interfaceName + ", methods="
				+ methods + ", uri=" + uri + ", nodeId=" + nodeId + "]";
	}
	
}
