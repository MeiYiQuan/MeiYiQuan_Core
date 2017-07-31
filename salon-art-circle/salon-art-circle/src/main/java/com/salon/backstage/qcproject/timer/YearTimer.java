package com.salon.backstage.qcproject.timer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.salon.backstage.qcproject.service.TimerService;

/**
 * 作者：齐潮
 * 创建日期：2017年2月28日
 * 类说明：每年执行一次的定时任务，用于更新用户年龄
 */
public class YearTimer {

	private final static Logger logger = LoggerFactory.getLogger(YearTimer.class);
	
	@Autowired
	private TimerService ts;
	
	/**
	 * 更新年龄
	 */
	public void updateUserAge(){
		int result = ts.updateUserAge();
		logger.debug("定时任务(每年1月1日00:00:00执行)：更新年龄结果  " + result);
	}
	
}
