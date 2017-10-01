package com.web.dao.entity;



import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;


/**
 * 系統功能
 * @author Elma Shen
 *
 */
@Entity
@Table(name = "func")
public class Func implements java.io.Serializable {
	
	private long id;
	private String fno;
	private String fname;
	private String action;
	private String icon;
	private String isuse;

	
	public Func() {
	}

	public Func(long id, String fno, String fname, String icon, String action, String isuse) {
		this.id = id;
		this.fno = fno;
		this.fname = fname;
		this.action = action;
		this.icon = icon;
		this.isuse = isuse;
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

	@Column(name = "fno", nullable = false, length = 10)
	public String getFno() {
		return fno;
	}
	public void setFno(String fno) {
		this.fno = fno;
	}

	@Column(name = "fname", nullable = false, length = 20)
	public String getFname() {
		return fname;
	}
	public void setFname(String fname) {
		this.fname = fname;
	}

	@Column(name = "action", length = 100)
	public String getAction() {
		return action;
	}
	public void setAction(String action) {
		this.action = action;
	}
	
	@Column(name = "icon", length = 30)
	public String getIcon() {
		return icon;
	}
	public void setIcon(String icon) {
		this.icon = icon;
	}

	@Column(name = "isuse", nullable = false, length = 1)
	public String getIsuse() {
		return isuse;
	}
	public void setIsuse(String isuse) {
		this.isuse = isuse;
	}
}
