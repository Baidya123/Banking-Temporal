package com.example.demo.queue;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.example.demo.utils.StandardJsonResponse;

@Service
public class RabbitMQSender {

	@Autowired
	private AmqpTemplate amqpTemplate;

	@Value("${truist.rabbitmq.exchange}")
	private String exchange;

	@Value("${truist.rabbitmq.routingkey}")
	private String routingkey;

	public void send(StandardJsonResponse response) {
		amqpTemplate.convertAndSend(exchange, routingkey, response);

	}
}