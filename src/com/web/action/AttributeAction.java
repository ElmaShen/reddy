package com.web.action;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import org.apache.struts2.interceptor.ServletRequestAware;

import com.web.dao.entity.Account;
import com.web.dao.entity.Attribute;
import com.web.dao.entity.Authority;
import com.web.dao.entity.Func;
import com.web.dao.model.AttributeType;
import com.web.dao.model.PageBean;
import com.web.service.SystemService;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class AttributeAction extends BaseActionSupport implements ServletRequestAware {
	/**
	 * 
	 */
	private static final long serialVersionUID = 7822620710398779427L;
	private static final String SESSION_LOGIN_USER = "LOGIN_USER";
	
	private SystemService systemService = AttributeAction.<SystemService>getBean("systemService");
	private String success;
	private String message;
	private List<Func> funcs;
	private String shType;
	private String shName;
	
	private Integer page; // 頁數
	private Integer pageSize; // 一頁筆數
	private PageBean pageBean;
	
	private long id;
	private Attribute attribute;
	
	
	private javax.servlet.http.HttpServletRequest request;
	public void setServletRequest(javax.servlet.http.HttpServletRequest request) {
		this.request = request;
	}
	
	
	
	public String attribute(){
		Account user = (Account)request.getSession().getAttribute(SESSION_LOGIN_USER);
		funcs = this.systemService.queryFuncByAuths(user.getAccount());
		return SUCCESS;
	}
	
	/**
	 * 列表頁
	 * @return
	 */
	public String attributeList() {
		Account user = (Account)request.getSession().getAttribute(SESSION_LOGIN_USER);
		funcs = this.systemService.queryFuncByAuths(user.getAccount());
		
		if(page == null || page == 0){
			page = 1;
		}
		pageSize = Integer.parseInt(loadConfig("page.size"));
		pageBean = this.systemService.queryAttributeByPage(shType, shName, pageSize, page);
		for(int idx=0; idx<pageBean.getList().size(); idx++){
			Attribute a = (Attribute)pageBean.getList().get(idx);
			a.setTypeName(this.attrCombo().get(a.getType()));
		}
		return SUCCESS;
	}
	
	/**
	 * 取得帳號資料
	 * @return
	 */
	public String queryAttribute(){
		attribute = this.systemService.queryAttributeById(id);
		return SUCCESS;
	}
	
	
	/**
	 * 屬性設定
	 * @return
	 */
	public String editAttribute(){
		if(attribute == null){
			success = "N";
			message = "傳入參數有誤,請重新設定";
			return SUCCESS;
		}
		
		if(attribute.getId() == 0){
			Attribute attr = this.systemService.queryAttributesByKey(attribute.getType(), attribute.getAttrKey(), attribute.getParentKey());
			if(attr != null){
				success = "N";
				message = "代碼已存在,請重新設定";
				return SUCCESS;
			}
			
			Account user = (Account)request.getSession().getAttribute(SESSION_LOGIN_USER);
			attribute.setCreator(user.getAccount());
			attribute.setCreateDate(new Date());
			this.systemService.updateAttribute(attribute);
		}else{
			Attribute attr = this.systemService.queryAttributeById(attribute.getId());
			if(attr != null){
				attr.setType(attribute.getType());
				attr.setAttrKey(attribute.getAttrKey());
				attr.setAttrName(attribute.getAttrName());
				attr.setIsuse(attribute.getIsuse());
				attr.setParentKey(attribute.getParentKey());
				this.systemService.updateAttribute(attr);
			}
		}
		success = "Y";
		message = "設定成功";
		return SUCCESS;
	}


	/**
	 * 屬性
	 * @return
	 */
	public Map<String, String> attrCombo() {
		Map<String, String> map = new LinkedHashMap<String, String>();
		map.put(AttributeType.area.name(), "地區");
		map.put(AttributeType.role.name(), "角色");
		map.put(AttributeType.tone.name(), "調性");
		map.put(AttributeType.skill.name(), "手法");
		map.put(AttributeType.voice.name(), "產業類別(音檔)");
		map.put(AttributeType.section.name(), "產業類別(個案)");
		map.put(AttributeType.category.name(), "產品分類");
		return map;
	}
	
	
	/**
	 * 產業類別(for 個案管理)
	 * @return
	 */
	public Map<String, String> sectionCombo() {
		List<Attribute> list = this.systemService.queryAttributesByType(AttributeType.section.name(), "Y");
		Map<String, String> map = new LinkedHashMap<String, String>();
		for (Attribute a : list) {
			map.put(a.getAttrKey(), a.getAttrName());
		}
		return map;
	}
	
	
	private String loadConfig(String param) {
		ResourceBundle rb = ResourceBundle.getBundle("config");
		return rb.getString(param);
	}
	
	

	public List<Func> getFuncs() {
		return funcs;
	}
	public void setFuncs(List<Func> funcs) {
		this.funcs = funcs;
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
	public String getShType() {
		return shType;
	}
	public void setShType(String shType) {
		this.shType = shType;
	}
	public String getShName() {
		return shName;
	}
	public void setShName(String shName) {
		this.shName = shName;
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
	public Attribute getAttribute() {
		return attribute;
	}
	public void setAttribute(Attribute attribute) {
		this.attribute = attribute;
	}
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
}