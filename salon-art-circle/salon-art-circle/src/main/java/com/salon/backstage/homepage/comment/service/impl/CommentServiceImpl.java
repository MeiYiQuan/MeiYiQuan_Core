package com.salon.backstage.homepage.comment.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.salon.backstage.common.util.ExtraSpringHibernateTemplate;
import com.salon.backstage.common.util.Paging;
import com.salon.backstage.common.util.StringUtil;
import com.salon.backstage.homepage.comment.service.ICommentService;
import com.salon.backstage.homepage.point.service.IPointService;
import com.salon.backstage.homepage.statistics.service.IStatisticsService;
import com.salon.backstage.pub.bsc.dao.po.Comment;
import com.salon.backstage.pub.bsc.dao.po.User;
import com.salon.backstage.pub.bsc.dao.vo.MobileMessage;
import com.salon.backstage.pub.bsc.domain.Constant;
import com.salon.backstage.pub.bsc.domain.Constant.PointEachType;
import com.salon.backstage.qcproject.dao.ObjectDao;
import com.salon.backstage.qcproject.dao.support.CommentSupport;
import com.salon.backstage.qcproject.util.Code;
import com.salon.backstage.qcproject.util.Sql;
import com.salon.backstage.qcproject.util.Statics;

@Service
public class CommentServiceImpl implements ICommentService {
	
	@Autowired
	private ExtraSpringHibernateTemplate extraSpringHibernateTemplate;
	
	@Autowired
	private IPointService pointService;
	
	@Autowired
	private IStatisticsService is;
	
	@Autowired
	private ObjectDao od;
	
	@SuppressWarnings("all")
	@Override
	public List<Map> queryByTeacherId(String teacherId) {
		StringBuilder firstSql = new StringBuilder();
		// 获取对应讲师的一级评论
		firstSql.append("select id,user_id,comm_content,comm_time from tb_comment where commed_type = 2 and comm_content_id = 0 and commed_id = '"+teacherId +"'");
		firstSql.append(" order by comm_time desc ");
		List<Map> firstList = extraSpringHibernateTemplate.createSQLQueryFindAll(firstSql.toString());
		List<Map> totalCommentLList = new ArrayList();
		Iterator<Map> firstListIte = firstList.iterator();
		while(firstListIte.hasNext()){
			Map totalComment = new HashMap();
			Map firstMap = firstListIte.next();
			//获得用户信息
			String sqlUserFirst = "select id,pic_save_url,username from tb_user where user_state = 0 and id = '" +(String)firstMap.get("user_id")+"'";
			Map userMapFirst = extraSpringHibernateTemplate.createSQLQueryFindFirstOne(sqlUserFirst);
			totalComment.put("id", firstMap.get("id"));
			totalComment.put("commentContent", firstMap.get("comm_content"));
			totalComment.put("commentTime", firstMap.get("comm_time"));
			totalComment.put("userId", userMapFirst.get("id"));
			totalComment.put("userPicSaveUrl", userMapFirst.get("pic_save_url"));
			totalComment.put("username", userMapFirst.get("username"));
			String secondSql = "select id,user_id,comm_content from tb_comment where comm_content_id ='" + firstMap.get("id")+"'";
			List<Map> secondList = extraSpringHibernateTemplate.createSQLQueryFindAll(secondSql);
			Iterator<Map> secondListIte = secondList.iterator();
			while(secondListIte.hasNext()){
				Map secondMap = secondListIte.next();
				//获得用户信息
				String sqlUserSecond = "select pic_save_url,username from tb_user where user_state = 0 and id = '" +(String)secondMap.get("user_id")+"'";
				Map userMapSecond = extraSpringHibernateTemplate.createSQLQueryFindFirstOne(sqlUserSecond);
				secondMap.put("secondUserPicSaveUrl", userMapSecond.get("pic_save_url"));
				secondMap.put("secondUsername", userMapSecond.get("username"));
			}
			totalComment.put("secondComment", secondList);
			totalCommentLList.add(totalComment);
		}
		return totalCommentLList;
	}
	
	/**
	 * 获取视频评论
	 * videoId:视频ID
	 */
	@SuppressWarnings("all")
	@Override
	public List<Map> queryByVideoId(String videoId) {
		//根据课程ID获得其评论
		StringBuilder sqlUserFirst = new StringBuilder();
		sqlUserFirst.append("select id,user_id,comm_content,comm_time from tb_comment where commed_type = 1 and comm_content_id = 0 and commed_id = '"+videoId+"'");
		sqlUserFirst.append(" order by comm_time desc ");
		List<Map> firstList = extraSpringHibernateTemplate.createSQLQueryFindAll(sqlUserFirst.toString());
		List<Map> totalCommentLList = new ArrayList();
		Iterator<Map> firstListIte = firstList.iterator();
		while(firstListIte.hasNext()){
			Map totalComment = new HashMap();
			Map firstMap = firstListIte.next();
			String sqlUser = "select id,pic_save_url,username from tb_user where user_state = 0 and id = '" +(String)firstMap.get("user_id")+"'";
			Map userMap = extraSpringHibernateTemplate.createSQLQueryFindFirstOne(sqlUser);
			totalComment.put("id", firstMap.get("id"));
			totalComment.put("commentContent", firstMap.get("comm_content"));
			totalComment.put("commentTime", firstMap.get("comm_time"));
			totalComment.put("userId", userMap.get("id"));
			totalComment.put("userPicSaveUrl", userMap.get("pic_save_url"));
			totalComment.put("username", userMap.get("username"));
			String secondSql = "select id,user_id,comm_content from tb_comment where comm_content_id ='" + firstMap.get("id")+"'";
			List<Map> secondList = extraSpringHibernateTemplate.createSQLQueryFindAll(secondSql);
			Iterator<Map> secondListIte = secondList.iterator();
			while(secondListIte.hasNext()){
				Map secondMap = secondListIte.next();
				//获得用户信息
				String sqlUserSecond = "select pic_save_url,username from tb_user where user_state = 0 and id = '" +(String)secondMap.get("user_id")+"'";
				Map userMapSecond = extraSpringHibernateTemplate.createSQLQueryFindFirstOne(sqlUserSecond);
				secondMap.put("secondUserPicSaveUrl", userMapSecond.get("pic_save_url"));
				secondMap.put("secondUsername", userMapSecond.get("username"));
			}
			totalComment.put("secondComment", secondList);
			totalCommentLList.add(totalComment);
		}
		return totalCommentLList;
	}
	
	/**
	 * 查询课程评论
	 * courseId : 课程Id
	 */
	@Override
	public List<Map> queryCourseDetailCommentByCourseId(String courseId) {
		String sql = "select id,title,time_long,order_num from tb_video where free = 0 and status = 1 and course_id = '"+courseId+"' order by order_num";
		Map map = extraSpringHibernateTemplate.createSQLQueryFindFirstOne(sql);
		List<Map> totalCommentLList = queryByVideoId((String)map.get("id"));
		return totalCommentLList;
	}
	
	/**
	 * 课程详情页-评论的详情接口
	 * commed_type:类型(1.详情2.评论)
	 * comm_content: 评论内容
	 * user_id:用户ID
	 * commed_id：详情ID
	 * @throws Exception 
	 * 
	 */
	@Override
	public String add(String userId,int commentype,int type,String content,String commenId) throws Exception {
		// TODO Auto-generated method stub
		Comment c=new Comment();
		c.setComm_time(System.currentTimeMillis());
		c.setCommed_type(commentype);
		c.setComm_content(content);
		c.setUser_id(userId);
		c.setCommed_id(commenId);
		c.setType(type);
		c.setStatus(Constant.NO_INT);
		extraSpringHibernateTemplate.getHibernateTemplate().save(c);
		String addMessage = "";
		if(type==Statics.COMMENT_LEVEL_ONE){
			//给课程添加评论的时候获得的积分接口
			User user = extraSpringHibernateTemplate.findFirstOneByPropEq(User.class, "id", userId);
			if(user!=null){
				MobileMessage addPointResult = pointService.addPoint(user, PointEachType.COMMENT_POINT,0);
				if(addPointResult.isResult()){
					addMessage = "获得了" + addPointResult.getResponse() + "个积分！";
				}
			}
			// 添加统计信息
			int staticsType = -1;
			
			switch(type){
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
				is.addStatistics(Statics.STATISTICS_COMMENT_COUNT, staticsType, commenId);
		}
		
		return "评论成功！" + addMessage;
	}
	
	/**
	 * 获得评论
	 * userid:用户ID
	 * commed_id:评论ID
	 */
	@Override
	public Code getComment(String userId,String commenId,int page,int size) {
		Sql sql = CommentSupport.getComments(commenId, userId);
		List<Map<String, Object>> list = od.getListBySql(sql.getSql(), sql.getParams(), (page-1)*size, size);
		return Code.init(true, 0, "", (list==null?new ArrayList<Map<String, Object>>():list));
		/*
		// TODO Auto-generated method stub
		String[] values={(String) json.get("userid"),(String) json.get("commed_id")};
		// pageNo前台传参从1开始
		boolean pageNoNull = StringUtil.isNullOrBlank(json.get("pageNo"));
		boolean pagesizeNull = StringUtil.isNullOrBlank(json.get("pagesize"));
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT tbc.id,tbc.commed_type,tbc.comm_content_id,tbc.commed_id,tbc.comm_content,tbc.comm_time,tbc.user_id,tb_user.username,tb_user.pic_save_url,(SELECT COUNT(tb_comment.id) FROM tb_comment WHERE tb_comment.comm_content_id=tbc.id ) AS secondCommentCount,(SELECT COUNT(tb_like.id) FROM tb_like,tb_comment WHERE tb_like.like_dislike=0 and tb_like.like_type=3 and tb_like.like_type_id=tb_comment.commed_id) AS activityLikeCount FROM tb_comment tbc LEFT JOIN tb_user ON tb_user.id = tbc.user_id WHERE user_id =? AND commed_id=?");
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
		*/
	}
}
































