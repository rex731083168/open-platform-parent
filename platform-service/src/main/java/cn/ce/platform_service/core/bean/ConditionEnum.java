package cn.ce.platform_service.core.bean;

/***
 * 
 * 查询mongodb条件控制枚举类
 * @author lida
 * @date 2017年8月8日13:31:38
 * 
 */
public enum ConditionEnum {
	LIKE, // like
	EQ, // ==
	NEQ, // !=
	UNION, 
	OR,//or
	LT, // <
	LTE, // <=
	BE, // >
	IN,
	NOTIN,
	BTE; // >=
}
