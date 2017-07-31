package com.salon.backstage.homepage.teacher.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.salon.backstage.common.util.ExtraSpringHibernateTemplate;
import com.salon.backstage.common.util.Paging;
import com.salon.backstage.common.util.StringUtil;
import com.salon.backstage.homepage.teacher.service.ITeacherService;
import com.salon.backstage.pub.bsc.domain.Constant;
import com.salon.backstage.qcproject.dao.ObjectDao;
import com.salon.backstage.qcproject.dao.support.CourseSupport;
import com.salon.backstage.qcproject.util.Sql;
import com.salon.backstage.qcproject.util.Statics;
import com.salon.backstage.qcproject.util.Enums.RequestStatus;

@Service
public class TeacherServiceImpl implements ITeacherService {
	@Autowired
	private ExtraSpringHibernateTemplate extraSpringHibernateTemplate;
	
	@Autowired
	private ObjectDao od;
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public List<Map> queryAllHomepage() {
		String sql = "select teacher_id id,name,level,head_url from tb_teacher where status = ? and put_home = ?";
		Object[] values = {1,1};
		List<Map> teacherList = extraSpringHibernateTemplate.createSQLQueryFindAll(sql, values);
		return teacherList;
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public List<Map> qureyAll(Integer page,Integer size) {
		String sql = "select teacher_id id,first_word,head_url,introduction,level,name "
						+ "from tb_teacher "
						+ "where status = " + Constant.YES_INT + " order by create_time asc";
		List<Map> teacherAllList = null;
		if(page!=null&&size!=null){
			int start = (page-1)*size;
			teacherAllList = extraSpringHibernateTemplate.createSQLQueryFindPaging(sql, start, size);
		}else{
			teacherAllList = extraSpringHibernateTemplate.createSQLQueryFindAll(sql);
		}
		return teacherAllList;
	}
	
	@Override
	public Map queryDetailAll(String teacherId) {
		String sql = "select teacher_id id,top_type,top_pic_url,top_video_url,head_url,name,level,remark from tb_teacher where status = " + Constant.YES_INT + " and teacher_id = '" + teacherId+"'";
		Map queryById = extraSpringHibernateTemplate.createSQLQueryFindFirstOne(sql);
		return queryById;
	}

	@Override
	public Map queryCourseDetailTeacherMap(String courseId) {
		//根据课程ID获取讲师的ID,从而获取讲师的信息
		String middleSql = "select teacher_id from tb_course where id = '"+courseId + "'";
		Map teacherIdMap = extraSpringHibernateTemplate.createSQLQueryFindFirstOne(middleSql);
		String teacherSql = "select teacher_id,head_url,name,level,introduction from tb_teacher where teacher_id = '"+(String)teacherIdMap.get("teacher_id")+"'";
		Map teacherMap = extraSpringHibernateTemplate.createSQLQueryFindFirstOne(teacherSql);
		return teacherMap;
	}

	@Override
	public 	List<Map<String, Object>> queryTeacherRecordVideos(String teacherId,int page,int size) {
		Sql sql = CourseSupport.getCoursesByTeacherId(teacherId,"and `course`.`playing` in (" + Statics.COURSE_PLAYING_SHOW + "," + Statics.COURSE_PLAYING_REQUESTTYPE + ") ",2);
		page = page < 1 ? 1 : page;
		int start = (page - 1)*size;
		List<Map<String, Object>> list = od.getListBySql(sql.getSql(), sql.getParams(), start, size);
		return (list==null?new ArrayList<Map<String, Object>>():list);
		/*
	String 	pageNo=StringUtil.isNullOrBlank((String)json.get("pageNo"))?"1":(String)json.get("pageNo");
	String 	pagesize=StringUtil.isNullOrBlank((String)json.get("pagesize"))?"5":(String)json.get("pagesize");
	Integer pn=	Integer.valueOf(pageNo);
	Integer ps=	Integer.valueOf(pagesize);
	StringBuffer sql= new StringBuffer();
		sql.append(" select");
		sql.append(" tb_course.id,");
		sql.append(" tb_course.course_compaign_type,");
		sql.append(" tb_course.course_compaign_video_url as video_save_url,");
		sql.append(" tb_course.pic_big_url as video_pic_url,");
		sql.append(" tb_course.title,");
		sql.append(" tb_course.remark,");
		sql.append(" tb_teacher.name,");
		sql.append(" tb_course.teacher_id as creater_id,");
		sql.append(" tb_teacher.level");
		sql.append(" FROM");
		sql.append(" tb_course");
		sql.append(" LEFT JOIN tb_teacher ON tb_course.teacher_id=tb_teacher.teacher_id");
		sql.append(" LEFT JOIN tb_user  ON tb_user.id=tb_course.teacher_id AND tb_user.user_type = 1 ");
		sql.append(" WHERE");
		sql.append(" tb_course.teacher_id =? ");
		sql.append(" ORDER BY tb_course.create_time DESC");
	//String sql01="select tb_video.id,tb_video.time_long,tb_video.video_save_url,tb_video.video_pic_url,tb_video.title,tb_video.remark,tb_teacher.name,tb_video.creater_id,tb_teacher.level  from tb_user,tb_video,tb_teacher,tb_course where  tb_teacher.teacher_id=tb_user.id and tb_teacher.teacher_id =tb_course.teacher_id and tb_video.id=tb_course.video_id and tb_user.user_type=1 and tb_user.id=?";
		String [] parms={(String)json.get("userid")};
	List<Map> pa=	extraSpringHibernateTemplate.createSQLQueryFindPaging(sql.toString(), (pn-1)*ps, ps,parms);
	return pa;
	*/
	}

	@Override
	public List<Map<String, Object>> queryUpcomingVideoByTeacherId(String teacherId,int page,int size) {
		Sql sql = CourseSupport.getCoursesByTeacherId(teacherId,"and `course`.`status` = " + Constant.YES_INT + " and `course`.`playing` = " + Statics.COURSE_PLAYING_WILLSHOW + " ",2);
		page = page < 1 ? 1 : page;
		int start = (page - 1)*size;
		List<Map<String, Object>> list = od.getListBySql(sql.getSql(), sql.getParams(), start, size);
		return (list==null?new ArrayList<Map<String, Object>>():list);
		/*
		String sql01= "select tb_video.id,tb_video.time_long,tb_video.video_save_url,tb_video.video_pic_url,tb_video.title,tb_video.remark,tb_teacher.name,tb_video.creater_id,tb_teacher.level  from tb_video,tb_teacher,tb_user,tb_course where tb_user.id = tb_teacher.teacher_id and tb_video.id=tb_course.video_id and tb_course.teacher_id=tb_user.id and tb_teacher.teacher_id and tb_video.status=0 and tb_user.id=?";
		return page(sql01, json);
		*/
	}

	@Override
	public List<Map<String, Object>> queryAskVideo(String teacherId,int page,int size) {
		/*
		String sql01="select "
						+ "DISTINCT tb_video.id ,"
						+ "tb_video.time_long,"
						+ "tb_video.video_save_url,"
						+ "tb_video.video_pic_url,"
						+ "tb_video.title,"
						+ "tb_video.remark,"
						+ "tb_teacher.name,"
						+ "tb_video.creater_id,"
						+ "tb_teacher.level "
					+ "from tb_user,tb_user_video_request,tb_video,tb_teacher,tb_course "
					+ "where tb_course.video_id = tb_video.id "
						+ "and tb_course.teacher_id = tb_user.id "
						+ "and tb_user.id=tb_teacher.teacher_id "
						+ "and tb_user_video_request.teacher_id = tb_user.id "
						+ "and tb_user.id=?";
		*/
		String sql = getSqlSupport().replace("${conditions}", "");
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("teacherId", teacherId);
		page = page < 1 ? 1 : page;
		int start = (page - 1)*size;
		List<Map<String, Object>> list = od.getListBySql(sql, params, start, size);
		return (list==null?new ArrayList<Map<String, Object>>():list);
	}
	
	private String getSqlSupport(){
		String sql = "select "
				+ "`uvr`.`id` as `id`,"
				+ "`uvr`.`course_name` as `title`,"
				+ "`uvr`.`question` as `reqRemark`,"
				+ "`teacher`.`name` as `name`,"
				+ "`teacher`.`introduction` as `remark`,"
				+ "'' as `video_save_url`,"
				+ Statics.COURSE_SHOWTYPE_PIC + " as `course_compaign_type`,"
				+ "`teacher`.`head_url` as `video_pic_url` "
			+ "from `tb_user_video_request` as `uvr` "
			+ "LEFT JOIN `tb_teacher` as `teacher` on `teacher`.`teacher_id` = `uvr`.`teacher_id` "
			+ "where `teacher`.`teacher_id` = :teacherId "
			+ "${conditions}"
			+ "ORDER BY `uvr`.`request_time` desc";
		return sql;
	}

	@Override
	public List<Map<String, Object>> queryReleasedVideoByTeacherId(String teacherId,int page,int size) {
		Sql sql = CourseSupport.getCoursesByTeacherId(teacherId,"and `course`.`status` = " + Constant.YES_INT + " and `course`.`playing` in (" + Statics.COURSE_PLAYING_SHOW + "," + Statics.COURSE_PLAYING_REQUESTTYPE + ") ",2);
		page = page < 1 ? 1 : page;
		int start = (page - 1)*size;
		List<Map<String, Object>> list = od.getListBySql(sql.getSql(), sql.getParams(), start, size);
		
//		String sql01= "select tb_video.id,tb_video.time_long,tb_video.video_save_url,tb_video.video_pic_url,tb_video.title,tb_video.remark,tb_teacher.name,tb_video.creater_id,tb_teacher.level  from tb_video,tb_teacher,tb_user,tb_course where tb_course.video_id = tb_video.id and tb_course.teacher_id= tb_user.id and tb_user.id = tb_teacher.teacher_id and tb_video.status=1 and tb_user.id=?";
		return (list==null?new ArrayList<Map<String, Object>>():list);
	}

	@Override
	public List<Map<String, Object>> queryTeacherVoteVideos(String teacherId,int page,int size) {
		String sql = getSqlSupport().replace("${conditions}", "and `uvr`.`feedback_status` = " + RequestStatus.REQUEST_STATUS_DOING.getType() + " ");
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("teacherId", teacherId);
		page = page < 1 ? 1 : page;
		int start = (page - 1)*size;
		List<Map<String, Object>> list = od.getListBySql(sql, params, start, size);
		return (list==null?new ArrayList<Map<String, Object>>():list);
	}

	@Override
	public List<Map> queryAboutActivity(Map<String, Object> json) {
		String sql01="select tb_activity.id,tb_activity.activity_time,tb_activity.description,tb_activity.district,tb_activity.organiser,tb_activity.show_pic_url,tb_activity.show_type,tb_activity.show_video_url,tb_activity.title,tb_activity_status.enroll_begin_time,tb_activity_status.enroll_end_time,"
+"tb_activity_status.prepare_start_time,tb_activity_status.prepare_end_time,tb_activity_status.activity_start_time,"
+"tb_activity_status.activity_end_time,tb_activity_status.cancel_time from tb_user,tb_activity_user_relationship,tb_activity,tb_activity_status where tb_activity.id = tb_activity_user_relationship.activity_id and tb_activity_status.activity_id=tb_activity.id and  tb_user.id = tb_activity_user_relationship.user_id and tb_user.user_type=1 and tb_user.id=?";
		return page(sql01,json);
	}
	
	private List<Map> page(String sql01,Map<String, Object> json) {
		String[] values = { (String) json.get("userid") };
		// pageNo前台传参从1开始
		boolean pageNoNull = StringUtil.isNullOrBlank(json.get("pageNo"));
		boolean pagesizeNull = StringUtil.isNullOrBlank(json.get("pagesize"));
		StringBuffer sql = new StringBuffer();
		sql.append(sql01);
		List<Map> participationList = extraSpringHibernateTemplate.createSQLQueryFindAll(sql.toString(), values);
		sql.append(" LIMIT ");
		if (pageNoNull && pagesizeNull) {
			sql.append(Constant.SUBJECT_PAGENO + ","
					+ Constant.SUBJECT_PAGESIZE);
		} else if (pageNoNull) {
			sql.append(Constant.SUBJECT_PAGENO + ","
					+ Integer.valueOf((String) json.get("pagesize")));
		} else if (pagesizeNull) {
			int pageNo = Integer.valueOf((String) json.get("pageNo"));
			if ((pageNo - 1) * Constant.SUBJECT_PAGESIZE > participationList
					.size()) {
				int max = participationList.size() / Constant.SUBJECT_PAGESIZE;
				int start = max * Constant.SUBJECT_PAGESIZE;
				sql.append(start + "," + Constant.SUBJECT_PAGESIZE);
			} else if (pageNo >= 1) {
				int start = (pageNo - 1) * Constant.SUBJECT_PAGESIZE;
				sql.append(start + "," + Constant.SUBJECT_PAGESIZE);
			} else {
				sql.append(Constant.SUBJECT_PAGENO + ","
						+ Constant.SUBJECT_PAGESIZE);
			}
		} else {
			int pageNo = Integer.valueOf((String) json.get("pageNo"));
			int pagesize = Integer.valueOf((String) json.get("pagesize"));
			if ((pageNo - 1) * pagesize > participationList.size()) {
				int max = participationList.size() / pagesize;
				int start = max * pagesize;
				sql.append(start + "," + pagesize);
			} else if (pageNo >= 1) {
				int start = (pageNo - 1) * pagesize;
				int end = pageNo * pagesize;
				sql.append(start + "," + end);
			} else {
				sql.append(Constant.SUBJECT_PAGENO + "," + pagesize);
			}
		}
		return extraSpringHibernateTemplate.createSQLQueryFindAll(sql.toString(), values);
	}

	@Override
	public Long queryCountByAcitityId(String activityId) {
		String sql="select user_id coun from tb_activity_user_relationship where activity_id=?";
		String[] values={activityId};
		return extraSpringHibernateTemplate.createSQLQueryCount(sql, values);
	}
}



















