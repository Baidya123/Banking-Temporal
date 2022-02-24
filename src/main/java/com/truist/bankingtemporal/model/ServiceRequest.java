package com.truist.bankingtemporal.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter @Setter @Builder @NoArgsConstructor @AllArgsConstructor
public class ServiceRequest {
    private long sourceAccountNumber;
    private long destinationAccountNumber;
    private double amount;
}
