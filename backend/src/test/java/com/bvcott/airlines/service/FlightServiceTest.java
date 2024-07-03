package com.bvcott.airlines.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.dao.DataIntegrityViolationException;

import com.bvcott.airlines.exception.AirlineNotFoundException;
import com.bvcott.airlines.exception.AirportNotFoundException;
import com.bvcott.airlines.model.Airline;
import com.bvcott.airlines.model.Airport;
import com.bvcott.airlines.model.Flight;
import com.bvcott.airlines.repository.AirlineRepository;
import com.bvcott.airlines.repository.AirportRepository;
import com.bvcott.airlines.repository.FlightRepository;

public class FlightServiceTest {
    @Mock private FlightRepository flightRepo;
    @Mock private AirlineRepository airlineRepo;
    @Mock private AirportRepository airportRepo;
    @InjectMocks private FlightService flightService;

    @BeforeEach
    void setUp() throws Exception {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void test_createFlight_throwsDataIntegrityViolationException_ifFlightNumberIsEmpty() {
        LocalDateTime departure = LocalDateTime.of(2024, 10, 10, 6, 30);
        LocalDateTime arrival = LocalDateTime.of(2024, 10, 10, 9, 45);
        Airport origin = new Airport("LUT", "Luton", "UK");
        Airport destination = new Airport("LGW", "Gatwick", "UK");
        Airline airline = new Airline("Test-airline", "TA");
        Flight flight = new Flight("", departure, arrival, origin, destination, airline);

        assertThrows(DataIntegrityViolationException.class, () -> flightService.createFlight(flight));
    }

    @Test
    void test_createFlight_throwsDataIntegrityViolationException_ifDepartureTimeIsNull() {
        LocalDateTime arrival = LocalDateTime.of(2024, 10, 10, 9, 45);
        Airport origin = new Airport("LUT", "Luton", "UK");
        Airport destination = new Airport("LGW", "Gatwick", "UK");
        Airline airline = new Airline("Test-airline", "TA");
        Flight flight = new Flight("MH370", null, arrival, origin, destination, airline);

        assertThrows(DataIntegrityViolationException.class, () -> flightService.createFlight(flight));
    }

    @Test
    void test_createFlight_throwsDataIntegrityViolationException_ifArrivalTimeIsEmpty() {
        LocalDateTime departure = LocalDateTime.of(2024, 10, 10, 6, 30);
        Airport origin = new Airport("LUT", "Luton", "UK");
        Airport destination = new Airport("LGW", "Gatwick", "UK");
        Airline airline = new Airline("Test-airline", "TA");
        Flight flight = new Flight("MH370", departure, null, origin, destination, airline);

        assertThrows(DataIntegrityViolationException.class, () -> flightService.createFlight(flight));
    }

    @Test
    void test_createFlight_throwsDataIntegrityViolationException_ifArrivalIsBeforeThanDeparture() {
        LocalDateTime departure = LocalDateTime.of(2024, 10, 10, 9, 30);
        LocalDateTime arrival = LocalDateTime.of(2024, 10, 10, 4, 45);
        Airport origin = new Airport("LUT", "Luton", "UK");
        Airport destination = new Airport("LGW", "Gatwick", "UK");
        Airline airline = new Airline("Test-airline", "TA");
        Flight flight = new Flight("MH370", departure, arrival, origin, destination, airline);

        assertThrows(DataIntegrityViolationException.class, () -> flightService.createFlight(flight));
    }

    @Test
    void test_createFlight_callsAirportRepo_findById_forOrigin() {
        LocalDateTime departure = LocalDateTime.of(2024, 10, 10, 6, 30);
        LocalDateTime arrival = LocalDateTime.of(2024, 10, 10, 9, 45);
        
        Airport origin = new Airport("LUT", "Luton", "UK");
        UUID originId = UUID.randomUUID();
        origin.setId(originId);
        
        Airport destination = new Airport("LGW", "Gatwick", "UK");
        UUID destinationId = UUID.randomUUID();
        destination.setId(destinationId);

        Airline airline = new Airline("Test-airline", "TA");
        Flight flight = new Flight("MH370", departure, arrival, origin, destination, airline);

        when(airportRepo.findById(origin.getId())).thenReturn(Optional.of(origin));
        when(airportRepo.findById(destination.getId())).thenReturn(Optional.of(destination));
        when(airlineRepo.findById(flight.getId())).thenReturn(Optional.of(airline));

        flightService.createFlight(flight);

        verify(airportRepo).findById(origin.getId());
    }

    @Test
    void test_createFlight_callsAirportRepo_findById_forDestination() {
        LocalDateTime departure = LocalDateTime.of(2024, 10, 10, 6, 30);
        LocalDateTime arrival = LocalDateTime.of(2024, 10, 10, 9, 45);
        Airport origin = new Airport("LUT", "Luton", "UK");
        UUID originId = UUID.randomUUID();
        origin.setId(originId);
        
        Airport destination = new Airport("LGW", "Gatwick", "UK");
        UUID destinationId = UUID.randomUUID();
        destination.setId(destinationId);

        Airline airline = new Airline("Test-airline", "TA");
        Flight flight = new Flight("MH370", departure, arrival, origin, destination, airline);

        when(airportRepo.findById(origin.getId())).thenReturn(Optional.of(origin));
        when(airportRepo.findById(destination.getId())).thenReturn(Optional.of(destination));
        when(airlineRepo.findById(flight.getId())).thenReturn(Optional.of(airline));

        flightService.createFlight(flight);

        verify(airportRepo).findById(destination.getId());
    }

    @Test
    void test_createFlight_throwsAirportNotFoundException_ifNoAirportFound_forOrigin() {
        LocalDateTime departure = LocalDateTime.of(2024, 10, 10, 6, 30);
        LocalDateTime arrival = LocalDateTime.of(2024, 10, 10, 9, 45);
        
        Airport origin = new Airport("LUT", "Luton", "UK");
        
        Airport destination = new Airport("LGW", "Gatwick", "UK");
        UUID destinationId = UUID.randomUUID();
        destination.setId(destinationId);

        Airline airline = new Airline("Test-airline", "TA");
        Flight flight = new Flight("MH370", departure, arrival, origin, destination, airline);

        when(airportRepo.findById(origin.getId())).thenReturn(Optional.empty());
        when(airportRepo.findById(destination.getId())).thenReturn(Optional.of(destination));

        assertThrows(AirportNotFoundException.class, () -> flightService.createFlight(flight));
    }

    @Test
    void test_createFlight_throwsAirportNotFoundException_ifNoAirportFound_forDestination() {
        LocalDateTime departure = LocalDateTime.of(2024, 10, 10, 6, 30);
        LocalDateTime arrival = LocalDateTime.of(2024, 10, 10, 9, 45);
        Airport origin = new Airport("LUT", "Luton", "UK");
        UUID originId = UUID.randomUUID();
        origin.setId(originId);
        
        Airport destination = new Airport("LGW", "Gatwick", "UK");

        Airline airline = new Airline("Test-airline", "TA");
        Flight flight = new Flight("MH370", departure, arrival, origin, destination, airline);

        when(airportRepo.findById(origin.getId())).thenReturn(Optional.of(origin));
        when(airportRepo.findById(destination.getId())).thenReturn(Optional.empty());

        assertThrows(AirportNotFoundException.class, () -> flightService.createFlight(flight));
    }

    @Test
    void test_createFlight_callsAirlineRepo_findById() {
        LocalDateTime departure = LocalDateTime.of(2024, 10, 10, 6, 30);
        LocalDateTime arrival = LocalDateTime.of(2024, 10, 10, 9, 45);
        Airport origin = new Airport("LUT", "Luton", "UK");
        UUID originId = UUID.randomUUID();
        origin.setId(originId);
        
        Airport destination = new Airport("LGW", "Gatwick", "UK");
        UUID destinationId = UUID.randomUUID();
        destination.setId(destinationId);

        Airline airline = new Airline("Test-airline", "TA");
        Flight flight = new Flight("MH370", departure, arrival, origin, destination, airline);

        when(airportRepo.findById(origin.getId())).thenReturn(Optional.of(origin));
        when(airportRepo.findById(destination.getId())).thenReturn(Optional.of(destination));
        when(airlineRepo.findById(flight.getId())).thenReturn(Optional.of(airline));

        flightService.createFlight(flight);

        verify(airlineRepo).findById(airline.getId());
    }

    @Test
    void test_createFlight_throwsAirportNotFound_ifAirportNotFound() {
        LocalDateTime departure = LocalDateTime.of(2024, 10, 10, 6, 30);
        LocalDateTime arrival = LocalDateTime.of(2024, 10, 10, 9, 45);
        Airport origin = new Airport("LUT", "Luton", "UK");
        UUID originId = UUID.randomUUID();
        origin.setId(originId);
        
        Airport destination = new Airport("LGW", "Gatwick", "UK");
        UUID destinationId = UUID.randomUUID();
        destination.setId(destinationId);

        Airline airline = new Airline("Test-airline", "TA");
        Flight flight = new Flight("MH370", departure, arrival, origin, destination, airline);

        when(airportRepo.findById(origin.getId())).thenReturn(Optional.of(origin));
        when(airportRepo.findById(destination.getId())).thenReturn(Optional.of(destination));
        when(airlineRepo.findById(flight.getId())).thenReturn(Optional.empty());

        assertThrows(AirlineNotFoundException.class, () -> flightService.createFlight(flight));
    }

    @Test
    void test_createFlight_persistedCorrectly() {
        LocalDateTime departure = LocalDateTime.of(2024, 10, 10, 6, 30);
        LocalDateTime arrival = LocalDateTime.of(2024, 10, 10, 9, 45);
        Airport origin = new Airport("LUT", "Luton", "UK");
        UUID originId = UUID.randomUUID();
        origin.setId(originId);
        
        Airport destination = new Airport("LGW", "Gatwick", "UK");
        UUID destinationId = UUID.randomUUID();
        destination.setId(destinationId);

        Airline airline = new Airline("Test-airline", "TA");
        Flight flight = new Flight("MH370", departure, arrival, origin, destination, airline);

        when(airportRepo.findById(origin.getId())).thenReturn(Optional.of(origin));
        when(airportRepo.findById(destination.getId())).thenReturn(Optional.of(destination));
        when(airlineRepo.findById(flight.getId())).thenReturn(Optional.of(airline));

        when(flightRepo.save(flight)).thenReturn(flight);
        Flight persistedFlight = flightService.createFlight(flight);
        
        assertEquals(flight.getDepartureTime(), persistedFlight.getDepartureTime());
        assertEquals(flight.getArrivalTime(), persistedFlight.getArrivalTime());
        assertEquals(flight.getOrigin(), persistedFlight.getOrigin());
        assertEquals(flight.getDestination(), persistedFlight.getDestination());
        assertEquals(flight.getAirline(), persistedFlight.getAirline());

        verify(flightRepo).save(flight);
    }

    

}
