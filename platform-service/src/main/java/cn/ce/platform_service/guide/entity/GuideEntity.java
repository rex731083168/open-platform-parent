package cn.ce.platform_service.guide.entity;

import java.io.Serializable;
import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

/**
 *
 * @Title: Guide.java
 * @Description
 * @version V1.0
 * @author yang.yu@300.cn
 * @date dat2017年10月12日 time下午5:49:59
 *
 **/
@Document(collection = "API_GUIDE")
public class GuideEntity implements Serializable {

	private static final long serialVersionUID = -9010780710345447535L;
	@Id
	private String id;
	@Field("guideName")
	private String guideName; // 指南名稱
	@Field("guideDesc")
	private String guideDesc; // 指南描述
	@Field("applyId")
	private String applyId; // 應用id
	@Field("creatUserName")
	private String creatUserName; // 創建人
	@Field("creatTime") // 創建時間
	private Date creatTime;
	/** 审核状态0:初始，1:提交审核，2:通过，3:未通过 */
	@Field("checkState")
	private Integer checkState;
	/** 审核备注 */
	@Field("checkMem")
	private String checkMem;

	public String getCheckMem() {
		return checkMem;
	}

	public void setCheckMem(String checkMem) {
		this.checkMem = checkMem;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getGuideName() {
		return guideName;
	}

	public void setGuideName(String guideName) {
		this.guideName = guideName;
	}

	public String getGuideDesc() {
		return guideDesc;
	}

	public void setGuideDesc(String guideDesc) {
		this.guideDesc = guideDesc;
	}

	public String getAppIyId() {
		return applyId;
	}

	public void setAppIyId(String applyId) {
		this.applyId = applyId;
	}

	public String getCreatUserName() {
		return creatUserName;
	}

	public void setCreatUserName(String creatUserName) {
		this.creatUserName = creatUserName;
	}

	public Date getCreatTime() {
		return creatTime;
	}

	public void setCreatTime(Date creatTime) {
		this.creatTime = creatTime;
	}

	public Integer getCheckState() {
		return checkState;
	}

	public void setCheckState(Integer checkState) {
		this.checkState = checkState;
	}

}
