package com.salon.backstage.homepage.comment.action;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.qc.util.Associate;
import com.qc.util.MathUtil;
import com.qc.util.MathUtil.IsInt;
import com.salon.backstage.common.util.HttpRequstParmsUtil;
import com.salon.backstage.common.util.MobileMessageCondition;
import com.salon.backstage.common.util.NetworkUtil;
import com.salon.backstage.common.util.StringUtil;
import com.salon.backstage.common.util.Validate;
import com.salon.backstage.homepage.comment.service.ICommentService;
import com.salon.backstage.pub.bsc.dao.po.User;
import com.salon.backstage.pub.bsc.domain.Constant;
import com.salon.backstage.qcproject.dao.ObjectDao;
import com.salon.backstage.qcproject.util.Code;
import com.salon.backstage.qcproject.util.Send;
import com.salon.backstage.qcproject.util.Statics;
import com.salon.backstage.user.IUserService;

@Controller
@RequestMapping("/comment")
public class CommentAction {
	
	@Autowired
	ICommentService commentService;
	@Autowired
	IUserService userService;
	
	@Autowired
	ObjectDao od;
	
	/**
	 * 讲师详情页-评论的详情接口
	 * teacherId:讲师ID
	 */
	@RequestMapping("/teacherDetailComment.do")
	@ResponseBody
	public Object teacherDetailComment(HttpServletRequest request){
		Map<String, Object> json = HttpRequstParmsUtil.getParmsForMap(request);
		String teacherId = (String)json.get("teacherId");
		List<Map> commContent = commentService.queryByTeacherId(teacherId);
		return Send.send(MobileMessageCondition.addCondition(true, 0, "成功", commContent));
	}
	
	/**
	 * 写评论
	 * commed_type:类型(1.详情2.评论)
	 * comm_content: 评论内容
	 * user_id:用户ID
	 * comm_content_id:用户ID
	 * commed_id：详情ID
	 * @throws Exception 
	 * 
	 */
	@RequestMapping("/addComment.do")
	@ResponseBody
	public Object addComment(HttpServletRequest request) throws Exception{
		String ip = NetworkUtil.getIpAddr(request);
		if(ip==null)
			return Send.send(Code.init(false, 100, "未知错误！"));
		if(Associate.isDoing(ip, "comment_add"))
			return Send.send(Code.init(false, 101, "正在评论，请稍后..."));
		
		Associate.save(ip, "comment_add");
		
		Object result = addSupport(request);
		
		Associate.clear(ip, "comment_add");
		
		return Send.send(result);
	}
	
	private Object addSupport(HttpServletRequest request) throws Exception{
		Map<String, Object> json = HttpRequstParmsUtil.getParmsForMap(request);
		boolean vali = Validate.validate(json, "commed_type", "commed_id", "type");
		if(!vali)
			return Code.init(false, 1, "参数丢失！");
		String content = json.get("comm_content")==null?"":json.get("comm_content").toString();
		if(content.trim().equals(""))
			return Code.init(false, 2, "评论内容不能为空！");
		IsInt commed_type = MathUtil.isToInteger(json.get("commed_type").toString());
		IsInt type = MathUtil.isToInteger(json.get("type").toString());
		if(!commed_type.is||(commed_type.value!=Statics.COMMENT_LEVEL_ONE&&commed_type.value!=Statics.COMMENT_LEVEL_TWO)
				||!type.is||(type.value!=Statics.COMMENT_TYPE_VIDEO&&type.value!=Statics.COMMENT_TYPE_ACTIVITY
						&&type.value!=Statics.COMMENT_TYPE_COURSE&&type.value!=Statics.COMMENT_TYPE_REQUEST
						&&type.value!=Statics.COMMENT_TYPE_TEACHER))
			return Code.init(false, 3, "参数不合法！");
		String userId = json.get(Send.USERID_NAME).toString();
		String commenId = json.get("commed_id").toString();
		
		User user = od.getObjById(User.class, userId);
		if(user==null)
			return MobileMessageCondition.addCondition(false, 8, "用户不存在，请重新登陆！", "");
		if(user.getIsComment()!=Constant.YES_INT)
			return MobileMessageCondition.addCondition(false, 9, "您已经被禁言，请联系管理员！", "");
		
		String message = commentService.add(userId, commed_type.value,type.value, content, commenId);
		return MobileMessageCondition.addCondition(true, 0, message, "");
	}
	
	/**查评论
	 * 课程详情页-评论的详情接口
	 * commed_id：详情ID
	 * 
	 */
	@RequestMapping("/getComment.do")
	@ResponseBody
	public Object getComment(HttpServletRequest request){
		Map<String, Object> json = HttpRequstParmsUtil.getParmsForMap(request);
		
		boolean vali = Validate.validate(json, "pageNo", "pagesize", "commed_id");
		if(!vali)
			return Send.send(Code.init(false, 1, "参数丢失！"));
		IsInt pageNo = MathUtil.isToInteger(json.get("pageNo").toString());
		IsInt pageSize = MathUtil.isToInteger(json.get("pagesize").toString());
		String commedId = json.get("commed_id").toString();
		String userId = json.get(Send.USERID_NAME).toString();
		if(!pageNo.is||!pageSize.is||pageNo.value<1||pageSize.value<1)
			return Send.send(Code.init(false, 2, "参数不合法！"));
		Code result = commentService.getComment(userId, commedId, pageNo.value, pageSize.value);
		return Send.send(result);
	}
	
	/**
	 * 课程详情页-评论的详情接口
	 * courseOrActivityId:评论内容的ID
	 * type:类型(1.课程,2.活动)
	 */
	@RequestMapping("/courseDetailComment.do")
	@ResponseBody
	public Object courseDetailComment(HttpServletRequest request){
		Map<String, Object> json = HttpRequstParmsUtil.getParmsForMap(request);
		String courseorActivityId = (String)json.get("courseOrActivityId");
		List<Map> commContent=null;
		if(json.get("type").equals("1")){
			commContent = commentService.queryCourseDetailCommentByCourseId(courseorActivityId);
		}else if(json.get("type").equals("2")){
			commContent = userService.queryActivityComment(json);//线下活动详情
			Long activityLikeCount = userService.queryActivityLikes(json);//活动的点赞数
			for (Map map : commContent) {
				String userId = map.get("userId").toString();
				Long commentCounts=userService.queryActivityComment(userId);
				map.put("secondCommentCount", commentCounts);//二次评论
				map.put("activityLikeCount", activityLikeCount);//评论内容
			}
		}
		return Send.send(MobileMessageCondition.addCondition(true, 0, "成功", commContent));
	}
}



