package cn.ce.platform_service.apisecret.entity;

import java.io.Serializable;
import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import cn.ce.platform_service.apis.entity.APIEntity;

/**
 * 
 * @ClassName: ApiSecretKey
 * @Description: API秘钥实体
 * @author lida
 * @date 2017年8月9日16:06:55
 *
 */
@Document(collection = "API_SECRET_KEY")
public class ApiSecretKey implements Serializable{
	
	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	@Id
	private String id;
	
	/**
	 * 秘钥
	 */
	@Field("secretKey")
	private String secretKey;
	
	/***
	 * 申请人id
	 */
	@Field("userId")
	private String userId;
	
	/***
	 * 申请人姓名
	 */
	@Field("userName")
	private String userName;
	
	/***
	 * 申请时间
	 */
	@Field("createDate")
	private Date createDate;
	
	/***
	 * apiId
	 */
	@Field("apiId")
	private String apiId;
	
	 /** 审核状态0:初始，1:提交审核，2:通过，3:未通过*/
    @Field("checkState")
    private Integer checkState;
        
    /** 审核备注  */
    @Field("checkMem")
    private String checkMem;
	
	@Transient
	private APIEntity api;
	

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getSecretKey() {
		return secretKey;
	}

	public void setSecretKey(String secretKey) {
		this.secretKey = secretKey;
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

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public String getApiId() {
		return apiId;
	}

	public void setApiId(String apiId) {
		this.apiId = apiId;
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

	public APIEntity getApi() {
		return api;
	}

	public void setApi(APIEntity api) {
		this.api = api;
	}

	
}
