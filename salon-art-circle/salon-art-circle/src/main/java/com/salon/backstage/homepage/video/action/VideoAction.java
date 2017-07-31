package com.salon.backstage.homepage.video.action;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.salon.backstage.homepage.video.service.IVideoService;

/**
 * 视频
 */

@Controller
@RequestMapping("/video")
public class VideoAction {
	
	@Autowired
	IVideoService videoService;
	
}
