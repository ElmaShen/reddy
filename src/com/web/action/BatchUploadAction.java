package com.web.action;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import org.apache.commons.lang.StringUtils;
import org.apache.struts2.interceptor.ServletRequestAware;

import com.util.DateUtils;
import com.web.dao.entity.Account;
import com.web.dao.entity.Attribute;
import com.web.dao.entity.Customer;
import com.web.dao.entity.CustomerAttach;
import com.web.dao.entity.Func;
import com.web.dao.entity.Sound;
import com.web.dao.model.AttributeType;
import com.web.service.AudioManageService;
import com.web.service.CustomerService;
import com.web.service.SystemService;

import net.sf.json.JSONObject;

public class BatchUploadAction extends BaseActionSupport implements ServletRequestAware {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 7822620710398779427L;
	private static final String SESSION_LOGIN_USER = "LOGIN_USER";
	
	private SystemService systemService = BatchUploadAction.<SystemService>getBean("systemService");
	private AudioManageService audioManageService = BatchUploadAction.<AudioManageService>getBean("audioManageService");
	private CustomerService customerService = CustomerAction.<CustomerService>getBean("customerService");
	private String success;
	private String message;
	private String fno;
	private List<Func> funcs;
	
    private String batchType;
    private String batchPath;
    private List<String> slist;	//成功
    private List<String> flist;	//失敗
    private String totTime;
    private Customer cust;
    private String voices;
    private final String SLASH = "\\";
//    private final String SLASH = "/";
	
	
	private javax.servlet.http.HttpServletRequest request;
	public void setServletRequest(javax.servlet.http.HttpServletRequest request) {
		this.request = request;
	}
	
	
	
	public String batchUpload() {
		Account user = (Account)request.getSession().getAttribute(SESSION_LOGIN_USER);
		funcs = this.systemService.queryFuncByAuths(user.getAccount());

		return SUCCESS;
	} 
	
	
	
	/**
	 * 批次轉檔
	 * @return
	 */
	public String startBatch() {
		try {
			Date start = new Date();
			File source = new File(batchPath);
			if (!source.exists()) {
				success = "N";
				message = "系統無您所指定的路徑";
				return SUCCESS;
			}
			
			String target = loadConfig("upload.path");
			slist = new ArrayList<String>();
			flist = new ArrayList<String>();
			String type = "";
			if("M".equals(batchType)){
				type = "音檔";
				target += loadConfig("upload.sound.path") ;
				copySoundFolder(batchPath, target);
			}else{
				type = "個案";
				target += loadConfig("upload.cust.path");
				String path = batchPath.substring(batchPath.lastIndexOf(this.SLASH)+1);
				//產業類別-客戶層
				if(path.indexOf("-") != -1){
					target += this.SLASH + path;
				}
				copyCustFolder(batchPath, target, "");
			}
			
			Date end = new Date();
			long time = end.getTime() - start.getTime();
			totTime = DateUtils.millisToHMS(time);
			
			Account user = (Account)request.getSession().getAttribute(SESSION_LOGIN_USER);
			this.systemService.updateSysRecord(user, "批次轉檔【"+type+"】：", "歷時="+totTime+", 成功="+slist.size()+", 失敗="+flist.size());
		}catch(Exception e) {
			e.printStackTrace();
		}
		return SUCCESS;
	}
	
	private void copySoundFolder(String oldPath, String newPath) {
		try{
			File a = new File(oldPath);
			String[] file = a.list();
			if(file != null){
				File temp = null;
				int cnt = 0;
				Map<String, String> map = this.voiceMap();
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
								String[] fn = fName.split("\\.");
								newPath += map.get(fn[1]) + this.SLASH + fn[0]+map.get(fn[1]) + this.SLASH;
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
						copySoundFolder(oldPath+this.SLASH+file[i], newPath);
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
		if(str != null && str.length > 0){
			//檔案格式需為mp3
			if(!"mp3".equals(str[str.length-1])){
				flist.add(fileName + "@#@檔案需為mp3");
			}else{
				Map<String, String> map = this.voiceMap();
				int n = 0;
				String title = "", tone = "", role = "", skill = "";
				int second = 0;
				try {
					try {
						//年份
						n = Integer.parseInt(str[0]);
					} catch (Exception e) {
						flist.add(fileName + "@#@年份需為西元年,數字格式");
					}
					
					if(n != 0){
						//產業類別
						if(map.containsKey(str[1])){	
							//篇名、秒數
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
							} catch (Exception e) {
								flist.add(fileName + "@#@秒數不正確");
							}
							
							if(second != 0){
								//調性.手法.角色
								for(int i=4; i<str.length-1; i++){
									if(this.toneMap().containsKey(str[i])){
										tone = str[i];
									}else if(this.skillMap().containsKey(str[i])){
										skill += str[i] + ",";
									}else{
										role = str[i];
									}
								}
								
								if(StringUtils.isEmpty(tone)){
									flist.add(fileName + "@#@無調性");
								}else if(StringUtils.isEmpty(skill)){
									flist.add(fileName + "@#@無手法");
								}else{
									if(cnt == 0){
										path += map.get(str[1]) + this.SLASH + str[0]+map.get(str[1]) + this.SLASH;
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
								}
							}
						}else{
							flist.add(fileName + "@#@產業類別無「"+str[1]+"」的代碼設定");
						}
					}
				} catch (Exception e) {
					flist.add(fileName + "@#@資料新增db有誤");
				}
			}
		}else{
			flist.add(fileName + "@#@檔名不正確");
		}
		return isOk;
	}
	
	
	private void copyCustFolder(String oldPath, String newPath, String secStr) {
		Account user = (Account)request.getSession().getAttribute(SESSION_LOGIN_USER);
		Map<String, String> map = this.sectionMapByName();
		String section = secStr;
		try{
			String cname = "", sec = "";
			String path = newPath.substring(newPath.lastIndexOf(this.SLASH)+1);
			if(path.indexOf("-") != -1){
				cname = path.split("-")[1];
				sec = path.split("-")[0];
				
				if(map.containsKey(sec)){
					section = map.get(sec);
					String custNo = this.customerService.queryMaxCustomerNo();
					int lastNo = Integer.parseInt(custNo.substring(1))+1;
					cust = new Customer();
					cust.setCustNo("C" + String.format("%07d", lastNo));
					cust.setCustName(cname);
					cust.setSection(section);
					cust.setBatch("Y");
					cust.setCreator(user.getAccount());
					cust.setCreateDate(new Date());
					this.customerService.updateCustomer(cust);
				}
			}
			
			File a = new File(oldPath);
			String[] file = a.list();
			if(file != null){
				File temp = null;
				for (int i = 0; i < file.length; i++) {
					if(oldPath.endsWith(File.separator)){
						temp = new File(oldPath+file[i]);
					}else{
						temp = new File(oldPath + File.separator + file[i]);
					}
					
					if(temp.isFile()){
						String fName = temp.getName().toString();
						//產品類別錯誤,folder內檔案均不匯入
						if(StringUtils.isEmpty(section)){
							flist.add(fName + "@#@無產品類別");
							continue;
						}
						
						if(fName.toUpperCase().equals("_DS_STORE")){
							flist.add(fName + "@#@為無效檔案");
						}else{
							if(oldPath.indexOf("素材") != -1){
								String[] fn = fName.split("\\.");
								if(!"mp3".equals(fn[fn.length-1])){
									flist.add(fName + "@#@【C.素材】檔案需為mp3");
									continue;
								}
							}
							
							slist.add(fName);
							File folder = new File(newPath);
							if(!folder.exists()){
								folder.mkdirs();
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
							
							if(cust != null){
								String type = newPath.substring(newPath.lastIndexOf(this.SLASH)+1);
								CustomerAttach ach = new CustomerAttach();
								ach.setType(type.indexOf(".") != -1 ? type.substring(0,1) : "");
								ach.setFilePath(newPath+this.SLASH);
								ach.setFileName(fName);
								ach.setCustId(cust.getId());
								this.customerService.updateCustomerAttach(ach);
							}
							
							//刪除原路徑檔案
							temp.delete();
						}
					}
					
					if(temp.isDirectory()){
						String str = "";
						if("提案".equals(temp.getName())){
							str = "A.";
						}
						if("文案".equals(temp.getName())){
							str = "B.";				
						}
						if("素材".equals(temp.getName())){
							str = "C.";
						}
						if("其他".equals(temp.getName())){
							str = "D.";
						}
						String ss = newPath+this.SLASH+str+temp.getName();
						copyCustFolder(oldPath+this.SLASH+file[i], ss, section);
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
	 * 整批刪除
	 * @return
	 */
	public String deleteAll(){
		//音檔
		if("M".equals(batchType)){
			int cnt = this.audioManageService.deleteSoundByVoice(voices);
			if(cnt > 0){
				Map<String, String> m = this.voiceMap();
				String dir = m.get(voices);
				String path = loadConfig("upload.path") + loadConfig("upload.sound.path") + dir;
				File f = new File(path);
				deleteDir(f);
				
				success = "Y";
				message = "刪除成功, 共計：" + cnt + "個檔案";
			}else{
				message = "無可刪除的檔案";
			}
		}
		
		//個案
		if("C".equals(batchType)){
			List<Customer> list = this.customerService.queryCustomerBySection(voices);
			if(list != null && list.size() > 0){
				Map<String, String> m = this.sectionMapByKey();
				String path = loadConfig("upload.path") + loadConfig("upload.cust.path") + this.SLASH;
				for (Customer c : list) {
					List<CustomerAttach> achs = this.customerService.queryCustomerAttachByCustId(c.getId());
					c.setAttachs(achs);
					this.customerService.deleteCustomer(c);
					
					path += m.get(c.getSection()) + "-" + c.getCustName();
					File f = new File(path);
					deleteDir(f);
				}
				
				success = "Y";
				message = "刪除成功, 共計：" + list.size() + "筆個案";
			}else{
				message = "無可刪除的檔案";
			}
		}
		return SUCCESS;
	}
	
	
	/**
	 * 產業類別 下拉
	 * @return
	 */
	public String changeComboValue(){
		Map<String, String> m = null;
		if("M".equals(batchType)) {	//音檔
			m = this.voiceMap();
		}else {	//個案
			List<Attribute> list = this.systemService.queryAttributesByType(AttributeType.section.name(), "Y");
			m = new LinkedHashMap<String, String>();
			for (Attribute a : list) {
				m.put(a.getAttrKey(), a.getAttrName());
			}
		}
		
		JSONObject json = JSONObject.fromObject(m);
		message = json.toString();
		success = "true";
		return SUCCESS;
	}
	
	
	/**
	 * 刪除目錄、檔案
	 * @param dir
	 * @return
	 */
	private boolean deleteDir(File dir) {
		if (dir.isDirectory()) {
			File[] files = dir.listFiles();
			for (int i=0;i<files.length;i++) { 
				if (!deleteDir(files[i]))
					return false;
			}
		}
		return dir.delete();
	}
	
	
	private String loadConfig(String param) {
		ResourceBundle rb = ResourceBundle.getBundle("config");
		return rb.getString(param);
	}
	

	private Map<String, String> skillMap() {
		List<Attribute> list = this.systemService.queryAttributesByType(AttributeType.skill.name(), "Y");
		Map<String, String> map = new LinkedHashMap<String, String>();
		for (Attribute a : list) {
			map.put(a.getAttrKey(), a.getAttrName());
		}
		return map;
	}
	
	private Map<String, String> toneMap() {
		List<Attribute> list = this.systemService.queryAttributesByType(AttributeType.tone.name(), "Y");
		Map<String, String> map = new LinkedHashMap<String, String>();
		for (Attribute a : list) {
			map.put(a.getAttrKey(), a.getAttrName());
		}
		return map;
	}
	
	
	private Map<String, String> voiceMap() {
		List<Attribute> list = this.systemService.queryAttributesByType(AttributeType.voice.name(), "Y");
		Map<String, String> map = new LinkedHashMap<String, String>();
		for (Attribute a : list) {
			map.put(a.getAttrKey(), a.getAttrName());
		}
		return map;
	}

	private Map<String, String> sectionMapByName() {
		List<Attribute> list = this.systemService.queryAttributesByType(AttributeType.section.name(), "Y");
		Map<String, String> map = new LinkedHashMap<String, String>();
		for (Attribute a : list) {
			map.put(a.getAttrName(), a.getAttrKey());
		}
		return map;
	}
	
	private Map<String, String> sectionMapByKey() {
		List<Attribute> list = this.systemService.queryAttributesByType(AttributeType.section.name(), "Y");
		Map<String, String> map = new LinkedHashMap<String, String>();
		for (Attribute a : list) {
			map.put(a.getAttrKey(), a.getAttrName());
		}
		return map;
	}
	
	

	/**
	 * 產業類別
	 * @return
	 */
	public Map<String, String> voiceCombo() {
//		Account user = (Account)request.getSession().getAttribute(SESSION_LOGIN_USER);
//		Map<String, Authority> auth = user.getAuthority();
//		
//		Map<String, String> map = new LinkedHashMap<String, String>();
//		if(auth.containsKey("F01")){
//			List<Attribute> list = this.systemService.queryAttributesByType(AttributeType.voice.name(), "Y");
//			Map<String, Attribute> m = new LinkedHashMap<String, Attribute>();
//			for (Attribute a : list) {
//				m.put(a.getAttrKey(), a);
//			}
//			
//			Authority au = auth.get("F01");
//			if(StringUtils.isNotEmpty(au.getVoice())){
//				String[] str = au.getVoice().split(",");
//				for(int i=0; i<str.length; i++){
//					if(StringUtils.isNotEmpty(str[i]) && m.containsKey(str[i])){
//						Attribute attr = m.get(str[i]);
//						map.put(attr.getAttrKey(), attr.getAttrName());
//					}
//				}
//			}
//		}
		return this.voiceMap();
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
	public String getBatchType() {
		return batchType;
	}
	public void setBatchType(String batchType) {
		this.batchType = batchType;
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
	public Customer getCust() {
		return cust;
	}
	public void setCust(Customer cust) {
		this.cust = cust;
	}
	public String getVoices() {
		return voices;
	}
	public void setVoices(String voices) {
		this.voices = voices;
	}
}