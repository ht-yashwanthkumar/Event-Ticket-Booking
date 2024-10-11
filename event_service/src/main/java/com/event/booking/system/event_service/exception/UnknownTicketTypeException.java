package com.event.booking.system.event_service.exception;

public class UnknownTicketTypeException extends RuntimeException {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String message;

	public UnknownTicketTypeException() {
	}

	public UnknownTicketTypeException(String msg) {
		super(msg);
		this.message = msg;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

}