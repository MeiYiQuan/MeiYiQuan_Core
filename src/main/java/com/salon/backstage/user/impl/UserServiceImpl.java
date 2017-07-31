package com.salon.backstage.user.impl;

import java.io.Serializable;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.qc.util.MathUtil;
import com.salon.backstage.common.util.ExtraSpringHibernateTemplate;
import com.salon.backstage.common.util.MD5Util;
import com.salon.backstage.common.util.MobileMessageCondition;
import com.salon.backstage.common.util.StringUtil;
import com.salon.backstage.homepage.point.service.IPointService;
import com.salon.backstage.homepage.push.service.PushService;
import com.salon.backstage.homepage.statistics.service.IStatisticsService;
import com.salon.backstage.pub.bsc.dao.po.Collect;
import com.salon.backstage.pub.bsc.dao.po.Comment;
import com.salon.backstage.pub.bsc.dao.po.Playrecord;
import com.salon.backstage.pub.bsc.dao.po.Suggestion;
import com.salon.backstage.pub.bsc.dao.po.SuggestionType;
import com.salon.backstage.pub.bsc.dao.po.User;
import com.salon.backstage.pub.bsc.dao.po.UserVideoRequest;
import com.salon.backstage.pub.bsc.dao.vo.MobileMessage;
import com.salon.backstage.pub.bsc.domain.Constant;
import com.salon.backstage.pub.bsc.domain.Constant.ChannelTop;
import com.salon.backstage.pub.bsc.domain.Constant.PointEachType;
import com.salon.backstage.pub.bsc.domain.Constant.UserType;
import com.salon.backstage.qcproject.dao.ObjectDao;
import com.salon.backstage.qcproject.util.CleanUtil;
import com.salon.backstage.qcproject.util.Code;
import com.salon.backstage.qcproject.util.DateUtil;
import com.salon.backstage.qcproject.util.Send;
import com.salon.backstage.qcproject.util.Statics;
import com.salon.backstage.user.IUserService;

@Service
public class UserServiceImpl implements IUserService {
	@Autowired
	private ExtraSpringHibernateTemplate extraSpringHibernateTemplate;
	@Autowired
	IPointService pointService;
	
	@Autowired
	private IStatisticsService is;
	
	@Autowired
	private PushService ps;
	
	@Autowired
	ObjectDao od;
	
	public UserServiceImpl(){}

	/**
	 * 用户登录,通过手机号以及密码获得用户
	 */
	@Override
	public User queryCountByPhonePassword(Map<String, Object> json)
			throws NumberFormatException, Exception {
		String[] propName = { "phone", "password" };
		Object[] propValue = { Long.valueOf((String) json.get("phone")),MD5Util.encrypt((String) json.get("password")) };
		User user = extraSpringHibernateTemplate.findFirstOneByPropEq(User.class, propName, propValue);
		if (user == null) {
			return user;
		}
		// 获得用户此次登录的设备类型,更新到数据库
		user.setEqui_type(Integer.valueOf((String) json.get("equi_type")));
		extraSpringHibernateTemplate.getHibernateTemplate().update(user);
//		Session session = extraSpringHibernateTemplate.getHibernateTemplate().getSessionFactory().getCurrentSession();
//		Transaction t = session.beginTransaction();
		
		return user;
	}

	/**
	 * 用户注册,封装用户信息
	 */
	@Override
	public User register(Map<String, Object> json) throws Exception {
		User user = mapToUser(json);
		extraSpringHibernateTemplate.getHibernateTemplate().save(user);
		String sql = " select * from tb_user where phone = " + user.getPhone();
		User getUser = (User) extraSpringHibernateTemplate.findFirstOneByPropEq(User.class, "phone", user.getPhone());
		return getUser;
	}

	/**
	 * 将Map集合中的数据封装进实体类对象中
	 */
	public User mapToUser(Map<String, Object> json) throws Exception {
		User user = new User();
		// 遍历集合取数据
		Iterator it = json.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry entry = (Map.Entry) it.next();
			String key = (String) entry.getKey();
			String value = (String) entry.getValue();
			saveProperty(key, value, user);
		}
		String phoneString = (String) json.get("phone");
		user.setUsername(phoneString.substring(0, 3) + "****"+ phoneString.substring(7, 11));
		user.setCreate_time(Long.valueOf(new SimpleDateFormat("yyyyMMddHHmmss").format(new Date())));// 用户的创建时间
		user.setUpdate_time(Long.valueOf(new SimpleDateFormat("yyyyMMddHHmmss").format(new Date())));// 用户的修改时间
		return user;
	}

	/**
	 * 遍历Map集合是判断Entry的key对应的实体类属性,并将Entry的value赋值给该属性
	 */
	public void saveProperty(String key, String value, User user)
			throws Exception {
		if (key.equals("password")) {
			user.setPassword(MD5Util.encrypt(value));
		} else if (key.equals("gender")) {
			user.setGender(Integer.valueOf(value));
		} else if (key.equals("phone")) {
			user.setPhone(Long.valueOf(value));
		} else if (key.equals("pic_save_url")) {
			user.setPic_save_url(value);
		} else if (key.equals("district")) {
			user.setDistrict(value);
		} else if (key.equals("birthday")) {
			user.setBirthday(Long.valueOf(value));
		} else if (key.equals("job")) {
			user.setJob(value);
		} else if (key.equals("zodiac")) {// 星座
			user.setZodiac(value);
		} else if (key.equals("vchat_iden")) {
			user.setVchat_iden(Integer.valueOf(value));
		} else if (key.equals("push_token")) {
			user.setPush_token(value);
		} else if (key.equals("user_type")) {
			user.setUser_type(Integer.valueOf(value));
		}
	}

	/**
	 * 根据手机号查看是否存在该用户
	 */
	@Override
	public User queryCountByPhone(Map<String, Object> json) {
		Long phone_value = Long.valueOf((String) json.get("phone"));
		return (User) extraSpringHibernateTemplate.findFirstOneByPropEq(User.class, "phone", phone_value);
	}

	/**
	 * 修改密码
	 */
	@Override
	public void editPassword(Map<String, Object> json)
			throws NoSuchAlgorithmException {
		User user = queryCountByPhone(json);
		user.setPassword(MD5Util.encrypt((String) json.get("password")));
		user.setUpdate_time(Long.valueOf(new SimpleDateFormat("yyyyMMddHHmmss").format(new Date())));
		extraSpringHibernateTemplate.getHibernateTemplate().update(user);
		
	}

	@Override
	public Map persionalDataById(Map<String, Object> json) {
		String sql = "select id,pic_save_url,username,gender,district,birthday,job,zodiac,jobId,zodiacIndex from tb_user where id=?";
		String[] values = { (String) json.get("userid") };
		Map map = extraSpringHibernateTemplate.createSQLQueryFindFirstOne(sql,values);
		return map;

	}

	@Override
	public void replaceHead(Map<String, Object> json) {
		User user = extraSpringHibernateTemplate.findFirstOneByPropEq(User.class, "id", json.get("userid"));
		user.setPic_save_url((String) json.get("picSaveUrl"));
		extraSpringHibernateTemplate.getHibernateTemplate().update(user);
	}

	@Override
	public List<Map> queryCountByUserIdAndTeacher(Map<String, Object> json) {
		// 名人大咖
		String[] values = { "2", (String) json.get("userid") };
		// pageNo前台传参从1开始
		boolean pageNoNull = StringUtil.isNullOrBlank(json.get("pageNo"));
		boolean pagesizeNull = StringUtil.isNullOrBlank(json.get("pagesize"));
		StringBuffer sql = new StringBuffer();
		sql.append("select tb_teacher.id,tb_teacher.head_url,tb_teacher.introduction,tb_teacher.level,tb_teacher.name from tb_collect,tb_teacher where tb_collect.collect_type_id=tb_teacher.id and tb_collect.collect_type=? and user_id=?");
		List<Map> collectList = extraSpringHibernateTemplate.createSQLQueryFindAll(sql.toString(), values);
		sql.append(" LIMIT ");
		if (pageNoNull && pagesizeNull) {
			sql.append(Constant.SUBJECT_PAGENO + ","+ Constant.SUBJECT_PAGESIZE);
		} else if (pageNoNull) {
			sql.append(Constant.SUBJECT_PAGENO + ","+ Integer.valueOf((String) json.get("pagesize")));
		} else if (pagesizeNull) {
			int pageNo = Integer.valueOf((String) json.get("pageNo"));
			if ((pageNo - 1) * Constant.SUBJECT_PAGESIZE > collectList.size()) {
				int max = collectList.size() / Constant.SUBJECT_PAGESIZE;
				int start = max * Constant.SUBJECT_PAGESIZE;
				sql.append(start + "," + Constant.SUBJECT_PAGESIZE);
			} else if (pageNo >= 1) {
				int start = (pageNo - 1) * Constant.SUBJECT_PAGESIZE;
				sql.append(start + "," + Constant.SUBJECT_PAGESIZE);
			} else {
				sql.append(Constant.SUBJECT_PAGENO + ","+ Constant.SUBJECT_PAGESIZE);
			}
		} else {
			int pageNo = Integer.valueOf((String) json.get("pageNo"));
			int pagesize = Integer.valueOf((String) json.get("pagesize"));
			if ((pageNo - 1) * pagesize > collectList.size()) {
				int max = collectList.size() / pagesize;
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
	public List<Map> queryCountByUserIdAndActivity(Map<String, Object> json) {
		// 名人大咖
		String[] values = { "1", (String) json.get("userid") };
		// pageNo前台传参从1开始
		boolean pageNoNull = StringUtil.isNullOrBlank(json.get("pageNo"));
		boolean pagesizeNull = StringUtil.isNullOrBlank(json.get("pagesize"));
		StringBuffer sql = new StringBuffer();
		sql.append("select tb_activity.id,tb_activity.activity_time,tb_activity.description,tb_activity.district,tb_activity.organiser,tb_activity.show_pic_url,tb_activity.show_type,tb_activity.show_video_url,tb_activity.title,tb_activity_status.enroll_begin_time,tb_activity_status.enroll_end_time,tb_activity_status.prepare_start_time,tb_activity_status.prepare_end_time,tb_activity_status.activity_start_time,tb_activity_status.activity_end_time,tb_activity_status.cancel_time from tb_activity,tb_activity_status,tb_collect where tb_activity.id=tb_collect.collect_type_id and tb_collect.collect_type=? and tb_activity_status.activity_id=tb_activity.id and tb_activity.user_id=? ");
		List<Map> collectList = extraSpringHibernateTemplate.createSQLQueryFindAll(sql.toString(), values);
		sql.append(" LIMIT ");
		if (pageNoNull && pagesizeNull) {
			sql.append(Constant.SUBJECT_PAGENO + ","+ Constant.SUBJECT_PAGESIZE);
		} else if (pageNoNull) {
			sql.append(Constant.SUBJECT_PAGENO + ","+ Integer.valueOf((String) json.get("pagesize")));
		} else if (pagesizeNull) {
			int pageNo = Integer.valueOf((String) json.get("pageNo"));
			if ((pageNo - 1) * Constant.SUBJECT_PAGESIZE > collectList.size()) {
				int max = collectList.size() / Constant.SUBJECT_PAGESIZE;
				int start = max * Constant.SUBJECT_PAGESIZE;
				sql.append(start + "," + Constant.SUBJECT_PAGESIZE);
			} else if (pageNo >= 1) {
				int start = (pageNo - 1) * Constant.SUBJECT_PAGESIZE;
				sql.append(start + "," + Constant.SUBJECT_PAGESIZE);
			} else {
				sql.append(Constant.SUBJECT_PAGENO + ","+ Constant.SUBJECT_PAGESIZE);
			}
		} else {
			int pageNo = Integer.valueOf((String) json.get("pageNo"));
			int pagesize = Integer.valueOf((String) json.get("pagesize"));
			if ((pageNo - 1) * pagesize > collectList.size()) {
				int max = collectList.size() / pagesize;
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
	public List<Map> queryCountByUserIdAndVideo(Map<String, Object> json) {
		// 视频
		String[] values = { "3", (String) json.get("userid") };
		boolean pageNoNull = StringUtil.isNullOrBlank(json.get("pageNo"));
		boolean pagesizeNull = StringUtil.isNullOrBlank(json.get("pagesize"));
		StringBuffer sql = new StringBuffer();
		sql.append("select tb_video.id,tb_video.video_save_url,tb_video.video_pic_url,tb_video.title,tb_video.remark,tb_teacher.name,tb_video.creater_id,tb_teacher.level from tb_collect,tb_video,tb_teacher,tb_course where  tb_collect.collect_type_id=tb_video.id and tb_video.course_id = tb_course.id and tb_course.teacher_id = tb_teacher.id and tb_collect.collect_type=? and tb_collect.user_id=?");
		List<Map> videoList = extraSpringHibernateTemplate.createSQLQueryFindAll(sql.toString(), values);
		sql.append(" LIMIT ");
		if (pageNoNull && pagesizeNull) {
			sql.append(Constant.SUBJECT_PAGENO + ","+ Constant.SUBJECT_PAGESIZE);
		} else if (pageNoNull) {
			sql.append(Constant.SUBJECT_PAGENO + ","+ Integer.valueOf((String) json.get("pagesize")));
		} else if (pagesizeNull) {
			int pageNo = Integer.valueOf((String) json.get("pageNo"));
			if ((pageNo - 1) * Constant.SUBJECT_PAGESIZE > videoList.size()) {
				int max = videoList.size() / Constant.SUBJECT_PAGESIZE;
				int start = max * Constant.SUBJECT_PAGESIZE;
				sql.append(start + "," + Constant.SUBJECT_PAGESIZE);
			} else if (pageNo >= 1) {
				int start = (pageNo - 1) * Constant.SUBJECT_PAGESIZE;
				sql.append(start + "," + Constant.SUBJECT_PAGESIZE);
			} else {
				sql.append(Constant.SUBJECT_PAGENO + ","+ Constant.SUBJECT_PAGESIZE);
			}
		} else {
			int pageNo = Integer.valueOf((String) json.get("pageNo"));
			int pagesize = Integer.valueOf((String) json.get("pagesize"));
			if ((pageNo - 1) * pagesize > videoList.size()) {
				int max = videoList.size() / pagesize;
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
	public void deleteCollectByCollectTypeId(Map<String, Object> json) {
		String collectTypeIdss = json.get("collectTypeIds").toString();
		String colllectTypess = json.get("collectTypes").toString();
		String[] collectTypeIds = collectTypeIdss.split(",");
		String[] colllectTypes = colllectTypess.split(",");
		for (int i = 0; i < collectTypeIds.length; i++) {
			for (int j = 0; j < colllectTypes.length; j++) {
				String sql = "select tb_collect.id  from tb_collect where collect_type=? and collect_type_id=? and user_id=?";
				String[] values = { colllectTypes[j], collectTypeIds[i],(String) json.get("userid") };
				Map map = extraSpringHibernateTemplate.createSQLQueryFindFirstOne(sql, values);
				String id = (String) map.get("id");
				extraSpringHibernateTemplate.deleteFirstOneByPropEq(Collect.class, "id", id);
			}
		}
	}

	@Override
	public List<Map> queryCollectForWeek(Map<String, Object> json) {
		StringBuffer sql = new StringBuffer("SELECT");
		sql.append(" tb_playrecord.id,");
		sql.append(" tb_playrecord.video_id,");
		sql.append(" tb_playrecord.user_id,");
		sql.append(" tb_playrecord.play_time,");
		sql.append(" tb_video.title,");
		sql.append(" tb_playrecord.continue_time,");
		sql.append(" tb_video.time_long,");
		sql.append(" tb_playrecord.course_id");
		sql.append(" FROM");
		sql.append(" tb_playrecord");
		sql.append(" LEFT JOIN tb_video ON tb_video.id=tb_playrecord.video_id");
		sql.append(" WHERE");
		sql.append(" tb_playrecord.video_id = tb_video.id");
		sql.append(" AND YEARWEEK(");
		sql.append(" date_format(");
		sql.append(" 	tb_playrecord.play_time,");
		sql.append(" '%Y-%m-%d'");
		sql.append(" )");
		sql.append(" ) <= YEARWEEK(now())");
		sql.append(" AND user_id =?");
		String[] values = { (String) json.get("userid") };
		return extraSpringHibernateTemplate.createSQLQueryFindAll(sql.toString(), values);
	}

	@Override
	public List<Map> queryCollectForMonth(Map<String, Object> json) {
		String sql = "select tb_playrecord.id,tb_playrecord.play_time,tb_video.title,tb_playrecord.continue_time,tb_video.time_long,tb_playrecord.course_id from tb_playrecord,tb_video  where tb_playrecord.video_id=tb_video.id and  date_format(tb_playrecord.play_time,'%Y-%m')=date_format(DATE_SUB(NOW(), INTERVAL 30 DAY),'%Y-%m') and user_id=?";
		String[] values = { (String) json.get("userid") };
		return extraSpringHibernateTemplate.createSQLQueryFindAll(sql, values);
	}

	@Override
	public List<Map> queryCollectForMonthBefore(Map<String, Object> json) {
		String sql = "select tb_playrecord.id,tb_playrecord.play_time,tb_video.title,tb_playrecord.continue_time,tb_video.time_long,tb_playrecord.course_id from tb_playrecord,tb_video  where tb_playrecord.video_id=tb_video.id and date_format(tb_playrecord.play_time,'%Y-%m')<date_format(DATE_SUB(NOW(), INTERVAL 30 DAY),'%Y-%m') and user_id=?;";
		String[] values = { (String) json.get("userid") };
		return extraSpringHibernateTemplate.createSQLQueryFindAll(sql, values);
	}

	@Override
	public List<Map> queryPlayRecordPercent(String videoId, String userId) {
		String sql = "select tb_playrecord.continue_time/tb_video.time_long*100  from tb_playrecord,tb_video where tb_playrecord.video_id=tb_video.id and tb_playrecord.video_id=? and user_id=?";
		String[] values = { userId, videoId };
		Map map = extraSpringHibernateTemplate.createSQLQueryFindFirstOne(sql,values);
		Object a = map.get("tb_playrecord.continue_time/tb_video.time_long*100");
		String sql1 = "SELECT CONCAT(ROUND(" + a + ",2),'%')";
		List all = extraSpringHibernateTemplate.createSQLQueryFindAll(sql1);
		return all;
	}

	@Override
	public void editUserGender(Map<String, Object> json) {
		int gender = 0;
		if (json.get("gender").equals("男")) {
			gender = 2;
		} else {
			gender = 1;
		}
		User user = extraSpringHibernateTemplate.findFirstOneByPropEq(User.class, "id", json.get("userid"));
		user.setGender(gender);
		extraSpringHibernateTemplate.getHibernateTemplate().update(user);
	}

	@Override
	public void editUserZodiac(Map<String, Object> json) {
		User user = extraSpringHibernateTemplate.findFirstOneByPropEq(User.class, "id", json.get("userid"));
		user.setZodiac((String) json.get("zodiac"));
		extraSpringHibernateTemplate.getHibernateTemplate().update(user);
	}

	@Override
	public void editPlayRecord(Map<String, Object> json) {
		String lists = (String) json.get("playRecordId");
		String[] list = lists.split(",");
		for (int i = 0; i < list.length; i++) {
			extraSpringHibernateTemplate.deleteFirstOneByPropEq(Playrecord.class, "id", list[i]);
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Map> queryMyParticipation(Map<String, Object> json) {
		String[] values = { (String) json.get("userid"),(String) json.get("manType") };
		// pageNo前台传参从1开始
		boolean pageNoNull = StringUtil.isNullOrBlank(json.get("pageNo"));
		boolean pagesizeNull = StringUtil.isNullOrBlank(json.get("pagesize"));
		StringBuffer sql = new StringBuffer();
		sql.append("select tb_activity.id,tb_activity.part_num,tb_activity.show_video_url,tb_activity.activity_status,tb_activity.activity_time,tb_activity.address,tb_activity.description,tb_activity.most_man,tb_activity.organiser,tb_activity.price,tb_activity.show_pic_url,tb_activity.show_type,tb_activity.title from tb_activity,tb_activity_user_relationship where tb_activity.id=tb_activity_user_relationship.activity_id and tb_activity_user_relationship.user_id=? and tb_activity_user_relationship.man_type=?");
		List<Map> participationList = extraSpringHibernateTemplate.createSQLQueryFindAll(sql.toString(), values);
		sql.append(" LIMIT ");
		if (pageNoNull && pagesizeNull) {
			sql.append(Constant.SUBJECT_PAGENO + ","+ Constant.SUBJECT_PAGESIZE);
		} else if (pageNoNull) {
			sql.append(Constant.SUBJECT_PAGENO + ","+ Integer.valueOf((String) json.get("pagesize")));
		} else if (pagesizeNull) {
			int pageNo = Integer.valueOf((String) json.get("pageNo"));
			if ((pageNo - 1) * Constant.SUBJECT_PAGESIZE > participationList.size()) {
				int max = participationList.size() / Constant.SUBJECT_PAGESIZE;
				int start = max * Constant.SUBJECT_PAGESIZE;
				sql.append(start + "," + Constant.SUBJECT_PAGESIZE);
			} else if (pageNo >= 1) {
				int start = (pageNo - 1) * Constant.SUBJECT_PAGESIZE;
				sql.append(start + "," + Constant.SUBJECT_PAGESIZE);
			} else {
				sql.append(Constant.SUBJECT_PAGENO + ","+ Constant.SUBJECT_PAGESIZE);
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
		List list = extraSpringHibernateTemplate.createSQLQueryFindAll(sql.toString(), values);
		return list;
	}

	@Override
	public List<Map> queryActivityForDetail(Map<String, Object> json) {
		String sql = "select tb_activity.id,tb_activity.title,tb_activity.activity_time,tb_activity.share_url,tb_activity.address,tb_activity.organiser,tb_activity.price "
							+ "from tb_activity ,tb_activity_user_relationship "
							+ "where tb_activity.id=tb_activity_user_relationship.activity_id "
							+ "and tb_activity_user_relationship.user_id=? "
							+ "and tb_activity_user_relationship.activity_id=?";
		String[] values = {(String) json.get("activityId") };
		return extraSpringHibernateTemplate.createSQLQueryFindAll(sql, values);
	}

	@Override
	public List<Map> queryActivityForparticipatorList(Map<String, Object> json) {
		String sql = "select tb_user.id,tb_user.pic_save_url,tb_user.username  from tb_activity_user_relationship,tb_user where tb_activity_user_relationship.user_id=tb_user.id and tb_activity_user_relationship.activity_id=? and tb_activity_user_relationship.man_type=1";
		String[] values = { (String) json.get("activityId") };
		return extraSpringHibernateTemplate.createSQLQueryFindAll(sql, values);
	}

	@Override
	public List<Map> queryActivityForGuest(Map<String, Object> json) {
		String sql = "select tb_teacher.id,tb_teacher.head_url,tb_teacher.name,tb_teacher.level,tb_activity.title  from tb_activity,tb_activity_user_relationship,tb_teacher where tb_activity.id=tb_activity_user_relationship.activity_id and tb_activity_user_relationship.user_id=tb_teacher.id and tb_activity_user_relationship.activity_id=? and tb_activity_user_relationship.man_type=3";
		String[] values = {(String) json.get("activityId")};
		return extraSpringHibernateTemplate.createSQLQueryFindAll(sql, values);
	}
	
	@Override
	public List<Map> queryActivityForparticipatorCount(Map<String, Object> json) {
		String sql = "select count(tb_user.id) count  from tb_activity_user_relationship,tb_user where tb_activity_user_relationship.user_id=tb_user.id and tb_activity_user_relationship.activity_id=? and tb_activity_user_relationship.man_type=1";
		String[] values = { (String) json.get("activityId") };
		return extraSpringHibernateTemplate.createSQLQueryFindAll(sql, values);
	}

	@Override
	public Map queryUserVideoRequest(Map<String, Object> json) {
		String sql = "select "
						+ "tb_user_video_request.id,"
						+ "tb_user_video_request.user_id,"
						+ "tb_user_video_request.teacher_id,"
						+ "sum(tb_user_video_request.vote) vote,"
						+ "tb_teacher.name,"
						+ "tb_user_video_request.course_name,"
						+ "tb_user_video_request.share_url,"
						+ "tb_user_video_request.feedback,"
						+ "tb_teacher.head_url,"
						+ "tb_teacher.level,"
						+ "tb_teacher.introduction,"
						+ "if(tb_user_video_request.top_channel_id = '" + ChannelTop.CHANNEL_TOP_CHUANGYEKAIDIAN.getTopId() + "',0,1) as `belong_type`,"
						+ "tb_teacher.top_pic_url "
						+ "from tb_user_video_request,tb_teacher "
						+ "where tb_user_video_request.teacher_id=tb_teacher.teacher_id "
						+ "AND  tb_user_video_request.user_id=? "
						+ "and tb_user_video_request.id=?";
		String[] values = { (String) json.get("userid"),(String) json.get("courseId") };
		return extraSpringHibernateTemplate.createSQLQueryFindFirstOne(sql,values);

	}

	@Override
	public void insertUserVideoRequest(Map<String, Object> json) {
		UserVideoRequest userVideoRequest = new UserVideoRequest();
		userVideoRequest.setRequest_time(Long.valueOf(new SimpleDateFormat("yyyyMMddHHmmss").format(new Date())));// 用户的创建时间
		userVideoRequest.setUser_id((String) json.get("userid"));
		userVideoRequest.setCourse_name((String) json.get("courseName"));
		userVideoRequest.setTeacher_id((String) json.get("teacherId"));
		userVideoRequest.setQuestion((String) json.get("question"));
		extraSpringHibernateTemplate.getHibernateTemplate().save(userVideoRequest);
	}

	@Override
	public User queryUserById(Map<String, Object> json) {
		User user = extraSpringHibernateTemplate.findFirstOneByPropEq(User.class, "id", json.get("userid"));
		return user;
	}

	@Override
	public void setUpEditPassword(Map<String, Object> json) {
		User user = extraSpringHibernateTemplate.findFirstOneByPropEq(User.class, "id", json.get("userid"));
		try {
			user.setPassword(MD5Util.encrypt((String) json.get("newPassword")));
			extraSpringHibernateTemplate.getHibernateTemplate().update(user);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
	}

	@Override
	public List<Map> querySystemInfo(Map<String, Object> json) {
		String[] values = { (String) json.get("userid") };
		// pageNo前台传参从1开始
		boolean pageNoNull = StringUtil.isNullOrBlank(json.get("pageNo"));
		boolean pagesizeNull = StringUtil.isNullOrBlank(json.get("pagesize"));
		StringBuffer sql = new StringBuffer();
		sql.append("select id,title,content,get_info_time,system_pic_url from tb_system_information where user_id=? order by get_info_time desc");
		List<Map> systemInfoList = extraSpringHibernateTemplate.createSQLQueryFindAll(sql.toString(), values);
		sql.append(" LIMIT ");
		if (pageNoNull && pagesizeNull) {
			sql.append(Constant.SUBJECT_PAGENO + ","+ Constant.SUBJECT_PAGESIZE);
		} else if (pageNoNull) {
			sql.append(Constant.SUBJECT_PAGENO + ","+ Integer.valueOf((String) json.get("pagesize")));
		} else if (pagesizeNull) {
			int pageNo = Integer.valueOf((String) json.get("pageNo"));
			if ((pageNo - 1) * Constant.SUBJECT_PAGESIZE > systemInfoList.size()) {
				int max = systemInfoList.size() / Constant.SUBJECT_PAGESIZE;
				int start = max * Constant.SUBJECT_PAGESIZE;
				sql.append(start + "," + Constant.SUBJECT_PAGESIZE);
			} else if (pageNo >= 1) {
				int start = (pageNo - 1) * Constant.SUBJECT_PAGESIZE;
				sql.append(start + "," + Constant.SUBJECT_PAGESIZE);
			} else {
				sql.append(Constant.SUBJECT_PAGENO + ","+ Constant.SUBJECT_PAGESIZE);
			}
		} else {
			int pageNo = Integer.valueOf((String) json.get("pageNo"));
			int pagesize = Integer.valueOf((String) json.get("pagesize"));
			if ((pageNo - 1) * pagesize > systemInfoList.size()) {
				int max = systemInfoList.size() / pagesize;
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
	public List<Map> querySystemInfoDetail(Map<String, Object> json) {
		String sql = "select title,system_pic_url,content,get_info_time from tb_system_information where id=? and user_id=?";
		String[] values = { (String) json.get("systemInfoId"),(String) json.get("userid") };
		return extraSpringHibernateTemplate.createSQLQueryFindAll(sql, values);
	}

	@Override
	public List<Map> queryPointAll(Map<String, Object> json) {
		String[] values = { (String) json.get("userid") };
		// pageNo前台传参从1开始
		boolean pageNoNull = StringUtil.isNullOrBlank(json.get("pageNo"));
		boolean pagesizeNull = StringUtil.isNullOrBlank(json.get("pagesize"));
		StringBuffer sql = new StringBuffer();
		sql.append("select tb_point.id,get_time,point_quantum,way_name from tb_point,tb_point_way_most where tb_point.point_way_id=tb_point_way_most.id and tb_point.user_id=? order by tb_point.get_time desc");
		List<Map> pointAll = extraSpringHibernateTemplate.createSQLQueryFindAll(sql.toString(), values);
		sql.append(" LIMIT ");
		if (pageNoNull && pagesizeNull) {
			sql.append(Constant.SUBJECT_PAGENO + ","+ Constant.SUBJECT_PAGESIZE);
		} else if (pageNoNull) {
			sql.append(Constant.SUBJECT_PAGENO + ","+ Integer.valueOf((String) json.get("pagesize")));
		} else if (pagesizeNull) {
			int pageNo = Integer.valueOf((String) json.get("pageNo"));
			if ((pageNo - 1) * Constant.SUBJECT_PAGESIZE > pointAll.size()) {
				int max = pointAll.size() / Constant.SUBJECT_PAGESIZE;
				int start = max * Constant.SUBJECT_PAGESIZE;
				sql.append(start + "," + Constant.SUBJECT_PAGESIZE);
			} else if (pageNo >= 1) {
				int start = (pageNo - 1) * Constant.SUBJECT_PAGESIZE;
				sql.append(start + "," + Constant.SUBJECT_PAGESIZE);
			} else {
				sql.append(Constant.SUBJECT_PAGENO + ","+ Constant.SUBJECT_PAGESIZE);
			}
		} else {
			int pageNo = Integer.valueOf((String) json.get("pageNo"));
			int pagesize = Integer.valueOf((String) json.get("pagesize"));
			if ((pageNo - 1) * pagesize > pointAll.size()) {
				int max = pointAll.size() / pagesize;
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
	public List<Map> queryCoupon(Map<String, Object> json) {
		String[] values = { (String) json.get("userid") };
		// pageNo前台传参从1开始
		boolean pageNoNull = StringUtil.isNullOrBlank(json.get("pageNo"));
		boolean pagesizeNull = StringUtil.isNullOrBlank(json.get("pagesize"));
		StringBuffer sql = new StringBuffer();
		sql.append("select tb_coupon_user.id,"
				+ "tb_coupon_user.use_type,"
				+ "tb_coupon.coupon_type,"
				+ "tb_coupon.status,"
				+ "tb_coupon.background_pic_url,"
				+ "tb_coupon.denomination,"
				+ "tb_coupon_user.number,"
				+ "tb_coupon_user.expire_time "
				+ "from tb_coupon,tb_coupon_user "
				+ "where tb_coupon.id=tb_coupon_user.coupon_id "
				+ "and tb_coupon_user.user_id=? "
				+ "and tb_coupon_user.status=" + Constant.YES_INT);
		List<Map> pointAll = extraSpringHibernateTemplate.createSQLQueryFindAll(sql.toString(), values);
		sql.append(" order by tb_coupon_user.expire_time asc LIMIT ");
		if (pageNoNull && pagesizeNull) {
			sql.append(Constant.SUBJECT_PAGENO + ","+ Constant.SUBJECT_PAGESIZE);
		} else if (pageNoNull) {
			sql.append(Constant.SUBJECT_PAGENO + ","+ Integer.valueOf((String) json.get("pagesize")));
		} else if (pagesizeNull) {
			int pageNo = Integer.valueOf((String) json.get("pageNo"));
			if ((pageNo - 1) * Constant.SUBJECT_PAGESIZE > pointAll.size()) {
				int max = pointAll.size() / Constant.SUBJECT_PAGESIZE;
				int start = max * Constant.SUBJECT_PAGESIZE;
				sql.append(start + "," + Constant.SUBJECT_PAGESIZE);
			} else if (pageNo >= 1) {
				int start = (pageNo - 1) * Constant.SUBJECT_PAGESIZE;
				sql.append(start + "," + Constant.SUBJECT_PAGESIZE);
			} else {
				sql.append(Constant.SUBJECT_PAGENO + ","+ Constant.SUBJECT_PAGESIZE);
			}
		} else {
			int pageNo = Integer.valueOf((String) json.get("pageNo"));
			int pagesize = Integer.valueOf((String) json.get("pagesize"));
			if ((pageNo - 1) * pagesize > pointAll.size()) {
				int max = pointAll.size() / pagesize;
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
	public List<Map> queryActivityListForCountry(Map<String, Object> json) {
		// pageNo前台传参从1开始
		boolean pageNoNull = StringUtil.isNullOrBlank(json.get("pageNo"));
		boolean pagesizeNull = StringUtil.isNullOrBlank(json.get("pagesize"));
		StringBuffer sql = new StringBuffer();
		sql.append("select tb_activity.id,tb_activity.share_url,tb_activity.activity_time,tb_activity.description,tb_activity.district,tb_activity.organiser,tb_activity.show_pic_url,tb_activity.show_type,tb_activity.show_video_url,tb_activity.title,tb_activity_status.enroll_begin_time,tb_activity_status.enroll_end_time,"
				+ "tb_activity_status.prepare_start_time,tb_activity_status.prepare_end_time,tb_activity_status.activity_start_time,"
				+ "tb_activity_status.activity_end_time,tb_activity_status.cancel_time "
				+ "from tb_activity,tb_activity_status "
				+ "where tb_activity_status.activity_id=tb_activity.id");
		List<Map> systemInfoList = extraSpringHibernateTemplate.createSQLQueryFindAll(sql.toString());
		sql.append(" LIMIT ");
		if (pageNoNull && pagesizeNull) {
			sql.append(Constant.SUBJECT_PAGENO + ","+ Constant.SUBJECT_PAGESIZE);
		} else if (pageNoNull) {
			sql.append(Constant.SUBJECT_PAGENO + ","+ Integer.valueOf((String) json.get("pagesize")));
		} else if (pagesizeNull) {
			int pageNo = Integer.valueOf((String) json.get("pageNo"));
			if ((pageNo - 1) * Constant.SUBJECT_PAGESIZE > systemInfoList.size()) {
				int max = systemInfoList.size() / Constant.SUBJECT_PAGESIZE;
				int start = max * Constant.SUBJECT_PAGESIZE;
				sql.append(start + "," + Constant.SUBJECT_PAGESIZE);
			} else if (pageNo >= 1) {
				int start = (pageNo - 1) * Constant.SUBJECT_PAGESIZE;
				sql.append(start + "," + Constant.SUBJECT_PAGESIZE);
			} else {
				sql.append(Constant.SUBJECT_PAGENO + ","+ Constant.SUBJECT_PAGESIZE);
			}
		} else {
			int pageNo = Integer.valueOf((String) json.get("pageNo"));
			int pagesize = Integer.valueOf((String) json.get("pagesize"));
			if ((pageNo - 1) * pagesize > systemInfoList.size()) {
				int max = systemInfoList.size() / pagesize;
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
		return extraSpringHibernateTemplate.createSQLQueryFindAll(sql.toString());
	}

	@Override
	public Long queryCountByAcitityId(String activityId) {
		String sql = "select count(user_id) coun from tb_activity_user_relationship where activity_id=?";
		String[] values = { activityId };
		List<Map> map = extraSpringHibernateTemplate.createSQLQueryFindAll(sql,values);
		long coun = Long.valueOf(map.get(0).get("coun").toString());
		return coun;

	}

	public List<Map> queryCountByUserIdAndCity(Map<String, Object> json) {
		String sql = "select id,district from tb_user where id=?";
		String[] values = { (String) json.get("userid") };
		List<Map> list1 = null;
		List<Map> list = extraSpringHibernateTemplate.createSQLQueryFindAll(sql, values);
		for (Map map : list) {
			String district = map.get("district").toString();
			String[] values01 = { district };
			// pageNo前台传参从1开始
			boolean pageNoNull = StringUtil.isNullOrBlank(json.get("pageNo"));
			boolean pagesizeNull = StringUtil.isNullOrBlank(json.get("pagesize"));
			StringBuffer sql01 = new StringBuffer();
			sql01.append("select tb_activity.id,tb_activity.share_url,tb_activity.activity_time,tb_activity.description,tb_activity.district,tb_activity.organiser,tb_activity.show_pic_url,tb_activity.show_type,tb_activity.show_video_url,tb_activity.title,tb_activity_status.enroll_begin_time,tb_activity_status.enroll_end_time,"
					+ "tb_activity_status.prepare_start_time,tb_activity_status.prepare_end_time,tb_activity_status.activity_start_time,"
					+ "tb_activity_status.activity_end_time,tb_activity_status.cancel_time "
					+ "from tb_activity,tb_activity_status "
					+ "where tb_activity_status.activity_id=tb_activity.id and tb_activity.address=?");
			List<Map> systemInfoList = extraSpringHibernateTemplate.createSQLQueryFindAll(sql01.toString(), values01);
			sql01.append(" LIMIT ");
			if (pageNoNull && pagesizeNull) {
				sql01.append(Constant.SUBJECT_PAGENO + ","+ Constant.SUBJECT_PAGESIZE);
			} else if (pageNoNull) {
				sql01.append(Constant.SUBJECT_PAGENO + ","+ Integer.valueOf((String) json.get("pagesize")));
			} else if (pagesizeNull) {
				int pageNo = Integer.valueOf((String) json.get("pageNo"));
				if ((pageNo - 1) * Constant.SUBJECT_PAGESIZE > systemInfoList.size()) {
					int max = systemInfoList.size() / Constant.SUBJECT_PAGESIZE;
					int start = max * Constant.SUBJECT_PAGESIZE;
					sql01.append(start + "," + Constant.SUBJECT_PAGESIZE);
				} else if (pageNo >= 1) {
					int start = (pageNo - 1) * Constant.SUBJECT_PAGESIZE;
					sql01.append(start + "," + Constant.SUBJECT_PAGESIZE);
				} else {
					sql01.append(Constant.SUBJECT_PAGENO + ","+ Constant.SUBJECT_PAGESIZE);
				}
			} else {
				int pageNo = Integer.valueOf((String) json.get("pageNo"));
				int pagesize = Integer.valueOf((String) json.get("pagesize"));
				if ((pageNo - 1) * pagesize > systemInfoList.size()) {
					int max = systemInfoList.size() / pagesize;
					int start = max * pagesize;
					sql01.append(start + "," + pagesize);
				} else if (pageNo >= 1) {
					int start = (pageNo - 1) * pagesize;
					int end = pageNo * pagesize;
					sql01.append(start + "," + end);
				} else {
					sql01.append(Constant.SUBJECT_PAGENO + "," + pagesize);
				}
			}
			list1 = extraSpringHibernateTemplate.createSQLQueryFindAll(sql01.toString(), values01);
		}
		return list1;
	}

	public List<Map> queryCountByUserIdAndEnd(Map<String, Object> json) {

		// pageNo前台传参从1开始
		boolean pageNoNull = StringUtil.isNullOrBlank(json.get("pageNo"));
		boolean pagesizeNull = StringUtil.isNullOrBlank(json.get("pagesize"));
		StringBuffer sql = new StringBuffer();
		sql.append("select tb_activity.id,tb_activity.share_url,tb_activity.activity_time,tb_activity.description,tb_activity.district,tb_activity.organiser,tb_activity.show_pic_url,tb_activity.show_type,tb_activity.show_video_url,tb_activity.title,tb_activity_status.enroll_begin_time,tb_activity_status.enroll_end_time,"
				+ "tb_activity_status.prepare_start_time,tb_activity_status.prepare_end_time,tb_activity_status.activity_start_time,"
				+ "tb_activity_status.activity_end_time,tb_activity_status.cancel_time "
				+ "from tb_activity,tb_activity_status "
				+ "where tb_activity_status.activity_id=tb_activity.id  and tb_activity.activity_status=0");
		List<Map> systemInfoList = extraSpringHibernateTemplate.createSQLQueryFindAll(sql.toString());
		sql.append(" LIMIT ");
		if (pageNoNull && pagesizeNull) {
			sql.append(Constant.SUBJECT_PAGENO + ","+ Constant.SUBJECT_PAGESIZE);
		} else if (pageNoNull) {
			sql.append(Constant.SUBJECT_PAGENO + ","+ Integer.valueOf((String) json.get("pagesize")));
		} else if (pagesizeNull) {
			int pageNo = Integer.valueOf((String) json.get("pageNo"));
			if ((pageNo - 1) * Constant.SUBJECT_PAGESIZE > systemInfoList.size()) {
				int max = systemInfoList.size() / Constant.SUBJECT_PAGESIZE;
				int start = max * Constant.SUBJECT_PAGESIZE;
				sql.append(start + "," + Constant.SUBJECT_PAGESIZE);
			} else if (pageNo >= 1) {
				int start = (pageNo - 1) * Constant.SUBJECT_PAGESIZE;
				sql.append(start + "," + Constant.SUBJECT_PAGESIZE);
			} else {
				sql.append(Constant.SUBJECT_PAGENO + ","+ Constant.SUBJECT_PAGESIZE);
			}
		} else {
			int pageNo = Integer.valueOf((String) json.get("pageNo"));
			int pagesize = Integer.valueOf((String) json.get("pagesize"));
			if ((pageNo - 1) * pagesize > systemInfoList.size()) {
				int max = systemInfoList.size() / pagesize;
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
		return extraSpringHibernateTemplate.createSQLQueryFindAll(sql.toString());
	}

	@Override
	public List<Map> queryActivityComment(Map<String, Object> json) {
		String[] values = { (String) json.get("courseOrActivityId") };
		// pageNo前台传参从1开始
		boolean pageNoNull = StringUtil.isNullOrBlank(json.get("pageNo"));
		boolean pagesizeNull = StringUtil.isNullOrBlank(json.get("pagesize"));
		StringBuffer sql = new StringBuffer();
		sql.append("select tb_user.id userId,tb_comment.id,tb_user.username,tb_user.pic_save_url,tb_comment.comm_content,tb_comment.comm_time from tb_comment,tb_user where tb_comment.user_id=tb_user.id and tb_comment.comm_content_id=? and tb_comment.commed_type=3 order by tb_comment.comm_time desc");
		List<Map> participationList = extraSpringHibernateTemplate.createSQLQueryFindAll(sql.toString(), values);
		sql.append(" LIMIT ");
		if (pageNoNull && pagesizeNull) {
			sql.append(Constant.SUBJECT_PAGENO + ","+ Constant.SUBJECT_PAGESIZE);
		} else if (pageNoNull) {
			sql.append(Constant.SUBJECT_PAGENO + ","+ Integer.valueOf((String) json.get("pagesize")));
		} else if (pagesizeNull) {
			int pageNo = Integer.valueOf((String) json.get("pageNo"));
			if ((pageNo - 1) * Constant.SUBJECT_PAGESIZE > participationList.size()) {
				int max = participationList.size() / Constant.SUBJECT_PAGESIZE;
				int start = max * Constant.SUBJECT_PAGESIZE;
				sql.append(start + "," + Constant.SUBJECT_PAGESIZE);
			} else if (pageNo >= 1) {
				int start = (pageNo - 1) * Constant.SUBJECT_PAGESIZE;
				sql.append(start + "," + Constant.SUBJECT_PAGESIZE);
			} else {
				sql.append(Constant.SUBJECT_PAGENO + ","+ Constant.SUBJECT_PAGESIZE);
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
	public Long queryActivityLikes(Map<String, Object> json) {
		String sql = "select user_id from tb_like where like_type_id=?";
		String[] values = { (String) json.get("courseorActivityId") };
		return extraSpringHibernateTemplate.createSQLQueryCount(sql, values);
	}

	@Override
	public List<Map> queryActivityStatus(String activityId,Map<String, Object> json) {
		String sql = "select distinct status from tb_activity_user_relationship where activity_id=? and user_id=?";
		String[] values = { activityId, (String) json.get("userid") };
		return extraSpringHibernateTemplate.createSQLQueryFindAll(sql, values);
	}

	@Override
	public Long queryActivityComment(String userId) {
		String sql = "select id from tb_comment where tb_comment.commed_id=? ";
		String[] values = { userId };
		return extraSpringHibernateTemplate.createSQLQueryCount(sql, values);
	}

	@Override
	public MobileMessage deleteCommentById(Map<String, Object> json) {
//		extraSpringHibernateTemplate.deleteFirstOneByPropEq(Comment.class,"id", json.get("commentId"));
		String userId = json.get(Send.USERID_NAME).toString();
		Comment comment = od.getObjById(Comment.class, json.get("commentId").toString());
		if(!userId.trim().equals(comment.getUser_id().trim()))
			return MobileMessageCondition.addCondition(false, 5, "别人的评论不能删除哦！", "");
		if(comment!=null){
			od.myDeleteById(Comment.class, comment.getId().toString());
			// 减少统计量
			if(comment.getCommed_type()==Statics.COMMENT_LEVEL_ONE){
				
				int staticsType = -1;
				
				switch(staticsType){
					case Statics.COMMENT_TYPE_ACTIVITY:
						staticsType = Statics.STATICS_TYPE_HD;
						break;
					case Statics.COMMENT_TYPE_COURSE:
						staticsType = Statics.STATICS_TYPE_KC;
						break;
					case Statics.COMMENT_TYPE_TEACHER:
						staticsType = Statics.STATICS_TYPE_JS;
						break;
					case Statics.COMMENT_TYPE_VIDEO:
						staticsType = Statics.STATICS_TYPE_SP;
						break;
				}
				
				if(staticsType!=-1)
					is.subStatistics(Statics.STATISTICS_COMMENT_COUNT, staticsType, comment.getCommed_id());
			}
		}
		return MobileMessageCondition.addCondition(true, 0, "删除成功", "");
	}

	@Override
	public Long queryVideoPlayCount(String activityId) {
		String sql = "select id from tb_user_video_request where tb_user_video_request.teacher_id=?";
		String[] values = { activityId };
		return extraSpringHibernateTemplate.createSQLQueryCount(sql, values);
	}

	@Override
	public Long queryTeacherDetail(Map<String, Object> json) {
		String sql01 = "select tb_video.id from tb_teacher,tb_video,tb_user,tb_course where tb_user.id = tb_teacher.teacher_id and tb_course.teacher_id=tb_user.id and tb_course.video_id=tb_video.id and tb_user.id=?";
		String[] values = { (String) json.get("userid") };
		return extraSpringHibernateTemplate.createSQLQueryCount(sql01, values);
	}

	@Override
	public Long queryTeacherVideo(Map<String, Object> json) {
		String sql01 = "select sum(tb_statistics.click_count),sum(tb_statistics.click_expect_count) from tb_statistics,tb_video,tb_teacher,tb_user,tb_course where tb_user.id= tb_teacher.teacher_id and tb_statistics.type=3 and tb_statistics.type_id=tb_video.id and tb_course.video_id=tb_video.id and tb_course.teacher_id=tb_user.id and tb_user.id=?";
		String[] values = { (String) json.get("userid") };
		Long videoClickCount = 0l;
		List<Map> teacherVideo = extraSpringHibernateTemplate.createSQLQueryFindAll(sql01, values);
		for (Map map : teacherVideo) {
			Long clickCount = Long.parseLong(map.get("sum(tb_statistics.click_count)").toString());// 点击量
			Long clickExpectCount = Long.parseLong(map.get("sum(tb_statistics.click_expect_count)").toString());// 虚拟点击量
			if (clickCount > clickExpectCount) {
				videoClickCount = clickCount;
			} else {
				videoClickCount = clickExpectCount;
			}
		}
		return videoClickCount;
	}

	@Override
	public Long queryTeacherVideo01(Map<String, Object> json) {
//		String sql01 = "select sum(tb_statistics.click_count),sum(tb_statistics.click_expect_count) from tb_order,tb_video,tb_teacher,tb_user,tb_course,tb_statistics where tb_statistics.type=3 and tb_statistics.type_id=tb_video.id and tb_order.course_id = tb_course.id and tb_course.video_id=tb_order.video_id and tb_user.id=tb_teacher.teacher_id and tb_course.teacher_id=tb_teacher.teacher_id and tb_order.video_id = tb_video.id and  tb_order.status=1 and tb_user.id=?";
		String sql01 = "select count(`order`.`id`) as `count` "
						+ "from `tb_order` as `order` "
						+ "LEFT JOIN `tb_video` as `video` on `video`.`id` = `order`.`video_id` "
						+ "LEFT JOIN `tb_course` as `course` on `course`.`id` = `video`.`course_id` "
						+ "where `order`.`status` = " + Constant.PAY_STATUS_NOPAY + " and `course`.`teacher_id` = ? "
						+ "GROUP BY `order`.`id`";
		String[] values = { (String) json.get("userid") };
		Long videoClickCount = (long) 0;
		List<Map> teacherVideo = extraSpringHibernateTemplate.createSQLQueryFindAll(sql01, values);
		if(teacherVideo!=null&&teacherVideo.size()>0){
			Map map = teacherVideo.get(0);
			if(map!=null&&map.size()>0){
				videoClickCount = map.get("count")==null?0:Long.parseLong(map.get("count").toString());
			}
		}
//		for (Map map : teacherVideo) {
//			Long clickCount = Long.parseLong(map.get("sum(tb_statistics.click_count)").toString());// 点击量
//			Long clickExpectCount = Long.parseLong(map.get("sum(tb_statistics.click_expect_count)").toString());// 虚拟点击量
//			if (clickCount > clickExpectCount) {
//				videoClickCount = clickCount;
//			} else {
//				videoClickCount = clickExpectCount;
//			}
//		}
		return videoClickCount;
	}

	@Override
	public Long queryCountByAcitity(Map<String, Object> json) {
		String sql01 = "select count(1) from tb_follow where tb_follow.follow_type=3 and tb_follow.follow_type_id=?";
		String[] values = { (String) json.get("userid") };
		return extraSpringHibernateTemplate.createSQLQueryCount(sql01, values);
	}

	@Override
	public List<Map> queryCountFuFeiCount(Map<String, Object> json) {
		String sql = "select sum(tb_order.price) from tb_order,tb_user,tb_teacher where tb_user.id=tb_teacher.teacher_id and tb_order.user_id=tb_teacher.teacher_id and tb_user.id =?";
		String[] values = { (String) json.get("userid") };
		return extraSpringHibernateTemplate.createSQLQueryFindAll(sql, values);
	}

	@Override
	public List<Map> queryTeacher(Map<String, Object> json) {
		String sql = "select tb_teacher.id,"
//							+ "tb_teacher.appearance_fee,"
							+ "tb_teacher.worth "
						+ "from tb_teacher "
						+ "where teacher_id=?";
		String[] values = { (String) json.get("userid") };
		return extraSpringHibernateTemplate.createSQLQueryFindAll(sql, values);
	}

	@Override
	public Map queryTeacherInfo(Map<String, Object> json) {
		String sql = "select tb_user.id, tb_teacher.name,tb_teacher.level,tb_user.pic_save_url,tb_user.job,sum(tb_point.point_quantum) from tb_teacher,tb_user,tb_point where tb_point.user_id = tb_user.id and tb_teacher.teacher_id=tb_user.id and tb_user.id=?";
		String[] values = { (String) json.get("userid") };
		return extraSpringHibernateTemplate.createSQLQueryFindFirstOne(sql,values);
	}

	@Override
	public Float queryTeacherIncomeMonth(Map<String, Object> json) {
		String sql = "SELECT sum(tb_order.price)*(tb_teacher.percent) from tb_order,tb_user,tb_video,tb_course,tb_teacher WHERE tb_video.id=tb_course.video_id and tb_course.teacher_id=tb_user.id and tb_teacher.teacher_id=tb_user.id  and  DATE_FORMAT( tb_order.pay_time, '%Y%m' ) = DATE_FORMAT( CURDATE( ) , '%Y%m' ) and tb_user.id=? and tb_order.status=1";
		String[] values = { (String) json.get("userid") };
		Map map = extraSpringHibernateTemplate.createSQLQueryFindFirstOne(sql,values);
		String toString = map.get("sum(tb_order.price)*(tb_teacher.percent)").toString();
		return Float.parseFloat(map.get("sum(tb_order.price)*(tb_teacher.percent)").toString());
	}

	@Override
	public Float queryBeforeIncomeMonth(Map<String, Object> json) {
		String sql = "SELECT sum(tb_order.price)*(tb_teacher.percent) FROM tb_order,tb_video,tb_teacher,tb_course,tb_user WHERE tb_order.video_id=tb_video.id and tb_video.id=tb_course.video_id and tb_course.teacher_id=tb_teacher.teacher_id and tb_teacher.teacher_id= tb_user.id   and PERIOD_DIFF( date_format( now( ) , '%Y%m' ) , date_format( tb_order.pay_time, '%Y%m' ) ) =1 and tb_user.id=? and tb_order.status=1";
		String[] values = { (String) json.get("userid") };
		Map map = extraSpringHibernateTemplate.createSQLQueryFindFirstOne(sql,values);
		return Float.parseFloat(map.get("sum(tb_order.price)*(tb_teacher.percent)").toString());
	}

	@Override
	public Float queryCurrentSeasonIncome(Map<String, Object> json) {
		String sql = "select sum(tb_order.price)*(tb_teacher.percent) from tb_order,tb_video,tb_teacher,tb_course,tb_user where tb_order.video_id=tb_video.id and tb_video.id=tb_course.video_id and tb_course.teacher_id=tb_teacher.teacher_id and tb_teacher.teacher_id= tb_user.id and QUARTER(tb_order.pay_time)=QUARTER(now()) and tb_user.id=? and tb_order.status=1";
		String[] values = { (String) json.get("userid") };
		Map map = extraSpringHibernateTemplate.createSQLQueryFindFirstOne(sql,values);
		return Float.parseFloat(map.get("sum(tb_order.price)*(tb_teacher.percent)").toString());
	}

	@Override
	public Float queryBeforeSeasonIncome(Map<String, Object> json) {
		String sql = "select sum(tb_order.price)*(tb_teacher.percent) from tb_order,tb_video,tb_teacher,tb_course,tb_user where tb_order.video_id=tb_video.id and tb_video.id=tb_course.video_id and tb_course.teacher_id=tb_teacher.teacher_id and tb_teacher.teacher_id= tb_user.id    and QUARTER(tb_order.pay_time)=QUARTER(DATE_SUB(now(),interval 1 QUARTER)) and tb_user.id=? and tb_order.status=1";
		String[] values = { (String) json.get("userid") };
		Map map = extraSpringHibernateTemplate.createSQLQueryFindFirstOne(sql,values);
		String toString = map.get("sum(tb_order.price)*(tb_teacher.percent)").toString();
		return Float.parseFloat(map.get("sum(tb_order.price)*(tb_teacher.percent)").toString());
	}

	@Override
	public Float queryIncomeAll(Map<String, Object> json) {
		String sql = "SELECT sum(tb_order.price)*(tb_teacher.percent) from tb_order,tb_user,tb_video,tb_course,tb_teacher WHERE tb_video.id=tb_course.video_id and tb_course.teacher_id=tb_user.id and tb_teacher.teacher_id=tb_user.id and tb_user.id=? and tb_order.status=1";
		String[] values = { (String) json.get("userid") };
		Map map = extraSpringHibernateTemplate.createSQLQueryFindFirstOne(sql,values);
		return Float.parseFloat(map.get("sum(tb_order.price)*(tb_teacher.percent)").toString());
	}

	@Override
	public Map queryQianDao(Map<String, Object> json) {
		String sql = "select max(tb_point.get_time),tb_point.user_id,sum(tb_point.point_quantum) coun "
						+ "from tb_point where tb_point.user_id=?";
		String[] values = { (String) json.get("userid") };
		return extraSpringHibernateTemplate.createSQLQueryFindFirstOne(sql,values);
	}

	@Override
	public void insertUserSuggest(Map<String, Object> json) {
		Suggestion suggestion = new Suggestion();
		suggestion.setContent(json.get("content").toString());
		suggestion.setPhone_num(Long.parseLong(json.get("phoneNum").toString()));
		suggestion.setUser_id(json.get("userid").toString());
		suggestion.setStatus(0);
		suggestion.setGenre(Integer.valueOf(json.get("genre").toString()));
		suggestion.setBack_time(System.currentTimeMillis());
		extraSpringHibernateTemplate.getHibernateTemplate().save(suggestion);
	}
	@Override
	public List<SuggestionType> suggestionList(Map<String, Object> json) {
		 List<SuggestionType> list=	extraSpringHibernateTemplate.findAllByPropEq(SuggestionType.class, "status", 1);
		return list;
	}
	@Override
	public List<Map> queryActivityBanner() {
		String sql = "select id,name,order_num,pic_redirect_url,jump_type,jump_id,create_time,pic_save_url,remark,update_time from tb_banner where status = ? and jump_type=?";
		String[] values = { "1", "2" };
		return extraSpringHibernateTemplate.createSQLQueryFindAll(sql, values);
	}

	@Override
	public Map queryVideoById(Map<String, Object> json) {
	String sql="select tb_video.id,tb_video.title,tb_video.per_cost,tb_video.remark from tb_video where tb_video.id=?";
	String[] values={(String) json.get("videoId")};
		return extraSpringHibernateTemplate.createSQLQueryFindFirstOne(sql, values);
	}

	@Override
	public Map queryCouponByNumber(Map<String, Object> json) {
		String sql="select tb_coupon.id,tb_coupon.denomination from tb_coupon,tb_coupon_user where tb_coupon.id=tb_coupon_user.coupon_id and tb_coupon_user.user_id=? and tb_coupon_user.number=? and tb_coupon_user.status=1 and tb_coupon_user.use_type_id BETWEEN 0 and 1 and tb_coupon.coupon_type = ?";
		String[] values={(String) json.get("userid"),(String) json.get("number"),(String) json.get("couponType")};
		return extraSpringHibernateTemplate.createSQLQueryFindFirstOne(sql, values);
	}

	@Override
	public void addCommentsForCourse(Map<String, Object> json) {
	Comment comment=new Comment();
	comment.setComm_content(json.get("commContent").toString());
	comment.setUser_id(json.get("userid").toString());
	comment.setComm_content_id(json.get("commedId").toString());
	comment.setComm_time(Long.valueOf(new SimpleDateFormat("yyyyMMddHHmmss").format(new Date())));
	comment.setUser_id(json.get("userid").toString());
	if(json.get("type").equals("1")){
		//课程评论
		comment.setCommed_type(0);
	}else if(json.get("type").equals("2")){
		comment.setCommed_type(3);
	}
	extraSpringHibernateTemplate.getHibernateTemplate().save(comment);
	}

	@Override
	public List<Map> queryRequestVideo(Map<String, Object> json) {
		String[] values = { (String) json.get("userid") };
		// pageNo前台传参从1开始
		boolean pageNoNull = StringUtil.isNullOrBlank(json.get("pageNo"));
		boolean pagesizeNull = StringUtil.isNullOrBlank(json.get("pagesize"));
		StringBuffer sql = new StringBuffer();
		sql.append(
					"select tb_course.id,tb_video.id videoId,tb_teacher.teacher_id teacherId,tb_course.title," 
					+ "		tb_course.remark,tb_teacher.name,tb_teacher.level,tb_course.pic_big_url," 
					+ "		sum(tb_user_video_request.vote) vote,tb_course.share_url " 
					+ "from tb_user_video_request, tb_video, tb_course, tb_teacher " 
					+ "where  tb_user_video_request.teacher_id = tb_course.teacher_id " 
					+ "		and tb_course.video_id = tb_video.id " 
					+ "		and tb_user_video_request.user_id = ? "
					);
		List<Map> participationList = extraSpringHibernateTemplate.createSQLQueryFindAll(sql.toString(), values);
		sql.append(" LIMIT ");
		if (pageNoNull && pagesizeNull) {
			sql.append(Constant.SUBJECT_PAGENO + ","+ Constant.SUBJECT_PAGESIZE);
		} else if (pageNoNull) {
			sql.append(Constant.SUBJECT_PAGENO + ","+ Integer.valueOf((String) json.get("pagesize")));
		} else if (pagesizeNull) {
			int pageNo = Integer.valueOf((String) json.get("pageNo"));
			if ((pageNo - 1) * Constant.SUBJECT_PAGESIZE > participationList.size()) {
				int max = participationList.size() / Constant.SUBJECT_PAGESIZE;
				int start = max * Constant.SUBJECT_PAGESIZE;
				sql.append(start + "," + Constant.SUBJECT_PAGESIZE);
			} else if (pageNo >= 1) {
				int start = (pageNo - 1) * Constant.SUBJECT_PAGESIZE;
				sql.append(start + "," + Constant.SUBJECT_PAGESIZE);
			} else {
				sql.append(Constant.SUBJECT_PAGENO + ","+ Constant.SUBJECT_PAGESIZE);
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
	public void addVote(Map<String, Object> json) {
		UserVideoRequest userVideoRequest=new UserVideoRequest();
		userVideoRequest.setUser_id(json.get("userid").toString());
		userVideoRequest.setVote(1);
		userVideoRequest.setTeacher_id(json.get("teacherId").toString());
		extraSpringHibernateTemplate.getHibernateTemplate().save(userVideoRequest);
		pointService.pointVote(json.get("userid").toString());//添加积分
	}

	@Override
	public void editUserDistrict(Map<String, Object> json) {
		User user = extraSpringHibernateTemplate.findFirstOneByPropEq(User.class, "id", json.get("userid"));
		user.setDistrict(json.get("district").toString());
		extraSpringHibernateTemplate.getHibernateTemplate().update(user);
	}

	@Override
	public void editUserJob(Map<String, Object> json) {
		User user = extraSpringHibernateTemplate.findFirstOneByPropEq(User.class, "id", json.get("userid"));
		user.setJob(json.get("job").toString());
		extraSpringHibernateTemplate.getHibernateTemplate().update(user);
		
	}

	@Override
	public List<Map> queryJobList() {
		String sql="select `id`,`job_name` from `tb_job` where `type` = " + Statics.JOB_TYPE_ZHIYE + " order by `orderInt` asc";
		return extraSpringHibernateTemplate.createSQLQueryFindAll(sql);
	}

	@Override
	public User queryUserByPhone(String phone) {
		return extraSpringHibernateTemplate.findFirstOneByPropEq(User.class, "phone", phone);
	}

	@Override
	public MobileMessage ssoLogin(String ssoId, String ssoName,String address,String picUrl,String registId,int phoneType, int type, int sex) throws Exception {
		// 通过ssoId去查看有没有这个用户
		User user = extraSpringHibernateTemplate.findFirstOneByPropEq(User.class, new String[]{"ssoId","vchat_iden"}, new Object[]{ssoId,type});
		if(user==null){
			// 还没有登陆过
			ssoName = CleanUtil.filterEmoji(ssoName, "");
			long now = System.currentTimeMillis();
			user = new User();
			user.setBirthday((long) 0);	// 生日
			user.setBuy_count(0);	// 购买视频次数
			user.setContinue_day(0);		// 连续签到天数
			user.setCreate_time(now);		// 创建时间
			user.setDistrict(address);				// 第三方软件上填写的地址
			user.setEqui_type(phoneType);			// 用户最后登陆的设备类型
			user.setGender(sex);		// 性别
			user.setJob("");			// 职业
			user.setLatest_login_time(now);	// 最后登陆时间
			user.setLatest_sign_time((long) 0);		// 最后签到时间
			user.setPassword("");					// 密码
			user.setPhone((long) 0);				// 电话
			user.setPic_save_url(picUrl);			// 头像地址
			user.setPush_token(registId);				// registId
			user.setSsoId(ssoId);						// 第三方登陆唯一标识
			user.setUpdate_time(now);					// 更新时间
			user.setUser_state(Constant.YES_INT);		// 用户状态，可用或者不可用
			user.setUser_type(Constant.USER_TYPE_STUDENT);		// 用户类型，学员或者讲师
			user.setUsername(ssoName);					// 用户昵称
			user.setVchat_iden(type);					// 用户类型，普通登陆用户或者微信登陆用户
			user.setZodiac("");							// 星座
			
			Serializable serId = extraSpringHibernateTemplate.getHibernateTemplate().save(user);
			if(serId==null)
				return MobileMessageCondition.addCondition(false, -4, "数据库异常", null);
			user.setId(serId.toString());
			// 注册送积分
			MobileMessage addPointResult1 = pointService.addPoint(user, PointEachType.USER_CREATE_POINT,0);
			if(addPointResult1.isResult())
				ps.pushEveryOne("", "注册送积分", Statics.PUSH_TYPE_FORTHWITH_NOTABLE, "恭喜您成功注册获得了" + addPointResult1.getResponse() + "个积分", null, user.getId().toString());
		}
		
		if(user.getUser_state()!=Constant.YES_INT)
			return MobileMessageCondition.addCondition(false, 8, "该用户已经被拉黑，请联系管理员！","");
		
		// 登陆送积分
		MobileMessage addPointResult2 = pointService.addPoint(user, PointEachType.DAY_FIRST_POINT,0);
		if(addPointResult2.isResult())
			ps.pushEveryOne("", "签到送积分", Statics.PUSH_TYPE_FORTHWITH_NOTABLE, "恭喜您今天首次登陆获得了" + addPointResult2.getResponse() + "个积分，当前已经连续签到" + user.getContinue_day() + "天", null, user.getId().toString());
		return MobileMessageCondition.addCondition(true, 0, "登陆成功！",user);
	}

	@Override
	public int getPointsByUserId(String userId) {
		String sql = "select sum(`point_unused`) as `points` "
						+ "from `tb_point` "
						+ "where `user_id` = ? and `status` = " + Constant.YES_INT;
		Map<String,Object> map = extraSpringHibernateTemplate.createSQLQueryFindFirstOne(sql, new Object[]{userId});
		if(map==null||map.size()<1||map.get("points")==null)
			return 0;
		return Integer.parseInt(map.get("points").toString());
	}

	@Override
	public MobileMessage createUser(long phone, String password, String district) throws Exception {
		long now = System.currentTimeMillis();
		User user = new User();
		user.setBirthday((long) 0);
		user.setBuy_count(0);
		user.setContinue_day(0);
		user.setCreate_time(now);
		user.setDistrict(district);
		user.setEqui_type(-1);
		user.setGender(Constant.USER_SEX_UNKNOW);
		user.setJob("");
		user.setLatest_login_time((long) 0);
		user.setLatest_sign_time((long) 0);
		user.setPassword(MD5Util.encrypt(password));
		user.setPhone(phone);
		user.setPic_save_url(null);
		user.setPush_token(null);
		user.setSsoId(null);
		user.setUpdate_time(now);
		user.setUser_state(Constant.YES_INT);
		user.setUser_type(Constant.USER_TYPE_STUDENT);
		String userName=(String.valueOf(phone)).substring(7, 11);
		user.setUsername(userName);
		user.setVchat_iden(UserType.USER_TYPE_GENERAL.getType());
		user.setZodiac(null);
		
//		Map<String,Object> userMap = new HashMap<String,Object>();
//		userMap.put("create_time", now);
//		userMap.put("birthday", 0);
//		userMap.put("buy_count", 0);
//		userMap.put("continue_day", 0);
//		userMap.put("district", district);
//		userMap.put("equi_type", -1);
//		userMap.put("gender", Constant.USER_SEX_UNKNOW);
//		userMap.put("job", "");
//		userMap.put("latest_login_time", 0);
//		userMap.put("latest_sign_time", 0);
//		userMap.put("password", MD5Util.encrypt(password));
//		userMap.put("phone", phone);
//		userMap.put("update_time", now);
//		userMap.put("user_state", Constant.YES_INT);
//		userMap.put("user_type", Constant.USER_TYPE_STUDENT);
//		userMap.put("username", Constant.USER_USERNAME_DEFAULT);
//		userMap.put("vchat_iden", UserType.USER_TYPE_GENERAL.getType());
		
//		Serializable serId = extraSpringHibernateTemplate.getHibernateTemplate().save(user);
//		Serializable serId = od.save(user);
		Serializable serId = od.save(user);
		
		if(serId==null)
			return MobileMessageCondition.addCondition(false, -4, "数据库异常", null);

		user.setId(serId.toString());
		
		MobileMessage addPointResult = pointService.addPoint(user, PointEachType.USER_CREATE_POINT,0);
		if(!addPointResult.isResult())
			return addPointResult;
		
		return MobileMessageCondition.addCondition(true, 0, "注册成功！",user);
	}
	
	@Override
	public Code getTeacherDetail(String teacherId) throws Exception {
		// 验证该讲师是否存在，不存在即不再去查询相关信息
		Map<String,Object> params = new HashMap<String,Object>();
		String sql = "select `id` from `tb_teacher` where `teacher_id` = :teacherId and `status` = " + Constant.YES_INT;
		params.put("teacherId", teacherId);
		Map<String, Object> teacher = od.getObjectBySql(sql, params);
		if(teacher==null)
			return Code.init(false, 8, "该讲师不存在！");
		
		// 创建用于保存结果集的map
		Map<String,Object> result = new HashMap<String,Object>();
		
		// 获得讲师收益信息
		// 当前时间
		long now = System.currentTimeMillis();
		Calendar c = Calendar.getInstance();
		c.setTimeInMillis(now);
		c.set(Calendar.DAY_OF_MONTH, 1);
		c.set(Calendar.HOUR_OF_DAY, 0);
		c.set(Calendar.MINUTE, 0);
		c.set(Calendar.SECOND, 0);
		// 当月1号的00:00:00的时间撮
		long monthFirst = c.getTimeInMillis();
		// 当前月份，从0-11
		int nowMonth = c.get(Calendar.MONTH);
		c.add(Calendar.MONTH, -1);
		// 上个月1号的00:00:00的时间撮
		long lastMonthFirst = c.getTimeInMillis();
		// 本季度第一个月份，从0-11
		int firstMonthForQuarter = DateUtil.getFirstMonthForQuarter(nowMonth);
		c.set(Calendar.MONTH, firstMonthForQuarter);
		// 本季度第一个月1号的00:00:00的时间撮
		long nowQuarterFirst = c.getTimeInMillis();
		c.add(Calendar.MONTH, -3);
		// 上季度第一个月1号的00:00:00的时间撮
		long lastQuarterFirst = c.getTimeInMillis();
		
		String shouyi = "select "
								// 本月收益
								+ "(select ifnull(sum(`teacher_achieve`),0) from `tb_teacher_order` where `teacher_id` = :teacherId and `status` = " + Constant.YES_INT + " and `create_time` >= " + monthFirst + " and `create_time`< " + now + ") as `incomeMonth`,"
								// 上个月收益
								+ "(select ifnull(sum(`teacher_achieve`),0) from `tb_teacher_order` where `teacher_id` = :teacherId and `status` = " + Constant.YES_INT + " and `create_time` >= " + lastMonthFirst + " and `create_time`< " + monthFirst + ") as `beforeIncomeMonth`,"
								// 本季度收益
								+ "(select ifnull(sum(`teacher_achieve`),0) from `tb_teacher_order` where `teacher_id` = :teacherId and `status` = " + Constant.YES_INT + " and `create_time` >= " + nowQuarterFirst + " and `create_time`< " + now + ") as `currenSeasonIncome`,"
								// 上个季度收益
								+ "(select ifnull(sum(`teacher_achieve`),0) from `tb_teacher_order` where `teacher_id` = :teacherId and `status` = " + Constant.YES_INT + " and `create_time` >= " + lastQuarterFirst + " and `create_time`< " + nowQuarterFirst + ") as `beforeSeasonIncome`,"
								// 总收益
								+ "(select ifnull(sum(`teacher_achieve`),0) from `tb_teacher_order` where `teacher_id` = :teacherId and `status` = " + Constant.YES_INT + ") as `incomeAll` "
						+ "from dual";
		Map<String,Object> shouyiMap = od.getObjectBySql(shouyi, params);
		result.putAll(shouyiMap);
		
		
		// 查询出有关视频的相关信息
		// 查询出有关数量的信息
		String videoCount = "select "
								// 视频总量
								+ "ifnull(count(DISTINCT `video`.`id`),0) as `videoCountAll`,"
								// 该讲师下视频总付费量
								+ "ifnull(count(DISTINCT `order`.`id`),0) as `totalChargeCount`,"
								// 该讲师下视频的总付费额
								+ "ifnull(sum(`order`.`price`),0) as `fuFeiMoney` "
								+ "from `tb_video` as `video` "
								+ "LEFT JOIN `tb_course` as `course` on `course`.`id` = `video`.`course_id` "
								+ "LEFT JOIN `tb_teacher` as `teacher` on `teacher`.`teacher_id` = `course`.`teacher_id` "
								+ "LEFT JOIN `tb_order` as `order` on `order`.`video_id` = `video`.`id` and `order`.`type` = " + Statics.ORDER_TYPE_VIDEO + " and `order`.`status` = " + Constant.PAY_STATUS_PAYED + " "
								+ "where `teacher`.`teacher_id` = :teacherId "
									+ "and `teacher`.`status` = " + Constant.YES_INT + " ";
		Map<String,Object> videoCountMap = od.getObjectBySql(videoCount, params);
		int payCount = Integer.parseInt(videoCountMap.get("totalChargeCount").toString());
		// 查看该讲师下视频的总点击量
		String clickCountSql = "select "
								+ "ifnull(sum(`statics`.`click_count` + `statics`.`click_expect_count`),0) as `sumClick` "
								+ "from `tb_statistics` as `statics` "
								+ "LEFT JOIN `tb_video` as `video` on `video`.`id` = `statics`.`type_id` "
								+ "LEFT JOIN `tb_course` as `course` on `course`.`id` = `video`.`course_id` "
								+ "LEFT JOIN `tb_teacher` as `teacher` on `teacher`.`teacher_id` = `course`.`teacher_id` "
								+ "where `teacher`.`teacher_id` = :teacherId "
									+ "and `teacher`.`status` = " + Constant.YES_INT + " "
									+ "and `statics`.`type` = " + Statics.STATICS_TYPE_SP + " ";
		Map<String,Object> clickCountMap = od.getObjectBySql(clickCountSql, params);
		int clickCount = clickCountMap==null?0:Integer.parseInt(clickCountMap.get("sumClick").toString());
		if(clickCount==0&&payCount==0){
			videoCountMap.put("videoClickRate", 0);
		}else{
			videoCountMap.put("videoClickRate", MathUtil.toAny(2, (payCount*100.0/clickCount)));
		}
		result.putAll(videoCountMap);
		
		
		// 获取讲师的粉丝量，身价，出场费等信息
		String teacherSql = "select "
								+ "`teacher`.`worth` as `worth`,"
								+ "`teacher`.`level` as `level`,"
								+ "`teacher`.`head_url` as `picSaveUrl`,"
								+ "`teacher`.`name` as `name`,"
								+ "(select ifnull(sum(`appearance`),0) from `tb_activity_user_relationship` where `user_id` = :teacherId and `man_type` = " + Statics.ACTIVITY_USER_TYPE_TOP + ") as `appearanceFee`,"
								+ "ifnull(count(DISTINCT `follow`.`user_id`),0) as `fensiCount` "
								+ "from `tb_teacher` as `teacher` "
								+ "LEFT JOIN `tb_follow` as `follow` on `follow`.`follow_type_id` = `teacher`.`teacher_id` and `follow`.`follow_type` = 3 "
								+ "where `teacher`.`teacher_id` = :teacherId "
									+ "and `teacher`.`status` = " + Constant.YES_INT + " ";
		Map<String,Object> teacherDetail = od.getObjectBySql(teacherSql, params);
		// 下面俩字段是为了符合原接口文档中的返回体格式，是不会用到的字段
		teacherDetail.put("job", "");
		teacherDetail.put("points", 0);
		result.putAll(teacherDetail);
		
		return Code.init(true, 0, "", result);
	}

}
