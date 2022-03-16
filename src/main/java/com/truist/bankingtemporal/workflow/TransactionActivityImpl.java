package com.truist.bankingtemporal.workflow;

import com.truist.bankingtemporal.model.BalanceRequest;
import com.truist.bankingtemporal.model.ServiceRequest;
import com.truist.bankingtemporal.service.TransactionService;

import io.temporal.workflow.Functions.Proc;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class TransactionActivityImpl implements TransactionActivity {

    private static final String DEBIT_STATUS = "debiting sender account";
    private static final String CREDIT_STATUS = "crediting receiver account";    
    private static final String BALANCE_STATUS = "finishing and fetching balance";
    private static final String DEBIT_ROLLBACK_STATUS = "reverting debited amount";

    private final TransactionService transactionService;
    @Override
    public boolean debitAccount(ServiceRequest debitRequest) {
        log.debug(DEBIT_STATUS);
        return transactionService.processDebit(debitRequest);
    }

    @Override
    public boolean creditAccount(ServiceRequest creditRequest) {
        log.debug(CREDIT_STATUS);
        return transactionService.processCredit(creditRequest);
    }

    @Override
    public boolean fetchBalance(BalanceRequest balanceRequest) {
        log.debug(BALANCE_STATUS);
        transactionService.fetchBalance(balanceRequest).complete(true);
        return true;
    }
    
    @Override
    public boolean debitRollback(ServiceRequest debitRollbackRequest) {
        log.debug(DEBIT_ROLLBACK_STATUS);
        return transactionService.processDebitRollback(debitRollbackRequest);
    }
}
