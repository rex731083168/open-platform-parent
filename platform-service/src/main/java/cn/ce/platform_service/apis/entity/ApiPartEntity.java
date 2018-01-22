package cn.ce.platform_service.apis.entity;

/**
* @Description : 说明
* @Author : makangwei
* @Date : 2018年1月19日
*/
public class ApiPartEntity {

	private String id;
	
	private String partType;
	
	private String apiId;
	
	private String name; //字段名称
	
	private String type; //字段类型
	
	private boolean required;
	
	private String example;
	
	private String desc;

	@Override
	public String toString() {
		return "ApiPartEntity [id=" + id + ", partType=" + partType + ", apiId=" + apiId + ", name=" + name + ", type="
				+ type + ", required=" + required + ", example=" + example + ", desc=" + desc + "]";
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getPartType() {
		return partType;
	}

	public void setPartType(String partType) {
		this.partType = partType;
	}

	public String getApiId() {
		return apiId;
	}

	public void setApiId(String apiId) {
		this.apiId = apiId;
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

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	
}
