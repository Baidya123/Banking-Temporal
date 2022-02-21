package com.truist.bankingtemporal.service;

import java.util.concurrent.CompletableFuture;

public interface TransactionService {
    boolean processDebit(Object transactionRequest);
    boolean processCredit(Object transactionRequest);
    void notifyAll(Object transactionRequest);
    CompletableFuture<Object> fetchBalance(Object transactionRequest);
}
