package com.web.dao.entity;



import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;


/**
 * 權限管理
 * @author Elma Shen
 *
 */
@Entity
@Table(name = "authority")
public class Authority implements java.io.Serializable {
	
	private long id;
	private String account;
	private String fno;
	private String added;
	private String deleted;
	private String download;
	private String voice;
	private String section;
	private Date createDate;
	private long accId;

	
	public Authority() {
	}

	public Authority(long id, String account, String fno, String added, String deleted,
			String download, String voice, String section, Date createDate, long accId) {
		this.id = id;
		this.account = account;
		this.fno = fno;
		this.added = added;
		this.deleted = deleted;
		this.download = download;
		this.voice = voice;
		this.section = section;
		this.createDate = createDate;
		this.accId = accId;
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
	
	@Column(name = "account", nullable = false, length = 20)
	public String getAccount() {
		return account;
	}
	public void setAccount(String account) {
		this.account = account;
	}

	@Column(name = "fno", nullable = false, length = 10)
	public String getFno() {
		return fno;
	}
	public void setFno(String fno) {
		this.fno = fno;
	}

	@Column(name = "added", length = 1)
	public String getAdded() {
		return added;
	}
	public void setAdded(String added) {
		this.added = added;
	}

	@Column(name = "deleted", length = 1)
	public String getDeleted() {
		return deleted;
	}
	public void setDeleted(String deleted) {
		this.deleted = deleted;
	}

	@Column(name = "download", length = 1)
	public String getDownload() {
		return download;
	}
	public void setDownload(String download) {
		this.download = download;
	}
	
	@Column(name = "voice", length = 200)
	public String getVoice() {
		return voice;
	}
	public void setVoice(String voice) {
		this.voice = voice;
	}

	@Column(name = "section", length = 200)
	public String getSection() {
		return section;
	}
	public void setSection(String section) {
		this.section = section;
	}

	@Column(name = "create_date", nullable = false)
	public Date getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	
	@Column(name = "acc_id", nullable = false, length = 20)
	public long getAccId() {
		return accId;
	}
	public void setAccId(long accId) {
		this.accId = accId;
	}
}
