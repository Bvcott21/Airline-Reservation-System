package com.bvcott.airlines.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
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
	void test_createAirline_ThrowsException_whenDuplicate() {
		Airline airline = new Airline("test-airline", "TA");
		
		when(airlineRepo.save(any(Airline.class))).thenThrow(DataIntegrityViolationException.class);
		
		assertThrows(AirlineAlreadyExistsException.class, () -> airlineService.createAirline(airline));
		
		verify(airlineRepo).save(airline);
		
		
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
	

}
