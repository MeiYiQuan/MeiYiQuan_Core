package com.salon.backstage.qcproject.timer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.salon.backstage.qcproject.service.TimerService;

/**
 * 作者：齐潮
 * 创建日期：2017年3月1日
 * 类说明：每天执行的定时任务
 */
public class DayTimer {

	private final static Logger logger = LoggerFactory.getLogger(DayTimer.class);
	
	@Autowired
	private TimerService ts;
	
	/**
	 * 每天00:00:00执行
	 */
	public void dayDoing1(){
		int upCoupons = ts.updateUserCoupons();
		logger.debug("定时任务(每天00:00:00执行)：更新用户优惠券状态结果  " + upCoupons);
		int upPoints = ts.updateUserPoints();
		logger.debug("定时任务(每天00:00:00执行)：更新用户积分状态结果  " + upPoints);
	}
	
	/**
	 * 每天09:00:00执行
	 */
	public void dayDoing2(){
		int result = ts.sendCouponWarningToUsers();
		logger.debug("定时任务(每天09:00:00执行)：优惠券到期提醒结果  " + result);
	}
	
	/**
	 * 每天10:00:00执行
	 */
	public void dayDoing3(){
		int result = ts.sendPointWarningToUsers();
		logger.debug("定时任务(每天10:00:00执行)：积分到期提醒结果  " + result);
	}
	
}
