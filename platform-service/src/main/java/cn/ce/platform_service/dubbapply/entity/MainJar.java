package cn.ce.platform_service.dubbapply.entity;

import java.util.Date;

/**
* @Description : 
* @Author : makangwei
* @Date : 2018年4月10日
*/
public class MainJar {

	private String id; 
	
	private String originalFileName;
	
	private boolean isDeleted;
	
	private boolean isParsed;
	
	private int jarSize;
	
	private String dfsPath;
	
	private Date createDate;
	
	private Date updateDate;
	
	private String userId;
	
	private String userName;
	
	private String enterpriseName;

	public MainJar(){}
	
	public MainJar(String id, boolean isDeleted, boolean isParsed, int jarSize, String dfsPath, Date createDate,
			Date updateDate, String userId, String userName, String enterpriseName) {
		super();
		this.id = id;
		this.isDeleted = isDeleted;
		this.isParsed = isParsed;
		this.jarSize = jarSize;
		this.dfsPath = dfsPath;
		this.createDate = createDate;
		this.updateDate = updateDate;
		this.userId = userId;
		this.userName = userName;
		this.enterpriseName = enterpriseName;
	}

	public String getOriginalFileName() {
		return originalFileName;
	}

	public void setOriginalFileName(String originalFileName) {
		this.originalFileName = originalFileName;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public boolean isDeleted() {
		return isDeleted;
	}

	public void setDeleted(boolean isDeleted) {
		this.isDeleted = isDeleted;
	}

	public boolean isParsed() {
		return isParsed;
	}

	public void setParsed(boolean isParsed) {
		this.isParsed = isParsed;
	}

	public int getJarSize() {
		return jarSize;
	}

	public void setJarSize(int jarSize) {
		this.jarSize = jarSize;
	}

	public String getDfsPath() {
		return dfsPath;
	}

	public void setDfsPath(String dfsPath) {
		this.dfsPath = dfsPath;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public Date getUpdateDate() {
		return updateDate;
	}

	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getEnterpriseName() {
		return enterpriseName;
	}

	public void setEnterpriseName(String enterpriseName) {
		this.enterpriseName = enterpriseName;
	}
	
}

