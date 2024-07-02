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
		log.debug("createAirline method called, object received: {}", airline);

		if(airline.getName().isEmpty() || airline.getCode().isEmpty()) {
			log.warn("Airline name or code is/are empty.");
			throw new DataIntegrityViolationException("Fields cannot be empty.");
		}
	
		Optional<Airline> nameMatcher = airlineRepo.findByName(airline.getName());
		Optional<Airline> codeMatcher = airlineRepo.findByCode(airline.getCode());

		if(nameMatcher.isPresent()) {
			log.warn("Airline already exists by name: " + airline.getName());
			throw new AirlineAlreadyExistsException("Airline already exists with name: " + airline.getName());
		} else if(codeMatcher.isPresent()) {
			log.warn("Airline already exists by code: " + airline.getCode());
			throw new AirlineAlreadyExistsException("Airline already exists with code: " + airline.getCode());
		} else {
			return airlineRepo.save(airline);
		}
		
	}

	public List<Airline> retrieveAll() {
		log.debug("retrieveAll method called.");
		return airlineRepo.findAll();
	}

	public Airline retrieveById(UUID id) {
		log.debug("retrieveById method called, ID received: {}", id);
		return airlineRepo
				.findById(id)
				.orElseThrow(() -> new AirlineNotFoundException("Airline not found with ID: " + id));
	}

	public void deleteById(UUID id) {
		log.debug("deleteById method called, ID received: {}", id);
		Optional<Airline> airlineFound = airlineRepo.findById(id);

		if(airlineFound.isPresent()) {
			airlineRepo.deleteById(id);
		} else {
			log.warn("Airline with ID: {} - NOT FOUND", id);
			throw new AirlineNotFoundException("Couldn't delete airline with ID: " + id + " - NOT FOUND");
		}
	}	

	public Airline updateAirline(UUID id, Airline airline) {
		log.debug("updateAirline method called, object received: {}", airline);

		Optional<Airline> existingAirlineOpt = airlineRepo.findById(id);

		if(!existingAirlineOpt.isPresent()) {
			log.warn("Airline with ID: {} - NOT FOUND", id);
			throw new AirlineNotFoundException("Airline with ID:" + id + "not found.");
		}

		Airline existingAirline = existingAirlineOpt.get();

		Optional<Airline> nameMatcher = airlineRepo.findByName(airline.getName());
		Optional<Airline> codeMatcher = airlineRepo.findByCode(airline.getCode());

		if(nameMatcher.isPresent()) {
			log.warn("Airline with name {} already exists.", airline.getName());
			throw new AirlineAlreadyExistsException("Airline with name: " + airline.getName() + " already exists.");
		} 

		if(codeMatcher.isPresent()) {
			log.warn("Airline with code {} already exists.", airline.getCode());
			throw new AirlineAlreadyExistsException("Airline with code: " + airline.getCode() + "already exsits.");
		}

		existingAirline.setName(airline.getName());
		existingAirline.setCode(airline.getCode());

		Airline savedAirline = airlineRepo.save(existingAirline);

		log.debug("Airline udpated successfully, updated object: {}", savedAirline);
		return savedAirline;
		
	}

}
