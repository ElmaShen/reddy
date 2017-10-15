package com.web.action;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.struts2.interceptor.ServletRequestAware;

import com.util.DateUtils;
import com.web.dao.entity.Account;
import com.web.dao.entity.Authority;
import com.web.dao.entity.Func;
import com.web.service.SystemService;

public class LogonAction extends BaseActionSupport implements ServletRequestAware {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 7822620710398779427L;
	private static final String SESSION_LOGIN_USER = "LOGIN_USER";
	
	private SystemService systemService = LogonAction.<SystemService>getBean("systemService");
	private String success;
	private String message;
	private String actionName;
	
	private String account;
	private String password;
	
	
	private javax.servlet.http.HttpServletRequest request;
	public void setServletRequest(javax.servlet.http.HttpServletRequest request) {
		this.request = request;
	}
	
	
	
	/**
	 * 登入
	 * @return
	 */
	public String login() {
		try {
			if(StringUtils.isEmpty(account) || StringUtils.isEmpty(password)){
				return "input";
			}
			
			Account	user = this.systemService.queryAccount(account);
			if(user == null){
				message = "查無使用者帳號!";
				return "input";
			}else{
				user.setLoginDate(new Date());
				this.systemService.updateAccount(user);
				//權限
				List<Authority> auth = this.systemService.queryAuthorityByAccount(account);
				if(auth != null && auth.size() > 0){
					Map<String, Authority> map = new HashMap<String, Authority>();
					for (Authority a : auth) {
						map.put(a.getFno(), a);
					}
					user.setAuthority(map);
				}
				
				if(!password.equals(user.getPassword())){
					message = "登入密碼錯誤!";
					this.systemService.updateSysRecord(user, "登入失敗", "密碼錯誤");
					return "input";
				}
				if("N".equals(user.getIsuse())){
					message = "此帳號目前停止使用!";
					this.systemService.updateSysRecord(user, "登入失敗", "帳號停用");
					return "input";
				}
				this.systemService.updateSysRecord(user, "登入成功", "");
			}
			
			request.getSession().setAttribute(SESSION_LOGIN_USER, user);
			List<Func> list = this.systemService.queryFuncByAuths(user.getAccount());
			if(list != null && list.size() > 0){
				Func f = list.get(0);
				int idx = f.getAction().indexOf(".");
				actionName = f.getAction().substring(1,idx);
			}else{
				actionName = "index";
			}
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage(), e);
		}
		return SUCCESS;
	}
	
	
	/**
	 * 登出
	 * @return
	 */
	public String logout(){
		Account user = (Account)request.getSession().getAttribute(SESSION_LOGIN_USER);
		if(user != null){
			request.getSession().removeAttribute(SESSION_LOGIN_USER);
			
			long time = new Date().getTime() - user.getLoginDate().getTime(); 
			String tStr = DateUtils.millisToHMS(time);
			this.systemService.updateSysRecord(user, "登出成功", "登入平台共歷時：" + tStr);
		}
		return SUCCESS;
	}
	
	
	/**
	 * 無權首頁
	 * @return
	 */
	public String index(){
		return SUCCESS;
	}

	
	
	public String getSuccess() {
		return success;
	}
	public void setSuccess(String success) {
		this.success = success;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public String getActionName() {
		return actionName;
	}
	public void setActionName(String actionName) {
		this.actionName = actionName;
	}
	public String getAccount() {
		return account;
	}
	public void setAccount(String account) {
		this.account = account;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
}