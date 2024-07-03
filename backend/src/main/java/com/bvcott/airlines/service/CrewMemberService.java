package com.bvcott.airlines.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;

import com.bvcott.airlines.exception.CrewMemberNotFoundException;
import com.bvcott.airlines.model.CrewMember;
import com.bvcott.airlines.repository.CrewMemberRepository;

public class CrewMemberService {
    private static final Logger log = LoggerFactory.getLogger(CrewMemberService.class);
    private CrewMemberRepository crewRepo;

    CrewMemberService(CrewMemberRepository crewRepo) {
        this.crewRepo = crewRepo;
    }

    public CrewMember createCrewMember(CrewMember crew) {
        log.debug("createCrewMember method called with object: {}", crew);

        if(crew.getName().isEmpty() || crew.getRole().isEmpty()) {
            log.warn("Couldn't persist, one or more of the following fields are empty: name, role.");
            throw new DataIntegrityViolationException("One or more of the following fields are empty: name, role");
        }

        return crewRepo.save(crew);
    }

    public List<CrewMember> retrieveAll() {
        log.debug("retrieveAll method called.");
        return crewRepo.findAll();
    }

    public CrewMember retrieveById(UUID id) {
        log.debug("retrieveById method called");
        return crewRepo
            .findById(id)
            .orElseThrow(() -> new CrewMemberNotFoundException("Crew member not found with ID: " + id));
    }

    public void deleteById(UUID id) {
        log.debug("deleteById method called with ID: " + id);
        Optional<CrewMember> crewFound = crewRepo.findById(id);

        if(!crewFound.isPresent()) {
            log.warn("Couldn't delete, CrewMember with ID: {} - NOT FOUND", id);
            throw new CrewMemberNotFoundException("Crew Member not found with ID: " + id);
        }

        log.info("Crew member found, deleting...");
        crewRepo.deleteById(id);
    }

    public CrewMember updateCrewMember(UUID id, CrewMember crew) {
        log.debug("updateCrewMember method called with ID: {} - and Object {}", id, crew);
        
        if(crew.getName().isEmpty() || crew.getRole().isEmpty()) {
            throw new DataIntegrityViolationException("Couldn't update, one or more of the following fields are empty: name, role");
        }

        Optional<CrewMember> crewFound = crewRepo.findById(id);
        
        if(!crewFound.isPresent()) {
            log.warn("Couldn't update, CrewMember with ID: {} - NOT FOUND", id);
            throw new CrewMemberNotFoundException("CrewMember not found with ID: " + id);
        }

        log.info("Crew member found, updating values...");
        CrewMember existingCrew = crewFound.get();
        existingCrew.setName(crew.getName());
        existingCrew.setRole(crew.getRole());

        CrewMember updatedCrew = crewRepo.save(existingCrew);
        log.info("CrewMember updated successfully.");

        return updatedCrew;
    }

    


}
