package com.suncity.sun_cities.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "This city name is already in DB")
public class CityAlreadyInDBException extends RuntimeException{
	
	private static final long serialVersionUID = 4387942087145580945L;
	
}
