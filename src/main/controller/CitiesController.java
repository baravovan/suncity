package main.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.apache.log4j.Logger;

import main.model.City;
import main.service.CitiesService;

@RestController
public class CitiesController {
	
	static Logger log = Logger.getLogger(CitiesController.class.getName());
	
	@Autowired
	CitiesService service;
	
	@RequestMapping(value = "/city/add", method = RequestMethod.POST)
	@ResponseBody
	public HttpEntity<String> createCity(@RequestParam(value="name") String name, @RequestParam(value="long") Float longitude, @RequestParam(value="latt") Float lattitude) {
		City city = new City(name, longitude, lattitude);
		return service.createCity(city);
	}
	
	@RequestMapping(value = "/city/delete", method = RequestMethod.POST)
	@ResponseBody
	public HttpEntity<String> deleteCity(@RequestParam(value="name") String name) {
		return service.deleteCity(name);
	}
	
	@RequestMapping(value = "/city/get", method = RequestMethod.POST)
	@ResponseBody
	public HttpEntity<City> getCity(@RequestParam(value="name") String name) {
		return service.getCity(name);
	}
	
	@RequestMapping(value = "/city/list", method = RequestMethod.POST)
	@ResponseBody
	public HttpEntity<List<City>> list() {
		return service.getCities();
	}
}
