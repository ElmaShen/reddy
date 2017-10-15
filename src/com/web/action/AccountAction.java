package com.web.action;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

import org.apache.struts2.interceptor.ServletRequestAware;

import com.web.dao.entity.Account;
import com.web.dao.entity.Attribute;
import com.web.dao.entity.Authority;
import com.web.dao.entity.Func;
import com.web.dao.entity.Sound;
import com.web.dao.model.AttributeType;
import com.web.dao.model.PageBean;
import com.web.service.SystemService;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class AccountAction extends BaseActionSupport implements ServletRequestAware {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 6368525219244451286L;
	private static final String SESSION_LOGIN_USER = "LOGIN_USER";
	
	private SystemService systemService = AccountAction.<SystemService>getBean("systemService");
	private String success;
	private String message;
	
	private long id;
	private String secType;
	private List<String> sections;
	private String shAccount;
	private String shName;
	private String shIsuse;
	
	private List<Func> allFuncs;
	private List<Func> funcs;
	private Account account;
	private String authVoice;
	private String authSec;
	private String authJson;
	private Integer page;
	private Integer pageSize;
	private PageBean pageBean;
	
	private javax.servlet.http.HttpServletRequest request;
	public void setServletRequest(javax.servlet.http.HttpServletRequest request) {
		this.request = request;
	}
	
	public String account(){
		allFuncs = this.systemService.queryAllFuncs();
		
		Account user = (Account)request.getSession().getAttribute(SESSION_LOGIN_USER);
		funcs = this.systemService.queryFuncByAuths(user.getAccount());
		return SUCCESS;
	}
	
	/**
	 * 取得產業類別
	 * 1.音檔 / 2.個案
	 * @return
	 */
	public String querySections(){
		List<Attribute> list = null;
		if("1".equals(secType)){
			list = this.systemService.queryAttributesByType(AttributeType.voice.name(), "Y");
		}
		if("2".equals(secType)){
			list = this.systemService.queryAttributesByType(AttributeType.section.name(), "Y");
		}
		if(list != null && list.size() > 0){
			sections = new ArrayList<String>();
			for (Attribute a : list) {
				sections.add(a.getAttrKey() + "." + a.getAttrName());
			}
		}
		return SUCCESS;
	}
	
	/**
	 * 取得帳號資料
	 * @return
	 */
	public String queryAccount(){
		account = this.systemService.queryAccountById(id);
		if(account != null){
			List<Authority> auths = this.systemService.queryAuthorityByAccid(account.getId());
			JSONArray jary = new JSONArray();
			for (Authority a : auths) {
				JSONObject json = new JSONObject();
				json.put("fno", a.getFno());
				json.put("added", a.getAdded());
				json.put("deleted", a.getDeleted());
				json.put("download", a.getDownload());
				json.put("voice", a.getVoice());
				json.put("section", a.getSection());
				jary.add(json);
			}
			authJson = jary.toString();
		}
		return SUCCESS;
	}
	
	/**
	 * 列表頁
	 * @return
	 */
	public String accountList() {
		allFuncs = this.systemService.queryAllFuncs();
		Account user = (Account)request.getSession().getAttribute(SESSION_LOGIN_USER);
		funcs = this.systemService.queryFuncByAuths(user.getAccount());
		
		if(page == null || page == 0){
			page = 1;
		}
		pageSize = Integer.parseInt(loadConfig("page.size"));
		pageBean = this.systemService.queryAccountByPage(shAccount, shName, shIsuse, pageSize, page);
		return SUCCESS;
	}
	
	
	/**
	 * 編輯帳號
	 * @return
	 */
	public String editAccount() {
		if(account == null){
			message = "傳入參數有誤,請重新設定";
			success = "N";
			return SUCCESS;
		}
		
		Account acc = this.systemService.queryAccount(account.getAccount());
		if(account.getId() == 0 && acc != null){
			message = "帳號:" +account.getAccount()+ "已存在,請重新設定";
			success = "N";
		}else{
			Account user = (Account)request.getSession().getAttribute(SESSION_LOGIN_USER);
			String type = "";
			long accId = 0;
			if(account.getId() == 0){
				type = "新增";
				
				account.setCreator(user.getAccount());
				account.setCreateDate(new Date());
				this.systemService.updateAccount(account);
				accId = account.getId();
			}else{
				type = "編輯";
				
				Account ac = this.systemService.queryAccountById(account.getId());
				ac.setAccount(account.getAccount());
				ac.setName(account.getName());
				ac.setPassword(account.getPassword());
				ac.setIsuse(account.getIsuse());
				this.systemService.updateAccount(ac);
				
				accId = ac.getId();
				//刪除之前已設定的權限
				List<Authority> list = this.systemService.queryAuthorityByAccid(ac.getId());
				for (Authority a : list) {
					this.systemService.deleteAuthority(a);
				}
			}
			
			JSONArray jsonArray = JSONArray.fromObject(authJson);
			for (int i = 0; i < jsonArray.size(); i++) {
				Authority auth = new Authority();
				
				JSONObject obj = jsonArray.getJSONObject(i);
				auth.setFno(obj.getString("fno"));
				auth.setAdded(obj.getString("added"));
				auth.setDeleted(obj.getString("deleted"));
				auth.setDownload(obj.getString("download"));
				auth.setVoice("F01".equals(obj.getString("fno"))?authVoice:"");	//音檔
				auth.setSection("F02".equals(obj.getString("fno"))?authSec:"");	//個案
				auth.setAccId(accId);
				auth.setAccount(account.getAccount());
				auth.setCreateDate(new Date());
				this.systemService.updateAuthority(auth);
			}
			this.systemService.updateSysRecord(user, "帳號管理【"+type+"】：", account.getAccount());
			
			message = "編輯成功";
			success = "Y";
		}
		return SUCCESS;
	}
	
	
	/**
	 * 刪除
	 * @return
	 */
	public String deleteAccount(){
		Account user = (Account)request.getSession().getAttribute(SESSION_LOGIN_USER);
		funcs = this.systemService.queryFuncByAuths(user.getAccount());
		
		Account acc = this.systemService.queryAccountById(id);
		this.systemService.deleteAccount(acc);
		this.systemService.updateSysRecord(user, "帳號管理【刪除】", acc.getAccount());
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
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getSecType() {
		return secType;
	}
	public void setSecType(String secType) {
		this.secType = secType;
	}
	public List<String> getSections() {
		return sections;
	}
	public void setSections(List<String> sections) {
		this.sections = sections;
	}
	public String getShAccount() {
		return shAccount;
	}
	public void setShAccount(String shAccount) {
		this.shAccount = shAccount;
	}
	public String getShName() {
		return shName;
	}
	public void setShName(String shName) {
		this.shName = shName;
	}
	public String getShIsuse() {
		return shIsuse;
	}
	public void setShIsuse(String shIsuse) {
		this.shIsuse = shIsuse;
	}
	public List<Func> getAllFuncs() {
		return allFuncs;
	}
	public void setAllFuncs(List<Func> allFuncs) {
		this.allFuncs = allFuncs;
	}
	public List<Func> getFuncs() {
		return funcs;
	}
	public void setFuncs(List<Func> funcs) {
		this.funcs = funcs;
	}
	public Account getAccount() {
		return account;
	}
	public void setAccount(Account account) {
		this.account = account;
	}
	public String getAuthVoice() {
		return authVoice;
	}
	public void setAuthVoice(String authVoice) {
		this.authVoice = authVoice;
	}
	public String getAuthSec() {
		return authSec;
	}
	public void setAuthSec(String authSec) {
		this.authSec = authSec;
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
	public String getAuthJson() {
		return authJson;
	}
	public void setAuthJson(String authJson) {
		this.authJson = authJson;
	}
}