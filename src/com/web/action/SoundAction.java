package com.web.action;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
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
	
	private Integer page; // 頁數
	private Integer pageSize; // 一頁筆數
	private PageBean pageBean;
	
	private Sound sound;
	private File[] upload;   
    private String[] uploadFileName;   
    private String[] uploadContentType; 
    
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
		
		if(page == null || page == 0){
			page = 1;
		}
		pageSize = Integer.parseInt(loadConfig("page.size"));
		pageBean = this.audioManageService.querySoundByPage(shYear, shMon, shArea, shCust, shTitle, shSection, shSecond, shTone, 
																	shRole, shSkill, pageSize, page);
		for(int idx=0; idx<pageBean.getList().size(); idx++){
			Sound s = (Sound)pageBean.getList().get(idx);
			s.setSectionName(this.voiceCombo().get(s.getSection()));
			s.setToneName(this.toneCombo().get(s.getTone()));
			s.setSkillName(this.skillCombo().get(s.getSkill()));
			
			String serverPath = this.loadConfig("server.path");
			String root = this.loadConfig("upload.path");
			String uri = serverPath + (s.getFilePath().replace(root, "")+s.getFileName()).replace("\\", "/");
			s.setFileUri(uri);
		}
		return SUCCESS;
	}
	
	
	/**
	 * 取得音檔資料
	 * @return
	 */
	public String querySound(){
		sound = this.audioManageService.querySoundById(id);
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
		
		if(sound.getId() == 0){
			Account user = (Account)request.getSession().getAttribute(SESSION_LOGIN_USER);
			String path = loadConfig("upload.path") + loadConfig("upload.sound.path");
			Attribute attr = this.systemService.queryAttributesByKey(AttributeType.voice.name(), sound.getSection(), null);
			try{
				if(attr != null){
					path += attr.getAttrName() + "\\" + sound.getYear()+attr.getAttrName() + "\\";
					File des = new File(path);
					if(!des.exists()){
						des.mkdirs();
					}
					
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
						sound.setFilePath(path);
						sound.setFileName(uploadFileName[idx]);
						sound.setBatch("N");
						sound.setCreator(user.getAccount());
						sound.setCreateDate(new Date());
						this.audioManageService.updateSound(sound);
					}
				}
			}catch (Exception e) {
				throw new RuntimeException(e.getMessage(), e);
			}
		}else{
			Sound s = this.audioManageService.querySoundById(sound.getId());
			s.setYear(sound.getYear());
			s.setMonth(sound.getMonth());
			s.setCustName(sound.getCustName());
			s.setArea(sound.getArea());
			s.setSecond(sound.getSecond());
			s.setSection(sound.getSection());
			s.setRole(sound.getRole());
			s.setSkill(sound.getSkill());
			s.setTone(sound.getTone());
			this.audioManageService.updateSound(s);
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
		
		Sound s = this.audioManageService.querySoundById(id);
		this.audioManageService.deleteSound(s);
		
		File df = new File(s.getFilePath()+s.getFileName());
		df.delete();
		return SUCCESS;
	}
	
	
	/**
	 * 批次轉檔
	 * @return
	 */
	public String batchUpload() {
		try {
			Date start = new Date();
			File source = new File(batchPath);
			if (!source.exists()) {
				success = "N";
				message = "系統無您所指定的路徑";
				return SUCCESS;
			}
			
			String target = loadConfig("upload.path") + loadConfig("upload.sound.path");
			slist = new ArrayList<String>();
			flist = new ArrayList<String>();
			copyFolder(batchPath, target);
			
			Date end = new Date();
			long time = end.getTime() - start.getTime();
			totTime = millisToHMS(time);
		}catch(Exception e) {
			e.printStackTrace();
		}
		return SUCCESS;
	}

	
	private void copyFolder(String oldPath, String newPath) {
		try{
			File a = new File(oldPath);
			String[] file = a.list();
			if(file != null){
				File temp = null;
				int cnt = 0;
				for (int i = 0; i < file.length; i++) {
					if(oldPath.endsWith(File.separator)){
						temp = new File(oldPath+file[i]);
					}else{
						temp = new File(oldPath + File.separator + file[i]);
					}
					 
					if(temp.isFile()){
						String fName = temp.getName().toString();
						boolean isOk = this.checkFileFormat(newPath, fName, cnt);
						if(isOk){
							slist.add(fName);
							//建立目錄
							if(cnt == 0){
								Map<String, String> map = this.voiceCombo();
								String[] fn = fName.split("\\.");
								newPath += map.get(fn[1]) + "\\" + fn[0]+map.get(fn[1]) + "\\";
								File folder = new File(newPath);
								if(!folder.exists()){
									folder.mkdirs();
								}
							}
							
							FileInputStream fis = new FileInputStream(temp);
							FileOutputStream fos = new FileOutputStream(newPath + "/" + fName);
							byte[] b = new byte[1024];
							int len;
							while ((len = fis.read(b)) > 0) {
								fos.write(b, 0, len);
							}
							fos.flush();
							fos.close();
							fis.close();
							
							//刪除原路徑檔案
							temp.delete();
							cnt++;
						}
					}
					//子資料夾
					if(temp.isDirectory()){
						copyFolder(oldPath+"\\"+file[i], newPath);
					}
				}
				
				success = "Y";
				message = "轉檔完成";
			}else{
				success = "N";
				message = "路徑下無檔案";
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * a.年份.產業類別.客戶.篇名.秒數.調性.角色.手法
	 * b.年份.產業類別.客戶.秒數.調性.角色.手法
	 * c.年份.產業類別.客戶.篇名.秒數.調性.手法
	 * d.年份.產業類別.客戶.秒數.調性.手法
	 * 
	 * [篇名]及[角色]不一定有
	 * 
	 * @param fileName
	 * @return
	 */
	private boolean checkFileFormat(String path, String fileName, int cnt){
		boolean isOk = false;
		String[] str = fileName.split("\\.");
		if(str != null){
			if(str.length-1 < 6 || str.length-1 > 8){
				flist.add(fileName);
			}else{
				//檔案格式需為mp3
				if(!"mp3".equals(str[str.length-1])){
					flist.add(fileName);
				}else{
					String title = "", tone = "", role = "", skill = "";
					int second = 0;
					try {
						if(str[3].indexOf("秒") != -1){
							second = Integer.parseInt(str[3].substring(0, str[3].indexOf("秒")));
						}else{
							title = str[3];
							if(str[4].indexOf("秒") != -1){
								second = Integer.parseInt(str[4].substring(0, str[4].indexOf("秒")));
							}else{
								tone = str[4];
							}
						}
						if(str.length-1 == 6){
							skill = str[5];
						}
						if(str.length-1 == 7){
							if(this.toneCombo().containsKey(str[5])){
								tone = str[5];
							}else{
								role = str[5];
							}
							skill = str[6];
						}
						if(str.length-1 == 8){
							tone = str[5];
							role = str[6];
							skill = str[7];
						}
						
						Map<String, String> map = this.voiceCombo();
						if(cnt == 0){
							path += map.get(str[1]) + "\\" + str[0]+map.get(str[1]) + "\\";
						}
						Account user = (Account)request.getSession().getAttribute(SESSION_LOGIN_USER);
						Sound s = new Sound();
						s.setYear(str[0]);
						s.setSection(str[1]);
						s.setCustName(str[2]);
						s.setTitle(title);
						s.setSecond(second);
						s.setRole(role);
						s.setSkill(skill);
						s.setTone(tone);
						s.setFilePath(path);
						s.setFileName(fileName);
						s.setBatch("Y");
						s.setCreator(user.getAccount());
						s.setCreateDate(new Date());
						this.audioManageService.updateSound(s);
						
						isOk = true;
					} catch (Exception e) {
						flist.add(fileName);
					}
				}
			}
		}else{
			flist.add(fileName);
		}
		return isOk;
	}
	
	
	private String millisToHMS(long millis) {
		long second = (millis / 1000) % 60;
		long minute = (millis / (1000 * 60)) % 60;
		long hour = (millis / (1000 * 60 * 60)) % 24;
		
		String time = String.format("%02d時%02d分%02d秒", hour, minute, second);
		return time;
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