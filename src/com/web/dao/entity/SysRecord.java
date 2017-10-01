package com.web.dao.entity;



import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;


/**
 * 操作記錄
 * @author Elma Shen
 *
 */
@Entity
@Table(name = "sys_record")
public class SysRecord implements java.io.Serializable {
	
	private long id;
	private String account;
	private String name;
	private String action;
	private String remark;
	private Date createDate;

	
	public SysRecord() {
	}

	public SysRecord(long id, String account, String name, String action, String remark, Date createDate) {
		this.id = id;
		this.account = account;
		this.name = name;
		this.action = action;
		this.remark = remark;
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

	@Column(name = "account", nullable = false, length = 20)
	public String getAccount() {
		return account;
	}
	public void setAccount(String account) {
		this.account = account;
	}

	@Column(name = "name", nullable = false, length = 30)
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}

	@Column(name = "action", nullable = false, length = 50)
	public String getAction() {
		return action;
	}
	public void setAction(String action) {
		this.action = action;
	}
	
	@Column(name = "remark", length = 500)
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}

	@Column(name = "create_date", nullable = false)
	public Date getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
}
