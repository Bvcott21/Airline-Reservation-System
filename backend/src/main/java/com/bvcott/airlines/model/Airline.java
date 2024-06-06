package com.bvcott.airlines.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;

@Entity
public class Airline {
	@Id @GeneratedValue(strategy = GenerationType.AUTO) 
	private UUID id;
	
	private String name;
	private String code;
	
	@OneToMany(mappedBy = "airline", cascade = {CascadeType.MERGE, CascadeType.PERSIST}) 
	private List<Flight> flights = new ArrayList<>();
	
	Airline() {}

	public Airline(String name, String code) {
		super();
		this.name = name;
		this.code = code;
	}
	
	public void addFlight(Flight flight) {
		if(!flights.contains(flight)) {
			flights.add(flight);
		}	
	}
	
	public void removeFlight(Flight flight) {
		if(flights.contains(flight)) {
			flights.remove(flight);
		}
	}
	
	public UUID getId() {
		return id;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public List<Flight> getFlights() {
		return flights;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	@Override
	public String toString() {
		String flightNumbers = flights
				.stream()
				.map(Flight::getFlightNumber)
				.collect(Collectors.joining(", "));	
		
		return "Airline [id=" + id + ", name=" + name + ", code=" + code + ", flights=" + flightNumbers + "]";
	}

	@Override
	public int hashCode() {
		return Objects.hash(code, name);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Airline other = (Airline) obj;
		return Objects.equals(code, other.code) && Objects.equals(name, other.name);
	}
	
	
}
