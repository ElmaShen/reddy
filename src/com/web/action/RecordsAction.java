package com.web.action;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

import org.apache.commons.lang.StringUtils;
import org.apache.struts2.interceptor.ServletRequestAware;

import com.web.dao.entity.Account;
import com.web.dao.entity.Func;
import com.web.dao.model.PageBean;
import com.web.service.SystemService;

public class RecordsAction extends BaseActionSupport implements ServletRequestAware {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 7822620710398779427L;
	private static final String SESSION_LOGIN_USER = "LOGIN_USER";
	
	private SystemService systemService = RecordsAction.<SystemService>getBean("systemService");
	private String success;
	private String message;
	private List<Func> funcs;
	
	private String shName;
	private String shStartDate;
	private String shEndDate;
	private Integer page;
	private Integer pageSize;
	private PageBean pageBean;
	
	
	
	private javax.servlet.http.HttpServletRequest request;
	public void setServletRequest(javax.servlet.http.HttpServletRequest request) {
		this.request = request;
	}
	
	
	
	public String records(){
		Account user = (Account)request.getSession().getAttribute(SESSION_LOGIN_USER);
		funcs = this.systemService.queryFuncByAuths(user.getAccount());
		
		this.systemService.updateSysRecord(user, "操作記錄【查詢】：", "");
		return SUCCESS;
	}
	
	/**
	 * 列表頁
	 * @return
	 */
	public String recordList() {
		Account user = (Account)request.getSession().getAttribute(SESSION_LOGIN_USER);
		funcs = this.systemService.queryFuncByAuths(user.getAccount());
		
		Date sDate = null, eDate = null;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			if(StringUtils.isNotEmpty(shStartDate)){
				sDate = sdf.parse(shStartDate + " 00:00:00");
			}
			if(StringUtils.isNotEmpty(shEndDate)){
				eDate = sdf.parse(shEndDate + " 23:59:59");
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		if(page == null || page == 0){
			page = 1;
		}
		pageSize = Integer.parseInt(loadConfig("page.size"));
		pageBean = this.systemService.querySysRecordByPage(shName, sDate, eDate, pageSize, page);
		return SUCCESS;
	}

	
	
	private String loadConfig(String param) {
		ResourceBundle rb = ResourceBundle.getBundle("config");
		return rb.getString(param);
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
	public List<Func> getFuncs() {
		return funcs;
	}
	public void setFuncs(List<Func> funcs) {
		this.funcs = funcs;
	}
	public String getShName() {
		return shName;
	}
	public void setShName(String shName) {
		this.shName = shName;
	}
	public String getShStartDate() {
		return shStartDate;
	}
	public void setShStartDate(String shStartDate) {
		this.shStartDate = shStartDate;
	}
	public String getShEndDate() {
		return shEndDate;
	}
	public void setShEndDate(String shEndDate) {
		this.shEndDate = shEndDate;
	}
	public Integer getPage() {
		return page;
	}
	public void setPage(Integer page) {
		this.page = page;
	}
	public Integer getPageSize() {
		return pageSize;
	}
	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}
	public PageBean getPageBean() {
		return pageBean;
	}
	public void setPageBean(PageBean pageBean) {
		this.pageBean = pageBean;
	}
}