package com.event.booking.system.event_service.feign;

import java.math.BigDecimal;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.event.booking.system.event_service.config.FeignConfig;
import com.event.booking.system.event_service.dto.ResponseBody;
import com.event.booking.system.event_service.dto.UserDto;

@FeignClient(name = "user-service", configuration = FeignConfig.class)
public interface UserClient {

	@PostMapping("/user/{userId}/reserve-credit/{amount}")
	public ResponseEntity<ResponseBody<Boolean>> reserveCredit(@PathVariable Long userId,
			@PathVariable BigDecimal amount);

	@GetMapping("/user/{userId}")
	public ResponseEntity<ResponseBody<UserDto>> getUserById(@PathVariable("userId") Long userId);

	@PostMapping("/user/{userId}/release-credit/{amount}")
	public ResponseEntity<ResponseBody<Void>> releaseCredit(@PathVariable("userId") Long userId,
			@PathVariable("amount") BigDecimal amount);
}