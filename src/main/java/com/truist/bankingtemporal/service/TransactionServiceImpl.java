package com.truist.bankingtemporal.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Service
@Slf4j
public class TransactionServiceImpl implements TransactionService {
    @Override
    public boolean processDebit(Object transactionRequest) {
        log.debug("Debit Successful from sender's account");
        return new CompletableFuture<>().complete(true);
    }

    @Override
    public boolean processCredit(Object transactionRequest) {
        log.debug("Credit Successful to receiver's account");
        return new CompletableFuture<>().complete(true);
    }

    @Override
    public void notifyAll(Object transactionRequest) {
        log.debug("Notification: Dear Mr.Sender, your account balance is $x");
        log.debug("Notification: Dear Mr.Receiver, your account balance is $x");
    }

    @Override
    public CompletableFuture<Object> fetchBalance(Object transactionRequest) {
        return new CompletableFuture<>();
    }
}
