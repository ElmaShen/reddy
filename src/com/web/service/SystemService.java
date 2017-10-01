package com.web.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.web.dao.entity.Account;
import com.web.dao.entity.Attribute;
import com.web.dao.entity.Authority;
import com.web.dao.entity.Func;
import com.web.dao.entity.SysRecord;
import com.web.dao.model.PageBean;

public interface SystemService {
	
	public List<Func> queryAllFuncs();
	public List<Func> queryFuncByAuths(String account);

	//帳號管理
	public Account queryAccountById(long id);
	public Account queryAccount(String account);
	public PageBean queryAccountByPage(String account, String name, String isuse, int pageSize, int page);
	public void updateAccount(Account account);
	public List<Authority> queryAuthorityByAccid(long accId);
	public List<Authority> queryAuthorityByAccount(String account);
	public void updateAuthority(Authority auth);
	public void deleteAuthority(Authority auth);
	
	//屬性設定
	public Attribute queryAttributeById(long id);
	public Attribute queryAttributesByKey(String type, String key, String parent);
	public List<Attribute> queryAttributesByType(String type, String isuse);
	public List<Attribute> querySubAttributes(String type, String parent, String isuse);
	public PageBean queryAttributeByPage(String type, String attrName, int pageSize, int page);
	public void updateAttribute(Attribute attr);
	
	//操作記錄
	public PageBean querySysRecordByPage(String name, Date startDate, Date endDate, int pageSize, int page);
	public void updateSysRecord(SysRecord record);
}
