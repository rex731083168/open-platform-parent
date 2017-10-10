package cn.ce.platform_service.app.entity;

import java.io.Serializable;
import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

/**
 * 
 * @ClassName: AppEntity
 * @Description: 服务分组类型
 * @author dingjia@300.cn
 *
 */
@Document(collection = "APIMG_APPS")
public class AppEntity implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/** 唯一标识 */
	@Id
	private String id;
	
	/** 提供者标识 */
	@Field("userId")
	private String userId;
	
	/** 提供者姓名 */
	@Field("userName")
	private String userName;
	
	@Field("appKey")
	private String appKey;
	
	/** 功能分组名称 */
	@Field("appName")
	private String appName;
	
	@Field("createDate")
	private Date createDate;
	
	 /** 审核状态0:初始，1:提交审核，2:通过，3:未通过*/
    @Field("checkState")
    private Integer checkState;
        
    /** 审核备注  */
    @Field("checkMem")
    private String checkMem;
    
    /** dao判断刨除当前修改id的数据进行判断时赋值 */
    @Transient
    private String neqId;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
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

	public String getAppKey() {
		return appKey;
	}

	public void setAppKey(String appKey) {
		this.appKey = appKey;
	}

	public String getAppName() {
		return appName;
	}

	public void setAppName(String appName) {
		this.appName = appName;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public Integer getCheckState() {
		return checkState;
	}

	public void setCheckState(Integer checkState) {
		this.checkState = checkState;
	}

	public String getCheckMem() {
		return checkMem;
	}

	public void setCheckMem(String checkMem) {
		this.checkMem = checkMem;
	}

	public String getNeqId() {
		return neqId;
	}

	public void setNeqId(String neqId) {
		this.neqId = neqId;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	@Override
	public String toString() {
		return "AppEntity [id=" + id + ", userId=" + userId + ", userName=" + userName + ", appKey=" + appKey
				+ ", appName=" + appName + ", createDate=" + createDate + ", checkState=" + checkState + ", checkMem="
				+ checkMem + ", neqId=" + neqId + "]";
	}

}
