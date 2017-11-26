package com.web.dao.entity;



import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * 個案管理 - 檔案
 * @author Elma Shen
 *
 */
@Entity
@Table(name = "customer_attach")
public class CustomerAttach implements java.io.Serializable {
	
	private long id;
	private String type;
	private String filePath;
	private String fileName;
	private long custId;
	
	private String fileUri;
	
	public CustomerAttach() {
	}

	public CustomerAttach(long id, String type, String filePath, String fileName, long custId) {
		this.id = id;
		this.type = type;
		this.filePath = filePath;
		this.fileName = fileName;
		this.custId = custId;
	}

	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name = "id", unique = true, nullable = false)
	public long getId() {
		return this.id;
	}
	public void setId(long id) {
		this.id = id;
	}
	
	@Column(name = "type", length = 1)
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}

	@Column(name = "file_path", nullable = false, length = 100)
	public String getFilePath() {
		return filePath;
	}
	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	@Column(name = "file_name", nullable = false, length = 200)
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	
	@Column(name = "cust_id", nullable = false)
	public long getCustId() {
		return custId;
	}
	public void setCustId(long custId) {
		this.custId = custId;
	}
	
	@Transient
	public String getFileUri() {
		return fileUri;
	}
	public void setFileUri(String fileUri) {
		this.fileUri = fileUri;
	}
}
