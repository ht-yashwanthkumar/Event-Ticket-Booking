package com.event.booking.system.event_service.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.event.booking.system.event_service.dto.BookTicketDto;
import com.event.booking.system.event_service.dto.EventDto;
import com.event.booking.system.event_service.dto.ResponseBody;
import com.event.booking.system.event_service.dto.TicketDto;
import com.event.booking.system.event_service.dto.TicketPriceDto;
import com.event.booking.system.event_service.service.EventService;
import com.event.booking.system.event_service.utils.HelperCls;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/event")
@Tag(name = "EventController", description = "This service helps in managing events")
public class EventController {

	private static final Logger LOGGER = LoggerFactory.getLogger(EventController.class);

	@Autowired
	EventService eventService;

	@Operation(summary = "Get All Events")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Events list", content = {
					@Content(mediaType = "application/json", schema = @Schema(implementation = EventDto.class)) }),
			@ApiResponse(responseCode = "204", description = "No Events found", content = @Content),
			@ApiResponse(responseCode = "500", description = "Service failed due to internal server error while fetching event details") })
	@GetMapping
	public ResponseEntity<ResponseBody<List<EventDto>>> getEvents() {
		LOGGER.debug("Entered into method getEvents");
		List<EventDto> events = eventService.getEvents();
		return !events.isEmpty()
				? new ResponseEntity<ResponseBody<List<EventDto>>>(
						ResponseBody.of("Events informations retrieved successfully", events), HttpStatus.OK)
				: new ResponseEntity<ResponseBody<List<EventDto>>>(
						ResponseBody.of("No Events information found", events), HttpStatus.NO_CONTENT);
	}

	@Operation(summary = "Get Event")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Event", content = {
					@Content(mediaType = "application/json", schema = @Schema(implementation = EventDto.class)) }),
			@ApiResponse(responseCode = "204", description = "No Event found", content = @Content),
			@ApiResponse(responseCode = "500", description = "Service failed due to internal server error while fetching event detail") })
	@GetMapping("/{eventId}")
	public ResponseEntity<ResponseBody<EventDto>> getEventById(@PathVariable("eventId") Long eventId) {
		EventDto eventDto = HelperCls.copyBeans(eventService.getEventById(eventId));
		return new ResponseEntity<ResponseBody<EventDto>>(
				ResponseBody.of("Event information retrieved successfully", eventDto), HttpStatus.OK);
	}

	@Operation(summary = "Save Event")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Event saved successfully", content = {
					@Content(mediaType = "application/json", schema = @Schema(implementation = Long.class)) }),
			@ApiResponse(responseCode = "500", description = "Service failed due to internal server error while saving event detail") })
	@PostMapping
	public ResponseEntity<ResponseBody<Long>> saveEvent(@RequestBody EventDto eventDto) {
		LOGGER.debug("Entered into method saveEvent");
		return new ResponseEntity<ResponseBody<Long>>(
				ResponseBody.of("New event successfully created", eventService.saveEvent(eventDto)), HttpStatus.OK);
	}

	@Operation(summary = "Update Event")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Event updated successfully", content = {
					@Content(mediaType = "application/json", schema = @Schema(implementation = Long.class)) }),
			@ApiResponse(responseCode = "500", description = "Service failed due to internal server error while updating event detail") })
	@PutMapping("/{eventId}")
	public ResponseEntity<ResponseBody<Long>> updateEvent(@PathVariable("eventId") Long eventId,
			@RequestBody EventDto eventDto) {
		LOGGER.debug("Entered into method updateEvent");
		return new ResponseEntity<ResponseBody<Long>>(
				ResponseBody.of("Event successfully updated.", eventService.updateEvent(eventId, eventDto)),
				HttpStatus.OK);
	}

	@Operation(summary = "Cancel/Delete Event")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Event cancelled/deleted successfully", content = {
					@Content(mediaType = "application/json", schema = @Schema(implementation = Void.class)) }),
			@ApiResponse(responseCode = "500", description = "Service failed due to internal server error while updating event detail") })
	@DeleteMapping("/{eventId}")
	public ResponseEntity<ResponseBody<Void>> deleteOrCancelEvent(@PathVariable("eventId") Long eventId,
			@RequestParam("userId") Long userId) {
		LOGGER.debug("Entered into method deleteOrCancelEvent");
		eventService.deleteOrCancelEvent(eventId, userId);
		return new ResponseEntity<ResponseBody<Void>>(ResponseBody.of("Event successfully cancelled."), HttpStatus.OK);
	}

	@Operation(summary = "Book Event Tickets")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Event's ticket purchased successfully", content = {
					@Content(mediaType = "application/json", schema = @Schema(implementation = String.class)) }),
			@ApiResponse(responseCode = "500", description = "Service failed due to internal server error while purchasing event tickets") })
	@PostMapping("/{eventId}/tickets")
	public ResponseEntity<ResponseBody<String>> purchaseTicket(@PathVariable("eventId") Long eventId,
			@RequestBody BookTicketDto bookTicketDto) {
		LOGGER.debug("Entered into method purchaseTicket");
		ResponseBody<String> responseBody = new ResponseBody<String>();

		eventService.purchaseTicket(eventId, bookTicketDto, responseBody);
		return new ResponseEntity<ResponseBody<String>>(responseBody, HttpStatus.OK);
	}

	@Operation(summary = "List Event Tickets")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Event's ticket", content = {
					@Content(mediaType = "application/json", schema = @Schema(implementation = TicketDto.class)) }),
			@ApiResponse(responseCode = "500", description = "Service failed due to internal server error while retreiving event tickets") })
	@GetMapping("/{eventId}/tickets")
	public ResponseEntity<ResponseBody<List<TicketDto>>> getTickets(@PathVariable Long eventId) {
		LOGGER.debug("Entered into method purchaseTicket");
		List<TicketDto> ticketDtos = eventService.getTicketsByEventId(eventId);
		return !ticketDtos.isEmpty()
				? new ResponseEntity<ResponseBody<List<TicketDto>>>(
						ResponseBody.of("Ticket successfully retrieved.", ticketDtos), HttpStatus.OK)
				: new ResponseEntity<ResponseBody<List<TicketDto>>>(ResponseBody.of("No Tickets found.", ticketDtos),
						HttpStatus.NO_CONTENT);
	}

	@Operation(summary = "Update Event Ticket Price")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Event's ticket Price Updated.", content = {
					@Content(mediaType = "application/json", schema = @Schema(implementation = String.class)) }),
			@ApiResponse(responseCode = "500", description = "Service failed due to internal server error while updating event tickets price") })
	@PutMapping("/{eventId}/ticket_price")
	public ResponseEntity<ResponseBody<String>> updateTicketPrice(@PathVariable Long eventId,
			@RequestBody List<TicketPriceDto> ticketPriceDto) {
		LOGGER.debug("Entered into method updateTicketPrice");
		return new ResponseEntity<ResponseBody<String>>(ResponseBody.of("Ticket successfully retrieved.",
				eventService.updateTicketPriceByEvent(eventId, ticketPriceDto)), HttpStatus.OK);
	}

}