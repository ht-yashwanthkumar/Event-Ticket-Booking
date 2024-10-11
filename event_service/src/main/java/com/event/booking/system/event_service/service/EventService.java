package com.event.booking.system.event_service.service;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.event.booking.system.event_service.dto.BookTicketDto;
import com.event.booking.system.event_service.dto.EventDto;
import com.event.booking.system.event_service.dto.ResponseBody;
import com.event.booking.system.event_service.dto.TicketDto;
import com.event.booking.system.event_service.dto.TicketPriceDto;
import com.event.booking.system.event_service.dto.UserDto;
import com.event.booking.system.event_service.entity.Event;
import com.event.booking.system.event_service.entity.EventStatus;
import com.event.booking.system.event_service.entity.Ticket;
import com.event.booking.system.event_service.entity.TicketPrice;
import com.event.booking.system.event_service.entity.TicketStatus;
import com.event.booking.system.event_service.exception.EventNotFoundException;
import com.event.booking.system.event_service.exception.UnAuthorizedException;
import com.event.booking.system.event_service.exception.UnknownTicketTypeException;
import com.event.booking.system.event_service.feign.UserClient;
import com.event.booking.system.event_service.repository.EventRepository;
import com.event.booking.system.event_service.repository.TicketPriceRepository;
import com.event.booking.system.event_service.utils.Constants;
import com.event.booking.system.event_service.utils.HelperCls;

import jakarta.transaction.Transactional;

@Service
public class EventService {

	private static final Logger LOGGER = LoggerFactory.getLogger(EventService.class);

	private EventRepository eventRepository;
	private TicketService ticketService;
	private TicketPriceRepository ticketPriceRepository;
	private UserClient userClient;
	private RefundService refundService;
	private EmailService emailService;

	@Autowired
	public EventService(EventRepository eventRepository, TicketService ticketService,
			TicketPriceRepository ticketPriceRepository, UserClient userClient, RefundService refundService,
			EmailService emailService) {
		super();
		this.eventRepository = eventRepository;
		this.ticketService = ticketService;
		this.ticketPriceRepository = ticketPriceRepository;
		this.userClient = userClient;
		this.refundService = refundService;
		this.emailService = emailService;
	}

	public List<EventDto> getEvents() {
		LOGGER.debug("Entered into method getEvents");

		List<Event> events = eventRepository.findAll();
		return events.stream().map(event -> HelperCls.copyBeans(event)).collect(Collectors.toList());
	}

	@Transactional
	public Long saveEvent(EventDto eventDto) {
		LOGGER.debug("Entered into method saveEvent");

		Event event = new Event();
		event.setName(eventDto.getName());
		event.setVenue(eventDto.getVenue());
		event.setDescription(eventDto.getDescription());
		event.setStartDateTime(HelperCls.formatDate(eventDto.getStartDateTime()));
		event.setEventStatus(EventStatus.NOT_STARTED.toString());
		event.setCapacity(eventDto.getCapacity());
		event.setPublisherUserId(eventDto.getPublisherUserId());
		event.setTicketPrices(HelperCls.copyBeans(eventDto.getTicketPrices(), event));
		Event savedEvent = eventRepository.save(event);
		return savedEvent.getPkEventId();
	}

	@Transactional
	public Long updateEvent(Long eventId, EventDto eventDto) {
		LOGGER.debug("Entered into method updateEvent");

		Optional<Event> optionalEvent = eventRepository.findById(eventId);
		if (optionalEvent.isEmpty()) {
			throw new EventNotFoundException("Event not found for the event-id: " + eventId);
		}
		Event existingEvent = optionalEvent.get();
		Optional.ofNullable(eventDto.getName()).ifPresent(existingEvent::setName);
		Optional.ofNullable(eventDto.getVenue()).ifPresent(existingEvent::setVenue);
		Optional.ofNullable(eventDto.getDescription()).ifPresent(existingEvent::setDescription);
		Optional.ofNullable(eventDto.getPublisherUserId()).ifPresent(existingEvent::setPublisherUserId);
		Optional.ofNullable(eventDto.getStartDateTime()).ifPresent(eDate -> {
			existingEvent.setStartDateTime(HelperCls.formatDate(eventDto.getStartDateTime()));
		});
		Optional.ofNullable(eventDto.getCapacity()).ifPresent(existingEvent::setCapacity);
		Optional.ofNullable(eventDto.getEventStatus()).ifPresent(existingEvent::setEventStatus);
		Optional.ofNullable(eventDto.getTicketPrices()).ifPresent(ticketPrices -> {
			updateTicketPriceByEvent(existingEvent.getPkEventId(), eventDto.getTicketPrices());
		});
		Event updated = eventRepository.save(existingEvent);
		List<Ticket> tickets = ticketService.getTicketsByEventId(existingEvent.getPkEventId());
		for (Ticket ticket : tickets) {
			ticketService.updateTicket(ticket);
		}

		return updated.getPkEventId();
	}

	public Event getEventById(Long eventId) {
		LOGGER.debug("Entered into method getEventById");

		Optional<Event> optionalEvent = eventRepository.findById(eventId);
		if (optionalEvent.isEmpty()) {
			throw new EventNotFoundException("Event not found for the ID " + eventId);
		}
		return optionalEvent.get();
	}

	@Transactional
	public void deleteOrCancelEvent(Long eventId, Long userId) {
		LOGGER.debug("Entered into method deleteOrCancelEvent");
		Optional<Event> optionalEvent = eventRepository.findById(eventId);
		if (optionalEvent.isEmpty()) {
			throw new EventNotFoundException("Event not found for the event id : " + eventId);
		}

		if (optionalEvent.get().getPublisherUserId().equals(userId)) {

			Event existinEvent = optionalEvent.get();
			List<Ticket> tickets = ticketService.getTicketsByEventId(existinEvent.getPkEventId());
			if (!tickets.isEmpty()) {
				if (refundService.initiateRefundForTheCancelledTickets(existinEvent, tickets)) {
					for (Ticket ticket : tickets) {
						ticket.setTicketStatus(TicketStatus.CANCELLED.toString());
						ticket.setEvent(existinEvent);
						ticketService.updateTicket(ticket);
					}
					emailService.sendEmail(Constants.TICKET_CANCELLING_CONFIRMATION_EMAIL, tickets,
							"Ticket Cancelling Confirmation");
				}
			}
			existinEvent.setEventStatus(EventStatus.CANCELLED.toString());
			eventRepository.save(existinEvent);

		} else {
			throw new UnAuthorizedException("You are not authorized to delete/cancel this event.");
		}
	}

	@Transactional
	public List<TicketDto> getTicketsByEventId(Long eventId) {
		LOGGER.debug("Entered into method getTicketsByEventId");
		Optional<Event> optionalEvent = eventRepository.findById(eventId);
		if (optionalEvent.isEmpty()) {
			throw new EventNotFoundException("Event not found for the event id : " + eventId);
		}

		List<Ticket> tickets = ticketService.getTicketsByEventId(eventId);
		return tickets.stream().map(ticket -> HelperCls.copyBeans(ticket)).collect(Collectors.toList());
	}

	@Transactional
	public String updateTicketPriceByEvent(Long eventId, List<TicketPriceDto> ticketPriceDtos) {
		LOGGER.debug("Entered into method updateTicketPriceByEvent");
		Optional<Event> optionalEvent = eventRepository.findById(eventId);
		if (optionalEvent.isEmpty()) {
			throw new EventNotFoundException("Event not found for the event id : " + eventId);
		}

		List<TicketPrice> ticketPrices = optionalEvent.get().getTicketPrices();
		Map<String, TicketPrice> map = ticketPrices.stream()
				.collect(Collectors.toMap(TicketPrice::getTicketType, Function.identity()));
		List<TicketPrice> updatedTicketPrices = new ArrayList<TicketPrice>();
		for (TicketPriceDto ticketPriceDto : ticketPriceDtos) {
			TicketPrice tPrice;
			if (map.containsKey(ticketPriceDto.getTicketType())) {
				tPrice = map.get(ticketPriceDto.getTicketType());
				tPrice.setPrice(ticketPriceDto.getPrice());
				tPrice.setTicketType(ticketPriceDto.getTicketType());
			} else {
				tPrice = new TicketPrice();
				tPrice.setPrice(ticketPriceDto.getPrice());
				tPrice.setTicketType(ticketPriceDto.getTicketType());
				tPrice.setEvent(optionalEvent.get());
			}
			ticketPriceRepository.save(tPrice);
			updatedTicketPrices.add(tPrice);
		}

		optionalEvent.get().setTicketPrices(updatedTicketPrices);
		eventRepository.save(optionalEvent.get());
		return "Ticket prices updated successfully for the event :" + eventId;
	}

	@Transactional
	public void purchaseTicket(Long eventId, BookTicketDto bookTicketDto, ResponseBody<String> responseBody) {
		LOGGER.debug("Entered into method purchaseTicket");
		Optional<Event> optionalEvent = eventRepository.findById(eventId);
		if (optionalEvent.isEmpty()) {
			throw new EventNotFoundException("Event not found for the event id : " + eventId);
		}

		Event event = optionalEvent.get();
		if (event.getEventStatus().equals(EventStatus.NOT_STARTED.toString())) {
			Integer totalNoOfTickets = getTotalNumberOfTickets(bookTicketDto.getTicketDtos());
			if (event.getCapacity() - totalNoOfTickets > 0) {
				Map<String, BigDecimal> ticketTypePricingMap = event.getTicketPrices().stream()
						.collect(Collectors.toMap(TicketPrice::getTicketType, TicketPrice::getPrice));
				if (isValidTickeType(bookTicketDto.getTicketDtos(), ticketTypePricingMap)) {
					BigDecimal totlaTicketAmount = getTotalTicketAmount(bookTicketDto.getTicketDtos(),
							ticketTypePricingMap);
					ResponseEntity<ResponseBody<UserDto>> userResponse = userClient
							.getUserById(bookTicketDto.getUserId());
					if (userResponse.getStatusCode() == HttpStatus.OK) {
						ResponseEntity<ResponseBody<Boolean>> userCreditResponse = userClient
								.reserveCredit(bookTicketDto.getUserId(), totlaTicketAmount);
						if (userCreditResponse.getStatusCode() == HttpStatus.OK) {
							event.setCapacity(event.getCapacity() - totalNoOfTickets);

							List<Ticket> tickets = new ArrayList<Ticket>();
							for (TicketDto ticketDto : bookTicketDto.getTicketDtos()) {
								Ticket ticket = new Ticket();
								ticket.setTicketStatus(TicketStatus.PURCHASED.toString());
								BigDecimal amountPaid = ticketTypePricingMap.get(ticketDto.getTicketType())
										.multiply(BigDecimal.valueOf(ticketDto.getNumberOfTickets()));
								ticket.setPaidAmount(amountPaid);
								ticket.setTicketType(ticketDto.getTicketType());
								ticket.setFkUserId(bookTicketDto.getUserId());
								ticket.setEvent(event);
								ticket.setQuantity(ticketDto.getNumberOfTickets());
								ticket.setPurchaseDate(new Timestamp(new Date().getTime()));
								ticket.setUserName(userResponse.getBody().getData().getUserName());
								ticket.setEmail(userResponse.getBody().getData().getEmail());
								ticketService.saveTicket(ticket);
								tickets.add(ticket);
							}

							eventRepository.save(event);

							emailService.sendEmail(Constants.TICKET_BOOKING_CONFIRMATION_EMAIL, tickets,
									"Ticket Booked Confirmation");
							responseBody.setMessage("Tickets booked successfully");

						} else {
							responseBody.setMessage("User doesn't have enough credit to book the tickets");
						}
					} else {
						responseBody.setMessage("User not found. Please register as new user");
					}

				} else {
					throw new UnknownTicketTypeException("Unknown ticket type ");
				}
			} else {
				responseBody.setMessage("Cannot book the ticket. All the seats are full");
			}
		} else {
			responseBody.setMessage("Cannot book tickets. Please contact administrator");
		}
	}

	private boolean isValidTickeType(List<TicketDto> ticketDtos, Map<String, BigDecimal> ticketTypePricingMap) {
		List<String> t = ticketDtos.stream().map(TicketDto::getTicketType).collect(Collectors.toList()).stream()
				.filter(ticket -> !ticketTypePricingMap.containsKey(ticket)).collect(Collectors.toList());
		return t.isEmpty();
	}

	public static Integer getTotalNumberOfTickets(List<TicketDto> tickets) {
		return tickets.stream().map(TicketDto::getNumberOfTickets).reduce(0, Integer::sum);
	}

	private BigDecimal getTotalTicketAmount(List<TicketDto> tickets, Map<String, BigDecimal> ticketTypePricingMap) {
		BigDecimal totlaTicketAmount = new BigDecimal(0);
		for (TicketDto ticketDto : tickets) {
			BigDecimal ticketXquantity = ticketTypePricingMap.get(ticketDto.getTicketType())
					.multiply(BigDecimal.valueOf(ticketDto.getNumberOfTickets()));
			totlaTicketAmount = totlaTicketAmount.add(ticketXquantity);
		}
		return totlaTicketAmount;
	}

}