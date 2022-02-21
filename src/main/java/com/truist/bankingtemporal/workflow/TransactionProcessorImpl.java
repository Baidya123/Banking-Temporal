package com.truist.bankingtemporal.workflow;


import com.truist.bankingtemporal.util.ActivityStubUtils;
import io.temporal.workflow.Workflow;

public class TransactionProcessorImpl implements TransactionProcessor {


    private final TransactionActivity transactionActivity = ActivityStubUtils.getActivitiesStub();

    private String status = "Transaction not started yet";

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
        status = DEBIT_STATUS;
        Workflow.await(() -> transactionActivity.debitAccount(transactionRequest));
    }

    @Override
    public void initCredit(Object transactionRequest) {
        status = CREDIT_STATUS;
        Workflow.await(() -> transactionActivity.creditAccount(transactionRequest));
    }

    @Override
    public void notifyRecipients(Object transactionRequest) {
        status = NOTIFY_STATUS;
        Workflow.await(() -> transactionActivity.notifyAccounts(transactionRequest));
    }

    public Object fetchBalance(Object transactionRequest) {
        status = BALANCE_STATUS;
        return transactionActivity.fetchBalance(transactionRequest);
    }

    @Override
    public String getStatus() {
        return this.status;
    }
}
