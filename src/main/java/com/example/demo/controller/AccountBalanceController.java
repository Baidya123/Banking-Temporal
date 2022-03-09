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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.model.Account;
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

	@GetMapping("/checkBalance")
	public ResponseEntity<StandardJsonResponse> getBalance(@RequestParam(required = true) long accountNo) {

		StandardJsonResponse jsonResponse = new StandardJsonResponseImpl();
		HashMap<String, Object> responseData = new HashMap<>();

		try {
			Optional<Account> account = Optional.of(accountService.findByAccountNumber(accountNo));

			if (account.isPresent()) {
				responseData.put("balance", account.get().getBalance());

				jsonResponse.setSuccess(true, "Account Balance", "Retrieved Successfully");
				jsonResponse.setData(responseData);
			} else {
				jsonResponse.setSuccess(false, "Resource not found", StandardJsonResponse.RESOURCE_NOT_FOUND_MSG);
			}

		} catch (Exception e) {
			logger.error("exception", e);
			jsonResponse.setSuccess(false, StandardJsonResponse.DEFAULT_MSG_TITLE_VALUE,
					StandardJsonResponse.DEFAULT_MSG_NAME_VALUE);
			return new ResponseEntity<>(jsonResponse, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		System.out.println("Calling Rabbit MQ");
		rabbitMQSender.send(jsonResponse);
		System.out.println("Published to  Rabbit MQ");
		return new ResponseEntity<>(jsonResponse, HttpStatus.OK);
	}

	@GetMapping(value = "/sendmail")
	public String sendmail() {

		emailService.sendMail("rimi.ank@gmail.com", "Test Subject", "Test mail");
		emailService.sendPreConfiguredMail("Happy Coding!!!");

		return "emailsent";
	}
	
	@GetMapping(value = "/findAllAccountBalance")
	public List<Account> findAllAccountBalance() {

		List<Account> accountList = (List<Account>) accountService.findAllAccountBalanceTest();

		return accountList;
	}

}