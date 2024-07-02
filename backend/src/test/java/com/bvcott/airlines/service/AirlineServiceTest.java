package com.bvcott.airlines.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.dao.DataIntegrityViolationException;

import com.bvcott.airlines.exception.AirlineAlreadyExistsException;
import com.bvcott.airlines.exception.AirlineNotFoundException;
import com.bvcott.airlines.model.Airline;
import com.bvcott.airlines.repository.AirlineRepository;


class AirlineServiceTest {
	@Mock private AirlineRepository airlineRepo;
	@InjectMocks private AirlineService airlineService;

	@BeforeEach
	void setUp() throws Exception {
		MockitoAnnotations.openMocks(this);
	}

	@Test
	void test_createAirline_calls_airlineRepository_saveMethodPassingSameObject() {
		Airline airline = new Airline("test-airline", "TA");
		
		when(airlineRepo.save(airline)).thenReturn(airline);
		Airline createdAirline = airlineService.createAirline(airline);
		
		verify(airlineRepo).save(airline);
		
		assertEquals(airline, createdAirline);
	}

	@Test
	void test_createAirline_calls_airlineRepository_findByName_whenCalled() {
		Airline airline =  new Airline("test-airline", "TA");

		airlineService.createAirline(airline);

		verify(airlineRepo).findByName(airline.getName());
	}

	@Test
	void test_createAirline_throwsDataIntegrityViolationException_ifNameIsEmpty() {
		Airline airline =  new Airline("", "TA");

		assertThrows(DataIntegrityViolationException.class, () -> airlineService.createAirline(airline));
	}

	@Test
	void test_createAirline_throwsDataIntegrityViolationException_ifCodeIsEmpty() {
		Airline airline =  new Airline("test-airline", "");

		assertThrows(DataIntegrityViolationException.class, () -> airlineService.createAirline(airline));
	}

	@Test
	void test_createAirline_throwsAirlineAlreadyExistsException_whenNameAlreadyExists() {
		Airline airline = new Airline("test-airline", "TA");
		when(airlineRepo.findByName(airline.getName())).thenReturn(Optional.of(airline));

		assertThrows(AirlineAlreadyExistsException.class, () -> airlineService.createAirline(airline));
	}
	
	@Test
	void test_createAirline_calls_airlineRepository_findByCode_whenCalled() {
		Airline airline = new Airline("test-airline", "TA");

		airlineService.createAirline(airline);

		verify(airlineRepo).findByCode(airline.getCode());
	}

	@Test
	void test_createAirline_throwsAirlineAlreadyExistsException_whenCodeAlreadyExists() {
		Airline airline = new Airline("test-airline", "TA");
		when(airlineRepo.findByCode(airline.getCode())).thenReturn(Optional.of(airline));
		assertThrows(AirlineAlreadyExistsException.class, () -> airlineService.createAirline(airline));


	}
	
	@Test
	void retrieveByName() {
		Airline airline = new Airline("test-airline", "TA");
		when(airlineRepo.findByName(airline.getName())).thenReturn(Optional.of(airline));
		Optional<Airline> foundAirline = airlineRepo.findByName(airline.getName());
		
		assertTrue(foundAirline.isPresent());
		assertEquals("test-airline", foundAirline.get().getName());
		verify(airlineRepo).findByName(airline.getName());
		
	}
	
	@Test
	void test_retrieveAll_calls_airlineRepository_findAllMethod() {
		airlineService.retrieveAll();
		
		verify(airlineRepo).findAll();
	}
	
	@Test
	void test_retrieveById_calls_airlineRepository_findByIdMethod() {
		Airline airline = new Airline("test-airline", "TA");
		UUID randomId = UUID.randomUUID();
		when(airlineRepo.findById(randomId)).thenReturn(Optional.of(airline));
		airlineService.retrieveById(randomId);
		verify(airlineRepo).findById(randomId);
	}
	
	@Test
	void test_retrieveById_throwsAirlineNotFoundException_whenNotFound() {
		UUID airlineId = UUID.randomUUID();
		when(airlineRepo.findById(airlineId)).thenReturn(Optional.empty());
		assertThrows(AirlineNotFoundException.class, () -> airlineService.retrieveById(airlineId));
	}
	
	@Test
	void test_deleteById_callsAirlineRepository_findById_whenCalled() {
		UUID id = UUID.randomUUID();
		Airline airline = new Airline("Test-airline", "TA");
		when(airlineRepo.findById(id)).thenReturn(Optional.of(airline));
		airlineService.deleteById(id);
		verify(airlineRepo).findById(id);
	}

	@Test
	void test_deleteById_throwsAirlineNotFoundException_whenNotFound() {
		UUID airlineId = UUID.randomUUID();
		when(airlineRepo.findById(airlineId)).thenReturn(Optional.empty());
		assertThrows(AirlineNotFoundException.class, () -> airlineService.deleteById(airlineId));
	}
	
	

	@Test
	void test_deleteById_calls_airlineRepository_deleteByIdMethod() {
		Airline airline = new Airline("test-airline", "TA");
		UUID randomId = UUID.randomUUID();
		when(airlineRepo.findById(randomId)).thenReturn(Optional.of(airline));
		airlineService.deleteById(randomId);
		verify(airlineRepo).deleteById(randomId);
	}
	
	@Test
	void test_updateAirline_callsAirlineRepository_findbyId_whenCalled() {
		Airline airline = new Airline("test-airline", "TA");
		UUID id = UUID.randomUUID();
		when(airlineRepo.findById(id)).thenReturn(Optional.of(airline));
		airlineService.updateAirline(id, airline);
		verify(airlineRepo).findById(id);
	}

	@Test
	void test_updateAirline_throwsAirlineNotFoundException_whenAirlineNotfound() {
		UUID id = UUID.randomUUID();
		Airline airline = new Airline("test-airline","TA");
		when(airlineRepo.findById(id)).thenReturn(Optional.empty());

		assertThrows(AirlineNotFoundException.class, () -> airlineService.updateAirline(id, airline));
	}

	@Test
	void test_updateAirline_callsAirlineRepository_findbyName_whenCalled() {
		Airline airline = new Airline("test-airline", "TA");
		UUID id = UUID.randomUUID();
		when(airlineRepo.findById(id)).thenReturn(Optional.of(airline));
		airlineService.updateAirline(id, airline);
		verify(airlineRepo).findByName(airline.getName());
	}

	@Test
	void test_updateAirline_callsAirlineRepository_findbyCode_whenCalled() {
		Airline airline = new Airline("test-airline", "TA");
		UUID id = UUID.randomUUID();
		when(airlineRepo.findById(id)).thenReturn(Optional.of(airline));
		airlineService.updateAirline(id, airline);
		verify(airlineRepo).findByCode(airline.getCode());
	}

	@Test
	void test_updateAirline_throwsAirlineAlreadyExistsException_whenNameAlreadyExists() {
		UUID id = UUID.randomUUID();
		Airline airline = new Airline("test-airline","TA");

		when(airlineRepo.findById(id)).thenReturn(Optional.of(airline));
		when(airlineRepo.findByName(airline.getName())).thenReturn(Optional.of(airline));

		assertThrows(AirlineAlreadyExistsException.class, () -> airlineService.updateAirline(id, airline));
	}

	@Test
	void test_updateAirline_throwsAirlineAlreadyExistsException_whenCodeAlreadyExists() {
		UUID id = UUID.randomUUID();
		Airline airline = new Airline("test-airline","TA");

		when(airlineRepo.findById(id)).thenReturn(Optional.of(airline));
		when(airlineRepo.findByCode(airline.getCode())).thenReturn(Optional.of(airline));
		
		assertThrows(AirlineAlreadyExistsException.class, () -> airlineService.updateAirline(id, airline));
	}

	@Test
	void test_updateAirline_succesfullyUdpates_whenAirlineFoundAndValuesAreUnique() {
		UUID id = UUID.randomUUID();
		Airline existingAirline = new Airline("test-airline","TA");
		Airline updatedAirline = new Airline("new-airline", "NA");

		when(airlineRepo.findById(id)).thenReturn(Optional.of(existingAirline));
		when(airlineRepo.findByCode(existingAirline.getCode())).thenReturn(Optional.empty());
		when(airlineRepo.findByCode(existingAirline.getName())).thenReturn(Optional.empty());
		when(airlineRepo.save(existingAirline)).thenReturn(updatedAirline);

		Airline result = airlineService.updateAirline(id, updatedAirline);

		assertEquals(updatedAirline.getName(), result.getName());
		assertEquals(updatedAirline.getCode(), result.getCode());
		verify(airlineRepo).save(existingAirline);
	}

}
