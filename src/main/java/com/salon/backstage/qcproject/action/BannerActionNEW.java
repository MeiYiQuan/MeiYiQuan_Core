package com.salon.backstage.qcproject.action;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.salon.backstage.pub.bsc.domain.Constant;
import com.salon.backstage.qcproject.service.BannerServiceNEW;
import com.salon.backstage.qcproject.util.Code;
import com.salon.backstage.qcproject.util.Send;
import com.salon.backstage.qcproject.util.Statics;

/**
 * 作者：齐潮
 * 创建日期：2017年1月5日
 * 类说明：处理有关轮播图的请求
 */
@Controller
@RequestMapping(value="bannernew")
public class BannerActionNEW {

	@Autowired
	private BannerServiceNEW bs;
	
	/**
	 * 在发现页面获取轮播图
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="getfindbanners",method=RequestMethod.POST)
	public Object getBanners(){
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("status", Constant.YES_INT);
		params.put("showtype", Statics.BANNER_SHOWTYPE_FIND);
		Code result = bs.getBannersByShowType(params);
		return Send.send(result);
	}
	
}
