package com.bvcott.airlines.model;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;

@Entity
public class Airport {
	@Id @GeneratedValue(strategy = GenerationType.AUTO)
	private UUID id;
	
	private String code;
	private String name; 
	private String location;
	
	@OneToMany(mappedBy = "origin", cascade = {CascadeType.MERGE, CascadeType.PERSIST})
	private List<Flight> departures = new ArrayList<>();
	
	@OneToMany(mappedBy = "destination", cascade = {CascadeType.MERGE, CascadeType.PERSIST})
	private List<Flight> arrivals = new ArrayList<>();

	Airport() {}
	
	public Airport(String code, String name, String location) {
		super();
		this.code = code;
		this.name = name;
		this.location = location;
	}
	
	public void addDeparture(Flight flight) {
		if(!departures.contains(flight)) {
			departures.add(flight);
		}
	}
	
	public void removeDeparture(Flight flight) {
		if(departures.contains(flight)) {
			departures.remove(flight);
		}
	}
	
	public void addArrival(Flight flight) {
		if(!arrivals.contains(flight)) {
			arrivals.add(flight);
		}
	}
	
	public void removeArrival(Flight flight) {
		if(arrivals.contains(flight)) {
			arrivals.remove(flight);
		}
	}

	public UUID getId() {
		return id;
	}

	public void setId(UUID id) {
		this.id = id;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public List<Flight> getDepartures() {
		return departures;
	}

	public void setDepartures(List<Flight> departures) {
		this.departures = departures;
	}

	public List<Flight> getArrivals() {
		return arrivals;
	}

	public void setArrivals(List<Flight> arrivals) {
		this.arrivals = arrivals;
	}

	@Override
	public String toString() {
		String arrivalsString = this.arrivals
				.stream()
				.map(Flight::getFlightNumber)
				.collect(Collectors.joining(", "));
		
		String departuresString = this.departures
				.stream()
				.map(Flight::getFlightNumber)
				.collect(Collectors.joining(", "));
		
		return "Airport [id=" + id + ", code=" + code + ", name=" + name + ", location=" + location + ", departures="
				+ departuresString + ", arrivals=" + arrivalsString + "]";
	}
	
	
}
