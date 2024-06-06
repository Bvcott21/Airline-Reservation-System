package com.bvcott.airlines;

import java.time.LocalDateTime;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.bvcott.airlines.model.Airline;
import com.bvcott.airlines.model.Airport;
import com.bvcott.airlines.model.Flight;
import com.bvcott.airlines.repository.AirlineRepository;
import com.bvcott.airlines.repository.AirportRepository;
import com.bvcott.airlines.repository.FlightRepository;

@Configuration
public class DatabaseLoader {
	private static final Logger log = LoggerFactory.getLogger(DatabaseLoader.class);
	
	private final AirlineRepository airlineRepo;
	private final AirportRepository airportRepo;
	private final FlightRepository flightRepo;
	
	DatabaseLoader(AirlineRepository airlineRepo, AirportRepository airportRepo, FlightRepository flightRepo) {
		this.airlineRepo = airlineRepo;
		this.airportRepo = airportRepo;
		this.flightRepo = flightRepo;
	}
	
	@Bean CommandLineRunner initDatabase() {
		return args -> {
			log.info("DATABASE PRELOAD: Creating airlines...");
			Airline airline1 = new Airline("Iberia", "IB");
			Airline airline2 = new Airline("British Airways", "BA");
			
			log.info("DATABASE PRELOAD: Creating Airports...");
			Airport airport1 = new Airport("LHR", "London Heathrow", "United Kingdom");
			Airport airport2 = new Airport("LGW", "London Gatwick", "United Kingdom");
			
			log.info("DATABASE PRELOAD: Creating Flights...");
			Flight flight1 = new Flight("1223", LocalDateTime.now().plusDays(10), LocalDateTime.now().plusDays(10).plusHours(3), airport1, airport2, airline1);
			Flight flight2 = new Flight("4578", LocalDateTime.now().plusDays(5), LocalDateTime.now().plusDays(5).plusHours(2), airport2, airport1, airline2);
			
			log.info("DATABASE PRELOAD: Persisting Airlines...");
			airline1 = airlineRepo.save(airline1);
			airline2 = airlineRepo.save(airline2);
			
			log.info("DATABASE PRELOAD: Persisting Airports...");
			airport1 = airportRepo.save(airport1);
			airport2 = airportRepo.save(airport2);
			
			log.info("DATABASE PRELOAD: Persisting Flights...");
			flight1 = flightRepo.save(flight1);
			flight2 = flightRepo.save(flight2);
			
			log.info("DATABASE PRELOAD: updating relationships...");
			
			log.info("DATABASE PRELOAD: Associating flights with airports...");
			airport1.addDeparture(flight1);
			airport2.addArrival(flight1);
			
			airport2.addDeparture(flight2);
			airport1.addArrival(flight2);
			
			log.info("DATABASE PRELOAD: Associating flights with airlines...");
			airline1.addFlight(flight1);
			airline2.addFlight(flight2);
			
			
			log.info("DATABASE PRELOAD: Associating flights with airports...");
			airport1.addDeparture(flight1);
			airport2.addArrival(flight1);
			
			airport2.addDeparture(flight2);
			airport1.addArrival(flight2);
			
			log.info("DATABASE PRELOAD: Updating Airlines...");
			airline1 = airlineRepo.save(airline1);
			airline2 = airlineRepo.save(airline2);
			
			log.info("DATABASE PRELOAD: Updating Airports...");
			airport1 = airportRepo.save(airport1);
			airport2 = airportRepo.save(airport2);
			
			log.info("DATABASE PRELOAD: Updating Flights...");
			flight1 = flightRepo.save(flight1);
			flight2 = flightRepo.save(flight2);
			
			log.info("Persisted Airline {}", airline1);
			log.info("Persisted Airline {}", airline2);
			log.info("Persisted airport {}", airport1);
			log.info("Persisted airport {}", airport2);	
			log.info("Persisted Flights {}", flight1);
			log.info("Persisted Flights {}", flight2);
		};
	}
	
}
