package main.dao.impl;

import java.io.Serializable;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import main.dao.api.BaseDao;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;

@Repository
public abstract class BaseDaoImpl<T extends Serializable> implements BaseDao<T>  {
	private Class<T> clazz;

	@Autowired
	private SessionFactory sessionFactory;

	public void setClazz(final Class<T> clazzToSet) {
		clazz = clazzToSet;
	}

	public Class<T> getClazz() {
		return clazz;
	}
	
	@Override
	public T getOne(final String name) {
		return getCurrentSession().get(clazz, name);
	}
	
	@Override
	public void add(final T entity) {
		getCurrentSession().persist(entity);
		getCurrentSession().flush();
	}

	@Override
	public List<T> getList() {
		Query<T> query = getCurrentSession().createQuery("from " + clazz.getSimpleName(), clazz);
		return query.list();
	}
	
	@Override
	public void delete(final T entity) {
		getCurrentSession().delete(entity);
	}

	@Override
	public void deleteByName(final String name) {
		final T entity = getOne(name);
		delete(entity);
	}

	protected final Session getCurrentSession() {
		return sessionFactory.getCurrentSession();
	}


}
