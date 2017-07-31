package com.salon.backstage.homepage.order.service.impl;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.qc.util.MathUtil;
import com.salon.backstage.common.util.ExtraSpringHibernateTemplate;
import com.salon.backstage.common.util.MobileMessageCondition;
import com.salon.backstage.common.util.PrimaryKeyUtil;
import com.salon.backstage.homepage.channel.service.ObjectService;
import com.salon.backstage.homepage.order.service.OrderService;
import com.salon.backstage.pub.bsc.dao.po.Activity;
import com.salon.backstage.pub.bsc.dao.po.ActivityStatus;
import com.salon.backstage.pub.bsc.dao.po.ActivityUser;
import com.salon.backstage.pub.bsc.dao.po.CouponUser;
import com.salon.backstage.pub.bsc.dao.po.Order;
import com.salon.backstage.pub.bsc.dao.po.Sys;
import com.salon.backstage.pub.bsc.dao.po.Video;
import com.salon.backstage.pub.bsc.dao.vo.MobileMessage;
import com.salon.backstage.pub.bsc.domain.Constant;
import com.salon.backstage.pub.bsc.domain.Constant.PayStyle;
import com.salon.backstage.qcproject.dao.ObjectDao;
import com.salon.backstage.qcproject.service.ActivityServiceNEW;
import com.salon.backstage.qcproject.util.Sql;
import com.salon.backstage.qcproject.util.Statics;

/**
 * 作者：齐潮
 * 创建日期：2016年12月23日
 * 类说明：
 */
@Service
public class OrderServiceImpl implements OrderService {

	@Autowired
	private ObjectService os;
	
	@Autowired
	private ActivityServiceNEW as;
	
	@Autowired
	private ObjectDao od;
	
	@Autowired
	private ExtraSpringHibernateTemplate template;
	
	@Override
	public MobileMessage createOrder(String userId,int isUseCoupon,String couponId,String shopId,int type,PayStyle payStyle) {
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("user_id", userId);
		params.put("video_id", shopId);
		params.put("type", type);
		params.put("status", Constant.PAY_STATUS_PAYED);
		Order order = od.getObjByParams(Order.class, params);
		if(order!=null)
			return MobileMessageCondition.addCondition(false, 66, "您已经购买过该产品！", "");
		MobileMessage shopResult = null;
		if(type==Statics.ORDER_TYPE_VIDEO){
			shopResult = videoSupport(shopId,isUseCoupon);
			if(!shopResult.isResult())
				return shopResult;
		}else if(type==Statics.ORDER_TYPE_ACTIVITY){
			shopResult = activitySupport(userId,shopId,isUseCoupon);
			if(!shopResult.isResult())
				return shopResult;
		}else{
			return MobileMessageCondition.addCondition(false, 150, "未知错误！", "");
		}
		double shopPrice = Double.parseDouble(shopResult.getResponse().toString());
		
		double price = 0;
		long now = System.currentTimeMillis();
		String nowMax = getMaxOrderNum();
		String orderNum = PrimaryKeyUtil.getOrderNum(nowMax);
		
		// 判断是否使用优惠券
		if(isUseCoupon==Constant.YES_INT){
			// 使用优惠券，优惠券id不能为空
			if(couponId==null||couponId.trim().equals(""))
				return MobileMessageCondition.addCondition(false, 99, "参数缺失！", "");
			
			CouponUser cu = od.getObjById(CouponUser.class, couponId);
			
			if(cu==null)
				return MobileMessageCondition.addCondition(false, 95, "该优惠券不存在！", "");
			if(cu.getStatus()!=Constant.YES_INT)
				return MobileMessageCondition.addCondition(false, 94, "该优惠券已经失效！", "");
			if(cu.getIsForever()!=Constant.YES_INT&&cu.getExpire_time()<now){
				cu.setStatus(Constant.NO_INT);
				od.update(cu);
				return MobileMessageCondition.addCondition(false, 93, "该优惠券已经过期！", "");
			}
			
			switch(cu.getCoupon_type()){
				case Statics.COUPON_TYPE_DISCOUNT:
					if(cu.getDenomination()==null||cu.getDenomination()<=0||cu.getDenomination()>1)
						return MobileMessageCondition.addCondition(false, 92, "系统错误！请稍后再试！", "");
					price = MathUtil.toAny(2, shopPrice * cu.getDenomination());
					break;
				case Statics.COUPON_TYPE_VOUCHER:
					if(cu.getDenomination()==null||cu.getDenomination()<=0)
						return MobileMessageCondition.addCondition(false, 92, "系统错误！请稍后再试！", "");
					if(cu.getDenomination()>=shopPrice){
						price = 0;
//						return MobileMessageCondition.addCondition(false, 91, "优惠券的抵用价格必须小于商品价格！", "");
					}else{
						price = MathUtil.toAny(2, shopPrice - cu.getDenomination());
					}
					break;
				default:
					return MobileMessageCondition.addCondition(false, 92, "系统错误！请稍后再试！", "");
			}
			
			cu.setUse_time(now);
			cu.setUse_type_id(orderNum);
			cu.setStatus(Constant.NO_INT);
			od.update(cu);
			
		}else{
			// 不使用优惠券，价格即为原价
			price = shopPrice;
			
		}
		
		order = new Order();
		order.setCreate_time(now);
		order.setIsUseCoupon(isUseCoupon);
		order.setOrder_num(orderNum);
		order.setPay_type(payStyle.getSendType());
		order.setPrice(price);
		order.setOldPrice(shopPrice);
		order.setStatus(Constant.PAY_STATUS_NOPAY);
		order.setUser_id(userId);
		order.setVideo_id(shopId);
		order.setType(type);
		
		// 判断支付方式，并给order添加平台收益和相应的税率
		params = new HashMap<String,Object>();
		params.put("type", Constant.SYSTEM_TYPE_NUMBER);
		params.put("status", Constant.YES_INT);
		if(payStyle==PayStyle.IOS){
			// 苹果内购
			params.put("sys_key", Constant.SYSTEM_KEY_IOS_BUY_TAX);
			Sys sys = od.getObjByParams(Sys.class, params);
			double tax = Double.parseDouble(sys.getSys_value());
			params.put("sys_key", Constant.SYSTEM_KEY_IOS_BUY);
			sys = od.getObjByParams(Sys.class, params);
			double iosDeduct = Double.parseDouble(sys.getSys_value());
			double achieve = MathUtil.toAny(2, price * (1 - tax) * (1 - iosDeduct));
			order.setAchieve(achieve);
			order.setTax(tax);
			order.setIos_deduct(iosDeduct);
		}else{
			// 其他支付方式，使用RMB支付
			params.put("sys_key", Constant.SYSTEM_KEY_BUY_TAX);
			Sys buyTax = od.getObjByParams(Sys.class, params);
			double tax = Double.parseDouble(buyTax.getSys_value());
			double taxedPrice = MathUtil.toAny(2, price * (1 - tax));
			order.setTax(tax);
			order.setAchieve(taxedPrice);
		}
		
		String id = os.save(order);
		
		if(id==null)
			return MobileMessageCondition.addCondition(false, 96, "订单生成失败！请稍后再试", null);
		order.setId(id);
		
		if(type==Statics.ORDER_TYPE_ACTIVITY){
			String sql = "update `tb_activity` set `part_num` = `part_num` + 1 where `id` = :id";
			params = new HashMap<String,Object>();
			params.put("id", shopId);
			int addResult = od.updateBySql(Sql.get(sql, params));
			if(addResult!=1)
				return MobileMessageCondition.addCondition(false, 90, "系统繁忙！请稍后再试！", null);
		}
		
		return MobileMessageCondition.addCondition(true, 0, "订单生成成功！", order);
	}
	
	/**
	 * 当购买的是视频时，需要通过这个方法来获得视频价格
	 * @param videoId
	 * @return
	 */
	private MobileMessage videoSupport(String videoId,int isUseCoupon){
		Video video = os.get(Video.class, new String[]{"id"}, new Object[]{videoId});
		if(video==null)
			return MobileMessageCondition.addCondition(false, 97, "该视频不存在！", "");
		if(video.getFree()==Constant.YES_INT)
			return MobileMessageCondition.addCondition(false, 85, "不能购买免费视频！", "");
		if(video.getPer_cost()<=0)
			return MobileMessageCondition.addCondition(false, 92, "系统错误！请稍后再试！", "");
		if(video.getCanUseCoupon()==Constant.NO_INT&&isUseCoupon==Constant.YES_INT)
			return MobileMessageCondition.addCondition(false, 90, "该视频不能使用优惠券！", "");
		double price = video.getPer_cost();
		return MobileMessageCondition.addCondition(true, 0, "", price);
	}
	
	/**
	 * 当购买的是活动时，需要通过这个方法来获得活动价格
	 * @param activityId
	 * @return
	 */
	private MobileMessage activitySupport(String userId,String activityId,int isUseCoupon){
		as.cleanActivities(activityId);
		Activity activity = os.get(Activity.class, new String[]{"id"}, new Object[]{activityId});
		if(activity==null)
			return MobileMessageCondition.addCondition(false, 97, "该活动不存在！", "");
		if(activity.getPrice()<=0)
			return MobileMessageCondition.addCondition(false, 92, "系统错误！请稍后再试！", "");
		if(activity.getCanUseCoupon()==Constant.NO_INT&&isUseCoupon==Constant.YES_INT)
			return MobileMessageCondition.addCondition(false, 90, "该活动不能使用优惠券！", "");
		if(activity.getPart_num()>=activity.getMost_man())
			return MobileMessageCondition.addCondition(false, 91, "该活动已经满员！", "");
		// 查看活动状态
		ActivityStatus actiStatus = os.get(ActivityStatus.class, new String[]{"activity_id"}, new Object[]{activityId});
		if(actiStatus==null)
			return MobileMessageCondition.addCondition(false, 97, "该活动不存在！", "");
		if(actiStatus.getIsCancel()==Constant.YES_INT)
			return MobileMessageCondition.addCondition(false, 84, "该活动已经取消！", "");
		long now = System.currentTimeMillis();
		if(now<actiStatus.getEnroll_begin_time())
			return MobileMessageCondition.addCondition(false, 85, "报名尚未开始！", "");
		if(now>actiStatus.getEnroll_end_time())
			return MobileMessageCondition.addCondition(false, 86, "报名已经结束！", "");
		// 查看该用户是否已经报过名
		Map<String,Object> actiUserParams = new HashMap<String,Object>();
		actiUserParams.put("user_id", userId);
		actiUserParams.put("activity_id", activityId);
		ActivityUser au = od.getObjByParams(ActivityUser.class, actiUserParams);
		if(au!=null)
			return MobileMessageCondition.addCondition(false, 87, "您在这个活动中已经报过名！", "");
		double price = activity.getPrice();
		return MobileMessageCondition.addCondition(true, 0, "", price);
	}
	

	@SuppressWarnings("unchecked")
	@Override
	public String getMaxOrderNum() {
		String sql = "select max(`order_num`) as `max` from `tb_order`";
		Map<String,Object> map = template.createSQLQueryFindFirstOne(sql);
		if(map==null||map.size()<1)
			return null;
		String result = map.get("max")==null?"10000000000000416922":map.get("max").toString();
		return result;
	}

}
