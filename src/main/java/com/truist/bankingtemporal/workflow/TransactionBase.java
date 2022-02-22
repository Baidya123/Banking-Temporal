package com.truist.bankingtemporal.workflow;

public interface TransactionBase {
    void initDebit(Object transactionRequest);

    void initCredit(Object transactionRequest);

    void notifyRecipients(Object transactionRequest);

    Object fetchBalance(Object transactionRequest);
}
