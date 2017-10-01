package com.web.interceptor;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.AbstractInterceptor;
import com.web.dao.entity.Account;


public class AuthorizationInterceptor extends AbstractInterceptor {
    
	/**
	 * 
	 */
	private static final long serialVersionUID = 1664716847701015778L;
	private Logger log = Logger.getLogger(AuthorizationInterceptor.class);
    private long logonTimeOut;

    public String intercept(ActionInvocation invocation) throws Exception {
        String resultName = "Logout";	//導頁設定
		ActionContext actionContext = invocation.getInvocationContext();
		HttpServletRequest request = (HttpServletRequest) actionContext.get(ServletActionContext.HTTP_REQUEST);
        //HttpServletResponse response = (HttpServletResponse)actionContext.get(ServletActionContext.HTTP_RESPONSE);
        
		Account user = (Account)request.getSession().getAttribute("LOGIN_USER");
		if (user != null){  
			resultName = invocation.invoke();
		}
		return resultName;
    }
    
	public long getLogonTimeOut() {
		return logonTimeOut;
	}
	public void setLogonTimeOut(long logonTimeOut) {
		this.logonTimeOut = logonTimeOut;
	}
}