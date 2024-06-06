package com.bvcott.airlines.model;

import java.util.UUID;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;

@Entity
public class Ticket {
	@Id @GeneratedValue(strategy = GenerationType.AUTO)
	private UUID id;
	
	@OneToOne @JoinColumn(name = "booking_id")
	private Booking booking;
	
	private Double price;
	
	Ticket() {}

	public Ticket(Booking booking, Double price) {
		super();
		this.booking = booking;
		this.price = price;
	}

	public UUID getId() {
		return id;
	}

	public void setId(UUID id) {
		this.id = id;
	}

	public Booking getBooking() {
		return booking;
	}

	public void setBooking(Booking booking) {
		this.booking = booking;
	}

	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

	@Override
	public String toString() {
		return "Ticket [id=" + id + ", booking=" + booking + ", price=" + price + "]";
	}
	
	
}
