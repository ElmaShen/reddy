package com.web.dao.entity;



import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * 個案管理
 * @author Elma Shen
 *
 */
@Entity
@Table(name = "customer")
public class Customer implements java.io.Serializable {
	
	private long id;
	private String custNo;
	private String custName;
	private String section;
	private String category;
	private String keywords;
	private String remark;
	private String batch;
	private String creator;
	private Date createDate;
	
	private String sectionName;
	private String categoryName;
	private List<CustomerAttach> attachs;

	
	public Customer() {
	}

	public Customer(long id, String custNo, String custName, String section, String category, String keywords,
								String remark, String batch, String creator, Date createDate) {
		this.id = id;
		this.custNo = custNo;
		this.custName = custName;
		this.section = section;
		this.category = category;
		this.keywords = keywords;
		this.remark = remark;
		this.batch = batch;
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
	
	@Column(name = "cust_no", nullable = false, length = 50)
	public String getCustNo() {
		return custNo;
	}
	public void setCustNo(String custNo) {
		this.custNo = custNo;
	}

	@Column(name = "cust_name", nullable = false, length = 50)
	public String getCustName() {
		return custName;
	}
	public void setCustName(String custName) {
		this.custName = custName;
	}

	@Column(name = "section", nullable = false, length = 10)
	public String getSection() {
		return section;
	}
	public void setSection(String section) {
		this.section = section;
	}
	
	@Column(name = "category", nullable = false, length = 10)
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}

	@Column(name = "keywords", length = 50)
	public String getKeywords() {
		return keywords;
	}
	public void setKeywords(String keywords) {
		this.keywords = keywords;
	}

	@Column(name = "remark", length = 100)
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}

	@Column(name = "batch", nullable = false, length = 1)
	public String getBatch() {
		return batch;
	}
	public void setBatch(String batch) {
		this.batch = batch;
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
	public String getSectionName() {
		return sectionName;
	}
	public void setSectionName(String sectionName) {
		this.sectionName = sectionName;
	}

	@Transient
	public String getCategoryName() {
		return categoryName;
	}
	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}

	@Transient
	public List<CustomerAttach> getAttachs() {
		return attachs;
	}
	public void setAttachs(List<CustomerAttach> attachs) {
		this.attachs = attachs;
	}
}
