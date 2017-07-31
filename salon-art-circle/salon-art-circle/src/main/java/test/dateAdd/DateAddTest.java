package test.dateAdd;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateAddTest {
	
	 public static void main(String[] args) {
	        Date date = new Date();// 新建此时的的系统时间
	        System.out.println("当前时间        :"+Long.valueOf(new SimpleDateFormat("yyyyMMddHHmmss").format(new Date())));
	        System.out.println("----------------------");
	        System.out.println("当前时间加1天:"+Long.valueOf(new SimpleDateFormat("yyyyMMddHHmmss").format(getNextDay(date,1))));// 返回明天的时间
	        System.out.println("----------------------");
	        System.out.println("当前时间加1月:"+Long.valueOf(new SimpleDateFormat("yyyyMMddHHmmss").format(getNextMonth(date,1))));// 返回明天的时间
	    }
	 
	    public static Date getNextDay(Date date,int day) {
	        Calendar calendar = Calendar.getInstance();
	        calendar.setTime(date);
	        calendar.add(Calendar.DAY_OF_MONTH, +day);//+1今天的时间加一天
	        date = calendar.getTime();
	        return date;
	    }
	    
	    public static Date getNextMonth(Date date,int month) {
	    	Calendar calendar = Calendar.getInstance();
	    	calendar.setTime(date);
	    	calendar.add(Calendar.MONTH, +3);//+1今天的时间加一天
	    	date = calendar.getTime();
	    	return date;
	    }
	    
	    public static Date another(){
	    	Date today = new Date();
	    	if(today.getMonth()<12)
	    		today.setMonth(today.getMonth()+1);
	    	else
	    		today.setYear(today.getYear()+1);
	    	return today;
	    }
	    
}






