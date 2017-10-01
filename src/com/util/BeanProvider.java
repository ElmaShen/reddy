package com.util;

public interface BeanProvider {
	public <T> T getBean(String name) throws Exception;
}
