package com.domain;


import javax.persistence.*;
import java.io.Serializable;
import java.time.Instant;

@Entity
@Table(name = "debit")
public class Debit implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
	@SequenceGenerator(name = "sequenceGenerator")
	private long transactionId;

	private long sourceAccountnumber;
	
	private long destinationAccountnumber;

	private Instant transactionDate;

	public Debit() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Debit( long sourceAccountnumber, long destinationAccountnumber, Instant transactionDate
			) {
		super();
		this.sourceAccountnumber = sourceAccountnumber;
		this.destinationAccountnumber = destinationAccountnumber;
		this.transactionDate = transactionDate;
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

	@Override
	public String toString() {
		return "Debit [transactionId=" + transactionId + ", sourceAccountnumber=" + sourceAccountnumber
				+ ", destinationAccountnumber=" + destinationAccountnumber + ", transactionDate=" + transactionDate
				+ "]";
	}



}
