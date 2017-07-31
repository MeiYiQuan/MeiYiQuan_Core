package com.salon.backstage.homepage.coupon.service;

import java.util.Map;

public interface ICouponService {
	/**
	 * 注册送优惠券接口，作废
	 */
	void couponRegister(String userId);
	/**
	 * 求教程送优惠券，作废
	 * @param json
	 */
	void couponCourse(Map<String, Object> json);
	
}
