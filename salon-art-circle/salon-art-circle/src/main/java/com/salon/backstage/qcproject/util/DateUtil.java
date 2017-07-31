package com.salon.backstage.qcproject.util;

import java.util.Calendar;

/**
 * 作者：齐潮
 * 创建日期：2017年2月27日
 * 类说明：时间工具类
 */
public class DateUtil {
	
	/**
	 * 获得某月第一天的凌晨00:00:00的时间撮
	 * @param now
	 * @param addMonth
	 * @return
	 */
	public final static long getMonthFirstDayMillions(long now,int addMonth){
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(now);
		calendar.add(Calendar.MONTH, addMonth);
		calendar.set(Calendar.DAY_OF_MONTH, 1);
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		return calendar.getTimeInMillis();
	}

	/**
	 * 获取当前月份所在季度的开始月份，这里的月份从0-11
	 * @param nowMonth
	 * @return
	 * @throws Exception 
	 */
	public final static int getFirstMonthForQuarter(int nowMonth) throws Exception{
		if(nowMonth>=0&&nowMonth<=2)
			return 0;
		if(nowMonth>=3&&nowMonth<=5)
			return 3;
		if(nowMonth>=6&&nowMonth<=8)
			return 6;
		if(nowMonth>=9&&nowMonth<=11)
			return 9;
		throw new Exception("月份不合理！");
	}
	
}
