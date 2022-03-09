
package com.example.demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.SimpleMailMessage;

@Configuration
public class EmailConfig {

	@Bean
	public SimpleMailMessage emailTemplate() {
		SimpleMailMessage message = new SimpleMailMessage();
		message.setTo("rimi.ank@gmail.com");
		message.setFrom("cute09rimmi@gmail.com");
		message.setSubject("Important email");
		message.setText("Testing for POC !!");
		return message;
	}
}
