package com.bvcott.airlines.service;

import com.bvcott.airlines.model.Airport;
import com.bvcott.airlines.repository.AirportRepository;

public class AirportService {
    private AirportRepository airportRepo;

    AirportService(AirportRepository airportRepo) {
        this.airportRepo = airportRepo;
    }
    
    public Airport createAirport(Airport airport) {
        return airportRepo.save(airport);
    }

}
