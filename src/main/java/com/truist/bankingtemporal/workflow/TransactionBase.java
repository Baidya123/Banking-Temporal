package com.truist.bankingtemporal.workflow;

import com.truist.bankingtemporal.model.BalanceRequest;
import com.truist.bankingtemporal.model.ServiceRequest;

public interface TransactionBase {
    void initDebit(ServiceRequest debitRequest, String notifyEmail);
    void initCredit(ServiceRequest creditRequest, String notifyEmail);
    void notifyRecipients(ServiceRequest transactionRequest, String notifyEmail);
    void notifyUser(String notifyEmail);
}
