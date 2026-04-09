package com.asian.auto.hub.exception;

public class ResourceConflictException extends RuntimeException {
  public ResourceConflictException(String message) {
      super(message);
  }

	public ResourceConflictException() {
		super();
		// TODO Auto-generated constructor stub
	}
}