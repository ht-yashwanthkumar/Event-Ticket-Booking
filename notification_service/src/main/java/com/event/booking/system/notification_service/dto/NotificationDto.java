package com.event.booking.system.notification_service.dto;

import java.util.Map;

public class NotificationDto {
	private String[] receipients;
	private String sender;
	private String subject;
	private String body;
	private boolean isTemplateResolved;
	private Map<String, String> paramsMap;

	public String[] getReceipients() {
		return receipients;
	}

	public void setReceipients(String[] receipients) {
		this.receipients = receipients;
	}

	public String getSender() {
		return sender;
	}

	public void setSender(String sender) {
		this.sender = sender;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

	public boolean isTemplateResolved() {
		return isTemplateResolved;
	}

	public void setTemplateResolved(boolean isTemplateResolved) {
		this.isTemplateResolved = isTemplateResolved;
	}

	public Map<String, String> getParamsMap() {
		return paramsMap;
	}

	public void setParamsMap(Map<String, String> paramsMap) {
		this.paramsMap = paramsMap;
	}

}