package cn.ce.platform_service.apis.entity;

import java.io.Serializable;

/**
 * 
 * @ClassName: RetExamEntity
 * @Description: 返回值示例类型
 * @author dingjia@300.cn
 *
 */
public class RetExamEntity implements Serializable{
    
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/** 示例名称  */
    private String exname;
    /** 类型 */
    private String extype;
    /** 状态码 */
    private String statecode;
    /** 值  */
    private String exvalue;
    
    public String getExname() {
        return exname;
    }

    public void setExname(String exname) {
        this.exname = exname;
    }

    public String getExtype() {
        return extype;
    }

    public void setExtype(String extype) {
        this.extype = extype;
    }

    public String getStatecode() {
        return statecode;
    }

    public void setStatecode(String statecode) {
        this.statecode = statecode;
    }

    public String getExvalue() {
        return exvalue;
    }

    public void setExvalue(String exvalue) {
        this.exvalue = exvalue;
    }

	@Override
	public String toString() {
		return "RetExamEntity [exname=" + exname + ", extype=" + extype + ", statecode=" + statecode + ", exvalue="
				+ exvalue + "]";
	}
    
}
