package cn.ce.platform_service.apis.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import cn.ce.platform_service.apisecret.entity.ApiSecretKey;
import cn.ce.platform_service.openApply.entity.OpenApplyEntity;

/**
 * 
 * @ClassName: APIEntity
 * @Description: 接口类型
 * @author dingjia@300.cn
 *
 */
@Document(collection = "APIMG_APIS")
public class APIEntity implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/** 唯一标识 */
	@Id
	private String id;
	/** 所属功能分组的标识 */
	@Field("appid")
	private String appId;
	/** 用户标识 */
	@Field("userId")
	private String userId;
	
	/** 用户姓名 */
	@Field("userName")
	private String userName;
	
	/** 接口名称 */
	@Field("apiChName")
	private String apiChName;
	/** 接口中文名称 */
	@Field("apiEnName")
	private String apiEnName;
	/** 接口地址 */
	@Field("testEndPoint")
	private String testEndPoint;
	/** 接口地址 */
	@Field("endPoint")
	private String endPoint;
	/** http方法GET或POST */
	@Field("httpMethod")
	private String httpMethod;
	/** http header参数 */
	@Field("headers")
	private List<SubArgEntity> headers;
	/** 应用参数 */
	@Field("args")
	private List<SubArgEntity> args;
	/** 返回结果描述 */
	@Field("result")
	private List<RetEntity> result;
	/** 返回结果示例 */
	@Field("retExample")
	private RetExamEntity retExample;
	/** 错误代码描述 */
	@Field("errCodes")
	private List<ErrorCodeEntity> errCodes;
	/** api接口版本信息 */
	@Field("apiVersion")
	private ApiVersion apiVersion;
	
	/** 接口描述 */
	@Field("desc")
	private String desc;
	/** 状态是否发布 */
	@Field("state")
	private int state;
	/** 调用次数限制（次/每天），0为不可调用，-1为无限制 */
	@Field("countByDay")
	private int countByDay;
	
	 /** 审核状态0:初始，1:提交审核，2:通过，3:未通过*/
    @Field("checkState")
    private Integer checkState;
        
    /** 审核备注  */
    @Field("checkMem")
    private String checkMem;
    
    /** 最大配额*/
    @Field("quotaMax")
    private int quotaMax;
    
    /** 最大配额 重置周期 */
    @Field("quotaRenewalRate")
    private int quotaRenewalRate;
    
    /** 频次  */
    @Field("rate")
    private int rate;
    
    /** 访问频次计数周期  */
    @Field("per")
    private int per;
    
    @Field("createTime")
    private Date createTime;
    
    /***
     * 密钥列表
     */
    @Transient
    private List<ApiSecretKey> apiSecret;
    
    /** 服务分类信息 */
    @Transient
    private OpenApplyEntity appEntity;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getAppId() {
		return appId;
	}

	public void setAppId(String appId) {
		this.appId = appId;
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

	public String getApiChName() {
		return apiChName;
	}

	public void setApiChName(String apiChName) {
		this.apiChName = apiChName;
	}

	public String getApiEnName() {
		return apiEnName;
	}

	public void setApiEnName(String apiEnName) {
		this.apiEnName = apiEnName;
	}

	public String getTestEndPoint() {
		return testEndPoint;
	}

	public void setTestEndPoint(String testEndPoint) {
		this.testEndPoint = testEndPoint;
	}

	public String getEndPoint() {
		return endPoint;
	}

	public void setEndPoint(String endPoint) {
		this.endPoint = endPoint;
	}

	public String getHttpMethod() {
		return httpMethod;
	}

	public void setHttpMethod(String httpMethod) {
		this.httpMethod = httpMethod;
	}

	public List<SubArgEntity> getHeaders() {
		return headers;
	}

	public void setHeaders(List<SubArgEntity> headers) {
		this.headers = headers;
	}

	public List<SubArgEntity> getArgs() {
		return args;
	}

	public void setArgs(List<SubArgEntity> args) {
		this.args = args;
	}

	public List<RetEntity> getResult() {
		return result;
	}

	public void setResult(List<RetEntity> result) {
		this.result = result;
	}

	public RetExamEntity getRetExample() {
		return retExample;
	}

	public void setRetExample(RetExamEntity retExample) {
		this.retExample = retExample;
	}

	public List<ErrorCodeEntity> getErrCodes() {
		return errCodes;
	}

	public void setErrCodes(List<ErrorCodeEntity> errCodes) {
		this.errCodes = errCodes;
	}

	public ApiVersion getApiVersion() {
		return apiVersion;
	}

	public void setApiVersion(ApiVersion apiVersion) {
		this.apiVersion = apiVersion;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

	public int getCountByDay() {
		return countByDay;
	}

	public void setCountByDay(int countByDay) {
		this.countByDay = countByDay;
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

	public int getQuotaMax() {
		return quotaMax;
	}

	public void setQuotaMax(int quotaMax) {
		this.quotaMax = quotaMax;
	}

	public int getQuotaRenewalRate() {
		return quotaRenewalRate;
	}

	public void setQuotaRenewalRate(int quotaRenewalRate) {
		this.quotaRenewalRate = quotaRenewalRate;
	}

	public int getRate() {
		return rate;
	}

	public void setRate(int rate) {
		this.rate = rate;
	}

	public int getPer() {
		return per;
	}

	public void setPer(int per) {
		this.per = per;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public List<ApiSecretKey> getApiSecret() {
		return apiSecret;
	}

	public void setApiSecret(List<ApiSecretKey> apiSecret) {
		this.apiSecret = apiSecret;
	}

	public OpenApplyEntity getAppEntity() {
		return appEntity;
	}

	public void setAppEntity(OpenApplyEntity appEntity) {
		this.appEntity = appEntity;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	@Override
	public String toString() {
		return "APIEntity [id=" + id + ", appId=" + appId + ", userId=" + userId + ", userName=" + userName
				+ ", apiChName=" + apiChName + ", apiEnName=" + apiEnName + ", testEndPoint=" + testEndPoint
				+ ", endPoint=" + endPoint + ", httpMethod=" + httpMethod + ", headers=" + headers + ", args=" + args
				+ ", result=" + result + ", retExample=" + retExample + ", errCodes=" + errCodes + ", apiVersion="
				+ apiVersion + ", desc=" + desc + ", state=" + state + ", countByDay=" + countByDay + ", checkState="
				+ checkState + ", checkMem=" + checkMem + ", quotaMax=" + quotaMax + ", quotaRenewalRate="
				+ quotaRenewalRate + ", rate=" + rate + ", per=" + per + ", createTime=" + createTime + ", apiSecret="
				+ apiSecret + ", appEntity=" + appEntity + "]";
	}
}
