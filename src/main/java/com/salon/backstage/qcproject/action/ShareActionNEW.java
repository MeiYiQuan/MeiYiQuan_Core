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
import com.salon.backstage.qcproject.service.ShareServiceNEW;
import com.salon.backstage.qcproject.util.Code;
import com.salon.backstage.qcproject.util.Enums.ShareDistrict;
import com.salon.backstage.qcproject.util.Enums.ShareType;
import com.salon.backstage.qcproject.util.Send;

/**
 * 作者：齐潮
 * 创建日期：2017年1月13日
 * 类说明：处理有关分享的回调
 */
@Controller
@RequestMapping(value="share")
public class ShareActionNEW {

	@Autowired
	private ShareServiceNEW ss;
	
	/**
	 * 分享成功后回调的接口
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="shared",method=RequestMethod.POST)
	public Object shared(HttpServletRequest request){
		String ip = NetworkUtil.getIpAddr(request);
		if(ip==null)
			return Send.send(Code.init(true, 0, ""));
		if(Associate.isDoing(ip, "share_shared"))
			return Send.send(Code.init(true, 0, ""));
		
		Associate.save(ip, "share_shared");
		
		Code result = sharedSupport(request);
		
		Associate.clear(ip, "share_shared");
		
		return Send.send(result);
	}
	
	/**
	 * 分享成功回调的核心方法
	 * @param request
	 * @return
	 */
	private Code sharedSupport(HttpServletRequest request){
		Map<String, Object> json = HttpRequstParmsUtil.getParmsForMap(request);
		boolean vali = Validate.validate(json, "shareId", "type", "district");
		if(!vali)
			return Code.init(false, 1, "参数丢失！");
		String userId = json.get(Send.USERID_NAME).toString();
		String shareId = json.get("shareId").toString();
		IsInt type = MathUtil.isToInteger(json.get("type").toString());
		IsInt district = MathUtil.isToInteger(json.get("district").toString());
		if(!type.is||ShareType.getShareType(type.value)==null||!district.is||ShareDistrict.getShareDistrict(district.value)==null)
			return Code.init(false, 2, "参数不合法！");
		ShareType st = ShareType.getShareType(type.value);
		ShareDistrict distri = ShareDistrict.getShareDistrict(district.value);
		Code result = ss.sharedHook(userId, shareId, st, distri);
		return result;
	}
	
}
