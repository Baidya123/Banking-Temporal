package com.domain;


import javax.persistence.*;
import java.io.Serializable;
import java.time.Instant;

@Entity
@Table(name = "credit")
public class Credit implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
	@SequenceGenerator(name = "sequenceGenerator")
	private long transactionId;

	private long sourceAccountnumber;
	
	private long destinationAccountnumber;

	private Instant transactionDate;
	
	private long amount;
	
	

	
	

	public Credit() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Credit( long sourceAccountnumber, long destinationAccountnumber, Instant transactionDate,long amount) {
		super();
		this.sourceAccountnumber = sourceAccountnumber;
		this.destinationAccountnumber = destinationAccountnumber;
		this.transactionDate = transactionDate;
		this.amount=amount;
	}

	public long getTransactionId() {
		return transactionId;
	}

	public void setTransactionId(long transactionId) {
		this.transactionId = transactionId;
	}

	public long getSourceAccountnumber() {
		return sourceAccountnumber;
	}

	public void setSourceAccountnumber(long sourceAccountnumber) {
		this.sourceAccountnumber = sourceAccountnumber;
	}

	public long getDestinationAccountnumber() {
		return destinationAccountnumber;
	}

	public void setDestinationAccountnumber(long destinationAccountnumber) {
		this.destinationAccountnumber = destinationAccountnumber;
	}

	public Instant getTransactionDate() {
		return transactionDate;
	}

	public void setTransactionDate(Instant transactionDate) {
		this.transactionDate = transactionDate;
	}

	public long getAmount() {
		return amount;
	}

	public void setAmount(long amount) {
		this.amount = amount;
	}

	@Override
	public String toString() {
		return "Credit [transactionId=" + transactionId + ", sourceAccountnumber=" + sourceAccountnumber
				+ ", destinationAccountnumber=" + destinationAccountnumber + ", transactionDate=" + transactionDate
				+ ", amount=" + amount + "]";
	}


}
