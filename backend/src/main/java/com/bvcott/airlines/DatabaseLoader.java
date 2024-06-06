package com.bvcott.airlines;

import java.time.LocalDateTime;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.bvcott.airlines.model.Airline;
import com.bvcott.airlines.model.Airport;
import com.bvcott.airlines.model.Booking;
import com.bvcott.airlines.model.Flight;
import com.bvcott.airlines.model.Passenger;
import com.bvcott.airlines.repository.AirlineRepository;
import com.bvcott.airlines.repository.AirportRepository;
import com.bvcott.airlines.repository.BookingRepository;
import com.bvcott.airlines.repository.FlightRepository;
import com.bvcott.airlines.repository.PassengerRepository;

@Configuration
public class DatabaseLoader {
	private static final Logger log = LoggerFactory.getLogger(DatabaseLoader.class);
	
	private final AirlineRepository airlineRepo;
	private final AirportRepository airportRepo;
	private final FlightRepository flightRepo;
	private final PassengerRepository passengerRepo;
	private final BookingRepository bookingRepo;
	
	DatabaseLoader(AirlineRepository airlineRepo, AirportRepository airportRepo, 
			FlightRepository flightRepo, PassengerRepository passengerRepo,
			BookingRepository bookingRepo) {
		this.airlineRepo = airlineRepo;
		this.airportRepo = airportRepo;
		this.flightRepo = flightRepo;
		this.passengerRepo = passengerRepo;
		this.bookingRepo = bookingRepo;
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
			
			log.info("DATABASE PRELOAD: Persisted Airline {}", airline1);
			log.info("DATABASE PRELOAD: Persisted Airline {}", airline2);
			log.info("DATABASE PRELOAD: Persisted airport {}", airport1);
			log.info("DATABASE PRELOAD: Persisted airport {}", airport2);	
			log.info("DATABASE PRELOAD: Persisted Flights {}", flight1);
			log.info("DATABASE PRELOAD: Persisted Flights {}", flight2);
			
			log.info("DATABASE PRELOAD: Creating Bookings...");
			Booking booking1 = new Booking(flight1, "ON TIME");
			Booking booking2 = new Booking(flight2, "DELAYED");
			
			log.info("DATABASE PRELOAD: Creating Passengers...");
			Passenger passenger1 = new Passenger("Edgar Afonso", "PASS123");
			Passenger passenger2 = new Passenger("John Doe", "PASS456");
			
			log.info("DATABASE PRELOAD: Associating passengers with bookings");
			passenger1.addBooking(booking1);
			passenger2.addBooking(booking2);
			
			log.info("DATABASE PRELOAD: Persisting Passengers & associated Bookings...");
			passenger1 = passengerRepo.save(passenger1);
			passenger2 = passengerRepo.save(passenger2);
			booking1 = passenger1.getBookings().get(0);
			booking2 = passenger2.getBookings().get(0);
			
			log.info("DATABASE PRELOAD: Persisted Passenger {}", passenger1);
			log.info("DATABASE PRELOAD: Persisted Passenger {}", passenger2);
			log.info("DATABASE PRELOAD: Persisted Booking {}", booking1);
			log.info("DATABASE PRELOAD: Persisted Booking {}", booking2);
		};
	}
	
}
