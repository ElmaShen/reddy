package com.web.action;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

import org.apache.commons.lang.StringUtils;
import org.apache.struts2.interceptor.ServletRequestAware;

import com.web.dao.entity.Account;
import com.web.dao.entity.Audio;
import com.web.dao.entity.Func;
import com.web.dao.model.PageBean;
import com.web.service.AudioManageService;
import com.web.service.SystemService;

public class PhotoAction extends BaseActionSupport implements ServletRequestAware {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 7822620710398779427L;
	private static final String SESSION_LOGIN_USER = "LOGIN_USER";
	
	private SystemService systemService = PhotoAction.<SystemService>getBean("systemService");
	private AudioManageService audioManageService = SoundAction.<AudioManageService>getBean("audioManageService");
	private String success;
	private String message;
	private List<Func> funcs;
	
	private long id;
	private int audioCnt;
	private int gno;
	private String shKeywords;
	private String shGname;
	private Integer page; // 頁數
	private Integer pageSize; // 一頁筆數
	private PageBean pageBean;
	
	private Audio audio;
	private List<Audio> alist;
	private File[] upload;   
    private String[] uploadFileName;   
    private String[] uploadContentType; 
    private final String SLASH = "\\";
//    private final String SLASH = "/";
    
    private InputStream fileInputStream;
    private String filename;
	
	
	private javax.servlet.http.HttpServletRequest request;
	public void setServletRequest(javax.servlet.http.HttpServletRequest request) {
		this.request = request;
	}
	
	
	
	public String photo(){
		Account user = (Account)request.getSession().getAttribute(SESSION_LOGIN_USER);
		funcs = this.systemService.queryFuncByAuths(user.getAccount());
		audioCnt = this.audioManageService.queryAudioCnt();
		return SUCCESS;
	}
	
	
	/**
	 * 取得Audio資料
	 * @return
	 */
	public String queryPhoto(){
		audio = this.audioManageService.queryAudioById(id);
		return SUCCESS;
	}
	
	/**
	 * 列表頁
	 * @return
	 */
	public String photoList() {
		Account user = (Account)request.getSession().getAttribute(SESSION_LOGIN_USER);
		funcs = this.systemService.queryFuncByAuths(user.getAccount());
		audioCnt = this.audioManageService.queryAudioCnt();
		
		if(page == null || page == 0){
			page = 1;
		}
		pageSize = Integer.parseInt(loadConfig("page.size"));
		pageBean = this.audioManageService.queryAudioByPage(shKeywords, shGname, pageSize, page);
		for(int idx=0; idx<pageBean.getList().size(); idx++){
			Audio a = (Audio)pageBean.getList().get(idx);
			
			String serverPath = this.loadConfig("server.path");
			String root = this.loadConfig("upload.path");
			String uri = serverPath + (a.getFilePath().replace(root, "")+a.getFileName()).replace(this.SLASH, "/");
			a.setFileUri(uri);
		}
		return SUCCESS;
	}
	
	
	/**
	 * 照片/影片上傳
	 * @return
	 */
	public String editPhoto() {
		if(audio == null){
			message = "傳入參數有誤,請重新設定";
			return SUCCESS;
		}
		
		String path = loadConfig("upload.path") + loadConfig("upload.audio.path");
		File des = new File(path);
		if(!des.exists()){
			des.mkdirs();
		}
		
		Account user = (Account)request.getSession().getAttribute(SESSION_LOGIN_USER);
		StringBuffer buf = new StringBuffer();
		String type = "";
		int gno = 0;
		if(audio.getId() == 0){
			type = "新增";
			
			if(StringUtils.isNotEmpty(audio.getGname())){
				//path += audio.getGname() + "\\";
				gno = (int)Math.round(Math.random()*100000);
			}
			
			this.uploadFile(user, path, gno, buf);
		}else{
			type = "編輯";
			
			Audio au = this.audioManageService.queryAudioById(audio.getId());
			if(au.getGno() == 0 && StringUtils.isNotEmpty(audio.getGname())){
				gno = (int)Math.round(Math.random()*100000);
			}else{
				gno = audio.getGno();
			}
			au.setGno(gno);
			au.setKeywords(audio.getKeywords());
			au.setGname(audio.getGname());
			this.audioManageService.updateAudio(au);
			//改變同一群組資料
			List<Audio> list = this.audioManageService.queryAudioByGno(gno);
			if(list != null && list.size() > 0){
				for (Audio a : list) {
					a.setKeywords(audio.getKeywords());
					a.setGname(audio.getGname());
					this.audioManageService.updateAudio(a);
				}
			}
			
			this.uploadFile(user, path, gno, buf);
		}
		
		this.systemService.updateSysRecord(user, "照片及影片管理【"+type+"】：", path + "<br>" + buf.toString());
		return SUCCESS;
	}
	
	
	private void uploadFile(Account user, String path, int gno, StringBuffer buf){
		try {
			if(upload != null){
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
					
					Audio a = new Audio();
					a.setKeywords(audio.getKeywords());
					a.setGno(gno);
					a.setGname(audio.getGname());
					a.setFilePath(path);
					a.setFileName(uploadFileName[idx]);
					a.setFtype(audio.getFtype());
					a.setCreator(user.getAccount());
					a.setCreateDate(new Date());
					this.audioManageService.updateAudio(a);
					
					buf.append(uploadFileName[idx]).append("<br>");
				}
			}
		} catch (IOException e) {
			throw new RuntimeException(e.getMessage(), e);
		}
	}
	
	/**
	 * 下載
	 * @return
	 */
	public String downloadPhoto() {
		Account user = (Account)request.getSession().getAttribute(SESSION_LOGIN_USER);
		try {
			Audio a = this.audioManageService.queryAudioById(id);
			if(a != null){
				String userAgent = request.getHeader("User-Agent");
				if((userAgent.contains("MSIE"))||(userAgent.contains("Trident"))){
					filename = java.net.URLEncoder.encode(a.getFileName(),"UTF-8");
				}else{
					filename = new String(a.getFileName().getBytes("UTF-8"),"ISO-8859-1");
				}
				
				String filePath = a.getFilePath() + a.getFileName();
				fileInputStream = new FileInputStream(new File(filePath));
				
				this.systemService.updateSysRecord(user, "照片及影片管理【下載】：", filePath);
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
	public String deletePhoto(){
		Account user = (Account)request.getSession().getAttribute(SESSION_LOGIN_USER);
		Audio a = this.audioManageService.queryAudioById(id);
		this.audioManageService.deleteAudio(a);
		
		String filePath = a.getFilePath()+a.getFileName();
		File f = new File(filePath);
		f.delete();
		
		this.systemService.updateSysRecord(user, "照片及影片管理【刪除】：", filePath);
		return SUCCESS;
	}
	
	
	/**
	 * 查詢群組相片
	 * @return
	 */
	public String queryByGno(){
		alist = this.audioManageService.queryAudioByGno(gno);
		for (Audio a : alist) {
			String serverPath = this.loadConfig("server.path");
			String root = this.loadConfig("upload.path");
			String uri = serverPath + (a.getFilePath().replace(root, "")+a.getFileName()).replace(this.SLASH, "/");
			a.setFileUri(uri);
		}
		return SUCCESS;
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
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public int getAudioCnt() {
		return audioCnt;
	}
	public void setAudioCnt(int audioCnt) {
		this.audioCnt = audioCnt;
	}
	public int getGno() {
		return gno;
	}
	public void setGno(int gno) {
		this.gno = gno;
	}
	public String getShKeywords() {
		return shKeywords;
	}
	public void setShKeywords(String shKeywords) {
		this.shKeywords = shKeywords;
	}
	public String getShGname() {
		return shGname;
	}
	public void setShGname(String shGname) {
		this.shGname = shGname;
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
	public Audio getAudio() {
		return audio;
	}
	public void setAudio(Audio audio) {
		this.audio = audio;
	}
	public List<Audio> getAlist() {
		return alist;
	}
	public void setAlist(List<Audio> alist) {
		this.alist = alist;
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
}