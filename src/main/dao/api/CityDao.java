package main.dao.api;

import main.model.City;

public interface  CityDao <T extends City> extends BaseDao<T>{
	
	City getCity(String name);
	
}
