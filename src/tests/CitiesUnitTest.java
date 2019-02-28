package tests;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
//import org.springframework.test.context.transaction.TestTransaction;

import main.exception.ThereIsNoSuchCityException;
import main.model.City;
import main.service.CitiesService;

import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;

@RunWith(SpringJUnit4ClassRunner.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class CitiesUnitTest extends ApplicationTest{
	
	@Autowired
	CitiesService service;

	@Test
	public void test_create() throws Exception {
		City city = new City("New City", 36.0f, 123.10f);
		ResponseEntity<String> response = service.createCity(city);
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
		
		ResponseEntity<City> responseGet = service.getCity(city.getName());
		assertThat(responseGet.getStatusCode()).isEqualTo(HttpStatus.OK);
		City get = (City) responseGet.getBody();
		
		assertThat(get.getName()).isEqualTo(city.getName());
		assertThat(get.getLongitude()).isEqualTo(city.getLongitude());
		assertThat(get.getLattitude()).isEqualTo(city.getLattitude());
		
		service.deleteCity(city.getName());
		
		/*
		// changes to the database will be committed!
        TestTransaction.flagForCommit();
        TestTransaction.end();
        assertFalse(TestTransaction.isActive());
        TestTransaction.start();
		*/
		try {
			responseGet = service.getCity(city.getName());
		}catch(Exception ex) {
			assertThat(ex.getClass()).isEqualTo(ThereIsNoSuchCityException.class);
		}
	}
	
	@Test
	public void test_list() throws Exception {
		ResponseEntity<List<City>> responceList = service.getCities();
		assertThat(responceList.getStatusCode()).isEqualTo(HttpStatus.OK);
		List<City> getList = (List<City>) responceList.getBody();
		Integer lengthBefore = getList.size();
		
		City city1 = new City("New City1", 36.0f, 123.10f);
		ResponseEntity<String> response = service.createCity(city1);
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
		City city2 = new City("New City2", 46.0f, 323.10f);
		response = service.createCity(city2);
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
		City city3 = new City("New City3", 56.0f, 23.10f);
		response = service.createCity(city3);
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
		
		responceList = service.getCities();
		assertThat(responceList.getStatusCode()).isEqualTo(HttpStatus.OK);
		getList = (List<City>) responceList.getBody();
		Integer lengthAfter = getList.size();
		
		assertThat(lengthAfter).isEqualTo(lengthBefore + 3);
		
		service.deleteCity(city1.getName());
		service.deleteCity(city2.getName());
		service.deleteCity(city3.getName());
		
		responceList = service.getCities();
		assertThat(responceList.getStatusCode()).isEqualTo(HttpStatus.OK);
		getList = (List<City>) responceList.getBody();
		lengthAfter = getList.size();
		
		assertThat(lengthAfter).isEqualTo(lengthBefore);
	}
}

