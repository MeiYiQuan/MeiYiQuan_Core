package com.salon.backstage.homepage.course.service;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.salon.backstage.qcproject.util.Code;

/**
 * 课程表接口
 *
 */
@SuppressWarnings("all")
public interface ICourseService {

	/**
	 * 首页推荐课程
	 */
	List<Map> queryAllHomepage();

	/**
	 * 首页频道(创业开店 潮流技术)对应的课程 
	 */
	List<Map> queryChannelCourseHomepage();

	/**
	 * 即将上映分页
	 */
	List<Map> queryCoursePlayingSoon(Map json);
	
	/**
	 * 热评榜
	 */
	List<Map> queryPlayRank(Map json);

	/**
	 * 热评榜
	 */
	List<Map> queryCommentRank(Map json);

	/**
	 * 热销榜
	 */
	List<Map> queryOrderRank(Map json);
	
	/**
	 * 根据讲师Id返回讲师详情页需要的Course集合
	 */
	List<Map> queryByTeacherId(String teacherId);
	
	/**
	 * 课程详情页:根据课程ID查询课程信息	
	 */
	Map queryByCourseId(String courseId);
	
	/**
	 * 首页-精彩专题-其中一个专题信息中根据专题ID查询专题对应的课程 
	 */
	List<Map> queryBySubject(Map map);

	/**
	 * 频道-免费视频-最新
	 */
	List<Map> channelTimeRank(Map<String, Object> json);

	/**
	 * 频道-免费视频-最热
	 */
	List<Map> channelPlayRank(Map<String, Object> json);

	/**
	 * 频道-免费视频-价格
	 */
	List<Map> channelPriceRank(Map<String, Object> json);
	
	/**
	 * 分页获取某频道下所有的课程
	 * @param page
	 * @param size
	 * @param type
	 * @param channelId
	 * @return
	 */
	public Code getChannelCourses(int page,int size,int type,String channelId);
	
	/**
	 * 分页获取即将上映列表
	 * @param userId
	 * @param page
	 * @param size
	 * @return
	 */
	public Code getCoursesForWillPlaying(String userId,int page,int size);
	/**
	 * 添加即将上映播放次数
	 * @param id
	 * @return
	 */
	Object playingSoonCount(String id);
	
}






