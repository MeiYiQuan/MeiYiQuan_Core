package com.salon.backstage.qcproject.dao.support;

import java.util.HashMap;
import java.util.Map;

import com.salon.backstage.pub.bsc.domain.Constant;
import com.salon.backstage.qcproject.util.Sql;
import com.salon.backstage.qcproject.util.Statics;

/**
 * 作者：齐潮
 * 创建日期：2017年1月11日
 * 类说明：用于产生与评论有关的sql
 */
public class CommentSupport {

	
	/*
	通过类型id去获取该类型id下的评论，包括二级评论。仅仅适用于详情页
	public final static Sql getCommentsByTypeId(String typeId,String userId,int start,int size){
		String order = "`comm_time` desc";
		String lin = "(select `lin`.* from (select `id` from `tb_comment` where `commed_id` = :typeId "
								+ "and `commed_type` = " + Statics.COMMENT_LEVEL_ONE + " "
								+ "order by " + order + " limit :start,:size) as `lin`)";
		
		
		String sql = "select `comment`.`user_id` as `user_id`,"
				+ "((select count(`id`) from `tb_like` where `like_type` = " + Statics.LIKE_TYPE_COMMENT + " and `like_type_id` = `comment`.`id` and `like_dislike` = " + Statics.LIKE_YES + ") "
				+ "- "
				+ "(select count(`id`) from `tb_like` where `like_type` = " + Statics.LIKE_TYPE_COMMENT + " and `like_type_id` = `comment`.`id` and `like_dislike` = " + Statics.LIKE_NOT + ")) "
				+ "as `activityLikeCount`,"
			+ "((select count(`id`) from `tb_like` where `like_type` = " + Statics.LIKE_TYPE_COMMENT + " and `like_type_id` = `comment`.`id` and `like_dislike` = " + Statics.LIKE_YES + " and `user_id` = :userId) "
				+ "- "
				+ "(select count(`id`) from `tb_like` where `like_type` = " + Statics.LIKE_TYPE_COMMENT + " and `like_type_id` = `comment`.`id` and `like_dislike` = " + Statics.LIKE_NOT + " and `user_id` = :userId)) "
				+ "as `userZanStatus`,"
			+ ""
			+ "`comment`.`id` as `id`,"
			+ "`comment`.`commed_id` as `commed_id`,"
			+ "`comment`.`commed_type` as `commed_type`,"
			+ "`comment`.`comm_time` as `comm_time`,"
			+ "`comment`.`comm_content` as `comm_content`,"
			+ "ifnull(`user`.`username`,'用户') as `username`,"
			+ "ifnull(`user`.`pic_save_url`,'') as `pic_save_url` "
		+ "from `tb_comment` as `comment` "
		+ "LEFT JOIN `tb_user` as `user` on `user`.`id` = `comment`.`user_id` "
		+ "where (`comment`.`id` in " + lin + " or `comment`.`commed_id` in " + lin + ") "
//						+ "and `comment`.`type` = :type "
						+ "order by `comment`." + order;
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("typeId", typeId);
//		params.put("type", type);
		params.put("userId", userId);
		params.put("start", start);
		params.put("size", size);
		Sql result = Sql.get(sql, params);
		return result;
	}
	
	将传进来的由getCommentsByTypeId方法生成的comments进行整理，将每个二级评论放到相应的一级评论下面
	public final static List<Map<String, Object>> cleanComments(List<Map<String, Object>> comments){
		if(comments==null)
			return new ArrayList<Map<String, Object>>();
		List<Map<String, Object>> oneLevel = new ArrayList<Map<String, Object>>();
		List<Map<String, Object>> twoLevel = new ArrayList<Map<String, Object>>();
		for(int i=0;i<comments.size();i++){
			Map<String, Object> comment = comments.get(i);
			int level = Integer.parseInt(comment.get("commed_type").toString());
			if(level==Statics.COMMENT_LEVEL_ONE){
				oneLevel.add(comment);
			}else if(level==Statics.COMMENT_LEVEL_TWO){
				twoLevel.add(comment);
			}
		}
		for(int i=0;i<oneLevel.size();i++){
			Map<String, Object> oneLevelComment = oneLevel.get(i);
			Object oneLevelId = oneLevelComment.get("id");
			List<Map<String, Object>> child = new ArrayList<Map<String, Object>>();
			oneLevelComment.put("secondComment", child);
			if(twoLevel.size()>0){
				for(int j=twoLevel.size()-1;j>=0;j--){
					Map<String, Object> twoLevelComment = twoLevel.get(j);
					if(oneLevelId.equals(twoLevelComment.get("commed_id"))){
						child.add(twoLevelComment);
						twoLevelComment.remove("commed_id");
					}
				}
			}
			oneLevelComment.put("secondCommentCount", child.size());
			oneLevelComment.remove("commed_id");
		}
		return oneLevel;
	}
	*/
	
	/**
	 * 获取评论列表
	 * @param commendId
	 * @param userId
	 * @return
	 */
	public final static Sql getComments(String commendId,String userId){
		String sql = "select `comment`.`user_id` as `user_id`,"
								+ "((select count(`id`) from `tb_like` where `like_type` = " + Statics.LIKE_TYPE_COMMENT + " and `like_type_id` = `comment`.`id` and `like_dislike` = " + Statics.LIKE_YES + ") "
									+ "- "
									+ "(select count(`id`) from `tb_like` where `like_type` = " + Statics.LIKE_TYPE_COMMENT + " and `like_type_id` = `comment`.`id` and `like_dislike` = " + Statics.LIKE_NOT + ")) "
									+ "as `activityLikeCount`,"
								+ "((select count(`id`) from `tb_like` where `like_type` = " + Statics.LIKE_TYPE_COMMENT + " and `like_type_id` = `comment`.`id` and `like_dislike` = " + Statics.LIKE_YES + " and `user_id` = :userId) "
									+ "- "
									+ "(select count(`id`) from `tb_like` where `like_type` = " + Statics.LIKE_TYPE_COMMENT + " and `like_type_id` = `comment`.`id` and `like_dislike` = " + Statics.LIKE_NOT + " and `user_id` = :userId)) "
									+ "as `likeDislike`,"
								+ "(select ifnull(count(`id`),0) as `count` from `tb_comment` where `commed_id` = `comment`.`id` and `status` = " + Constant.NO_INT + ") as `secondCommentCount`,"
								+ "`comment`.`id` as `id`,"
								+ "`comment`.`commed_id` as `commed_id`,"
								+ "`comment`.`commed_type` as `commed_type`,"
								+ "`comment`.`comm_time` as `comm_time`,"
								+ "`comment`.`comm_content` as `comm_content`,"
								+ "ifnull(`user`.`username`,'用户') as `username`,"
								+ "ifnull(`user`.`pic_save_url`,'') as `pic_save_url` "
							+ "from `tb_comment` as `comment` "
							+ "LEFT JOIN `tb_user` as `user` on `user`.`id` = `comment`.`user_id` "
							+ "where `comment`.`commed_id` = :commendId "
							+ "and `comment`.`status` = " + Constant.NO_INT + " "
							+ "group by `comment`.`id` "
							+ "order by `comment`.`comm_time` desc";
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("commendId", commendId);
//		params.put("type", type);
		params.put("userId", userId);
		Sql result = Sql.get(sql, params);
		return result;
	}
	
	
}
