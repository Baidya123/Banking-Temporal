package com.truist.bankingtemporal.workflow;

import java.util.ArrayList;

import com.truist.bankingtemporal.model.BalanceRequest;
import com.truist.bankingtemporal.model.ServiceRequest;
import com.truist.bankingtemporal.model.TransferRequest;
import com.truist.bankingtemporal.util.ActivityStubUtils;

import io.temporal.activity.Activity;
import io.temporal.failure.ActivityFailure;
import io.temporal.workflow.Saga;
import io.temporal.workflow.Workflow;

/**
 * Workflow method implementation class to execute all the activities
 *
 */
public class TransactionProcessorImpl implements TransactionProcessor, TransactionBase {

	private final TransactionActivity transactionActivity = ActivityStubUtils.getActivitiesStub();
	private final NotificationActivity notificationActivity = ActivityStubUtils.getNotificationActivitiesStub();

	@Override
	public void process(TransferRequest transferRequest) {
		/*
		 *  Configure SAGA to run compensation activities in parallel
		 *  {@link io.temporal.workflow.Saga} implements the logic to perform compensation operations
		 */
		Saga.Options sagaOptions = new Saga.Options.Builder().setParallelCompensation(true).build();
		Saga saga = new Saga(sagaOptions);

		ServiceRequest serviceRequest = createDebitRequestObj(transferRequest);
		ServiceRequest rollbackRequest = createDebitRequestObj(transferRequest);

		try {
			// 1. initiate debit activity
			initDebit(serviceRequest, transferRequest.getNotifyEmail()); // await response

			/* to invoke debit rollback operation */
			saga.addCompensation(transactionActivity::debitRollback,
					rollbackRequest,
					Workflow.getInfo().getWorkflowId(),
					transferRequest.getNotifyEmail());

			long tmp = serviceRequest.getSourceAccountNumber();
			serviceRequest.setSourceAccountNumber(serviceRequest.getDestinationAccountNumber());
			serviceRequest.setDestinationAccountNumber(tmp);

			// 2. initiate crdeit activity
			initCredit(serviceRequest, transferRequest.getNotifyEmail()); // await response

		}catch (ActivityFailure e) {
			saga.compensate();
			throw Activity.wrap(e);
		}
		try {
			// 3. initiate notification activity
			notifyRecipients(serviceRequest, transferRequest.getNotifyEmail()); // pick email ids from DB

			// 4. notify user
			notifyUser(transferRequest.getNotifyEmail());

		} catch (ActivityFailure e) {
			throw e;
		}

	}

	@Override
	public void initDebit(ServiceRequest debitRequest, String notifyEmail) {
		transactionActivity.debitAccount(debitRequest, Workflow.getInfo().getWorkflowId(), notifyEmail);
	}

	@Override
	public void initCredit(ServiceRequest creditRequest, String notifyEmail) {
		transactionActivity.creditAccount(creditRequest, Workflow.getInfo().getWorkflowId(), notifyEmail);
	}

	@Override
	public void notifyRecipients(ServiceRequest transactionRequest, String notifyEmail) {
		notificationActivity.notifyAccounts(transactionRequest, Workflow.getInfo().getWorkflowId(), notifyEmail);
	}

	@Override
	public void notifyUser(String notifyEmail) {
		transactionActivity.notifyUser(Workflow.getInfo().getWorkflowId(), notifyEmail);
	}

	private ServiceRequest createDebitRequestObj(TransferRequest transferRequest) {
		return ServiceRequest.builder().sourceAccountNumber(transferRequest.getCreditAccount().getAccountNumber())
				.destinationAccountNumber(transferRequest.getDebitAccount().getAccountNumber())
				.amount(transferRequest.getPayment().getAmount()).build();
	}


}