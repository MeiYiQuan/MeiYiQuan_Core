package com.salon.backstage.homepage.comment.service;

import java.util.List;
import java.util.Map;

import com.salon.backstage.common.util.Paging;
import com.salon.backstage.qcproject.util.Code;

/**
 * 评论表接口
 *
 */
public interface ICommentService {
	
	/**
	 * 根据teacherId在评论表中查询评论
	 */
	List<Map> queryByTeacherId(String teacherId);
	/**
	 * 评论
	 * @return 
	 * @throws Exception 
	 */
	String add(String userId,int commentype,int type,String content,String commenId) throws Exception;
	/**
	 * 查评论
	 */
	Code getComment(String userId,String commenId,int page,int size);
	
	/**
	 * 根据videoId在评论表中查询评论
	 */
	List<Map> queryByVideoId(String courseId);

	/**
	 * 首页-视频详情-供首次进入视频详情页查询视频评论使用
	 */
	List<Map> queryCourseDetailCommentByCourseId(String courseId);
	

}
