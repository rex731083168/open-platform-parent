package cn.ce.platform_service.apis.entity;

/***
 * 
 * 
 * @ClassName:  QueryApiEntity   
 * @Description:api查询实体对象 
 * @author: author 
 * @date:   2017年10月16日 下午5:12:00   
 * @Copyright: 2017 中企动力科技股份有限公司 © 1999-2017 300.cn All Rights Reserved
 *
 */
public class QueryApiEntity {
	
	/** 应用id */
	private String openApplyId;
	
	/** 用户类型 */
	private Integer userType;
	
	/** 用户id */
	private String userId;
	
	/** 审核状态 */
	private Integer checkState;
	
	/** apichName */
	private String apiChName;
	
	private String apiType;
	
	private String enterpriseName;
	
	private Integer apiSource;

	public String getOpenApplyId() {
		return openApplyId;
	}

	public void setOpenApplyId(String openApplyId) {
		this.openApplyId = openApplyId;
	}

	public Integer getUserType() {
		return userType;
	}

	public void setUserType(Integer userType) {
		this.userType = userType;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public Integer getCheckState() {
		return checkState;
	}

	public void setCheckState(Integer checkState) {
		this.checkState = checkState;
	}

	public String getApiChName() {
		return apiChName;
	}

	public void setApiChName(String apiChName) {
		this.apiChName = apiChName;
	}

	public String getApiType() {
		return apiType;
	}

	public void setApiType(String apiType) {
		this.apiType = apiType;
	}

	public String getEnterpriseName() {
		return enterpriseName;
	}

	public void setEnterpriseName(String enterpriseName) {
		this.enterpriseName = enterpriseName;
	}

	public Integer getApiSource() {
		return apiSource;
	}

	public void setApiSource(Integer apiSource) {
		this.apiSource = apiSource;
	}

}
