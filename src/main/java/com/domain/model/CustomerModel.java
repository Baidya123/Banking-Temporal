package com.domain.model;


public class CustomerModel {
	private long customerId;
    private String name ;
    
	public CustomerModel() {
		super();
		// TODO Auto-generated constructor stub
	}
	public CustomerModel(long customerId, String name) {
		super();
		this.customerId = customerId;
		this.name = name;
	}
	public long getCustomerId() {
		return customerId;
	}
	public void setCustomerId(long customerId) {
		this.customerId = customerId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	@Override
	public String toString() {
		return "CustomerModel [customerId=" + customerId + ", name=" + name + "]";
	}

    

   

  
    
}
