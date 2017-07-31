package com.salon.backstage.qcproject.service.impl;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.salon.backstage.pub.bsc.dao.po.Activity;
import com.salon.backstage.pub.bsc.dao.po.Course;
import com.salon.backstage.pub.bsc.dao.po.Shared;
import com.salon.backstage.pub.bsc.dao.po.UserVideoRequest;
import com.salon.backstage.pub.bsc.dao.po.Video;
import com.salon.backstage.qcproject.dao.ObjectDao;
import com.salon.backstage.qcproject.service.CouponUserServiceNEW;
import com.salon.backstage.qcproject.service.ShareServiceNEW;
import com.salon.backstage.qcproject.util.Code;
import com.salon.backstage.qcproject.util.Enums.ShareDistrict;
import com.salon.backstage.qcproject.util.Enums.ShareType;
import com.salon.backstage.qcproject.util.Enums.SystemCoupons;

/**
 * 作者：齐潮
 * 创建日期：2017年2月14日
 * 类说明：
 */
@Service
public class ShareServiceImplNEW implements ShareServiceNEW {

	@Autowired
	private ObjectDao od;
	
	@Autowired
	private CouponUserServiceNEW cus;
	
	@Override
	public Code sharedHook(String userId, String sharedId, ShareType type,ShareDistrict district) {
		String url = null;
		SystemCoupons sysCoupon = null;
		switch(type){
			case SHARE_TYPE_VIDEO:
				Video video = od.getObjById(Video.class, sharedId);
				if(video==null)
					return Code.init(false, 5, "该视频已经不存在！");
				url = video.getShare_url();
				sysCoupon = SystemCoupons.SHARE_VIDEO;
				break;
			case SHARE_TYPE_ACTIVITY:
				Activity activity = od.getObjById(Activity.class, sharedId);
				if(activity==null)
					return Code.init(false, 5, "该活动已经不存在！");
				url = activity.getShare_url();
				sysCoupon = SystemCoupons.SHARE_ACTIVITY;
				break;
			case SHARE_TYPE_COURSE:
				Course course = od.getObjById(Course.class, sharedId);
				if(course==null)
					return Code.init(false, 5, "该课程已经不存在！");
				url = course.getShare_url();
				sysCoupon = SystemCoupons.SHARE_COURSE;
				break;
			case SHARE_TYPE_REQUEST:
				UserVideoRequest req = od.getObjById(UserVideoRequest.class, sharedId);
				if(req==null)
					return Code.init(false, 5, "该求课程已经不存在！");
				url = req.getShare_url();
				sysCoupon = SystemCoupons.SHARE_REQUEST;
				break;
			default:
				return Code.init(false, 2, "参数不合法！");
		}
		
		// 检查是否已经分享过这个链接
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("userId", userId);
		params.put("shareId", sharedId);
		Shared shared = od.getObjByParams(Shared.class, params);
		
		// 记录分项记录
		long now = System.currentTimeMillis();
		Shared s = new Shared();
		s.setDistrict(district.getDistrict());
		s.setShare_time(now);
		s.setShareId(sharedId);
		s.setType(type.getType());
		s.setUrl(url);
		s.setUserId(userId);
		s.setShare_time(now);
		String sId = od.save(s);
		if(sId==null)
			return Code.init(false, 6, "数据库错误！");
		
		// 当符合赠送优惠券的条件并且没有分享过此信息时，赠送用户优惠券并推送信息给用户
		/*if(sysCoupon!=null&&shared==null){
			cus.sendForUser(sysCoupon, now, userId, "分享链接获得优惠券", "恭喜您成功分享了链接，获得了优惠券！");
			
			// 获得该条件下可以活得的优惠券信息，id以及张数数
			params = new HashMap<String,Object>();
			params.put("sys_key", sysCoupon.getSystemKey());
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
						
						long expireTime = now + coupon.getExpire_time();
						Calendar cal = Calendar.getInstance();
						cal.setTimeInMillis(expireTime);
						cal.add(Calendar.DATE, -7);
						long warnTime = cal.getTimeInMillis();
						
						for(int i=0;i<en.getValue();i++){
							Map<String, Object> uc = new HashMap<String, Object>();
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
							uc.put("number", coupon.getNumber());
							uc.put("coupon_provide_type", Statics.COUPON_PROVIDE_TYPE_SYSTEM);
							uc.put("get_type", sysCoupon.getType());
							userCoupons.add(uc);
						}
						
					}
					
					// 赠送给用户优惠券并推送信息
					if(userCoupons.size()>0){
						int saveResult = od.saveObjects(CouponUser.class, userCoupons);
						if(saveResult>0)
							ps.pushEveryOne("", "分享链接获得优惠券", Statics.PUSH_TYPE_FORTHWITH_NOTABLE, "恭喜您成功分享了链接，获得了优惠券！", null, userId);
					}
				}
			}
			
		}*/
		
		return Code.init(true, 0, "分享成功！");
	}

}
