package cn.ce.platform_service.dubbapply.entity;

import java.util.Date;

public class DubboApplyEntity {

	private String pkid;
	private String id;
	private String parentId;
	private String type; // 应用->jar 服务 ->Service 接口-> interface
	private String name;
	private String des;
	private String version;
	private String filePath; // jar file path
	private String tag; // jar type 1 service 2 depency
	private Date creatTime;
	private String creatUserName;
	private String creatUserId;

	public String getPkid() {
		return pkid;
	}

	public void setPkid(String pkid) {
		this.pkid = pkid;
	}

	public String getId() {
		return id;
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getParentId() {
		return parentId;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDes() {
		return des;
	}

	public void setDes(String des) {
		this.des = des;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getTag() {
		return tag;
	}

	public void setTag(String tag) {
		this.tag = tag;
	}

	public Date getCreatTime() {
		return creatTime;
	}

	public void setCreatTime(Date creatTime) {
		this.creatTime = creatTime;
	}

	public String getCreatUserName() {
		return creatUserName;
	}

	public void setCreatUserName(String creatUserName) {
		this.creatUserName = creatUserName;
	}

	public String getCreatUserId() {
		return creatUserId;
	}

	public void setCreatUserId(String creatUserId) {
		this.creatUserId = creatUserId;
	}
}
