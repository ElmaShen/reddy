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
 * 音檔管理
 * @author Elma Shen
 *
 */
@Entity
@Table(name = "sound")
public class Sound implements java.io.Serializable {
	
	private long id;
	private String year;
	private String month;
	private String section;
	private String custName;
	private String title;
	private String role;
	private String area;
	private String skill;
	private String tone;
	private int second;
	private String filePath;
	private String fileName;
	private String batch;
	private String creator;
	private Date createDate;
	
	private String sectionName;
	private String toneName;
	private String skillName;
	private String fileUri;
	
	
	public Sound() {
	}

	public Sound(long id, String year, String month, String section, String custName, String title, String role, String area, String skill,
			String tone, int second, String filePath, String fileName, String batch, String creator, Date createDate) {
		this.id = id;
		this.year = year;
		this.month = month;
		this.section = section;
		this.custName = custName;
		this.title = title;
		this.role = role;
		this.area = area;
		this.skill = skill;
		this.tone = tone;
		this.second = second;
		this.filePath = filePath;
		this.fileName = fileName;
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
	
	@Column(name = "year", nullable = false, length = 4)
	public String getYear() {
		return year;
	}
	public void setYear(String year) {
		this.year = year;
	}

	@Column(name = "month", length = 2)
	public String getMonth() {
		return month;
	}
	public void setMonth(String month) {
		this.month = month;
	}

	@Column(name = "section", nullable = false, length = 10)
	public String getSection() {
		return section;
	}
	public void setSection(String section) {
		this.section = section;
	}

	@Column(name = "cust_name", nullable = false, length = 50)
	public String getCustName() {
		return custName;
	}
	public void setCustName(String custName) {
		this.custName = custName;
	}
	
	@Column(name = "title", length = 50)
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}

	@Column(name = "role", length = 10)
	public String getRole() {
		return role;
	}
	public void setRole(String role) {
		this.role = role;
	}

	@Column(name = "area", length = 20)
	public String getArea() {
		return area;
	}
	public void setArea(String area) {
		this.area = area;
	}

	@Column(name = "skill", nullable = false, length = 10)
	public String getSkill() {
		return skill;
	}
	public void setSkill(String skill) {
		this.skill = skill;
	}

	@Column(name = "tone", nullable = false, length = 10)
	public String getTone() {
		return tone;
	}
	public void setTone(String tone) {
		this.tone = tone;
	}

	@Column(name = "second", nullable = false, length = 11)
	public int getSecond() {
		return second;
	}
	public void setSecond(int second) {
		this.second = second;
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
	public String getToneName() {
		return toneName;
	}
	public void setToneName(String toneName) {
		this.toneName = toneName;
	}

	@Transient
	public String getSkillName() {
		return skillName;
	}
	public void setSkillName(String skillName) {
		this.skillName = skillName;
	}

	@Transient
	public String getFileUri() {
		return fileUri;
	}
	public void setFileUri(String fileUri) {
		this.fileUri = fileUri;
	}
}
