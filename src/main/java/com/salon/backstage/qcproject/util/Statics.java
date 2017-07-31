package com.salon.backstage.qcproject.util;

import com.salon.backstage.pub.bsc.domain.Constant;

/**
 * 作者：齐潮
 * 创建日期：2016年12月26日
 * 类说明：存储一些静态量
 */
public class Statics {

	/**
	 * 用户点击我的收藏：活动
	 */
	public final static int COLLECT_TYPE_HD = 1;
	
	/**
	 * 用户点击我的收藏：名人大咖
	 */
	public final static int COLLECT_TYPE_MRDK = 2;
	
	/**
	 * 用户点击我的收藏：视频
	 */
	public final static int COLLECT_TYPE_SP = 3;
	
	/**
	 * 表示点倒赞
	 */
	public final static int LIKE_NOT = 1;
	
	/**
	 * 表示点正赞
	 */
	public final static int LIKE_YES = 0;
	
	/**
	 * 统计类型：课程
	 */
	public final static int STATICS_TYPE_KC = 1;
	
	/**
	 * 统计类型：活动
	 */
	public final static int STATICS_TYPE_HD = 2;
	
	/**
	 * 统计类型：视频
	 */
	public final static int STATICS_TYPE_SP = 3;
	
	/**
	 * 统计类型：讲师
	 */
	public final static int STATICS_TYPE_JS = 4;
	
	/**
	 * 活动宣传类型：图片
	 */
	public final static int ACTIVITY_SHOWTYPE_PIC = 0;
	
	/**
	 * 活动宣传类型：视频
	 */
	public final static int ACTIVITY_SHOWTYPE_VIDEO = 1;
	
	/**
	 * 课程宣传类型：图片
	 */
	public final static int COURSE_SHOWTYPE_PIC = 0;
	
	/**
	 * 课程宣传类型：视频
	 */
	public final static int COURSE_SHOWTYPE_VIDEO = 1;
	
	/**
	 * 请求活动类型：全国
	 */
	public final static int ACTIVITY_REQUEST_ALL = 1;
	
	/**
	 * 请求活动类型：地区
	 */
	public final static int ACTIVITY_REQUEST_DISTRICT = 2;
	
	/**
	 * 请求活动类型：已结束
	 */
	public final static int ACTIVITY_REQUEST_ENDED = 3;
	
	/**
	 * 正在热播：热播榜
	 */
	public final static int COURSE_BEINGHIT_LOOKING = 1;
	
	/**
	 * 正在热播：热销榜
	 */
	public final static int COURSE_BEINGHIT_BUY = 2;
	
	/**
	 * 正在热播：热评榜
	 */
	public final static int COURSE_BEINGHIT_COMMENT = 3;
	
	/**
	 * 推送类型：即时(通过数据库查出push信息，信息灵活性低)
	 */
	public final static int PUSH_TYPE_FORTHWITH_TABLE = 1;
	
	/**
	 * 推送类型：即时(项目中自定义的推送信息，不需要查询数据库，信息灵活性高)
	 */
	public final static int PUSH_TYPE_FORTHWITH_NOTABLE = 2;
	
	/**
	 * 推送类型：后台人为推送(只能通过push对象来推送，查询数据库)
	 */
	public final static int PUSH_TYPE_MANMADE = 3;
	
	/**
	 * 推送id：表示视频点赞时通知该视频的讲师时需要发送的推送消息
	 */
	public final static String PUSH_ID_VIDEO_DIANZAN = "push_video_dianzan";
	
	/**
	 * 推送id：表示视频购买成功时向视频发布者推送的购买提醒
	 */
	public final static String PUSH_ID_VIDEO_BUY = "push_video_buy";
	
	/**
	 * 推送标题：当推送类型为PUSH_TYPE_FORTHWITH_NOTABLE时，生成系统信息时的标题
	 */
	public final static String PUSH_TITLE_DEFAULT = "系统自定义推送";
	
	/**
	 * 首页推荐类型：推荐视频
	 */
	public final static int HOMEPAGE_TYPE_VIDEO = 1;
	
	/**
	 * 首页推荐类型：推荐名人大佬
	 */
	public final static int HOMEPAGE_TYPE_CELEBRITY = 2;
	
	/**
	 * 首页推荐类型：推荐开店创业
	 */
	public final static int HOMEPAGE_TYPE_SHOP = 3;
	
	/**
	 * 首页推荐类型：推荐潮流技术
	 */
	public final static int HOMEPAGE_TYPE_TECHNOLOGY = 4;
	
	/**
	 * 轮播图关联类型：讲师
	 */
	public final static int BANNER_TYPE_TEACHER = 1;
	
	/**
	 * 轮播图关联类型：视频
	 */
	public final static int BANNER_TYPE_VIDEO = 0;
	
	/**
	 * 轮播图关联类型：活动
	 */
	public final static int BANNER_TYPE_ACTIVITY = 2;
	
	/**
	 * 轮播图展示位置：首页
	 */
	public final static int BANNER_SHOWTYPE_HOME = 1;
	
	/**
	 * 轮播图展示位置：发现
	 */
	public final static int BANNER_SHOWTYPE_FIND = 2;
	
	/**
	 * 课程所属频道：创业开店
	 */
	public final static int COURSE_CHANNEL_CYKD = 1;
	
	/**
	 * 课程所属频道：学潮流技术
	 */
	public final static int COURSE_CHANNEL_XCLJS = 2;
	
	/**
	 * 课程所属频道：不放入频道
	 */
	public final static int COURSE_CHANNEL_NONE = 0;
	
	/**
	 * 课程类型：平台直接制作-即将上映
	 */
	public final static int COURSE_PLAYING_WILLSHOW = 0;
	
	/**
	 * 课程类型：平台直接制作-已经上映
	 */
	public final static int COURSE_PLAYING_SHOW = 1;
	
	/**
	 * 课程类型：由求课程转变而来-已经上映
	 */
	public final static int COURSE_PLAYING_REQUESTTYPE = 2;
	
	/**
	 * 参与活动的身份：学员，普通
	 */
	public final static int ACTIVITY_USER_TYPE_STUDENT = Constant.USER_TYPE_STUDENT;
	
	/**
	 * 参与活动的身份：讲师
	 */
	public final static int ACTIVITY_USER_TYPE_TEACHER = Constant.USER_TYPE_TEACHER;
	
	/**
	 * 参与活动的身份：嘉宾
	 */
	public final static int ACTIVITY_USER_TYPE_TOP = 2;
	
	/**
	 * 优惠券发放类型：系统自动发放
	 */
	public final static int COUPON_PROVIDE_TYPE_SYSTEM = 1;
	
	/**
	 * 优惠券发放类型：通过后台管理系统人为发放
	 */
	public final static int COUPON_PROVIDE_TYPE_ADMIN = 0;
	
	/**
	 * 优惠券类型：抵用券
	 */
	public final static int COUPON_TYPE_VOUCHER = 0;
	
	/**
	 * 优惠券类型：打折券
	 */
	public final static int COUPON_TYPE_DISCOUNT = 1;
	
	/**
	 * 优惠券有效期类型：按月计算
	 */
	public final static int COUPON_EXPIRE_TYPE_MONTH = 1;
	
	/**
	 * 优惠券有效期类型：按天计算
	 */
	public final static int COUPON_EXPIRE_TYPE_DAY = 2;
	
	/**
	 * 优惠券有效期类型：永久
	 */
	public final static int COUPON_EXPIRE_TYPE_FOREVER = 3;
	
	/**
	 * redis中用于表示存储app端用户token的hash值
	 */
	public final static String REDIS_TOKEN_HASH = "apptokens";
	
	/**
	 * 评论级别：一级
	 */
	public final static int COMMENT_LEVEL_ONE = 1;
	
	/**
	 * 评论级别：二级
	 */
	public final static int COMMENT_LEVEL_TWO = 2;
	
	/**
	 * 评论类型：视频
	 */
	public final static int COMMENT_TYPE_VIDEO = 1;
	
	/**
	 * 评论类型：活动
	 */
	public final static int COMMENT_TYPE_ACTIVITY = 2;
	
	/**
	 * 评论类型：求课程
	 */
	public final static int COMMENT_TYPE_REQUEST = 3;
	
	/**
	 * 评论类型：讲师
	 */
	public final static int COMMENT_TYPE_TEACHER = 4;
	
	/**
	 * 评论类型：课程(即将上映)
	 */
	public final static int COMMENT_TYPE_COURSE = 5;
	
	/**
	 * 点赞类型：讲师
	 */
	public final static int LIKE_TYPE_TEACHER = 4;
	
	/**
	 * 点赞类型：评论
	 */
	public final static int LIKE_TYPE_COMMENT = 2;
	
	/**
	 * 点赞类型：活动
	 */
	public final static int LIKE_TYPE_ACTIVITY = 3;
	
	/**
	 * 点赞类型：课程
	 */
	public final static int LIKE_TYPE_COURSE = 1;
	
	/**
	 * 订单类型：视频
	 */
	public final static int ORDER_TYPE_VIDEO = 1;
	
	/**
	 * 订单类型：活动
	 */
	public final static int ORDER_TYPE_ACTIVITY = 2;
	
	/**
	 * 订单：活动类型的订单的有效时长，单位毫秒，主要是charge中要用，对活动查询的时候也需要用
	 */
	public final static long ORDER_ACTIVITY_EXTIME =1000 * 60 * 1;
	
	/**
	 * 订单：活动类型的订单的有效时长缓冲时间，单位毫秒。本项目的有效时长比ping++的有效时长多出来的时间
	 */
	public final static long ORDER_ACTIVITY_MOREEXTIME = 1000 * 30;
	
	/**
	 * 用户没有生日时，用户表里年龄段对应的年龄段Id
	 */
	public final static String USER_AGEID_NOBIRTHDAY = "noage";
	
	/**
	 * 当job表中的name值表示年龄段时的分隔符
	 */
	public final static String USER_AGEID_JOB_NAME_INDEX = "-";
	
	/**
	 * 讲师收益表：表示使用平台默认的订单收益抽取比例
	 */
	public final static int TEACHER_ORDER_PERCENT_TYPE_DEFAULT = 2;
	
	/**
	 * 讲师收益表：表示使用讲师自定义的订单收益抽取比例
	 */
	public final static int TEACHER_ORDER_PERCENT_TYPE_CUSTOM = 1;
	
	/**
	 * 讲师提现表：提现状态，刚刚申请，审核中
	 */
	public final static int TEACHER_SEND_STATUS_WILL = 1;
	
	/**
	 * 讲师提现表：提现状态，通过
	 */
	public final static int TEACHER_SEND_STATUS_OK = 2;
	
	/**
	 * 讲师提现表：提现状态，驳回
	 */
	public final static int TEACHER_SEND_STATUS_NO = 3;
	
	
	
	
	
	
	
	
	
	
	/**
	 * 统计数据——连续签到天数
	 */
	public final static String STATISTICS_SIGNDAYS = "signdays";
	/**
	 * 统计数据——优惠券数量
	 */
	public final static String STATISTICS_COUPON_COUNT = "coupon_count";
	/**
	 * 统计数据——点击量
	 */
	public final static String STATISTICS_CLICK_COUNT = "click_count";
	/**
	 * 统计数据——收藏量
	 */
	public final static String STATISTICS_COLLECT_COUNT = "collect_count";
	/**
	 * 统计数据——点赞量
	 */
	public final static String STATISTICS_LIKE_COUNT = "like_count";
	/**
	 * 统计数据——倒赞量
	 */
	public final static String STATISTICS_DISLIKE_COUNT = "dislike_count";
	/**
	 * 求课程来源：首页
	 */
	public final static int COURSE_QUESTION_HOMEPAGE = 1;
	/**
	 * 求课程来源：视频
	 */
	public final static int COURSE_QUESTION_VOID = 2;
	/**
	 * 求课程来源：讲师
	 */
	public final static int COURSE_QUESTION_TEACHER = 3;
	/**
	 * job表类型：职业
	 */
	public final static int JOB_TYPE_ZHIYE = 1;
	/**
	 * job表类型：讲师等级
	 */
	public final static int JOB_TYPE_LEVEL = 2;
	/**
	 * job表类型：年龄
	 */
	public final static int JOB_TYPE_AGE = 3;
	
	
	
	/**
	 * 统计数据——评论量
	 */
	public final static String STATISTICS_COMMENT_COUNT = "comment_count";
	/**
	 * 统计数据——分享量
	 */
	public final static String STATISTICS_SHARE_COUNT = "share_count";
	/**
	 * 统计数据——播放量
	 */
	public final static String STATISTICS_PLAY_COUNT = "play_count";
	/**
	 * 统计数据——关注量
	 */
	public final static String STATISTICS_FOLLOW_COUNT = "follow_count";
	
	
}
