package com.web.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.web.dao.entity.CustomerAttach;



@Repository("customerAttachDAO")
public class CustomerAttachDAO extends RootDAO {
	
	
	/**
	 * 依PK查詢
	 * @param id
	 * @return
	 */
	public CustomerAttach queryById(long id){
		Criteria crit = sessionFactory.getCurrentSession().createCriteria(CustomerAttach.class);
		crit.add(Restrictions.eq("id", id));
		return (CustomerAttach)crit.uniqueResult();
	}
	
	
	/**
	 * 依個案pk查詢
	 * @param custId
	 * @return
	 */
	public List<CustomerAttach> queryByCustId(long custId){
		Criteria crit = sessionFactory.getCurrentSession().createCriteria(CustomerAttach.class);
		crit.add(Restrictions.eq("custId", custId));
		return crit.list();
	}
}