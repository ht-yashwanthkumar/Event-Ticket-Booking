package com.event.booking.system.user_service.feign;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.event.booking.system.user_service.dto.ResponseBody;
import com.event.booking.system.user_service.dto.TicketDto;

@FeignClient("event-service")
public interface TicketClient {
	@GetMapping("/ticket")
	public ResponseEntity<ResponseBody<List<TicketDto>>> getTickets(@RequestParam("userId") Long userId,
			@RequestParam("eventId") Long eventId);

	@DeleteMapping("/ticket")
	public ResponseEntity<ResponseBody<String>> cancelTickets(@RequestParam("userId") Long userId,
			@RequestParam("eventId") Long eventId);
}
