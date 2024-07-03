package com.bvcott.airlines.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;

import com.bvcott.airlines.exception.AirlineNotFoundException;
import com.bvcott.airlines.exception.AirportNotFoundException;
import com.bvcott.airlines.model.Airline;
import com.bvcott.airlines.model.Airport;
import com.bvcott.airlines.model.Flight;
import com.bvcott.airlines.repository.AirlineRepository;
import com.bvcott.airlines.repository.AirportRepository;
import com.bvcott.airlines.repository.FlightRepository;

public class FlightService {
    private static final Logger log = LoggerFactory.getLogger(FlightService.class);

    private FlightRepository flightRepo;
    private AirlineRepository airlineRepo;
    private AirportRepository airportRepo;

    FlightService(FlightRepository flightRepo, AirlineRepository airlineRepo, AirportRepository airportRepo) {
        this.flightRepo = flightRepo;
        this.airlineRepo = airlineRepo;
        this.airportRepo = airportRepo;
    }

    public Flight createFlight(Flight flight) {
        log.debug("createFlight method called with object: {}", flight);
        
        if(flight.getFlightNumber().isEmpty()) {
            log.warn("Can't persist, flight number is empty.");
            throw new DataIntegrityViolationException("Flight number can't be empty");
        }

        if(flight.getDepartureTime() == null) {
            log.warn("Can't persist, departure time is null");
            throw new DataIntegrityViolationException("All flights need a departure time.");
        }

        if(flight.getArrivalTime() == null) {
            log.warn("Can't persist, arrival time is null");
            throw new DataIntegrityViolationException("All flights need an arrival time.");
        }

        if(flight.getArrivalTime().isBefore(flight.getDepartureTime())) {
            log.warn("Can't persist, arrival time is before departure time.");
            throw new DataIntegrityViolationException("Flights can't have an arrival time which is earlier than the departure time.");
        }

        Airport origin = airportRepo
            .findById(flight.getOrigin().getId())
            .orElseThrow(() -> new AirportNotFoundException("Origin Airport not found."));

        Airport destination = airportRepo
            .findById(flight.getDestination().getId())
            .orElseThrow(() -> new AirportNotFoundException("Destination Airport not found."));

        Airline airline = airlineRepo
            .findById(flight.getAirline().getId())
            .orElseThrow(() -> new AirlineNotFoundException("Airline not found."));

        log.debug("Setting persisted entities in flight");
        flight.setOrigin(origin);
        flight.setDestination(destination);
        flight.setAirline(airline);

        Flight persistedFlight = flightRepo.save(flight);

        log.info("Flight persisted correctly");

        return persistedFlight;
    }
}
