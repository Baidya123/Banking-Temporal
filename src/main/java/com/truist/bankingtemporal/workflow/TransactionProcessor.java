package com.truist.bankingtemporal.workflow;

import io.temporal.workflow.QueryMethod;
import io.temporal.workflow.SignalMethod;
import io.temporal.workflow.WorkflowInterface;
import io.temporal.workflow.WorkflowMethod;

@WorkflowInterface
public interface TransactionProcessor {
    public static final String DEBIT_STATUS = "debiting sender account";
    public static final String CREDIT_STATUS = "crediting receiver account";
    public static final String NOTIFY_STATUS = "notifying all";
    public static final String BALANCE_STATUS = "finishing and fetching balance";
    @WorkflowMethod
    Object process(Object transactionRequest);

    @SignalMethod(name = DEBIT_STATUS)
    void initDebit(Object transactionRequest);

    @SignalMethod(name = CREDIT_STATUS)
    void initCredit(Object transactionRequest);

    @SignalMethod(name = NOTIFY_STATUS)
    void notifyRecipients(Object transactionRequest);

    @QueryMethod
    String getStatus();
}
