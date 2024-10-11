package com.event.booking.system.notification_service.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.ui.freemarker.FreeMarkerConfigurationFactoryBean;

@Configuration
public class NotificationConfiguration {

	@Bean
	public FreeMarkerConfigurationFactoryBean getConfigurationFactoryBean() {
		return new FreeMarkerConfigurationFactoryBean();
	}
}
