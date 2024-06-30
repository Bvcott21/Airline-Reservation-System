package com.bvcott.airlines.exception;

public class AirlineAlreadyExistsException extends RuntimeException {
	public AirlineAlreadyExistsException(String message) {
		super(message);
	}
}
