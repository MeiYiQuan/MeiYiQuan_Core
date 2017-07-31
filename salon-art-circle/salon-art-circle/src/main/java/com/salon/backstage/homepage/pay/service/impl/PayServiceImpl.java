package com.salon.backstage.homepage.pay.service.impl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.qc.util.MathUtil;
import com.salon.backstage.common.util.ExtraSpringHibernateTemplate;
import com.salon.backstage.common.util.MobileMessageCondition;
import com.salon.backstage.homepage.channel.service.ObjectService;
import com.salon.backstage.homepage.pay.service.PayService;
import com.salon.backstage.homepage.point.service.IPointService;
import com.salon.backstage.homepage.push.service.PushService;
import com.salon.backstage.homepage.statistics.service.IStatisticsService;
import com.salon.backstage.pub.bsc.dao.po.Activity;
import com.salon.backstage.pub.bsc.dao.po.ActivityUser;
import com.salon.backstage.pub.bsc.dao.po.IosCoin;
import com.salon.backstage.pub.bsc.dao.po.Order;
import com.salon.backstage.pub.bsc.dao.po.Sys;
import com.salon.backstage.pub.bsc.dao.po.Teacher;
import com.salon.backstage.pub.bsc.dao.po.TeacherOrder;
import com.salon.backstage.pub.bsc.dao.po.User;
import com.salon.backstage.pub.bsc.dao.po.UserToBuyVideos;
import com.salon.backstage.pub.bsc.dao.po.UserToIosCoins;
import com.salon.backstage.pub.bsc.dao.vo.MobileMessage;
import com.salon.backstage.pub.bsc.domain.Constant;
import com.salon.backstage.pub.bsc.domain.Constant.PointEachType;
import com.salon.backstage.qcproject.dao.ObjectDao;
import com.salon.backstage.qcproject.dao.support.UserSupport;
import com.salon.backstage.qcproject.service.ActivityServiceNEW;
import com.salon.backstage.qcproject.service.CouponUserServiceNEW;
import com.salon.backstage.qcproject.util.Code;
import com.salon.backstage.qcproject.util.Enums.SystemCoupons;
import com.salon.backstage.qcproject.util.Sql;
import com.salon.backstage.qcproject.util.Statics;

/**
 * 作者：齐潮
 * 创建日期：2016年12月21日
 * 类说明：
 */
@Service
public class PayServiceImpl implements PayService {
	
	private final static Logger logger = LoggerFactory.getLogger(PayServiceImpl.class);
	
	@Autowired
	private ExtraSpringHibernateTemplate template;
	
	@Autowired
	private ObjectService os;
	
	@Autowired
	private ObjectDao od;
	
	@Autowired
	private PushService ps;
	
	@Autowired
	private CouponUserServiceNEW cus;
	
	@Autowired
	private ActivityServiceNEW as;
	
	@Autowired
	private IPointService point;
	
	@Autowired
	private IStatisticsService is;
	
	@Override
	public MobileMessage webHook(String orderNum, double money) throws Exception {
		Order order = os.get(Order.class, new String[]{"order_num"}, new Object[]{orderNum}); 
		if(order==null||order.getStatus()!=Constant.PAY_STATUS_NOPAY)
			return MobileMessageCondition.addCondition(true, 0, null, null);
		if(order.getPrice()!=money)
			return MobileMessageCondition.addCondition(false, 1, null, null);
		long now = System.currentTimeMillis();
		order.setPay_time(now);
		order.setStatus(Constant.PAY_STATUS_PAYED);
		template.getHibernateTemplate().update(order);
		
		// 添加有关讲师收益的信息
		Map<String, Object> merUser = saveTeacherOder(order, now);
		
		
		// 盈余步骤，保险
		order = os.get(Order.class, new String[]{"order_num"}, new Object[]{orderNum});
		if(order==null)
			return MobileMessageCondition.addCondition(false, 1, null, null);
		if(order.getStatus()!=Constant.PAY_STATUS_PAYED)
			return MobileMessageCondition.addCondition(false, 1, null, null);
		
		User user = null;
		if(order.getType()==Statics.ORDER_TYPE_VIDEO){
			UserToBuyVideos buy = new UserToBuyVideos();
			buy.setBuyTime(now);
			buy.setOrderNum(orderNum);
			buy.setUserId(order.getUser_id());
			buy.setVideoId(order.getVideo_id());
			Serializable serId = template.getHibernateTemplate().save(buy);
			if(serId==null)
				return MobileMessageCondition.addCondition(false, 1, null, null);
			
			// 通知卖家有人购买了他的视频
			
			if(merUser!=null){
				ps.pushEveryOne(Statics.PUSH_ID_VIDEO_BUY, Statics.PUSH_TYPE_FORTHWITH_TABLE, merUser.get("id").toString());
			}
		}else if(order.getType()==Statics.ORDER_TYPE_ACTIVITY){
			ActivityUser au = new ActivityUser();
			au.setActivity_id(order.getVideo_id());
			au.setEnter_time(now);
			au.setAppearance(order.getPrice());
			user = od.getObjById(User.class, order.getUser_id());
			if(user==null)
				return MobileMessageCondition.addCondition(true, 0, null, null);
			au.setMan_type(user.getUser_type());
			au.setUser_id(user.getId().toString());
			String auId = od.save(au);
			if(auId==null)
				return MobileMessageCondition.addCondition(false, 1, null, null);
			
			// 通知主办方有人参加了他的活动
			as.cleanActivities(order.getVideo_id());
			Activity activity = od.getObjById(Activity.class, order.getVideo_id());
			if(activity!=null&&!activity.getTeacher_id().equals("")){
				String count = "select count(`id`) from `tb_order` where `status` = " + Constant.PAY_STATUS_PAYED + " and `video_id` = '" + order.getVideo_id() + "'";
				int activityBuyCount = od.getObjCountBySql(count, null);
				ps.pushEveryOne("", "活动购买通知", Statics.PUSH_TYPE_FORTHWITH_NOTABLE, "有人购买了您的\"" + activity.getTitle() + "\"活动，当前该活动的参加人数为：" + activity.getPart_num() + "人，已经支付的人数为：" + (activityBuyCount + 1) + "人", null, activity.getTeacher_id());
			}
			
			// 参与活动赠送优惠券
			cus.sendForUser(SystemCoupons.BUY_ACTIVITY, now, user.getId().toString(), "参加活动获得优惠券", "恭喜您成功的参与的活动获得了优惠券！请注意查看！");
		}
		
		// 增加积分
		user = user==null?od.getObjById(User.class, order.getUser_id()):user;
		if(user!=null){
			MobileMessage addPoint = point.addPoint(user, PointEachType.PAY_POINT,order.getPrice());
			String addStr = "";
			// 通知买家购买成功和获得的积分数量
			if(addPoint.isResult())
				addStr = "您获得了" + addPoint.getResponse().toString() + "个积分！";
			ps.pushEveryOne("", "购买成功通知", Statics.PUSH_TYPE_FORTHWITH_NOTABLE, "恭喜您成功购买我们的产品！" + addStr, null, user.getId().toString());
		}
		
		// 统计量加1
//		is.addStatistics(Statics.statics, type, ids);
		
		
		return MobileMessageCondition.addCondition(true, 0, null, null);
	}

	/**
	 * 保存讲师收益有关的信息
	 * @param order
	 * @param now
	 * @return
	 */
	private Map<String, Object> saveTeacherOder(Order order,long now){
		Sql userSql = UserSupport.getUserByVideoId(order.getVideo_id());
		Map<String, Object> merUser = od.getObjectBySql(userSql.getSql(), userSql.getParams());
		if(merUser!=null){
			TeacherOrder to = new TeacherOrder();
			double percent = Double.parseDouble(merUser.get("teachPercent").toString());
			if(percent==-1){
				Map<String,Object> params = new HashMap<String,Object>();
				params.put("sys_key", Constant.SYSTEM_KEY_TEACHER_DFAULT_PERCENT);
				params.put("status", Constant.YES_INT);
				params.put("type", Constant.SYSTEM_TYPE_NUMBER);
				Sys sys = od.getObjByParams(Sys.class, params);
				if(sys==null){
					percent = 0;
				}else{
					percent = Double.parseDouble(sys.getSys_value());
				}
				to.setPercent_type(Statics.TEACHER_ORDER_PERCENT_TYPE_DEFAULT);
			}else{
				to.setPercent_type(Statics.TEACHER_ORDER_PERCENT_TYPE_CUSTOM);
			}
			double achieve = order.getAchieve();
			double teacherAchieve = MathUtil.toAny(2, (achieve * percent));
			double serverAchieve = achieve - teacherAchieve;
			to.setServer_achieve(serverAchieve);
			to.setStatus(Constant.YES_INT);
			to.setSum_money(achieve);
			to.setTeacher_achieve(teacherAchieve);
			to.setTeacher_id(merUser.get("id").toString());
			to.setCreate_time(now);
			to.setOrder_number(order.getOrder_num());
			to.setTeacher_percent(percent);
			String toId = od.save(to);
			if(toId==null)
				logger.error("订单编号为" + order.getOrder_num() + "的订单在走ping++回调时没有保存有效的讲师收益信息！");
		}
		return merUser;
	}
	
	@Override
	public Code getIosTypeList() {
		String sql = "select `id`,`payAmount`,`iosAmount`,`multiple` from `tb_ios_coin` where `isDoing` = " + Constant.YES_INT + " order by `payAmount` asc";
		List<Map<String, Object>> list = od.getListBySql(sql, null, null, null);
		Code result = Code.init(true, 0, "", (list==null?new ArrayList<Map<String, Object>>():list));
		return result;
	}

	@Override
	public Code iosWebHook(String userId, String coinId) {
		User user = od.getObjById(User.class, userId);
		if(user==null)
			return Code.init(false, 5, "用户不存在！","");
		IosCoin ic = od.getObjById(IosCoin.class, coinId);
		UserToIosCoins uc = new UserToIosCoins();
		uc.setBuyTime(System.currentTimeMillis());
		uc.setCoinId(coinId);
		uc.setUserId(userId);
		if(ic==null){
			uc.setState(Constant.NO_INT);
			uc.setDefeatMessage("没有找到指定的点券购买规则！");
			String ucId = od.save(uc);
			if(ucId==null)
				logger.warn("在保存用户充值记录时没有保存成功！充值失败：没有找到指定的点券购买规则！用户Id：" + userId + "  coinId：" + coinId);
			return Code.init(false, 6, "充值失败！请联系管理员！",user.getIosCoints());
		}
		if(ic.getIsDoing()!=Constant.YES_INT){
			uc.setState(Constant.NO_INT);
			uc.setDefeatMessage("该充值规则已经禁用！");
			String ucId = od.save(uc);
			if(ucId==null)
				logger.warn("在保存用户充值记录时没有保存成功！充值失败：该充值规则已经禁用！用户Id：" + userId + "  coinId：" + coinId);
			return Code.init(false, 6, "充值失败！请联系管理员！",user.getIosCoints());
		}
		double iosAmount = ic.getIosAmount();
		double addPercent = ic.getMultiple();
		double coins = MathUtil.toAny(2, iosAmount * (1 + addPercent));
		uc.setCoin(coins);
		uc.setState(Constant.YES_INT);
		String ucId = od.save(uc);
		if(ucId==null)
			logger.warn("在保存用户充值记录时没有保存成功！充值成功！用户Id：" + userId + "  coinId：" + coinId + " 充值数值：" + coins);
		double userCoins = user.getIosCoints();
		user.setIosCoints(userCoins + coins);
		od.update(user);
		return Code.init(true, 0, "充值成功！", user.getIosCoints());
	}

	@Override
	public MobileMessage iosHook(Order order) throws Exception {
		
		User user = od.getObjById(User.class, order.getUser_id());
		if(user==null){
			od.myDeleteById(Order.class, order.getId().toString());
			return MobileMessageCondition.addCondition(false, 9, "非法操作！", "");
		}
		double shengyu = user.getIosCoints() - order.getPrice();
		if(shengyu < 0){
			od.myDeleteById(Order.class, order.getId().toString());
			return MobileMessageCondition.addCondition(false, 8, "余额不足！", "");
		}
		
		long now = System.currentTimeMillis();
		
		user.setIosCoints(shengyu);
		user.setUpdate_time(now);
		template.getHibernateTemplate().update(user);
		
		order.setPay_time(now);
		order.setStatus(Constant.PAY_STATUS_PAYED);
		template.getHibernateTemplate().update(order);
		
		Map<String, Object> merUser = saveTeacherOder(order, now);
		
		if(order.getType()==Statics.ORDER_TYPE_VIDEO){
			UserToBuyVideos buy = new UserToBuyVideos();
			buy.setBuyTime(now);
			buy.setOrderNum(order.getOrder_num());
			buy.setUserId(order.getUser_id());
			buy.setVideoId(order.getVideo_id());
			Serializable serId = template.getHibernateTemplate().save(buy);
			if(serId==null)
				return MobileMessageCondition.addCondition(false, 1, "数据库错误！", "");
			
			// 通知卖家有人购买了他的视频
			if(merUser!=null){
				ps.pushEveryOne(Statics.PUSH_ID_VIDEO_BUY, Statics.PUSH_TYPE_FORTHWITH_TABLE, merUser.get("id").toString());
			}
		}else if(order.getType()==Statics.ORDER_TYPE_ACTIVITY){
			ActivityUser au = new ActivityUser();
			au.setActivity_id(order.getVideo_id());
			au.setEnter_time(now);
			au.setAppearance(order.getPrice());
			au.setMan_type(user.getUser_type());
			au.setUser_id(user.getId().toString());
			String auId = od.save(au);
			if(auId==null)
				return MobileMessageCondition.addCondition(false, 1, "数据库错误！", "");
			
			/**
			 * 活动区域主办方 二期开放
			 */
			/*// 通知主办方有人参加了他的活动
			as.cleanActivities(order.getVideo_id());
			Activity activity = od.getObjById(Activity.class, order.getVideo_id());
			if(activity!=null&&!activity.getTeacher_id().equals("")){
				String count = "select count(`id`) from `tb_order` where `status` = " + Constant.PAY_STATUS_PAYED + " and `video_id` = '" + order.getVideo_id() + "'";
				int activityBuyCount = od.getObjCountBySql(count, null);
				ps.pushEveryOne("", "活动购买通知", Statics.PUSH_TYPE_FORTHWITH_NOTABLE, "有人购买了您的\"" + activity.getTitle() + "\"活动，当前该活动的参加人数为：" + activity.getPart_num() + "人，已经支付的人数为：" + (activityBuyCount + 1) + "人", null, activity.getTeacher_id());
			}
			
			// 参与活动赠送优惠券
			cus.sendForUser(SystemCoupons.BUY_ACTIVITY, now, user.getId().toString(), "参加活动获得优惠券", "恭喜您成功的参与的活动获得了优惠券！请注意查看！");*/
		}
		
		// 增加积分
		MobileMessage addPoint = point.addPoint(user, PointEachType.PAY_POINT,order.getPrice());
		String addStr = "";
		// 通知买家购买成功和获得的积分数量
		if(addPoint.isResult())
			addStr = "您获得了" + addPoint.getResponse().toString() + "个积分！";
		ps.pushEveryOne("", "购买成功通知", Statics.PUSH_TYPE_FORTHWITH_NOTABLE, "恭喜您成功购买我们的产品！" + addStr, null, user.getId().toString());
		
		// 统计量加1
//		is.addStatistics(Statics.statics, type, ids);
		
		return MobileMessageCondition.addCondition(true, 0, "购买成功！", shengyu);
	}

}
