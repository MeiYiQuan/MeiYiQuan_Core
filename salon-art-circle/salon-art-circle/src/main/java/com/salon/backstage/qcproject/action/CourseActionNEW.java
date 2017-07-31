package com.salon.backstage.qcproject.action;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.qc.util.MathUtil;
import com.qc.util.MathUtil.IsInt;
import com.salon.backstage.common.util.HttpRequstParmsUtil;
import com.salon.backstage.common.util.Validate;
import com.salon.backstage.qcproject.service.CourseServiceNEW;
import com.salon.backstage.qcproject.util.Code;
import com.salon.backstage.qcproject.util.Send;
import com.salon.backstage.qcproject.util.Statics;

/**
 * 作者：齐潮
 * 创建日期：2017年1月3日
 * 类说明：处理有关课程的业务逻辑
 */
@Controller
@RequestMapping(value="coursenew")
public class CourseActionNEW {

	@Autowired
	private CourseServiceNEW cs;
	
	/**
	 * 查询出正在热播三大榜单：热播榜，热销榜，热评榜
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="courseRank",method=RequestMethod.POST)
	public Object getCollectsByType(HttpServletRequest request){
		Map<String, Object> json = HttpRequstParmsUtil.getParmsForMap(request);
		boolean vali = Validate.validate(json, "pageNo", "pagesize", "type");
		if(!vali)
			return Send.send(Code.init(false, 1, "参数丢失！"));
		IsInt pageNo = MathUtil.isToInteger(json.get("pageNo").toString());
		IsInt pageSize = MathUtil.isToInteger(json.get("pagesize").toString());
		IsInt type = MathUtil.isToInteger(json.get("type").toString());
		if(!pageNo.is||!pageSize.is||!type.is||pageNo.value<1||pageSize.value<1
				||(type.value!=Statics.COURSE_BEINGHIT_BUY&&type.value!=Statics.COURSE_BEINGHIT_COMMENT&&type.value!=Statics.COURSE_BEINGHIT_LOOKING))
			return Send.send(Code.init(false, 2, "参数不合法！"));
		Code result = cs.getCoursesTops(pageNo.value, pageSize.value, type.value);
		return Send.send(result);
	}
	
	/**
	 * 点击课程详情页，进来会自动选择第一个视频来播放。
	 * 也可以在播放记录里点击相应视频来播放
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="courseDetail",method=RequestMethod.POST)
	public Object getCourseDetail(HttpServletRequest request){
		Map<String, Object> json = HttpRequstParmsUtil.getParmsForMap(request);
		boolean vali = Validate.validate(json, "courseId");
		if(!vali)
			return Send.send(Code.init(false, 1, "参数丢失！"));
		String courseId = json.get("courseId").toString();
		String userId = json.get("userid").toString();
		Object videoId = json.get("videoId");
		Code result = cs.getCourseVideoDetail(null, courseId, userId, videoId);
		return Send.send(result);
	}
	
	/**
	 * 在课程详情里点击视频进行跳转，这个接口可以直接获得视频详情以及评论列表
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="courseDetailJumpVideo",method=RequestMethod.POST)
	public Object getCourseDetailJumpVideo(HttpServletRequest request){
		Map<String, Object> json = HttpRequstParmsUtil.getParmsForMap(request);
		boolean vali = Validate.validate(json, "videoId", "courseId");
		if(!vali)
			return Send.send(Code.init(false, 1, "参数丢失！"));
		String courseId = json.get("courseId").toString();
		String videoId = json.get("videoId").toString();
		String userId = json.get("userid").toString();
		Code result = cs.getCourseVideoByVideoId(courseId, userId, videoId);
		Object sendResult = Send.send(result);
		return sendResult;
	}
	
	
}
