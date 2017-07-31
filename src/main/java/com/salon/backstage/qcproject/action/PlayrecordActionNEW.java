package com.salon.backstage.qcproject.action;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.qc.util.Associate;
import com.salon.backstage.common.util.HttpRequstParmsUtil;
import com.salon.backstage.common.util.NetworkUtil;
import com.salon.backstage.common.util.Validate;
import com.salon.backstage.qcproject.service.PlayrecordServiceNEW;
import com.salon.backstage.qcproject.util.Code;
import com.salon.backstage.qcproject.util.Send;

/**
 * 作者：齐潮
 * 创建日期：2017年1月7日
 * 类说明：处理有关播放记录的请求
 */
@Controller
@RequestMapping(value="playrecordnew")
public class PlayrecordActionNEW {

	@Autowired
	private PlayrecordServiceNEW ps;
	
	/**
	 * 用于保存播放记录的接口
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="saveOrUpdate",method=RequestMethod.POST)
	public Object saveOrUpdate(HttpServletRequest request){
		String ip = NetworkUtil.getIpAddr(request);
		if(ip==null)
			return Send.send(Code.init(false, 100, "未知错误！"));
		if(Associate.isDoing(ip, "palyrecord_save_update"))
			return Send.send(Code.init(false, 101, "正在保存播放记录，请稍后..."));
		
		Associate.save(ip, "palyrecord_save_update");
		
		Code result = saveOrUpdateSupport(request);
		
		Associate.clear(ip, "palyrecord_save_update");
		
		return Send.send(result);
	}
	
	/**
	 * 保存播放记录的核心方法
	 * @param request
	 * @return
	 */
	private Code saveOrUpdateSupport(HttpServletRequest request){
		Map<String, Object> json = HttpRequstParmsUtil.getParmsForMap(request);
		boolean vali = Validate.validate(json, "videoId", "continueTime");
		if(!vali)
			return Code.init(false, 1, "参数丢失！");
		String videoId = json.get("videoId").toString();
		String continueTime = json.get("continueTime").toString();
		String userId = json.get("userid").toString();
		Code result = ps.saveOrUpdateRecord(userId, videoId, continueTime);
		return result;
	}
	
	/**
	 * 获取用户的播放记录列表
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="getUserRecords",method=RequestMethod.POST)
	public Object getUserRecords(HttpServletRequest request){
		Map<String, Object> json = HttpRequstParmsUtil.getParmsForMap(request);
		String userId = json.get("userid").toString();
		Code result = ps.getUserPlayRecords(userId);
		return Send.send(result);
	}
	
	/**
	 * 批量删除用户的播放记录
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="deleteRecords",method=RequestMethod.POST)
	public Object deleteRecords(HttpServletRequest request){
		String ip = NetworkUtil.getIpAddr(request);
		if(ip==null)
			return Send.send(Code.init(false, 100, "未知错误！"));
		if(Associate.isDoing(ip, "palyrecord_delete"))
			return Send.send(Code.init(false, 101, "正在删除，请稍后..."));
		
		Associate.save(ip, "palyrecord_delete");
		
		Code result = deleteSupport(request);
		
		Associate.clear(ip, "palyrecord_delete");
		
		return Send.send(result);
	}
	
	/**
	 * 删除的核心方法
	 * @param request
	 * @return
	 */
	private Code deleteSupport(HttpServletRequest request){
		Map<String, Object> json = HttpRequstParmsUtil.getParmsForMap(request);
		boolean vali = Validate.validate(json, "playRecordId");
		if(!vali)
			return Code.init(false, 1, "参数丢失！");
		String playRecordId = json.get("playRecordId").toString();
		String[] playRecordIds = playRecordId.split(",");
		if(playRecordIds==null||playRecordIds.length<1)
			return Code.init(false, 6, "暂无播放记录");
		Code result = ps.deleteRecordsByIds(playRecordIds);
		return result;
	}
	
}
