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
import com.salon.backstage.pub.bsc.domain.Constant.ChannelTop;
import com.salon.backstage.qcproject.service.ChannelServiceNEW;
import com.salon.backstage.qcproject.util.Code;
import com.salon.backstage.qcproject.util.Send;

/**
 * 作者：齐潮
 * 创建日期：2017年1月8日
 * 类说明：处理有关频道的请求
 */
@Controller
@RequestMapping(value="channelnew")
public class ChannelActionNEW {

	@Autowired
	private ChannelServiceNEW cs;
	
	/**
	 * 条件获取频道列表(类型，名称，地区)
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="getchannels",method=RequestMethod.POST)
	public Object getChannels(HttpServletRequest request){
		Map<String, Object> json = HttpRequstParmsUtil.getParmsForMap(request);
		boolean vali = Validate.validate(json, "type");
		if(!vali)
			return Send.send(Code.init(false, 1, "参数丢失！"));
		IsInt type = MathUtil.isToInteger(json.get("type").toString());
		if(!type.is||ChannelTop.getChannelTopByType(type.value)==null)
			return Send.send(Code.init(false, 2, "参数不合法！"));
		Object channelName = json.get("channelName");
		Object district = json.get("district");
		Code result = cs.getChannels(ChannelTop.getChannelTopByType(type.value), channelName, district);
		return Send.send(result);
	}
	
}
