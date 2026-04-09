package com.asian.auto.hub.exception;


public class ResourceNotFoundException extends RuntimeException {
  public ResourceNotFoundException(String message) {
      super(message);
  }

	public ResourceNotFoundException() {
		super();
		// TODO Auto-generated constructor stub
	}
}