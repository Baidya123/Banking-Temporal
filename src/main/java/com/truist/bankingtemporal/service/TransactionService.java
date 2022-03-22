package com.truist.bankingtemporal.service;

import java.util.concurrent.CompletableFuture;

import com.truist.bankingtemporal.model.BalanceRequest;
import com.truist.bankingtemporal.model.ServiceRequest;

public interface TransactionService {
    void processDebit(ServiceRequest debitRequest);
    void processCredit(ServiceRequest creditRequest);
    void notifyAll(ServiceRequest transactionRequest);
    CompletableFuture<Object> fetchBalance(BalanceRequest balanceRequest);
    boolean processDebitRollback(ServiceRequest debitRequest);
    void setNotifyEmail(String notifyEmail);
    void notifyUser(String message);
}
