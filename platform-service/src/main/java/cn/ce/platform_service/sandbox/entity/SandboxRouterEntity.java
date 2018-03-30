package cn.ce.platform_service.sandbox.entity;

import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

/**
* @Author : makangwei
* @Date : 2018年3月21日
*/
@Document(collection="sandbox_router")
public class SandboxRouterEntity {

	@Field("saas_id")
	private String saas_id;
	@Field("resource_type")
	private String resource_type;
	@Field("sandbox_id ")
	private String sandbox_id;
	@Field("target_url")
	private String target_url;
	
	public SandboxRouterEntity() {
		
	}
	
	public SandboxRouterEntity(String saas_id, String resource_type, String sandbox_id, String target_url) {
		super();
		this.saas_id = saas_id;
		this.resource_type = resource_type;
		this.sandbox_id = sandbox_id;
		this.target_url = target_url;
	}
	public String getSandbox_id() {
		return sandbox_id;
	}
	public void setSandbox_id(String sandbox_id) {
		this.sandbox_id = sandbox_id;
	}
	public String getSaas_id() {
		return saas_id;
	}
	public void setSaas_id(String saas_id) {
		this.saas_id = saas_id;
	}
	public String getResource_type() {
		return resource_type;
	}
	public void setResource_type(String resource_type) {
		this.resource_type = resource_type;
	}
	public String getTarget_url() {
		return target_url;
	}
	public void setTarget_url(String target_url) {
		this.target_url = target_url;
	}
	
}
