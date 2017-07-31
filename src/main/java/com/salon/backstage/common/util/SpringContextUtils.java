package com.salon.backstage.common.util;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

public class SpringContextUtils implements ApplicationContextAware {
	private static ApplicationContext app_context;

	@Override
	public void setApplicationContext(ApplicationContext appContext) throws BeansException {
		app_context = appContext;
	}

	public static ApplicationContext getApplicationContext() {
		return app_context;
	}
}
