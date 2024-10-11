package com.event.booking.system.event_service.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.event.booking.system.event_service.entity.Ticket;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, Long> {

	@Query("SELECT ticket from Ticket as ticket where ticket.event.pkEventId = :eventId and ticket.ticketStatus = :ticketStatus")
	public List<Ticket> findTicketsByEvent(Long eventId, String ticketStatus);

	@Query("SELECT ticket from Ticket as ticket where ticket.event.pkEventId = :eventId and ticket.fkUserId= :userId and ticket.ticketStatus = :ticketStatus")
	public List<Ticket> findTicketsByEventAndUser(Long eventId, Long userId, String ticketStatus);

	public List<Ticket> findTicketsByfkUserId(Long userId);
}