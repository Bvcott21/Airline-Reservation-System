package com.bvcott.airlines.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.dao.DataIntegrityViolationException;

import com.bvcott.airlines.exception.AirportAlreadyExistsException;
import com.bvcott.airlines.model.Airport;
import com.bvcott.airlines.repository.AirportRepository;

public class AirportServiceTest {
    @Mock private AirportRepository airportRepo;
    @InjectMocks private AirportService airportService;

    @BeforeEach
    void setUp() throws Exception {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateAirport_calls_airportRepository_saveMethodPassingSameObject() {
        Airport airport = new Airport("LUT", "Luton", "UK");
        airportService.createAirport(airport);
        verify(airportRepo).save(airport);
    }

    @Test
    void test_createAirport_callsAirportRepository_findByCode() {
        Airport airport = new Airport("LUT", "Luton", "UK");
        airportService.createAirport(airport);
        verify(airportRepo).findByCode(airport.getCode());
    }

    @Test
    void test_CreateAirport_callsAirportRepository_findByName() {
        Airport airport = new Airport("LUT", "Luton", "UK");
        airportService.createAirport(airport);
        verify(airportRepo).findByName(airport.getName());
    }

    @Test
    void test_createAirport_throwsDataIntegrityViolationException_ifCodeIsEmpty() {
        Airport airport = new Airport("", "Luton", "UK");
        
        assertThrows(DataIntegrityViolationException.class, () -> airportService.createAirport(airport));
    }

    @Test
    void test_createAirport_throwsDataIntegrityViolationException_ifNameIsEmpty() {
        Airport airport = new Airport("LUT", "", "UK");
        
        assertThrows(DataIntegrityViolationException.class, () -> airportService.createAirport(airport));
    }

    @Test
    void test_createAirport_throwsDataIntegrityViolationException_ifLocationIsEmpty() {
        Airport airport = new Airport("LUT", "Luton", "");
        
        assertThrows(DataIntegrityViolationException.class, () -> airportService.createAirport(airport));
    }

    @Test
    void test_createAirport_throwsAirportAlreadyExistsException_ifCodeAlreadyExists() {
        Airport airport = new Airport("LUT", "Luton", "UK");

        when(airportRepo.findByCode(airport.getCode())).thenReturn(Optional.of(airport));

        assertThrows(AirportAlreadyExistsException.class, () -> airportService.createAirport(airport));
    }

    @Test
    void test_createAirport_throwsAirportAlreadyExistsException_ifNameAlreadyExists() {
        Airport airport = new Airport("LUT", "Luton", "UK");

        when(airportRepo.findByName(airport.getName())).thenReturn(Optional.of(airport));

        assertThrows(AirportAlreadyExistsException.class, () -> airportService.createAirport(airport));
    }

    @Test
    void test_createAirport_successfullySavesAirport() {
        Airport airport = new Airport("LUT", "Luton", "UK");
        when(airportRepo.save(airport)).thenReturn(airport);
        Airport createdAirport = airportService.createAirport(airport);
        
        verify(airportRepo).save(airport);
        assertEquals(airport, createdAirport);
    }

}
