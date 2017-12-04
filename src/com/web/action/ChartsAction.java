package com.web.action;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.struts2.interceptor.ServletRequestAware;

import com.web.dao.entity.Account;
import com.web.dao.entity.Attribute;
import com.web.dao.entity.Func;
import com.web.dao.model.AttributeType;
import com.web.service.AudioManageService;
import com.web.service.SystemService;

import net.sf.json.JSONObject;

public class ChartsAction extends BaseActionSupport implements ServletRequestAware {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 7822620710398779427L;
	private static final String SESSION_LOGIN_USER = "LOGIN_USER";
	
	private SystemService systemService = ChartsAction.<SystemService>getBean("systemService");
	private AudioManageService audioManageService = SoundAction.<AudioManageService>getBean("audioManageService");
	private String success;
	private String message;
	private List<Func> funcs;
	
	private String shStartDate;
	private String shEndDate;
	private String jsonStr;
	
	
	private javax.servlet.http.HttpServletRequest request;
	public void setServletRequest(javax.servlet.http.HttpServletRequest request) {
		this.request = request;
	}
	
	
	
	public String charts(){
		Account user = (Account)request.getSession().getAttribute(SESSION_LOGIN_USER);
		funcs = this.systemService.queryFuncByAuths(user.getAccount());
		
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		Calendar cal = Calendar.getInstance();
		shEndDate = df.format(cal.getTime());
		cal.add(Calendar.MONTH, -1);
		shStartDate = df.format(cal.getTime());
		return SUCCESS;
	}

	
	
	/**
	 * 圖表查詢
	 * @return
	 */
	public String chartsReport(){
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
		
		List<Object[]> list = this.audioManageService.querySoundGroupBySec(sDate, eDate);
		if(list != null && list.size() > 0){
			List<Attribute> attrs = this.systemService.queryAttributesByType(AttributeType.voice.name(), "Y");
			Map<String, String> map = new LinkedHashMap<String, String>();
			for (Attribute a : attrs) {
				map.put(a.getAttrKey(), a.getAttrName());
			}
			
			Map<String, Integer> m = new HashMap<String, Integer>();
			for (Object[] obj : list) {
				if(map.containsKey(obj[0])){
					m.put(map.get(obj[0]), Integer.parseInt(obj[1].toString()));
				}
			}
			
			JSONObject json = JSONObject.fromObject(m);
			jsonStr = json.toString();
		}
		
		Account user = (Account)request.getSession().getAttribute(SESSION_LOGIN_USER);
		StringBuffer term = new StringBuffer();
		if(StringUtils.isNotEmpty(shStartDate) || StringUtils.isNotEmpty(shEndDate)){
			term.append("日期:").append(shStartDate).append("~").append(shEndDate);
		}
		this.systemService.updateSysRecord(user, "統計圖表【查詢】", term.toString());
		return SUCCESS;			
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
	public String getJsonStr() {
		return jsonStr;
	}
	public void setJsonStr(String jsonStr) {
		this.jsonStr = jsonStr;
	}
}