package com.web.action;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import org.apache.commons.lang.StringUtils;
import org.apache.struts2.interceptor.ServletRequestAware;

import com.web.dao.entity.Account;
import com.web.dao.entity.Attribute;
import com.web.dao.entity.Authority;
import com.web.dao.entity.Func;
import com.web.dao.entity.Sound;
import com.web.dao.model.AttributeType;
import com.web.dao.model.PageBean;
import com.web.service.AudioManageService;
import com.web.service.SystemService;

public class SoundAction extends BaseActionSupport implements ServletRequestAware {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 7822620710398779427L;
	private static final String SESSION_LOGIN_USER = "LOGIN_USER";
	
	private SystemService systemService = SoundAction.<SystemService>getBean("systemService");
	private AudioManageService audioManageService = SoundAction.<AudioManageService>getBean("audioManageService");
	private String success;
	private String message;
	private String fno;
	private List<Func> funcs;
	
	private long id;
	private int soundCnt;
	private String shYear;
	private String shMon;
	private String shArea;
	private String shCust;
	private String shTitle;
	private String shSection;
	private Integer shSecond;
	private String shTone;
	private String shRole;
	private String shSkill;
	private String shAreaName;
	private String shSectionName;
	private String shToneName;
	private String shRoleName;
	private String shSkillName;
	
	private Integer page; // 頁數
	private Integer pageSize; // 一頁筆數
	private PageBean pageBean;
	
	private Sound sound;
	private String[] skills;
	private File[] upload;   
    private String[] uploadFileName;   
    private String[] uploadContentType; 
    private final String SLASH = "\\";
//    private final String SLASH = "/";
    
    private InputStream fileInputStream;
    private String filename;
    private String batchPath;
    private List<String> slist;	//成功
    private List<String> flist;	//失敗
    private String totTime;
	
	
	private javax.servlet.http.HttpServletRequest request;
	public void setServletRequest(javax.servlet.http.HttpServletRequest request) {
		this.request = request;
	}
	
	
	
	public String sound() {
		Account user = (Account)request.getSession().getAttribute(SESSION_LOGIN_USER);
		funcs = this.systemService.queryFuncByAuths(user.getAccount());
		//音檔 產業類別
		String[] ary = null;
		if(user.getAuthority() != null && user.getAuthority().containsKey("F01")) {
			Authority au = user.getAuthority().get("F01");
			if(au.getVoice() != null) {
				ary = au.getVoice().split(",");
			}
		}
		soundCnt = 0;
		if(ary != null && ary.length > 0){
			soundCnt = audioManageService.querySoundCount(ary);;
		}
		
		Calendar cal = Calendar.getInstance();
		shYear = String.valueOf(cal.get(Calendar.YEAR));
		return SUCCESS;
	} 
	
	/**
	 * 列表頁
	 * @return
	 */
	public String soundList() {
		Account user = (Account)request.getSession().getAttribute(SESSION_LOGIN_USER);
		funcs = this.systemService.queryFuncByAuths(user.getAccount());
		String[] ary = null;
		if(user.getAuthority() != null && user.getAuthority().containsKey("F01")) {
			Authority au = user.getAuthority().get("F01");
			if(au.getVoice() != null) {
				ary = au.getVoice().split(",");
			}
		}
		soundCnt = 0;
		if(ary != null && ary.length > 0){
			soundCnt = audioManageService.querySoundCount(ary);;
		}

		
		if(page == null || page == 0){
			page = 1;
		}
		pageSize = Integer.parseInt(loadConfig("page.size"));
		pageBean = this.audioManageService.querySoundByPage(shYear, shMon, shArea, shCust, shTitle, shSection, ary, shSecond, shTone, 
																	shRole, shSkill, pageSize, page);
		for(int idx=0; idx<pageBean.getList().size(); idx++){
			Sound s = (Sound)pageBean.getList().get(idx);
			s.setSectionName(this.voiceCombo().get(s.getSection()));
			s.setToneName(this.toneCombo().get(s.getTone()));
			s.setSkillName(this.skillCombo().get(s.getSkill()));
			
			String serverPath = this.loadConfig("server.path");
			String root = this.loadConfig("upload.path");
			String uri = serverPath + (s.getFilePath().replace(root, "")+s.getFileName()).replace(this.SLASH, "/");
			s.setFileUri(uri);
		}
		
		StringBuffer term = new StringBuffer();
		if(StringUtils.isNotEmpty(shYear)){
			term.append("年份:").append(shYear).append(shMon).append(", ");
		}
		if(StringUtils.isNotEmpty(shAreaName)){
			term.append("地區:").append(shAreaName).append(", ");
		}
		if(StringUtils.isNotEmpty(shCust)){
			term.append("客戶:").append(shCust).append(", ");
		}
		if(StringUtils.isNotEmpty(shTitle)){
			term.append("篇名:").append(shTitle).append(", ");
		}
		if(StringUtils.isNotEmpty(shSectionName)){
			term.append("產業類別:").append(shSectionName).append(", ");
		}
		if(shSecond != null && shSecond != 0){
			term.append("秒數:").append(shSecond).append(", ");
		}
		if(StringUtils.isNotEmpty(shToneName)){
			term.append("調性:").append(shToneName).append(", ");
		}
		if(StringUtils.isNotEmpty(shRoleName)){
			term.append("角色:").append(shRoleName).append(", ");
		}
		if(StringUtils.isNotEmpty(shSkillName)){
			term.append("手法:").append(shSkillName);
		}
		this.systemService.updateSysRecord(user, "音檔管理【查詢】", term.toString());
		return SUCCESS;
	}
	
	
	/**
	 * 取得音檔資料
	 * @return
	 */
	public String querySound(){
		sound = this.audioManageService.querySoundById(id);
		sound.setSectionName(this.voiceCombo().get(sound.getSection()));
		sound.setToneName(this.toneCombo().get(sound.getTone()));
		String[] ary = sound.getSkill().split(",");
		if(ary != null && ary.length > 0){
			String n = "";
			for (String so : ary) {
				if(StringUtils.isNotEmpty(so)){
					n += this.skillCombo().get(so) + "  ";
				}
			}
			sound.setSkillName(n);
		}
		return SUCCESS;
	}
	
	
	/**
	 * 編輯音檔
	 * @return
	 */
	public String editSounds() {
		if(sound == null){
			message = "傳入參數有誤,請重新設定";
			return SUCCESS;
		}
		
		Account user = (Account)request.getSession().getAttribute(SESSION_LOGIN_USER);
		Sound s = null;
		if(sound.getId() == 0){
			String path = loadConfig("upload.path") + loadConfig("upload.sound.path");
			Attribute attr = this.systemService.queryAttributesByKey(AttributeType.voice.name(), sound.getSection(), null);
			try{
				if(attr != null){
					path += attr.getAttrName() + this.SLASH + sound.getYear()+attr.getAttrName() + this.SLASH;
					File des = new File(path);
					if(!des.exists()){
						des.mkdirs();
					}
					
					StringBuffer buf = new StringBuffer();
					for(int idx=0; idx<upload.length; idx++){
						String savePath = path + uploadFileName[idx];
						
						FileOutputStream fos = new FileOutputStream(savePath);
						FileInputStream fis = new FileInputStream(upload[idx]);
						byte[] buffer = new byte[1024];
						int len = 0;
						while((len = fis.read(buffer)) > 0) {
							fos.write(buffer, 0, len);
						}
						fos.flush();
						fos.close();
						fis.close();
						
						//insert sound table
						s = new Sound();
						s.setFilePath(path);
						s.setFileName(uploadFileName[idx]);
						s.setBatch("N");
						s.setCreator(user.getAccount());
						s.setCreateDate(new Date());
						s.setYear(sound.getYear());
						s.setMonth(sound.getMonth());
						s.setCustName(sound.getCustName());
						s.setTitle(sound.getTitle());
						s.setArea(sound.getArea());
						s.setSecond(sound.getSecond());
						s.setSection(sound.getSection());
						s.setRole(sound.getRole());
						s.setTone(sound.getTone());
						s.setSkill("");
						if(skills != null && skills.length > 0){
							String tmp = "";
							for (String sk : skills) {
								if(StringUtils.isNotEmpty(sk)){
									tmp += sk + ",";
								}
							}
							s.setSkill(tmp);
						}
						this.audioManageService.updateSound(s);
						
						buf.append(uploadFileName[idx]).append(",");
					}
					this.systemService.updateSysRecord(user, "音檔管理【新增】", path + buf.toString());
				}
			}catch (Exception e) {
				throw new RuntimeException(e.getMessage(), e);
			}
		}else{
			s = this.audioManageService.querySoundById(sound.getId());
			s.setYear(sound.getYear());
			s.setMonth(sound.getMonth());
			s.setCustName(sound.getCustName());
			s.setTitle(sound.getTitle());
			s.setArea(sound.getArea());
			s.setSecond(sound.getSecond());
			s.setSection(sound.getSection());
			s.setRole(sound.getRole());
			s.setTone(sound.getTone());
			s.setSkill("");
			if(skills != null && skills.length > 0){
				String tmp = "";
				for (String sk : skills) {
					if(StringUtils.isNotEmpty(sk)){
						tmp += sk + ",";
					}
				}
				s.setSkill(tmp);
			}
			this.audioManageService.updateSound(s);
			this.systemService.updateSysRecord(user, "音檔管理【編輯】", s.getFilePath() + s.getFileName());
		}
		
		success = "Y";
		message = "編輯成功";
		return SUCCESS;
	}
	
	
	/**
	 * 下載音檔
	 * @return
	 */
	public String downloadSound() {
		try {
			Account user = (Account)request.getSession().getAttribute(SESSION_LOGIN_USER);
			
			Sound s = this.audioManageService.querySoundById(id);
			if(s != null){
				String userAgent = request.getHeader("User-Agent");
				if((userAgent.contains("MSIE"))||(userAgent.contains("Trident"))){
					filename = java.net.URLEncoder.encode(s.getFileName(),"UTF-8");	//IE6.11正常、FF的中文部分會出現%XX%XX的代碼
				}else{
					filename = new String(s.getFileName().getBytes("UTF-8"),"ISO-8859-1");	//FF/Chrome正常，IE6檔名整個亂碼 (連副檔名都看不見)
				}
				
				String filePath = s.getFilePath() + s.getFileName();
				fileInputStream = new FileInputStream(new File(filePath));
				
				this.systemService.updateSysRecord(user, "音檔管理【下載】", filePath);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return SUCCESS;
	}
	
	
	/**
	 * 刪除
	 * @return
	 */
	public String deleteSound(){
		Account user = (Account)request.getSession().getAttribute(SESSION_LOGIN_USER);
		funcs = this.systemService.queryFuncByAuths(user.getAccount());
		String[] ary = null;
		if(user.getAuthority() != null && user.getAuthority().containsKey("F01")) {
			Authority au = user.getAuthority().get("F01");
			if(au.getVoice() != null) {
				ary = au.getVoice().split(",");
			}
		}
		soundCnt = 0;
		if(ary != null && ary.length > 0){
			soundCnt = audioManageService.querySoundCount(ary);;
		}
		
		Sound s = this.audioManageService.querySoundById(id);
		this.audioManageService.deleteSound(s);
		
		String filePath = s.getFilePath()+s.getFileName();
		File df = new File(filePath);
		df.delete();
		
		this.systemService.updateSysRecord(user, "音檔管理【刪除】", filePath);
		success = "Y";
		return SUCCESS;
	}
	
	
	
	private String loadConfig(String param) {
		ResourceBundle rb = ResourceBundle.getBundle("config");
		return rb.getString(param);
	}
	

	
	/**
	 * 月份
	 * @return
	 */
	public Map<String, String> monCombo() {
		Map<String, String> map = new LinkedHashMap<String, String>();
		for (int m=1; m<=12; m++) {
			String val = String.format("%02d", m);
			map.put(val, val);
		}
		return map;
	}
	
	/**
	 * 秒數
	 * @return
	 */
	public Map<Integer, Integer> secondCombo() {
		Map<Integer, Integer> map = new LinkedHashMap<Integer, Integer>();
		for (int s=5; s<=30; s+=5) {
			map.put(s, s);
		}
		return map;
	}
	
	/**
	 * 地區
	 * @return
	 */
	public Map<String, String> areaCombo() {
		List<Attribute> list = this.systemService.queryAttributesByType(AttributeType.area.name(), "Y");
		Map<String, String> map = new LinkedHashMap<String, String>();
		for (Attribute a : list) {
			map.put(a.getAttrKey(), a.getAttrName());
		}
		return map;
	}


	/**
	 * 產業類別(for 音檔管理)
	 * @return
	 */
	public Map<String, String> voiceCombo() {
		Account user = (Account)request.getSession().getAttribute(SESSION_LOGIN_USER);
		Map<String, Authority> auth = user.getAuthority();
		
		Map<String, String> map = new LinkedHashMap<String, String>();
		if(auth.containsKey("F01")){
			List<Attribute> list = this.systemService.queryAttributesByType(AttributeType.voice.name(), "Y");
			Map<String, Attribute> m = new LinkedHashMap<String, Attribute>();
			for (Attribute a : list) {
				m.put(a.getAttrKey(), a);
			}
			
			Authority au = auth.get("F01");
			if(StringUtils.isNotEmpty(au.getVoice())){
				String[] str = au.getVoice().split(",");
				for(int i=0; i<str.length; i++){
					if(StringUtils.isNotEmpty(str[i]) && m.containsKey(str[i])){
						Attribute attr = m.get(str[i]);
						map.put(attr.getAttrKey(), attr.getAttrName());
					}
				}
			}
		}
		return map;
	}
	
	/**
	 * 角色
	 * @return
	 */
	public Map<String, String> roleCombo() {
		List<Attribute> list = this.systemService.queryAttributesByType(AttributeType.role.name(), "Y");
		Map<String, String> map = new LinkedHashMap<String, String>();
		for (Attribute a : list) {
			map.put(a.getAttrKey(), a.getAttrName());
		}
		return map;
	}
	
	
	/**
	 * 調性
	 * @return
	 */
	public Map<String, String> toneCombo() {
		List<Attribute> list = this.systemService.queryAttributesByType(AttributeType.tone.name(), "Y");
		Map<String, String> map = new LinkedHashMap<String, String>();
		for (Attribute a : list) {
			map.put(a.getAttrKey(), a.getAttrName());
		}
		return map;
	}
	
	/**
	 * 手法
	 * @return
	 */
	public Map<String, String> skillCombo() {
		List<Attribute> list = this.systemService.queryAttributesByType(AttributeType.skill.name(), "Y");
		Map<String, String> map = new LinkedHashMap<String, String>();
		for (Attribute a : list) {
			map.put(a.getAttrKey(), a.getAttrName());
		}
		return map;
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
	public String getFno() {
		return fno;
	}
	public void setFno(String fno) {
		this.fno = fno;
	}
	public List<Func> getFuncs() {
		return funcs;
	}
	public void setFuncs(List<Func> funcs) {
		this.funcs = funcs;
	}
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public int getSoundCnt() {
		return soundCnt;
	}
	public void setSoundCnt(int soundCnt) {
		this.soundCnt = soundCnt;
	}
	public String getShYear() {
		return shYear;
	}
	public void setShYear(String shYear) {
		this.shYear = shYear;
	}
	public String getShMon() {
		return shMon;
	}
	public void setShMon(String shMon) {
		this.shMon = shMon;
	}
	public String getShArea() {
		return shArea;
	}
	public void setShArea(String shArea) {
		this.shArea = shArea;
	}
	public String getShCust() {
		return shCust;
	}
	public void setShCust(String shCust) {
		this.shCust = shCust;
	}
	public String getShTitle() {
		return shTitle;
	}
	public void setShTitle(String shTitle) {
		this.shTitle = shTitle;
	}
	public String getShSection() {
		return shSection;
	}
	public void setShSection(String shSection) {
		this.shSection = shSection;
	}
	public Integer getShSecond() {
		return shSecond;
	}
	public void setShSecond(Integer shSecond) {
		this.shSecond = shSecond;
	}
	public String getShTone() {
		return shTone;
	}
	public void setShTone(String shTone) {
		this.shTone = shTone;
	}
	public String getShRole() {
		return shRole;
	}
	public void setShRole(String shRole) {
		this.shRole = shRole;
	}
	public String getShSkill() {
		return shSkill;
	}
	public void setShSkill(String shSkill) {
		this.shSkill = shSkill;
	}
	public String getShAreaName() {
		return shAreaName;
	}
	public void setShAreaName(String shAreaName) {
		this.shAreaName = shAreaName;
	}
	public String getShSectionName() {
		return shSectionName;
	}
	public void setShSectionName(String shSectionName) {
		this.shSectionName = shSectionName;
	}
	public String getShToneName() {
		return shToneName;
	}
	public void setShToneName(String shToneName) {
		this.shToneName = shToneName;
	}
	public String getShRoleName() {
		return shRoleName;
	}
	public void setShRoleName(String shRoleName) {
		this.shRoleName = shRoleName;
	}
	public String getShSkillName() {
		return shSkillName;
	}
	public void setShSkillName(String shSkillName) {
		this.shSkillName = shSkillName;
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
	public Sound getSound() {
		return sound;
	}
	public void setSound(Sound sound) {
		this.sound = sound;
	}
	public String[] getSkills() {
		return skills;
	}
	public void setSkills(String[] skills) {
		this.skills = skills;
	}
	public File[] getUpload() {
		return upload;
	}
	public void setUpload(File[] upload) {
		this.upload = upload;
	}
	public String[] getUploadFileName() {
		return uploadFileName;
	}
	public void setUploadFileName(String[] uploadFileName) {
		this.uploadFileName = uploadFileName;
	}
	public String[] getUploadContentType() {
		return uploadContentType;
	}
	public void setUploadContentType(String[] uploadContentType) {
		this.uploadContentType = uploadContentType;
	}
	public InputStream getFileInputStream() {
		return fileInputStream;
	}
	public void setFileInputStream(InputStream fileInputStream) {
		this.fileInputStream = fileInputStream;
	}
	public String getFilename() {
		return filename;
	}
	public void setFilename(String filename) {
		this.filename = filename;
	}
	public String getBatchPath() {
		return batchPath;
	}
	public void setBatchPath(String batchPath) {
		this.batchPath = batchPath;
	}
	public List<String> getSlist() {
		return slist;
	}
	public void setSlist(List<String> slist) {
		this.slist = slist;
	}
	public List<String> getFlist() {
		return flist;
	}
	public void setFlist(List<String> flist) {
		this.flist = flist;
	}
	public String getTotTime() {
		return totTime;
	}
	public void setTotTime(String totTime) {
		this.totTime = totTime;
	}
}