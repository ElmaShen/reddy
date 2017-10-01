package com.util;

public abstract class BeanProviderFactory {
    static BeanProvider provider = null;
	public final static BeanProvider getBeanProvider(){
       if(provider != null){
    	   return provider;
       }else{
    	   throw new NullPointerException("尚未初始化BeanProvider");
       }
    }
}
