package com.salon.backstage.homepage.like.service.impl;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.salon.backstage.common.util.ExtraSpringHibernateTemplate;
import com.salon.backstage.common.util.MobileMessageCondition;
import com.salon.backstage.common.util.afterCreate.DateAddUtil;
import com.salon.backstage.homepage.like.service.ILikeService;
import com.salon.backstage.homepage.point.service.IPointService;
import com.salon.backstage.homepage.statistics.service.IStatisticsService;
import com.salon.backstage.pub.bsc.dao.po.CouponUser;
import com.salon.backstage.pub.bsc.dao.po.Like;
import com.salon.backstage.pub.bsc.dao.po.Statistics;
import com.salon.backstage.pub.bsc.dao.po.Sys;
import com.salon.backstage.pub.bsc.dao.po.User;
import com.salon.backstage.pub.bsc.dao.vo.MobileMessage;
import com.salon.backstage.pub.bsc.domain.Constant;
import com.salon.backstage.qcproject.dao.ObjectDao;
import com.salon.backstage.qcproject.service.CouponUserServiceNEW;
import com.salon.backstage.qcproject.util.Enums.SystemCoupons;
import com.salon.backstage.qcproject.util.Statics;


@Service
public class LikeServiceImpl implements ILikeService {
	
	@Autowired
	private ExtraSpringHibernateTemplate extraSpringHibernateTemplate;
	
	@Autowired
	private IStatisticsService iStatisticsService;
	
	@Autowired
	private ObjectDao od;
	
	@Autowired
	private CouponUserServiceNEW cus;
	
	@Override
	public int queryTeacherLikeCount(String teacherId) {
		Statistics statics = iStatisticsService.getStatistics(teacherId, Statics.STATICS_TYPE_JS);
		//实际值
		int follow_count = statics.getFollow_count();
		//虚拟量
		int follow_expect_count = statics.getFollow_expect_count();
		int coun = follow_count + follow_expect_count;
		return coun;
	}
	
	@Override
	public MobileMessage clickFromFromPlayingsoon(Map<String, Object> json) {
		try {
		String likedId = (String)json.get("likedId");
		String userId = (String)json.get("userid");
		String type = (String)json.get("type");
		String [] propName={"like_type_id","user_id"};
		String [] propValue={likedId,userId};
		Like like = extraSpringHibernateTemplate.findFirstOneByPropEq(Like.class, propName, propValue);
			if(like==null){
				 like = new Like();
				like.setUser_id(userId);
				like.setLike_dislike(Integer.valueOf(type));
				like.setLike_time(System.currentTimeMillis());
				like.setLike_type_id(likedId);
				extraSpringHibernateTemplate.getHibernateTemplate().save(like);
				int exist = existInCouponUser( userId, likedId);//点赞过程中判断是否已经获得该类型对应的优惠券0 未获得,1已经获得
				if(exist == 0){
					addCoupon( userId, likedId);
				}
				//执行点赞倒赞统计
				if(type.equals("0")){
//					iStatisticsService.addStatistics(likedId,Statics.STATISTICS_LIKE_COUNT);
					iStatisticsService.addStatistics(Statics.STATISTICS_LIKE_COUNT, Statics.STATICS_TYPE_KC,likedId);
				}else{
//					iStatisticsService.addStatistics(likedId,Statics.STATISTICS_DISLIKE_COUNT);
					iStatisticsService.addStatistics(Statics.STATISTICS_DISLIKE_COUNT, Statics.STATICS_TYPE_KC,likedId);
				}
				return MobileMessageCondition.addCondition(true, 0, "成功", "");
			}else{
				return  MobileMessageCondition.addCondition(false, 98, "您已经点过了", "");
			}
		} catch (Exception e) {
			e.printStackTrace();
			return  MobileMessageCondition.addCondition(false, 97, "保存点赞/点倒赞操作出现异常", null);
		}finally{
			
		}
	}
	
	/**
	 * 点赞过程中判断是否已经获得该类型对应的优惠券
	 */
	private int existInCouponUser(String userId,String likedId) { 
		String sql = "select * from tb_coupon_user where  get_type_id = '"+likedId+"' and user_id = '"+userId+"'";
		List<CouponUser> couponUserList = extraSpringHibernateTemplate.createSQLQueryFindAll(sql);
		int exist = 0;
		if(couponUserList.size()>0){
			exist = 1;
		}
		return exist;     //0 未获得,1已经获得
	}
	
	/**
	 * 给用户添加优惠券
	 */
	private void addCoupon(String userId,String likedId) { 
		CouponUser couponUser = new CouponUser();
		couponUser.setCoupon_id(Constant.COUPON_CASH_FIRST_ID);
		couponUser.setGet_time(Long.valueOf(new SimpleDateFormat("yyyyMMddHHmmss").format(new Date())));
		couponUser.setWarn_time(Long.valueOf(new SimpleDateFormat("yyyyMMddHHmmss").format(DateAddUtil.descDay(DateAddUtil.addMonth(new Date(), 3), 7))));
		couponUser.setExpire_time(Long.valueOf(new SimpleDateFormat("yyyyMMddHHmmss").format(DateAddUtil.addMonth(new Date(), 3))));
		couponUser.setUser_id(userId);
		/*couponUser.setGet_type(1);*/
		couponUser.setGet_type_id(likedId);
//		couponUser.setNumber(getMaxNumber()+1);
		extraSpringHibernateTemplate.getHibernateTemplate().save(couponUser);
	}
	
	/**
	 * 获得最大优惠券号
	 */
	private Long getMaxNumber(){
		String sql = "select max(number) maxNumber from tb_coupon_user";
		List<Map> listMap = extraSpringHibernateTemplate.createSQLQueryFindAll(sql);
		Long maxNumber = Long.valueOf(listMap.get(0).get("maxNumber").toString());
		return maxNumber;
	}

	@Override
	public MobileMessage click(String userId, String likeId, int type, int likeType,int index) {
		User user = od.getObjById(User.class, userId);
		if(user==null)
			return MobileMessageCondition.addCondition(false, 96, "系统错误！请重新登陆！", "");
		if(user.getUser_state()==Constant.NO_INT)
			return MobileMessageCondition.addCondition(false, 95, "权限不足！请重新登陆！", "");
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("user_id", userId);
		params.put("like_type", likeType);
		params.put("like_dislike", type);
		params.put("like_type_id", likeId);
		Like like = od.getObjByParams(Like.class, params);
		if(like!=null)
			return MobileMessageCondition.addCondition(false, 98, "您已经点过了", "");
		like = new Like();
		long now = System.currentTimeMillis();
		like.setLike_dislike(type);
		like.setLike_time(now);
		like.setLike_type(likeType);
		like.setUser_id(userId);
		like.setLike_type_id(likeId);
		String id = od.save(like);
		if(id==null)
			return MobileMessageCondition.addCondition(false, 97, "系统繁忙，请稍后再试！", "");
		// 添加积分（取消）
		// 添加统计
		if(likeType!=Statics.LIKE_TYPE_COMMENT){
			int staticsType = -1;
			switch(likeType){
				case Statics.LIKE_TYPE_ACTIVITY:
					staticsType = Statics.STATICS_TYPE_HD;
					break;
				case Statics.LIKE_TYPE_COURSE:
					staticsType = Statics.STATICS_TYPE_KC;
					break;
				case Statics.LIKE_TYPE_TEACHER:
					staticsType = Statics.STATICS_TYPE_JS;
					break;
			}
			if(type==Statics.LIKE_YES){
				iStatisticsService.addStatistics(Statics.STATISTICS_LIKE_COUNT, staticsType,likeId);
			}else if(type==Statics.LIKE_NOT){
				iStatisticsService.addStatistics(Statics.STATISTICS_DISLIKE_COUNT, staticsType,likeId);
			}
		}
		// 如果是对即将上映的操作就给用户赠送优惠券
		if(index==9){
			cus.sendForUser(SystemCoupons.LIKE_OR_DISLIKE, now, userId, "点赞或者点倒赞获得优惠券", "恭喜！由于您积极参与点赞活动，获得了优惠券，请注意查看！");
		}
		return MobileMessageCondition.addCondition(true, 0, "操作成功！", "");
	}
	
}















