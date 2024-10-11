package com.event.booking.system.event_service.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;

import com.event.booking.system.event_service.dto.EventDto;
import com.event.booking.system.event_service.dto.TicketDto;
import com.event.booking.system.event_service.dto.TicketPriceDto;
import com.event.booking.system.event_service.entity.Event;
import com.event.booking.system.event_service.entity.Ticket;
import com.event.booking.system.event_service.entity.TicketPrice;

public class HelperCls {
	public static EventDto copyBeans(Event src) {
		EventDto dst = new EventDto();
		dst.setCapacity(src.getCapacity());
		dst.setDescription(src.getDescription());
		dst.setEventStatus(src.getEventStatus());
		dst.setName(src.getName());
		dst.setPkEventId(src.getPkEventId());
		dst.setPublisherUserId(src.getPublisherUserId());
		dst.setStartDateTime(formatDate(src.getStartDateTime()));
		dst.setVenue(src.getVenue());
		List<TicketPriceDto> dtoList = src.getTicketPrices().stream().map(tPrice -> {
			TicketPriceDto tp = new TicketPriceDto();
			BeanUtils.copyProperties(tPrice, tp);
			return tp;
		}).collect(Collectors.toList());
		dst.setTicketPrices(dtoList);
		return dst;
	}

	public static List<TicketPrice> copyBeans(List<TicketPriceDto> ticketPriceDtos, Event event) {
		List<TicketPrice> ticketPrices = new ArrayList<TicketPrice>();
		for (TicketPriceDto ticketPriceDto : ticketPriceDtos) {
			TicketPrice ticketPrice = new TicketPrice();
			BeanUtils.copyProperties(ticketPriceDto, ticketPrice);
			ticketPrice.setEvent(event);
			ticketPrices.add(ticketPrice);
		}
		return ticketPrices;
	}

	public static TicketDto copyBeans(Ticket ticket) {
		TicketDto ticketDto = new TicketDto();
		ticketDto.setEventId(ticket.getEvent().getPkEventId());
		ticketDto.setNumberOfTickets(ticket.getQuantity());
		ticketDto.setTicketId(ticket.getPkTicketId());
		ticketDto.setPaymentAmount(ticket.getPaidAmount());
		ticketDto.setTicketType(ticket.getTicketType());
		ticketDto.setUserId(ticket.getFkUserId());
		ticketDto.setEventName(ticket.getEvent().getName());
		ticketDto.setEventLocation(ticket.getEvent().getVenue());
		ticketDto.setEventDate(formatDate(ticket.getEvent().getStartDateTime()));
		ticketDto.setUserName(ticket.getUserName());
		ticketDto.setTicketStatus(ticket.getTicketStatus());
		return ticketDto;
	}

	public static String formatDate(Date date) {
		SimpleDateFormat formatter = new SimpleDateFormat("E, dd MMM yyyy HH:mm:ss z");
		return formatter.format(date);
	}

	public static Date formatDate(String date) {
		SimpleDateFormat formatter = new SimpleDateFormat("E, dd MMM yyyy HH:mm:ss z");
		try {
			return formatter.parse(date);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
}