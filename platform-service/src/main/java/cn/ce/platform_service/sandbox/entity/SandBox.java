package cn.ce.platform_service.sandbox.entity;

import java.util.Date;

/**
* @Description : 说明
* @Author : makangwei
* @Date : 2018年3月23日
*/
public class SandBox {

	private String boxId; //沙箱名称，是沙箱环境的唯一标识。后台生成
	
	private String name; //沙箱名称,用于在页面上显示
	
	private Template templateName; //生成沙箱所使用的模板名称
	
	private String boxUrl; //沙箱地址
	
	private String resourcePool; //所属沙箱资源
	
	private CreateState createState; //创建时状态
	
	private int state; //状态：0禁用，1启用
	
	private boolean deleted; //是否已经删除：0未删除,1已删除
	
	private Date createDate; //创建时间
	
	private Date deleteDate; //删除时间
	
	private String userId; //创建者id
	
	private String enterpriseName; //所属组织

	public enum Template{
		test,prod;
	}
	public enum CreateState{
		BUILDDING,RUNNING,FAILED;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getBoxId() {
		return boxId;
	}

	public void setBoxId(String boxId) {
		this.boxId = boxId;
	}

	public Template getTemplateName() {
		return templateName;
	}

	public void setTemplateName(Template templateName) {
		this.templateName = templateName;
	}

	public String getBoxUrl() {
		return boxUrl;
	}

	public void setBoxUrl(String boxUrl) {
		this.boxUrl = boxUrl;
	}

	public String getResourcePool() {
		return resourcePool;
	}

	public void setResourcePool(String resourcePool) {
		this.resourcePool = resourcePool;
	}

	public CreateState getCreateState() {
		return createState;
	}

	public void setCreateState(CreateState createState) {
		this.createState = createState;
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

	public boolean isDeleted() {
		return deleted;
	}

	public void setDeleted(boolean deleted) {
		this.deleted = deleted;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public Date getDeleteDate() {
		return deleteDate;
	}

	public void setDeleteDate(Date deleteDate) {
		this.deleteDate = deleteDate;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getEnterpriseName() {
		return enterpriseName;
	}

	public void setEnterpriseName(String enterpriseName) {
		this.enterpriseName = enterpriseName;
	}
	
}

