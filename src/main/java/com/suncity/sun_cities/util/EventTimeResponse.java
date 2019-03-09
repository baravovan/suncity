package com.suncity.sun_cities.util;

public class EventTimeResponse {
	String city;
	String date;
	String sunrise;
	String sunset;
	
	public EventTimeResponse() {}
	
	public EventTimeResponse(String city,String date,String sunrise,String sunset) {
		this.city = city;
		this.date = date;
		this.sunrise = sunrise;
		this.sunset = sunset;
	}

	public String getCity() {
		return city;
	}

	public String getDate() {
		return date;
	}

	public String getSunrise() {
		return sunrise;
	}

	public String getSunset() {
		return sunset;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public void setSunrise(String sunrise) {
		this.sunrise = sunrise;
	}

	public void setSunset(String sunset) {
		this.sunset = sunset;
	}
}
