package com.event.booking.system.notification_service.config.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.event.booking.system.notification_service.dto.NotificationDto;
import com.event.booking.system.notification_service.dto.ResponseBody;
import com.event.booking.system.notification_service.service.NotificationService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/notification")

@Tag(name = "NotificationController", description = "This service helps in managing notifications")
public class NotificationController {

	private static Logger LOGGER = LoggerFactory.getLogger(NotificationController.class);

	@Autowired
	NotificationService notificationService;

	@Operation(summary = "Get All Events")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Email sent successfully", content = {
					@Content(mediaType = "application/json", schema = @Schema(implementation = String.class)) }),
			@ApiResponse(responseCode = "500", description = "Service failed due to internal server error while fetching sending emails") })

	@PostMapping("/_sendEmail")
	public ResponseEntity<ResponseBody<String>> sendEmail(@RequestBody NotificationDto notificationDto) {
		LOGGER.debug("Entered into method sendEmail");
		return new ResponseEntity<ResponseBody<String>>(
				ResponseBody.of("Email send successfully", notificationService.sendEmail(notificationDto)),
				HttpStatus.OK);

	}
}