package com.salon.backstage.qcproject.util;

/**
 * 作者：齐潮
 * 创建日期：2016年12月26日
 * 类说明：储存一些以enum形式存储的静态量
 */
public class Enums {
	
	/**
	 * 记录分享区域
	 * @author Administrator
	 *
	 */
	public static enum ShareDistrict{
		/**
		 * 分享区域：QQ好友
		 */
		SHARE_DISTRICT_QQ(1),
		/**
		 * 分享区域：QQ空间
		 */
		SHARE_DISTRICT_QQ_ZONE(2),
		/**
		 * 分享区域：微信好友
		 */
		SHARE_DISTRICT_WEIXIN(3),
		/**
		 * 分享区域：微信朋友圈
		 */
		SHARE_DISTRICT_WEIXIN_PENGYOUQUAN(4),
		/**
		 * 分享区域：新浪微博
		 */
		SHARE_DISTRICT_SINA(5);
		
		private int district;
		
		private ShareDistrict(int district){
			this.district = district;
		}
		
		public int getDistrict(){
			return this.district;
		}
		
		public static ShareDistrict getShareDistrict(int district){
			ShareDistrict[] sd = ShareDistrict.values();
			for(ShareDistrict s:sd){
				if(s.district == district)
					return s;
			}
			return null;
		}
	}
	
	/**
	 * 记录分享类型，
	 * @author Administrator
	 *
	 */
	public static enum ShareType{
		/**
		 * 分享类型：视频
		 */
		SHARE_TYPE_VIDEO(3),
		/**
		 * 分享类型：课程
		 */
		SHARE_TYPE_COURSE(7),
		/**
		 * 分享类型：求课程
		 */
		SHARE_TYPE_REQUEST(8),
		/**
		 * 分享类型：活动
		 */
		SHARE_TYPE_ACTIVITY(4);
		
		private int type;
		
		private ShareType(int type){
			this.type = type;
		}
		
		public int getType(){
			return this.type;
		}
		
		public static ShareType getShareType(int type){
			ShareType[] ss = ShareType.values();
			for(ShareType s:ss){
				if(s.type == type)
					return s;
			}
			return null;
		}
		
	}
	
	/**
	 * 存储系统发放的优惠券的类型以及其对应的系统表的key。
	 * type:数字标识，
	 * systemKey:系统表的key，
	 * couponType:这个type用于程序运算时的算法选择
	 * @author Administrator
	 *
	 */
	public static enum SystemCoupons{
		
		/**
		 * 分享下载客户端宣传链接
		 */
		SHARE_CLIENT_URL(1,"getcoupons_share_client_url",SystemCouponsType.ONLY_ONE),
		/**
		 * 用户发起求教程申请，审核通过
		 */
		USER_REQUEST_PASS(2,"getcoupons_user_request_pass",SystemCouponsType.NONE),
		/**
		 * 分享视频
		 */
		SHARE_VIDEO(3,"getcoupons_share_video",SystemCouponsType.NONE),
		/**
		 * 分享活动
		 */
		SHARE_ACTIVITY(4,"getcoupons_share_activity",SystemCouponsType.NONE),
		/**
		 * 即将上映课程，点击喜欢或不喜欢
		 */
		LIKE_OR_DISLIKE(5,"getcoupons_like_or_dislike",SystemCouponsType.NONE),
		/**
		 * 参与线下活动支付的金额，赠送一个优惠价
		 */
		BUY_ACTIVITY(6,"getcoupons_buy_activity",SystemCouponsType.NONE),
		/**
		 * 分享课程
		 */
		SHARE_COURSE(7,"getcoupons_share_course",SystemCouponsType.NONE),
		/**
		 * 分享求课程
		 */
		SHARE_REQUEST(8,"getcoupons_share_request",SystemCouponsType.NONE);
		
		private int type;
		private String systemKey;
		private SystemCouponsType couponType;
		
		private SystemCoupons(int type,String systemKey,SystemCouponsType couponType){
			this.type = type;
			this.systemKey = systemKey;
			this.couponType = couponType;
		}
		
		public String getSystemKey(){
			return this.systemKey;
		}
		
		public int getType(){
			return this.type;
		}
		
		public SystemCouponsType getSystemCouponsType(){
			return this.couponType;
		}
		
		/**
		 * 通过type来获取有效的SystemCoupons，如果type不合法，返回null
		 * @param type
		 * @return
		 */
		public static SystemCoupons getSystemCoupons(int type){
			SystemCoupons[] cs = SystemCoupons.values();
			for(SystemCoupons c:cs){
				if(c.type==type)
					return c;
			}
			return null;
		}
		
	}
	
	/**
	 * 发放优惠券的处理类型，作废
	 * @author Administrator
	 *
	 */
	public static enum SystemCouponsType{
		/**
		 * 仅一次
		 */
		ONLY_ONE,
		/**
		 * 无次数限制
		 */
		NONE;
		
	}
	
	/**
	 * 用于记录活动状态的信息
	 * @author Administrator
	 *
	 */
	public static enum ActivityStatus{
		/**
		 * 报名未开始
		 */
		ACTIVITY_STATUS_NONE(1,"未开始"),
		/**
		 * 报名中
		 */
		ACTIVITY_STATUS_ENROLLING(2,"报名中"),
		/**
		 * 报名结束，准备中
		 */
		ACTIVITY_STATUS_PREPARING(3,"报名结束"),
		/**
		 * 进行中
		 */
		ACTIVITY_STATUS_DOING(4,"进行中"),
		/**
		 * 活动已经结束
		 */
		ACTIVITY_STATUS_END(5,"活动结束"),
		/**
		 * 活动已取消
		 */
		ACTIVITY_STATUS_CANCEL(6,"已取消");
		
		private int type;
		
		private String message;
		
		private ActivityStatus(int type,String message){
			this.type = type;
			this.message = message;
		}
		
		public int getType(){
			return this.type;
		}
		
		public String getMessage(){
			return this.message;
		}
		
		/**
		 * 通过数字标识符获取一个活动状态信息，type不合法时返回null
		 * @param type
		 * @return
		 */
		public static ActivityStatus getActivityStatus(int type){
			ActivityStatus[] array = ActivityStatus.values();
			for(int i=0;i<array.length;i++){
				if(array[i].type==type)
					return array[i];
			}
			return null;
		}
	}
	
	
	
	
	/**
	 * 记录求课程的状态信息
	 * @author Administrator
	 *
	 */
	public static enum RequestStatus{
		/**
		 * 刚刚申请求课程信息，后台正在初步审核
		 */
		REQUEST_STATUS_INREVIEW(1,"审核中"),
		/**
		 * 没有通过审核，不予制作课程
		 */
		REQUEST_STATUS_DEFEAT(2,"审核失败"),
		/**
		 * 通过了审核，制作视频中(只要通过了审核，就视为正在制作视频，具体视频预测完成的时间在表中记录)
		 */
		REQUEST_STATUS_DOING(3,"制作中"),
		/**
		 * 过了预计的时间，因为某种原因导致没有将视频制作出来(不能通过后台来上传这个视频),制作失败
		 */
		REQUEST_STATUS_DOINGDEFEAT(4,"制作失败"),
		/**
		 * 制作完成，已经添加了相应的课程，以及相应的视频，可以通过app来查看该视频
		 */
		REQUEST_STATUS_SUCCESS(5,"制作完成");
		
		private int type;
		
		private String message;
		
		private RequestStatus(int type,String message){
			this.type = type;
			this.message = message;
		}
		
		public int getType(){
			return this.type;
		}
		
		public String getMessage(){
			return this.message;
		}
		
		/**
		 * 通过数字标识符获取一个求课程的状态信息，type不合法时返回null
		 * @param type
		 * @return
		 */
		public static RequestStatus getRequestStatus(int type){
			RequestStatus[] array = RequestStatus.values();
			for(int i=0;i<array.length;i++){
				if(array[i].type==type)
					return array[i];
			}
			return null;
		}
	}
	
}
