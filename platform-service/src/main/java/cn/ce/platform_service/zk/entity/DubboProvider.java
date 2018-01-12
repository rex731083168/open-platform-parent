package cn.ce.platform_service.zk.entity;

import java.util.Arrays;
import java.util.List;

import org.springframework.data.mongodb.core.mapping.Document;

/**
* @Description : 说明
* @Author : makangwei
* @Date : 2018年1月9日
*/
@Document(collection= "DUBBO_PROVIDER")
public class DubboProvider {

	private String id;
	
	private String application; //应用名称
	
	private String path; //服务路径
	
	private String group; //分组
	
	private String version; //服务版本
	
	private String dubboVersion; //dubbo版本
	
	private String token; //验证令牌
	
	private String timeOut; //调用超时
	
	private String owner; //负责人
	
	private String interfaceName; //接口名称
	
	private String[] methods; //调用方法
	
	private String timeStamp; //时间戳
	
	private String loadBalance; //负载策略
	
	private String protocol; //协议
	
	private String uri;	//服务地址
	
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

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getGroup() {
		return group;
	}

	public void setGroup(String group) {
		this.group = group;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getDubboVersion() {
		return dubboVersion;
	}

	public void setDubboVersion(String dubboVersion) {
		this.dubboVersion = dubboVersion;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getTimeOut() {
		return timeOut;
	}

	public void setTimeOut(String timeOut) {
		this.timeOut = timeOut;
	}

	public String getOwner() {
		return owner;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}

	public String getInterfaceName() {
		return interfaceName;
	}

	public void setInterfaceName(String interfaceName) {
		this.interfaceName = interfaceName;
	}

	public String[] getMethods() {
		return methods;
	}

	public void setMethods(String[] methods) {
		this.methods = methods;
	}

	public String getTimeStamp() {
		return timeStamp;
	}

	public void setTimeStamp(String timeStamp) {
		this.timeStamp = timeStamp;
	}

	public String getLoadBalance() {
		return loadBalance;
	}

	public void setLoadBalance(String loadBalance) {
		this.loadBalance = loadBalance;
	}

	public String getProtocol() {
		return protocol;
	}

	public void setProtocol(String protocol) {
		this.protocol = protocol;
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
		return "DubboProvider [id=" + id + ", application=" + application + ", path=" + path + ", group=" + group
				+ ", version=" + version + ", dubboVersion=" + dubboVersion + ", token=" + token + ", timeOut="
				+ timeOut + ", owner=" + owner + ", interfaceName=" + interfaceName + ", methods="
				+ Arrays.toString(methods) + ", timeStamp=" + timeStamp + ", loadBalance=" + loadBalance + ", protocol="
				+ protocol + ", uri=" + uri + ", nodeId=" + nodeId + "]";
	}
	
}
