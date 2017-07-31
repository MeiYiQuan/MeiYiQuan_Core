package com.salon.backstage.qcproject.action;

import java.util.HashMap;
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
import com.salon.backstage.pub.bsc.dao.po.Sys;
import com.salon.backstage.pub.bsc.domain.Constant;
import com.salon.backstage.qcproject.dao.ObjectDao;
import com.salon.backstage.qcproject.service.UserVideoRequestServiceNEW;
import com.salon.backstage.qcproject.util.Code;
import com.salon.backstage.qcproject.util.Send;


/**
 * 作者：齐潮
 * 创建日期：2016年12月27日
 * 类说明：处理有关求课程的请求
 */
@Controller
@RequestMapping(value="requestnew")
public class UserVideoRequestActionNEW {

	@Autowired
	private UserVideoRequestServiceNEW uvrs;
	
	@Autowired
	private ObjectDao od;
	
	/**
	 * 我的--发起的教程
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="requestCourse",method=RequestMethod.POST)
	public Object getRequestsByUserId(HttpServletRequest request){
		Map<String, Object> json = HttpRequstParmsUtil.getParmsForMap(request);
		boolean vali = Validate.validate(json, "pageNo", "pagesize");
		if(!vali)
			return Send.send(Code.init(false, 1, "参数丢失！"));
		IsInt pageNo = MathUtil.isToInteger(json.get("pageNo").toString());
		IsInt pageSize = MathUtil.isToInteger(json.get("pagesize").toString());
		if(!pageNo.is||!pageSize.is||pageNo.value<1||pageSize.value<1)
			return Send.send(Code.init(false, 2, "参数不合法！"));
		Code result = uvrs.getRequestsByUserId(json.get("userid").toString(), pageNo.value, pageSize.value);
		return Send.send(result);
	}
	
	/**
	 * 在添加求课程时，获取背景图
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="getBanner",method=RequestMethod.POST)
	public Object getRequestBanner(HttpServletRequest request){
		Map<String, Object> params = new HashMap<String,Object>();
		params.put("sys_key", Constant.SYSTEM_KEY_REQUEST_BACKPIC);
		params.put("type", Constant.SYSTEM_TYPE_PHOTO);
		params.put("status", Constant.YES_INT);
		Sys sys = od.getObjByParams(Sys.class, params);
		if(sys==null)
			return Send.send(Code.init(true, 0, "", ""));
		return Send.send(Code.init(true, 0, "", sys.getSys_value()));
	}
	
}
