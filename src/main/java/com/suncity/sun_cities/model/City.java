package com.suncity.sun_cities.model;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name="cities")
public class City extends Model implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	private String name;
	private Float longitude;
	private Float lattitude;
	
	public City() {}
	
	public City(String name, Float longitude, Float lattitude) {
		this.name = name;
		this.longitude = longitude;
		this.lattitude = lattitude;
	}
	
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	public String getName() {
		return name;
	}
	public Float getLongitude() {
		return longitude;
	}
	public Float getLattitude() {
		return lattitude;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	public void setLongitude(Float longitude) {
		this.longitude = longitude;
	}
	public void setLattitude(Float lattitude) {
		this.lattitude = lattitude;
	}
}
