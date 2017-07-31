package com.salon.backstage.qcproject.dao.support;

import java.util.HashMap;
import java.util.Map;

import com.salon.backstage.qcproject.util.Sql;
import com.salon.backstage.qcproject.util.Statics;

/**
 * 作者：齐潮
 * 创建日期：2016年12月28日
 * 类说明：用于产生关于用户的sql
 */
public class UserSupport {

	/**
	 * 通过视频id去获取发布该视频的用户信息
	 * @param videoId
	 * @return
	 */
	public final static Sql getUserByVideoId(String videoId){
		String sql = "select `user`.*,`teacher`.`percent` as `teachPercent` "
						+ "from `tb_user` as `user` "
						+ "LEFT JOIN `tb_teacher` as `teacher` on `teacher`.`teacher_id` = `user`.`id` "
						+ "LEFT JOIN `tb_course` as `course` on `course`.`teacher_id` = `teacher`.`teacher_id` "
						+ "LEFT JOIN `tb_video` as `video` on `video`.`course_id` = `course`.`id` "
						+ "where `video`.`id` = :videoId "
						+ "GROUP BY `video`.`id` ";
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("videoId", videoId);
		Sql result = Sql.get(sql, params);
		return result;
	}
	
	/**
	 * 获取讲师体现列表
	 * @return
	 */
	public final static Sql getTeacherSends(String teacherId){
		String sql = "select "
							+ "`verify_time` as `time`,"
							+ "`send_money` as `money` "
						+ "from `tb_teacher_send` "
						+ "where `teacher_id` = :teacherId "
							+ "and `status` = " + Statics.TEACHER_SEND_STATUS_OK + " "
						+ "order by `verify_time` desc";
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("teacherId", teacherId);
		Sql s = Sql.get(sql, params);
		return s;
	}
	
}
