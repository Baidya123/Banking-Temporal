package com.example.DebitMicroservice.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter @ToString
public class WithdrawalModel {
	private long destinationAccountNumber;
    private long amount ;
    private long sourceAccountNumber;
}
