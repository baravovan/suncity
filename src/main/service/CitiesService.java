package main.service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import main.dao.api.CityDao;
import main.exception.CityAlreadyInDBException;
import main.exception.ThereIsNoSuchCityException;
import main.model.City;

@Component
public class CitiesService {
	
	static Logger log = Logger.getLogger(CitiesService.class.getName());
	
	@Autowired
	private CityDao<City> cityDao;
	
	@Transactional
	public ResponseEntity<String> createCity(City city){
		
		ResponseEntity<List<City>> responceList = getCities();
		List<City> list = (List<City>) responceList.getBody();
		Set<String> set = new HashSet<String>();
		
		for(City item:list)
			set.add(item.getName());
		
		if(set.contains(city.getName()))
			throw new CityAlreadyInDBException();
		
		cityDao.add(city);
		
		return new ResponseEntity<String>("City " + city.getName() + " was created", HttpStatus.OK);
	}
	
	@Transactional
	public HttpEntity<String> deleteCity(String name){
		try {
			City entity = cityDao.getCity(name);
			if(entity != null)
				cityDao.delete(entity);
		}catch(Exception ex) {
			log.info("Error occured " + ex.getMessage());
			return new ResponseEntity<String>("city was not deleted", HttpStatus.EXPECTATION_FAILED);
		}
		return new ResponseEntity<String>("City " + name + " was deleted", HttpStatus.OK);
	}
	
	@Transactional
	public ResponseEntity<City> getCity(String name){
		City city = null;
			city = cityDao.getCity(name);
			if(city == null) {
				throw new ThereIsNoSuchCityException();
			}
		return new ResponseEntity<City>(city, HttpStatus.OK);
	}
	
	@Transactional
	public ResponseEntity<List<City>> getCities(){
		List<City> list = new ArrayList<City>();
		try {
			list = cityDao.getList();
		}catch(Exception ex) {
			log.info("Error occured " + ex.getMessage());
			return new ResponseEntity<List<City>>(list, HttpStatus.EXPECTATION_FAILED);
		}
		return new ResponseEntity<List<City>>(list, HttpStatus.OK);
	}
}
