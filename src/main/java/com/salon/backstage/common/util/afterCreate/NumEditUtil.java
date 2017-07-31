package com.salon.backstage.common.util.afterCreate;


public class NumEditUtil {
	
	/**
	 * 数据库中的时间取到年月日
	 */
	public static Integer LongSub(Long lo){
		String st = ""+lo;
		String stNum = st.substring(0,8);
		int retNum = Integer.valueOf(stNum);
		return retNum;
	}
	
	/**
	 * Long 转为 String
	 */
	public static String LongToString(Long lo){
		String st = ""+lo;
		return st;
	}
	
	/**
	 * Integer 转为 String
	 */
	public static String LongToString(Integer it){
		String st = ""+it;
		return st;
	}
	
}
