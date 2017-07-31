package com.salon.backstage.qcproject.service.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.Map.Entry;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.salon.backstage.homepage.push.service.PushService;
import com.salon.backstage.pub.bsc.dao.po.Coupon;
import com.salon.backstage.pub.bsc.dao.po.CouponUser;
import com.salon.backstage.pub.bsc.dao.po.Sys;
import com.salon.backstage.pub.bsc.domain.Constant;
import com.salon.backstage.qcproject.dao.ObjectDao;
import com.salon.backstage.qcproject.service.CouponUserServiceNEW;
import com.salon.backstage.qcproject.util.Statics;
import com.salon.backstage.qcproject.util.Enums.SystemCoupons;

/**
 * 作者：齐潮
 * 创建日期：2017年2月15日
 * 类说明：
 */
@Service
public class CouponUserServiceImplNEW implements CouponUserServiceNEW {

	@Autowired
	private ObjectDao od;
	
	@Autowired
	private PushService ps;
	
	@SuppressWarnings("unchecked")
	@Override
	public void sendForUser(SystemCoupons sysCoupon,long now,String userId,String pushTitle,String pushContent) {
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("sys_key", sysCoupon.getSystemKey());
		params.put("status", Constant.YES_INT);
		params.put("type", Constant.SYSTEM_TYPE_COUPONS);
		Sys sys = od.getObjByParams(Sys.class, params);
		if(sys!=null){
			Map<String,Integer> map = JSONObject.parseObject(sys.getSys_value(), Map.class);
			if(map!=null&&map.size()>0){
				Set<Entry<String, Integer>> set = map.entrySet();
				List<Map<String, Object>> userCoupons = new ArrayList<Map<String, Object>>();
				
				// 将要赠送的优惠券给加入到list
				for(Entry<String, Integer> en:set){
					if(en.getValue()<1)
						continue;
					Coupon coupon = od.getObjById(Coupon.class, en.getKey());
					if(coupon==null||coupon.getStatus()!=Constant.YES_INT)
						continue;
					
					long expireTime = now + (coupon.getExpire_time() * 24 * 60 * 60 * 1000);
					Calendar cal = Calendar.getInstance();
					cal.setTimeInMillis(expireTime);
					cal.add(Calendar.DATE, -7);
					long warnTime = cal.getTimeInMillis();
					
					for(int i=0;i<en.getValue();i++){
						Map<String, Object> uc = new HashMap<String, Object>();
						String uuid = UUID.randomUUID().toString().replace("-", "").toUpperCase();
						uc.put("user_id", userId);
						uc.put("get_time", now);
						uc.put("expire_time", expireTime);
						uc.put("warn_time", warnTime);
						uc.put("coupon_id", coupon.getId());
						uc.put("status", Constant.YES_INT);
						uc.put("coupon_type", coupon.getCoupon_type());
						uc.put("denomination", coupon.getDenomination());
						int isEver = coupon.getExpire_type()==Statics.COUPON_EXPIRE_TYPE_FOREVER?Constant.YES_INT:Constant.NO_INT;
						uc.put("isForever", isEver);
						uc.put("number", uuid);
						uc.put("coupon_provide_type", Statics.COUPON_PROVIDE_TYPE_SYSTEM);
						uc.put("get_type", sysCoupon.getType());
						userCoupons.add(uc);
					}
					
				}
				
				// 赠送给用户优惠券并推送信息
				if(userCoupons.size()>0){
					int saveResult = od.saveObjects(CouponUser.class, userCoupons);
					if(saveResult>0)
						ps.pushEveryOne("", pushTitle, Statics.PUSH_TYPE_FORTHWITH_NOTABLE, pushContent, null, userId);
				}
			}
		}
	}

	
}
