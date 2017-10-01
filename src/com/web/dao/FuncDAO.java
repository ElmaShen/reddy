package com.web.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.SQLQuery;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.web.dao.entity.Func;



@Repository("funcDAO")
public class FuncDAO extends RootDAO {

	
	/**
	 * 啟用的系統功能
	 * @return
	 */
	public List<Func> queryAll(){
		Criteria crit = sessionFactory.getCurrentSession().createCriteria(Func.class);
		crit.add(Restrictions.eq("isuse", "Y"));
		return crit.list();
	}
	
	/**
	 * 依權限查詢
	 * @param account
	 * @return
	 */
	public List<Func> queryByAuths(String account){
		String sql = "select f.* from func f, authority a where a.account='"+account+"' and f.fno = a.fno order by f.fno";
		SQLQuery query = sessionFactory.getCurrentSession().createSQLQuery(sql);
		query.addEntity(Func.class);
		List<Func> list = query.list();
		return list;
	}
}