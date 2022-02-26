package com.truist.bankingtemporal.workflow;

import com.truist.bankingtemporal.model.BalanceRequest;
import com.truist.bankingtemporal.model.ServiceRequest;

public interface TransactionBase {
    void initDebit(ServiceRequest debitRequest);

    void initCredit(ServiceRequest creditRequest);

    void notifyRecipients(ServiceRequest transactionRequest);

    Object fetchBalance(BalanceRequest balanceRequest);
}
