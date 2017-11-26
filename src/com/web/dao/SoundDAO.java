package com.web.dao;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.Projections;
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
	    * 查詢筆數
	    * @param voices
	    * @return
	    */
    public int queryCount(String[] voices) {
        Criteria crit = sessionFactory.getCurrentSession().createCriteria(Sound.class);
        crit.add(Restrictions.in("section", voices));
        crit.setProjection(Projections.rowCount());
        return (int)crit.uniqueResult();
    }
	
	
	/**
     * 查詢所有筆數
     * @return
     */
    public int getAllRowCount(String shYear, String shMon, String shArea, String shCust, String shTitle, String shSection, String[] voices,
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
    	if(voices != null && voices.length > 0){
    		if(!StringUtils.isEmpty(shSection)){
        		crit.add(Restrictions.eq("section", shSection));
        	}else {
        		crit.add(Restrictions.in("section", voices));
        	}
    	}else{
    		crit.add(Restrictions.eq("section", "N"));
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
    public List<Sound> queryForPage(String shYear, String shMon, String shArea, String shCust, String shTitle, String shSection, String[] voices,
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
    	if(voices != null && voices.length > 0){
    		if(!StringUtils.isEmpty(shSection)){
        		hql += "and section = :section ";
        	}else {
        		hql += "and section in (:voices)";
        	}
    	}else{
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
    	hql += "order by year desc, month desc, cust_name desc ";
    	
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
    	if(voices != null && voices.length > 0){
    		if(!StringUtils.isEmpty(shSection)){
        		query.setParameter("section", shSection);
        	}else {
        		query.setParameterList("voices", voices);
        	}
    	}else{
    		query.setParameter("section", "N");
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
    
    
    /**
     * 
     * @param startDate
     * @param endDate
     */
    public List<Object[]> queryGroupBySec(Date startDate, Date endDate){
    	String sql = "select section, COUNT(*) from sound where create_date >= :sDate and create_date <= :eDate group by section order by section";
    	Query query = sessionFactory.getCurrentSession().createSQLQuery(sql);
    	query.setParameter("sDate", startDate);
    	query.setParameter("eDate", endDate);
    	return query.list();
    }
    
    
    /**
     * 依產業別刪除
     * @param voice
     */
    public int deleteByVoice(String voice){
    	String sql = "delete from sound where section = :voice ";
    	Query query = sessionFactory.getCurrentSession().createSQLQuery(sql);
    	query.setParameter("voice", voice);
    	return query.executeUpdate();
    }
}