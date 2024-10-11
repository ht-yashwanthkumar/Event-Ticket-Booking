package com.event.booking.system.event_service.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.event.booking.system.event_service.dto.NotificationDto;
import com.event.booking.system.event_service.dto.ResponseBody;

@FeignClient(name = "notification-service")
public interface NotificationClient {
	@PostMapping("/notification/_sendEmail")
	public ResponseEntity<ResponseBody<String>> sendEmail(@RequestBody NotificationDto notificationDto);
}