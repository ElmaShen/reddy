package com.web.dao;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.web.dao.entity.Audio;



@Repository("audioDAO")
public class AudioDAO extends RootDAO {
	
	
	/**
     * 依PK查詢
     * @param id
     * @return
     */
	public Audio queryById(long id){
		Criteria crit = sessionFactory.getCurrentSession().createCriteria(Audio.class);
		crit.add(Restrictions.eq("id", id));
		return (Audio)crit.uniqueResult();
	}
	
	/**
     * 依群組編號查詢
     * @param gno
     * @return
     */
	public List<Audio> queryByGno(int gno){
		Criteria crit = sessionFactory.getCurrentSession().createCriteria(Audio.class);
		crit.add(Restrictions.eq("gno", gno));
		return crit.list();
	}
	
	/**
     * 查詢所有筆數
     * @return
     */
    public int getAllRowCount(String keywords, String gname){
    	Criteria crit = sessionFactory.getCurrentSession().createCriteria(Audio.class);
    	if(!StringUtils.isEmpty(keywords)){
    		crit.add(Restrictions.like("keywords", "%"+keywords+"%"));
    	}
    	if(!StringUtils.isEmpty(gname)){
    		crit.add(Restrictions.like("gname", "%"+gname+"%"));
    	}
		return crit.list().size();
    }
    
    
    /**
     * 分頁查詢
     * @return
     */
    public List<Audio> queryForPage(String keywords, String gname, int first, int pageSize){
    	String hql = "from Audio where 1=1 ";
    	if(!StringUtils.isEmpty(keywords)){
    		hql += "and keywords like :keywords ";
    	}
    	if(!StringUtils.isEmpty(gname)){
    		hql += "and gname like :gname ";
    	}
    	hql += "order by createDate desc ";
    	
        Query query = sessionFactory.getCurrentSession().createQuery(hql);
        if(!StringUtils.isEmpty(keywords)){
        	query.setParameter("keywords", "%"+keywords+"%");
    	}
    	if(!StringUtils.isEmpty(gname)){
    		query.setParameter("gname", "%"+gname+"%");
    	}
        query.setFirstResult(first);
        query.setMaxResults(pageSize);
        List<Audio> list = query.list();
        return list;
    }
}