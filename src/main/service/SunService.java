package main.service;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import com.google.gson.Gson;

import main.model.City;
import main.util.EventTimeResponse;
import main.util.ParameterStringBuilder;
import org.json.*;

@Component
public class SunService {
	
	@Autowired
	CitiesService service;
	
	static Logger log = Logger.getLogger(SunService.class.getName());
	
	@Transactional
	public ResponseEntity<String> getDates(Float longitude, Float lattitude, String date){
		String result;
		try {
			String uri = "https://api.sunrise-sunset.org/json?";
			Map<String, String> parameters = new HashMap<>();
			parameters.put("lat", lattitude.toString());
			parameters.put("lng", longitude.toString());
			
			if(!date.equals(null) && !date.equals(""))
				parameters.put("date", date);
			else
				parameters.put("date", "today");
			
			String resultParams = ParameterStringBuilder.getParamsString(parameters);
		    RestTemplate restTemplate = new RestTemplate();
		    result = restTemplate.getForObject(uri + resultParams, String.class);
		}catch(HibernateException hex) {
			log.info("Hibernate Error occured " + hex.getMessage());
			return new ResponseEntity<String>("Sun API getDates failed Hibernate Error", HttpStatus.EXPECTATION_FAILED);
		}
		catch(UnsupportedEncodingException ex) {
			log.info("ParameterStringBuilder Error occured " + ex.getMessage());
			return new ResponseEntity<String>("Sun API getDates failed ParameterStringBuilder Error", HttpStatus.EXPECTATION_FAILED);
		}
		return new ResponseEntity<String>(result, HttpStatus.OK);
	}
	
	@Transactional
	public ResponseEntity<String> getSunset(String cityName, String date){
		String result = "";
		try{
			ResponseEntity<City> responseGet = service.getCity(cityName);
			City getCity = (City) responseGet.getBody();
			if(getCity != null) {
				ResponseEntity<String> response = getDates(getCity.getLongitude(), getCity.getLattitude(), date);
				JSONObject obj = new JSONObject(response.getBody());
				String sunset = obj.getJSONObject("results").getString("sunset");
				Gson gson = new Gson();  
				result = gson.toJson(new EventTimeResponse(cityName, date, "", sunset)); 				
			}
		}catch(JSONException ex) {
			log.info("Error occured JSONException" + ex.getMessage());
			return new ResponseEntity<String>("JSONException occured", HttpStatus.EXPECTATION_FAILED);
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
					ResponseEntity<String> response = getDates(getCity.getLongitude(), getCity.getLattitude(), date);
					JSONObject obj = new JSONObject(response.getBody());
					String sunset = obj.getJSONObject("results").getString("sunset");
					list.add(new EventTimeResponse(cityName, date, "", sunset));
				}
			}
			
			Gson gson = new Gson();  
			result = gson.toJson(list); 
			
		}catch(JSONException ex) {
			log.info("Error occured JSONException" + ex.getMessage());
			return new ResponseEntity<String>("JSONException occured", HttpStatus.EXPECTATION_FAILED);
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
				ResponseEntity<String> response = getDates(getCity.getLongitude(), getCity.getLattitude(), date);
				JSONObject obj = new JSONObject(response.getBody());
				String sunrise = obj.getJSONObject("results").getString("sunrise");
				Gson gson = new Gson();  
				result = gson.toJson(new EventTimeResponse(cityName, date, sunrise, "")); 
			}
		}catch(JSONException ex) {
			log.info("Error occured JSONException" + ex.getMessage());
			return new ResponseEntity<String>("JSONException occured", HttpStatus.EXPECTATION_FAILED);
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
					ResponseEntity<String> response = getDates(getCity.getLongitude(), getCity.getLattitude(), date);
					JSONObject obj = new JSONObject(response.getBody());
					String sunrise = obj.getJSONObject("results").getString("sunrise");
					list.add(new EventTimeResponse(cityName, date, sunrise, ""));
				}
			}
			
			Gson gson = new Gson();  
			result = gson.toJson(list); 
			
		}catch(JSONException ex) {
			log.info("Error occured JSONException" + ex.getMessage());
			return new ResponseEntity<String>("JSONException occured", HttpStatus.EXPECTATION_FAILED);
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
					ResponseEntity<String> response = getDates(getCity.getLongitude(), getCity.getLattitude(), date);
					JSONObject obj = new JSONObject(response.getBody());
					String sunrise = obj.getJSONObject("results").getString("sunrise");
					String sunset = obj.getJSONObject("results").getString("sunset");	
					list.add(new EventTimeResponse(cityName, date, sunrise, sunset));
				}
			}
			
			Gson gson = new Gson();  
			result = gson.toJson(list); 
		}catch(JSONException ex) {
			log.info("Error occured JSONException" + ex.getMessage());
			return new ResponseEntity<String>("JSONException occured", HttpStatus.EXPECTATION_FAILED);
		}
		return new ResponseEntity<String>(result, HttpStatus.OK);
	}
}
