package com.salon.backstage.common.util;

import java.text.SimpleDateFormat;
import java.util.Date;

public class OrderNumUtil {
	
	public static String getOrderNum(){
		////19710101 000000 + 00000 /年月日时分秒+五位随机数  共19位
		String orderNum = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date())+(int)(1+Math.random()*9)+(int)(1+Math.random()*9)+(int)(1+Math.random()*9)+(int)(1+Math.random()*9)+(int)(1+Math.random()*9);
		return orderNum;
		
	}
}
