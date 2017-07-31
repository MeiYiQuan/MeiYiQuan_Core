package com.salon.backstage.common.web.action;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.salon.backstage.common.util.Application;
import com.salon.backstage.common.util.Configs;

public class IncubatorServletContextListener implements ServletContextListener {
	private static final Logger log = LoggerFactory.getLogger(IncubatorServletContextListener.class);

	@Override
	public void contextDestroyed(ServletContextEvent event) {
		log.info("contextDestroyed");
	}

	@Override
	public void contextInitialized(ServletContextEvent event) {
		log.info("contextInitialized");
		event.getServletContext().setAttribute(Application.attr_name_site_name,
				Configs.get().getString("system.site_name"));
	}
}
