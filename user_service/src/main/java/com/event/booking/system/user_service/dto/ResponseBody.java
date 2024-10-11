package com.event.booking.system.user_service.dto;

public class ResponseBody<T> {

	private String message;
	private T data;

	public ResponseBody() {
		super();
	}

	public ResponseBody(String message, T data) {
		super();
		this.message = message;
		this.data = data;
	}

	public ResponseBody(String message) {
		super();
		this.message = message;
	}

	public static <T> ResponseBody<T> of(String message, T data) {
		return new ResponseBody<T>(message, data);
	}

	public static ResponseBody<Void> of(String message) {
		return new ResponseBody<Void>(message);
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public T getData() {
		return data;
	}

	public void setData(T data) {
		this.data = data;
	}

}