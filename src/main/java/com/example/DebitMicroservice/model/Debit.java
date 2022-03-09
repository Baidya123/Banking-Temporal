package com.example.DebitMicroservice.model;

import java.time.Instant;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "debit")
@Getter @Setter @ToString
public class Debit {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
	@SequenceGenerator(name = "sequenceGenerator")
	private long transactionId;

	private long sourceAccountnumber;
	
	private long destinationAccountnumber;

	private Instant transactionDate;
	
	private long amount;
}
