package com.salon.backstage.homepage.object.action;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Map;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.pingplusplus.Pingpp;
import com.qc.util.Associate;
import com.salon.backstage.common.util.HttpRequstParmsUtil;
import com.salon.backstage.common.util.MobileMessageCondition;
import com.salon.backstage.common.util.NetworkUtil;
import com.salon.backstage.homepage.channel.service.IChannelService;
import com.salon.backstage.homepage.channel.service.ObjectService;
import com.salon.backstage.homepage.object.service.UserVideoRequestService;
import com.salon.backstage.homepage.pay.action.PayAction;
import com.salon.backstage.homepage.point.service.IPointService;
import com.salon.backstage.pub.bsc.dao.po.Teacher;
import com.salon.backstage.pub.bsc.dao.po.User;
import com.salon.backstage.pub.bsc.dao.po.UserToRequest;
import com.salon.backstage.pub.bsc.dao.po.UserVideoRequest;
import com.salon.backstage.pub.bsc.dao.vo.MobileMessage;
import com.salon.backstage.pub.bsc.domain.Constant.ChannelTop;
import com.salon.backstage.pub.bsc.domain.Constant.PointEachType;
import com.salon.backstage.qcproject.dao.ObjectDao;
import com.salon.backstage.qcproject.util.Enums.RequestStatus;
import com.salon.backstage.qcproject.util.Send;
import com.salon.backstage.qcproject.util.Statics;

/**
 * 作者：齐潮
 * 创建日期：2016年12月16日
 * 类说明：用于处理跟求课程有关的请求
 */
@Controller
@RequestMapping(value="uvrequest")
public class UserVideoRequestController {

	@Autowired
	private UserVideoRequestService uvrs;
	
	@Autowired
	private IChannelService cs;
	
	@Autowired
	private IPointService ps;
	
	@Autowired
	private ObjectDao od;
	
	@Autowired
	private ObjectService os;
	
	private static String url = null;
	
	static{
		InputStream inputStream = UserVideoRequestController.class.getClassLoader().getResourceAsStream("config.properties");
		Properties p = new Properties();
		try {
			p.load(new InputStreamReader(inputStream, "utf-8"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		UserVideoRequestController.url = p.getProperty("share.url.prefix");
	}
	
	/**
	 * 分页，分类型取获得求课程列表(公共的列表，只查询出相应类型中还未反馈的求课程信息)
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="getAll",method=RequestMethod.POST)
	public Object getAllRequests(HttpServletRequest request){
		Map<String, Object> json = HttpRequstParmsUtil.getParmsForMap(request);
		Object pageObj = json.get("page");
		Object sizeObj = json.get("size");
		Object typeObj = json.get("type");
		if(pageObj==null||sizeObj==null||typeObj==null)
			return Send.send(MobileMessageCondition.addCondition(false, 99, "参数缺失！", null));
		int page = Integer.parseInt(pageObj.toString());
		int size = Integer.parseInt(sizeObj.toString());
		int type = Integer.parseInt(typeObj.toString());
		if(page<1||size<1)
			return Send.send(MobileMessageCondition.addCondition(false, 98, "参数不合法！", null));
		ChannelTop channelType = ChannelTop.getChannelTopByType(type);
		if(channelType==null)
			return Send.send(MobileMessageCondition.addCondition(false, 98, "参数不合法！", null));
		MobileMessage result = uvrs.getRequestList(page, size, channelType);
		return Send.send(result);
	}
	
	/**
	 * 创建一个求课程信息
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="create",method=RequestMethod.POST)
	public Object createRequest(HttpServletRequest request){
		String ip = NetworkUtil.getIpAddr(request);
		if(ip==null)
			return Send.send(MobileMessageCondition.addCondition(false, 100, "未知错误！", null));
		if(Associate.isDoing(ip, "request_create"))
			return Send.send(MobileMessageCondition.addCondition(false, 101, "正在创建，请稍后...", null));
		
		Associate.save(ip, "request_create");
		
		Map<String, Object> json = HttpRequstParmsUtil.getParmsForMap(request);
		Object userId = json.get("userid");				// 该用户的id
		Object channelIdObj = json.get("channelId");	// 频道id
		Object teacherIdObj = json.get("teacherId");	// 讲师id
		Object coursenameObj = json.get("coursename");	// 课程名称
		Object questionObj = json.get("question");		// 疑惑
		Object fromtypeObj = json.get("cross");		// 来源，具体见静态量
		if(channelIdObj==null||teacherIdObj==null||coursenameObj==null||questionObj==null||fromtypeObj==null){
			Associate.clear(ip, "request_create");
			return Send.send(MobileMessageCondition.addCondition(false, 99, "参数缺失！", null));
		}
		String channelId = channelIdObj.toString();
		String teacherId = teacherIdObj.toString();
		String coursename = coursenameObj.toString();
		String question = questionObj.toString();
		int fromtype = Integer.parseInt(fromtypeObj.toString());
		if(coursename.trim().equals("")||teacherId.trim().equals("")||channelId.trim().equals("")||question.trim().equals("")
				||(fromtype!=Statics.COURSE_QUESTION_HOMEPAGE&&fromtype!=Statics.COURSE_QUESTION_TEACHER&&fromtype!=Statics.COURSE_QUESTION_VOID)){
			Associate.clear(ip, "request_create");
			return Send.send(MobileMessageCondition.addCondition(false, 98, "参数不合法！", null));
		}
		Teacher teacher = os.get(Teacher.class, new String[]{"teacher_id"}, new Object[]{teacherId});
		if(teacher==null){
			Associate.clear(ip, "request_create");
			return Send.send(MobileMessageCondition.addCondition(false, 97, "该讲师已经不存在！", null));
		}
		Map<String,Object> isHaveChannel = cs.isHave(channelId);
		if(isHaveChannel.get("isHave").equals(false)){
			Associate.clear(ip, "request_create");
			return Send.send(MobileMessageCondition.addCondition(false, 96, "该频道或其父频道已经不存在！", null));
		}
			
		String topId = isHaveChannel.get("topId").toString();
		
		UserVideoRequest req = new UserVideoRequest();
		
		String shareUrl = new String(UserVideoRequestController.url);
		req.setShare_url(shareUrl.replace("id", teacherId).replace("type", "8"));
		req.setTop_channel_id(topId);
		req.setChannel_id(channelId);
		req.setCourse_name(coursename);
		req.setFeedback_time((long) 0);
		req.setQuestion(question);
		req.setRequest_time(System.currentTimeMillis());
		req.setTeacher_id(teacherId);
		req.setUser_id(userId.toString());
		req.setFrom_type(fromtype);
//		Sys sys = os.get(Sys.class, new String[]{"sys_key"}, new Object[]{Constant.SYSTEM_KEY_REQUEPICURL});
		req.setPic_url(teacher.getHead_url());
		req.setVote(0);
		req.setFeedback_status(RequestStatus.REQUEST_STATUS_INREVIEW.getType());
		String reqId = os.save(req);
		if(reqId==null){
			Associate.clear(ip, "request_create");
			return Send.send(MobileMessageCondition.addCondition(false, 95, "数据库错误！", null));
		}
		
		Associate.clear(ip, "request_create");
		
		return Send.send(MobileMessageCondition.addCondition(true, 0, "您提交的求教程已提交成功，后台正在审核中，通过后会显示在投票页面，请您关注！", null));
	}
	
	/**
	 * 处理用户对求课程投票的请求
	 * @param request
	 * @return
	 * @throws Exception 
	 */
	@ResponseBody
	@RequestMapping(value="check",method=RequestMethod.POST)
	public Object checkRequest(HttpServletRequest request) throws Exception{
		String ip = NetworkUtil.getIpAddr(request);
		if(ip==null)
			return Send.send(MobileMessageCondition.addCondition(false, 100, "未知错误！", ""));
		if(Associate.isDoing(ip, "request_create"))
			return Send.send(MobileMessageCondition.addCondition(false, 101, "正在投票，请稍后...", ""));
		
		Associate.save(ip, "request_create");
		
		Map<String, Object> json = HttpRequstParmsUtil.getParmsForMap(request);
		Object userId = json.get("userid");					// 该用户的id
		Object requestIdObj = json.get("requestId");		// 求课程id
		if(requestIdObj==null){
			Associate.clear(ip, "request_create");
			return Send.send(MobileMessageCondition.addCondition(false, 99, "参数缺失！", ""));
		}
		String requestId = requestIdObj.toString();
		if(requestId.trim().equals("")){
			Associate.clear(ip, "request_create");
			return Send.send(MobileMessageCondition.addCondition(false, 98, "参数不合法！", ""));
		}
		UserVideoRequest uvr = os.get(UserVideoRequest.class, new String[]{"id"}, new Object[]{requestId});
		if(uvr==null){
			Associate.clear(ip, "request_create");
			return Send.send(MobileMessageCondition.addCondition(false, 97, "该求课程已经不存在！", ""));
		}
		/*
		 * 反馈过之后也能投票   2016-12-22
		int status = uvr.getFeedback_status();
		if(status==Constant.YES_INT){
			Associate.clear(ip, "request_create");
			return MobileMessageCondition.addCondition(false, 96, "该求课程已经反馈！", null);
		}
		*/
		
		UserToRequest urt = os.get(UserToRequest.class, new String[]{"userId","requestId"}, new Object[]{userId.toString(),requestId});
		if(urt!=null){
			Associate.clear(ip, "request_create");
			return Send.send(MobileMessageCondition.addCondition(false, 94, "该求课程您已经投过票！", ""));
		}
		urt = new UserToRequest();
		urt.setCreateTime(System.currentTimeMillis());
		urt.setRequestId(requestId);
		urt.setUserId(userId.toString());
		String urtId = os.save(urt);
		if(urtId==null){
			Associate.clear(ip, "request_create");
			return Send.send(MobileMessageCondition.addCondition(false, 95, "数据库错误！", ""));
		}
		uvr.setVote(uvr.getVote() + 1);
		od.update(uvr);
		
		
		
		// 添加积分
		User user = od.getObjById(User.class, userId.toString());
		int point = -1;
		if(user!=null){
			MobileMessage addPointResult = ps.addPoint(user, PointEachType.VOTE_POINT, 0);
			if(addPointResult.isResult())
				point = Integer.parseInt(addPointResult.getResponse().toString());
		}
		String addMessage = point==-1?"":"获得了" + point + "个积分！";
		
		Associate.clear(ip, "request_create");
		
		return Send.send(MobileMessageCondition.addCondition(true, 0, "投票成功！" + addMessage, ""));
	}
	
	/**
	 * 获取周榜首
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="top",method=RequestMethod.POST)
	public Object getTop(HttpServletRequest request){
		MobileMessage result = uvrs.getTop();
		return Send.send(result);
	}
	
}
