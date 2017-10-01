package com.web.dao;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.web.dao.entity.Sound;



@Repository("soundDAO")
public class SoundDAO extends RootDAO {

	
	/**
	 * 依PK查詢
	 * @param id
	 * @return
	 */
	public Sound queryById(long id){
		Criteria crit = sessionFactory.getCurrentSession().createCriteria(Sound.class);
		crit.add(Restrictions.eq("id", id));
		return (Sound)crit.uniqueResult();
	}
	
	
	
	/**
     * 查詢所有筆數
     * @return
     */
    public int getAllRowCount(String shYear, String shMon, String shArea, String shCust, String shTitle, String shSection, 
										Integer shSecond, String shTone, String shRole, String shSkill){
    	Criteria crit = sessionFactory.getCurrentSession().createCriteria(Sound.class);
    	if(!StringUtils.isEmpty(shYear)){
    		crit.add(Restrictions.eq("year", shYear));
    	}
    	if(!StringUtils.isEmpty(shMon)){
    		crit.add(Restrictions.eq("month", shMon));
    	}
    	if(!StringUtils.isEmpty(shArea)){
    		crit.add(Restrictions.like("area", "%"+shArea+"%"));
    	}
    	if(!StringUtils.isEmpty(shCust)){
    		crit.add(Restrictions.like("custName", "%"+shCust+"%"));
    	}
    	if(!StringUtils.isEmpty(shTitle)){
    		crit.add(Restrictions.eq("title", shTitle));
    	}
    	if(!StringUtils.isEmpty(shSection)){
    		crit.add(Restrictions.eq("section", shSection));
    	}
    	if(shSecond != null){
    		crit.add(Restrictions.eq("second", shSecond));
    	}
    	if(!StringUtils.isEmpty(shTone)){
    		crit.add(Restrictions.eq("tone", shTone));
    	}
    	if(!StringUtils.isEmpty(shRole)){
    		crit.add(Restrictions.eq("role", shRole));
    	}
    	if(!StringUtils.isEmpty(shSkill)){
    		crit.add(Restrictions.eq("skill", shSkill));
    	}
		return crit.list().size();
    }
    
    
    /**
     * 分頁查詢
     * @return
     */
    public List<Sound> queryForPage(String shYear, String shMon, String shArea, String shCust, String shTitle, String shSection, 
									Integer shSecond, String shTone, String shRole, String shSkill, int first, int pageSize){
    	String hql = "from Sound where 1=1 ";
    	if(!StringUtils.isEmpty(shYear)){
    		hql += "and year = :year ";
    	}
    	if(!StringUtils.isEmpty(shMon)){
    		hql += "and month = :month ";
    	}
    	if(!StringUtils.isEmpty(shArea)){
    		hql += "and area like :area ";
    	}
    	if(!StringUtils.isEmpty(shCust)){
    		hql += "and custName like :custName ";
    	}
    	if(!StringUtils.isEmpty(shTitle)){
    		hql += "and title = :title ";
    	}
    	if(!StringUtils.isEmpty(shSection)){
    		hql += "and section = :section ";
    	}
    	if(shSecond != null){
    		hql += "and second = :second ";
    	}
    	if(!StringUtils.isEmpty(shTone)){
    		hql += "and tone = :tone ";
    	}
    	if(!StringUtils.isEmpty(shRole)){
    		hql += "and role = :role ";
    	}
    	if(!StringUtils.isEmpty(shSkill)){
    		hql += "and skill = :skill ";
    	}
    	hql += "order by createDate desc ";
    	
        Query query = sessionFactory.getCurrentSession().createQuery(hql);
        if(!StringUtils.isEmpty(shYear)){
        	query.setParameter("year", shYear);
    	}
    	if(!StringUtils.isEmpty(shMon)){
    		query.setParameter("month", shMon);
    	}
    	if(!StringUtils.isEmpty(shArea)){
    		query.setParameter("area", shArea);
    	}
    	if(!StringUtils.isEmpty(shCust)){
    		query.setParameter("custName", "%"+shCust+"%");
    	}
    	if(!StringUtils.isEmpty(shTitle)){
    		query.setParameter("title", "%"+shTitle+"%");
    	}
    	if(!StringUtils.isEmpty(shSection)){
    		query.setParameter("section", shSection);
    	}
    	if(shSecond != null){
    		query.setParameter("second", shSecond);
    	}
    	if(!StringUtils.isEmpty(shTone)){
    		query.setParameter("tone", shTone);
    	}
    	if(!StringUtils.isEmpty(shRole)){
    		query.setParameter("role", shRole);
    	}
    	if(!StringUtils.isEmpty(shSkill)){
    		query.setParameter("skill", shSkill);
    	}
        query.setFirstResult(first);
        query.setMaxResults(pageSize);
        List<Sound> list = query.list();
        return list;
    }
}