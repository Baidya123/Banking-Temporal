package com.truist.bankingtemporal.workflow;

import com.truist.bankingtemporal.model.BalanceRequest;
import com.truist.bankingtemporal.model.ServiceRequest;
import com.truist.bankingtemporal.model.TransferRequest;

import io.temporal.activity.ActivityInterface;
import io.temporal.activity.ActivityMethod;
import io.temporal.workflow.WorkflowMethod;

public interface TransactionBase {
    void initDebit(ServiceRequest debitRequest);
    void initCredit(ServiceRequest creditRequest);
    void notifyRecipients(ServiceRequest transactionRequest);
    Object fetchBalance(BalanceRequest balanceRequest);
}
