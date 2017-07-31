package com.salon.backstage.qcproject.dao.support;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.salon.backstage.pub.bsc.domain.Constant;
import com.salon.backstage.qcproject.util.CleanUtil;
import com.salon.backstage.qcproject.util.Sql;
import com.salon.backstage.qcproject.util.Statics;
import com.salon.backstage.qcproject.util.Enums.ActivityStatus;

/**
 * 作者：齐潮
 * 创建日期：2016年12月28日
 * 类说明：用于产生与活动有关的sql
 */
public class ActivitySupport {

	/**
	 * 测试用的查询条件，将left关联设置成了内链接的格式，防止空指针。正式上线就设置为空字符就可以
	 */
	public final static String TEST_ADD_CONDITION = "and `status`.`id` is not null ";
	
	/**
	 * 用于去直接拼接查询activity状态有关的字段，使用时要保证表的别名一致
	 */
	public final static String ACTIVITY_SELECT_KENAMES = "`active`.`id` as `id`,`active`.`show_video_url` as `show_video_url`,"
															+ "	if(`active`.`show_type` = " + Statics.ACTIVITY_SHOWTYPE_VIDEO + ",`active`.`show_video_picurl`,`active`.`show_pic_url`) as `show_pic_url`,"
															+ "`active`.`title` as `title`,`active`.`description` as `description`,`active`.`district` as `district`,`active`.`part_num` as `partNum`,"
															+ "`active`.`organiser` as `organiser`,`active`.`show_type` as `show_type`,`active`.`share_url` as `share_url`,"
															+ "`active`.address as  address,"
															+ "`status`.`create_time` as `create_time`,`status`.`enroll_begin_time` as `enroll_begin_time`,`status`.`enroll_end_time` as `enroll_end_time`,"
															+ "`status`.`prepare_start_time` as `prepare_start_time`,`status`.`prepare_end_time` as `prepare_end_time`,`status`.`activity_start_time` as `activity_time`,"
															+ "`status`.`activity_end_time` as `activity_end_time`,`status`.`cancel_time` as `cancel_time`,`status`.`isCancel` as `isCancel` ";
	
	
	/**
	 * 对某个活动进行减少参与人数
	 * @param activityId
	 * @param count
	 * @return
	 */
	public final static Sql subPerson(String activityId,int count){
		String sql = "update `tb_activity` set `part_num` = `part_num` - " + count + " where `id` = :activityId";
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("activityId", activityId);
		Sql result = Sql.get(sql, params);
		return result;
	}
	
	/**
	 * 获取某个讲师所有的活动
	 * @param teacherId
	 * @return
	 */
	public final static Sql getActivityByTeacherId(String teacherId){
		/*String sql = "select `activity`.`id` as `id`,"
							+ "`activity`.`title` as `title`,"
							+ "`activity`.`activity_time` as `activity_time`,"
							+ "`activity`.`description` as `description`,"
							+ "`activity`.`district` as `district`,"
							+ "`activity`.`organiser` as `organiser`,"
							+ "if(`activity`.`show_type` = " + Statics.ACTIVITY_SHOWTYPE_PIC + ",`activity`.`show_pic_url`,`activity`.`show_video_picurl`) as `show_pic_url`,"
							+ "`activity`.`part_num` as `activityUserCount` "
						+ "from `tb_activity` as `activity` "
						+ "where `activity`.`teacher_id` = :teacherId "
						+ "order by `activity`.`activity_time` asc";*/
		
		String sql="SELECT `av`.`id` as `id`,"
							+ "`av`.`title` as `title`,"
							+ "`av`.`activity_time` as `activity_time`,"
							+ "`av`.`description` as `description`,"
							+ "`av`.`address` as `district`,"
							+ "`av`.`organiser` as `organiser`"
							+  ",if(`av`.`show_type` = " + Statics.ACTIVITY_SHOWTYPE_PIC + ",`av`.`show_pic_url`,`av`.`show_video_picurl`) as `show_pic_url`"
							+ " FROM tb_user u "
							+ " LEFT JOIN tb_activity_user_relationship a ON u.id=a.user_id"
							+ " LEFT JOIN tb_activity av ON av.id=a.activity_id "
							
							+ " WHERE a.man_type=2"
							+ " and a.user_id = '"+teacherId+"'";
		Map<String,Object> params = new HashMap<String,Object>();
		/*params.put("teacherId", teacherId);*/
		Sql result = Sql.get(sql, params);
		return result;
	}
	
	/**
	 * 将活动所有参与者进行分类存储
	 * @param userList
	 * @param jiabins
	 * @param comeInUsers
	 */
	public final static void cleanActivityUsers(List<Map<String, Object>> userList,List<Map<String, Object>> jiabins,List<Map<String, Object>> comeInUsers){
		if(userList==null)
			return;
		for(int i=0;i<userList.size();i++){
			Map<String, Object> user = userList.get(i);
			int type = Integer.parseInt(user.get("type").toString());
			if(type==Statics.ACTIVITY_USER_TYPE_TOP){
				jiabins.add(user);
			}else{
				comeInUsers.add(user);
			}
		}
	}
	
	
	/**
	 * 获取一个活动下所有的参与者
	 * @param activityId
	 * @return
	 */
	public final static Sql getActivityUsers(String activityId){
		String sql = "select `user`.`id` as `id`,"
							+ "ifnull(`user`.`pic_save_url`,'') as `pic_save_url`,"
							+ "`user`.`username` as `username`,"
							+ "`teacher`.`name` as `name`,"
							+ "`teacher`.`level` as `level`,"
							+ "ifnull(`teacher`.`head_url`,'') as `head_url`,"
							+ "`activity`.`title` as `title`,"
							+ "`ship`.`man_type` as `type` "
						+ "from `tb_activity_user_relationship` as `ship` "
						+ "left join `tb_activity` as `activity` on `activity`.`id` = `ship`.`activity_id` "
						+ "left join `tb_user` as `user` on `ship`.`user_id` = `user`.`id` "
						+ "left join `tb_teacher` as `teacher` on `teacher`.`teacher_id` = `user`.`id` "
						+ "where `ship`.`activity_id` = :activityId "
						+ "group by `ship`.`id` "
						+ "order by `ship`.`man_type` desc,`ship`.`enter_time` asc";
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("activityId", activityId);
		Sql result = Sql.get(sql, params);
		return result;
	}
	
	/**
	 * 获取某个活动的详情
	 * @param activityId
	 * @param userId
	 * @return
	 */
	public final static Sql getActivityDetail(String activityId, String userId){
		String sql = "select `activity`.`id` as `id`,"
							+ "if(`ship`.`id` is null," + Constant.NO_INT + "," + Constant.YES_INT + ") as `isComeIn`,"
							+ "if(`collect`.`id` is null," + Constant.NO_INT + "," + Constant.YES_INT + ") as `isCollect`,"
							+ "`activity`.`title` as `title`,"
							+ "`activity`.`activity_time` as `activity_time`,"
							+ "`activity`.`share_url` as `share_url`,"
							+ "`activity`.`address` as `address`,"
							+ "`activity`.`organiser` as `organiser`,"
							+ "`activity`.`description` as `desc`,"
							+ "`activity`.`remark` as `remark`,"
							+ "`activity`.`price` as `price` , "
							
							+ "`activity`.`show_type` as `show_type`,"
							+ "`activity`.`show_pic_url` as `show_pic_url`,"
							+ "`activity`.`show_video_url` as `show_video_url`,"
							+ "`activity`.`show_video_picurl` as `show_video_picurl` "
							
						+ " from `tb_activity` as `activity` "
						+ "LEFT JOIN `tb_activity_user_relationship` as `ship` on `ship`.`activity_id` = `activity`.`id` and `ship`.`user_id` = :userId "
						+ "LEFT JOIN `tb_collect` as `collect` on `collect`.`collect_type_id` = `activity`.`id` and `collect`.`collect_type` = " + Statics.COLLECT_TYPE_HD + " and `collect`.`user_id` = :userId "
						+ "where `activity`.`id` = :activityId "
						+ "GROUP BY `activity`.`id` ";
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("userId", userId);
		params.put("activityId", activityId);
		Sql result = Sql.get(sql, params);
		return result;
	}
	
	/**
	 * 获取我的参与的sql
	 * @param userId
	 * @return
	 */
	public final static Sql getActivitiesByUserId(String userId){
		String sql = "select `active`.`most_man` as `most_man`,`active`.`price` as `price`," + ActivitySupport.ACTIVITY_SELECT_KENAMES 
						+ "from `tb_activity` as `active` "
						+ "LEFT JOIN `tb_activity_status` as `status` on `status`.`activity_id` = `active`.`id` "
						+ "LEFT JOIN `tb_activity_user_relationship` as `ship` on `ship`.`activity_id` = `active`.`id` "
						+ "where `ship`.`user_id` = :userId " + ActivitySupport.TEST_ADD_CONDITION
						+ "GROUP BY `active`.`id` "
						+ "ORDER BY `status`.`activity_start_time` ASC ";
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("userId", userId);
		Sql s = Sql.get(sql, params);
		return s;
	}
	
	/**
	 * 根据前台传来的类型来获取线下活动列表
	 * @param type
	 * @param userId
	 * @return
	 */
	public final static Sql getActivitiesByType(String userAddress,int type){
		String condition = null;
		switch(type){
			case Statics.ACTIVITY_REQUEST_ALL:
				condition = " and `status`.`activity_end_time`>(UNIX_TIMESTAMP()*1000) ";
				break;
			case Statics.ACTIVITY_REQUEST_DISTRICT:
				condition = "and '" + userAddress + "' like concat('%',`active`.`district`,'%') "
						+ "  and `status`.`activity_end_time`>(UNIX_TIMESTAMP()*1000) ";
				break;
			case Statics.ACTIVITY_REQUEST_ENDED:
				condition = "and `status`.`activity_end_time`<=(UNIX_TIMESTAMP()*1000) ";
				break;
		}
		String sql = "select "
						+ ActivitySupport.ACTIVITY_SELECT_KENAMES
						+ "from `tb_activity` as `active` "
						+ "LEFT JOIN `tb_activity_status` as `status` on `status`.`activity_id` = `active`.`id` "
						+ "where 1 = 1 "
						+ condition + ActivitySupport.TEST_ADD_CONDITION 
						+ "GROUP BY `active`.`id` "
						+ "ORDER BY `status`.`activity_start_time` ASC ";
		Sql s = Sql.get(sql, null);
		return s;
	}
	
	
	/**
	 * 将传入的关于活动的list中每个元素判断一下当前活动的状态，并给予相应的字段来储存。
	 * 使用时需要与ActivitySupport.ACTIVITY_STATUS_KENAMES配合。
	 * 并且会删除map中一些无用字段。
	 * keyName传null会用默认值：好处是在本类中就可以看出此字段会不会与其他字段冲突
	 * @param keyName
	 * @param list
	 */
	public final static void cleanStatus(String keyName,List<Map<String, Object>> list){
		if(keyName==null)
			keyName = "activityStatus";
		if(list!=null){
			for(Map<String, Object> map:list){
				int isCancel = Integer.parseInt(map.get("isCancel").toString());
				if(isCancel==Constant.YES_INT){
					map.put(keyName, ActivityStatus.ACTIVITY_STATUS_CANCEL.getMessage());
					cleanSupport(map);
					continue;
				}
				long now = System.currentTimeMillis();
				long enrollBegin = Long.parseLong(map.get("enroll_begin_time").toString());
				if(now<enrollBegin){
					map.put(keyName, ActivityStatus.ACTIVITY_STATUS_NONE.getMessage());
					cleanSupport(map);
					continue;
				}
				long enrollEnd = Long.parseLong(map.get("enroll_end_time").toString());
				if(now<enrollEnd){
					map.put(keyName, ActivityStatus.ACTIVITY_STATUS_ENROLLING.getMessage());
					cleanSupport(map);
					continue;
				}
				
				long begin = Long.parseLong(map.get("activity_time").toString());
				if(now<begin){
					map.put(keyName, ActivityStatus.ACTIVITY_STATUS_PREPARING.getMessage());
					cleanSupport(map);
					continue;
				}
				long end = Long.parseLong(map.get("activity_end_time").toString());
				if(now<end){
					map.put(keyName, ActivityStatus.ACTIVITY_STATUS_DOING.getMessage());
				}else{
					map.put(keyName, ActivityStatus.ACTIVITY_STATUS_END.getMessage());
				}
				cleanSupport(map);
			}
		}
	}
	
	/**
	 * 选择性的去删除map中一些无用的字段
	 * @param map
	 */
	private final static void cleanSupport(Map<String, Object> map){
		CleanUtil.cleanMap(map, "isCancel","create_time","enroll_begin_time","enroll_end_time","prepare_start_time","prepare_end_time","activity_end_time","cancel_time");
	}
	/**
	 * 获取线下活动参与人数
	 * @param id
	 */
	public final static String orderCount(String id){
		String sql="SELECT  ifnull(COUNT(o.id),0) co    FROM tb_activity a LEFT JOIN tb_order o ON  a.id= o.video_id"
				   + " WHERE o.`status`=1  AND a.id='"+id+"'";
		return sql;
	}
	
}
