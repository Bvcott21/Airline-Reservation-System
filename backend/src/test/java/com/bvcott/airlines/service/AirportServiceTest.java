package com.bvcott.airlines.service;

import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

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
    void test_createAirport_callsAirportRepository_findByCode() {}

    @Test
    void test_CreateAirport_callsAirportRepository_findByName() {}

    @Test
    void test_createAirport_throwsDataIntegrityViolationException_ifCodeIsEmpty() {}

    @Test
    void test_createAirport_throwsDataIntegrityViolationException_ifNameIsEmpty() {}

    @Test
    void test_createAirport_throwsDataIntegrityViolationException_ifLocationIsEmpty() {}

    @Test
    void test_createAirport_throwsAirportAlreadyExistsException_ifCodeAlreadyExists() {}

    @Test
    void test_createAirport_throwsAirportAlreadyExistsException_ifNameAlreadyExists() {}

    @Test
    void test_createAirport_successfullySavesAirport() {}

}
