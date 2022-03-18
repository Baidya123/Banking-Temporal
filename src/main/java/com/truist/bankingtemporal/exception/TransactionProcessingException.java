package com.truist.bankingtemporal.exception;

public class TransactionProcessingException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public TransactionProcessingException() {
		super();
	}

	public TransactionProcessingException(String message, Throwable cause) {
		super(message, cause);
	}

	public TransactionProcessingException(String message) {
		super(message);
	}

	public TransactionProcessingException(Throwable cause) {
		super(cause);
	}
}
