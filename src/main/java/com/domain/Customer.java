package com.domain;

import javax.persistence.*;

import java.io.Serializable;

@Entity
@Table(name = "customer")
public class Customer implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
	@SequenceGenerator(name = "sequenceGenerator")
	private long customerId;
	/*
	 * @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy =
	 * "customer") private BankAccount account;
	 */

	private String name;

	public Customer() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Customer(  String name) {
		super();
		//this.account = account;
		this.name = name;
	}

	public long getCustomerId() {
		return customerId;
	}

	public void setCustomerId(long customerId) {
		this.customerId = customerId;
	}

	/*
	 * public BankAccount getAccount() { return account; }
	 * 
	 * public void setAccount(BankAccount account) { this.account = account; }
	 */

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return "Customer [customerId=" + customerId + ", name=" + name + "]";
	}

	
	
}
