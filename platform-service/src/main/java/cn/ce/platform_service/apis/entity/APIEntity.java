package cn.ce.platform_service.apis.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import cn.ce.platform_service.apisecret.entity.ApiSecretKey;
import cn.ce.platform_service.app.entity.AppEntity;

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
	private String appid;
	/** 用户标识 */
	@Field("userid")
	private String userid;
	
	/** 用户姓名 */
	@Field("username")
	private String username;
	
	/** 接口名称 */
	@Field("apichname")
	private String apichname;
	/** 接口中文名称 */
	@Field("apienname")
	private String apienname;
	/** 接口地址 */
	@Field("testendpoint")
	private String testendpoint;
	/** 接口地址 */
	@Field("endpoint")
	private String endpoint;
	/** http方法GET或POST */
	@Field("httpmethod")
	private String httpmethod;
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
	@Field("retexample")
	private RetExamEntity retexample;
	/** 错误代码描述 */
	@Field("errcodes")
	private List<ErrorCodeEntity> errcodes;
	/** api接口版本信息 */
	@Field("apiversion")
	private ApiVersion apiversion;
	
	/** 接口描述 */
	@Field("desc")
	private String desc;
	/** 状态是否发布 */
	@Field("state")
	private int state;
	/** 调用次数限制（次/每天），0为不可调用，-1为无限制 */
	@Field("countbyday")
	private int countbyday;
	
	 /** 审核状态0:初始，1:提交审核，2:通过，3:未通过*/
    @Field("checkstate")
    private Integer checkState;
        
    /** 审核备注  */
    @Field("checkmem")
    private String checkMem;
    
    /** 最大配额*/
    @Field("quota_max")
    private int quota_max;
    
    /** 最大配额 重置周期 */
    @Field("quota_renewal_rate")
    private int quota_renewal_rate;
    
    /** 频次  */
    @Field("rate")
    private int rate;
    
    /** 访问频次计数周期  */
    @Field("per")
    private int per;
    
    @Field("createtime")
    private Date createtime;
    
    /***
     * 密钥列表
     */
    @Transient
    private List<ApiSecretKey> apiSecret;
    
    /** 服务分类信息 */
    @Transient
    private AppEntity app;
    
    
	public AppEntity getApp() {
		return app;
	}

	public void setApp(AppEntity app) {
		this.app = app;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public Date getCreatetime() {
		return createtime;
	}

	public void setCreatetime(Date createtime) {
		this.createtime = createtime;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getAppid() {
		return appid;
	}

	public void setAppid(String appid) {
		this.appid = appid;
	}

	public String getApichname() {
		return apichname;
	}

	public String getUserid() {
		return userid;
	}

	public void setUserid(String userid) {
		this.userid = userid;
	}

	public void setApichname(String apichname) {
		this.apichname = apichname;
	}

	public String getApienname() {
		return apienname;
	}

	public void setApienname(String apienname) {
		this.apienname = apienname;
	}

	public String getTestendpoint() {
		return testendpoint;
	}

	public void setTestendpoint(String testendpoint) {
		this.testendpoint = testendpoint;
	}

	public String getEndpoint() {
		return endpoint;
	}

	public void setEndpoint(String endpoint) {
		this.endpoint = endpoint;
	}

	public String getHttpmethod() {
		return httpmethod;
	}

	public void setHttpmethod(String httpmethod) {
		this.httpmethod = httpmethod;
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

	public RetExamEntity getRetexample() {
		return retexample;
	}

	public void setRetexample(RetExamEntity retexample) {
		this.retexample = retexample;
	}

	public List<ErrorCodeEntity> getErrcodes() {
		return errcodes;
	}

	public void setErrcodes(List<ErrorCodeEntity> errcodes) {
		this.errcodes = errcodes;
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

	public int getCountbyday() {
		return countbyday;
	}

	public void setCountbyday(int countbyday) {
		this.countbyday = countbyday;
	}

	public List<RetEntity> getResult() {
		return result;
	}

	public void setResult(List<RetEntity> result) {
		this.result = result;
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

	public int getQuota_max() {
		return quota_max;
	}

	public void setQuota_max(int quota_max) {
		this.quota_max = quota_max;
	}

	public int getQuota_renewal_rate() {
		return quota_renewal_rate;
	}

	public void setQuota_renewal_rate(int quota_renewal_rate) {
		this.quota_renewal_rate = quota_renewal_rate;
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

	public List<ApiSecretKey> getApiSecret() {
		return apiSecret;
	}

	public void setApiSecret(List<ApiSecretKey> apiSecret) {
		this.apiSecret = apiSecret;
	}

	public ApiVersion getApiversion() {
		return apiversion;
	}

	public void setApiversion(ApiVersion apiversion) {
		this.apiversion = apiversion;
	}

	@Override
	public String toString() {
		return "APIEntity [id=" + id + ", appid=" + appid + ", userid=" + userid + ", username=" + username
				+ ", apichname=" + apichname + ", apienname=" + apienname + ", testendpoint=" + testendpoint
				+ ", endpoint=" + endpoint + ", httpmethod=" + httpmethod + ", headers=" + headers + ", args=" + args
				+ ", result=" + result + ", retexample=" + retexample + ", errcodes=" + errcodes + ", apiversion="
				+ apiversion + ", desc=" + desc + ", state=" + state + ", countbyday=" + countbyday + ", checkState="
				+ checkState + ", checkMem=" + checkMem + ", quota_max=" + quota_max + ", quota_renewal_rate="
				+ quota_renewal_rate + ", rate=" + rate + ", per=" + per + ", createtime=" + createtime + ", apiSecret="
				+ apiSecret + ", app=" + app + "]";
	}
	
}
