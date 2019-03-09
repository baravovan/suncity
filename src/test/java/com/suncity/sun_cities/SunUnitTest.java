package com.suncity.sun_cities;

import static org.assertj.core.api.Assertions.assertThat;
import java.util.ArrayList;
import java.util.List;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;

import com.google.gson.Gson;
import com.suncity.sun_cities.model.City;
import com.suncity.sun_cities.service.CitiesService;
import com.suncity.sun_cities.service.SunService;
import com.suncity.sun_cities.util.CityInfoResponse;
import com.suncity.sun_cities.util.EventTimeResponse;
import org.json.*;


@RunWith(SpringRunner.class)
@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class SunUnitTest extends CitiesUnitTest{
		
	@Autowired
	SunService service;
	
	@Autowired
	CitiesService serviceCity;
	
	@Test
	public void test_getDates() throws Exception {
		Float longitude = -4.4203400f;
		Float lattitude = 36.7201600f;
		String date = "2019-03-03";
		ResponseEntity<CityInfoResponse> response = service.getDates(longitude, lattitude, date);

		assertThat(response.getBody().getResults().getSunrise()).isEqualTo("6:45:08 AM");
		assertThat(response.getBody().getResults().getSunset()).isEqualTo("6:13:57 PM");
	}
	
	@Test
	public void test_Sunset() throws Exception {
		City city1 = new City("New City1", 36.0f, 123.10f);
		ResponseEntity<String> response = serviceCity.createCity(city1);
		String date = "2019-03-03";
		response = service.getSunset(city1.getName(), date);
		JSONObject obj = new JSONObject(response.getBody());
		String sunrise = obj.getString("sunset");
		assertThat(sunrise).isEqualTo("4:22:51 PM");
		serviceCity.deleteCity(city1.getName());
	}
	
	@Test
	public void test_Sunrise() throws Exception {
		City city1 = new City("New City1", 36.0f, 123.10f);
		ResponseEntity<String> response = serviceCity.createCity(city1);
		String date = "2019-03-03";
		response = service.getSunrise(city1.getName(), date);

		JSONObject jObject  = new JSONObject(response.getBody());
		String sunrise=(String) jObject.get("sunrise");
		assertThat(sunrise).isEqualTo("3:12:55 AM");
		serviceCity.deleteCity(city1.getName());
	}
	
	@Test
	public void test_Both() throws Exception {
		City city1 = new City("New City1", 36.0f, 123.10f);
		City city2 = new City("New City2", 46.0f, 323.10f);
		City city3 = new City("New City3", 56.0f, 23.10f);
		serviceCity.createCity(city1);
		serviceCity.createCity(city2);
		serviceCity.createCity(city3);
		String date = "2019-03-03";
		
		List<String> list = new ArrayList<String>();
		list.add(city1.getName());
		list.add(city2.getName());
		list.add(city3.getName());
		ResponseEntity<String> responseList = service.getBoth(list, date);
		
		String json = responseList.getBody();
		Gson gson = new Gson();
		EventTimeResponse[] data = gson.fromJson(json, EventTimeResponse[].class);
		
		assertThat(data[0].getCity()).isEqualTo(city1.getName());
		assertThat(data[1].getCity()).isEqualTo(city2.getName());
		assertThat(data[2].getCity()).isEqualTo(city3.getName());
		
		serviceCity.deleteCity(city1.getName());
		serviceCity.deleteCity(city2.getName());
		serviceCity.deleteCity(city3.getName());
	}
}
