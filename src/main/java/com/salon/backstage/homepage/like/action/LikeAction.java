package com.salon.backstage.homepage.like.action;

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
import com.salon.backstage.common.util.NetworkUtil;
import com.salon.backstage.common.util.Validate;
import com.salon.backstage.homepage.like.service.ILikeService;
import com.salon.backstage.pub.bsc.dao.vo.MobileMessage;
import com.salon.backstage.qcproject.util.Code;
import com.salon.backstage.qcproject.util.Send;
import com.salon.backstage.qcproject.util.Statics;

@Controller
@RequestMapping("/like")
public class LikeAction {
	
	@Autowired
	ILikeService likeService;
	
	/**
	 * 处理从即将上映详情页传来的点赞请求
	 */
	@ResponseBody
	@RequestMapping("/clickLikeFromPlayingsoon.do")
	public Object clickLikeFromPlayingsoon(HttpServletRequest request) {
		String ip = NetworkUtil.getIpAddr(request);
		if(ip==null)
			return Send.send(Code.init(false, 100, "未知错误！"));
		if(Associate.isDoing(ip, "like_click"))
			return Send.send(Code.init(false, 101, "正在执行，请稍后..."));
		
		Associate.save(ip, "like_click");
		
		Object result = clickSupport(request);
		
		Associate.clear(ip, "like_click");
		
		return Send.send(result);
	}
	
	
	private Object clickSupport(HttpServletRequest request){
		Map<String, Object> json = HttpRequstParmsUtil.getParmsForMap(request);
		boolean vali = Validate.validate(json, "likedId", "likeType", "type");
		if(!vali)
			return Code.init(false, 1, "参数丢失！");
		IsInt likeType = MathUtil.isToInteger(json.get("likeType").toString());
		IsInt type = MathUtil.isToInteger(json.get("type").toString());
		String likeId = json.get("likedId").toString();
		String userId = json.get(Send.USERID_NAME).toString();
		int index = json.get("index")==null||json.get("index").equals("")?0:Integer.parseInt(json.get("index").toString());
		if(!likeType.is||!type.is||(type.value!=Statics.LIKE_YES&&type.value!=Statics.LIKE_NOT)
				||(likeType.value!=Statics.LIKE_TYPE_ACTIVITY&&likeType.value!=Statics.LIKE_TYPE_COMMENT
						&&likeType.value!=Statics.LIKE_TYPE_COURSE&&likeType.value!=Statics.LIKE_TYPE_TEACHER))
			return Code.init(false, 2, "参数不合法！");
		MobileMessage result = likeService.click(userId, likeId, type.value, likeType.value,index);
		return result;
	}
	
	
	
}










