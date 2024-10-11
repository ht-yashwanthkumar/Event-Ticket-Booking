package com.event.booking.system.event_service.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import com.event.booking.system.event_service.dto.ResponseBody;
import com.event.booking.system.event_service.entity.Event;
import com.event.booking.system.event_service.entity.Ticket;
import com.event.booking.system.event_service.feign.UserClient;

@Component
public class RefundService {

	@Autowired
	UserClient userClient;

	public boolean initiateRefundForTheCancelledTickets(Event event, List<Ticket> tickets) {

		Map<Long, List<Ticket>> usersTicketsMap = tickets.stream().collect(Collectors.groupingBy(Ticket::getFkUserId));
		for (Entry<Long, List<Ticket>> userTicketMap : usersTicketsMap.entrySet()) {
			BigDecimal totalAmount = new BigDecimal(0);
			for (Ticket ticket : userTicketMap.getValue()) {
				totalAmount = totalAmount.add(ticket.getPaidAmount());
			}
			ResponseEntity<ResponseBody<Void>> releaseCreditResponse = userClient.releaseCredit(userTicketMap.getKey(),
					totalAmount); // Need to modify to refund amount for all the users in just one API Call
			if (!releaseCreditResponse.getStatusCode().equals(HttpStatus.OK)) {
				throw new RuntimeException("Exception occurred while initiating the refund to users");
			}
		}
		return true;
	}
}