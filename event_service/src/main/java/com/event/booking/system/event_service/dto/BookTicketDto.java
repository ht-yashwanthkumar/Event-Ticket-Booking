package com.event.booking.system.event_service.dto;

import java.util.List;

public class BookTicketDto {
	private Long userId;
	private List<TicketDto> ticketDtos;

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public List<TicketDto> getTicketDtos() {
		return ticketDtos;
	}

	public void setTicketDtos(List<TicketDto> ticketDtos) {
		this.ticketDtos = ticketDtos;
	}

}