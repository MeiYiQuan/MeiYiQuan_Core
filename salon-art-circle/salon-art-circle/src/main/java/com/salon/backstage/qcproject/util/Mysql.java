package com.salon.backstage.qcproject.util;
/**
 * 作者：齐潮
 * 创建日期：2017年1月13日
 * 类说明：存储有关数据库结构的静态量。只用于开发，项目上线后不再修改
 */
public class Mysql {

	/**
	 * 视频和照片地址的长度
	 */
	public final static int PHOTO_VIDEO_URL_LENGTH = 300;
	
	/**
	 * id的长度
	 */
	public final static int ID_LENGTH = 32;
	
	/**
	 * 表示时间的长度
	 */
	public final static int TIMESTAMP_LENGTH = 13;
	
	/**
	 * 数据类型为decimal的参数，关系到价格的地方要用
	 */
	public final static String PRICE_LENGTHS = "9,2";
	
}
