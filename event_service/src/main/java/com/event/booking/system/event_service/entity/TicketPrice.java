package com.event.booking.system.event_service.entity;

import java.math.BigDecimal;

import org.hibernate.annotations.SQLRestriction;

import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "ticket_price")
@SQLRestriction(value = "is_deleted = 0")
public class TicketPrice {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "pk_ticket_price_id")
	private Long pkTicketPriceId;

	@Column(name = "ticket_type")
	private String ticketType;

	@Column(name = "ticket_price")
	private BigDecimal price;

	@ManyToOne
	@JoinColumn(name = "fk_event_id", referencedColumnName = "pk_event_id")
	private Event event;

	/** The is deleted. */
	@Column(name = "is_deleted")
	@Convert(converter = org.hibernate.type.NumericBooleanConverter.class)
	private boolean isDeleted = false;

	public Long getPkTicketPriceId() {
		return pkTicketPriceId;
	}

	public void setPkTicketPriceId(Long pkTicketPriceId) {
		this.pkTicketPriceId = pkTicketPriceId;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	public Event getEvent() {
		return event;
	}

	public void setEvent(Event event) {
		this.event = event;
	}

	public String getTicketType() {
		return ticketType;
	}

	public void setTicketType(String ticketType) {
		this.ticketType = ticketType;
	}

	public boolean isDeleted() {
		return isDeleted;
	}

	public void setDeleted(boolean isDeleted) {
		this.isDeleted = isDeleted;
	}

}