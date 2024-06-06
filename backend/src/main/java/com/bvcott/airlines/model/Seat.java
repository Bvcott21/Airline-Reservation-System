package com.bvcott.airlines.model;

import java.util.UUID;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class Seat {
	@Id @GeneratedValue(strategy = GenerationType.AUTO)
	private UUID id;
	
	private String seatNumber;
	private String seatClass;
	private Boolean isAvailable;
	
	@ManyToOne @JoinColumn(name = "flight_id")
	private Flight flight;
	
	Seat() {}

	public Seat(String seatNumber, String seatClass, Flight flight) {
		super();
		this.seatNumber = seatNumber;
		this.seatClass = seatClass;
		this.isAvailable = true;
		this.flight = flight;
	}

	public UUID getId() {
		return id;
	}

	public void setId(UUID id) {
		this.id = id;
	}

	public String getSeatNumber() {
		return seatNumber;
	}

	public void setSeatNumber(String seatNumber) {
		this.seatNumber = seatNumber;
	}

	public String getSeatClass() {
		return seatClass;
	}

	public void setSeatClass(String seatClass) {
		this.seatClass = seatClass;
	}

	public Boolean getIsAvailable() {
		return isAvailable;
	}

	public void setIsAvailable(Boolean isAvailable) {
		this.isAvailable = isAvailable;
	}

	public Flight getFlight() {
		return flight;
	}

	public void setFlight(Flight flight) {
		this.flight = flight;
	}

	@Override
	public String toString() {
		return "Seat [id=" + id + ", seatNumber=" + seatNumber + ", seatClass=" + seatClass + ", isAvailable="
				+ isAvailable + ", flight=" + flight + "]";
	}
	
	
	
}
