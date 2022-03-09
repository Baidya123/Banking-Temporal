package com.example.demo.utils;


import java.util.HashMap;
import java.util.UUID;


public interface StandardJsonResponse {

    String DEFAULT_MSG_NAME_FIELD = "message";
    String DEFAULT_MSG_TITLE_FIELD = "title";
    String DEFAULT_MSG_TITLE_VALUE = "Internal Server Error";
    String DEFAULT_MSG_NAME_VALUE = "The server encountered an unexpected condition which prevented it from fulfilling the request.";
    String RESOURCE_NOT_FOUND_MSG = "The resource requested is not found. Please check your resource ID.";
    
    public static final String uuid = UUID.randomUUID().toString();

    void setSuccess(boolean success, String title, String msg);

    /**
     * @return the success
     */
    boolean isSuccess();

    void setSuccess(boolean success);

    /**
     * @return the messages
     */
    HashMap<String, String> getMessages();

    /**
     * @param messages the messages to set
     */
    void setMessages(HashMap<String, String> messages);

    
    /**
     * @return the data
     */
    HashMap<String, Object> getData();

    /**
     * @param data the data to set
     */
    void setData(HashMap<String, Object> data);

    
}