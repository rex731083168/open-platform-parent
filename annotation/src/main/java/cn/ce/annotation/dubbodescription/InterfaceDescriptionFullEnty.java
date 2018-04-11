package cn.ce.annotation.dubbodescription;

/**
 * 自定义注解解析实体类
 * 
 * @author huangdayang 2018年3月19日
 */
public class InterfaceDescriptionFullEnty extends InterfaceDescriptionEnty {
	
	//包名
	private String packagename;
	//类名
	private String classname;
	//方法名
	private String method;
	
	public String getPackagename() {
		return packagename;
	}
	public void setPackagename(String packagename) {
		this.packagename = packagename;
	}
	public String getClassname() {
		return classname;
	}
	public void setClassname(String classname) {
		this.classname = classname;
	}
	public String getMethod() {
		return method;
	}
	public void setMethod(String method) {
		this.method = method;
	}
	
	@Override
	public String toString() {
		return "InterfaceDescriptionFullEnty [packagename=" + packagename + ", classname=" + classname + ", method="
				+ method + ", toString()=" + super.toString() + "]";
	}
	
	

}
