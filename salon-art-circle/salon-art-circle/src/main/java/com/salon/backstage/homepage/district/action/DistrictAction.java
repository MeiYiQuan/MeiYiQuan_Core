package com.salon.backstage.homepage.district.action;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.salon.backstage.common.util.HttpRequstParmsUtil;
import com.salon.backstage.common.util.MobileMessageCondition;
import com.salon.backstage.homepage.district.service.IDistrictService;
import com.salon.backstage.qcproject.util.Send;

@Controller
@SuppressWarnings("all")
@RequestMapping("/district")
public class DistrictAction {
	
	@Autowired
	IDistrictService districtService;
	
	/**
	 * 返回用户注册时需要的省份信息 的接口
	 */
	@RequestMapping("/register.do")
	@ResponseBody
	public Object provRegister(HttpServletRequest request){
		 Map<String, Object> json = HttpRequstParmsUtil.getParmsForMap(request);
		 List<Map> distList = districtService.queryProv();
		 return Send.send(MobileMessageCondition.addCondition(true, 0, "成功", distList));
	}
	
	/**
	 * 返回用户注册时需要的详细信息 的接口
	 */
	@RequestMapping("/registerDetail.do")
	@ResponseBody
	public Object districtRegister(HttpServletRequest request){
		Map<String, Object> json = HttpRequstParmsUtil.getParmsForMap(request);
		String districtId = (String)json.get("districtId");
		List<Map> distList = districtService.queryDetail(districtId);
		return Send.send(MobileMessageCondition.addCondition(true, 0, "成功", distList));
	}
	
	
	
	
	
	
	
}




