package cn.ce.platform_service.apis.entity;

import java.io.Serializable;


/**
 * 
 * @ClassName: SubArgEntity
 * @Description: 应用参数类型
 * @author dingjia@300.cn
 *
 */
public class SubArgEntity  implements Serializable{
    
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/** 名称 */
    private String argname;
    /** 类型 */
    private String argtype;
    /** 是否必须 */
    private boolean required;
    /** 示例值 */
    private String example;
    /** 说明 */
    private String desc;
    
    public String getArgname() {
        return argname;
    }
    public void setArgname(String argname) {
        this.argname = argname;
    }
    public String getArgtype() {
        return argtype;
    }
    public void setArgtype(String argtype) {
        this.argtype = argtype;
    }
    public boolean isRequired() {
        return required;
    }
    public void setRequired(boolean required) {
        this.required = required;
    }
    public String getExample() {
        return example;
    }
    public void setExample(String example) {
        this.example = example;
    }
    public String getDesc() {
        return desc;
    }
    public void setDesc(String desc) {
        this.desc = desc;
    }
	@Override
	public String toString() {
		return "SubArgEntity [argname=" + argname + ", argtype=" + argtype + ", required=" + required + ", example="
				+ example + ", desc=" + desc + "]";
	}
    
    
    
}
