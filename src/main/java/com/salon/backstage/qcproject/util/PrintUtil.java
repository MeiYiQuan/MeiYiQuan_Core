package com.salon.backstage.qcproject.util;

import org.slf4j.Logger;

/**
 * 作者：齐潮
 * 创建日期：2016年12月3日
 * 类说明：配合logback来使用，进行一些自定义格式的打印
 */
public class PrintUtil {

	/**
	 * 将错误信息已特定的格式打印到日志
	 * @param logger
	 * @param e
	 */
	public static void printException(Logger logger,Exception e){
		StackTraceElement[] st = e.getStackTrace();
		StringBuffer str = new StringBuffer("出现了异常：" + e.toString() + "\n");
		if(st!=null&&st.length>0){
			for(int i = 0;i<st.length;i++){
				str.append("\t\t" + st[i].toString() + "\n");
			}
		}
		logger.error(str.toString());
	}
	
	/**
	 * 可以自定义名称
	 * @param name
	 * @param logger
	 * @param e
	 */
	public static void printException(String name,Logger logger,Exception e){
		StackTraceElement[] st = e.getStackTrace();
		StringBuffer str = new StringBuffer(name + "：" + e.toString() + "\n");
		if(st!=null&&st.length>0){
			for(int i = 0;i<st.length;i++){
				str.append("\t\t" + st[i].toString() + "\n");
			}
		}
		logger.error(str.toString());
	}
	
	
	// ---------------------------Throwable型异常-------------------------
	/**
	 * 将错误信息已特定的格式打印到日志
	 * @param logger
	 * @param e
	 */
	public static void printThrowable(Logger logger,Throwable e){
		StackTraceElement[] st = e.getStackTrace();
		StringBuffer str = new StringBuffer("出现了异常：" + e.toString() + "\n");
		if(st!=null&&st.length>0){
			for(int i = 0;i<st.length;i++){
				str.append("\t\t" + st[i].toString() + "\n");
			}
		}
		logger.error(str.toString());
	}
	
	/**
	 * 可以自定义名称
	 * @param name
	 * @param logger
	 * @param e
	 */
	public static void printThrowable(String name,Logger logger,Throwable e){
		StackTraceElement[] st = e.getStackTrace();
		StringBuffer str = new StringBuffer(name + "：" + e.toString() + "\n");
		if(st!=null&&st.length>0){
			for(int i = 0;i<st.length;i++){
				str.append("\t\t" + st[i].toString() + "\n");
			}
		}
		logger.error(str.toString());
	}
}
