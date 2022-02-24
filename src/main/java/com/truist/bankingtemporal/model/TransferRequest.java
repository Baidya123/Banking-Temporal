package com.truist.bankingtemporal.model;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class TransferRequest {
    private Account debitAccount;
    private Account creditAccount;
    private Payment payment;
}
