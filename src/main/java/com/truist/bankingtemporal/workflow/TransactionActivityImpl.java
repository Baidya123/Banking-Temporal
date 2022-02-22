package com.truist.bankingtemporal.workflow;

import com.truist.bankingtemporal.service.TransactionService;
import com.truist.bankingtemporal.service.TransactionServiceImpl;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TransactionActivityImpl implements TransactionActivity {

    private static final String DEBIT_STATUS = "debiting sender account";
    private static final String CREDIT_STATUS = "crediting receiver account";
    private static final String NOTIFY_STATUS = "notifying all";
    private static final String BALANCE_STATUS = "finishing and fetching balance";

    private final TransactionService transactionService = new TransactionServiceImpl();

    @Override
    public boolean debitAccount(Object transactionRequest) {
        log.debug(DEBIT_STATUS);
        return transactionService.processDebit(transactionRequest);
    }

    @Override
    public boolean creditAccount(Object transactionRequest) {
        log.debug(CREDIT_STATUS);
        return transactionService.processCredit(transactionRequest);
    }

    @Override
    public boolean notifyAccounts(Object transactionRequest) {
        log.debug(NOTIFY_STATUS);
        transactionService.notifyAll(transactionRequest);
        return true;
    }

    @Override
    public boolean fetchBalance(Object transactionRequest) {
        log.debug(BALANCE_STATUS);
        transactionService.fetchBalance(transactionRequest).complete(true);
        return true;
    }
}
