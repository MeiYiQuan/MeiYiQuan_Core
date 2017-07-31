package com.salon.backstage.pub.bsc.domain;

/**
 * 系统常量类
 */
public class Constant {
	
	public static final String REDIS_USER = "redis_user";
	
	public static final int RANK_PAGENO = 0;  // 首页-正在热播-排行榜 页数
	public static final int RANK_PAGESIZE = 5;// 首页-正在热播-排行榜 每页显示个数
	
	public static final int PLAYSOON_PAGENO = 0;   //首页-即将上映 页数
	public static final int PLAYSOON_PAGESIZE = 5; //首页-即将上映 每页显示个数
	
	public static final int SUBJECT_PAGENO = 0;    //首页-正在热播-精彩专题 
	public static final int SUBJECT_PAGESIZE = 5;
	
	public static final int TEACHER_DETAIL_COURSE_PAGENO = 0;  //讲师详情页课程分页
	public static final int TEACHER_DETAIL_COURSE_PAGESIZE = 5;
	
	public static final int SUBJECT_SOMEONE_PAGENO = 0;     //首页-正在热播-精彩专题-某个专题显示的课程 
	public static final int SUBJECT_SOMEONE_PAGESIZE = 5;
	
	public static final int CHANNEL_COURSE_PAGENO = 0;    //频道对应课程的分页
	public static final int CHANNEL_COURSE_PAGESIZE = 5;
	
	public static final String COUPON_CASH_FIRST_ID = "1";     // 1元现金券      
	public static final String COUPON_CASH_SECOND_ID = "2";    // 5元现金券     
	public static final String COUPON_CASH_THIRD_ID = "3";     // 10元现金券   
	public static final String COUPON_CASH_FOURTH_ID = "4";    // 20元现金券  
	public static final String COUPON_CASH_FIVE_ID = "5";      // 30元现金券  
	public static final String COUPON_CASH_SIXTH_ID = "6";     // 50元现金券   
	public static final String COUPON_CASH_SEVENTH_ID = "7";   // 100元现金券  
	
	public static final String COUPON_OFF_FIRST_ID = "8";      // 1折券 
	public static final String COUPON_OFF_SECOND_ID = "9";     // 2折券
	public static final String COUPON_OFF_THIRD_ID = "10";     // 3折券
	public static final String COUPON_OFF_FOURTH_ID = "11";    // 4折券
	public static final String COUPON_OFF_FIVE_ID = "12";      // 5折券  
	public static final String COUPON_OFF_SIXTH_ID = "13";     // 6折券 
	public static final String COUPON_OFF_SEVENTH_ID = "14";   // 7折券 
	public static final String COUPON_OFF_EIGHTH_ID = "15";    // 8折券 
	public static final String COUPON_OFF_NINTH_ID = "16";     // 9折券
	
	public static final int REGISTER_COUPON_SECOND_AMOUNT = 10;   // 首次注册赠送5元优惠券的个数
	public static final int REGISTER_COUPON_THIRD_AMOUNT = 3;     // 首次注册赠送10元优惠券的个数
	public static final int REGISTER_COUPON_FOURTH_AMOUNT = 2;    // 首次注册赠送20元优惠券的个数(1个),为第一次消费加1个,共2个 
	
	public static final String USER_USERNAME_DEFAULT = "美艺圈";
	
	/**
	 * 系统常量类型：图片
	 */
	public static final int SYSTEM_TYPE_PHOTO = 1;
	
	/**
	 * 系统常量类型：数值
	 */
	public static final int SYSTEM_TYPE_NUMBER = 2;
	
	/**
	 * 系统常量类型：优惠券发放规则
	 */
	public static final int SYSTEM_TYPE_COUPONS = 3;
	
	/**
	 * 系统常量类型：文本类型
	 */
	public static final int SYSTEM_TYPE_TEXT = 4;
	
	/**
	 * 系统常量key值：推送完毕生成系统信息时的图片地址
	 */
	public static final String SYSTEM_KEY_PUSH_SYSPHOTO = "push_systeminfo_photo";
	
	/**
	 * 系统常量key值：讲师从订单收益中抽取的百分比。当讲师没有设置抽取百分比时(对应字段值为-1表示没有设置)，选用这个默认字段
	 */
	public static final String SYSTEM_KEY_TEACHER_DFAULT_PERCENT = "teacher_order_default_percent";
	
	/**
	 * 系统常量key值：当后台管理系统没有给视频设置免费观看时长时，免费观看时长取下面的字段
	 */
	public static final String SYSTEM_KEY_VIDEO_FREESHOW_DEFAULTTIME = "video_freeshow_defaulttime";
	
	/**
	 * 系统常量key值：用于表示求课程背景图片的key字段
	 */
	public static final String SYSTEM_KEY_REQUEPICURL = "user_video_request_pic_url";
	
	/**
	 * 系统常量key值：用于表示关于我们的介绍信息
	 */
	public static final String SYSTEM_KEY_ABOUTUS = "system_about_us";
	
	/**
	 * 系统常量key值：用于表示苹果内购方式的税率，是个小于1的数
	 */
	public static final String SYSTEM_KEY_IOS_BUY_TAX = "order_tax_ios";
	
	/**
	 * 系统常量key值：用于表示普通购买方式的税率，是个小于1的数
	 */
	public static final String SYSTEM_KEY_BUY_TAX = "order_tax";
	
	/**
	 * 系统常量key值：用于表示苹果内购方式苹果官方扣除比例，是个小于1的数
	 */
	public static final String SYSTEM_KEY_IOS_BUY = "order_ios";
	
	/**
	 * 系统常量key值：用于表示添加求课程时背景图片的url
	 */
	public static final String SYSTEM_KEY_REQUEST_BACKPIC = "request_background_pic";
	
	/**
	 * 订单支付状态：未支付
	 */
	public static final int PAY_STATUS_NOPAY = 0;
	
	/**
	 * 订单支付状态：已支付
	 */
	public static final int PAY_STATUS_PAYED = 1;
	
	/**
	 * 记录具体的支付方式
	 * @author Administrator
	 *
	 */
	public static enum PayStyle{
		/**
		 * ping++的支付宝方式
		 */
		PINGPP_ALIPAY(1,Constant.PAY_PINGPP,"alipay"),
		/**
		 * ping++的微信支付方式
		 */
		PINGPP_WX(2,Constant.PAY_PINGPP,"wx"),
		/**
		 * 支付宝原生支付
		 */
		ALIPAY(3,Constant.PAY_ALIPAY,""),
		/**
		 * ios专用支付方式
		 */
		IOS(4,Constant.PAY_IOS,"");
		
		private int sendType;
		
		private int payType;
		
		private String value;
		
		private PayStyle(int sendType,int payType,String value){
			this.sendType = sendType;
			this.payType = payType;
			this.value = value;
		}
		
		/**
		 * 通过前台传来的支付方式的标识来获取一个支付方式
		 * @param sendType
		 * @return
		 */
		public static PayStyle getPayStyle(int sendType){
			if(PINGPP_ALIPAY.sendType==sendType){
				return PINGPP_ALIPAY;
			}else if(PINGPP_WX.sendType==sendType){
				return PINGPP_WX;
			}else if(ALIPAY.sendType==sendType){
				return ALIPAY;
			}else if(IOS.sendType==sendType){
				return IOS;
			}
			return null;
		}
		
		public int getSendType(){
			return this.sendType;
		}
		
		public int getPayType(){
			return this.payType;
		}
		
		public String getValue(){
			return this.value;
		}
	}
	
	/**
	 * 通过ping++支付的方式
	 */
	public static final int PAY_PINGPP = 1;
	
	/**
	 * 通过原生的支付宝方式
	 */
	public static final int PAY_ALIPAY = 2;
	
	/**
	 * 通过苹果专有支付的方式
	 */
	public static final int PAY_IOS = 3;
	
	/**
	 * 存储积分类型的对应关系(具体到每一个)
	 * @author Administrator
	 *
	 */
	public static enum PointEachType{
		/**
		 * 新建用户获得的积分
		 */
		USER_CREATE_POINT(1,PointType.ONLY_ONE),
		/**
		 * 现金消费获得的积分
		 */
		PAY_POINT(2,PointType.PAY_SUCCESS),
		/**
		 * 上传头像获得的积分
		 */
		USER_PIC_POINT(3,PointType.ONLY_ONE),
		/**
		 * 手机验证获得的积分
		 */
		PHONE_VALIDAE_POINT(4,PointType.ONLY_ONE),
		/**
		 * 完善个人资料获得的积分
		 */
		USER_DETAIL_POINT(5,PointType.ONLY_ONE),
		/**
		 * 每日首次登陆获得的积分
		 */
		DAY_FIRST_POINT(6,PointType.SIGN_IN_DAY),
		/**
		 * 课程评论获得的积分
		 */
		COMMENT_POINT(7,PointType.MANY_OF_DAY),
		/**
		 * 邀请好友注册，通过手机验证获得的积分
		 */
		FRIEND_POINT(8,PointType.NO_RESTRICT),
		/**
		 * 下载客户端并注册获得的积分
		 */
		DOWNLOAD_CLIENT_POINT(9,PointType.ONLY_ONE),
		/**
		 * 观看课程获得的积分
		 */
		LEARN_POINT(10,PointType.NO_RESTRICT),
		/**
		 * 收藏获得的积分
		 */
		COLLECT_POINT(11,PointType.NO_RESTRICT),
		/**
		 * 参与投票获得的积分
		 */
		VOTE_POINT(12,PointType.MANY_OF_DAY);
		
		private int type;
		
		private PointType point;
		
		private PointEachType(int type,PointType point){
			this.type = type;
			this.point = point;
		}
		
		public int getType(){
			return this.type;
		}
		
		public PointType getPointType(){
			return this.point;
		}
		
		/**
		 * 获得一个PointEachType
		 * @param type
		 * @return
		 */
		public static PointEachType getPointEachType(int type){
			if(USER_CREATE_POINT.type == type){
				return USER_CREATE_POINT;
			}else if(PAY_POINT.type == type){
				return PAY_POINT;
			}else if(USER_PIC_POINT.type == type){
				return USER_PIC_POINT;
			}else if(PHONE_VALIDAE_POINT.type == type){
				return PHONE_VALIDAE_POINT;
			}else if(USER_DETAIL_POINT.type == type){
				return USER_DETAIL_POINT;
			}else if(DAY_FIRST_POINT.type == type){
				return DAY_FIRST_POINT;
			}else if(COMMENT_POINT.type == type){
				return COMMENT_POINT;
			}else if(FRIEND_POINT.type == type){
				return FRIEND_POINT;
			}else if(DOWNLOAD_CLIENT_POINT.type == type){
				return DOWNLOAD_CLIENT_POINT;
			}else if(LEARN_POINT.type == type){
				return LEARN_POINT;
			}else if(COLLECT_POINT.type == type){
				return COLLECT_POINT;
			}else if(VOTE_POINT.type == type){
				return VOTE_POINT;
			}
			return null;
			
		}
		
	}
	
	/**
	 * 存储积分类型的对应关系，大的分类，与算法对应
	 * @author Administrator
	 *
	 */
	public static enum PointType{
		/**
		 * 无上限，每天无数次，没有任何限制。每次添加的积分数量为PointMost的way_single字段
		 */
		NO_RESTRICT(1),
		/**
		 * 每日签到。每次添加的积分数量为PointDay的day_point字段
		 */
		SIGN_IN_DAY(2),
		/**
		 * 每日没有次数限制，但是有每日的积分上限。每次添加的积分数量为PointMost的way_single字段
		 */
		MANY_OF_DAY(3),
		/**
		 * 仅一次。每次添加的积分数量为PointMost的way_most字段
		 */
		ONLY_ONE(4),
		/**
		 * 现金消费获取积分，每元添加的积分为PointMost的way_single字段
		 */
		PAY_SUCCESS(5);
		
		private int type;
		
		PointType(int type){
			this.type = type;
		}
		
		/**
		 * 获取积分类型标识
		 * @return
		 */
		public int getType(){
			return this.type;
		}
		
		/**
		 * 获取积分类型
		 * @param type
		 * @return
		 */
		public static PointType getPointType(int type){
			if(NO_RESTRICT.type == type){
				return NO_RESTRICT;
			}else if(SIGN_IN_DAY.type == type){
				return SIGN_IN_DAY;
			}else if(MANY_OF_DAY.type == type){
				return MANY_OF_DAY;
			}else if(ONLY_ONE.type == type){
				return ONLY_ONE;
			}else if(PAY_SUCCESS.type == type){
				return PAY_SUCCESS;
			}
			
			return null;
		}
		
	}
	
	/**
	 * 存储用户类型的对应关系
	 * @author Administrator
	 *
	 */
	public static enum UserType{
		/**
		 * 用户类型：普通用户
		 */
		USER_TYPE_GENERAL(0),
		/**
		 * 用户类型：微信用户
		 */
		USER_TYPE_WEIXIN(1);
		
		private int type;
		
		private UserType(int type){
			this.type = type;
		}
		
		public static UserType getUserType(int type){
			if(USER_TYPE_GENERAL.type==type){
				return USER_TYPE_GENERAL;
			}else if(USER_TYPE_WEIXIN.type==type){
				return USER_TYPE_WEIXIN;
			}
			return null;
		}
		
		public int getType(){
			return this.type;
		}
	}
	
	/**
	 * 表示该用户是学员
	 */
	public static final int USER_TYPE_STUDENT = 0;
	
	/**
	 * 表示该用户是讲师
	 */
	public static final int USER_TYPE_TEACHER = 1;
	
	/**
	 * 表示是安卓登陆
	 */
	public static final int USER_PHONETYPE_ANDROID = 0;
	
	/**
	 * 表示时ios登陆
	 */
	public static final int USER_PHONETYPE_IOS = 1;
	
	/**
	 * 用户性别：男
	 */
	public static final int USER_SEX_MAN = 2;
	
	/**
	 * 用户性别：女
	 */
	public static final int USER_SEX_WOMEN = 1;
	
	/**
	 * 用户性别：未知
	 */
	public static final int USER_SEX_UNKNOW = 0;
	
	/**
	 * 含义为：是，已经做了的，启用
	 */
	public static final int YES_INT = 1;
	
	/**
	 * 含义为：否，还没有做，禁用
	 */
	public static final int NO_INT = 2;
	
	/**
	 * 频道级别：顶级
	 */
	public static final int CHANNEL_TOP = 0;
	
	/**
	 * 频道级别：一级，用于接收app发送的选择和sql语句拼接的select字段中，在sql拼接字段中用这些值来表示频道级别
	 */
	public static final int CHANNEL_ONE_LEVEL = 1;
	
	/**
	 * 频道级别：二级，用于接收app发送的选择和sql语句拼接的select字段中，在sql拼接字段中用这些值来表示频道级别
	 */
	public static final int CHANNEL_TWO_LEVEL = 2;
	
	/**
	 * 频道级别：未知，用于接收app发送的选择和sql语句拼接的select字段中，在sql拼接字段中用这些值来表示频道级别
	 */
	public static final int CHANNEL_UNKNOWN = -1;
	
	/**
	 * 顶级频道的pid的值，是否为顶级频道的标识
	 */
	public static final String CHANNEL_TOP_PID = "0";
	
	/**
	 * 用于去表示两个顶级频道的id与app端传送的类型标识的对应关系
	 * @author Administrator
	 *
	 */
	public static enum ChannelTop{
		
		/**
		 * 创业开店
		 */
		CHANNEL_TOP_CHUANGYEKAIDIAN(1,"67850e9f91524c6d9ac8c8b6ce3533ce"),
		/**
		 * 潮流技术
		 */
		CHANNEL_TOP_CHAOLIUJISHU(2,"fe82dcb588314eaa94282f8b88b9d9b1"),
		/**
		 * 全部
		 */
		CHANNEL_TOP_ALL(3,null);
		
		private Object type;
		
		private String topId;
		
		private ChannelTop(Object type,String topId){
			this.type = type;
			this.topId = topId;
		}
		
		/**
		 * 传进来type，获得相应的ChannelTop，如果返回null，则表示没有传入合法的参数
		 * @param type
		 * @return
		 */
		public static ChannelTop getChannelTopByType(Object type){
			if(CHANNEL_TOP_CHUANGYEKAIDIAN.type.equals(type)){
				return CHANNEL_TOP_CHUANGYEKAIDIAN;
			}else if(CHANNEL_TOP_CHAOLIUJISHU.type.equals(type)){
				return CHANNEL_TOP_CHAOLIUJISHU;
			}else if(CHANNEL_TOP_ALL.type.equals(type)){
				return CHANNEL_TOP_ALL;
			}
			return null;
		}
		
		/**
		 * 获得当前的顶级频道的id
		 * @return
		 */
		public String getTopId(){
			return this.topId;
		}
	}
	
}











