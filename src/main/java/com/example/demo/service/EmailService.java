package com.example.demo.service;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

	@Autowired
	private JavaMailSender mailSender;

	protected final Log logger = LogFactory.getLog(getClass());

	public boolean sendMail(String to, String subject, String body) {

		boolean foo = false;

		try {
			SimpleMailMessage message = new SimpleMailMessage();
			message.setTo(to);
			message.setSubject(subject);
			message.setText(body);
			mailSender.send(message);
			foo = true;
		}

		catch (MailException e) {

			logger.error("Exceoption from mail service :: " + e.getLocalizedMessage());
			e.printStackTrace();
		}

		return foo;
	}

}
