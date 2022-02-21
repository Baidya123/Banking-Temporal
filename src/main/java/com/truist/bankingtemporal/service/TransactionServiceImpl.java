package com.truist.bankingtemporal.service;

import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Service
public class TransactionServiceImpl implements TransactionService {
    @Override
    public CompletableFuture<Boolean> processDebit(Object transactionRequest) {
        return null;
    }

    @Override
    public CompletableFuture<Boolean> processCredit(Object transactionRequest) {
        return null;
    }

    @Override
    public void notifyAll(Object transactionRequest) {
    }

    @Override
    public CompletableFuture<Boolean> fetchBalance(Object transactionRequest) {
        return null;
    }
}
