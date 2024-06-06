package com.bvcott.airlines.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bvcott.airlines.model.Seat;

public interface SeatRepository extends JpaRepository<Seat, UUID> {

}
