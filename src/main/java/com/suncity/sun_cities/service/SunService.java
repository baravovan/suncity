package com.suncity.sun_cities.service;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import com.google.gson.Gson;
import com.suncity.sun_cities.model.City;
import com.suncity.sun_cities.util.EventTimeResponse;
import com.suncity.sun_cities.util.ParameterStringBuilder;
import com.suncity.sun_cities.util.CityInfoResponse;

@Component
public class SunService {
	
	@Autowired
	CitiesService service;
	
	private static Logger log = Logger.getLogger(SunService.class.getName());
	
	@Transactional
	public ResponseEntity<CityInfoResponse> getDates(Float longitude, Float lattitude, String date){
		CityInfoResponse result = null;
		try {
			String uri = "https://api.sunrise-sunset.org/json?";
			Map<String, String> parameters = new HashMap<>();
			parameters.put("lat", lattitude.toString());
			parameters.put("lng", longitude.toString());
			
			if(!StringUtils.isBlank(date))
				parameters.put("date", date);
			else
				parameters.put("date", "today");
			
			String resultParams = ParameterStringBuilder.getParamsString(parameters);
		    RestTemplate restTemplate = new RestTemplate();
		    result = restTemplate.getForObject(uri + resultParams, CityInfoResponse.class);
		}catch(HibernateException hex) {
			log.info("Hibernate Error occured " + hex.getMessage());
			return new ResponseEntity<CityInfoResponse>(result, HttpStatus.EXPECTATION_FAILED);
		}
		catch(UnsupportedEncodingException ex) {
			log.info("ParameterStringBuilder Error occured " + ex.getMessage());
			return new ResponseEntity<CityInfoResponse>(result, HttpStatus.EXPECTATION_FAILED);
		}
		return new ResponseEntity<CityInfoResponse>(result, HttpStatus.OK);
	}
	
	@Transactional
	public ResponseEntity<String> getSunset(String cityName, String date){
		String result = "";
		try{
			ResponseEntity<City> responseGet = service.getCity(cityName);
			City getCity = (City) responseGet.getBody();
			if(getCity != null) {
				ResponseEntity<CityInfoResponse> response = getDates(getCity.getLongitude(), getCity.getLattitude(), date);
				Gson gson = new Gson();  
				result = gson.toJson(new EventTimeResponse(cityName, date, "", response.getBody().getResults().getSunset())); 				
			}
		}catch(Exception e) {
			log.info("Error occured getSunset" + e.getMessage());
			return new ResponseEntity<String>("getSunset occured", HttpStatus.EXPECTATION_FAILED);
		}
		return new ResponseEntity<String>(result, HttpStatus.OK);
	}
	
	@Transactional
	public ResponseEntity<String> getSunsets(List<String> cityNames, String date){
		String result = "";
		List<EventTimeResponse> list = new ArrayList<>(); 
		try {
			for(String cityName : cityNames) {
				ResponseEntity<City> responseGet = service.getCity(cityName);
				City getCity = (City) responseGet.getBody();
				if(getCity != null) {
					ResponseEntity<CityInfoResponse> response = getDates(getCity.getLongitude(), getCity.getLattitude(), date);
					list.add(new EventTimeResponse(cityName, date, "", response.getBody().getResults().getSunset()));
				}
			}
			
			Gson gson = new Gson();  
			result = gson.toJson(list); 
			
		}catch(Exception e) {
			log.info("Error occured getSunset" + e.getMessage());
			return new ResponseEntity<String>("getSunset occured", HttpStatus.EXPECTATION_FAILED);
		}
		return new ResponseEntity<String>(result, HttpStatus.OK);
	}
	
	@Transactional
	public ResponseEntity<String> getSunrise(String cityName, String date){
		String result = "";
		try{
			ResponseEntity<City> responseGet = service.getCity(cityName);
			City getCity = (City) responseGet.getBody();
			if(getCity != null) {
				ResponseEntity<CityInfoResponse> response = getDates(getCity.getLongitude(), getCity.getLattitude(), date);
				Gson gson = new Gson();  
				result = gson.toJson(new EventTimeResponse(cityName, date, response.getBody().getResults().getSunrise(), "")); 
			}
		}catch(Exception e) {
			log.info("Error occured getSunset" + e.getMessage());
			return new ResponseEntity<String>("getSunset occured", HttpStatus.EXPECTATION_FAILED);
		}
		return new ResponseEntity<String>(result, HttpStatus.OK);
	}
	
	@Transactional
	public ResponseEntity<String> getSunrises(List<String> cityNames, String date){
		String result = "";
		List<EventTimeResponse> list = new ArrayList<>(); 
		try {
			for(String cityName : cityNames) {
				ResponseEntity<City> responseGet = service.getCity(cityName);
				City getCity = (City) responseGet.getBody();
				if(getCity != null) {
					ResponseEntity<CityInfoResponse> response = getDates(getCity.getLongitude(), getCity.getLattitude(), date);
					list.add(new EventTimeResponse(cityName, date, response.getBody().getResults().getSunrise(), ""));
				}
			}
			
			Gson gson = new Gson();  
			result = gson.toJson(list); 
			
		}catch(Exception e) {
			log.info("Error occured getSunset" + e.getMessage());
			return new ResponseEntity<String>("getSunset occured", HttpStatus.EXPECTATION_FAILED);
		}
		return new ResponseEntity<String>(result, HttpStatus.OK);
	}
	
	@Transactional
	public ResponseEntity<String> getBoth(List<String> cityNames, String date){
		String result = "";
		List<EventTimeResponse> list = new ArrayList<>(); 
	
		try{
			for(String cityName : cityNames) {			
				ResponseEntity<City> responseGet = service.getCity(cityName);
				City getCity = (City) responseGet.getBody();
				if(getCity != null) {
					ResponseEntity<CityInfoResponse> response = getDates(getCity.getLongitude(), getCity.getLattitude(), date);
					list.add(new EventTimeResponse(cityName, date, response.getBody().getResults().getSunrise(), response.getBody().getResults().getSunset()));
				}
			}
			
			Gson gson = new Gson();  
			result = gson.toJson(list); 
		}catch(Exception e) {
			log.info("Error occured getSunset" + e.getMessage());
			return new ResponseEntity<String>("getSunset occured", HttpStatus.EXPECTATION_FAILED);
		}
		return new ResponseEntity<String>(result, HttpStatus.OK);
	}
}
