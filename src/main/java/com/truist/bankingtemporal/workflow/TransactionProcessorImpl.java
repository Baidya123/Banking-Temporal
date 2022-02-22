package com.truist.bankingtemporal.workflow;


import com.truist.bankingtemporal.util.ActivityStubUtils;
import io.temporal.workflow.Workflow;

public class TransactionProcessorImpl implements TransactionProcessor, TransactionBase {

    private final TransactionActivity transactionActivity = ActivityStubUtils.getActivitiesStub();

    @Override
    public Object process(Object transactionRequest) {
        // 1. debit sender
        initDebit(transactionRequest);

        // 2. credit receiver
        initCredit(transactionRequest);

        // 3. send notifications
        notifyRecipients(transactionRequest);

        // 4. fetch balance of both sender & receiver
        Object response = fetchBalance(transactionRequest);

        return response;
    }

    @Override
    public void initDebit(Object transactionRequest) {
        Workflow.await(() -> transactionActivity.debitAccount(transactionRequest));
    }

    @Override
    public void initCredit(Object transactionRequest) {
        Workflow.await(() -> transactionActivity.creditAccount(transactionRequest));
    }

    @Override
    public void notifyRecipients(Object transactionRequest) {
        Workflow.await(() -> transactionActivity.notifyAccounts(transactionRequest));
    }

    @Override
    public Object fetchBalance(Object transactionRequest) {
        return transactionActivity.fetchBalance(transactionRequest);
    }

}
