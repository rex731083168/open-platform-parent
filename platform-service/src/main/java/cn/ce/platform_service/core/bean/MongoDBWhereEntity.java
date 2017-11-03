package cn.ce.platform_service.core.bean;

/***
 * 
 * 查询mondoDB条件存储实体
 * @author lida
 * @date 2017年8月8日13:32:34
 * 
 */
public class MongoDBWhereEntity {

	private Object value; //值
	
	private ConditionEnum method;//方式
	
	public MongoDBWhereEntity() {
		// TODO Auto-generated constructor stub
	}
	
	public MongoDBWhereEntity(Object _value, ConditionEnum _method){
		this.value = _value;
		this.method = _method;
	}
	
	public Object getValue() {
		return value;
	}
	public void setValue(Object value) {
		this.value = value;
	}
	
	public ConditionEnum getMethod() {
		return method;
	}
	
	public void setMethod(ConditionEnum method) {
		this.method = method;
	}
}

