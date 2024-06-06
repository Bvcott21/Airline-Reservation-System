package com.bvcott.airlines.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bvcott.airlines.model.Passenger;

public interface PassengerRepository extends JpaRepository<Passenger, UUID> {

}
