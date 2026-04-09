package com.asian.auto.hub.serviceimpl;

public class UserAlreadyExisteException extends RuntimeException {

	public UserAlreadyExisteException() {
		super();
		
	}

	public UserAlreadyExisteException(String message) {
		super(message);
		
	}

}
