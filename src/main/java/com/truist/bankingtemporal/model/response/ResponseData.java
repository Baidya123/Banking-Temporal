package com.truist.bankingtemporal.model.response;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.Instant;

@Getter @Setter @ToString
public class ResponseData {
    private long transactionId;
    private long sourceAccountnumber;
    private long destinationAccountnumber;
    private Instant transactionDate;
    private double amount;
}
