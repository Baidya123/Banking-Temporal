package com.truist.bankingtemporal.workflow;

import com.truist.bankingtemporal.model.BalanceRequest;
import com.truist.bankingtemporal.model.ServiceRequest;

import io.temporal.activity.ActivityInterface;

@ActivityInterface
public interface TransactionActivity {
    boolean debitAccount(ServiceRequest debitRequest);
    boolean creditAccount(ServiceRequest creditRequest);
    boolean fetchBalance(BalanceRequest balanceRequest);
    boolean debitRollback(ServiceRequest debitRollbackRequest);
}
