package com.event.booking.system.event_service.exception;

import feign.Response;
import feign.codec.ErrorDecoder;

public class CustomErrorDecoder implements ErrorDecoder {

	@Override
	public Exception decode(String methodKey, Response response) {
		switch (response.status()) {
		case 402:
			return new CreditLimitException("User doesn't have enough credit balance ");
		default:
			return new Exception("Generic error");
		}
	}
}