package com.example.demo.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.model.Account;
import com.example.demo.model.EmailRequest;
import com.example.demo.queue.RabbitMQSender;
import com.example.demo.service.AccountService;
import com.example.demo.service.EmailService;
import com.example.demo.utils.StandardJsonResponse;
import com.example.demo.utils.StandardJsonResponseImpl;

@RestController
@RequestMapping(value = "/api/v1")
public class AccountBalanceController {

	protected final Log logger = LogFactory.getLog(getClass());

	@Autowired
	AccountService accountService;

	@Autowired
	RabbitMQSender rabbitMQSender;

	@Autowired
	private EmailService emailService;

	/*
	 * Account balance retrieval , through Rabbit MQ and email notification
	 */
	@GetMapping("/checkBalance")
	public ResponseEntity<StandardJsonResponse> getBalance(@RequestParam(required = true) long accountNo) {

		StandardJsonResponse jsonResponse = new StandardJsonResponseImpl();
		HashMap<String, Object> responseData = new HashMap<>();
		HashMap<String, String> responseMessage = new HashMap<>();
		HashMap<String, String> recepientMail = new HashMap<>();

		try {
			Optional<List<Object[]>> account = Optional.ofNullable(accountService.findByAccountNumber(accountNo));
			Object[] userDetails = account.get().get(0);

			String email = String.valueOf(userDetails[1]);
			logger.info("EmailID fetched from dB is :: " + email + " for account Number :: " + accountNo);

			if (!account.isPresent()) {

				jsonResponse.setSuccess(false, "Resource not found", StandardJsonResponse.RESOURCE_NOT_FOUND_MSG);
				logger.info("Account not available");
				responseMessage.put("account unavailable", StandardJsonResponse.RESOURCE_NOT_FOUND_MSG);
				jsonResponse.setMessages(responseMessage);
				// rabbitMQSender.send(jsonResponse);

			} else {
				responseData.put("balance", userDetails[0]);
				jsonResponse.setData(responseData);
				recepientMail.put("to", email);
				jsonResponse.setRecipient(recepientMail);
				jsonResponse.setSuccess(true, "Account Balance", "Retrieved Successfully");

				logger.info("Calling Rabbit MQ to publish message");
				rabbitMQSender.send(jsonResponse);

			}

		} catch (Exception e) {
			logger.error(e.getMessage());
			jsonResponse.setSuccess(false, StandardJsonResponse.DEFAULT_MSG_TITLE_VALUE,
					StandardJsonResponse.DEFAULT_MSG_NAME_VALUE);
			responseMessage.put("error", StandardJsonResponse.DEFAULT_MSG_NAME_VALUE);
			jsonResponse.setMessages(responseMessage);
			// rabbitMQSender.send(jsonResponse);
			return new ResponseEntity<>(jsonResponse, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		logger.info("Published to  Rabbit MQ");
		return new ResponseEntity<>(jsonResponse, HttpStatus.OK);
	}

	@GetMapping(value = "/findAllAccountBalance")
	public List<Account> findAllAccountBalance() {

		List<Account> accountList = (List<Account>) accountService.findAllAccountBalanceTest();

		return accountList;
	}

	/*
	 * Notification email to users on account activity
	 */
	@PostMapping("/emailNotify")
	public ResponseEntity<?> sendEmail(@RequestBody EmailRequest request) {

		logger.info(request);

		boolean result = this.emailService.sendMail(request.getTo(), request.getSubject(), request.getMessage());

		if (result) {

			return ResponseEntity.ok("Email Sent Successfully to user with email-Id:: " + request.getTo());

		} else {

			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("email sending failed for user :: " + request.getTo());
		}
	}

}
