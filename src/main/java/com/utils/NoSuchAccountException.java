package com.utils;

public class NoSuchAccountException extends RuntimeException {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	String message;

    public NoSuchAccountException(String message) {
        super(message);
        this.message = message;
    }
}
