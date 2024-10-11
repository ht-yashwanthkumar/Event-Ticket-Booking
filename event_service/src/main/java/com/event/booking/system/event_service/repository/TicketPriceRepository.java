package com.event.booking.system.event_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.event.booking.system.event_service.entity.TicketPrice;

@Repository
public interface TicketPriceRepository extends JpaRepository<TicketPrice, Long> {

}