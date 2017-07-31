package com.salon.backstage.qcproject.service;

import com.salon.backstage.qcproject.util.Enums.SystemCoupons;

/**
 * 作者：齐潮
 * 创建日期：2017年2月15日
 * 类说明：用于给用户发放优惠券
 */
public interface CouponUserServiceNEW {

	/**
	 * 用于自动发放优惠券
	 * @param sysCoupon
	 * @param now
	 * @param userId
	 * @param pushTitle
	 * @param pushContent
	 */
	public void sendForUser(SystemCoupons sysCoupon,long now,String userId,String pushTitle,String pushContent);
	
}
