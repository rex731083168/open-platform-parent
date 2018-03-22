package cn.ce.platform_service.gateway.entity;

import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

/**
* @Author : makangwei
* @Date : 2018年3月21日
*/
@Document(collection="saas")
public class SaasEntity {

	@Field("saas_id")
	private String saasId;
	@Field("resource_type")
	private String resourceType;
	@Field("target_url")
	private String targetUrl;
	
	public String getSaasId() {
		return saasId;
	}
	public void setSaasId(String saasId) {
		this.saasId = saasId;
	}
	public String getResourceType() {
		return resourceType;
	}
	public void setResourceType(String resourceType) {
		this.resourceType = resourceType;
	}
	public String getTargetUrl() {
		return targetUrl;
	}
	public void setTargetUrl(String targetUrl) {
		this.targetUrl = targetUrl;
	}
	@Override
	public String toString() {
		return "SaasEntity [saasId=" + saasId + ", resourceType=" + resourceType + ", targetUrl=" + targetUrl + "]";
	}
	
}
