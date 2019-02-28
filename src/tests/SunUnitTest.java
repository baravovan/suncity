package tests;
import org.apache.log4j.Logger;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertFalse;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TestTransaction;

import com.google.gson.Gson;

import main.model.City;
import main.service.CitiesService;
import main.service.SunService;
import main.util.EventTimeResponse;

import org.json.*;

import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;

@RunWith(SpringJUnit4ClassRunner.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
public class SunUnitTest extends ApplicationTest{
	
	static Logger log = Logger.getLogger(SunUnitTest.class.getName());
	
	@Autowired
	SunService service;
	
	@Autowired
	CitiesService serviceCity;
	
	@Test
	public void test_getDates() throws Exception {
		Float longitude = -4.4203400f;
		Float lattitude = 36.7201600f;
		String date = "2019-03-03";
		ResponseEntity<String> response = service.getDates(longitude, lattitude, date);

		JSONObject obj = new JSONObject(response.getBody());
		String sunrise = obj.getJSONObject("results").getString("sunrise");
		String sunset = obj.getJSONObject("results").getString("sunset");
		
		assertThat(sunrise).isEqualTo("6:45:08 AM");
		assertThat(sunset).isEqualTo("6:13:57 PM");
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
		
		
		// changes to the database will be committed!
        TestTransaction.flagForCommit();
        TestTransaction.end();
        assertFalse(TestTransaction.isActive());
        TestTransaction.start();
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
		
		// changes to the database will be committed!
        TestTransaction.flagForCommit();
        TestTransaction.end();
        assertFalse(TestTransaction.isActive());
        TestTransaction.start();
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
		
		// changes to the database will be committed!
        TestTransaction.flagForCommit();
        TestTransaction.end();
        assertFalse(TestTransaction.isActive());
        TestTransaction.start();
	}
}
