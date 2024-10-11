package com.event.booking.system.event_service.entity;

import java.math.BigDecimal;
import java.util.Date;

import org.hibernate.annotations.SQLRestriction;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;

@Entity
@Table(name = "ticket")
@SQLRestriction(value = "is_deleted = 0")
public class Ticket {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "pk_ticket_id")
	private Long pkTicketId;

	@Column(name = "purchase_date")
	@Temporal(TemporalType.TIMESTAMP)
	private Date purchaseDate;

	@Column(name = "ticket_status")
	private String ticketStatus;

	@Column(name = "paid_amount")
	private BigDecimal paidAmount;

	@Column(name = "fk_user_id")
	private Long fkUserId;

	@Column(name = "user_name")
	private String userName;

	@Column(name = "quantity")
	private Integer quantity;

	@Column(name = "ticket_type")
	private String ticketType;

	@Column(name = "refund_status")
	private RefundStatus refundStatus;

	@Column(name = "user_email")
	private String email;

	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "fk_event_id", referencedColumnName = "pk_event_id")
	private Event event;

	/** The is deleted. */
	@Column(name = "is_deleted")
	@Convert(converter = org.hibernate.type.NumericBooleanConverter.class)
	private boolean isDeleted = false;

	public Long getPkTicketId() {
		return pkTicketId;
	}

	public void setPkTicketId(Long pkTicketId) {
		this.pkTicketId = pkTicketId;
	}

	public Date getPurchaseDate() {
		return purchaseDate;
	}

	public void setPurchaseDate(Date purchaseDate) {
		this.purchaseDate = purchaseDate;
	}

	public String getTicketStatus() {
		return ticketStatus;
	}

	public void setTicketStatus(String ticketStatus) {
		this.ticketStatus = ticketStatus;
	}

	public BigDecimal getPaidAmount() {
		return paidAmount;
	}

	public void setPaidAmount(BigDecimal paidAmount) {
		this.paidAmount = paidAmount;
	}

	public Event getEvent() {
		return event;
	}

	public void setEvent(Event event) {
		this.event = event;
	}

	public Long getFkUserId() {
		return fkUserId;
	}

	public void setFkUserId(Long fkUserId) {
		this.fkUserId = fkUserId;
	}

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	public String getTicketType() {
		return ticketType;
	}

	public void setTicketType(String ticketType) {
		this.ticketType = ticketType;
	}

	public RefundStatus getRefundStatus() {
		return refundStatus;
	}

	public void setRefundStatus(RefundStatus refundStatus) {
		this.refundStatus = refundStatus;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public boolean isDeleted() {
		return isDeleted;
	}

	public void setDeleted(boolean isDeleted) {
		this.isDeleted = isDeleted;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

}