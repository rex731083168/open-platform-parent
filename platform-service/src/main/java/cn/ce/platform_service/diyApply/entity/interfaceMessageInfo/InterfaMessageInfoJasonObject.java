/**
  * Copyright 2017 bejson.com 
  */
package cn.ce.platform_service.diyApply.entity.interfaceMessageInfo;

import net.sf.json.JSONObject;

/**
 * Auto-generated: 2017-10-13 16:18:9
 *
 * @author bejson.com (i@bejson.com)
 * @website http://www.bejson.com/java2pojo/
 */
public class InterfaMessageInfoJasonObject {

	private JSONObject data;
	private String msg;
	private int status;

	public JSONObject getData() {
		return data;
	}

	public void setData(JSONObject data) {
		this.data = data;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public String getMsg() {
		return msg;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public int getStatus() {
		return status;
	}

}