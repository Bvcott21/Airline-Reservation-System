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
import jakarta.persistence.ManyToMany;

@Entity
public class Passenger {
	@Id @GeneratedValue(strategy = GenerationType.AUTO)
	private UUID id;
	private String name;
	private String passportNumber;
	
	@ManyToMany(mappedBy = "passengers", cascade = {CascadeType.MERGE, CascadeType.PERSIST})
	private List<Booking> bookings = new ArrayList<>();
	
	Passenger() {}

	public Passenger(String name, String passportNumber) {
		super();
		this.name = name;
		this.passportNumber = passportNumber;
	}
	
	public void addBooking(Booking booking) {
		if(!bookings.contains(booking)) {
			bookings.add(booking);
			booking.addPassenger(this);
		}
	}
	
	public void removeBooking(Booking booking) {
		if(bookings.contains(booking)) {
			bookings.remove(booking);
			booking.removePassenger(this);
		}
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

	public String getPassportNumber() {
		return passportNumber;
	}

	public void setPassportNumber(String passportNumber) {
		this.passportNumber = passportNumber;
	}

	public List<Booking> getBookings() {
		return bookings;
	}

	public void setBookings(List<Booking> bookings) {
		this.bookings = bookings;
	}

	@Override
	public String toString() {
		String bookingsString = bookings
				.stream()
				.map(Booking::getId)
				.map(UUID::toString)
				.collect(Collectors.joining(", "));
		
				return "Passenger [id=" + id + ", name=" + name + ", passportNumber=" + passportNumber + ", bookings="
				+ bookingsString + "]";
	}

	
	
	
}
