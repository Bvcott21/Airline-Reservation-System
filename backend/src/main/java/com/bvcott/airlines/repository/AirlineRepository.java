package com.bvcott.airlines.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.bvcott.airlines.model.Airline;

@Repository
public interface AirlineRepository extends JpaRepository<Airline, UUID> {
	Optional<Airline> findByName(String name);
	Optional<Airline> findByCode(String code);
}
