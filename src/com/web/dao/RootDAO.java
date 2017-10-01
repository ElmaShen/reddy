package com.web.dao;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;

public class RootDAO {
	
	@Autowired
	protected SessionFactory sessionFactory;

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}
	
	public void saveOrUpdate(Object object) {
		try {
			sessionFactory.getCurrentSession().saveOrUpdate(object);
			flush();
		} catch (RuntimeException re) {
			throw re;
		}
	}

	public void save(Object entity) {
		try {
			sessionFactory.getCurrentSession().save(entity);
			flush();
		} catch (RuntimeException re) {
			throw re;
		}
	}	
	
	public void delete(Object entity) {
		try {
			sessionFactory.getCurrentSession().delete(entity);
			flush();
		} catch (RuntimeException re) {
			throw re;
		}
	}	
	
	
	/**
	 * 取得Hibernate Session
	 * @return
	 */
	public Session getSession(){
		return sessionFactory.getCurrentSession();
	}
 
	public void flush() {
		sessionFactory.getCurrentSession().flush();
	}
}