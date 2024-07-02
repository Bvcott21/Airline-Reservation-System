package com.bvcott.airlines.service;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;

import com.bvcott.airlines.exception.AirportAlreadyExistsException;
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

}
