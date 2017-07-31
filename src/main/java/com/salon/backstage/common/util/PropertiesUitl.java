package com.salon.backstage.common.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.Properties;

import org.springframework.jdbc.support.JdbcUtils;

public class PropertiesUitl {
	public static 	InputStream inputStream ;
	public static Properties p ;
	static{
		inputStream = JdbcUtils.class.getClassLoader().getResourceAsStream("config.properties");
		p = new Properties();
		 try {
				p.load(inputStream);
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
	} 
	public static String getProperty(String key){
	
		try {
			return 	new String(p.getProperty(key).trim().getBytes("ISO-8859-1"),"utf-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return key;
	}
	public static String getProperty2(String key){
			return p.getProperty(key);
	}

}
