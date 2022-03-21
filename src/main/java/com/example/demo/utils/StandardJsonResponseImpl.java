package com.example.demo.utils;

import java.util.HashMap;

public class StandardJsonResponseImpl implements StandardJsonResponse {
	private boolean success = false;

	private HashMap<String, String> messages;

	private HashMap<String, String> errors;

	private HashMap<String, Object> data;
	
	private HashMap<String, String> recipient;


	public StandardJsonResponseImpl() {

		messages = new HashMap<String, String>();
		errors = new HashMap<String, String>();
		data = new HashMap<String, Object>();
		recipient= new HashMap<String, String>();
	}

	@Override
	public void setSuccess(boolean success) {
		this.success = success;
		if (!success) {
			messages.put(DEFAULT_MSG_NAME_FIELD, DEFAULT_MSG_NAME_VALUE);
			messages.put(DEFAULT_MSG_TITLE_FIELD, DEFAULT_MSG_TITLE_VALUE);
		}
	}

	/**
	 * @return the success
	 */
	@Override
	public boolean isSuccess() {
		return success;
	}

	/**
	 * @return the messages
	 */
	@Override
	public HashMap<String, String> getMessages() {
		return messages;
	}

	/**
	 * @param messages the messages to set
	 */
	@Override
	public void setMessages(HashMap<String, String> messages) {
		this.messages = messages;
	}

	@Override
	public HashMap<String, Object> getData() {
		return data;
	}

	@Override
	public void setData(HashMap<String, Object> data) {
		this.data = data;
	}

	/**
	 * @param success
	 * @param title   - message title
	 * @param message -message body
	 */
	@Override
	public void setSuccess(boolean success, String title, String message) {
		this.success = success;
		messages.put(DEFAULT_MSG_NAME_FIELD, (message == null || message.isEmpty()) ? "" : message);
		messages.put(DEFAULT_MSG_TITLE_FIELD, (title == null || title.isEmpty()) ? "" : title);
	}

	@Override
	public HashMap<String, String> getRecipient() {
		return recipient;
	}

	
	@Override
	public void setRecipient(HashMap<String, String> recipient) {
		this.recipient=recipient;
		
	}

}
