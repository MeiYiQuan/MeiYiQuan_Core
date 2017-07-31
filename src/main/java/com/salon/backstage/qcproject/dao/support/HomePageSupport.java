package com.salon.backstage.qcproject.dao.support;

import com.salon.backstage.pub.bsc.domain.Constant;
import com.salon.backstage.qcproject.util.Sql;
import com.salon.backstage.qcproject.util.Statics;

/**
 * 作者：齐潮
 * 创建日期：2017年1月5日
 * 类说明：用于产生于首页有关的sql
 */
public class HomePageSupport {

	/**
	 * 获取首页中的推荐视频，名人大佬，创业开店，学潮流技术。
	 * 需要多次调用本方法才能获取相应的结果
	 * @param type
	 * @return
	 */
	public final static Sql getHomePages(int type){
		String left = null;
		String se = null;
		String where = null;
		switch(type){
			case Statics.HOMEPAGE_TYPE_CELEBRITY:
				left = "left join `tb_teacher` as `teacher` on `teacher`.`teacher_id` = `home`.`relation_id` ";
				where = "and `teacher`.`put_home` = " + Constant.YES_INT + " and `teacher`.`status` = " + Constant.YES_INT + " ";
				se = "`teacher`.`name` as `title`,`teacher`.`teacher_id` as `id`,`teacher`.`introduction` as `remark` ";
				break;
			case Statics.HOMEPAGE_TYPE_SHOP:
				left = "left join `tb_course` as `course` on `course`.`id` = `home`.`relation_id` ";
				where = /*"and `course`.`belong_type` = " + Statics.COURSE_CHANNEL_CYKD + " "*/
							"and `course`.`homepage_show` = " + Constant.YES_INT + " "
							+ "and `course`.`to_home` = " + Constant.YES_INT + " "
							+ "and `course`.`status` = " + Constant.YES_INT + " "
							+ "and `course`.`playing` in (" + Statics.COURSE_PLAYING_REQUESTTYPE + "," + Statics.COURSE_PLAYING_SHOW + ") ";
				se = "`course`.`title` as `title`,`course`.`id` as `id`,`course`.`remark` as `remark` ";
				break;
			case Statics.HOMEPAGE_TYPE_TECHNOLOGY:
				left = "left join `tb_course` as `course` on `course`.`id` = `home`.`relation_id` ";
				where = /*"and `course`.`belong_type` = " + Statics.COURSE_CHANNEL_XCLJS + " "*/
							"and `course`.`homepage_show` = " + Constant.YES_INT + " "
							+ "and `course`.`to_home` = " + Constant.YES_INT + " "
							+ "and `course`.`status` = " + Constant.YES_INT + " "
							+ "and `course`.`playing` in (" + Statics.COURSE_PLAYING_REQUESTTYPE + "," + Statics.COURSE_PLAYING_SHOW + ") ";
				se = "`course`.`title` as `title`,`course`.`id` as `id`,`course`.`remark` as `remark` ";
				break;
			case Statics.HOMEPAGE_TYPE_VIDEO:
				left = "left join `tb_course` as `course` on `course`.`id` = `home`.`relation_id`  ";
						/*+ "left join `tb_course` as `course` on `course`.`id` = `home`.`relation_id` ";*/
				where = /*"and `video`.`id` is not null "*/
						 "and `course`.`to_home` = " + Constant.YES_INT + " "
						+ "and `course`.`status` = " + Constant.YES_INT + " "
						+ "and `course`.`playing` in (" + Statics.COURSE_PLAYING_REQUESTTYPE + "," + Statics.COURSE_PLAYING_SHOW + ") ";
				se = "`course`.`title` as `title`,`course`.`id` as `id`,`course`.`remark` as `remark` ";
				break;
		}
		
		String sql = "select "
						+ "`home`.`pic_url` as `pic_url`,"
						+ se
						+ "from `tb_homepage` as `home` "
						+ left
						+ "where `home`.`status` = " + Constant.YES_INT + " and `home`.`type` = " + type + " "
						+ where
						+ "ORDER BY `home`.`top_num` DESC";
		
		Sql result = Sql.get(sql, null);
		return result;
	}
	
	
}
