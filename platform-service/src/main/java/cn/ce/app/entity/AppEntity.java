package cn.ce.app.entity;

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
	@Field("userid")
	private String userid;
	
	/** 提供者姓名 */
	@Field("username")
	private String username;
	
	@Field("appkey")
	private String appkey;
	
	/** 功能分组名称 */
	@Field("appname")
	private String appname;
	
	@Field("createdate")
	private Date createDate;
	
	 /** 审核状态0:初始，1:提交审核，2:通过，3:未通过*/
    @Field("checkstate")
    private Integer checkState;
        
    /** 审核备注  */
    @Field("checkmem")
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

	public String getUserid() {
		return userid;
	}

	public void setUserid(String userid) {
		this.userid = userid;
	}

	public String getAppname() {
		return appname;
	}

	public void setAppname(String appname) {
		this.appname = appname;
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

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getAppkey() {
		return appkey;
	}

	public void setAppkey(String appkey) {
		this.appkey = appkey;
	}

	public String getNeqId() {
		return neqId;
	}

	public void setNeqId(String neqId) {
		this.neqId = neqId;
	}
	
	

}
