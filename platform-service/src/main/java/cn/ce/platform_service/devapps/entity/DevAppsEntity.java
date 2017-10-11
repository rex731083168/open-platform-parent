package cn.ce.platform_service.devapps.entity;

import java.io.File;
import java.io.Serializable;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

/**
 *
 * @Title: DevAppsEntity.java
 * @Description
 * @version V1.0
 * @author yang.yu@300.cn
 * @date dat2017年10月11日 time下午1:34:48
 *
 **/
@Document(collection = "DEV_CUS_APPLY")
public class DevAppsEntity implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -59565070682411237L;

	/** 唯一标识 */
	@Id
	private String id;

	/** 提供者标识 */
	@Field("userId")
	private String userId;

	/** 提供者姓名 */
	@Field("userName")
	private String userName;

	/** 应用名称 */
	@Field("")
	private String devAppName;
	/** 产品授权码 */
	@Field("")
	private String productAuthCode;
	/** 访问域名 */
	@Field("")
	private String domainUrl;
	/** 应用logo */
	@Field("")
	private byte[] logoImage;

	/** 应用描述 */
	@Field("")
	private String appDescription;
	/** 限流类型 1频次 2配合 */
	@Field("")
	private String currentLimitingType;
	/** 频次 || 配额 */
	@Field("")
	private String flowFrequency;
	/** 频次时间范围 单位 s */
	@Field("")
	private String dateScope;

	/** 审核状态0:初始，1:提交审核，2:通过，3:未通过 */
	@Field("checkState")
	private Integer checkState;

	/** 审核备注 */
	@Field("checkMem")
	private String checkMem;

	/** dao判断刨除当前修改id的数据进行判断时赋值 */
	@Transient
	private String neqId;

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

	public String getDateScope() {
		return dateScope;
	}

	public void setDateScope(String dateScope) {
		this.dateScope = dateScope;
	}

	public String getCurrentLimitingType() {
		return currentLimitingType;
	}

	public void setCurrentLimitingType(String currentLimitingType) {
		this.currentLimitingType = currentLimitingType;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getDevAppName() {
		return devAppName;
	}

	public void setDevAppName(String devAppName) {
		this.devAppName = devAppName;
	}

	public String getProductAuthCode() {
		return productAuthCode;
	}

	public void setProductAuthCode(String productAuthCode) {
		this.productAuthCode = productAuthCode;
	}

	public String getDomainUrl() {
		return domainUrl;
	}

	public void setDomainUrl(String domainUrl) {
		this.domainUrl = domainUrl;
	}

	public String getAppDescription() {
		return appDescription;
	}

	public void setAppDescription(String appDescription) {
		this.appDescription = appDescription;
	}

	public String getFlowFrequency() {
		return flowFrequency;
	}

	public void setFlowFrequency(String flowFrequency) {
		this.flowFrequency = flowFrequency;
	}

	public byte[] getLogoImage() {
		return logoImage;
	}

	public void setLogoImage(byte[] logoImage) {
		this.logoImage = logoImage;
	}
}
