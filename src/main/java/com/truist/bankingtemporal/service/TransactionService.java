package com.truist.bankingtemporal.service;

import java.util.concurrent.CompletableFuture;

import com.truist.bankingtemporal.model.BalanceRequest;
import com.truist.bankingtemporal.model.ServiceRequest;

public interface TransactionService {
    void processDebit(ServiceRequest debitRequest, String workflowId, String notifyEmail);
    void processCredit(ServiceRequest creditRequest, String workflowId, String notifyEmail);
    void notifyAll(ServiceRequest transactionRequest, String workflowId, String notifyEmail);
    boolean processDebitRollback(ServiceRequest debitRequest, String workflowId, String notifyEmail);
    void notifyUser(String message, String notifyEmail);
}
