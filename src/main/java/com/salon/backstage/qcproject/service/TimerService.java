package com.salon.backstage.qcproject.service;
/**
 * 作者：齐潮
 * 创建日期：2017年2月28日
 * 类说明：用于定时任务
 */
public interface TimerService {

	/**
	 * 更新用户年龄
	 */
	public int updateUserAge();
	
	/**
	 * 更新所有用户的优惠券信息
	 * @return
	 */
	public int updateUserCoupons();
	
	/**
	 * 更新所有用户的积分信息
	 * @return
	 */
	public int updateUserPoints();
	
	/**
	 * 向用户发送优惠券到期提醒
	 * @return
	 */
	public int sendCouponWarningToUsers();
	
	/**
	 * 向用户发送优惠券到期提醒
	 * @return
	 */
	public int sendPointWarningToUsers();
	
}
