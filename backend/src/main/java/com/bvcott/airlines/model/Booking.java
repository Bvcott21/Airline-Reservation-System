package com.bvcott.airlines.model;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;

@Entity
public class Booking {
	@Id @GeneratedValue(strategy = GenerationType.AUTO)
	private UUID id;
	
	@ManyToOne @JoinColumn(name = "flight_id")
	private Flight flight;
	
	@ManyToMany
	private List<Passenger> passengers = new ArrayList<>();
	
	@OneToMany @JoinColumn(name = "seat_id")
	private List<Seat> seat = new ArrayList<>();
	
	private String status;
	
	Booking() {}
	
	public Booking(Flight flight, String status) {
		super();
		this.flight = flight;
		this.status = status;
	}
	
	public void addPassenger(Passenger passenger) {
		if(!passengers.contains(passenger)) {
			passengers.add(passenger);
		}
	}
	
	public void removePassenger(Passenger passenger) {
		if(passengers.contains(passenger)) {
			passengers.remove(passenger);
		}
	}

	public UUID getId() {
		return id;
	}

	public void setId(UUID id) {
		this.id = id;
	}

	public Flight getFlight() {
		return flight;
	}

	public void setFlight(Flight flight) {
		this.flight = flight;
	}

	public List<Passenger> getPassengers() {
		return passengers;
	}

	public void setPassengers(List<Passenger> passengers) {
		this.passengers = passengers;
	}

	public List<Seat> getSeat() {
		return seat;
	}

	public void setSeat(List<Seat> seat) {
		this.seat = seat;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	@Override
	public String toString() {
		String passengersString = passengers
				.stream()
				.map(Passenger::getName)
				.collect(Collectors.joining(", "));
		
		return "Booking [id=" + id + ", flight=" + flight.getFlightNumber() + ", passengers=" + passengersString + ", seat=" + seat
				+ ", status=" + status + "]";
	}
	
}
