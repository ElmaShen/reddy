package com.util;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.springframework.web.context.ContextLoaderListener;

//Adapter design pattern
public class SpringProviderListener extends ContextLoaderListener implements
		ServletContextListener {

	@Override
	public void contextDestroyed(ServletContextEvent arg0) {
		// TODO Auto-generated method stub
	}

	@Override
	public void contextInitialized(ServletContextEvent arg0) {
		super.contextInitialized(arg0);		
		SpringProvider.getInstance().initContext(arg0.getServletContext());
		BeanProviderFactory.provider = SpringProvider.getInstance();
	}

}
