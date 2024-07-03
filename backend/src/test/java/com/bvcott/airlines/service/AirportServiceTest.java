package com.bvcott.airlines.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.dao.DataIntegrityViolationException;

import com.bvcott.airlines.exception.AirportAlreadyExistsException;
import com.bvcott.airlines.exception.AirportNotFoundException;
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

    @Test
    void retrieveAll_callsAirportRepository_findAllMethod_whenCalled() {
        airportService.retrieveAll();
        verify(airportRepo).findAll();
    }

    @Test
    void test_retrieveByIdCalls_airportRepository_findById_whenCalled() {
        UUID id = UUID.randomUUID();
        Airport airport = new Airport("LUT", "Luton", "UK");

        when(airportRepo.findById(id)).thenReturn(Optional.of(airport));
        airportService.retrieveById(id);

        verify(airportRepo).findById(id);
    }

    @Test
    void test_retrieveById_throwsAirportNotFoundException_ifNoAirportFound() {
        UUID id = UUID.randomUUID();
        assertThrows(AirportNotFoundException.class, () -> airportService.retrieveById(id));
    }

    @Test
    void test_retrieveById_returnsCorrectAirportObject() {
        UUID id = UUID.randomUUID();
        Airport airport = new Airport("LUT", "Luton", "UK");

        when(airportRepo.findById(id)).thenReturn(Optional.of(airport));
        Airport savedAirport = airportService.retrieveById(id);

        assertEquals(airport, savedAirport);
        verify(airportRepo).findById(id);
    }

    @Test
    void test_deleteById_callsAirportRepo_findById() {
        UUID id = UUID.randomUUID();
        Airport airport = new Airport("LUT", "Luton", "UK");

        when(airportRepo.findById(id)).thenReturn(Optional.of(airport));

        airportService.deleteById(id);
        verify(airportRepo).findById(id);
    }

    @Test
    void test_deleteById_throwsAirportNotFoundException_ifAirportNotFound() {
        UUID id = UUID.randomUUID();
        when(airportRepo.findById(id)).thenReturn(Optional.empty());
        assertThrows(AirportNotFoundException.class, () -> airportService.deleteById(id));
    }

    @Test
    void test_deleteById_calls_airportRepo_deleteById_whenAirportIsFound() {
        UUID id = UUID.randomUUID();
        Airport airport = new Airport("LUT", "Luton", "UK");

        when(airportRepo.findById(id)).thenReturn(Optional.of(airport));

        airportService.deleteById(id);
        verify(airportRepo).deleteById(id);
    }

    @Test
    void test_updateAirport_callsAirportRepo_findById_whenCalled() {
        UUID id = UUID.randomUUID();
        Airport airport = new Airport("LUT", "Luton", "UK");

        when(airportRepo.findById(id)).thenReturn(Optional.of(airport));

        airportService.updateAirport(id, airport);

        verify(airportRepo).findById(id);
    }

    @Test
    void test_updateAirport_throwsAirportNotFoundException_ifAirportNotFound() {
        UUID id = UUID.randomUUID();
        Airport airport = new Airport("LUT", "Luton", "UK");

        assertThrows(AirportNotFoundException.class, () ->  airportService.updateAirport(id, airport));
    }

    @Test
    void test_updateAirport_throwsDataIntegrityViolationException_ifNameIsEmpty() {
        Airport airport = new Airport("LUT", "", "UK");
        assertThrows(DataIntegrityViolationException.class, () -> airportService.updateAirport(UUID.randomUUID(), airport));
    }

    @Test
    void test_updateAirport_throwsDataIntegrityViolationException_ifCodeIsEmpty() {
        Airport airport = new Airport("", "Luton", "UK");
        assertThrows(DataIntegrityViolationException.class, () -> airportService.updateAirport(UUID.randomUUID(), airport));
    }

    @Test
    void test_updateAirport_throwsDataIntegrityViolationException_ifLocationIsEmpty() {
        Airport airport = new Airport("LUT", "Luton", "");
        assertThrows(DataIntegrityViolationException.class, () -> airportService.updateAirport(UUID.randomUUID(), airport));
    }
    
    @Test
    void test_updateAirport_callsAirportRepo_findByName() {
        UUID id = UUID.randomUUID();
        Airport airport = new Airport("LUT", "Luton", "UK");

        when(airportRepo.findById(id)).thenReturn(Optional.of(airport));

        airportService.updateAirport(id, airport);
        
        verify(airportRepo).findByName(airport.getName());
    }

    @Test
    void test_updateAirport_callsAirportRepo_findByCode() {
        UUID id = UUID.randomUUID();
        Airport airport = new Airport("LUT", "Luton", "UK");

        when(airportRepo.findById(id)).thenReturn(Optional.of(airport));

        airportService.updateAirport(id, airport);

        verify(airportRepo).findByCode(airport.getCode());
    }

    @Test
    void test_updateAirport_throwsAirportAlreadyExistsException_ifNameExsists() {
        UUID id = UUID.randomUUID();
        Airport airport = new Airport("LUT", "Luton", "UK");

        when(airportRepo.findById(id)).thenReturn(Optional.of(airport));
        when(airportRepo.findByName(airport.getName())).thenReturn(Optional.of(airport));

        assertThrows(AirportAlreadyExistsException.class, () -> airportService.updateAirport(id, airport));
    }

    @Test
    void test_updateAirport_throwsAirportAlreadyExistsException_ifCodeExsists() {
        UUID id = UUID.randomUUID();
        Airport airport = new Airport("LUT", "Luton", "UK");

        when(airportRepo.findById(id)).thenReturn(Optional.of(airport));
        when(airportRepo.findByCode(airport.getCode())).thenReturn(Optional.of(airport));

        assertThrows(AirportAlreadyExistsException.class, () -> airportService.updateAirport(id, airport));
    }

    @Test
    void test_updateAirport_successfullyUpdatesAirport() {
        UUID id = UUID.randomUUID();
        Airport existingAirport = new Airport("LUT", "Luton", "UK");
        Airport updatedAirport = new Airport("NEW", "new-airport", "NEW");

        when(airportRepo.findById(id)).thenReturn(Optional.of(existingAirport));
        when(airportRepo.findByCode(existingAirport.getCode())).thenReturn(Optional.empty());
        when(airportRepo.findByName(existingAirport.getName())).thenReturn(Optional.empty());
        when(airportRepo.save(existingAirport)).thenReturn(updatedAirport);

        Airport result = airportService.updateAirport(id, updatedAirport);

        assertEquals(updatedAirport.getName(), result.getName());
        assertEquals(updatedAirport.getCode(), result.getCode());
        assertEquals(updatedAirport.getLocation(), result.getLocation());
        verify(airportRepo).save(existingAirport);
    }

}
