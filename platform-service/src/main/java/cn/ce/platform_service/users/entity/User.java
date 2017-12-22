package cn.ce.platform_service.users.entity;

import java.io.Serializable;
import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

/**
 * @author 作者 E -mail: dingjia@300.cn 创建时间：2017年7月17日 下午3:53:13
 * @version V1.0 类说明
 */
@Document(collection = "APIMG_USERS")
public class User implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/** 唯一标识 */
	@Id
	private String id;
	/** 用户名称 */
	@Field("userName")
	private String userName;
	/** 密码 */
	@Field("password")
	private String password;
	/** 邮箱 */
	@Field("email")
	private String email;
	/** 电话 */
	@Field("telNumber")
	private String telNumber;

	/******************** 认证时需要的信息 ************************/
	@Field("enterpriseName")
	private String enterpriseName; // 企业名称

	@Field("idCard")
	private String idCard; // 身份证号码

	@Field("userRealName")
	private String userRealName; // 真实姓名

	/**
	 * 组织机构编码,标识当前用户的组织
	 */
	@Field("orgId")
	private String orgId;

	/** 组织机构名称 冗余字段 */
	@Field("orgName")
	private String orgName;

	/** 签名秘钥 */
	private String appSecret;

	/** 状态  0:禁用，1:启用*/
	@Field("state") 
	private int state;
	/** 所属服务分类 */
	@Field("appId")
	private String appId;
	/** 用户类型 0:管理员，1:普通用户，2:提供者 */
	@Field("userType")
	private int userType;
	/** 注册时间 */
	@Field("regTime")
	private Date regTime;
	@Field("checkCode")
	private Integer checkCode; // 短信校验码

	/** 审核状态0:初始，1:提交审核，2:通过，3:未通过 */
	@Field("checkState")
	private int checkState;

	/** 审核备注 */
	@Field("checkMem")
	private String checkMem;

	public User() {
		super();
	}

	public User(String id, String userName, String password, String email, String telNumber, int state, String appId,
			int userType, Date regTime, int checkState, String checkMem, String orgId, String appSecret) {
		super();
		this.id = id;
		this.userName = userName;
		this.password = password;
		this.email = email;
		this.telNumber = telNumber;
		this.state = state;
		this.appId = appId;
		this.userType = userType;
		this.regTime = regTime;
		this.checkState = checkState;
		this.checkMem = checkMem;
		this.orgId = orgId;
		this.appSecret = appSecret;
	}

	public String getOrgId() {
		return orgId;
	}

	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getTelNumber() {
		return telNumber;
	}

	public void setTelNumber(String telNumber) {
		this.telNumber = telNumber;
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

	public String getAppId() {
		return appId;
	}

	public void setAppId(String appId) {
		this.appId = appId;
	}

	public int getUserType() {
		return userType;
	}

	public void setUserType(int userType) {
		this.userType = userType;
	}

	public Date getRegTime() {
		return regTime;
	}

	public void setRegTime(Date regTime) {
		this.regTime = regTime;
	}

	public int getCheckState() {
		return checkState;
	}

	public void setCheckState(int checkState) {
		this.checkState = checkState;
	}

	public String getCheckMem() {
		return checkMem;
	}

	public void setCheckMem(String checkMem) {
		this.checkMem = checkMem;
	}

	public String getAppSecret() {
		return appSecret;
	}

	public void setAppSecret(String appSecret) {
		this.appSecret = appSecret;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}



	public Integer getCheckCode() {
		return checkCode;
	}

	public void setCheckCode(Integer checkCode) {
		this.checkCode = checkCode;
	}

	public String getOrgName() {
		return orgName;
	}

	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}

	public String getEnterpriseName() {
		return enterpriseName;
	}

	public void setEnterpriseName(String enterpriseName) {
		this.enterpriseName = enterpriseName;
	}

	public String getUserRealName() {
		return userRealName;
	}

	public void setUserRealName(String userRealName) {
		this.userRealName = userRealName;
	}

	public String getIdCard() {
		return idCard;
	}

	public void setIdCard(String idCard) {
		this.idCard = idCard;
	}

	@Override
	public String toString() {
		return "User [id=" + id + ", userName=" + userName + ", password=" + password + ", email=" + email
				+ ", telNumber=" + telNumber + ", enterpriseName=" + enterpriseName + ", idCard=" + idCard
				+ ", userRealName=" + userRealName + ", orgId=" + orgId + ", orgName=" + orgName + ", appSecret="
				+ appSecret + ", state=" + state + ", appId=" + appId + ", userType=" + userType + ", regTime="
				+ regTime + ", checkCode=" + checkCode + ", checkState=" + checkState + ", checkMem=" + checkMem + "]";
	}

}
