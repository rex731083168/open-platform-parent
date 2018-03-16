package cn.ce.test;

import java.util.UUID;

/**
* @Description : 说明
* @Author : makangwei
* @Date : 2018年1月16日
*/
public class UuidTest {

	public static void main(String[] args) {
		for(int i=0; i<100; i++){
			UUID uuid = UUID.randomUUID();
			String s = uuid.toString();
			System.out.println(s);
		}
	}
	
	
	
}
