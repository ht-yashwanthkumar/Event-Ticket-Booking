package com.event.booking.system.notification_service.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import com.event.booking.system.notification_service.dto.NotificationDto;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Service
public class NotificationService {

	private static Logger LOGGER = LoggerFactory.getLogger(NotificationService.class);

	@Autowired
	JavaMailSender javaMailSender;

	@Value("spring.mail.username")
	String emailSender;

	@Autowired
	EmailTemplateResolver templateResolver;

	public String sendEmail(NotificationDto notificationDto) {
		LOGGER.debug("Enetered into method sendEmail");

		LOGGER.debug("Sending email to {}", notificationDto.getSender());

		try {

			if (!notificationDto.isTemplateResolved()) {
				notificationDto.setBody(
						templateResolver.resolveTemplate(notificationDto.getBody(), notificationDto.getParamsMap()));
			}

			MimeMessage mimeMessage = javaMailSender.createMimeMessage();
			MimeMessageHelper helper = new MimeMessageHelper(mimeMessage);
			helper.setSubject(notificationDto.getSubject());
			helper.setText(notificationDto.getBody(), true);
			helper.setTo(notificationDto.getReceipients());
			helper.setFrom(emailSender);

			javaMailSender.send(mimeMessage);
		} catch (MessagingException e) {
			LOGGER.error("Exception occurred while sending email. Exception is {}", e.getMessage());
		}

		return "Email sendtsuccessfully for the user " + notificationDto.getReceipients()[0];

	}
}