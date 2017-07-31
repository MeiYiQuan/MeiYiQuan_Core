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
import com.salon.backstage.qcproject.service.CollectServiceNEW;
import com.salon.backstage.qcproject.util.Code;
import com.salon.backstage.qcproject.util.Send;
import com.salon.backstage.qcproject.util.Statics;

/**
 * 作者：齐潮
 * 创建日期：2016年12月27日
 * 类说明：处理有关收藏的请求
 */
@Controller
@RequestMapping(value="collectnew")
public class CollectActionNEW {

	@Autowired
	private CollectServiceNEW cs;
	
	
	/**
	 * 获取我的收藏列表，根据传入的type来区分列表类型
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="myCollect",method=RequestMethod.POST)
	public Object getCollectsByType(HttpServletRequest request){
		Map<String, Object> json = HttpRequstParmsUtil.getParmsForMap(request);
		boolean vali = Validate.validate(json, "pageNo", "pageSize", "type");
		if(!vali)
			return Send.send(Code.init(false, 1, "参数丢失！"));
		IsInt pageNo = MathUtil.isToInteger(json.get("pageNo").toString());
		IsInt pageSize = MathUtil.isToInteger(json.get("pageSize").toString());
		IsInt type = MathUtil.isToInteger(json.get("type").toString());
		if(!pageNo.is||!pageSize.is||!type.is||pageNo.value<1||pageSize.value<1
				||(type.value!=Statics.COLLECT_TYPE_HD&&type.value!=Statics.COLLECT_TYPE_MRDK&&type.value!=Statics.COLLECT_TYPE_SP))
			return Send.send(Code.init(false, 2, "参数不合法！"));
		Code result = cs.getCollectsByUserId(json.get("userid").toString(), pageNo.value, pageSize.value, type.value);
		return Send.send(result);
	}
	
	/**
	 * 收藏与取消收藏
	 * @param request
	 * @return
	 * @throws Exception 
	 */
	@ResponseBody
	@RequestMapping(value="collect",method=RequestMethod.POST)
	public Object doingCollect(HttpServletRequest request) throws Exception{
		String ip = NetworkUtil.getIpAddr(request);
		if(ip==null)
			return Send.send(Code.init(false, 100, "未知错误！"));
		if(Associate.isDoing(ip, "collect_doing"))
			return Send.send(Code.init(false, 101, "正在执行，请稍后..."));
		
		Associate.save(ip, "collect_doing");
		
		Code result = doingCollectSupport(request);
		
		Associate.clear(ip, "collect_doing");
		
		return Send.send(result);
	}
	
	/**
	 * 处理收藏与取消收藏的核心方法
	 * @param request
	 * @return
	 * @throws Exception 
	 */
	private Code doingCollectSupport(HttpServletRequest request) throws Exception{
		Map<String, Object> json = HttpRequstParmsUtil.getParmsForMap(request);
		boolean vali = Validate.validate(json, "collectId", "type");
		if(!vali)
			return Code.init(false, 1, "参数丢失！");
		String userId = json.get("userid").toString();
		String collectId = json.get("collectId").toString();
		IsInt type = MathUtil.isToInteger(json.get("type").toString());
		if(!type.is||(type.value!=Statics.COLLECT_TYPE_HD&&type.value!=Statics.COLLECT_TYPE_MRDK&&type.value!=Statics.COLLECT_TYPE_SP))
			return Code.init(false, 2, "参数不合法！");
		Code result = cs.doing(userId, type.value, collectId);
		return result;
	}
	
	/**
	 * 批量删除收藏信息
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="myCollectEdit",method=RequestMethod.POST)
	public Object deleteCollectsByIds(HttpServletRequest request){
		String ip = NetworkUtil.getIpAddr(request);
		if(ip==null)
			return Send.send(Code.init(false, 100, "未知错误！"));
		if(Associate.isDoing(ip, "collect_delete"))
			return Send.send(Code.init(false, 101, "正在删除，请稍后..."));
		
		Associate.save(ip, "collect_delete");
		
		Code result = deleteSupport(request);
		
		Associate.clear(ip, "collect_delete");
		
		return Send.send(result);
	}
	
	/**
	 * 删除收藏信息的核心方法
	 * @param request
	 * @return
	 */
	private Code deleteSupport(HttpServletRequest request){
		Map<String, Object> json = HttpRequstParmsUtil.getParmsForMap(request);
		boolean vali = Validate.validate(json, "collectType", "collectTypeIds");
		if(!vali)
			return Code.init(false, 1, "参数丢失！");
		String userId = json.get("userid").toString();
		IsInt collectType = MathUtil.isToInteger(json.get("collectType").toString());
		String[] collectTypeIds = json.get("collectTypeIds").toString().split(",");
		if(!collectType.is||(collectType.value!=Statics.COLLECT_TYPE_HD&&collectType.value!=Statics.COLLECT_TYPE_MRDK&&collectType.value!=Statics.COLLECT_TYPE_SP)
				||collectTypeIds==null||collectTypeIds.length<1||collectTypeIds[0].trim().equals(""))
			return Code.init(false, 2, "参数不合法！");
		Code result = cs.delete(userId, collectType.value, collectTypeIds);
		return result;
	}
	
}
