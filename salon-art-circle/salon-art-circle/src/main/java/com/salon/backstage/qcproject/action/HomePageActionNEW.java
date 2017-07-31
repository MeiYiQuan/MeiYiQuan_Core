package com.salon.backstage.qcproject.action;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.salon.backstage.qcproject.service.HomePageServiceNEW;
import com.salon.backstage.qcproject.util.Code;
import com.salon.backstage.qcproject.util.Send;

/**
 * 作者：齐潮
 * 创建日期：2017年1月5日
 * 类说明：处理有关首页的请求
 */
@Controller
@RequestMapping(value="homepagenew")
public class HomePageActionNEW {

	@Autowired
	private HomePageServiceNEW hs;
	
	/**
	 * 获取首页-正在热播的所有内容，轮播图，推荐视频，名人大佬，创业开店，学潮流技术
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="playing",method=RequestMethod.POST)
	public Object getCollectsByType(HttpServletRequest request){
		Code result = hs.getHomePageAll();
		return Send.send(result);
	}
	
}
