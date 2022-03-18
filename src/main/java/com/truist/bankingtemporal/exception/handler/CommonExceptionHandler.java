package com.truist.bankingtemporal.exception.handler;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.truist.bankingtemporal.exception.NoSuchAccountException;
import com.truist.bankingtemporal.exception.TransactionProcessingException;
import com.truist.bankingtemporal.exception.response.ErrorResponse;

import lombok.extern.slf4j.Slf4j;

@RestControllerAdvice
@Slf4j
public class CommonExceptionHandler extends ResponseEntityExceptionHandler {

	@ExceptionHandler(value = { TransactionProcessingException.class, NoSuchAccountException.class })
	protected ResponseEntity<Object> handleServerExceptions(Exception ex, WebRequest request) {

		if (ex instanceof NoSuchAccountException) {
			ErrorResponse response = ErrorResponse.builder().error(HttpStatus.NOT_FOUND)
					.code(HttpStatus.NOT_FOUND.value()).message(ex.getMessage()).build();

			return handleExceptionInternal(ex, response, new HttpHeaders(), HttpStatus.NOT_FOUND, request);
		} else {
			ErrorResponse response = ErrorResponse.builder().error(HttpStatus.INTERNAL_SERVER_ERROR)
					.code(HttpStatus.INTERNAL_SERVER_ERROR.value()).message(ex.getMessage()).build();

			return handleExceptionInternal(ex, response, new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR, request);
		}
	}

}
