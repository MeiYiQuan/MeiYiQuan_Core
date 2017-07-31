package com.salon.backstage.common.cache;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.Transaction;

public class CacheUtil {
	   public static void main(String[] args) throws InterruptedException {
		   //连接本地的 Redis 服务
		      Jedis jedis = new Jedis("localhost");
		      
		      
		      System.out.println("1111");
		      System.out.println("1111");
		      System.out.println("1111");
		     // System.out.println("Connection to server sucessfully");
		      jedis.set("timekey", "abc");
		      jedis.setex("timekey", 5, "mins");// 通过此方法，可以指定key的存活（有效时间） 时间为秒  
		        Thread.sleep(1000);// 睡眠5秒后，剩余时间将为<=5  
		        System.out.println(jedis.ttl("timekey")); // 输出结果为5 
		        System.out.println(jedis.get("timekey")); // 输出结果为5 
		        jedis.setex("timekey", 1, "min"); // 设为1后，下面再看剩余时间就是1了  
		        System.out.println(jedis.ttl("timekey")); // 输出结果为1  
		        System.out.println(jedis.exists("timekey"));// 检查key是否存在 
		        System.out.println(jedis.get("timekey")); // 输出结果为5 
		     // redis事务
/*		 Transaction tran=    	jedis.multi();
		 tran.set("abc", "abc");
		 tran.set("def", "def");
		 tran.exec();
		 System.out.println(  jedis.get("abc")+ jedis.get("def"));*/
	   }
	   
	   
	
	   
	   
	   
}
