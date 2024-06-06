package com.bvcott.airlines.model;

import java.util.UUID;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class CrewMember {
	@Id @GeneratedValue(strategy = GenerationType.AUTO)
	private UUID id;
	
	private String name;
	private String role;
	
	@ManyToOne @JoinColumn(name = "flight_id")
	private Flight flight;

	CrewMember() {}
	
	public CrewMember(String name, String role) {
		super();
		this.name = name;
		this.role = role;
	}

	public UUID getId() {
		return id;
	}

	public void setId(UUID id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public Flight getFlight() {
		return flight;
	}

	public void setFlight(Flight flight) {
		this.flight = flight;
	}

	@Override
	public String toString() {
		return "CrewMember [id=" + id + ", name=" + name + ", role=" + role + ", flight=" + flight + "]";
	}
	
	
}
