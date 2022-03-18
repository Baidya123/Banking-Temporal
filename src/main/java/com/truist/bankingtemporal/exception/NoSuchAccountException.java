package com.truist.bankingtemporal.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class NoSuchAccountException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public NoSuchAccountException() {
		super();
	}

	public NoSuchAccountException(String message, Throwable cause) {
		super(message, cause);
	}

	public NoSuchAccountException(String message) {
		super(message);
	}

	public NoSuchAccountException(Throwable cause) {
		super(cause);
	}
}
