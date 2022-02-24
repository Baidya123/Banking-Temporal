package com.truist.bankingtemporal.service;

import com.truist.bankingtemporal.model.BalanceRequest;
import com.truist.bankingtemporal.model.ServiceRequest;

import java.util.concurrent.CompletableFuture;

public interface TransactionService {
    boolean processDebit(ServiceRequest debitRequest);
    boolean processCredit(ServiceRequest creditRequest);
    void notifyAll(Object transactionRequest);
    CompletableFuture<Object> fetchBalance(BalanceRequest balanceRequest);
}
