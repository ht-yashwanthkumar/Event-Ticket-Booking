package com.event.booking.system.event_service.entity;

import java.util.Date;
import java.util.List;

import org.hibernate.annotations.SQLRestriction;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;

@Entity
@Table(name = "event")
@SQLRestriction(value = "is_deleted = 0")
public class Event {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "pk_event_id")
	private Long pkEventId;

	@Column(name = "name")
	private String name;

	@Column(name = "description")
	private String description;

	@Column(name = "venue")
	private String venue;

	@Column(name = "event_start_date")
	@Temporal(TemporalType.TIMESTAMP)
	private Date startDateTime;

	@Column(name = "event_status")
	private String eventStatus;

	@Column(name = "event_capacity")
	private Integer capacity;

	@Column(name = "publisher_user_id")
	private Long publisherUserId;

	@OneToMany(mappedBy = "event", cascade = CascadeType.ALL)
	private List<TicketPrice> ticketPrices;

	@OneToMany(mappedBy = "event", cascade = CascadeType.ALL)
	private List<Ticket> ticket;

	/** The is deleted. */
	@Column(name = "is_deleted")
	@Convert(converter = org.hibernate.type.NumericBooleanConverter.class)
	private boolean isDeleted = false;

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

	public Date getStartDateTime() {
		return startDateTime;
	}

	public void setStartDateTime(Date startDateTime) {
		this.startDateTime = startDateTime;
	}

	public String getEventStatus() {
		return eventStatus;
	}

	public void setEventStatus(String eventStatus) {
		this.eventStatus = eventStatus;
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

	public List<TicketPrice> getTicketPrices() {
		return ticketPrices;
	}

	public void setTicketPrices(List<TicketPrice> ticketPrices) {
		this.ticketPrices = ticketPrices;
	}

	public List<Ticket> getTicket() {
		return ticket;
	}

	public void setTicket(List<Ticket> ticket) {
		this.ticket = ticket;
	}

	public boolean isDeleted() {
		return isDeleted;
	}

	public void setDeleted(boolean isDeleted) {
		this.isDeleted = isDeleted;
	}

}