package com.salon.backstage.homepage.task.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.salon.backstage.common.util.ExtraSpringHibernateTemplate;
import com.salon.backstage.homepage.task.service.ITaskService;

@Service
public class TaskServiceImpl implements ITaskService {
	Logger logger = LoggerFactory.getLogger("系统调度");
	@Autowired
	private ExtraSpringHibernateTemplate extraSpringHibernateTemplate;
	/***
	 * 定时器测试
	 */
	@Override
	public void addTest() {
		/*Follow f=new Follow();
		f.setFollow_time(System.currentTimeMillis()*1000);
		f.setFollow_type(1);
		f.setUser_id("123");*/
		//extraSpringHibernateTemplate.getHibernateTemplate().save(f);
		logger.info(" 定时器测试");
	}
}









