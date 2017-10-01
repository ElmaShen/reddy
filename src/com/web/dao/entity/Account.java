package com.web.dao.entity;



import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * 帳號管理
 * @author Elma Shen
 *
 */
@Entity
@Table(name = "account")
public class Account implements java.io.Serializable {

	private long id;
	private String account;
	private String name;
	private String password;
	private String isuse;
	private String creator;
	private Date createDate;
	private Date loginDate;
	
	private Map<String, Authority> authority;

	
	public Account() {
	}

	public Account(long id, String account, String name, String password, String isuse, 
			String creator, Date createDate, Date loginDate) {
		this.id = id;
		this.account = account;
		this.name = name;
		this.password = password;
		this.isuse = isuse;
		this.creator = creator;
		this.createDate = createDate;
		this.loginDate = loginDate;
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

	@Column(name = "password", nullable = false, length = 20)
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}

	@Column(name = "isuse", nullable = false, length = 1)
	public String getIsuse() {
		return isuse;
	}
	public void setIsuse(String isuse) {
		this.isuse = isuse;
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

	@Column(name = "login_date")
	public Date getLoginDate() {
		return loginDate;
	}
	public void setLoginDate(Date loginDate) {
		this.loginDate = loginDate;
	}

	@Transient
	public Map<String, Authority> getAuthority() {
		return authority;
	}
	public void setAuthority(Map<String, Authority> authority) {
		this.authority = authority;
	}
}
