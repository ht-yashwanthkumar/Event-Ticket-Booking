package com.event.booking.system.event_service.service;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.event.booking.system.event_service.dto.ResponseBody;
import com.event.booking.system.event_service.entity.Event;
import com.event.booking.system.event_service.entity.Ticket;
import com.event.booking.system.event_service.entity.TicketStatus;
import com.event.booking.system.event_service.exception.EventNotFoundException;
import com.event.booking.system.event_service.exception.TicketNotFoundException;
import com.event.booking.system.event_service.repository.EventRepository;
import com.event.booking.system.event_service.repository.TicketRepository;
import com.event.booking.system.event_service.utils.Constants;

import jakarta.transaction.Transactional;

@Service
public class TicketService {

	private static final Logger LOGGER = LoggerFactory.getLogger(TicketService.class);

	@Autowired
	TicketRepository ticketRepository;

	@Autowired
	EventRepository eventRepository;

	@Autowired
	RefundService refundService;

	@Autowired
	EmailService emailService;

	@Transactional
	public void saveTicket(Ticket ticket) {
		LOGGER.debug("Entered into method saveTicket");

		ticketRepository.save(ticket);
	}

	@Transactional
	public void updateTicket(Ticket ticket) {
		LOGGER.debug("Entered into method updateTicket");

		ticketRepository.save(ticket);
	}

	public List<Ticket> getTicketsByEventId(Long eventId) {
		LOGGER.debug("Entered into method getTicketsByUserId");

		return Optional.ofNullable(ticketRepository.findTicketsByEvent(eventId, TicketStatus.PURCHASED.toString()))
				.orElseThrow(() -> new TicketNotFoundException("Tickets not found"));

	}

	public List<Ticket> getTicketsByUserId(Long userId) {
		LOGGER.debug("Entered into method getTicketsByUserId");
		return Optional.ofNullable(ticketRepository.findTicketsByfkUserId(userId))
				.orElseThrow(() -> new TicketNotFoundException("Tickets not found"));
	}

	public List<Ticket> getTicketsByEventAndUser(Long userId, Long eventId) {
		LOGGER.debug("Entered into method getTicketsByEventAndUser");
		return Optional
				.ofNullable(
						ticketRepository.findTicketsByEventAndUser(eventId, userId, TicketStatus.PURCHASED.toString()))
				.orElseThrow(() -> new TicketNotFoundException("Tickets not found"));
	}

	public List<Ticket> getAllTickets() {
		LOGGER.debug("Entered into method getAllTickets");

		return Optional.ofNullable(ticketRepository.findAll())
				.orElseThrow(() -> new TicketNotFoundException("Tickets not found"));
	}

	@Transactional
	public void cancelTicket(Long userId, Long eventId, ResponseBody<String> responseBody) {
		LOGGER.debug("Entered into method cancelTicket");

		Optional<Event> optionalEvent = eventRepository.findById(eventId);
		if (optionalEvent.isEmpty()) {
			throw new EventNotFoundException("Event not found for the ID " + eventId);
		}

		Event event = optionalEvent.get();

		// handle case for not to cancel tickets on event day
		List<Ticket> tickets = getTicketsByEventAndUser(userId, eventId);
		if (!tickets.isEmpty()) {
			boolean refundStatus = refundService.initiateRefundForTheCancelledTickets(optionalEvent.get(), tickets);
			if (refundStatus) {
				Long totalIssuedTickets = tickets.stream()
						.filter(ticket -> ticket.getTicketStatus().equals(TicketStatus.PURCHASED.toString())).count();
				for (Ticket ticket : tickets) {
					ticket.setTicketStatus(TicketStatus.CANCELLED.toString());
					updateTicket(ticket);
				}

				event.setCapacity((int) (event.getCapacity() + totalIssuedTickets));
				eventRepository.save(event);
				emailService.sendEmail(Constants.TICKET_CANCELLING_CONFIRMATION_EMAIL, tickets,
						"Ticket Cancelling Confirmation");
				responseBody.setMessage("Successfully cancelled the ticket");

			} else {
				responseBody.setMessage("Something went wrong in initiating refund. Please try again later");
			}
		} else {
			responseBody.setMessage("No user tickets found for cancellations");
		}
	}

}