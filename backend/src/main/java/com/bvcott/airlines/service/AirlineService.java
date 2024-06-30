package com.bvcott.airlines.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import com.bvcott.airlines.exception.AirlineAlreadyExistsException;
import com.bvcott.airlines.exception.AirlineNotFoundException;
import com.bvcott.airlines.model.Airline;
import com.bvcott.airlines.repository.AirlineRepository;

@Service
public class AirlineService {
	private static final Logger log = LoggerFactory.getLogger(AirlineService.class);
	private AirlineRepository airlineRepo;
	
	AirlineService(AirlineRepository airlineRepo) {
		this.airlineRepo = airlineRepo;
	}
	
	public Airline createAirline(Airline airline) {
		log.debug("AirlineService: createAirline Method called, object received: {}", airline);
		log.debug("AirlineService: Called Airline Repository's save method passing in: {}", airline);
	
		try {
			return airlineRepo.save(airline);
		} catch (DataIntegrityViolationException e) {
			throw new AirlineAlreadyExistsException("Airline already exists with name: " + airline.getName());
		}
	}

	public List<Airline> retrieveAll() {
		log.debug("AirlineService: retrieveAll method called.");
		log.debug("AirlineService: called Airline Repository's findAll method.");
		return airlineRepo.findAll();
	}

	public Airline retrieveById(UUID id) {
		log.debug("AirlineService: retrieveById method called, ID received: {}", id);
		return airlineRepo
				.findById(id)
				.orElseThrow(() -> new AirlineNotFoundException("Airline not found with ID: " + id));
	}

	public void deleteById(UUID id) {
		log.debug("AirlineService: deleteById method called, ID received: {}", id);
		this.retrieveById(id);
		log.debug("AirlineService: Airline found.");
		log.debug("AirlineService: called Airline Repository's deleteById method.");
		airlineRepo.deleteById(id);
		
	}	

}
