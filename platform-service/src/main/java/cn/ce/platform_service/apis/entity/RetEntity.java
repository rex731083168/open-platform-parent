package cn.ce.platform_service.apis.entity;

import java.io.Serializable;


/**
 * 
 * @ClassName: RetEntity
 * @Description: 返回描述
 * @author dingjia@300.cn
 *
 */
public class RetEntity implements Serializable{

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/** 返回值名称 */
    private String retname;
    /** 类型 */
    private String rettype;
    /** 示例 */
    private String example;
    /** 说明  */
    private String desc;
    
    public String getRetname() {
        return retname;
    }

    public void setRetname(String retname) {
        this.retname = retname;
    }

    public String getRettype() {
        return rettype;
    }

    public void setRettype(String rettype) {
        this.rettype = rettype;
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

}
