package com.web.dao;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.web.dao.entity.Attribute;
import com.web.dao.entity.Func;



@Repository("attributeDAO")
public class AttributeDAO extends RootDAO {
	
	
	/**
	 * 依PK查詢
	 * @param type
	 * @param isuse
	 * @return
	 */
	public Attribute queryById(long id){
		Criteria crit = sessionFactory.getCurrentSession().createCriteria(Attribute.class);
		crit.add(Restrictions.eq("id", id));
		return (Attribute)crit.uniqueResult();
	}
	
	
	/**
	 * 依Key值查詢
	 * @param type
	 * @param isuse
	 * @return
	 */
	public Attribute queryByKey(String type, String key, String parent){
		Criteria crit = sessionFactory.getCurrentSession().createCriteria(Attribute.class);
		crit.add(Restrictions.eq("type", type));
		crit.add(Restrictions.eq("attrKey", key));
		if(!StringUtils.isEmpty(parent)){
			crit.add(Restrictions.eq("parentKey", parent));
		}
		return (Attribute)crit.uniqueResult();
	}
	
	/**
	 * 依類型查詢
	 * @param type
	 * @param isuse
	 * @return
	 */
	public List<Attribute> queryByType(String type, String isuse){
		Criteria crit = sessionFactory.getCurrentSession().createCriteria(Attribute.class);
		crit.add(Restrictions.eq("type", type));
		crit.add(Restrictions.eq("isuse", isuse));
		return crit.list();
	}
	
	/**
	 * 查詢次分類
	 * @param type
	 * @param parent
	 * @param isuse
	 * @return
	 */
	public List<Attribute> queryByParent(String type, String parent, String isuse){
		Criteria crit = sessionFactory.getCurrentSession().createCriteria(Attribute.class);
		crit.add(Restrictions.eq("type", type));
		crit.add(Restrictions.eq("parentKey", parent));
		crit.add(Restrictions.eq("isuse", isuse));
		return crit.list();
	}
	
	/**
     * 查詢所有筆數
     * @return
     */
    public int getAllRowCount(String type, String attrName){
    	Criteria crit = sessionFactory.getCurrentSession().createCriteria(Attribute.class);
    	if(!StringUtils.isEmpty(type)){
    		crit.add(Restrictions.eq("type", type));
    	}
    	if(!StringUtils.isEmpty(attrName)){
    		crit.add(Restrictions.like("attrName", "%"+attrName+"%"));
    	}
		return crit.list().size();
    }
    
    /**
     * 分頁查詢
     * @return
     */
    public List<Attribute> queryForPage(String type, String attrName, int first, int pageSize){
    	String hql = "from Attribute where 1=1 ";
    	if(!StringUtils.isEmpty(type)){
    		hql += "and type = :type ";
    	}
    	if(!StringUtils.isEmpty(attrName)){
    		hql += "and attrName like :attrName ";
    	}
    	hql += "order by createDate desc ";
    	
        Query query = sessionFactory.getCurrentSession().createQuery(hql);
        if(!StringUtils.isEmpty(type)){
    		query.setParameter("type", type);
    	}
    	if(!StringUtils.isEmpty(attrName)){
    		query.setParameter("attrName", "%"+attrName+"%");
    	}
        query.setFirstResult(first);
        query.setMaxResults(pageSize);
        List<Attribute> list = query.list();
        return list;
    }
}