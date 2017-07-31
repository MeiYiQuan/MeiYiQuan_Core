package com.salon.backstage.qcproject.dao.support;

import com.salon.backstage.qcproject.util.Sql;

import java.util.HashMap;
import java.util.Map;

import com.salon.backstage.qcproject.util.Enums.RequestStatus;

/**
 * 作者：齐潮
 * 创建日期：2016年12月27日
 * 类说明：用于拼接有关求课程的sql
 */
public class UserVideoRequestSupport {

	/**
	 * 通过userId去获取该用户发起的所有的求课程
	 * @param userId
	 * @return
	 */
	public final static Sql getRequestsByUserId(String userId){
		String inStr = RequestStatus.REQUEST_STATUS_DOING.getType() + "," + RequestStatus.REQUEST_STATUS_INREVIEW.getType() + ","
						+ RequestStatus.REQUEST_STATUS_DEFEAT.getType() + "," + RequestStatus.REQUEST_STATUS_DOINGDEFEAT.getType() + ","
						+ RequestStatus.REQUEST_STATUS_SUCCESS.getType();
		String sql = "select `uvr`.`id` as `id`,`uvr`.`course_name` as `coursename`,`uvr`.`pic_url` as `url`,"
						+ "		`uvr`.`feedback_status` as `status`,`uvr`.`createdTime` as `createdTime`,"
						+ "		`uvr`.`share_url` as `share_url`,"
						+ "		`teac`.`name` as `teachname`,`uvr`.`question` as `question`,"
						+ "		`uvr`.`request_time` as `requesttime`,`uvr`.`vote` as `vote` "
						+ "from `tb_user_video_request` as `uvr` "
						+ "LEFT JOIN `tb_teacher` as `teac` on `teac`.`teacher_id` = `uvr`.`teacher_id` "
						+ "where `uvr`.`feedback_status` in (" + inStr + ") and `uvr`.`user_id` = :userId "
						+ "group by `uvr`.`id` "
						+ "order by `uvr`.`request_time` desc";
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("userId", userId);
		Sql s = Sql.get(sql, params);
		return s;
	}
	
}
