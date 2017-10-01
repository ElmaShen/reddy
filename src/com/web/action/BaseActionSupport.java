package com.web.action;

import com.opensymphony.xwork2.ActionSupport;
import com.util.BeanProviderFactory;


public class BaseActionSupport extends ActionSupport {
	private static final long serialVersionUID = 1L;
	
	@SuppressWarnings("unchecked")
	static <T> T getBean(String name) {
		T t;
		try {
			t = BeanProviderFactory.getBeanProvider().<T>getBean(name);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		return t;
	}
}
