package com.salon.backstage.common.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
import java.util.UUID;

public class PrimaryKeyUtil {
	
	/**
	 * 获取UUID，已经将"-"替换成了空字符串
	 * @param id
	 * @return
	 */
	public static String getUUID(String id) {
		return id + UUID.randomUUID().toString().replaceAll("-", "");
	}
	
	/**
	 * 获取订单编号(需要传入当前最大的订单编号)，如果传入了null，则会产生第一个订单编号
	 * @param nowMax
	 * @return
	 */
	public static String getOrderNum(String nowMax){
		if(nowMax==null||nowMax.trim().equals(""))
			return hebing(1, getRandom(6));
		long old = Long.parseLong(nowMax.substring(0,14));
		long newMath = old + 1;
		return hebing(newMath, getRandom(6));
	}
	
	/**
	 * 用于产生订单编号时最后的合并
	 * @param first
	 * @param last
	 * @return
	 */
	private static String hebing(long first,long last){
		String strFirst = first + "";
		int oldLenth = strFirst.length();
		StringBuffer str = new StringBuffer();
		for(int i=1;i<=(14-oldLenth);i++){
			str.append("0");
		}
		str.append(strFirst);
		str.append(last + "");
		return str.toString();
	}
	
	/**
	 * 随机出指定位数的随机数，如果是4位，随机范围为：1000-9999
	 * @param count
	 * @return
	 */
	public static long getRandom(int count){
		long max = (long) Math.pow(10, count);
		long min = (long) Math.pow(10, (count-1));
		long cha = max - min;
		double random = Math.random();
		long result = (long) (min + (random*cha));
		return result;
	}
	
	/**
	 * 消费码串
	 */
	public static String getPayCodeRandStr(String id){
		StringBuffer buf = new StringBuffer();
		Random random = new Random();
		buf.append(id);
		for(int i = 0; i < 4; i++){
            buf.append(random.nextInt(10));//取四个随机数追加到StringBuffer
	    }
		for(int i = 0; i < 4; i++){
            buf.append(random.nextInt(10));//取四个随机数追加到StringBuffer
	    }
		for(int i = 0; i < 4; i++){
            buf.append(random.nextInt(10));//取四个随机数追加到StringBuffer
	    }
	    return buf.toString();
	}
	
	/**
	 * 时间戳Ms+8位随机串
	 */
	public static String getTimeRandStr8(String id){
		StringBuffer buf = new StringBuffer();
		Random random = new Random();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSSS");
		String time = sdf.format(new Date());
		buf.append(id);
		buf.append(time);
		for(int i = 0; i < 4; i++){
            buf.append(random.nextInt(10));//取四个随机数追加到StringBuffer
	    }
		for(int i = 0; i < 4; i++){
            buf.append(random.nextInt(10));//取四个随机数追加到StringBuffer
	    }
	    return buf.toString();
	}
	
	/**
	 * 时间戳Ms+8位随机串
	 */
	public static String getTimeRandStr10(String id){
		StringBuffer buf = new StringBuffer();
		Random random = new Random();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSSS");
		String time = sdf.format(new Date());
		buf.append(id);
		buf.append(time);
		for(int i = 0; i < 5; i++){
            buf.append(random.nextInt(10));//取四个随机数追加到StringBuffer
	    }
		for(int i = 0; i < 5; i++){
            buf.append(random.nextInt(10));//取四个随机数追加到StringBuffer
	    }
	    return buf.toString();
	}
	
	/**
	 * 时间戳Ms+4位随机串
	 */
	public static String getTimeRandStr4(String id){
		StringBuffer buf = new StringBuffer();
		Random random = new Random();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSSS");
		String time = sdf.format(new Date());
		buf.append(id);
		buf.append(time);
		for(int i = 0; i < 4; i++){
            buf.append(random.nextInt(10));//取四个随机数追加到StringBuffer
	    }
	    return buf.toString();
	}
	
	
	
	/**
	 * 时间戳Ms+5位随机串
	 * @param id
	 * @return
	 */
	public static String getTimeRandStr5(String id){
		StringBuffer buf = new StringBuffer();
		Random random = new Random();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSSS");
		String time = sdf.format(new Date());
		buf.append(id);
		buf.append(time);
		for(int i = 0; i < 4; i++){
            buf.append(random.nextInt(10));//取四个随机数追加到StringBuffer
	    }
		for(int i = 0; i < 4; i++){
            buf.append(random.nextInt(10));//取四个随机数追加到StringBuffer
	    }
	    return buf.toString();
	}
	/**
	 * 时间戳Ms+4位随机串
	 */
	public static String getTimeRandStr(String id){
		StringBuffer buf = new StringBuffer();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		String time = sdf.format(new Date());
		buf.append(id);
		buf.append(time);
	    return buf.toString();
	}
	/**
	 * 4位随机串
	 */
	public static String getRandStr4(){
		StringBuffer buf = new StringBuffer();
		Random random = new Random();
		for(int i = 0; i < 4; i++){
            buf.append(random.nextInt(10));//取四个随机数追加到StringBuffer
	    }
	    return buf.toString();
	}
	/**
	 * 获取当前时间戳MS
	 */
	public static String getCurrTimeMsStr(String id){
		StringBuffer buf = new StringBuffer();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSSS");
		String time = sdf.format(new Date());
		buf.append(id);
		buf.append(time);
	    return buf.toString();
	}
	
	public static void main(String[] args) {
		String [] arry=new String[1000];
		for (int i = 0; i < 1000; i++) {
			arry[i]=getTimeRandStr5("");
		}
		boolean flag=false;
		 for (int i = 0; i < arry.length; i++) {
		  String temp=arry[i];
		  int count=0;
		  for (int j = 0; j < arry.length; j++) {
			  String temp2=arry[j];
		   //有重复值就count+1 
		   if(temp.equals(temp2)){
		    count++;
		   }
		  }
		  //由于中间又一次会跟自己本身比较所有这里要判断count>=2
		  if(count>=2){
		   flag=true;
		  }
		 }
		 if(flag){
		  System.out.println("有重复值存在！！！");
		 }else{
		  System.out.println("没有重复值存在！！！");
		 }

	}
}
