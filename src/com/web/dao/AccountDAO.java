package com.web.dao;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.web.dao.entity.Account;



@Repository("accountDAO")
public class AccountDAO extends RootDAO {
	
	
	/**
	 * 依PK查詢
	 * @param id
	 * @return
	 */
	public Account queryById(long id){
		Criteria crit = sessionFactory.getCurrentSession().createCriteria(Account.class);
		crit.add(Restrictions.eq("id", id));
		return (Account)crit.uniqueResult();
	}
	
	
	/**
	 * 依帳號查詢
	 * @param account
	 * @return
	 */
	public Account queryByAccount(String account){
		Criteria crit = sessionFactory.getCurrentSession().createCriteria(Account.class);
		crit.add(Restrictions.eq("account", account));
		return (Account)crit.uniqueResult();
	}
	
	
	/**
     * 查詢所有筆數
     * @return
     */
    public int getAllRowCount(String account, String name, String isuse){
    	Criteria crit = sessionFactory.getCurrentSession().createCriteria(Account.class);
    	if(!StringUtils.isEmpty(account)){
    		crit.add(Restrictions.eq("account", account));
    	}
    	if(!StringUtils.isEmpty(name)){
    		crit.add(Restrictions.like("name", "%"+name+"%"));
    	}
    	if(!StringUtils.isEmpty(isuse)){
    		crit.add(Restrictions.eq("isuse", isuse));
    	}
		return crit.list().size();
    }
    
    
    /**
     * 分頁查詢
     * @return
     */
    public List<Account> queryForPage(String account, String name, String isuse, int first, int pageSize){
    	String hql = "from Account where 1=1 ";
    	if(!StringUtils.isEmpty(account)){
    		hql += "and account = :account ";
    	}
    	if(!StringUtils.isEmpty(name)){
    		hql += "and name like :name ";
    	}
    	if(!StringUtils.isEmpty(isuse)){
    		hql += "and isuse = :isuse ";
    	}
    	hql += "order by createDate desc ";
    	
        Query query = sessionFactory.getCurrentSession().createQuery(hql);
    	if(!StringUtils.isEmpty(account)){
    		query.setParameter("account", account);
    	}
    	if(!StringUtils.isEmpty(name)){
    		query.setParameter("name", "%"+name+"%");
    	}
    	if(!StringUtils.isEmpty(isuse)){
    		query.setParameter("isuse", isuse);
    	}
        query.setFirstResult(first);
        query.setMaxResults(pageSize);
        List<Account> list = query.list();
        return list;
    }
}