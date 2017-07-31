package com.salon.backstage.common.util.afterCreate;

import java.util.Calendar;
import java.util.Date;

public class DateAddUtil {
	
	/**
	 * 当前时间增加[天]
	 */
	public static Date addDay(Date date,int day) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DAY_OF_MONTH, +day);
        date = calendar.getTime();
        return date;
    }
    
	/**
	 * 当前时间减少[天]
	 */
	public static Date descDay(Date date,int day) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.DAY_OF_MONTH, -day);
		date = calendar.getTime();
		return date;
	}
	
	/**
	 * 当前时间增加[月]
	 */
    public static Date addMonth(Date date,int month) {
    	Calendar calendar = Calendar.getInstance();
    	calendar.setTime(date);
    	calendar.add(Calendar.MONTH, +month);
    	date = calendar.getTime();
    	return date;
    }	
	
    /**
     * 当前时间减少[月]
     */
    public static Date descMonth(Date date,int month) {
    	Calendar calendar = Calendar.getInstance();
    	calendar.setTime(date);
    	calendar.add(Calendar.MONTH, -month);
    	date = calendar.getTime();
    	return date;
    }	
    
    /**
     * 当前时间增加[年]
     */
    public static Date addYear(Date date,int year) {
    	Calendar calendar = Calendar.getInstance();
    	calendar.setTime(date);
    	calendar.add(Calendar.YEAR, +year);
    	date = calendar.getTime();
    	return date;
    }	
    
    /**
     * 当前时间减少[年]
     */
    public static Date descYear(Date date,int year) {
    	Calendar calendar = Calendar.getInstance();
    	calendar.setTime(date);
    	calendar.add(Calendar.YEAR, -year);
    	date = calendar.getTime();
    	return date;
    }	
    
}











