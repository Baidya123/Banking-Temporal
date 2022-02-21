package com.truist.bankingtemporal.service;

import java.util.concurrent.CompletableFuture;

public interface TransactionService {
    CompletableFuture<Boolean> processDebit(Object transactionRequest);
    CompletableFuture<Boolean> processCredit(Object transactionRequest);
    void notifyAll(Object transactionRequest);
    CompletableFuture<Boolean> fetchBalance(Object transactionRequest);
}
