package com.web.action;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ConnectException;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.zip.ZipOutputStream;

import org.apache.commons.lang.StringUtils;
import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.ServletResponseAware;

import com.artofsolving.jodconverter.DocumentConverter;
import com.artofsolving.jodconverter.openoffice.connection.OpenOfficeConnection;
import com.artofsolving.jodconverter.openoffice.connection.SocketOpenOfficeConnection;
import com.artofsolving.jodconverter.openoffice.converter.OpenOfficeDocumentConverter;
import com.util.ZipUtils;
import com.web.dao.entity.Account;
import com.web.dao.entity.Attribute;
import com.web.dao.entity.Authority;
import com.web.dao.entity.Customer;
import com.web.dao.entity.CustomerAttach;
import com.web.dao.entity.Func;
import com.web.dao.model.AttributeType;
import com.web.dao.model.PageBean;
import com.web.service.CustomerService;
import com.web.service.SystemService;

import net.sf.json.JSONObject;

public class CustomerAction extends BaseActionSupport implements ServletRequestAware, ServletResponseAware {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -5368163204466913743L;
	private static final String SESSION_LOGIN_USER = "LOGIN_USER";
	
	private SystemService systemService = CustomerAction.<SystemService>getBean("systemService");
	private CustomerService customerService = CustomerAction.<CustomerService>getBean("customerService");
	private String success;
	private String message;
	private String jsonStr;
	private String parent;
	private List<Func> funcs;
	
	private long id;
	private long attrId;
	private int custCnt;
	private String flag;
	private String shSection;
	private String shSectionName;
	private String shCategory;
	private String shCategoryName;
	private String shCust;
	private String shKeyword;
	
	private Integer page; // 頁數
	private Integer pageSize; // 一頁筆數
	private PageBean pageBean;
	
	private Customer customer;
	private CustomerAttach customerAttach;
	//提案
	private File[] aupload;   
    private String[] auploadFileName;   
    private String[] auploadContentType; 
    //文案
    private File[] bupload;   
    private String[] buploadFileName;   
    private String[] buploadContentType; 
    //素材
    private File[] cupload;   
    private String[] cuploadFileName;   
    private String[] cuploadContentType; 
    //其他
    private File[] dupload;   
    private String[] duploadFileName;   
    private String[] duploadContentType; 
    
    private final String SLASH = "\\";
//    private final String SLASH = "/";
    private InputStream fileInputStream;
    private String filename;
	
	
	private javax.servlet.http.HttpServletRequest request;
	public void setServletRequest(javax.servlet.http.HttpServletRequest request) {
		this.request = request;
	}
	
	private javax.servlet.http.HttpServletResponse response;
	public void setServletResponse(javax.servlet.http.HttpServletResponse response) {
		this.response = response;
	}
	
	

	public String customer() {
		Account user = (Account)request.getSession().getAttribute(SESSION_LOGIN_USER);
		funcs = this.systemService.queryFuncByAuths(user.getAccount());
		//個案 產業類別
		String[] ary = null;
		if(user.getAuthority() != null && user.getAuthority().containsKey("F02")) {
			Authority au = user.getAuthority().get("F02");
			if(au.getSection() != null) {
				ary = au.getSection().split(",");
			}
		}
		custCnt = 0;
		if(ary != null && ary.length > 0){
			custCnt = this.customerService.queryCustomerCnt(ary);
		}
		
		return SUCCESS;
	} 
	
	/**
	 * 列表頁
	 * @return
	 */
	public String customerList() {
		Account user = (Account)request.getSession().getAttribute(SESSION_LOGIN_USER);
		funcs = this.systemService.queryFuncByAuths(user.getAccount());
		String[] ary = null;
		if(user.getAuthority() != null && user.getAuthority().containsKey("F02")) {
			Authority au = user.getAuthority().get("F02");
			if(au.getSection() != null) {
				ary = au.getSection().split(",");
			}
		}
		custCnt = 0;
		if(ary != null && ary.length > 0){
			custCnt = this.customerService.queryCustomerCnt(ary);
		}
				
		if(page == null || page == 0){
			page = 1;
		}
		pageSize = Integer.parseInt(loadConfig("page.size"));
		pageBean = this.customerService.queryCustomerByPage(shSection, ary, shCategory, shCust, shKeyword, pageSize, page);
		for(int idx=0; idx<pageBean.getList().size(); idx++){
			Customer c = (Customer)pageBean.getList().get(idx);
			c.setSectionName(this.sectionCombo().get(c.getSection()));
			if(StringUtils.isNotEmpty(c.getCategory())){
				Attribute atr = this.systemService.queryAttributesByKey(AttributeType.category.name(), c.getCategory(), c.getSection());
				c.setCategoryName(atr.getAttrName());
			}
		}
		
		StringBuffer term = new StringBuffer();
		if(StringUtils.isNotEmpty(shSectionName)){
			term.append("產業類別:").append(shSectionName).append(", ");
		}
		if(StringUtils.isNotEmpty(shCategoryName)){
			term.append("產品分類:").append(shCategoryName).append(", ");
		}
		if(StringUtils.isNotEmpty(shCust)){
			term.append("客戶名稱:").append(shCust).append(", ");
		}
		if(StringUtils.isNotEmpty(shKeyword)){
			term.append("關鍵字:").append(shKeyword);
		}
		this.systemService.updateSysRecord(user, "照片及影片管理【查詢】", term.toString());
		return SUCCESS;
	}
	
	/**
	 * 取得個案資料
	 * @return
	 */
	public String queryCustomer(){
		customer = this.customerService.queryCustomerById(id);
		if(customer != null && !StringUtils.isEmpty(flag)){
			customer.setSectionName(this.sectionCombo().get(customer.getSection()));
			if(StringUtils.isNotEmpty(customer.getCategory())){
				Attribute atr = this.systemService.queryAttributesByKey(AttributeType.category.name(), customer.getCategory(), customer.getSection());
				customer.setCategoryName(atr.getAttrName());
			}
			
			List<CustomerAttach> achs = this.customerService.queryCustomerAttachByCustId(customer.getId());
			if(achs != null){
				for (CustomerAttach a : achs) {
					if("C".equals(a.getType())){
						String serverPath = this.loadConfig("server.path");
						String root = this.loadConfig("upload.path");
						String uri = serverPath + (a.getFilePath().replace(root, "")+a.getFileName()).replace(this.SLASH, "/");
						a.setFileUri(uri);
					}
				}
				customer.setAttachs(achs);
			}
		}
		return SUCCESS;
	}
	
	/**
	 * 設定個案
	 * @return
	 */
	public String editCustomer(){
		if(customer == null){
			message = "傳入參數有誤,請重新設定";
			return SUCCESS;
		}
		
		Account user = (Account)request.getSession().getAttribute(SESSION_LOGIN_USER);
		String type = "";
		if(customer.getId() == 0){
			type = "新增";
			
			String custNo = this.customerService.queryMaxCustomerNo();
			int lastNo = Integer.parseInt(custNo.substring(1))+1;
			customer.setCustNo("C" + String.format("%07d", lastNo));
			customer.setBatch("N");
			customer.setCreator(user.getAccount());
			customer.setCreateDate(new Date());
			this.customerService.updateCustomer(customer);
		}else{
			type = "編輯";
			
			Customer cust = this.customerService.queryCustomerById(customer.getId());
			if(cust != null){
				cust.setSection(customer.getSection());
				cust.setCategory(customer.getCategory());
				cust.setKeywords(customer.getKeywords());
				cust.setRemark(customer.getRemark());
				this.customerService.updateCustomer(cust);
			}
		}
		
		String path = loadConfig("upload.path") + loadConfig("upload.cust.path");
		Map<String, String> map = this.sectionCombo();
		String secName = map.get(customer.getSection());
		path += this.SLASH + secName + "-" + customer.getCustName() + this.SLASH;
		StringBuffer buf = new StringBuffer();
		if(aupload != null){
			uploadFile(path, "A.提案", aupload, auploadFileName, auploadContentType, buf);
		}
		if(bupload != null){
			uploadFile(path, "B.文案", bupload, buploadFileName, buploadContentType, buf);
		}
		if(cupload != null){
			uploadFile(path, "C.素材", cupload, cuploadFileName, cuploadContentType, buf);
		}
		if(dupload != null){
			uploadFile(path, "D.其他", dupload, duploadFileName, duploadContentType, buf);
		}
		
		this.systemService.updateSysRecord(user, "個案管理【"+type+"】", path + buf.toString());
		success = "Y";
		message = "設定成功";
		return SUCCESS;
	}
	
	
	private void uploadFile(String path, String type, File[] upload, String[] uploadFileName, String[] uploadContentType, StringBuffer buf){
		try{
			path += type + this.SLASH;
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
				
				CustomerAttach ach = new CustomerAttach();
				ach.setType(type.substring(0,1));
				ach.setFilePath(path);
				ach.setFileName(uploadFileName[idx]);
				ach.setCustId(customer.getId());
				this.customerService.updateCustomerAttach(ach);
				
				buf.append(uploadFileName[idx]).append(",");
			}
		}catch (Exception e) {
			throw new RuntimeException(e.getMessage(), e);
		}
	}
	
	
	/**
	 * 線上閱覽
	 * @return
	 */
	public String onlineReadDoc() {
		try {
			CustomerAttach ach = this.customerService.queryCustomerAttachById(id);
			if(ach != null){
				String path = ach.getFilePath();
				String fname = ach.getFileName();
				
				String tmp = fname.split("\\.")[1];
				if(!tmp.equals("pdf") && !tmp.equals("PDF")){
					int result = office2PDF(path, fname);
					if(result != -1){
						path = this.loadConfig("online.read.path");
						fname = fname.split("\\.")[0] + ".pdf";
					}
				}
				
				response.setContentType("application/pdf;charset=UTF-8");  
				response.setHeader("Content-Disposition","inline; filename="+fname+"");
				
				File file = new File(path + fname);
				BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file));
				OutputStream responseOut = response.getOutputStream();

				int bytesRead = 0;
				byte[] buffer = new byte[1024];
				while ((bytesRead = bis.read(buffer)) != -1) {
					responseOut.write(buffer, 0, bytesRead);
				}
				bis.close();
				responseOut.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * document to pdf
	 * @param path
	 * @param sourceFile
	 * @param destFile
	 * @return
	 */
	private int office2PDF(String path, String sourceFile) {  
        try {  
            File inputFile = new File(path + sourceFile);  
            if (!inputFile.exists()) {  
                return -1;
            }  
  
            String onPath = this.loadConfig("online.read.path");
            String destFile = sourceFile.split("\\.")[0] + ".pdf";
            File outputFile = new File(onPath + destFile);  
            if (!outputFile.getParentFile().exists()) {  
                outputFile.getParentFile().mkdirs();  
            }  
  
            // OpenOffice的安裝目錄
            String OpenOffice_HOME = this.loadConfig("openoffice.path");  
            if (OpenOffice_HOME.charAt(OpenOffice_HOME.length() - 1) != '\\') {  
                OpenOffice_HOME += this.SLASH;  
            }  
            // 啟動OpenOffice的服務
            String command = OpenOffice_HOME + "soffice.exe -headless -accept=\"socket,host=127.0.0.1,port=8100;urp;\"";
//            String command = OpenOffice_HOME + "soffice -headless -accept=\"socket,host=127.0.0.1,port=8100;urp;\"";
            Process pro = Runtime.getRuntime().exec(command);  
            OpenOfficeConnection connection = new SocketOpenOfficeConnection("127.0.0.1", 8100);  
            connection.connect();  
            // convert  
            DocumentConverter converter = new OpenOfficeDocumentConverter(connection);  
            converter.convert(inputFile, outputFile);  
  
            // close the connection  
            connection.disconnect();  
            pro.destroy();  
  
            return 0;  
        } catch (FileNotFoundException e) {  
            e.printStackTrace();  
            return -1;  
        } catch (ConnectException e) {  
            e.printStackTrace();  
        } catch (IOException e) {  
            e.printStackTrace();  
        }  
  
        return 1;  
    }  
	
	
	/**
	 * 下載
	 * @return
	 */
	public String downloadCust() {
		try {
			Account user = (Account)request.getSession().getAttribute(SESSION_LOGIN_USER);
			
			CustomerAttach ach = this.customerService.queryCustomerAttachById(id);
			if(ach != null){
				String userAgent = request.getHeader("User-Agent");
				if((userAgent.contains("MSIE"))||(userAgent.contains("Trident"))){
					filename = java.net.URLEncoder.encode(ach.getFileName(),"UTF-8");
				}else{
					filename = new String(ach.getFileName().getBytes("UTF-8"),"ISO-8859-1");
				}
				
				String filePath = ach.getFilePath() + ach.getFileName();
				fileInputStream = new FileInputStream(new File(filePath));
				
				this.systemService.updateSysRecord(user, "個案管理【下載】", filePath);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return SUCCESS;
	}
	
	
	/**
	 * 整批下載
	 * @return
	 */
	public String downloadAll() {
		Account user = (Account)request.getSession().getAttribute(SESSION_LOGIN_USER);
		this.systemService.updateSysRecord(user, "個案管理【整批下載】", "");
		
		String zipName = "files.zip";
		List<CustomerAttach> list = this.customerService.queryCustomerAttachByCustId(id);
		if(list != null && list.size() > 0){
	        response.setContentType("application/octet-stream");  
	        response.setHeader("Content-Disposition","attachment; filename="+zipName);
	        try {
	        	ZipOutputStream out = new ZipOutputStream(response.getOutputStream());
	        	for (CustomerAttach ach : list) {
	                ZipUtils.doCompress(ach.getFilePath()+ach.getFileName(), out);
	                response.flushBuffer();
	            }
	        	out.close();
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
		}
		return null;
	}
	
	/**
	 * 刪除客戶
	 * @return
	 */
	public String deleteCust(){
		Account user = (Account)request.getSession().getAttribute(SESSION_LOGIN_USER);
		funcs = this.systemService.queryFuncByAuths(user.getAccount());
		String[] ary = null;
		if(user.getAuthority() != null && user.getAuthority().containsKey("F02")) {
			Authority au = user.getAuthority().get("F02");
			if(au.getSection() != null) {
				ary = au.getSection().split(",");
			}
		}
		custCnt = 0;
		if(ary != null && ary.length > 0){
			custCnt = this.customerService.queryCustomerCnt(ary);
		}
		
		Customer c = this.customerService.queryCustomerById(id);
		List<CustomerAttach> achs = this.customerService.queryCustomerAttachByCustId(c.getId());
		c.setAttachs(achs);
		this.customerService.deleteCustomer(c);
		
		Map<String, String> map = this.sectionCombo();
		String path = map.get(c.getSection()) + "-" + c.getCustName();
		File df = new File(path);
		df.delete();
		
		this.systemService.updateSysRecord(user, "個案管理【刪除客戶】", c.getCustName());
		return SUCCESS;
	}
	
	
	/**
	 * 刪除文件
	 * @return
	 */
	public String deleteDocument(){
		Account user = (Account)request.getSession().getAttribute(SESSION_LOGIN_USER);
		
		CustomerAttach ach = this.customerService.queryCustomerAttachById(attrId);
		if(ach != null){
			this.customerService.deleteCustomerAttach(ach);
			
			String filePath = ach.getFilePath()+ach.getFileName();
			File df = new File(filePath);
			df.delete();
			success = "Y";
			
			this.systemService.updateSysRecord(user, "個案管理【刪除文件】", filePath);
		}
		return SUCCESS;
	}
	
	
	private String loadConfig(String param) {
		ResourceBundle rb = ResourceBundle.getBundle("config");
		return rb.getString(param);
	}

	
	/**
	 * 產業類別(for 個案管理)
	 * @return
	 */
	public Map<String, String> sectionCombo() {
		Account user = (Account)request.getSession().getAttribute(SESSION_LOGIN_USER);
		Map<String, Authority> auth = user.getAuthority();
		
		Map<String, String> map = new LinkedHashMap<String, String>();
		if(auth.containsKey("F02")){
			List<Attribute> list = this.systemService.queryAttributesByType(AttributeType.section.name(), "Y");
			Map<String, Attribute> m = new LinkedHashMap<String, Attribute>();
			for (Attribute a : list) {
				m.put(a.getAttrKey(), a);
			}
			
			Authority au = auth.get("F02");
			if(StringUtils.isNotEmpty(au.getSection())){
				String[] str = au.getSection().split(",");
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
	 * 產品類別
	 * @return
	 */
	public String categoryCombo() {
		try {
			Map<String, String> map = new LinkedHashMap<String, String>();
			List<Attribute> list = this.systemService.querySubAttributes(AttributeType.category.name(), parent, "Y");
			for (Attribute a : list) {
				map.put(a.getAttrKey(), a.getAttrName());
			}
			
			JSONObject json = JSONObject.fromObject(map);
			jsonStr = json.toString();
			success = "true";
		} catch (Exception e) {
			message = e.getMessage();
			success = "false";
		}
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
	public String getJsonStr() {
		return jsonStr;
	}
	public void setJsonStr(String jsonStr) {
		this.jsonStr = jsonStr;
	}
	public String getParent() {
		return parent;
	}
	public void setParent(String parent) {
		this.parent = parent;
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
	public long getAttrId() {
		return attrId;
	}
	public void setAttrId(long attrId) {
		this.attrId = attrId;
	}
	public int getCustCnt() {
		return custCnt;
	}
	public void setCustCnt(int custCnt) {
		this.custCnt = custCnt;
	}
	public String getFlag() {
		return flag;
	}
	public void setFlag(String flag) {
		this.flag = flag;
	}
	public String getShSection() {
		return shSection;
	}
	public void setShSection(String shSection) {
		this.shSection = shSection;
	}
	public String getShSectionName() {
		return shSectionName;
	}
	public void setShSectionName(String shSectionName) {
		this.shSectionName = shSectionName;
	}
	public String getShCategory() {
		return shCategory;
	}
	public void setShCategory(String shCategory) {
		this.shCategory = shCategory;
	}
	public String getShCategoryName() {
		return shCategoryName;
	}
	public void setShCategoryName(String shCategoryName) {
		this.shCategoryName = shCategoryName;
	}
	public String getShCust() {
		return shCust;
	}
	public void setShCust(String shCust) {
		this.shCust = shCust;
	}
	public String getShKeyword() {
		return shKeyword;
	}
	public void setShKeyword(String shKeyword) {
		this.shKeyword = shKeyword;
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
	public Customer getCustomer() {
		return customer;
	}
	public void setCustomer(Customer customer) {
		this.customer = customer;
	}
	public CustomerAttach getCustomerAttach() {
		return customerAttach;
	}
	public void setCustomerAttach(CustomerAttach customerAttach) {
		this.customerAttach = customerAttach;
	}
	public File[] getAupload() {
		return aupload;
	}
	public void setAupload(File[] aupload) {
		this.aupload = aupload;
	}
	public String[] getAuploadFileName() {
		return auploadFileName;
	}
	public void setAuploadFileName(String[] auploadFileName) {
		this.auploadFileName = auploadFileName;
	}
	public String[] getAuploadContentType() {
		return auploadContentType;
	}
	public void setAuploadContentType(String[] auploadContentType) {
		this.auploadContentType = auploadContentType;
	}
	public File[] getBupload() {
		return bupload;
	}
	public void setBupload(File[] bupload) {
		this.bupload = bupload;
	}
	public String[] getBuploadFileName() {
		return buploadFileName;
	}
	public void setBuploadFileName(String[] buploadFileName) {
		this.buploadFileName = buploadFileName;
	}
	public String[] getBuploadContentType() {
		return buploadContentType;
	}
	public void setBuploadContentType(String[] buploadContentType) {
		this.buploadContentType = buploadContentType;
	}
	public File[] getCupload() {
		return cupload;
	}
	public void setCupload(File[] cupload) {
		this.cupload = cupload;
	}
	public String[] getCuploadFileName() {
		return cuploadFileName;
	}
	public void setCuploadFileName(String[] cuploadFileName) {
		this.cuploadFileName = cuploadFileName;
	}
	public String[] getCuploadContentType() {
		return cuploadContentType;
	}
	public void setCuploadContentType(String[] cuploadContentType) {
		this.cuploadContentType = cuploadContentType;
	}
	public File[] getDupload() {
		return dupload;
	}
	public void setDupload(File[] dupload) {
		this.dupload = dupload;
	}
	public String[] getDuploadFileName() {
		return duploadFileName;
	}
	public void setDuploadFileName(String[] duploadFileName) {
		this.duploadFileName = duploadFileName;
	}
	public String[] getDuploadContentType() {
		return duploadContentType;
	}
	public void setDuploadContentType(String[] duploadContentType) {
		this.duploadContentType = duploadContentType;
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