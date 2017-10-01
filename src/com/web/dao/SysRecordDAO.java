package com.web.dao;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.web.dao.entity.SysRecord;



@Repository("sysRecordDAO")
public class SysRecordDAO extends RootDAO {
	
	
	/**
     * 查詢所有筆數
     * @return
     */
    public int getAllRowCount(String name, Date startDate, Date endDate){
    	Criteria crit = sessionFactory.getCurrentSession().createCriteria(SysRecord.class);
    	if(!StringUtils.isEmpty(name)){
    		crit.add(Restrictions.like("name", "%"+name+"%"));
    	}
    	if(startDate != null){
    		crit.add(Restrictions.ge("createDate", startDate));
    	}
    	if(endDate != null){
    		crit.add(Restrictions.le("createDate", endDate));
    	}
		return crit.list().size();
    }
    
    
    /**
     * 分頁查詢
     * @return
     */
    public List<SysRecord> queryForPage(String name, Date startDate, Date endDate, int first, int pageSize){
    	String hql = "from SysRecord where 1=1 ";
    	if(!StringUtils.isEmpty(name)){
    		hql += "and name like :name ";
    	}
    	if(startDate != null){
    		hql += "and createDate >= :startDate ";
    	}
    	if(endDate != null){
    		hql += "and createDate <= :endDate ";
    	}
    	hql += "order by createDate desc ";
    	
        Query query = sessionFactory.getCurrentSession().createQuery(hql);
    	if(!StringUtils.isEmpty(name)){
    		query.setParameter("name", "%"+name+"%");
    	}
    	if(startDate != null){
    		query.setParameter("startDate", startDate);
    	}
    	if(endDate != null){
    		query.setParameter("endDate", endDate);
    	}
        query.setFirstResult(first);
        query.setMaxResults(pageSize);
        List<SysRecord> list = query.list();
        return list;
    }
}