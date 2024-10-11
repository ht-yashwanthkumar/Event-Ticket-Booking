package com.event.booking.system.event_service.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.event.booking.system.event_service.dto.ResponseBody;
import com.event.booking.system.event_service.dto.TicketDto;
import com.event.booking.system.event_service.entity.Ticket;
import com.event.booking.system.event_service.service.TicketService;
import com.event.booking.system.event_service.utils.HelperCls;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.websocket.server.PathParam;

@RestController
@RequestMapping("/ticket")
@Tag(name = "TicketController", description = "This service helps in managing tickets")
public class TicketController {

	private static final Logger LOGGER = LoggerFactory.getLogger(TicketController.class);

	@Autowired
	TicketService ticketService;

	@Operation(summary = "Get All Tickets")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Tickets list", content = {
					@Content(mediaType = "application/json", schema = @Schema(implementation = TicketDto.class)) }),
			@ApiResponse(responseCode = "204", description = "No Tickets found", content = @Content),
			@ApiResponse(responseCode = "500", description = "Service failed due to internal server error while fetching ticket details") })
	@GetMapping
	public ResponseEntity<ResponseBody<List<TicketDto>>> getTickets(@PathParam("userId") Long userId,
			@PathParam("eventId") Long eventId) {
		LOGGER.debug("Entered into method getEvents");
		List<Ticket> tickets;
		if (userId != null) {
			tickets = ticketService.getTicketsByUserId(userId);
		} else if (eventId != null) {
			tickets = ticketService.getTicketsByEventId(userId);
		} else {
			tickets = ticketService.getAllTickets();
		}
		return !tickets.isEmpty()
				? new ResponseEntity<ResponseBody<List<TicketDto>>>(
						ResponseBody.of("Tickets information retrieved successfully",
								tickets.stream().map(ticket -> HelperCls.copyBeans(ticket))
										.collect(Collectors.toList())),
						HttpStatus.OK)
				: new ResponseEntity<ResponseBody<List<TicketDto>>>(
						ResponseBody.of("No Tickets information found", tickets.stream()
								.map(ticket -> HelperCls.copyBeans(ticket)).collect(Collectors.toList())),
						HttpStatus.NO_CONTENT);
	}

	@Operation(summary = "Cancel user's tickets")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Status of ticket cancelling", content = {
					@Content(mediaType = "application/json", schema = @Schema(implementation = Boolean.class)) }),
			@ApiResponse(responseCode = "500", description = "Service failed due to internal server error while cancelling user's tickets") })
	@DeleteMapping
	public ResponseEntity<ResponseBody<String>> cancelTickets(@PathParam("userId") Long userId,
			@PathParam("eventId") Long eventId) {
		LOGGER.debug("Entered into method cancelTickets");
		ResponseBody<String> responseBody = new ResponseBody<String>();
		ticketService.cancelTicket(userId, eventId, responseBody);
		return new ResponseEntity<ResponseBody<String>>(responseBody, HttpStatus.OK);
	}

}