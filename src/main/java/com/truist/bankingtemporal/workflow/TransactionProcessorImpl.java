package com.truist.bankingtemporal.workflow;

import java.util.ArrayList;

import com.truist.bankingtemporal.model.BalanceRequest;
import com.truist.bankingtemporal.model.ServiceRequest;
import com.truist.bankingtemporal.model.TransferRequest;
import com.truist.bankingtemporal.util.ActivityStubUtils;

import io.temporal.failure.ActivityFailure;
import io.temporal.workflow.Saga;
import io.temporal.workflow.Workflow;

public class TransactionProcessorImpl implements TransactionProcessor, TransactionBase {

	private final TransactionActivity transactionActivity = ActivityStubUtils.getActivitiesStub();
	private final NotificationActivity notificationActivity = ActivityStubUtils.getNotificationActivitiesStub();

	@Override
	public Object process(TransferRequest transferRequest) {
		// Configure SAGA to run compensation activities in parallel
		Saga.Options sagaOptions = new Saga.Options.Builder().setParallelCompensation(true).build();
		Saga saga = new Saga(sagaOptions);

		ServiceRequest serviceRequest = createDebitRequestObj(transferRequest);
		ServiceRequest rollbackRequest = createDebitRequestObj(transferRequest);

		try {
			// 1. debit sender
			initDebit(serviceRequest); // await response

			/* to invoke debit rollback operation */
			saga.addCompensation(transactionActivity::debitRollback, rollbackRequest);

			long tmp = serviceRequest.getSourceAccountNumber();
			serviceRequest.setSourceAccountNumber(serviceRequest.getDestinationAccountNumber());
			serviceRequest.setDestinationAccountNumber(tmp);

			// 2. credit receiver
			initCredit(serviceRequest); // await response

		} catch (ActivityFailure e) {
			saga.compensate();
			throw e;
		}
		try {
			// 3. send notifications
			notifyRecipients(serviceRequest); // pick email ids from DB

			// 4. fetch balance of both sender & receiver
			Object response = fetchBalance(createBalanceRequestObj(transferRequest)); // await response

			return response;

		} catch (ActivityFailure e) {
			throw e;
		}

	}

	@Override
	public void initDebit(ServiceRequest debitRequest) {
		boolean success = transactionActivity.debitAccount(debitRequest);
		if (!success) {
			// throw new TransactionProcessingException("Failed to debit from Source
			// account");
		}
//        Workflow.await(() -> transactionActivity.debitAccount(debitRequest));
	}

	@Override
	public void initCredit(ServiceRequest creditRequest) {
		boolean success = transactionActivity.creditAccount(creditRequest);
		if (!success) {
			// throw new TransactionProcessingException("Failed to credit to Destination
			// account");
		}
//        Workflow.await(() -> transactionActivity.creditAccount(creditRequest));
	}

	@Override
	public void notifyRecipients(ServiceRequest transactionRequest) {
		Workflow.await(() -> notificationActivity.notifyAccounts(transactionRequest));
	}

	@Override
	public Object fetchBalance(BalanceRequest balanceRequest) {
		return transactionActivity.fetchBalance(balanceRequest);
	}

	private ServiceRequest createDebitRequestObj(TransferRequest transferRequest) {
		return ServiceRequest.builder().sourceAccountNumber(transferRequest.getCreditAccount().getAccountNumber())
				.destinationAccountNumber(transferRequest.getDebitAccount().getAccountNumber())
				.amount(transferRequest.getPayment().getAmount()).build();
	}

	private BalanceRequest createBalanceRequestObj(TransferRequest transferRequest) {
		BalanceRequest balanceRequest = new BalanceRequest();
		balanceRequest.setAccountNumbers(new ArrayList(2) {
			{
				add(transferRequest.getDebitAccount().getAccountNumber());
				add(transferRequest.getCreditAccount().getAccountNumber());
			}
		});
		return balanceRequest;
	}

}