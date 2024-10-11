package com.event.booking.system.event_service.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(value = EventNotFoundException.class)
	public ResponseEntity<Object> exception(EventNotFoundException exception) {
		return new ResponseEntity<>(exception.getMessage(), HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler(value = UnAuthorizedException.class)
	public ResponseEntity<Object> exception(UnAuthorizedException exception) {
		return new ResponseEntity<>(exception.getMessage(), HttpStatus.UNAUTHORIZED);
	}

	@ExceptionHandler(value = TicketNotFoundException.class)
	public ResponseEntity<Object> exception(TicketNotFoundException exception) {
		return new ResponseEntity<>(exception.getMessage(), HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler(value = UnknownTicketTypeException.class)
	public ResponseEntity<Object> exception(UnknownTicketTypeException exception) {
		return new ResponseEntity<>(exception.getMessage(), HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler(value = CreditLimitException.class)
	public ResponseEntity<Object> exception(CreditLimitException exception) {
		return new ResponseEntity<>(exception.getMessage(), HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(value = RuntimeException.class)
	public ResponseEntity<Object> exception(RuntimeException exception) {
		return new ResponseEntity<>("Unknown exception occurred", HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler(value = Exception.class)
	public ResponseEntity<Object> exception(Exception exception) {
		return new ResponseEntity<>("Unknown exception occurred", HttpStatus.NOT_FOUND);
	}
}