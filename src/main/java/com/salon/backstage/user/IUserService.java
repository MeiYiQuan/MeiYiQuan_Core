package com.salon.backstage.user;

import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Map;

import com.salon.backstage.pub.bsc.dao.po.SuggestionType;
import com.salon.backstage.pub.bsc.dao.po.User;
import com.salon.backstage.pub.bsc.dao.vo.MobileMessage;
import com.salon.backstage.qcproject.util.Code;
public interface IUserService {
	/**
	 * 通过电话和密码登陆
	 * @param json
	 * @return
	 * @throws NumberFormatException
	 * @throws Exception
	 */
	User queryCountByPhonePassword(Map<String, Object> json) throws NumberFormatException, Exception;
	
	
	/**
	 * 通过userId去获取该用户所有的积分
	 * @param userId
	 * @return
	 */
	public int getPointsByUserId(String userId);
	
	/**
	 * 注册的方法
	 * @param phone
	 * @param password
	 * @param district
	 * @return
	 * @throws NoSuchAlgorithmException 
	 * @throws Exception 
	 */
	public MobileMessage createUser(long phone,String password,String district) throws NoSuchAlgorithmException, Exception;
	
	/**
	 * 第三方登陆
	 * @param ssoId
	 * @param ssoName
	 * @param type
	 * @param sex
	 * @return
	 * @throws Exception 
	 */
	MobileMessage ssoLogin(String ssoId,String ssoName,String address,String picUrl,String registId,int phoneType,int type,int sex) throws Exception;
	
	/**
	 * 注册
	 * @param json
	 * @return
	 * @throws Exception
	 */
	User register(Map<String, Object> json) throws Exception;
	/**
	 * 修改密码(忘记)
	 * @param json
	 * @throws NoSuchAlgorithmException
	 */
	void editPassword(Map<String, Object> json) throws NoSuchAlgorithmException;
	/**
	 * 通过电话查询
	 * @param json
	 * @return
	 */
	User queryCountByPhone(Map<String, Object> json);
	
	/**
	 * 个人资料
	 * @param json
	 * @return
	 */
	Map persionalDataById(Map<String, Object> json);
	/**
	 * 更换头像
	 * @param json
	 */
	 void replaceHead(Map<String, Object> json);
	/**
	 * 名人大咖
	 * @param json
	 * @return
	 */
	 List<Map> queryCountByUserIdAndTeacher(Map<String, Object> json);
	 /**
	  * 视频
	  * @param json
	  * @return
	  */
	 List<Map> queryCountByUserIdAndVideo(Map<String, Object> json);
	 /**
	  * 活动
	  * @param json
	  * @return
	  */
	 List<Map> queryCountByUserIdAndActivity(Map<String, Object> json);
	 /**
	  * 修改我的搜集
	  * @param json
	  */
	 void deleteCollectByCollectTypeId(Map<String, Object> json);
	 /**
	  *浏览一周之前的数据
	  * @param json
	  * @return
	  */
	 List<Map> queryCollectForWeek(Map<String, Object> json);
	 /**
	  * 浏览一个月的数据
	  * @param json
	  * @return
	  */
	 List<Map> queryCollectForMonth(Map<String, Object> json);
	 /**
	  * 浏览一个月以前的数据
	  * @param json
	  * @return
	  */
	 List<Map> queryCollectForMonthBefore(Map<String, Object> json);
	 /**
	  * 浏览视频的百分比
	  * @param json
	  * @return
	  */
	 List<Map> queryPlayRecordPercent(String videoId,String userId);
	 /**
	  * 修改用户性别
	  * @param json
	  */
	 void editUserGender(Map<String, Object> json);
	 /**
	  * 修改用户星座
	  * @param json
	  */
	 void editUserZodiac(Map<String, Object> json);
	 /**
	  * 编辑用户的播放记录
	  * @param json
	  */
	 void  editPlayRecord(Map<String, Object> json);
	 /**
	  * 我的参与
	  * @param json
	  * @return
	  */
	 List<Map> queryMyParticipation(Map<String, Object> json);
	 /**
	  * 我参与的详情
	  * @param json
	  * @return
	  */
	 List<Map> queryActivityForDetail(Map<String, Object> json);
	 /**
	  * 我参与活动的参与者
	  * @param json
	  * @return
	  */
	 List<Map> queryActivityForparticipatorList(Map<String, Object> json);
	 /**
	  * 我参与活动的嘉宾
	  * @param json
	  * @return
	  */
	 List<Map> queryActivityForGuest(Map<String, Object> json);
	 /**
	  * 参与活动的人数
	  * @param json
	  * @return
	  */
	 List<Map> queryActivityForparticipatorCount(Map<String, Object> json);
	 /**
	  * 我发起教程的详情
	  * @param json
	  * @return
	  */
	 Map queryUserVideoRequest(Map<String, Object> json);
	 /**
	  * 添加要发起的教程的反馈
	  * @param json
	  */
	 void insertUserVideoRequest(Map<String, Object> json);
	 /**
	  * 修改登录密码
	  * @param json
	  */
	 void setUpEditPassword(Map<String, Object> json);
	 /**
	  * 通过id查询用户
	  * @param json
	  * @return
	  */
	 User queryUserById(Map<String, Object> json);
	 /**
	  * 获取系统消息
	  * @param json
	  * @return
	  */
	 List<Map> querySystemInfo(Map<String, Object> json);
	 /**
	  * 获取系统消息详情
	  * @param json
	  * @return
	  */
	 List<Map> querySystemInfoDetail(Map<String, Object> json);
	 /**
	  * 获取积分记录
	  * @param json
	  * @return
	  */
	 List<Map> queryPointAll(Map<String, Object> json);
	 /**
	  * 获取优惠券列表
	  * @param json
	  * @return
	  */
	 List<Map> queryCoupon(Map<String, Object> json);
	 /**
	  * 全国线下活动
	  * @param json
	  * @return
	  */
	 List<Map> queryActivityListForCountry(Map<String, Object> json);
	 /**
	  * 获取活动Id
	  * @param acitityId
	  * @return
	  */
	 Long queryCountByAcitityId(String acitityId);
	 /**
	  * 地方线下活动
	  * @param json
	  * @return
	  */
	 List<Map> queryCountByUserIdAndCity(Map<String, Object> json);
	 /**
	  * 获取结束的活动
	  * @param json
	  * @return
	  */
	 List<Map> queryCountByUserIdAndEnd(Map<String, Object> json);
	 /**
	  * 获取活动评论
	  * @param json
	  * @return
	  */
	 List<Map> queryActivityComment(Map<String, Object> json);
	 /**
	  * 活动的点赞量
	  * @param json
	  * @return
	  */
	 Long queryActivityLikes(Map<String, Object> json);
	 /**
	  * 获取活动的状态
	  * @param activityId
	  * @param json
	  * @return
	  */
	 List<Map> queryActivityStatus(String activityId,Map<String,Object> json);
	 /**
	  * 通过userid获取用户的评论
	  * @param userId
	  * @return
	  */
	 Long queryActivityComment(String userId);
	 /**
	  * 通过id删除评论
	  * @param json
	  */
	 MobileMessage deleteCommentById(Map<String, Object> json);
	 /**
	  * 视频播放量
	  * @param activityId
	  * @return
	  */
	 Long queryVideoPlayCount(String activityId);
	 /**
	  * 讲师详情
	  * @param json
	  * @return
	  */
	 Long queryTeacherDetail(Map<String, Object> json);
	 /**
	  * 视频点击总量
	  * @param json
	  * @return
	  */
	 Long queryTeacherVideo(Map<String, Object> json);
	 /**
	  * 视频付费点击量
	  * @param json
	  * @return
	  */
	Long queryTeacherVideo01(Map<String, Object> json);
	/**
	 * 查询活动数
	 * @param json
	 * @return
	 */
	Long queryCountByAcitity(Map<String, Object> json);
	/**
	 * 查询讲师的视频
	 * @param json
	 * @return
	 */
	List<Map> queryCountFuFeiCount(Map<String, Object> json);
	/**
	 * 查询讲师详情
	 * @param json
	 * @return
	 */
	List<Map> queryTeacher(Map<String, Object> json);
	/**
	 * 查询讲师的信息
	 * @param json
	 * @return
	 */
	Map queryTeacherInfo(Map<String, Object> json);
	/**
	 * 讲师的本月收益
	 * @param json
	 * @return
	 */
	Float queryTeacherIncomeMonth(Map<String, Object> json);
	/**
	 * 讲师上个月的收益
	 * @param json
	 * @return
	 */
	Float queryBeforeIncomeMonth(Map<String, Object> json);
	/**
	 * 讲师上个季度的收益
	 * @param json
	 * @return
	 */
	Float queryCurrentSeasonIncome(Map<String, Object> json);
	/**
	 * 讲师上个季度的收入
	 * @param json
	 * @return
	 */
	Float queryBeforeSeasonIncome(Map<String, Object> json);
	/**
	 * 讲师的总收益
	 * @param json
	 * @return
	 */
	Float queryIncomeAll(Map<String, Object> json);
	/**
	 * 是否登陆了
	 * @param json
	 */
	Map queryQianDao(Map<String, Object> json);
	/**
	 * 添加意见反馈
	 * @param json
	 */
	void insertUserSuggest(Map<String, Object> json);
	/**
	 *线下活动的轮播图
	 * @return
	 */
	List<Map> queryActivityBanner();
	/**
	 * 通过视频id查看视频信息
	 * @param json
	 * @return
	 */
	Map queryVideoById(Map<String, Object> json);
	/**
	 * 根据券号查询
	 * @param json
	 */
	Map queryCouponByNumber(Map<String, Object> json);
	/**
	 * 添加课程评论
	 * @param json
	 */
	void addCommentsForCourse(Map<String, Object> json);
	/**
	 * 发起的教程
	 * @param json
	 * @return
	 */
	List<Map> queryRequestVideo(Map<String, Object> json);
	/**
	 * 发起的教程中的投票
	 * @param json
	 */
	void addVote(Map<String, Object> json);
	/**
	 * 修改地区
	 * @param json
	 */
	void editUserDistrict(Map<String, Object> json);
	/**
	 * 修改用户的职业
	 * @param json
	 */
	void editUserJob(Map<String, Object> json);
	/**
	 * 查询职业
	 * @return
	 */
	List<Map> queryJobList();
	/**
	 * 通过电话查询用户
	 * @param phone
	 * @return
	 */
	User queryUserByPhone(String phone);

	/**
	 * 反馈意见分类列表
	 * @param json
	 * @return
	 */
	List<SuggestionType> suggestionList(Map<String, Object> json);
	
	/**
	 * 获取讲师收益信息
	 * @param teacherId
	 * @return
	 * @throws Exception 
	 */
	public Code getTeacherDetail(String teacherId) throws Exception;
	

}
