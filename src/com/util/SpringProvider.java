package com.util;

import javax.servlet.ServletContext;

import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

public class SpringProvider implements BeanProvider {
    private static ApplicationContext context = null;
    private static final SpringProvider s = new SpringProvider();
    
    private SpringProvider(){
    	
    }
    
    final static SpringProvider getInstance(){
    	return s;
    }
	
    final void initContext(ServletContext sc){
		synchronized(this){
			if(context == null){
				context = WebApplicationContextUtils.getRequiredWebApplicationContext(sc);
				
			}
		}
	}
	
	@Override
	public <T> T getBean(String name) throws Exception {
		T bean = (T)context.getBean(name);
		return bean;
	}
}
