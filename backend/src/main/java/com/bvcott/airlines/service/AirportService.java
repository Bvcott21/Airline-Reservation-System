package com.bvcott.airlines.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;

import com.bvcott.airlines.exception.AirportAlreadyExistsException;
import com.bvcott.airlines.exception.AirportNotFoundException;
import com.bvcott.airlines.model.Airport;
import com.bvcott.airlines.repository.AirportRepository;

public class AirportService {
    private static final Logger log = LoggerFactory.getLogger(AirportService.class);
    private AirportRepository airportRepo;

    AirportService(AirportRepository airportRepo) {
        this.airportRepo = airportRepo;
    }
    
    public Airport createAirport(Airport airport) {
        log.debug("createAirport method called, object received: {}", airport);

        if(airport.getName().isEmpty() || airport.getCode().isEmpty() || airport.getLocation().isEmpty()) {
            log.warn("one of the following fields is empty: Name, Code, Location");
            throw new DataIntegrityViolationException("Fields cannot be empty");
        }

        Optional<Airport> nameMatcher = airportRepo.findByName(airport.getName());
        Optional<Airport> codeMatcher = airportRepo.findByCode(airport.getCode());

        if(nameMatcher.isPresent()) {
            log.warn("Airport already exists with name: " + airport.getName());
            throw new AirportAlreadyExistsException("Airport already exists with name: " + airport.getName());
        } else if(codeMatcher.isPresent()) {
            log.warn("Airport already exists with code: " + airport.getCode());
            throw new AirportAlreadyExistsException("Airport already exists with code: " + airport.getCode());
        } else {
            return airportRepo.save(airport);
        }
    }

    public List<Airport> retrieveAll() {
        log.debug("retrieveAll method called.");
        return airportRepo.findAll();
    }

    public Airport retrieveById(UUID id) {
        log.debug("retrieveById method called with ID: " + id);
        return airportRepo
            .findById(id)
            .orElseThrow(() -> new AirportNotFoundException("Airport not found with ID: " + id));
    }

    public void deleteById(UUID id) {
        log.debug("deleteById method called, ID received: {}", id);
        Optional<Airport> airportFound = airportRepo.findById(id);

        if(airportFound.isPresent()) {
            airportRepo.deleteById(id);
        } else {
            log.warn("Airport with ID: {} - NOT FOUND", id);
            throw new AirportNotFoundException("Can't delete, airport not found with ID: " + id);
        }
    }

    public Airport updateAirport(UUID id, Airport airport) {
        log.debug("updateAirport method called with ID: {}, and object: {}", id, airport);
        
        if(airport.getName().isEmpty() || airport.getCode().isEmpty() || airport.getLocation().isEmpty()) {
            log.warn("One or more of the following fields are empty: name, code, location");
            throw new DataIntegrityViolationException("Can't update, one or more of the following fields are empty: name, code, location.");
        }

        Optional<Airport> airportFound = airportRepo.findById(id);

        if(!airportFound.isPresent()) {
            log.warn("Airport not found with ID: {}", id);
            throw new AirportNotFoundException("Airport not found with ID: " + id);
        }

        Airport existingAirport = airportFound.get();

        Optional<Airport> nameMatcher = airportRepo.findByName(airport.getName());
        Optional<Airport> codeMatcher = airportRepo.findByCode(airport.getCode());

        if(nameMatcher.isPresent()) {
            log.warn("Airport with name: {} already exists", airport.getName());
            throw new AirportAlreadyExistsException("Airport already exists with name: " + airport.getName());
        }

        if(codeMatcher.isPresent()) {
            log.warn("Airport with code: {} already exists", airport.getCode());
            throw new AirportAlreadyExistsException("Airport already exists with code: " + airport.getCode());
        }

        existingAirport.setName(airport.getName());
        existingAirport.setCode(airport.getCode());
        existingAirport.setLocation(airport.getLocation());

        Airport updatedAirport = airportRepo.save(existingAirport);
        
        log.debug("Airport updated successfully, updated object: {}", updatedAirport);
        return updatedAirport;
    }
}
