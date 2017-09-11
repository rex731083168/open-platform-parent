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
	@Field("username")
	private String username;
	/** 密码 */
	@Field("password")
	private String password;
	/** 邮箱 */
	@Field("email")
	private String email;
	/** 电话 */
	@Field("tel")
	private String tel;
	/** 状态 */
	@Field("state")
	private int state;
	/** 所属服务分类 */
	@Field("appid")
	private String appid;
	/** 用户类型  0:管理员，1:普通用户，2:提供者 */
	@Field("userType")
	private int userType;
	/** 注册时间 */
	@Field("regtime")
	private Date regtime;

	/** 审核状态0:初始，1:提交审核，2:通过，3:未通过 */
	@Field("checkstate")
	private int checkState;

	/** 审核备注 */
	@Field("checkmem")
	private String checkMem;

	/** 签名秘钥 */
	private String appSecret;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
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

	public String getTel() {
		return tel;
	}

	public void setTel(String tel) {
		this.tel = tel;
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

	public String getAppid() {
		return appid;
	}

	public void setAppid(String appid) {
		this.appid = appid;
	}

	public int getUserType() {
		return userType;
	}

	public void setUserType(int userType) {
		this.userType = userType;
	}

	public Date getRegtime() {
		return regtime;
	}

	public void setRegtime(Date regtime) {
		this.regtime = regtime;
	}

	public String getAppSecret() {
		return appSecret;
	}

	public void setAppSecret(String appSecret) {
		this.appSecret = appSecret;
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

}
