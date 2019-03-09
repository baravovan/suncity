package com.suncity.sun_cities.util;

import org.springframework.http.HttpStatus;

public class CityInfoResponse {
	CityInfo results;
	HttpStatus status;
	
	public CityInfo getResults() {
		return results;
	}
	public HttpStatus getStatus() {
		return status;
	}
	public void setResults(CityInfo results) {
		this.results = results;
	}
	public void setStatus(HttpStatus status) {
		this.status = status;
	}
}
