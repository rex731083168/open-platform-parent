package cn.ce.platform_service.apis.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import cn.ce.platform_service.openApply.entity.OpenApplyEntity;

/**
 * 
 * @ClassName: APIEntity
 * @Description: 接口类型
 * @author dingjia@300.cn
 *
 */
@Document(collection = "APIMG_APIS")
public class ApiEntity implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/** 唯一标识 */
	@Id
	private String id;
	/** 所属功能分组的标识 */
	/**
	 * important : 这里存放的是会员中心返回来的他们维护的开放应用的主键
	 */
	@Field("openApplyId")
	private String openApplyId;
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
//	@Field("apiEnName")
//	private String apiEnName;
	/*所属开放应用的code码*/
	@Field("appCode") 
	private String appCode;

	
	/** 接口地址  创建api的时候定义该地址。当saas-id找不到真实地址的时候就会访问该地址 
	 * 	目前该字段没有用到
	 * */
	@Field("endPoint")
	private String endPoint;
	
	@Field("defaultTargetUrl")
	private String defaultTargetUrl;
	
	@Field("listenPath")
	private String listenPath;
	private String protocol; //协议。http或者https
	private String orgPath; //回原地址。非必填
	
	/** http方法GET或POST */
	@Field("httpMethod")
	private String httpMethod;
	
	@Deprecated
	/** http header参数 */
	@Field("headers")
	private List<SubArgEntity> headers;
	/** 应用参数 */
	@Field("args")
	@Deprecated
	private List<SubArgEntity> args;
	
	private List<SubArgEntity> queryArgs; //查询参数
	
	@Deprecated
	/** 返回结果描述 */
	@Field("result")
	private List<RetEntity> result;
	@Deprecated
	/** 返回结果示例 */
	@Field("retExample")
	private RetExamEntity retExample;
	@Deprecated
	/** 错误代码描述 */
	@Field("errCodes")
	private List<ErrorCodeEntity> errCodes;
	/** api接口版本信息 */
	@Field("apiVersion")
	private ApiVersion apiVersion;
	
	/** 开放OPEN，保留RETAIN*/
	@Field("apiType")
	private String apiType;
	
	/** 接口描述 */
	@Field("desc")
	private String desc;
	/** 状态是否可用  默认为0,禁用为1*/
	@Field("state")
	private int state;
	/** 调用次数限制（次/每天），0为不可调用，-1为无限制 */
	/** TODO 20171121 mkw 当前该参数没有用到*/
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
    
    /**
     * api来源 0,null代表提供者录入
     * 1代表文件导入
     * */
    @Field("apiSource")
    private Integer apiSource;
    
    @Field("enterpriseName")
    private String enterpriseName;
    
    
    /***
     * 资源类型
     */
    @Field("resourceType")
    private String resourceType;
    
    /***
     * 资源类型名称
     */
    @Field("resourceTypeName")
    private String resourceTypeName;
    
    private String requestBodyType; //请求body类型 :json/form
    
    private String responseBodyType; //返回参数类型:json/form
    
    /** 服务分类信息 */
    @Transient
    private OpenApplyEntity openApplyEntity;

    
    
	public String getResourceType() {
		return resourceType;
	}

	public void setResourceType(String resourceType) {
		this.resourceType = resourceType;
	}
	
	

	public String getResourceTypeName() {
		return resourceTypeName;
	}

	public void setResourceTypeName(String resourceTypeName) {
		this.resourceTypeName = resourceTypeName;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getOpenApplyId() {
		return openApplyId;
	}

	public void setOpenApplyId(String openApplyId) {
		this.openApplyId = openApplyId;
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

	public OpenApplyEntity getOpenApplyEntity() {
		return openApplyEntity;
	}

	public void setOpenApplyEntity(OpenApplyEntity openApplyEntity) {
		this.openApplyEntity = openApplyEntity;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public String getAppCode() {
		return appCode;
	}

	public void setAppCode(String appCode) {
		this.appCode = appCode;
	}
	
	

	public String getListenPath() {
		return listenPath;
	}

	public void setListenPath(String listenPath) {
		this.listenPath = listenPath;
	}
	
	

	public String getApiType() {
		return apiType;
	}

	public void setApiType(String apiType) {
		this.apiType = apiType;
	}

	public Integer getApiSource() {
		return apiSource;
	}

	public void setApiSource(Integer apiSource) {
		this.apiSource = apiSource;
	}

	public String getEnterpriseName() {
		return enterpriseName;
	}

	public void setEnterpriseName(String enterpriseName) {
		this.enterpriseName = enterpriseName;
	}
	
	public String getDefaultTargetUrl() {
		return defaultTargetUrl;
	}

	public void setDefaultTargetUrl(String defaultTargetUrl) {
		this.defaultTargetUrl = defaultTargetUrl;
	}
	
	public String getRequestBodyType() {
		return requestBodyType;
	}

	public void setRequestBodyType(String requestBodyType) {
		this.requestBodyType = requestBodyType;
	}

	public String getResponseBodyType() {
		return responseBodyType;
	}

	public void setResponseBodyType(String responseBodyType) {
		this.responseBodyType = responseBodyType;
	}

	public List<SubArgEntity> getQueryArgs() {
		return queryArgs;
	}

	public void setQueryArgs(List<SubArgEntity> queryArgs) {
		this.queryArgs = queryArgs;
	}

	public String getProtocol() {
		return protocol;
	}

	public void setProtocol(String protocol) {
		this.protocol = protocol;
	}

	public String getOrgPath() {
		return orgPath;
	}

	public void setOrgPath(String orgPath) {
		this.orgPath = orgPath;
	}

	@Override
	public String toString() {
		return "ApiEntity [id=" + id + ", openApplyId=" + openApplyId + ", userId=" + userId + ", userName=" + userName
				+ ", apiChName=" + apiChName + ", appCode=" + appCode + ", endPoint=" + endPoint + ", defaultTargetUrl="
				+ defaultTargetUrl + ", listenPath=" + listenPath + ", protocol=" + protocol + ", orgPath=" + orgPath
				+ ", httpMethod=" + httpMethod + ", headers=" + headers + ", args=" + args + ", queryArgs=" + queryArgs
				+ ", result=" + result + ", retExample=" + retExample + ", errCodes=" + errCodes + ", apiVersion="
				+ apiVersion + ", apiType=" + apiType + ", desc=" + desc + ", state=" + state + ", countByDay="
				+ countByDay + ", checkState=" + checkState + ", checkMem=" + checkMem + ", quotaMax=" + quotaMax
				+ ", quotaRenewalRate=" + quotaRenewalRate + ", rate=" + rate + ", per=" + per + ", createTime="
				+ createTime + ", apiSource=" + apiSource + ", enterpriseName=" + enterpriseName + ", resourceType="
				+ resourceType + ", resourceTypeName=" + resourceTypeName + ", requestBodyType=" + requestBodyType
				+ ", responseBodyType=" + responseBodyType + ", openApplyEntity=" + openApplyEntity + "]";
	}

	
}
