package com.web.service.impl;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.web.dao.AccountDAO;
import com.web.dao.AttributeDAO;
import com.web.dao.AuthorityDAO;
import com.web.dao.FuncDAO;
import com.web.dao.SysRecordDAO;
import com.web.dao.entity.Account;
import com.web.dao.entity.Attribute;
import com.web.dao.entity.Authority;
import com.web.dao.entity.Func;
import com.web.dao.entity.SysRecord;
import com.web.dao.model.PageBean;
import com.web.service.SystemService;

@Service("systemService")
public class SystemServiceImpl implements SystemService {

	@Autowired
    private FuncDAO funcDAO;
	
	@Autowired
    private AccountDAO accountDAO;
	
	@Autowired
    private AuthorityDAO authorityDAO;
	
	@Autowired
    private AttributeDAO attributeDAO;
	
	@Autowired
    private SysRecordDAO sysRecordDAO;
	
	
	
	
	@Override
	public Account queryAccountById(long id){
		return accountDAO.queryById(id);
	}
	
	@Override
	public Account queryAccount(String account){
		return accountDAO.queryByAccount(account);
	}
	
	@Override
	public PageBean queryAccountByPage(String account, String name, String isuse, int pageSize, int page){
		int totalCount = accountDAO.getAllRowCount(account, name, isuse);    
        int totalPage = PageBean.countTotalPage(pageSize, totalCount);  
        int offset = PageBean.countOffset(pageSize, page); 
        int currentPage = PageBean.countCurrentPage(page);
        List<Account> list = accountDAO.queryForPage(account, name, isuse, offset, pageSize);
		
		//Set PageBean
        PageBean pageBean = new PageBean();
        pageBean.setPageSize(pageSize);    
        pageBean.setCurrentPage(currentPage);
        pageBean.setTotalCount(totalCount);
        pageBean.setTotalPage(totalPage);
        pageBean.setList(list);
        pageBean.init();
        return pageBean;		
	}
	
	@Override
	public void updateAccount(Account account){
		accountDAO.saveOrUpdate(account);
	}
	
	@Override
	public List<Authority> queryAuthorityByAccid(long accId){
		return authorityDAO.queryByAccid(accId);
	}
	
	@Override
	public List<Authority> queryAuthorityByAccount(String account){
		return authorityDAO.queryByAccount(account);
	}
	
	@Override
	public void updateAuthority(Authority auth){
		authorityDAO.saveOrUpdate(auth);
	}
	
	@Override
	public void deleteAuthority(Authority auth){
		authorityDAO.delete(auth);
	}
	
	@Override
	public List<Func> queryAllFuncs(){
		return funcDAO.queryAll();
	}
	
	@Override
	public List<Func> queryFuncByAuths(String account){
		return funcDAO.queryByAuths(account);
	}
	
	@Override
	public Attribute queryAttributeById(long id){
		return attributeDAO.queryById(id);
	}
	
	@Override
	public Attribute queryAttributesByKey(String type, String key, String parent){
		return attributeDAO.queryByKey(type, key, parent);
	}
	
	@Override
	public List<Attribute> queryAttributesByType(String type, String isuse){
		return attributeDAO.queryByType(type, isuse);
	}
	
	@Override
	public List<Attribute> querySubAttributes(String type, String parent, String isuse){
		return attributeDAO.queryByParent(type, parent, isuse);
	}
	
	@Override
	public PageBean queryAttributeByPage(String type, String attrName, int pageSize, int page){
		int totalCount = attributeDAO.getAllRowCount(type, attrName);    
        int totalPage = PageBean.countTotalPage(pageSize, totalCount);  
        int offset = PageBean.countOffset(pageSize, page); 
        int currentPage = PageBean.countCurrentPage(page);
        List<Attribute> list = attributeDAO.queryForPage(type, attrName, offset, pageSize);
		
		//Set PageBean
        PageBean pageBean = new PageBean();
        pageBean.setPageSize(pageSize);    
        pageBean.setCurrentPage(currentPage);
        pageBean.setTotalCount(totalCount);
        pageBean.setTotalPage(totalPage);
        pageBean.setList(list);
        pageBean.init();
        return pageBean;
	}
	
	@Override
	public void updateAttribute(Attribute attr){
		attributeDAO.saveOrUpdate(attr);
	}
	
	@Override
	public PageBean querySysRecordByPage(String name, Date startDate, Date endDate, int pageSize, int page){
		int totalCount = sysRecordDAO.getAllRowCount(name, startDate, endDate);    
        int totalPage = PageBean.countTotalPage(pageSize, totalCount);  
        int offset = PageBean.countOffset(pageSize, page); 
        int currentPage = PageBean.countCurrentPage(page);
        List<SysRecord> list = sysRecordDAO.queryForPage(name, startDate, endDate, offset, pageSize);
		
		//Set PageBean
        PageBean pageBean = new PageBean();
        pageBean.setPageSize(pageSize);    
        pageBean.setCurrentPage(currentPage);
        pageBean.setTotalCount(totalCount);
        pageBean.setTotalPage(totalPage);
        pageBean.setList(list);
        pageBean.init();
        return pageBean;
	}
	
	@Override
	public void updateSysRecord(SysRecord record){
		sysRecordDAO.saveOrUpdate(record);
	}
}
