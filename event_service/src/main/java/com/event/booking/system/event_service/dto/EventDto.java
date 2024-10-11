package com.event.booking.system.event_service.dto;

import java.util.List;

public class EventDto {

	private Long pkEventId;
	private String name;
	private String description;
	private String venue;
	private String startDateTime;
	private Integer capacity;
	private Long publisherUserId;
	private String eventStatus;
	private List<TicketPriceDto> ticketPrices;
	private List<TicketDto> tickets;

	public Long getPkEventId() {
		return pkEventId;
	}

	public void setPkEventId(Long pkEventId) {
		this.pkEventId = pkEventId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getVenue() {
		return venue;
	}

	public void setVenue(String venue) {
		this.venue = venue;
	}

	public String getStartDateTime() {
		return startDateTime;
	}

	public void setStartDateTime(String startDateTime) {
		this.startDateTime = startDateTime;
	}

	public Integer getCapacity() {
		return capacity;
	}

	public void setCapacity(Integer capacity) {
		this.capacity = capacity;
	}

	public Long getPublisherUserId() {
		return publisherUserId;
	}

	public void setPublisherUserId(Long publisherUserId) {
		this.publisherUserId = publisherUserId;
	}

	public String getEventStatus() {
		return eventStatus;
	}

	public void setEventStatus(String eventStatus) {
		this.eventStatus = eventStatus;
	}

	public List<TicketPriceDto> getTicketPrices() {
		return ticketPrices;
	}

	public void setTicketPrices(List<TicketPriceDto> ticketPrices) {
		this.ticketPrices = ticketPrices;
	}

	public List<TicketDto> getTickets() {
		return tickets;
	}

	public void setTickets(List<TicketDto> tickets) {
		this.tickets = tickets;
	}

}