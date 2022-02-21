package com.truist.bankingtemporal.workflow;

import com.truist.bankingtemporal.service.TransactionService;
import com.truist.bankingtemporal.service.TransactionServiceImpl;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TransactionActivityImpl implements TransactionActivity {

    private final TransactionService transactionService = new TransactionServiceImpl();

    @Override
    public boolean debitAccount(Object transactionRequest) {
        return transactionService.processDebit(transactionRequest);
    }

    @Override
    public boolean creditAccount(Object transactionRequest) {
        return transactionService.processCredit(transactionRequest);
    }

    @Override
    public boolean notifyAccounts(Object transactionRequest) {
        transactionService.notifyAll(transactionRequest);
        return true;
    }

    @Override
    public boolean fetchBalance(Object transactionRequest) {
        transactionService.fetchBalance(transactionRequest).complete(true);
        return true;
    }
}
