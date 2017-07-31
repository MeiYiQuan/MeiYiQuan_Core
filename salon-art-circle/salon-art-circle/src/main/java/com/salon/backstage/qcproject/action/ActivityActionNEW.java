package com.salon.backstage.qcproject.action;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.qc.util.Associate;
import com.qc.util.MathUtil;
import com.qc.util.MathUtil.IsInt;
import com.salon.backstage.common.util.HttpRequstParmsUtil;
import com.salon.backstage.common.util.NetworkUtil;
import com.salon.backstage.common.util.Validate;
import com.salon.backstage.qcproject.service.ActivityServiceNEW;
import com.salon.backstage.qcproject.util.Code;
import com.salon.backstage.qcproject.util.Send;
import com.salon.backstage.qcproject.util.Statics;


/**
 * 作者：齐潮
 * 创建日期：2016年12月28日
 * 类说明：处理有关活动的请求
 */
@Controller
@RequestMapping(value="activitynew")
public class ActivityActionNEW {

	@Autowired
	private ActivityServiceNEW as;
	
	
	/**
	 * 根据类型分页获取线下活动列表
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="offlineActivity",method=RequestMethod.POST)
	public Object getActivitiesByType(HttpServletRequest request){
		Map<String, Object> json = HttpRequstParmsUtil.getParmsForMap(request);
		boolean vali = Validate.validate(json, "pageNo", "pagesize", "type");
		if(!vali)
			return Send.send(Code.init(false, 1, "参数丢失！"));
		IsInt type = MathUtil.isToInteger(json.get("type").toString());
		IsInt pageNo = MathUtil.isToInteger(json.get("pageNo").toString());
		IsInt pageSize = MathUtil.isToInteger(json.get("pagesize").toString());
		if(!pageNo.is||!pageSize.is||pageNo.value<1||pageSize.value<1
				||!type.is||(type.value!=Statics.ACTIVITY_REQUEST_ALL&&type.value!=Statics.ACTIVITY_REQUEST_DISTRICT&&type.value!=Statics.ACTIVITY_REQUEST_ENDED))
			return Send.send(Code.init(false, 2, "参数不合法！"));
		Code result = as.getActivitiesByType(json.get("userid").toString(), type.value, pageNo.value, pageSize.value);
		return Send.send(result);
	}
	
	/**
	 * 我的参与
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="myParticipation",method=RequestMethod.POST)
	public Object getMyParticipation(HttpServletRequest request){
		Map<String, Object> json = HttpRequstParmsUtil.getParmsForMap(request);
		boolean vali = Validate.validate(json, "pageNo", "pagesize");
		if(!vali)
			return Send.send(Code.init(false, 1, "参数丢失！"));
		IsInt pageNo = MathUtil.isToInteger(json.get("pageNo").toString());
		IsInt pageSize = MathUtil.isToInteger(json.get("pagesize").toString());
		if(!pageNo.is||!pageSize.is||pageNo.value<1||pageSize.value<1)
			return Send.send(Code.init(false, 2, "参数不合法！"));
		Code result = as.getActivitiesByUserId(json.get("userid").toString(), pageNo.value, pageSize.value);
		return Send.send(result);
	}
	
	/**
	 * 发现-线下活动详情：立即参与
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="comeIn",method=RequestMethod.POST)
	public Object comeInActivity(HttpServletRequest request){
		String ip = NetworkUtil.getIpAddr(request);
		if(ip==null)
			return Send.send(Code.init(false, 100, "未知错误！"));
		if(Associate.isDoing(ip, "activity_comeIn"))
			return Send.send(Code.init(false, 101, "正在参与，请稍后..."));
		
		Associate.save(ip, "activity_comeIn");
		
		Code result = comeInSupport(request);
		
		Associate.clear(ip, "activity_comeIn");
		
		return Send.send(result);
	}
	
	/**
	 * 立即参与的核心方法
	 * @param request
	 * @return
	 */
	private Code comeInSupport(HttpServletRequest request){
		Map<String, Object> json = HttpRequstParmsUtil.getParmsForMap(request);
		boolean vali = Validate.validate(json, "activityId");
		if(!vali)
			return Code.init(false, 1, "参数丢失！");
		String activityId = json.get("activityId").toString();
		String userId = json.get("userid").toString();
		Code result = as.comeInActivity(userId, activityId);
		return result;
	}
	
}
