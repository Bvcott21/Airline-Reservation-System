package com.bvcott.airlines.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;

@Entity
public class Flight {
	@Id @GeneratedValue(strategy = GenerationType.AUTO) 
	private UUID id;
	
	private String flightNumber;
	private LocalDateTime departureTime;
	private LocalDateTime arrivalTime;
	
	@ManyToOne @JoinColumn(name = "origin_id") 
	private Airport origin;
	
	@ManyToOne @JoinColumn(name = "destination_id") 
	private Airport destination;
	
	@ManyToOne @JoinColumn(name = "airline_id")
	private Airline airline;
	
	@OneToMany(mappedBy = "flight", cascade = {CascadeType.MERGE, CascadeType.PERSIST}) 
	private List<Seat> seats = new ArrayList<>();
	
	@ManyToMany(mappedBy = "flight", cascade = {CascadeType.MERGE, CascadeType.PERSIST}) 
	private List<CrewMember> crew = new ArrayList<>();
	
	Flight() {}

	public Flight(String flightNumber, LocalDateTime departureTime, LocalDateTime arrivalTime, Airport origin,
			Airport destination, Airline airline) {
		super();
		this.flightNumber = flightNumber;
		this.departureTime = departureTime;
		this.arrivalTime = arrivalTime;
		this.origin = origin;
		this.destination = destination;
		this.airline = airline;
	}
	
	public void addSeat(Seat seat) {
		if(!seats.contains(seat)) {
			seat.setIsAvailable(false);
			seats.add(seat);
			seat.setFlight(this);
		}
	}
	
	public void removeSeat(Seat seat) {
		if(seats.contains(seat)) {
			seat.setIsAvailable(true);
			seats.remove(seat);
			seat.setFlight(null);
		}
	}
	
	public void addCrewMember(CrewMember crewMember) {
		if(!crew.contains(crewMember)) {
			crewMember.setFlight(this);
			crew.add(crewMember);
		}
	}
	
	public void removeCrewMember(CrewMember crewMember) {
		if(crew.contains(crewMember)) {
			crewMember.setFlight(null);
			crew.remove(crewMember);
		}
	}

	public UUID getId() {
		return id;
	}

	public void setId(UUID id) {
		this.id = id;
	}

	public String getFlightNumber() {
		return flightNumber;
	}

	public void setFlightNumber(String flightNumber) {
		this.flightNumber = flightNumber;
	}

	public LocalDateTime getDepartureTime() {
		return departureTime;
	}

	public void setDepartureTime(LocalDateTime departureTime) {
		this.departureTime = departureTime;
	}

	public LocalDateTime getArrivalTime() {
		return arrivalTime;
	}

	public void setArrivalTime(LocalDateTime arrivalTime) {
		this.arrivalTime = arrivalTime;
	}

	public Airport getOrigin() {
		return origin;
	}

	public void setOrigin(Airport origin) {
		this.origin = origin;
	}

	public Airport getDestination() {
		return destination;
	}

	public void setDestination(Airport destination) {
		this.destination = destination;
	}

	public Airline getAirline() {
		return airline;
	}

	public void setAirline(Airline airline) {
		this.airline = airline;
	}

	public List<Seat> getSeats() {
		return seats;
	}

	public void setSeats(List<Seat> seats) {
		this.seats = seats;
	}

	public List<CrewMember> getCrew() {
		return crew;
	}

	public void setCrew(List<CrewMember> crew) {
		this.crew = crew;
	}

	@Override
	public String toString() {
		String seatsString = seats
				.stream()
				.map(Seat::getSeatNumber)
				.collect(Collectors.joining(", "));
		
		String crewString = crew
				.stream()
				.map(CrewMember::getName)
				.collect(Collectors.joining(", "));
		
		return "Flight [id=" + id + ", flightNumber=" + flightNumber + ", departureTime=" + departureTime
				+ ", arrivalTime=" + arrivalTime + ", origin=" + origin.getName() + ", destination=" + destination.getName() + ", airline="
				+ airline.getName() + ", seats=" + seatsString + ", crew=" + crewString + "]";
	}

	
	
	
}
