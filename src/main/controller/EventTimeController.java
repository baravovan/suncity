package main.controller;

import java.util.Arrays;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.UnexpectedRollbackException;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import main.service.SunService;

@RestController
public class EventTimeController {
	
static Logger log = Logger.getLogger(EventTimeController.class.getName());
	
	@Autowired
	SunService service;
	
	@RequestMapping(value = "/eventtime", method = RequestMethod.POST)
	@ResponseBody
	public String getTime(@RequestParam(value="action") String action, @RequestParam(value="date") String date, @RequestParam(value="city") String[] city) {
		List<String> list = Arrays.asList(city);
		String result = "";
		switch (action) {
        case "sunset":  
        	result = service.getSunsets(list, date).getBody();
            break;
        case "sunrise":  
        	result = service.getSunrises(list, date).getBody();
            break;
        default: 
        	result = service.getBoth(list, date).getBody();
            break;
		}
		return result;
	}
}
