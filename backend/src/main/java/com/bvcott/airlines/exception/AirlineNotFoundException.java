package com.bvcott.airlines.exception;

public class AirlineNotFoundException extends RuntimeException {
	public AirlineNotFoundException(String message) {
		super(message);
	}
}
