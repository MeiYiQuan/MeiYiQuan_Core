package com.salon.backstage.homepage.coupon.service.impl;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.salon.backstage.common.util.ExtraSpringHibernateTemplate;
import com.salon.backstage.common.util.afterCreate.DateAddUtil;
import com.salon.backstage.homepage.coupon.service.ICouponService;
import com.salon.backstage.pub.bsc.dao.po.CouponUser;
import com.salon.backstage.pub.bsc.domain.Constant;

@Service
public class CouponServiceImpl implements ICouponService{
	
	@Autowired
	private ExtraSpringHibernateTemplate extraSpringHibernateTemplate;
	
	@Override
	public void couponRegister(String userId) {
		//添加5元优惠券
		for(int secondStart = 1;secondStart <= Constant.REGISTER_COUPON_SECOND_AMOUNT;secondStart++){
			CouponUser couponUser = new CouponUser();
			couponUser.setCoupon_id(Constant.COUPON_CASH_SECOND_ID);
			couponUser.setGet_time(Long.valueOf(new SimpleDateFormat("yyyyMMddHHmmss").format(new Date())));
			couponUser.setWarn_time(Long.valueOf(new SimpleDateFormat("yyyyMMddHHmmss").format(DateAddUtil.descDay(DateAddUtil.addMonth(new Date(), 3), 7))));
			couponUser.setExpire_time(Long.valueOf(new SimpleDateFormat("yyyyMMddHHmmss").format(DateAddUtil.addMonth(new Date(), 3))));
			couponUser.setUser_id((String)userId);
//			couponUser.setNumber(getMaxNumber()+1);
			extraSpringHibernateTemplate.getHibernateTemplate().save(couponUser);
		}
		//添加10元优惠券
		for(int thirdStart = 1;thirdStart <= Constant.REGISTER_COUPON_THIRD_AMOUNT;thirdStart++){
			CouponUser couponUser = new CouponUser();
			couponUser.setCoupon_id(Constant.COUPON_CASH_THIRD_ID);
			couponUser.setGet_time(Long.valueOf(new SimpleDateFormat("yyyyMMddHHmmss").format(new Date())));
			couponUser.setWarn_time(Long.valueOf(new SimpleDateFormat("yyyyMMddHHmmss").format(DateAddUtil.descDay(DateAddUtil.addMonth(new Date(), 3), 7))));
			couponUser.setExpire_time(Long.valueOf(new SimpleDateFormat("yyyyMMddHHmmss").format(DateAddUtil.addMonth(new Date(), 3))));
			couponUser.setUser_id((String)userId);
//			couponUser.setNumber(getMaxNumber()+1);
			extraSpringHibernateTemplate.getHibernateTemplate().save(couponUser);
		}
		//添加20元优惠券
		for(int fourthStart = 1;fourthStart <= Constant.REGISTER_COUPON_FOURTH_AMOUNT;fourthStart++){
			CouponUser couponUser = new CouponUser();
			couponUser.setCoupon_id(Constant.COUPON_CASH_FOURTH_ID);
			couponUser.setGet_time(Long.valueOf(new SimpleDateFormat("yyyyMMddHHmmss").format(new Date())));
			couponUser.setWarn_time(Long.valueOf(new SimpleDateFormat("yyyyMMddHHmmss").format(DateAddUtil.descDay(DateAddUtil.addMonth(new Date(), 3), 7))));
			couponUser.setExpire_time(Long.valueOf(new SimpleDateFormat("yyyyMMddHHmmss").format(DateAddUtil.addMonth(new Date(), 3))));
			couponUser.setUser_id((String)userId);
//			couponUser.setNumber(getMaxNumber()+1);
			extraSpringHibernateTemplate.getHibernateTemplate().save(couponUser);
		}
	}
	
	/**
	 * 获得最大优惠券号
	 */
	private Long getMaxNumber(){
		String sql = "select ifnull(max(number),0) as maxNumber from tb_coupon_user";
		List<Map> listMap = extraSpringHibernateTemplate.createSQLQueryFindAll(sql);
		Long maxNumber = Long.valueOf(listMap.get(0).get("maxNumber").toString());
		return maxNumber;
	}
	
	/**
	 * 通过求课程获取50元优惠券
	 */
	@Override
	public void couponCourse(Map<String, Object> json) {
		String userId = json.get("userid").toString();
		CouponUser couponUser = new CouponUser();
		couponUser.setCoupon_id(Constant.COUPON_CASH_SIXTH_ID);
		couponUser.setGet_time(Long.valueOf(new SimpleDateFormat("yyyyMMddHHmmss").format(new Date())));
		couponUser.setWarn_time(Long.valueOf(new SimpleDateFormat("yyyyMMddHHmmss").format(DateAddUtil.descDay(DateAddUtil.addMonth(new Date(), 3), 7))));
		couponUser.setExpire_time(Long.valueOf(new SimpleDateFormat("yyyyMMddHHmmss").format(DateAddUtil.addMonth(new Date(), 3))));
		couponUser.setUser_id((String)userId);
//		couponUser.setNumber(getMaxNumber()+1);
		couponUser.setGet_type(2);
		extraSpringHibernateTemplate.getHibernateTemplate().save(couponUser);
	}
}
