package cn.ce.platform_console.diyApply.controller;

/**
* @Description : 说明
* @Author : makangwei
* @Date : 2018年3月7日
*/
public class ResultMenu extends RegistMenu{
	
	private String appId;
	private String sort_des;	//菜单排序字段
	private int type;	//类型 1标品菜单,2定制化菜单
	private int parentType;	//父菜单类型 1为标品菜单，2为定制化菜单 
	private String logicalId; //逻辑菜单id
	private String logicalParentId;
	private String unit; //所属业务
	private String action; //权限标识
	public String getAppId() {
		return appId;
	}
	public void setAppId(String appId) {
		this.appId = appId;
	}
	public String getSort_des() {
		return sort_des;
	}
	public void setSort_des(String sort_des) {
		this.sort_des = sort_des;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public int getParentType() {
		return parentType;
	}
	public void setParentType(int parentType) {
		this.parentType = parentType;
	}
	public String getLogicalId() {
		return logicalId;
	}
	public void setLogicalId(String logicalId) {
		this.logicalId = logicalId;
	}
	public String getLogicalParentId() {
		return logicalParentId;
	}
	public void setLogicalParentId(String logicalParentId) {
		this.logicalParentId = logicalParentId;
	}
	public String getUnit() {
		return unit;
	}
	public void setUnit(String unit) {
		this.unit = unit;
	}
	public String getAction() {
		return action;
	}
	public void setAction(String action) {
		this.action = action;
	}
	@Override
	public String toString() {
		return super.toString()+"ResultMenu [appId=" + appId + ", sort_des=" + sort_des + ", type=" + type + ", parentType=" + parentType
				+ ", logicalId=" + logicalId + ", logicalParentId=" + logicalParentId + ", unit=" + unit + ", action="
				+ action + "]";
	}
	
	
	
}

