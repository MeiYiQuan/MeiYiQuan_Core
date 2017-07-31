package com.salon.backstage.qcproject.dao.support;

import java.util.HashMap;
import java.util.Map;

import com.salon.backstage.qcproject.util.Sql;
import com.salon.backstage.qcproject.util.Statics;

/**
 * 作者：齐潮
 * 创建日期：2016年12月26日
 * 类说明：用于产生与收藏有关的sql语句
 */
public class CollectSupport {

	
	/**
	 * 批量删除收藏信息
	 * @param type
	 * @param collectIds
	 * @return
	 */
	public final static Sql deleteByIds(String[] collectIds){
		StringBuffer str = new StringBuffer();
		Map<String,Object> params = new HashMap<String,Object>();
		for(int i=0;i<collectIds.length;i++){
			str.append(":id" + i + ",");
			params.put("id" + i, collectIds[i]);
		}
		String sql = "delete from `tb_collect` where `id` in (" + str.substring(0, str.length()-1) + ")";
		Sql result = Sql.get(sql, params);
		return result;
	}
	
	/**
	 * 获取去查询我的收藏列表的Sql对象
	 * @param userId
	 * @param type
	 * @return
	 */
	public final static Sql getCollectsSql(String userId,int type){
		String[] typeSql = null;	// 这个数组的第一个元素表示需要拼接到select后面的信息，第二个元素表示left join信息和部分where信息
		switch(type){
			case Statics.COLLECT_TYPE_MRDK:
				typeSql = getMRDS();
				break;
			case Statics.COLLECT_TYPE_HD:
				typeSql = getHD();
				break;
			case Statics.COLLECT_TYPE_SP:
				typeSql = getSP();
				break;
		}
		String sql = "select `collect`.`id` as `collectId`," + typeSql[0]
						+ "from (select * from `tb_collect` where `collect_type` = " + type + ") as `collect` "
						+ typeSql[1] + "and `collect`.`user_id` = :userId "
						+ "group by `collect`.`id` "
						+ "order by `collect`.`collect_time` desc";
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("userId", userId);
		Sql result = Sql.get(sql, params);
		return result;
	}
	
	/**
	 * 获取用于拼接收藏名人大师的sql片段
	 * @return
	 */
	private final static String[] getMRDS(){
		String sql_se = "`teacher`.`teacher_id` as `id`,`teacher`.`name` as `name`,`teacher`.`head_url` as `head_url`,`teacher`.`level` as `level`,`teacher`.`introduction` as `introduction` ";
		String sql_where = "left join `tb_teacher` as `teacher` on `teacher`.`teacher_id` = `collect`.`collect_type_id` "
							+ "where 1 = 1 ";
		String[] result = new String[2];
		result[0] = sql_se;
		result[1] = sql_where;
		return result;
	}
	
	/**
	 * 获取用于拼接收藏活动的sql片段
	 * @return
	 */
	private final static String[] getHD(){
		String sql_se = ActivitySupport.ACTIVITY_SELECT_KENAMES;
		
		String sql_where = "left join `tb_activity` as `active` on `active`.`id` = `collect`.`collect_type_id` "
							+ "left join `tb_activity_status` as `status` on `status`.`activity_id` = `active`.`id` "
							+ "where 1 = 1 ";
		String[] result = new String[2];
		result[0] = sql_se;
		result[1] = sql_where;
		return result;
	}
	
	/**
	 * 获取用于拼接收藏视频的sql片段
	 * @return
	 */
	private final static String[] getSP(){
		String sql_se = "`video`.`id` as `id`,"
							+ "`video`.`title` as `title`,"
							+ "`video`.`course_id` as `courseId`,"
							+ "`video`.`remark` as `remark`,"
							+ "ifnull(`teacher`.`name`,'') as `name`,"
							+ "`video`.`video_pic_url` as `video_pic_url`,"
							+ "ifnull(`teacher`.`level`,'') as `level`,"
							+ "`video`.`video_save_url` as `video_save_url`,"
							+ "ifnull(`statics`.`play_count`,0) as `videoPlayCounts` ";
		String sql_where = "left join `tb_video` as `video` on `video`.`id` = `collect`.`collect_type_id` "
							+ "left join `tb_course` as `course` on `course`.`id` = `video`.`course_id` "
							+ "left join `tb_teacher` as `teacher` on `teacher`.`teacher_id` = `course`.`teacher_id` "
							+ "left join `tb_statistics` as `statics` on `statics`.`type_id` = `video`.`id` and `statics`.`type` = " + Statics.STATICS_TYPE_SP + " "
							+ "where 1 = 1 ";
		String[] result = new String[2];
		result[0] = sql_se;
		result[1] = sql_where;
		return result;
	}
}
