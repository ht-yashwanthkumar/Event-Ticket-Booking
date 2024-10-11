package com.event.booking.system.event_service.dto;

import java.math.BigDecimal;

public class TicketPriceDto {
	private Long pkTicketPriceId;
	private String ticketType;
	private BigDecimal price;

	public Long getPkTicketPriceId() {
		return pkTicketPriceId;
	}

	public void setPkTicketPriceId(Long pkTicketPriceId) {
		this.pkTicketPriceId = pkTicketPriceId;
	}

	public String getTicketType() {
		return ticketType;
	}

	public void setTicketType(String ticketType) {
		this.ticketType = ticketType;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

}