package com.security.exception;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class CustomExceptionHandler extends ResponseEntityExceptionHandler {

	@Autowired
	private ErrorResponse errorResponse;

	@ExceptionHandler(InvalidCredentialsException.class)
	public ResponseEntity<Object> handleInvalidCredentialsException(InvalidCredentialsException ex) {
		// Create a custom error response
		ErrorResponse errorResponse = new ErrorResponse(HttpStatus.UNAUTHORIZED, ex.getMessage());

		// Return the error response with the appropriate HTTP status code
		return new ResponseEntity<>(errorResponse, errorResponse.getStatus());

	}

	@ExceptionHandler(InvalidTokenException.class)
	public ResponseEntity<ErrorResponse> handleInvalidTokenException(InvalidTokenException ex) {
		if (ex.getMessage().equals("Invalid Token Signature")) {
			errorResponse = new ErrorResponse(HttpStatus.UNAUTHORIZED, "Invalid Token Signature");
		} else {
			errorResponse = new ErrorResponse(HttpStatus.UNAUTHORIZED, "Invalid Token");
		}
		return new ResponseEntity<>(errorResponse, HttpStatus.UNAUTHORIZED);
	}

	@ExceptionHandler(RoleMismatchException.class)
    public ResponseEntity<ErrorResponse> handleRoleMismatchException(RoleMismatchException ex) {
		
        ErrorResponse errorResponse = new ErrorResponse(HttpStatus.FORBIDDEN, ex.getMessage());
        
        return new ResponseEntity<>(errorResponse, errorResponse.getStatus());
    }
}
