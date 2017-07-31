package com.salon.backstage.homepage.pay.service;

import com.salon.backstage.pub.bsc.dao.po.Order;
import com.salon.backstage.pub.bsc.dao.vo.MobileMessage;
import com.salon.backstage.qcproject.util.Code;

/**
 * 作者：齐潮
 * 创建日期：2016年12月21日
 * 类说明：处理有关支付的业务逻辑
 */
public interface PayService {

	/**
	 * webHook回调
	 * @param orderNum
	 * @param money
	 * @return
	 * @throws Exception 
	 */
	public MobileMessage webHook(String orderNum,double money) throws Exception;
	
	/**
	 * ios内购支付回调
	 * @param order
	 * @return
	 * @throws Exception 
	 */
	public MobileMessage iosHook(Order order) throws Exception;
	
	/**
	 * 获取ios内购可以选择的所有点券信息
	 * @return
	 */
	public Code getIosTypeList();
	
	/**
	 * ios充值成功后的回调
	 * @param userId
	 * @param coinId
	 * @return
	 */
	public Code iosWebHook(String userId,String coinId);

}
