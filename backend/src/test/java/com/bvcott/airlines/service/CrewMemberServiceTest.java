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

import com.bvcott.airlines.exception.CrewMemberNotFoundException;
import com.bvcott.airlines.model.CrewMember;
import com.bvcott.airlines.repository.CrewMemberRepository;

public class CrewMemberServiceTest {
    @Mock private CrewMemberRepository crewRepo;
    @InjectMocks private CrewMemberService crewService;

    @BeforeEach
    void setUp() throws Exception {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void test_createCrewMember_throwsDataIntegrityViolationExceptionIfNameIsEmpty() {
        CrewMember crew = new CrewMember("", "Captain");
        assertThrows(DataIntegrityViolationException.class, () -> crewService.createCrewMember(crew));
    }

    @Test
    void test_createCrewMember_throwsDataIntegrityViolationExceptionIfRoleIsEmpty() {
        CrewMember crew = new CrewMember("John Doe", "");
        assertThrows(DataIntegrityViolationException.class, () -> crewService.createCrewMember(crew));
    }

    @Test
    void test_createCrewMember_callsCrewRepo_save_whenMemberWithCorrectDetailsPassedIn() {
        CrewMember crew = new CrewMember("John Doe", "Captain");
        crewService.createCrewMember(crew);
        verify(crewRepo).save(crew);
    }

    @Test
    void test_retrieveAll_callsCrewRepo_findAll() {
        crewService.retrieveAll();
        verify(crewRepo).findAll();
    }

    @Test
    void test_retrieveById_calls_crewRepo_findById_whenCalled() {
        UUID id = UUID.randomUUID();
        CrewMember crew = new CrewMember("John Doe", "Captain");

        when(crewRepo.findById(id)).thenReturn(Optional.of(crew));

        crewService.retrieveById(id);
        verify(crewRepo).findById(id);
    }

    @Test
    void test_retrieveById_throwsCrewMemberNotFoundException_whenMemberNotFound() {
        UUID id = UUID.randomUUID();
        assertThrows(CrewMemberNotFoundException.class, () -> crewService.retrieveById(id));
    }

    @Test
    void test_deleteById_callsCrewRepo_findById_whenCalled() {
        UUID id = UUID.randomUUID();
        CrewMember crew = new CrewMember("John Doe", "Captain");
        
        when(crewRepo.findById(id)).thenReturn(Optional.of(crew));

        crewService.deleteById(id);

        verify(crewRepo).findById(id);
    }

    @Test
    void test_deleteById_throwsCrewMemberNotFoundException_ifMemberNotFound() {
        UUID id = UUID.randomUUID();

        assertThrows(CrewMemberNotFoundException.class, () -> crewService.deleteById(id));
    }


    @Test
    void test_deleteById_callsCrewRepo_deleteById_whenCrewFound() {
        UUID id = UUID.randomUUID();
        CrewMember crew = new CrewMember("John Doe", "Captain");
        
        when(crewRepo.findById(id)).thenReturn(Optional.of(crew));

        crewService.deleteById(id);

        verify(crewRepo).deleteById(id);
    }

    @Test 
    void test_updateCrewMember_throwsDataIntegrityViolation_ifNameIsEmpty() {
        UUID id = UUID.randomUUID();
        CrewMember crew = new CrewMember("", "Captain");

        assertThrows(DataIntegrityViolationException.class, () -> crewService.updateCrewMember(id, crew));
    }

    @Test 
    void test_updateCrewMember_throwsDataIntegrityViolationException_ifRoleIsEmpty() {
        UUID id = UUID.randomUUID();
        CrewMember crew = new CrewMember("John Doe", "");

        assertThrows(DataIntegrityViolationException.class, () -> crewService.updateCrewMember(id, crew));
    }

    @Test 
    void test_updateCrewMember_callsCrewRepo_findById_whenCalled() {
        UUID id = UUID.randomUUID();
        CrewMember crew = new CrewMember("John Doe", "Captain");

        when(crewRepo.findById(id)).thenReturn(Optional.of(crew));

        crewService.updateCrewMember(id, crew);

        verify(crewRepo).findById(id);
    }
    
    @Test 
    void test_updateCrewMember_throwsCrewMemberNotFoundException_whenCalled() {
        UUID id = UUID.randomUUID();
        CrewMember crew = new CrewMember("John Doe", "Captain");

        when(crewRepo.findById(id)).thenReturn(Optional.empty());

        assertThrows(CrewMemberNotFoundException.class, () -> crewService.updateCrewMember(id, crew));
        
        verify(crewRepo).findById(id);
    }
    
    @Test 
    void test_updateCrewMember_calls_crewRepo_saveMethod_whenNoFieldsAreEmpty_andCrewMemberFound() {
        UUID id = UUID.randomUUID();
        CrewMember crew = new CrewMember("John Doe", "Captain");

        when(crewRepo.findById(id)).thenReturn(Optional.of(crew));

        crewService.updateCrewMember(id, crew);

        verify(crewRepo).save(crew);
    }

    @Test void test_updateCrewMember_updatesCrewMembersDetails() {
        UUID id = UUID.randomUUID();
        CrewMember existingCrew = new CrewMember("John Doe", "Captain");
        CrewMember updatedCrew = new CrewMember("new member", "new role");

        when(crewRepo.findById(id)).thenReturn(Optional.of(existingCrew));
        when(crewRepo.save(existingCrew)).thenReturn(updatedCrew);

        CrewMember result = crewService.updateCrewMember(id, updatedCrew);

        assertEquals(updatedCrew.getName(), result.getName());
        assertEquals(updatedCrew.getRole(), result.getRole());
        verify(crewRepo).save(existingCrew);
    }

}
