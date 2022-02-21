package com.truist.bankingtemporal.workflow;

import io.temporal.activity.ActivityInterface;

@ActivityInterface
public interface TransactionActivity {
    boolean debitAccount(Object transactionRequest);
    boolean creditAccount(Object transactionRequest);
    boolean notifyAccounts(Object transactionRequest);
    boolean fetchBalance(Object transactionRequest);
}
