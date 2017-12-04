package com.web.dao;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.web.dao.entity.Customer;



@Repository("customerDAO")
public class CustomerDAO extends RootDAO {
	
	/**
	 * 查詢最大客戶編號
	 * @return
	 */
	public String queryMaxCno(){
    	Criteria crit = sessionFactory.getCurrentSession().createCriteria(Customer.class);
		crit.setProjection(Projections.max("custNo"));
		if(crit.uniqueResult() != null){
			return crit.uniqueResult().toString();
		}
		return "C0000000";
    }
	
	/**
	 * 依PK查詢
	 * @param id
	 * @return
	 */
	public Customer queryById(long id){
		Criteria crit = sessionFactory.getCurrentSession().createCriteria(Customer.class);
		crit.add(Restrictions.eq("id", id));
		return (Customer)crit.uniqueResult();
	}
	
	
	/**
    * 查詢筆數
    * @param sections
    * @return
    */
	 public int queryCount(String[] sections) {
	     Criteria crit = sessionFactory.getCurrentSession().createCriteria(Customer.class);
	     crit.add(Restrictions.in("section", sections));
	     crit.setProjection(Projections.rowCount());
	     return (int)crit.uniqueResult();
	 }
	 
	 /**
    * 依產業別查詢
    * @param section
    * @return
    */
	 public List<Customer> queryBySection(String section){
		 Criteria crit = sessionFactory.getCurrentSession().createCriteria(Customer.class);
	     crit.add(Restrictions.eq("section", section));
	     return crit.list();
	 }
	
	/**
     * 查詢所有筆數
     * @return
     */
    public int getAllRowCount(String section, String[] sections, String category, String custName, String keyword){
    	Criteria crit = sessionFactory.getCurrentSession().createCriteria(Customer.class);
    	if(sections != null && sections.length > 0){
    		if(!StringUtils.isEmpty(section)){
        		crit.add(Restrictions.eq("section", section));
        	}else {
        		crit.add(Restrictions.in("section", sections));
        	}
    	}else{
    		crit.add(Restrictions.eq("section", "N"));
    	}
    	if(!StringUtils.isEmpty(category)){
    		crit.add(Restrictions.like("category", category));
    	}
    	if(!StringUtils.isEmpty(custName)){
    		crit.add(Restrictions.like("custName", "%"+custName+"%"));
    	}
    	if(!StringUtils.isEmpty(keyword)){
    		crit.add(Restrictions.like("keywords", "%"+keyword+"%"));
    	}
		return crit.list().size();
    }
    
    
    /**
     * 分頁查詢
     * @return
     */
    public List<Customer> queryForPage(String section, String[] sections, String category, String custName, String keyword, int first, int pageSize){
    	String hql = "from Customer where 1=1 ";
    	if(sections != null && sections.length > 0){
    		if(!StringUtils.isEmpty(section)){
        		hql += "and section = :section ";
        	}else {
        		hql += "and section in (:sections) ";
        	}
    	}else{
    		hql += "and section = :section ";
    	}
    	if(!StringUtils.isEmpty(category)){
    		hql += "and category = :category ";
    	}
    	if(!StringUtils.isEmpty(custName)){
    		hql += "and custName like :custName ";
    	}
    	if(!StringUtils.isEmpty(keyword)){
    		hql += "and keywords like :keywords ";
    	}
    	hql += "order by createDate desc ";
    	
        Query query = sessionFactory.getCurrentSession().createQuery(hql);
        if(sections != null && sections.length > 0){
        	if(!StringUtils.isEmpty(section)){
            	query.setParameter("section", section);
        	}else {
        		query.setParameterList("sections", sections);
        	}
        }else{
        	query.setParameter("section", "N");
        }
    	if(!StringUtils.isEmpty(category)){
    		query.setParameter("category", category);
    	}
    	if(!StringUtils.isEmpty(custName)){
    		query.setParameter("custName", "%"+custName+"%");
    	}
    	if(!StringUtils.isEmpty(keyword)){
    		query.setParameter("keywords", "%"+keyword+"%");
    	}
        query.setFirstResult(first);
        query.setMaxResults(pageSize);
        List<Customer> list = query.list();
        return list;
    }
}