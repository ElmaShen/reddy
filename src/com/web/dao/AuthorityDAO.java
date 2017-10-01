package com.web.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.web.dao.entity.Authority;



@Repository("authorityDAO")
public class AuthorityDAO extends RootDAO {
	
	
	/**
	 * 依Account id查詢
	 * @param accId
	 * @return
	 */
	public List<Authority> queryByAccid(long accId){
		Criteria crit = sessionFactory.getCurrentSession().createCriteria(Authority.class);
		crit.add(Restrictions.eq("accId", accId));
		return crit.list();
	}
	
	
	/**
	 * 依帳號查詢
	 * @param account
	 * @return
	 */
	public List<Authority> queryByAccount(String account){
		Criteria crit = sessionFactory.getCurrentSession().createCriteria(Authority.class);
		crit.add(Restrictions.eq("account", account));
		return crit.list();
	}
}