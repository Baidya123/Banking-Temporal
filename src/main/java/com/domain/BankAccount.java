package com.domain;

import javax.persistence.*;
import java.io.Serializable;
import java.time.Instant;



@Entity
@Table(name = "account")
public class BankAccount implements Serializable{

    private static final long serialVersionUID = 1L;

    @Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
	@SequenceGenerator(name = "sequenceGenerator")
	private long accountNumber;

	private long balance;

	private Instant creationDate;

	@OneToOne
	@JoinColumn(name = "customerId", referencedColumnName = "customerId")
	private Customer customer;

	private String email;

	public BankAccount() {
		super();
		// TODO Auto-generated constructor stub
	}

	public BankAccount(long accountNumber, long balance, Instant creationDate, Customer customer, String email) {
		super();
		this.accountNumber = accountNumber;
		this.balance = balance;
		this.creationDate = creationDate;
		this.customer = customer;
		this.email = email;
	}

	public long getAccountNumber() {
		return accountNumber;
	}

	public void setAccountNumber(long accountNumber) {
		this.accountNumber = accountNumber;
	}

	public long getBalance() {
		return balance;
	}

	public void setBalance(long balance) {
		this.balance = balance;
	}

	public Instant getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(Instant creationDate) {
		this.creationDate = creationDate;
	}

	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@Override
	public String toString() {
		return "BankAccount [accountNumber=" + accountNumber + ", balance=" + balance + ", creationDate=" + creationDate
				+ ", customer=" + customer + ", email=" + email + "]";
	}
    
    
    
}