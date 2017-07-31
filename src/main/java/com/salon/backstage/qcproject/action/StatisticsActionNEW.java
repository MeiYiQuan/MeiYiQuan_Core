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
import com.salon.backstage.homepage.statistics.service.IStatisticsService;
import com.salon.backstage.qcproject.util.Code;
import com.salon.backstage.qcproject.util.Send;
import com.salon.backstage.qcproject.util.Statics;

/**
 * 作者：齐潮
 * 创建日期：2017年1月17日
 * 类说明：处理有关统计的请求
 */
@Controller
@RequestMapping(value="statistics")
public class StatisticsActionNEW {

	@Autowired
	private IStatisticsService is;
	
	/**
	 * 接收有关统计的请求
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="doing",method=RequestMethod.POST)
	public Object staticsDoing(HttpServletRequest request){
		String ip = NetworkUtil.getIpAddr(request);
		if(ip==null)
			return Send.send(Code.init(false, 100, "未知错误！"));
		if(Associate.isDoing(ip, "statistics_doing"))
			return Send.send(Code.init(false, 101, "正在执行，请稍后..."));
		
		Associate.save(ip, "statistics_doing");
		
		Code result = doingSupport(request);
		
		Associate.clear(ip, "statistics_doing");
		
		return Send.send(result);
	}
	
	/**
	 * 增加统计信息的核心方法
	 * @param request
	 * @return
	 */
	private Code doingSupport(HttpServletRequest request){
		Map<String, Object> json = HttpRequstParmsUtil.getParmsForMap(request);
		boolean vali = Validate.validate(json, "id", "type");
		if(!vali)
			return Code.init(false, 1, "参数丢失！");
		String id = json.get("id").toString();
		IsInt type = MathUtil.isToInteger(json.get("type").toString());
		if(!type.is)
			return Code.init(false, 2, "参数不合法！");
		boolean result = false;
		switch(type.value){
			case 1:
				// 点击视频播放时触发，给当前视频的播放量+1
				result = is.addStatistics(Statics.STATISTICS_PLAY_COUNT, Statics.STATICS_TYPE_SP, id);
				break;
			default:
				return Code.init(false, 2, "参数不合法！");
		}
		if(!result)
			return Code.init(false, 3, "系统繁忙！请稍后再试！");
		return Code.init(true, 0, "","");
	}
	
	
	
}
