
package com.salon.backstage.find.web.action;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.salon.backstage.common.cache.RedisClient;
import com.salon.backstage.find.IFindService;
import com.salon.backstage.pub.bsc.dao.vo.MobileMessage;

@Controller
@RequestMapping("/find")
public class FindAction {
	Logger log = LoggerFactory.getLogger(FindAction.class);
	@Autowired
	private RedisClient redisClient;
	@Autowired
	private IFindService findService;
	@Autowired
	private MobileMessage mobileMessage;
	
	@ResponseBody
	@RequestMapping("/offlineActivity.do")
	public Object offlineActivity(HttpServletRequest request){
		Map<String,List> map=new HashMap<>();
	
		return request;
		
	
		
	}
	
}
