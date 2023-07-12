package com.security.exception;

public class RoleMismatchException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public RoleMismatchException(String message) {

		super(message);
	}
}
