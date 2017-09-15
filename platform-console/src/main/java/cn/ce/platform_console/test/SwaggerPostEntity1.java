package cn.ce.platform_console.test;

import java.util.List;

/**
* @Description : 说明
* @Author : makangwei
* @Date : 2017年9月13日
*/
public class SwaggerPostEntity1 {

	private String userName;
	
	private String password;
	
	private List<String> hobbys;
	
	private String[] majors;
	
	private School school;

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public List<String> getHobbys() {
		return hobbys;
	}

	public void setHobbys(List<String> hobbys) {
		this.hobbys = hobbys;
	}

	public String[] getMajors() {
		return majors;
	}

	public void setMajors(String[] majors) {
		this.majors = majors;
	}

	public School getSchool() {
		return school;
	}

	public void setSchool(School school) {
		this.school = school;
	}
}

