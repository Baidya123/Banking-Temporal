package com.truist.bankingtemporal.workflow;

import com.truist.bankingtemporal.model.BalanceRequest;
import com.truist.bankingtemporal.model.ServiceRequest;
import com.truist.bankingtemporal.model.TransferRequest;
import io.temporal.activity.ActivityInterface;
import io.temporal.workflow.Functions.Proc;

@ActivityInterface
public interface TransactionActivity {
    boolean debitAccount(ServiceRequest debitRequest);
    boolean creditAccount(ServiceRequest creditRequest);
    boolean notifyAccounts(ServiceRequest transactionRequest);
    boolean fetchBalance(BalanceRequest balanceRequest);
    boolean debitRollback(ServiceRequest debitRollbackRequest);
}
