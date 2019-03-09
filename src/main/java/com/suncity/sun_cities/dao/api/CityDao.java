package com.suncity.sun_cities.dao.api;

import com.suncity.sun_cities.model.City;

public interface  CityDao <T extends City> extends BaseDao<T>{
	
	City getCity(String name);
	
}
