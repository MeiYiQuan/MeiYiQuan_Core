package com.salon.backstage.homepage.push.service;

import java.util.Map;

/**
 * 作者：齐潮
 * 创建日期：2016年12月21日
 * 类说明：处理极光推送
 */
public interface PushService {

	/**
	 * 将推送信息推送给指定的任何用户
	 * @param pushId
	 * @param type
	 * @param userIds
	 */
	public void pushEveryOne(String pushId,int type,String... userIds);
	
	/**
	 * 将自定义推送信息推送给制定用户
	 * @param message
	 * @param map
	 * @param userIds
	 */
	public void pushEveryOne(String pushId,String title,int type,String message,Map<String,String> params,String... userIds);
	
	/**
	 * 推送信息和参数给全部ios用户
	 * @param pushId
	 * @param type
	 */
	public void pushAllIos(String pushId,int type);
	
	/**
	 * 推送信息和参数给全部android用户
	 * @param pushId
	 * @param type
	 */
	public void pushAllAndroid(String pushId,int type);
	
	/**
	 * 推送信息和参数给全部用户
	 * @param pushId
	 * @param type
	 */
	public void pushAll(String pushId,int type);
	
}
