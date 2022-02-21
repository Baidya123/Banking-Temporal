package com.truist.bankingtemporal.workflow;

import com.truist.bankingtemporal.service.TransactionService;
import com.truist.bankingtemporal.service.TransactionServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.ExecutionException;

@Slf4j
//@RequiredArgsConstructor
//@Component
public class TransactionActivityImpl implements TransactionActivity{

//    @Autowired
    private final TransactionService transactionService = new TransactionServiceImpl();

    @Override
    public boolean debitAccount(Object transactionRequest) {
        try {
            transactionService.processDebit(transactionRequest).get();
            log.debug("Debit Successful from sender's account");
        } catch (InterruptedException | ExecutionException e) {
            log.debug("Failed to debit from sender's account");
            log.error(e.getMessage());
            return false;
        }

        return true;
    }

    @Override
    public boolean creditAccount(Object transactionRequest) {

        try {
            transactionService.processCredit(transactionRequest).get();
            log.debug("Credit Successful to receiver's account");
        } catch (InterruptedException | ExecutionException e) {
            log.debug("Failed to credit to receiver's account");
            log.error(e.getMessage());
            return false;
        }

        return true;
    }

    @Override
    public boolean notifyAccounts(Object transactionRequest) {
        log.debug("Dear Mr.Sender, your account has been debited with $x");
        log.debug("Dear Mr.Receiver, your account has been credited with $x");
        return true;
    }

    @Override
    public boolean fetchBalance(Object transactionRequest) {
        log.debug("Dear Mr.Sender, your account balance is $x");
        log.debug("Dear Mr.Receiver, your account balance is $x");
        return true;
    }
}
