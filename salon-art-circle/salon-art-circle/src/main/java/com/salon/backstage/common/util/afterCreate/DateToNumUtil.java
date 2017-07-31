package com.salon.backstage.common.util.afterCreate;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateToNumUtil {
	
	/**
	 * 时间转为Integer(精确到年月日)
	 */
	public static Integer DateToInteger(Date date) {
		int num = Integer.valueOf(new SimpleDateFormat("yyyyMMdd").format(date));
		return num;
	}
	
	/**
	 * 时间转为Long(精确到年月日时分秒)
	 */
	public static Long DateToLong(Date date) {
//		long num = Long.valueOf(new SimpleDateFormat("yyyyMMddHHmmss").format(date));
		long num = date.getTime();
		return num;
	}
	
}
