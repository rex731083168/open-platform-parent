package cn.ce.platform_service.apis.entity;

/**
* @Description : api参数实体
* @Author : makangwei
* @Date : 2018年1月19日
*/
public class ApiArgEntity {

	private String id;
	private String apiId;
	private String argName;
	private String argType;
	private boolean required; 
	private String example;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getApiId() {
		return apiId;
	}
	public void setApiId(String apiId) {
		this.apiId = apiId;
	}
	public String getArgName() {
		return argName;
	}
	public void setArgName(String argName) {
		this.argName = argName;
	}
	public String getArgType() {
		return argType;
	}
	public void setArgType(String argType) {
		this.argType = argType;
	}
	public boolean isRequired() {
		return required;
	}
	public void setRequired(boolean required) {
		this.required = required;
	}
	public String getExample() {
		return example;
	}
	public void setExample(String example) {
		this.example = example;
	}
	@Override
	public String toString() {
		return "ApiArgEntity [id=" + id + ", apiId=" + apiId + ", argName=" + argName + ", argType=" + argType
				+ ", required=" + required + ", example=" + example + ", getId()=" + getId() + ", getApiId()="
				+ getApiId() + ", getArgName()=" + getArgName() + ", getArgType()=" + getArgType() + ", isRequired()="
				+ isRequired() + ", getExample()=" + getExample() + ", getClass()=" + getClass() + ", hashCode()="
				+ hashCode() + ", toString()=" + super.toString() + "]";
	}
	
}
