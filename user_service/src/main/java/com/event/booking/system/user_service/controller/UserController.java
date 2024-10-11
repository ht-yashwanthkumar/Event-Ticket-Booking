package com.event.booking.system.user_service.controller;

import java.math.BigDecimal;
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
import org.springframework.web.bind.annotation.RestController;

import com.event.booking.system.user_service.dto.ResponseBody;
import com.event.booking.system.user_service.dto.TicketDto;
import com.event.booking.system.user_service.dto.UserDto;
import com.event.booking.system.user_service.service.CreditService;
import com.event.booking.system.user_service.service.UserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

@RestController
@RequestMapping("/user")
@Tag(name = "UserController", description = "This service helps in getting User details")
public class UserController {

	private static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);

	private UserService userService;
	private CreditService creditService;

	@Autowired
	public UserController(UserService userService, CreditService creditService) {
		super();
		this.userService = userService;
		this.creditService = creditService;
	}

	@Operation(summary = "Get All Users Details")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Users list", content = {
					@Content(mediaType = "application/json", schema = @Schema(implementation = UserDto.class)) }),
			@ApiResponse(responseCode = "204", description = "No users found", content = @Content),
			@ApiResponse(responseCode = "500", description = "Service failed due to internal server error while fetching Users details") })
	@GetMapping
	public ResponseEntity<ResponseBody<List<UserDto>>> getAllUsers() {
		LOGGER.debug("Entered into getAllUsers method");
		List<UserDto> users = userService.getAllUsers();
		return !users.isEmpty()
				? new ResponseEntity<ResponseBody<List<UserDto>>>(
						ResponseBody.of("User information retreived Successfully", users), HttpStatus.OK)
				: new ResponseEntity<ResponseBody<List<UserDto>>>(ResponseBody.of("No User information found", users),
						HttpStatus.NO_CONTENT);
	}

	@Operation(summary = "Save User Details")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "User saved successfully", content = {
					@Content(mediaType = "application/json", schema = @Schema(implementation = UserDto.class)) }),
			@ApiResponse(responseCode = "500", description = "Service failed due to internal server error while saving User details") })
	@PostMapping
	public ResponseEntity<ResponseBody<Long>> saveUser(@RequestBody UserDto userDto) {
		LOGGER.debug("Entered into saveUser method");

		return new ResponseEntity<ResponseBody<Long>>(
				ResponseBody.of("User created Successfully", userService.saveUser(userDto)), HttpStatus.OK);
	}

	@Operation(summary = "Update User Details")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "User updated successfully", content = {
					@Content(mediaType = "application/json", schema = @Schema(implementation = UserDto.class)) }),
			@ApiResponse(responseCode = "500", description = "Service failed due to internal server error while updating User details") })
	@PutMapping("/{userId}")
	public ResponseEntity<ResponseBody<Long>> updateUser(@PathVariable("userId") Long userId,
			@RequestBody UserDto userDto) {
		LOGGER.debug("Entered into updateUser method");
		return new ResponseEntity<ResponseBody<Long>>(
				ResponseBody.of("User updated Successfully", userService.updateUser(userId, userDto)), HttpStatus.OK);

	}

	@Operation(summary = "Get User Details")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "User information", content = {
					@Content(mediaType = "application/json", schema = @Schema(implementation = UserDto.class)) }),
			@ApiResponse(responseCode = "204", description = "No user found", content = @Content),
			@ApiResponse(responseCode = "500", description = "Service failed due to internal server error while fetching User details") })
	@GetMapping("/{userId}")
	public ResponseEntity<ResponseBody<UserDto>> getUserById(@PathVariable("userId") Long userId) {
		LOGGER.debug("Entered into getUserById method");
		UserDto userDto = userService.getUserById(userId);
		return new ResponseEntity<ResponseBody<UserDto>>(
				ResponseBody.of("User information retreived successfully", userDto), HttpStatus.OK);

	}

	@Operation(summary = "Get Users's Tickets")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "User's ticket information", content = {
					@Content(mediaType = "application/json", schema = @Schema(implementation = TicketDto.class)) }),
			@ApiResponse(responseCode = "204", description = "No user's ticket information found", content = @Content),
			@ApiResponse(responseCode = "500", description = "Service failed due to internal server error while fetching User's ticket details") })
	@GetMapping("/{userId}/tickets")
	public ResponseEntity<ResponseBody<List<TicketDto>>> getTicketsByUserId(@PathVariable("userId") Long userId) {
		LOGGER.debug("Entered into getTicketsByUserId method");
		return new ResponseEntity<ResponseBody<List<TicketDto>>>(
				ResponseBody.of("Ticket information retreived successfully", userService.getTicketsByUserId(userId)),
				HttpStatus.OK);

	}

	@Operation(summary = "Cancelling Users's Tickets For Particular Event")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "User's tickets successfully cancelled", content = {
					@Content(mediaType = "application/json", schema = @Schema(implementation = String.class)) }),
			@ApiResponse(responseCode = "500", description = "Service failed due to internal server error while cancelling User's tickets for the particular event") })
	@DeleteMapping("/{userId}/event/{eventId}/tickets")
	public ResponseEntity<ResponseBody<String>> cancelTickets(@PathVariable("userId") Long userId,
			@PathVariable("eventId") Long eventId) {
		LOGGER.debug("Entered into cancelTickets method");
		return new ResponseEntity<ResponseBody<String>>(
				ResponseBody.of("Tickets cancelled successfully", userService.cancelTickets(userId, eventId)),
				HttpStatus.OK);

	}

	@Operation(summary = "Reserving/Deducting Amount During Ticket Booking")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "User's amount successfully debited during ticket booking", content = {
					@Content(mediaType = "application/json", schema = @Schema(implementation = Boolean.class)) }),
			@ApiResponse(responseCode = "500", description = "Service failed due to internal server error while booking User's tickets") })
	@PostMapping("/{userId}/reserve-credit/{amount}")
	public ResponseEntity<ResponseBody<Boolean>> reserveCredit(@PathVariable("userId") Long userId,
			@PathVariable("amount") BigDecimal amount) {
		LOGGER.debug("Entered into reserveCredit method");

		boolean reservedStatus = creditService.reserveCredit(userId, amount);
		return reservedStatus
				? new ResponseEntity<ResponseBody<Boolean>>(
						ResponseBody.of("User credit reserved Successfully", reservedStatus), HttpStatus.OK)
				: new ResponseEntity<ResponseBody<Boolean>>(
						ResponseBody.of("User doesn't have enough credit to make payment", reservedStatus),
						HttpStatus.PAYMENT_REQUIRED);
	}

	@Operation(summary = "UnReserving/Crediting Amount During Event Of Ticket Cancelling/Event Cancelling")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "User's amount successfully creadited back during ticket cancelling/event cancelling", content = {
					@Content(mediaType = "application/json", schema = @Schema(implementation = Void.class)) }),
			@ApiResponse(responseCode = "500", description = "Service failed due to internal server error while cancelling User's tickets") })
	@PostMapping("/{userId}/release-credit/{amount}")
	public ResponseEntity<ResponseBody<Void>> unReserveCredit(@PathVariable("userId") Long userId,
			@PathVariable("amount") BigDecimal amount) {
		LOGGER.debug("Entered into unReserveCredit method");
		creditService.unReserveCredit(userId, amount);
		return new ResponseEntity<ResponseBody<Void>>(ResponseBody.of("User credit updated Successfully"),
				HttpStatus.OK);
	}

}