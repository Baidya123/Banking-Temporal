package com.truist.bankingtemporal.workflow;


import com.truist.bankingtemporal.exception.TransactionProcessingException;
import com.truist.bankingtemporal.model.BalanceRequest;
import com.truist.bankingtemporal.model.ServiceRequest;
import com.truist.bankingtemporal.model.TransferRequest;
import com.truist.bankingtemporal.util.ActivityStubUtils;
import io.temporal.workflow.Workflow;

import java.util.ArrayList;

public class TransactionProcessorImpl implements TransactionProcessor, TransactionBase {

    private final TransactionActivity transactionActivity = ActivityStubUtils.getActivitiesStub();

    @Override
    public Object process(TransferRequest transferRequest) {

        ServiceRequest serviceRequest = createDebitRequestObj(transferRequest);

        // 1. debit sender
        initDebit(serviceRequest); // await response

        // 2. credit receiver
        initCredit(serviceRequest); // await response

        // 3. send notifications
        notifyRecipients(transferRequest); // pick email ids from DB

        // 4. fetch balance of both sender & receiver
        Object response = fetchBalance(createBalanceRequestObj(transferRequest)); // await response

        return response;
    }

    @Override
    public void initDebit(ServiceRequest debitRequest) {
        boolean success = transactionActivity.debitAccount(debitRequest);
        if (!success) {
            throw new TransactionProcessingException("Failed to debit from Source account");
        }
//        Workflow.await(() -> transactionActivity.debitAccount(debitRequest));
    }

    @Override
    public void initCredit(ServiceRequest creditRequest) {
        boolean success = transactionActivity.creditAccount(creditRequest);
        if (!success) {
            throw new TransactionProcessingException("Failed to credit to Destination account");
        }
//        Workflow.await(() -> transactionActivity.creditAccount(creditRequest));
    }

    @Override
    public void notifyRecipients(Object transactionRequest) {
        Workflow.await(() -> transactionActivity.notifyAccounts(transactionRequest));
    }

    @Override
    public Object fetchBalance(BalanceRequest balanceRequest) {
        return transactionActivity.fetchBalance(balanceRequest);
    }


    private ServiceRequest createDebitRequestObj(TransferRequest transferRequest) {
        return ServiceRequest.builder()
                .sourceAccountNumber(transferRequest.getDebitAccount().getAccountNumber())
                .destinationAccountNumber(transferRequest.getCreditAccount().getAccountNumber())
                .amount(transferRequest.getPayment().getAmount())
                .build();
    }

    private BalanceRequest createBalanceRequestObj(TransferRequest transferRequest) {
        BalanceRequest balanceRequest = new BalanceRequest();
        balanceRequest.setAccountNumbers(
                new ArrayList(2){{
                    add(transferRequest.getDebitAccount().getAccountNumber());
                    add(transferRequest.getCreditAccount().getAccountNumber());
                }}
        );
        return balanceRequest;
    }


}
