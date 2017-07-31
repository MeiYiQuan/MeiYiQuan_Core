package com.salon.backstage.qcproject.dao.support;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.qc.util.Condition;
import com.qc.util.Condition.Con;
import com.qc.util.Condition.Cs;
import com.qc.util.enums.E;
import com.salon.backstage.pub.bsc.domain.Constant;
import com.salon.backstage.qcproject.util.Sql;
import com.salon.backstage.qcproject.util.Statics;

/**
 * 作者：齐潮
 * 创建日期：2017年1月3日
 * 类说明：产生有关课程的sql
 */
public class CourseSupport {

	
	/**
	 * 获取即将上映列表，并携带该用户是否已经点过赞的操作
	 * @param userId
	 * @return
	 */
	public final static Sql getWillPlayings(String userId){
		String sql = "select `course`.`course_compaign_type` as `course_compaign_type`,"
							+ "`course`.`pic_big_url` as `pic_big_url`,"
							+ "`course`.`course_compaign_video_url` as `course_compaign_video_url`,"
							+ "`course`.`playing_time` as `playing_time`,"
							+ "ifnull(`teacher`.`level`,'') as `teacherLevel`,"
							+ "(select ifnull(count(`id`),0) from `tb_like` where `like_type_id` = `course`.`id` and `like_dislike` = " + Statics.LIKE_YES + ") as `likeCount`,"
							+ "(select ifnull(count(`id`),0) from `tb_like` where `like_type_id` = `course`.`id` and `like_dislike` = " + Statics.LIKE_NOT + ") as `dislikeCount`,"
							+ "`course`.`remark` as `remark`,"
							+ "(select ifnull(count(`id`),0) from `tb_comment` where `commed_id` = `course`.`id` and `status` = " + Constant.NO_INT + ") as `commentCount`,"
							+ "ifnull(`teacher`.`name`,'虚位以待') as `teacherName`,"
							+ "`course`.`id` as `id`,"
//							+ "ifnull(`subject`.`name`,'') as `title`,"
							+ "`course`.`title` as `title`,"
							+ " (select ifnull(sum(`click_count`),0) from `tb_statistics` where `type_id` = `course`.`id` ) as `playCount`,"
							/*+ "0 as `playCount`,"*/
							+ "`course`.`teacher_id` as `teacher_id`,"
							+ "`course`.`share_url` as `share_url`,"
							+ "case "
								+ "when `like`.`like_dislike` = " + Statics.LIKE_YES + " then 0 "
								+ "when `like`.`like_dislike` = " + Statics.LIKE_NOT + " then 1 "
								+ "when `like`.`like_dislike` is null then 2 "
								+ "else 2 "
							+ "end as `likeDislike` "
						+ "from `tb_course` as `course` "
						+ " left join tb_statistics as statistics on statistics.type_id=course.id  "
						+ " left join `tb_teacher` as `teacher` on `teacher`.`id` = `course`.`teacher_id` "
						+ "left join `tb_like` as `like` on `like`.`like_type_id` = `course`.`id` and `like`.`user_id` = '"+userId+"'"
						+ "left join `tb_subject` as `subject` on `subject`.`id` = `course`.`subject_id` and `subject`.`status` = " + Constant.YES_INT + " "
						+ "where `course`.`status` = " + Constant.YES_INT + " "
						+ "and `course`.`playing` = " + Statics.COURSE_PLAYING_WILLSHOW + " "
						+ "group by `course`.`id` "
						+ "order by `course`.`playing_time` ASC";
		Map<String,Object> params = new HashMap<String,Object>();
		/*params.put("userId", userId);*/
		Sql result = Sql.get(sql, params);
		return result;
	}
	
	/**
	 * 通过讲师id去获取该讲师所有的课程,type值为1表示用于讲师详情页，等于2表示讲师收益页
	 * @param teacherId
	 * @return
	 */
	public final static Sql getCoursesByTeacherId(String teacherId,String addCondition,int type){
		String select = null;
		switch(type){
		case 1:
			select = "select `course`.`id` as `id`,"
						+ "`course`.`pic_big_url` as `pic_big_url`,"
						+ "`course`.`title` as `title`,"
						+ "`course`.`description` as `description`,"
						+ "`course`.`cost` as `cost`,"
						+ "`course`.`playing_time` as `playing_time`,"
						+ "ifnull(sum(`statics`.`play_count` + `statics`.`play_expect_count`),0) as `playCount` ";
			break;
		case 2:
			select = "select `course`.`id` as `id`,"
						+ "`course`.`pic_big_url` as `video_pic_url`,"
						+ "`course`.`title` as `title`,"
						+ "`course`.`description` as `remark`,"
						+ "`course`.`course_compaign_type` as `course_compaign_type`,"
						+ "`course`.`course_compaign_video_url` as `video_save_url` ";
			break;
		}
		String sql = select
//							+ "(select count(DISTINCT `user_id`) from `tb_playrecord` where `course_id` = `course`.`id`) as `playCount` "
						+ "from `tb_course` as `course` "
						+ "left join `tb_video` as `video` on `video`.`course_id` = `course`.`id` "
						+ "left join `tb_statistics` as `statics` on `statics`.`type_id` = `video`.`id` "
						+ "where `course`.`teacher_id` = :teacherId "
//							+ "and `course`.`status` = " + Constant.YES_INT + " "
//							+ "and `course`.`playing` in (" + Statics.COURSE_PLAYING_SHOW + "," + Statics.COURSE_PLAYING_REQUESTTYPE + ") "
						+ addCondition
						+ "group by `course`.`id` "
						+ "order by `course`.`create_time` desc";
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("teacherId", teacherId);
		Sql result = Sql.get(sql, params);
		return result;
	}
	
	/**
	 * 获得课程的相关推荐列表
	 * @param courseId
	 * @return
	 */
	public final static Sql getCourseRecommends(String courseId){
		String sql = "select `course`.`id` as `id`,"
							+ "`course`.`pic_big_url` as `pic_big_url`,"
							+ "`course`.`title` as `title`,"
							+ "`course`.`title` as `description` "
						+ "from `tb_course` as `course`,`tb_course_recommend` as `recommend` "
						+ "where `course`.`id` = `recommend`.`recommend_course_id` "
							+ "and `course`.`status` = " + Constant.YES_INT + " "
							+ "and `recommend`.`course_id` = :courseId "
						+ "group by `recommend`.`id` "
						+ "order by `course`.`playing_time` desc";
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("courseId", courseId);
		Sql result = Sql.get(sql, params);
		return result;
	}
	
	/**
	 * 根据频道id获取其下所有的课程
	 * @param channelId
	 * @param type
	 * @return
	 */
	public final static Sql getCoursesByChannelId(String channelId,int type){
		String orderStr = null;
		switch(type){
		case 1:
			orderStr = "order by `course`.`playing_time` desc";
			break;
		case 2:
			orderStr = "order by sum(`statics`.`play_count` + `statics`.`play_expect_count`) desc";
			break;
		case 3:
			orderStr = "order by `course`.`cost` desc";
			break;
		}
		String and="`course`.`channel_id` like '%"+channelId+"%' ";
		String sql = "select `course`.`channel_id` as `channelId`,"
							+ "`course`.`id` as `courseID`,"
							+ "`course`.`course_compaign_type` as `courseCompaignType`,"
							+ "ifnull(`course`.`course_compaign_video_url`,'') as `courseVideo`,"
							+ "ifnull(`course`.`pic_big_url`,'') as `coursePic`,"
							+ "`course`.`title` as `courseTitle`,"
							+ "`course`.`description` as `courseDescrpt`,"
							+ "`course`.`share_url` as `shareUrl`,"
							+ "`course`.`teacher_id` as `teacherId`,"
							+ "ifnull(`teacher`.`name`,'未命名') as `teacherName`,"
							+ "ifnull(`teacher`.`level`,'未知') as `teacherLevel`,"
							+ "ifnull(sum(`statics`.`play_count` + `statics`.`play_expect_count`),0) as `coun` "
						+ "from `tb_course` as `course` "
						+ "LEFT JOIN `tb_teacher` as `teacher` on `teacher`.`teacher_id` = `course`.`teacher_id` "
						+ "LEFT JOIN `tb_video` as `video` on `video`.`course_id` = `course`.`id` "
						+ "LEFT JOIN `tb_statistics` as `statics` on `statics`.`type_id` = `video`.`id` and `statics`.`type` = " + Statics.STATICS_TYPE_SP + " "
						+ "where "
						/*+ " `course`.`channel_id` = :channelId "*/
						+ " "+and+""
						+ "and `course`.`status` = " + Constant.YES_INT + " "
						+ "and `course`.`playing` in (" + Statics.COURSE_PLAYING_REQUESTTYPE + "," + Statics.COURSE_PLAYING_SHOW + ") "
						+ "GROUP BY `course`.`id` "
						+ orderStr;
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("channelId", channelId);
		Sql result = Sql.get(sql, null);
		return result;
		
	}
	
	
	/**
	 * 通过videoId去获取相应的课程信息,加查一个视频标题
	 * @param videoId
	 * @return
	 */
	public final static Sql getCourseByVideoId(String videoId){
		String sql = "select "
						+ "`course`.*,`video`.`title` as `video_title`,`video`.`time_long` as `time_long` "
						+ "from `tb_course` as `course`,`tb_video` as `video` "
						+ "where `course`.`id` = `video`.`course_id` "
						+ "and `video`.`id` = :videoId "
						+ "group by `video`.`id`";
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("videoId", videoId);
		Sql result = Sql.get(sql, params);
		return result;
	}
	
	/**
	 * 获取一个课程中第一个视频的信息,可以直接获取该视频的播放量和是否已经收藏
	 * @param courseId
	 * @param userId
	 * @param videoId
	 * @return
	 */
	public final static Sql getCourseVideo(String courseId,String userId,int showTimeDefault,Object videoId){
		Cs cs = Condition.getConditions(Con.getCon("`video`.`id`", E.EQ, videoId));
		String sql = "select "
						+ "ifnull(`statics`.`play_count` + `statics`.`play_expect_count`,0) as `playrecordCount`,"
						+ "`video`.`id` as `id`,"
						+ "`video`.`title` as `title`,"
						+ "`video`.`per_cost` as `per_cost`,"
						+ "`course`.`remark` as `remark`,"
						+ "`video`.`create_time` as `play_time`,"
						+ "`video`.`share_url` as `share_url`,"
						+ "`video`.`time_long` as `time_long`,"
						+ "`video`.`free` as `free`,"
						+ "if(`video`.`free` = " + Constant.NO_INT + " and (`video`.`freeTime` is null or `video`.`freeTime` = 0)," + showTimeDefault + ",`video`.`freeTime`) as `freeTime`,"
						+ "`video`.`video_pic_url` as `video_pic_url`,"
						+ "if(`buy`.`id` is null," + Constant.NO_INT + "," + Constant.YES_INT + ") as `isBuy`,"
						+ "if(`collect`.`id` is null," + Constant.NO_INT + "," + Constant.YES_INT + ") as `collectStatus`,"
						+ "`video`.`video_save_url` as `video_save_url` "
						
						+ "from `tb_course` as `course` "
						+ "LEFT JOIN `tb_video` as `video` on `video`.`course_id` = `course`.`id` "
						+ "left join `tb_statistics` as `statics` on `statics`.`type_id` = `video`.`id` "
						+ "LEFT JOIN `tb_user_buyvideos` as `buy` on `buy`.`videoId` = `video`.`id` and `buy`.`userId` = :userId "
						+ "LEFT JOIN `tb_collect` as `collect` on `collect`.`collect_type_id` = `video`.`id` "
							+ "and `collect`.`collect_type` = " + Statics.COLLECT_TYPE_SP + " and `collect`.`user_id` = :userId "
//						+ "LEFT JOIN "
//							+ "(select `video_id`,count(`id`) as `count` from `tb_playrecord` group by `video_id`) as `play` "
//							+ "on `play`.`video_id` = `video`.`id` "
						+ "where `course`.`id` = :courseId "
							+ "and `video`.`id` is not null "
							+ "and `course`.`playing` in (" + Statics.COURSE_PLAYING_REQUESTTYPE + "," + Statics.COURSE_PLAYING_SHOW + ") "
							+ "and `course`.`status` = " + Constant.YES_INT + " "
							+ cs.getConditions()
						+ "GROUP BY `video`.`id` "
						+ "order by `video`.`order_num` desc ";
		
						/*
						+ "from `tb_course` as `course` "
						+ "LEFT JOIN `tb_video` as `video` on `video`.`id` = `course`.`video_id` "
						+ "LEFT JOIN `tb_collect` as `collect` on `collect`.`collect_type_id` = `video`.`id` "
							+ "and `collect`.`collect_type` = " + Statics.COLLECT_TYPE_SP + " and `collect`.`user_id` = :userId "
						+ "LEFT JOIN `tb_playrecord` as `play` on `play`.`video_id` = `video`.`id` "
						+ "where `course`.`id` = :courseId and `play`.`id` is not null "
						+ "GROUP BY `course`.`id`";
						*/
						
		Map<String,Object> params = cs.getParams();
		params.put("courseId", courseId);
		params.put("userId", userId);
		Sql result = Sql.get(sql, params);
		return result;
	}
	
	/**
	 * 获取一个课程下所有的视频
	 * @param courseId
	 * @return
	 */
	public final static Sql getCourseVideos(String courseId){
		String sql = "select `id`,`title`,`time_long` from `tb_video` where `course_id` = :courseId order by `order_num` asc";
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("courseId", courseId);
		Sql result = Sql.get(sql, params);
		return result;
	}
	
	/**
	 * 将传进来的信息进行整理，是为了与之前的接口返回的数据对应.
	 * 要保证courseVideo非空
	 * @param courseVideo
	 * @param courseVideos
	 * @return
	 */
	public final static Map<String,Object> cleanCourseVideos(Map<String, Object> courseVideo,List<Map<String, Object>> courseVideos){
		Map<String,Object> result = new HashMap<String,Object>();
		result.put("playrecordCount", courseVideo.get("playrecordCount"));
		courseVideo.remove("playrecordCount");
		result.put("videoMessage", courseVideo);
		int videoCount = courseVideos==null?0:courseVideos.size();
		result.put("videoCount", videoCount);
		result.put("videoList", (courseVideos==null?new ArrayList<Map<String,Object>>():courseVideos));
		return result;
	}
	
	
	/**
	 * 查询出正在热播三大榜单：热播榜，热销榜，热评榜
	 * @param type
	 * @return
	 */
	public final static Sql getCourses(int type){
		String leftAdd = null;
		switch(type){
			case Statics.COURSE_BEINGHIT_LOOKING:
				leftAdd = lookingSupport();
				break;
			case Statics.COURSE_BEINGHIT_BUY:
				leftAdd = buySupport();
				break;
			case Statics.COURSE_BEINGHIT_COMMENT:
				leftAdd = commentSupport();
				break;
		}
		String sql = "select "
						+ "`course`.`id` as `courseId`,"
						+ "`teacher`.`id` as `teacherId`,"
						+ "`course`.`title` as `title`,"
						+ "`course`.`remark` as `courseRemark`,"
						+ "`teacher`.`name` as `teacherName`,"
						+ "`teacher`.`level` as `teacherLevel`,"
						+ "ifnull(`otherTable`.`sum`,0) as `sum` "
					+ "from `tb_course` as `course` "
					+ "LEFT JOIN `tb_teacher` as `teacher` on `teacher`.`teacher_id` = `course`.`teacher_id` "
					+ leftAdd
					+ "where `course`.`status` = " + Constant.YES_INT + " "
							+ "and `course`.`to_home` = " + Constant.YES_INT + " "
							+ "and `course`.`playing` in (" + Statics.COURSE_PLAYING_REQUESTTYPE + "," + Statics.COURSE_PLAYING_SHOW + ") "
					+ "GROUP BY `course`.`id` "
					+ "ORDER BY `otherTable`.`sum` desc,`course`.`create_time` asc";
		Sql result = Sql.get(sql, null);
		return result;
	}
	
	/**
	 * 热播榜需要的表链接
	 * @return
	 */
	private final static String lookingSupport(){
		String sql = "LEFT JOIN "
						+ "("
						+ "select ifnull(`videoCount`.`id`,'') as `id`,sum(`videoCount`.`playCount`) as `sum` "
						+ "from "
						+ "("
						+ "	select `course`.`id` as `id`,`statics`.`play_count` as `playCount` "
						+ "	from `tb_course` as `course` "
						+ "	LEFT JOIN `tb_video` as `video` on `video`.`course_id` = `course`.`id` "
						+ "	LEFT JOIN `tb_statistics` as `statics` on `statics`.`type_id` = `video`.`id` "
						+ "	where `statics`.`type` = " + Statics.STATICS_TYPE_SP + " "
						+ "	GROUP BY `video`.`id`"
						+ ") as `videoCount` "
						+ "GROUP BY `videoCount`.`id`"
						+ ") as `otherTable` on `otherTable`.`id` = `course`.`id` ";
		return sql;
	}
	
	/**
	 * 热销榜需要的表链接
	 * @return
	 */
	private final static String buySupport(){
		String sql = "LEFT JOIN "
						+ "("
						+ "select ifnull(`orderSupport`.`id`,'') as `id`,count(`orderSupport`.`order`) as `sum` "
						+ "from "
						+ "("
						+ "	select `course`.`id` as `id`,`order`.`id` as `order` "
						+ "	from `tb_course` as `course` "
						+ "	LEFT JOIN `tb_video` as `video` on `video`.`course_id` = `course`.`id` "
						+ "	LEFT JOIN `tb_order` as `order` on `order`.`video_id` = `video`.`id` "
						+ "	where `order`.`status` = " + Constant.PAY_STATUS_PAYED + " "
						+ "	GROUP BY `order`.`id`"
						+ ") as `orderSupport` "
						+ "GROUP BY `orderSupport`.`id`"
						+ ") as `otherTable` on `otherTable`.`id` = `course`.`id` ";
		return sql;
	}
	
	/**
	 * 热评榜需要的表链接
	 * @return
	 */
	private final static String commentSupport(){
		String sql = "LEFT JOIN "
						+ "("
						+ "select ifnull(`commentSupport`.`id`,'') as `id`,count(`commentSupport`.`comment`) as `sum` "
						+ "from "
						+ "("
						+ "	select `course`.`id` as `id`,`comment`.`id` as `comment` "
						+ "	from `tb_course` as `course` "
						+ "	LEFT JOIN `tb_video` as `video` on `video`.`course_id` = `course`.`id` "
						+ "	LEFT JOIN `tb_comment` as `comment` on `comment`.`commed_id` = `video`.`id` "
						+ "	where `comment`.`id` is not null "
						+ "	GROUP BY `comment`.`id`"
						+ ") as `commentSupport` "
						+ "GROUP BY `commentSupport`.`id`"
						+ ") as `otherTable` on `otherTable`.`id` = `course`.`id` ";
		return sql;
	}
}
