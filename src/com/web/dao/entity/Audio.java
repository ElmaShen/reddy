package com.web.dao.entity;



import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;


/**
 * 照片及音檔
 * @author Elma Shen
 *
 */
@Entity
@Table(name = "audio")
public class Audio implements java.io.Serializable {

	private long id;
	private String keywords;
	private int gno;
	private String gname;
	private String filePath;
	private String fileName;
	private String ftype;
	private String creator;
	private Date createDate;
	
	private String fileUri;

	
	public Audio() {
	}

	public Audio(long id, String keywords, int gno, String gname, String filePath,
			String fileName, String ftype, String creator, Date createDate) {
		this.id = id;
		this.keywords = keywords;
		this.gno = gno;
		this.gname = gname;
		this.filePath = filePath;
		this.fileName = fileName;
		this.ftype = ftype;
		this.creator = creator;
		this.createDate = createDate;
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
	
	@Column(name = "keywords", length = 50)
	public String getKeywords() {
		return keywords;
	}
	public void setKeywords(String keywords) {
		this.keywords = keywords;
	}

	@Column(name = "gno", length = 11)
	public int getGno() {
		return gno;
	}
	public void setGno(int gno) {
		this.gno = gno;
	}

	@Column(name = "gname", length = 50)
	public String getGname() {
		return gname;
	}
	public void setGname(String gname) {
		this.gname = gname;
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

	@Column(name = "ftype", nullable = false, length = 1)
	public String getFtype() {
		return ftype;
	}
	public void setFtype(String ftype) {
		this.ftype = ftype;
	}

	@Column(name = "creator", nullable = false, length = 20)
	public String getCreator() {
		return creator;
	}
	public void setCreator(String creator) {
		this.creator = creator;
	}

	@Column(name = "create_date", nullable = false)
	public Date getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	@Transient
	public String getFileUri() {
		return fileUri;
	}
	public void setFileUri(String fileUri) {
		this.fileUri = fileUri;
	}
}
