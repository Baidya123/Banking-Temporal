package com.utils;


import com.fasterxml.jackson.annotation.JsonProperty;

public class ResponseModel {
	

  @JsonProperty("message")
  private Object message = null;
	
	/*
	 * @JsonProperty("errors") private Object errors = null;
	 */
  
  @JsonProperty("data")
  private Object data = null;
  
	/*
	 * @JsonProperty("paging") private Object paging = null;
	 */
	/*
	 * public Object getErrors() { return this.errors; }
	 * 
	 * public void setErrors(Object errors) { this.errors = errors; }
	 */
  
  public Object getData() {
    return this.data;
  }
  
  public void setData(Object data) {
    this.data = data;
  }
  
	/*
	 * public Object getPaging() { return this.paging; }
	 * 
	 * public void setPaging(Object paging) { this.paging = paging; }
	 */
  
  public Object getMessage() {
	return message;
}

public void setMessage(Object message) {
	this.message = message;
}

@Override
public String toString() {
	return "ResponseModel [message=" + message + ", data=" + data + "]";
}


}
