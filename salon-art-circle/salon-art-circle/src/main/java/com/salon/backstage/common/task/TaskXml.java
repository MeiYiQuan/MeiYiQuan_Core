package com.salon.backstage.common.task;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.salon.backstage.homepage.task.service.ITaskService;

/**
 * 基于注解的定时器
 * 
 */
@Component
public class TaskXml {
	@Autowired
	ITaskService itaskService;

	/**
	 * 定时计算。每天凌晨 01:00 执行一次
	 */
	@Scheduled(cron = "0 01 21 * * *")
	public void show() {
		System.out.println("XMl:is show run");
	}
	
	/**
	 * 心跳更新。启动时执行一次，之后每隔2秒执行一次
	 */
	@Scheduled(fixedRate = 1000 * 60 * 60)
	public void print() {
		itaskService.addTest();
	}
}
