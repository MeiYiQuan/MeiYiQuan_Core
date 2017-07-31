package com.salon.backstage.homepage.channel.action;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.salon.backstage.common.util.HttpRequstParmsUtil;
import com.salon.backstage.common.util.MobileMessageCondition;
import com.salon.backstage.homepage.channel.service.IChannelService;
import com.salon.backstage.pub.bsc.domain.Constant;
import com.salon.backstage.pub.bsc.domain.Constant.ChannelTop;
import com.salon.backstage.qcproject.util.Send;

@Controller
@RequestMapping("/channel")
public class ChannelAction {
	
	@Autowired
	IChannelService channelService;
	
	/**
	 * 频道
	 * type:类型(1 创业开店,2 潮流技术)
	 */
	@RequestMapping("/list.do")
	@ResponseBody
	public Object startshop(HttpServletRequest request){
		Map<String, Object> json = HttpRequstParmsUtil.getParmsForMap(request);
		int type = Integer.valueOf((String)json.get("type"));
		List<Map<String,Object>> channelList = null;
		if(type == 1){
			channelList = channelService.queryStartshopList();
		}else{
			channelList = channelService.queryTechnologyList();
		}
		return Send.send(MobileMessageCondition.addCondition(true, 0, "成功", channelList));
	}
	
	/**
	 * 获得所有的二级频道
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="getTwoLevels",method=RequestMethod.POST)
	public Object getAllTwoLevels(){
		List<Map<String, Object>> list = channelService.getChannelsByTopType(ChannelTop.CHANNEL_TOP_ALL);
		List<Map<String, Object>> twoLevels = new ArrayList<Map<String,Object>>();
		if(list==null||list.size()<1)
			return Send.send(MobileMessageCondition.addCondition(true, 0, null, twoLevels));
		for(int i=0;i<list.size();i++){
			Map<String,Object> map = list.get(i);
			int type = Integer.parseInt(map.get("type").toString());
			if(type==Constant.CHANNEL_TWO_LEVEL){
				map.remove("pid");
				map.remove("enable");
				map.remove("create_time");
				map.remove("update_time");
				map.remove("update_admin_id");
				map.remove("logo_url");
				map.remove("type");
				twoLevels.add(map);
			}
		}
		return Send.send(MobileMessageCondition.addCondition(true, 0, null, twoLevels));
	}
	
}




















