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
 * 系統屬性
 * @author Elma Shen
 *
 */
@Entity
@Table(name = "attribute")
public class Attribute implements java.io.Serializable {

	private long id;
	private String type;
	private String attrKey;
	private String attrName;
	private String parentKey;
	private String isuse;
	private String creator;
	private Date createDate;
	
	private String typeName;

	
	public Attribute() {
	}

	public Attribute(long id, String type, String attrKey, String attrName, String parentKey,
			String isuse, String creator, Date createDate) {
		this.id = id;
		this.type = type;
		this.attrKey = attrKey;
		this.attrName = attrName;
		this.parentKey = parentKey;
		this.isuse = isuse;
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

	@Column(name = "type", nullable = false, length = 20)
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}

	@Column(name = "attr_key", nullable = false, length = 20)
	public String getAttrKey() {
		return attrKey;
	}
	public void setAttrKey(String attrKey) {
		this.attrKey = attrKey;
	}

	@Column(name = "attr_name", nullable = false, length = 20)
	public String getAttrName() {
		return attrName;
	}
	public void setAttrName(String attrName) {
		this.attrName = attrName;
	}

	@Column(name = "parent_key", length = 20)
	public String getParentKey() {
		return parentKey;
	}
	public void setParentKey(String parentKey) {
		this.parentKey = parentKey;
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

	@Transient
	public String getTypeName() {
		return typeName;
	}
	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}
}
