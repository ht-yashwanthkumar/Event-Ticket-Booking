package com.event.booking.system.notification_service.service;

import java.io.StringReader;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.ui.freemarker.FreeMarkerConfigurationFactoryBean;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import freemarker.template.Configuration;
import freemarker.template.Template;

@Component
public class EmailTemplateResolver {

	private static final Logger LOGGER = LoggerFactory.getLogger(EmailTemplateResolver.class);

	/** The Free Marker Template Configiration */
	@Autowired
	private FreeMarkerConfigurationFactoryBean freeMarkerConfigurationFactoryBean;

	public String resolveTemplate(String bodyTemplate, Map<String, String> paramsMap) {

		try {
			Configuration configuration = freeMarkerConfigurationFactoryBean.createConfiguration();
			StringBuilder buf = new StringBuilder();
			Template mailTemplate = new Template("template", new StringReader(bodyTemplate), configuration);

			String mailBody = FreeMarkerTemplateUtils.processTemplateIntoString(mailTemplate, paramsMap);

			if (mailBody != null) {
				buf.append(mailBody + "<HR/>");
			}
			return buf.toString();
		} catch (Exception ex) {
			LOGGER.error("Exception occurred while resolving the template");
		}
		return null;
	}
}