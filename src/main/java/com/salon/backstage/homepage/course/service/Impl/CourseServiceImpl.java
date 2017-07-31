package com.salon.backstage.homepage.course.service.Impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.salon.backstage.common.util.ExtraSpringHibernateTemplate;
import com.salon.backstage.common.util.MobileMessageCondition;
import com.salon.backstage.common.util.StringUtil;
import com.salon.backstage.homepage.course.service.ICourseService;
import com.salon.backstage.homepage.statistics.service.IStatisticsService;
import com.salon.backstage.pub.bsc.dao.po.Statistics;
import com.salon.backstage.pub.bsc.dao.po.Teacher;
import com.salon.backstage.pub.bsc.domain.Constant;
import com.salon.backstage.qcproject.dao.ObjectDao;
import com.salon.backstage.qcproject.dao.support.CourseSupport;
import com.salon.backstage.qcproject.util.Code;
import com.salon.backstage.qcproject.util.Sql;
import com.salon.backstage.qcproject.util.Statics;

@Service
public class CourseServiceImpl implements ICourseService{
	
	@Autowired
	private ExtraSpringHibernateTemplate extraSpringHibernateTemplate;
	
	@Autowired
	private IStatisticsService is;
	
	@Autowired
	private ObjectDao od;
	
	@SuppressWarnings("all")
	@Override
	public List<Map> queryAllHomepage() {
		//状态为1启用的、已经上映的1、在首页显示的to_home(推荐)为1显示
		String sql = "select id,title,pic_small_url,description from tb_course where status = ? and playing = ? and to_home = ?";
		Object[] values = {1,1,1};
		List<Map> courseList = extraSpringHibernateTemplate.createSQLQueryFindAll(sql, values);
		return courseList;
	}
	
	@SuppressWarnings("all")
	@Override
	public List<Map> queryChannelCourseHomepage() {
		//状态为1启用的、已经上映的1、在首页显示的homepage_show(创业开店、潮流技术)为1显示 
		String sql = "select id,title,pic_small_url,description,pic_big_url,belong_type,show_big from tb_course where status = ? and playing = ? and homepage_show = ? and belong_type in(1,2)";
		Object[] values = {1,1,1};
		List<Map> channelCourseList = extraSpringHibernateTemplate.createSQLQueryFindAll(sql, values);
		return channelCourseList;
	}
	
	@SuppressWarnings("all")
	@Override
	public List<Map> queryCoursePlayingSoon(Map json) {
	//pageNo前台传参从1开始
	String pageNo=StringUtil.isNullOrBlank((String)json.get("pageNo")) ? "1":(String)json.get("pageNo");
	String pagesize=StringUtil.isNullOrBlank((String)json.get("pagesize")) ? "5":(String)json.get("pagesize");
	Integer pn=Integer.valueOf(pageNo);
	Integer ps=Integer.valueOf(pagesize);
	String userId = (String)json.get("userid");
		StringBuffer sql = new StringBuffer();
		//playing 0即将上映,status 1 可用, 首页即将上映页面宣传头类型course_compaign_type,description简介,remark详细介绍
		sql.append("SELECT");
		sql.append(" tc.course_compaign_type,");
		sql.append(" tc.course_compaign_video_url,");
		sql.append(" tc.pic_big_url,");
		sql.append(" tc.playing_time,");
		sql.append(" tt.`name` as teacherName, ");
	
		sql.append(" ts.like_count as likeCount,");
		sql.append(" ts.dislike_count as dislikeCount,");
		sql.append(" tc.remark,");
		sql.append(" ts.comment_count as commentCount,");
		sql.append(" tt.`level` as teacherLevel, ");
		sql.append(" tc.id,");
		sql.append(" tc.title,");
		sql.append(" ts.play_count as playCount,");
		sql.append(" tc.teacher_id,");
		sql.append(" tc.share_url ,");
		sql.append(" tc.playing ,");
		sql.append(" (CASE WHEN  tb_like.like_dislike IS NULL THEN 2 ELSE tb_like.like_dislike END) AS likeDislike");
	
		sql.append(" FROM");
		sql.append(" tb_course tc");
		sql.append(" LEFT JOIN tb_statistics ts ON ts.type_id=tc.id");
		sql.append(" LEFT JOIN tb_teacher tt ON tt.teacher_id= tc.teacher_id");
		sql.append(" LEFT JOIN tb_like  ON tb_like.like_type_id =tc.id AND tb_like.user_id='"+userId+"'");
		sql.append(" WHERE tc.playing=? AND tc.status=? ");
		sql.append(" GROUP BY tc.id ");
		sql.append(" ORDER BY tc.playing_time asc  ");
		Object[] values = {Statics.COURSE_PLAYING_WILLSHOW,Constant.YES_INT};
		List<Map> coursePlayingSoonList = extraSpringHibernateTemplate.createSQLQueryFindPaging(sql.toString(), (pn-1)*ps, ps, values);
		//List<Map> coursePlayingSoonList = extraSpringHibernateTemplate.createSQLQueryFindAll(sql.toString(),values);
		//List<Map> coursePlayingSoonDetailList = queryData(coursePlayingSoonList,userId);
		return coursePlayingSoonList;
	}
	
	@SuppressWarnings("all")
	public List<Map> queryData(List<Map> coursePlayingSoonList , String userId){
		Iterator<Map> iter = coursePlayingSoonList.iterator();
		List<Map> coursePlayingSoonDetailList = new ArrayList<Map>();
		while(iter.hasNext()){ 
			Map mapCourse = iter.next();
			String courseId = (String)mapCourse.get("id");
			String sql = "select play_count,comment_count,like_count,dislike_count,click_expect_count,collect_expect_count,comment_expect_count,dislike_expect_count,like_expect_count,play_expect_count,share_expect_count from tb_statistics where type = 1 and type_id = '"+courseId+"'";
			Map map = extraSpringHibernateTemplate.createSQLQueryFindFirstOne(sql);
			//实际值
			int play_count = Integer.valueOf(map.get("play_count").toString());
			int comment_count = Integer.valueOf(map.get("comment_count").toString());
			int like_count = Integer.valueOf(map.get("like_count").toString());
			int dislike_count = Integer.valueOf(map.get("dislike_count").toString());
			//虚拟量
			int play_expect_count = Integer.valueOf(map.get("play_expect_count").toString());
			int comment_expect_count = Integer.valueOf(map.get("comment_expect_count").toString());
			int like_expect_count = Integer.valueOf(map.get("like_expect_count").toString());
			int dislike_expect_count = Integer.valueOf(map.get("dislike_expect_count").toString());
			
			mapCourse.put("playCount", play_count>=play_expect_count?play_count:play_expect_count);
			mapCourse.put("commentCount", comment_count>=comment_expect_count?comment_count:comment_expect_count);
			mapCourse.put("likeCount", like_count>=like_expect_count?like_count:like_expect_count);
			mapCourse.put("dislikeCount", dislike_count>=dislike_expect_count?dislike_count:dislike_expect_count);
            //讲师信息
            Teacher teacher = extraSpringHibernateTemplate.findFirstOneByPropEq(Teacher.class, "teacher_id", (String)mapCourse.get("teacher_id"));
            mapCourse.put("teacherName", teacher.getName());
            mapCourse.put("teacherLevel", teacher.getLevel());
            coursePlayingSoonDetailList.add(mapCourse);
            //判断点赞类型
            String sqlLikeDislike = "select like_dislike from tb_like where like_type = 0 and like_type_id = '"+courseId+"' and user_id = '"+userId+"'";
            List<Map> listMap = extraSpringHibernateTemplate.createSQLQueryFindAll(sqlLikeDislike);
            int likeDislike = 2;
            if(listMap != null && !"[]".equals(listMap.toString())){
            	likeDislike = Integer.valueOf(listMap.get(0).get("like_dislike").toString());
            }
            mapCourse.put("likeDislike", likeDislike); //0 赞,1 倒赞,2没有操作过
        }  
		return coursePlayingSoonDetailList;
	}
	
	@SuppressWarnings("all")
	@Override
	public List<Map> queryPlayRank(Map json) {
		//pageNo前台传参从1开始
		boolean pageNoNull = StringUtil.isNullOrBlank(json.get("pageNo"));
		boolean pagesizeNull = StringUtil.isNullOrBlank(json.get("pagesize"));
		StringBuffer sql = new StringBuffer();
		sql.append("select course_id,count(1) as coun from tb_playrecord GROUP BY course_id ");
		List<Map> queryAllCourse = extraSpringHibernateTemplate.createSQLQueryFindAll(sql.toString());
		
		sql.append(" ORDER BY coun DESC LIMIT ");
		if(pageNoNull && pagesizeNull){
			sql.append(Constant.RANK_PAGENO+","+Constant.RANK_PAGESIZE);
		}else if(pageNoNull){
			sql.append(Constant.RANK_PAGENO+","+Integer.valueOf((String)json.get("pagesize")));
		}else if(pagesizeNull){
			int pageNo = Integer.valueOf((String)json.get("pageNo"));
			if((pageNo-1)*Constant.RANK_PAGESIZE > queryAllCourse.size()){
				int max = queryAllCourse.size()/Constant.RANK_PAGESIZE;
				int start = max*Constant.RANK_PAGESIZE;
				sql.append(start+","+Constant.RANK_PAGESIZE);
			}else if(pageNo >= 1){
				int start = (pageNo-1) * Constant.RANK_PAGESIZE;
				sql.append(start+","+Constant.RANK_PAGESIZE);
			}else{
				sql.append(Constant.RANK_PAGENO+","+Constant.RANK_PAGESIZE);
			}
		}else{
			int pageNo = Integer.valueOf((String)json.get("pageNo"));
			int pagesize = Integer.valueOf((String)json.get("pagesize"));
			if((pageNo-1)*pagesize > queryAllCourse.size()){
				int max = queryAllCourse.size()/pagesize;
				int start = max*pagesize;
				sql.append(start+","+pagesize);
			}else if(pageNo >= 1){
				int start = (pageNo-1) * pagesize;
				int end = pageNo * pagesize;
				sql.append(start+","+end);
			}else{
				sql.append(Constant.RANK_PAGENO+","+pagesize);
			}
		} 
		List<Map> playRankList = extraSpringHibernateTemplate.createSQLQueryFindAll(sql.toString());
		List<Map> courseRankList = selectCourse(playRankList);
		return courseRankList;
	}

	@SuppressWarnings("all")
	@Override
	public List<Map> queryCommentRank(Map json) {
		//pageNo前台传参从1开始
		boolean pageNoNull = StringUtil.isNullOrBlank(json.get("pageNo"));
		boolean pagesizeNull = StringUtil.isNullOrBlank(json.get("pagesize"));
		StringBuffer sql = new StringBuffer();
		sql.append("select commed_id,count(1) as coun from tb_comment WHERE commed_type = 0 GROUP BY commed_id ");
		List<Map> queryAllCourse = extraSpringHibernateTemplate.createSQLQueryFindAll(sql.toString());
		
		sql.append(" ORDER BY coun DESC LIMIT ");
		if(pageNoNull && pagesizeNull){
			sql.append(Constant.RANK_PAGENO+","+Constant.RANK_PAGESIZE);
		}else if(pageNoNull){
			sql.append(Constant.RANK_PAGENO+","+Integer.valueOf((String)json.get("pagesize")));
		}else if(pagesizeNull){
			int pageNo = Integer.valueOf((String)json.get("pageNo"));
			if((pageNo-1)*Constant.RANK_PAGESIZE > queryAllCourse.size()){
				int max = queryAllCourse.size()/Constant.RANK_PAGESIZE;
				int start = max*Constant.RANK_PAGESIZE;
				sql.append(start+","+Constant.RANK_PAGESIZE);
			}else if(pageNo >= 1){
				int start = (pageNo-1) * Constant.RANK_PAGESIZE;
				sql.append(start+","+Constant.RANK_PAGESIZE);
			}else{
				sql.append(Constant.RANK_PAGENO+","+Constant.RANK_PAGESIZE);
			}
		}else{
			int pageNo = Integer.valueOf((String)json.get("pageNo"));
			int pagesize = Integer.valueOf((String)json.get("pagesize"));
			if((pageNo-1)*pagesize > queryAllCourse.size()){
				int max = queryAllCourse.size()/pagesize;
				int start = max*pagesize;
				sql.append(start+","+pagesize);
			}else if(pageNo >= 1){
				int start = (pageNo-1) * pagesize;
				int end = pageNo * pagesize;
				sql.append(start+","+end);
			}else{
				sql.append(Constant.RANK_PAGENO+","+pagesize);
			}
		} 
		List<Map> commentRankList = extraSpringHibernateTemplate.createSQLQueryFindAll(sql.toString());
		
		Iterator<Map> iter = commentRankList.iterator();
		List<Map> courseRankList = new ArrayList();
		while(iter.hasNext()){  
            Map mapCourse = iter.next();
            
            String sqlCourse = "select id,remark,teacher_id from tb_course where id = ?";
            Object[] valuesCourse = {mapCourse.get("commed_id")};
            Map courseMessage = extraSpringHibernateTemplate.createSQLQueryFindFirstOne(sqlCourse, valuesCourse);
            
            String sqlTeacher = "select id,name,level from tb_teacher where id = ?";
            Object[] valuesTeacher = {courseMessage.get("teacher_id")};
            Map teacherMessage = extraSpringHibernateTemplate.createSQLQueryFindFirstOne(sqlTeacher, valuesTeacher);
            
            Map rankCourseMap = new HashMap();
            rankCourseMap.put("courseId", courseMessage.get("id"));
            rankCourseMap.put("teacherId", teacherMessage.get("id"));
            rankCourseMap.put("teacherName", teacherMessage.get("name"));
            rankCourseMap.put("teacherLevel", teacherMessage.get("level"));
            rankCourseMap.put("courseRemark", courseMessage.get("remark"));
            
            courseRankList.add(rankCourseMap);
		}  
		return courseRankList;
	}

	@SuppressWarnings({ "all" })
	@Override
	public List<Map> queryOrderRank(Map json) {
		//pageNo前台传参从1开始
		boolean pageNoNull = StringUtil.isNullOrBlank(json.get("pageNo"));
		boolean pagesizeNull = StringUtil.isNullOrBlank(json.get("pagesize"));
		StringBuffer sql = new StringBuffer();
		sql.append("select course_id,count(1) as coun from tb_order GROUP BY course_id ");
		List<Map> queryAllCourse = extraSpringHibernateTemplate.createSQLQueryFindAll(sql.toString());
		
		sql.append(" ORDER BY coun DESC LIMIT ");
		if(pageNoNull && pagesizeNull){
			sql.append(Constant.RANK_PAGENO+","+Constant.RANK_PAGESIZE);
		}else if(pageNoNull){
			sql.append(Constant.RANK_PAGENO+","+Integer.valueOf((String)json.get("pagesize")));
		}else if(pagesizeNull){
			int pageNo = Integer.valueOf((String)json.get("pageNo"));
			if((pageNo-1)*Constant.RANK_PAGESIZE > queryAllCourse.size()){
				int max = queryAllCourse.size()/Constant.RANK_PAGESIZE;
				int start = max*Constant.RANK_PAGESIZE;
				sql.append(start+","+Constant.RANK_PAGESIZE);
			}else if(pageNo >= 1){
				int start = (pageNo-1) * Constant.RANK_PAGESIZE;
				sql.append(start+","+Constant.RANK_PAGESIZE);
			}else{
				sql.append(Constant.RANK_PAGENO+","+Constant.RANK_PAGESIZE);
			}
		}else{
			int pageNo = Integer.valueOf((String)json.get("pageNo"));
			int pagesize = Integer.valueOf((String)json.get("pagesize"));
			if((pageNo-1)*pagesize > queryAllCourse.size()){
				int max = queryAllCourse.size()/pagesize;
				int start = max*pagesize;
				sql.append(start+","+pagesize);
			}else if(pageNo >= 1){
				int start = (pageNo-1) * pagesize;
				int end = pageNo * pagesize;
				sql.append(start+","+end);
			}else{
				sql.append(Constant.RANK_PAGENO+","+pagesize);
			}
		} 
		List<Map> orderRankList = extraSpringHibernateTemplate.createSQLQueryFindAll(sql.toString());
		List<Map> courseRankList = selectCourse(orderRankList);
		return courseRankList;
	}
	
	
	@SuppressWarnings({ "all" })
	public List<Map> selectCourse(List<Map> rankList){
		Iterator<Map> iter = rankList.iterator();
		List<Map> courseRankList = new ArrayList();
		while(iter.hasNext()){  
            Map mapCourse = iter.next();
            
            String sqlCourse = "select id,remark,teacher_id from tb_course where id = ?";
            Object[] valuesCourse = {mapCourse.get("course_id")};
            Map courseMessage = extraSpringHibernateTemplate.createSQLQueryFindFirstOne(sqlCourse, valuesCourse);
            
            String sqlTeacher = "select teacher_id id,name,level from tb_teacher where teacher_id = ?";
            Object[] valuesTeacher = {courseMessage.get("teacher_id")};
            Map teacherMessage = extraSpringHibernateTemplate.createSQLQueryFindFirstOne(sqlTeacher, valuesTeacher);
            
            Map rankCourseMap = new HashMap();
            rankCourseMap.put("courseId", courseMessage.get("id"));
            rankCourseMap.put("teacherId", teacherMessage.get("id"));
            rankCourseMap.put("teacherName", teacherMessage.get("name"));
            rankCourseMap.put("teacherLevel", teacherMessage.get("level"));
            rankCourseMap.put("courseRemark", courseMessage.get("remark"));
            
            courseRankList.add(rankCourseMap);
		}  
		return courseRankList;
	}


	@SuppressWarnings("all")
	@Override
	public List<Map> queryByTeacherId(String teacherId) {
		Sql sql = CourseSupport.getCoursesByTeacherId(teacherId,"and `course`.`status` = " + Constant.YES_INT + " and `course`.`playing` in (" + Statics.COURSE_PLAYING_SHOW + "," + Statics.COURSE_PLAYING_REQUESTTYPE + ") ",1);
		List<Map<String, Object>> list = od.getListBySql(sql.getSql(), sql.getParams(), null, null);
		
		
		/*
		String sql = "select id,pic_big_url,title,description,cost,playing_time from tb_course where playing = 1 and status = 1 and teacher_id = '"+teacherId+"' ORDER BY playing_time DESC";
		List<Map> teacherDetailCourseList = extraSpringHibernateTemplate.createSQLQueryFindAll(sql);
		Iterator<Map> teacherDetailCourseListIte = teacherDetailCourseList.iterator();
		while(teacherDetailCourseListIte.hasNext()){
			Map teacherDetailCourseListMap = teacherDetailCourseListIte.next();
			String sqlPlay = "select count(1) from tb_playrecord where course_id = " +teacherDetailCourseListMap.get("id");
			Long playCount = extraSpringHibernateTemplate.createSQLQueryCount(sqlPlay);
			teacherDetailCourseListMap.put("playCount", playCount);
		}
		*/
		
		return (list==null?new ArrayList():list);
	}

	@SuppressWarnings("all")
	@Override
	public Map queryByCourseId(String courseId) {
		Map totalMap = new HashMap();
		//根据课程ID去对应视频表中order_num=0的视频,查出其基本信息
		String videoSql = "select id,title,remark,share_url,video_save_url,video_pic_url,time_long,play_time,per_cost from tb_video where free = 0 and order_num = 0 and course_id = '"+courseId+"'";
		Map oneMap = extraSpringHibernateTemplate.createSQLQueryFindFirstOne(videoSql);
		
		//根据oneMap中的视频ID去播放记录表中查询改视频的播放次数
		String playrecordSql = "select count(1) coun from tb_playrecord where video_id = '" + (String)oneMap.get("id")+"'";
		List<Map> playrecordList = extraSpringHibernateTemplate.createSQLQueryFindAll(playrecordSql);
		Object playrecordCount = playrecordList.get(0).get("coun");
		
		totalMap.put("videoMessage", oneMap);
		totalMap.put("playrecordCount", playrecordCount);
		return totalMap;
	}
	
	@SuppressWarnings("all")
	@Override
	public List<Map> queryBySubject(Map json) {
		String subjectId = (String)json.get("subjectId");
		//pageNo前台传参从1开始
		boolean pageNoNull = StringUtil.isNullOrBlank(json.get("pageNo"));
		boolean pagesizeNull = StringUtil.isNullOrBlank(json.get("pagesize"));
		// playing1正在上映   status1启用 
		StringBuilder sql = new StringBuilder();
		sql.append("select ");
		sql.append(" c.id courseId,c.course_compaign_type courseType,c.pic_big_url coursePic,c.course_compaign_video_url courseVideo,c.title courseTitle,c.description courseDescri,t.id teacherId,t.name teacherName,t.level teacherLevel ");
		sql.append(" from tb_course c ");
		sql.append(" join tb_teacher t on c.teacher_id = t.teacher_id ");
		sql.append(" where c.playing in (" + Statics.COURSE_PLAYING_REQUESTTYPE + "," + Statics.COURSE_PLAYING_SHOW + ") and c.status = " + Constant.YES_INT + " and c.subject_id = '"+subjectId+"'");
		List<Map> courseList = extraSpringHibernateTemplate.createSQLQueryFindAll(sql.toString());
		
		sql.append(" ORDER BY c.playing_time DESC LIMIT ");
		if(pageNoNull && pagesizeNull){
			sql.append(Constant.SUBJECT_SOMEONE_PAGENO+","+Constant.SUBJECT_SOMEONE_PAGESIZE);
		}else if(pageNoNull){
			sql.append(Constant.SUBJECT_SOMEONE_PAGENO+","+Integer.valueOf((String)json.get("pagesize")));
		}else if(pagesizeNull){
			int pageNo = Integer.valueOf((String)json.get("pageNo"));
			if((pageNo-1)*Constant.SUBJECT_SOMEONE_PAGESIZE > courseList.size()){
				int max = courseList.size()/Constant.SUBJECT_SOMEONE_PAGESIZE;
				int start = max*Constant.SUBJECT_SOMEONE_PAGESIZE;
				sql.append(start+","+Constant.SUBJECT_SOMEONE_PAGESIZE);
			}else if(pageNo >= 1){
				int start = (pageNo-1) * Constant.SUBJECT_SOMEONE_PAGESIZE;
				sql.append(start+","+Constant.SUBJECT_SOMEONE_PAGESIZE);
			}else{
				sql.append(Constant.SUBJECT_SOMEONE_PAGENO+","+Constant.SUBJECT_SOMEONE_PAGESIZE);
			}
		}else{
			int pageNo = Integer.valueOf((String)json.get("pageNo"));
			int pagesize = Integer.valueOf((String)json.get("pagesize"));
			if((pageNo-1)*pagesize > courseList.size()){
				int max = courseList.size()/pagesize;
				int start = max*pagesize;
				sql.append(start+","+pagesize);
			}else if(pageNo >= 1){
				int start = (pageNo-1) * pagesize;
				int end = pageNo * pagesize;
				sql.append(start+","+end);
			}else{
				sql.append(Constant.SUBJECT_SOMEONE_PAGENO+","+pagesize);
			}
		} 
		List<Map> coursePageList = extraSpringHibernateTemplate.createSQLQueryFindAll(sql.toString());
		return coursePageList;
	}

	@SuppressWarnings("all")
	@Override
	public List<Map> channelTimeRank(Map<String, Object> json) {
		//pageNo前台传参从1开始
		boolean pageNoNull = StringUtil.isNullOrBlank(json.get("pageNo"));
		boolean pagesizeNull = StringUtil.isNullOrBlank(json.get("pagesize"));
		String channelId = (String)json.get("channelId");
		StringBuilder sql = new StringBuilder();
		sql.append(" select c.id courseID,c.channel_id channelId,c.course_compaign_type courseCompaignType,c.pic_big_url coursePic,c.share_url shareUrl, ");
		sql.append(" c.course_compaign_video_url courseVideo,c.description courseDescrpt,c.title courseTitle,c.teacher_id teacherId,t.level teacherLevel,t.name teacherName  ");
		sql.append(" from tb_course c ");
		sql.append(" join tb_teacher t ");
		sql.append(" on t.teacher_id = c.teacher_id ");
		sql.append(" where c.playing = 1 and c.status = 1 and t.status = 1 ");
		sql.append(" order by c.playing_time desc ");
		List<Map> courseAll = extraSpringHibernateTemplate.createSQLQueryFindAll(sql.toString());
		
		sql.append(" LIMIT ");
		if(pageNoNull && pagesizeNull){
			sql.append(Constant.CHANNEL_COURSE_PAGENO+","+Constant.CHANNEL_COURSE_PAGESIZE);
		}else if(pageNoNull){
			sql.append(Constant.CHANNEL_COURSE_PAGENO+","+Integer.valueOf((String)json.get("pagesize")));
		}else if(pagesizeNull){
			int pageNo = Integer.valueOf((String)json.get("pageNo"));
			if((pageNo-1)*Constant.CHANNEL_COURSE_PAGESIZE > courseAll.size()){
				int max = courseAll.size()/Constant.CHANNEL_COURSE_PAGESIZE;
				int start = max*Constant.CHANNEL_COURSE_PAGESIZE;
				sql.append(start+","+Constant.CHANNEL_COURSE_PAGESIZE);
			}else if(pageNo >= 1){
				int start = (pageNo-1) * Constant.CHANNEL_COURSE_PAGESIZE;
				sql.append(start+","+Constant.CHANNEL_COURSE_PAGESIZE);
			}else{
				sql.append(Constant.CHANNEL_COURSE_PAGENO+","+Constant.CHANNEL_COURSE_PAGESIZE);
			}
		}else{
			int pageNo = Integer.valueOf((String)json.get("pageNo"));
			int pagesize = Integer.valueOf((String)json.get("pagesize"));
			if((pageNo-1)*pagesize > courseAll.size()){
				int max = courseAll.size()/pagesize;
				int start = max*pagesize;
				sql.append(start+","+pagesize);
			}else if(pageNo >= 1){
				int start = (pageNo-1) * pagesize;
				int end = pageNo * pagesize;
				sql.append(start+","+end);
			}else{
				sql.append(Constant.CHANNEL_COURSE_PAGENO+","+pagesize);
			}
		} 
		List<Map> courseList = extraSpringHibernateTemplate.createSQLQueryFindAll(sql.toString());
		Iterator<Map> ite = courseList.iterator();
		courseList.size();
		while(ite.hasNext()){
			Map courseMap = ite.next();
			courseMap.put("coun", playCount((String)courseMap.get("courseID")));
		}
		return courseList;
	}
	
	/**
	 * 查询课程的播放次数
	 */
	@SuppressWarnings("all")
	public Object playCount(String courseId){
		String sql = "select play_count,comment_count,like_count,dislike_count,click_expect_count,collect_expect_count,comment_expect_count,dislike_expect_count,like_expect_count,play_expect_count,share_expect_count from tb_statistics where type = 1 and type_id = '"+courseId+"'";
		Map map = extraSpringHibernateTemplate.createSQLQueryFindFirstOne(sql);
		if(map==null||map.size()<1)
			return 0;
		// 实际值
		int play_count = Integer.valueOf(map.get("play_count").toString());
		// 虚拟量
		int play_expect_count = Integer.valueOf(map.get("play_expect_count").toString());
		// 返回的数据
		int coun = play_count>=play_expect_count?play_count:play_expect_count;
		return coun;
	}
	
	@SuppressWarnings("all")
	@Override
	public List<Map> channelPlayRank(Map<String, Object> json) {
		//pageNo前台传参从1开始
		boolean pageNoNull = StringUtil.isNullOrBlank(json.get("pageNo"));
		boolean pagesizeNull = StringUtil.isNullOrBlank(json.get("pagesize"));
		String channelId = (String)json.get("channelId");
		StringBuilder sql = new StringBuilder();
		sql.append(" select c.id courseID,c.channel_id channelId,c.course_compaign_type courseCompaignType, ");
		sql.append(" c.pic_big_url coursePic,c.course_compaign_video_url courseVideo,c.description courseDescrpt, ");
		sql.append(" c.title courseTitle,c.teacher_id teacherId,c.share_url shareUrl,t.level teacherLevel,t.name teacherName ");
		sql.append(" from tb_course c ");
		sql.append(" join tb_teacher t on t.teacher_id = c.teacher_id ");
		sql.append(" join (select course_id,count(1) coun from tb_playrecord group by course_id order by course_id desc) pr ");
		sql.append(" on pr.course_id = c.id ");
		sql.append(" where c.playing = 1 and c.status = 1 and t.status = 1 ");
		sql.append(" order by pr.coun desc ");
		List<Map> courseAll = extraSpringHibernateTemplate.createSQLQueryFindAll(sql.toString());
		
		sql.append(" LIMIT ");
		if(pageNoNull && pagesizeNull){
			sql.append(Constant.CHANNEL_COURSE_PAGENO+","+Constant.CHANNEL_COURSE_PAGESIZE);
		}else if(pageNoNull){
			sql.append(Constant.CHANNEL_COURSE_PAGENO+","+Integer.valueOf((String)json.get("pagesize")));
		}else if(pagesizeNull){
			int pageNo = Integer.valueOf((String)json.get("pageNo"));
			if((pageNo-1)*Constant.CHANNEL_COURSE_PAGESIZE > courseAll.size()){
				int max = courseAll.size()/Constant.CHANNEL_COURSE_PAGESIZE;
				int start = max*Constant.CHANNEL_COURSE_PAGESIZE;
				sql.append(start+","+Constant.CHANNEL_COURSE_PAGESIZE);
			}else if(pageNo >= 1){
				int start = (pageNo-1) * Constant.CHANNEL_COURSE_PAGESIZE;
				sql.append(start+","+Constant.CHANNEL_COURSE_PAGESIZE);
			}else{
				sql.append(Constant.CHANNEL_COURSE_PAGENO+","+Constant.CHANNEL_COURSE_PAGESIZE);
			}
		}else{
			int pageNo = Integer.valueOf((String)json.get("pageNo"));
			int pagesize = Integer.valueOf((String)json.get("pagesize"));
			if((pageNo-1)*pagesize > courseAll.size()){
				int max = courseAll.size()/pagesize;
				int start = max*pagesize;
				sql.append(start+","+pagesize);
			}else if(pageNo >= 1){
				int start = (pageNo-1) * pagesize;
				int end = pageNo * pagesize;
				sql.append(start+","+end);
			}else{
				sql.append(Constant.CHANNEL_COURSE_PAGENO+","+pagesize);
			}
		}
		List<Map> courseList = extraSpringHibernateTemplate.createSQLQueryFindAll(sql.toString());
		Iterator<Map> ite = courseList.iterator();
		while(ite.hasNext()){
			Map courseMap = ite.next();
			courseMap.put("coun", playCount((String)courseMap.get("courseID")));
		}
		return courseList;
	}
	
	@SuppressWarnings("all")
	@Override
	public List<Map> channelPriceRank(Map<String, Object> json) {
		//pageNo前台传参从1开始
		boolean pageNoNull = StringUtil.isNullOrBlank(json.get("pageNo"));
		boolean pagesizeNull = StringUtil.isNullOrBlank(json.get("pagesize"));
		String channelId = (String)json.get("channelId");
		StringBuilder sql = new StringBuilder();
		sql.append(" select c.id courseID,c.channel_id channelId,c.course_compaign_type courseCompaignType,c.cost cost, ");
		sql.append(" c.pic_big_url coursePic,c.course_compaign_video_url courseVideo,c.description courseDescrpt, ");
		sql.append(" c.title courseTitle,c.teacher_id teacherId,c.share_url shareUrl,t.level teacherLevel,t.name teacherName ");
		sql.append(" from tb_course c ");
		sql.append(" join tb_teacher t on t.teacher_id = c.teacher_id ");
		sql.append(" where c.playing = 1 and c.status = 1 and t.status = 1  ");
		sql.append(" order by c.cost desc ");
		List<Map> courseAll = extraSpringHibernateTemplate.createSQLQueryFindAll(sql.toString());
		
		sql.append(" LIMIT ");
		if(pageNoNull && pagesizeNull){
			sql.append(Constant.CHANNEL_COURSE_PAGENO+","+Constant.CHANNEL_COURSE_PAGESIZE);
		}else if(pageNoNull){
			sql.append(Constant.CHANNEL_COURSE_PAGENO+","+Integer.valueOf((String)json.get("pagesize")));
		}else if(pagesizeNull){
			int pageNo = Integer.valueOf((String)json.get("pageNo"));
			if((pageNo-1)*Constant.CHANNEL_COURSE_PAGESIZE > courseAll.size()){
				int max = courseAll.size()/Constant.CHANNEL_COURSE_PAGESIZE;
				int start = max*Constant.CHANNEL_COURSE_PAGESIZE;
				sql.append(start+","+Constant.CHANNEL_COURSE_PAGESIZE);
			}else if(pageNo >= 1){
				int start = (pageNo-1) * Constant.CHANNEL_COURSE_PAGESIZE;
				sql.append(start+","+Constant.CHANNEL_COURSE_PAGESIZE);
			}else{
				sql.append(Constant.CHANNEL_COURSE_PAGENO+","+Constant.CHANNEL_COURSE_PAGESIZE);
			}
		}else{
			int pageNo = Integer.valueOf((String)json.get("pageNo"));
			int pagesize = Integer.valueOf((String)json.get("pagesize"));
			if((pageNo-1)*pagesize > courseAll.size()){
				int max = courseAll.size()/pagesize;
				int start = max*pagesize;
				sql.append(start+","+pagesize);
			}else if(pageNo >= 1){
				int start = (pageNo-1) * pagesize;
				int end = pageNo * pagesize;
				sql.append(start+","+end);
			}else{
				sql.append(Constant.CHANNEL_COURSE_PAGENO+","+pagesize);
			}
		}
		List<Map> courseList = extraSpringHibernateTemplate.createSQLQueryFindAll(sql.toString());
		for(Iterator<Map> ite = courseList.iterator();ite.hasNext();){
			Map courseMap = ite.next();
			courseMap.put("coun", playCount((String)courseMap.get("courseID")));
		}
		return courseList;
	}

	@Override
	public Code getChannelCourses(int page, int size, int type, String channelId) {
		int start = (page-1)*size;
		Sql sql = CourseSupport.getCoursesByChannelId(channelId, type);
		List<Map<String, Object>> list = od.getListBySql(sql.getSql(), sql.getParams(), start, size);
		return Code.init(true, 0, "", (list==null?new ArrayList<Map<String, Object>>():list));
	}

	@Override
	public Code getCoursesForWillPlaying(String userId, int page, int size) {
		int start = (page-1)*size;
		Sql sql = CourseSupport.getWillPlayings(userId);
		List<Map<String, Object>> list = od.getListBySql(sql.getSql(), sql.getParams(), start, size);
		return Code.init(true, 0, "", (list==null?new ArrayList<Map<String, Object>>():list));
	}

	/**
	 * 添加即将上映次数
	 */
	@Override
	public Object playingSoonCount(String id) {
		Map<String, Object> conditions=new HashMap<>();
		Map<String, Object> settings=new HashMap<>();
		Map<String, Object> params=new HashMap<>();
		Map<String, Object> paramss=new HashMap<>();
		paramss.put("type_id", id);
		int ss=  od.getPosCount(Statistics.class, paramss);
		if(ss<0){
			od.save(Statistics.class, paramss);
		}
		
		String sql="select s.* from tb_statistics s where s.type_id='"+id+"'";
		Map<String, Object> map=od.getObjectBySql(sql, params);
		String sid=map.get("id").toString();
		String click_count=map.get("click_count").toString();
		int c= Integer.parseInt(click_count);
		int b=c+1;
		String s = String.valueOf(b);
		settings.put("click_count", s);
		
		int i=od.updateById(Statistics.class, sid, settings);
		JSONObject json=new JSONObject();
		if(i<0){
			json.put("content", "失败");
		}
		json.put("content", "成功");
		
		return json;
	}
}















