package com.event.booking.system.event_service.service;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.event.booking.system.event_service.dto.NotificationDto;
import com.event.booking.system.event_service.dto.ResponseBody;
import com.event.booking.system.event_service.entity.Ticket;
import com.event.booking.system.event_service.feign.NotificationClient;

@Service
public class EmailService {

	private static final Logger LOGGER = LoggerFactory.getLogger(EmailService.class);

	@Autowired
	NotificationClient notificationClient;

	public void sendEmail(String templateFile, List<Ticket> tickets, String sub) {

		ClassLoader classLoader = getClass().getClassLoader();
		URL resource = classLoader.getResource("templates/" + templateFile);
		if (resource == null) {
			LOGGER.debug("Couldn't find email template " + templateFile);
			return;
		}

		File inputSqlFile = new File(resource.getFile());
		Path filePath = Paths.get(inputSqlFile.getAbsolutePath());
		String template;
		try {
			template = Files.readString(filePath);
			if (template.isEmpty()) {
				LOGGER.debug("No contents found in email template");
				return;
			}

			ResponseEntity<ResponseBody<String>> response = notificationClient
					.sendEmail(getNotificationDto(tickets, sub, template));
			if (!response.getStatusCode().equals(HttpStatus.OK)) {
				throw new RuntimeException("Exception occurred while sending email");
			}
		} catch (IOException e) {
			throw new RuntimeException("Exception occurred while sending email");
		}
	}

	private NotificationDto getNotificationDto(List<Ticket> tickets, String sub, String template) {
		NotificationDto dto = new NotificationDto();
		dto.setSubject(sub);
		dto.setReceipients(new String[] { tickets.get(0).getEmail() });

		Map<String, String> paramsMap = new HashMap<String, String>();
		paramsMap.put("name", tickets.get(0).getUserName());
		paramsMap.put("eventName", tickets.get(0).getEvent().getName());
		paramsMap.put("eventDate", tickets.get(0).getEvent().getStartDateTime().toString());
		paramsMap.put("eventLocation", tickets.get(0).getEvent().getVenue());
		paramsMap.put("userName", tickets.get(0).getUserName());
		paramsMap.put("ticketType", getTicketType(tickets));
		paramsMap.put("numberOfTickets", getTotalTickets(tickets));
		paramsMap.put("paymentAmount", getPaymentAmount(tickets));
		dto.setParamsMap(paramsMap);
		dto.setTemplateResolved(Boolean.FALSE);
		dto.setBody(template);
		return dto;
	}

	private String getTicketType(List<Ticket> tickets) {
		return tickets.stream().map(Ticket::getTicketType).collect(Collectors.joining(","));
	}

	private String getPaymentAmount(List<Ticket> tickets) {
		return tickets.stream().map(Ticket::getPaidAmount).reduce(BigDecimal.ZERO, BigDecimal::add).toString();
	}

	public static String getTotalTickets(List<Ticket> tickets) {
		return String.valueOf(tickets.stream().map(Ticket::getQuantity).reduce(0, Integer::sum));
	}
}