package com.example.demo.queue;

import java.time.LocalDateTime;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.example.demo.service.EmailService;
import com.example.demo.utils.StandardJsonResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class RabbitMQReceiver {
	protected final Log logger = LogFactory.getLog(getClass());
	private static final String Email_Subject = "Account Balance Information";

	@Autowired
	private EmailService emailService;

	@RabbitListener(queues = "${truist.rabbitmq.queue}")
	public void recievedMessage(StandardJsonResponse response) {
		logger.info("Recieved Message From RabbitMQ: " + response.getData());

		LocalDateTime date = LocalDateTime.now();
		ObjectMapper objectMapper = new ObjectMapper();
		
		try {

			final String emailTo = objectMapper.writeValueAsString(response.getRecipient().get("to"));
			logger.info("email ID of Recepient ::" + emailTo);

			if (!response.isSuccess()) {

				if (response.getMessages().containsKey("error")) {

					String errorMsg = objectMapper.writeValueAsString(response.getMessages().get("error"));
					logger.info("Attempting to send email on failure with reason  :: " + errorMsg);

					emailService.sendMail(emailTo, Email_Subject, errorMsg);
				} else {
					String errorMsg = objectMapper
							.writeValueAsString(response.getMessages().get("account unavailable"));
					logger.info("Attempting to send email on account not available  :: " + errorMsg);

					emailService.sendMail(emailTo, Email_Subject, errorMsg);
				}

				logger.info("Email sent succesfully on processing failure!!");
			}

			else {
				String jsonString = "Available balance in your account is :: " + " Rs."
						+ objectMapper.writeValueAsString(response.getData().get("balance")) + " as on  : "
						+ date.toString();
				logger.info("Attempting to send email with updated balance info :: " + jsonString);

				emailService.sendMail(emailTo, Email_Subject, jsonString);

				logger.info("Email sent succesfully!!");
			}

		} catch (JsonProcessingException e) {

			logger.info("Error occured on sending email:: " + e.getLocalizedMessage());

			e.printStackTrace();
		}

	}

}
