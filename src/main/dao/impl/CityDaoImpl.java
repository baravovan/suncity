package main.dao.impl;

import javax.transaction.Transactional;

import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;

import main.dao.api.CityDao;
import main.model.City;

@Repository
@Transactional
public class CityDaoImpl extends BaseDaoImpl<City> implements CityDao<City>{
	
	public CityDaoImpl() {
		setClazz(City.class);
	}

	@Override
	public City getCity(String name) {
		Query<?> query = getCurrentSession().createQuery("from City where name = :name");
		query.setParameter("name", name);

		if (query.list().size() < 1)
			return null;
		return (City) query.list().get(0);

	}
	
}
