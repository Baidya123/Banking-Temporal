package com.truist.bankingtemporal.workflow;

import org.springframework.stereotype.Component;

import com.truist.bankingtemporal.model.ServiceRequest;
import com.truist.bankingtemporal.service.TransactionService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Implementation of our workflow activity interface. It overwrites our defined
 * activity methods.
 */

@Component
@Slf4j
@RequiredArgsConstructor
public class TransactionActivityImpl implements TransactionActivity {

	private static final String DEBIT_STATUS = "debiting sender account";
	private static final String CREDIT_STATUS = "crediting receiver account";
	private static final String NOTIFY_STATUS = "notifying dashboard user";
	private static final String DEBIT_ROLLBACK_STATUS = "reverting debited amount";

	private final TransactionService transactionService;

	@Override
	public void debitAccount(ServiceRequest debitRequest, String workflowId, String notifyEmail) {
		log.debug(DEBIT_STATUS);
		transactionService.processDebit(debitRequest, workflowId, notifyEmail);

	}

	@Override
	public void creditAccount(ServiceRequest creditRequest, String workflowId, String notifyEmail) {
		log.debug(CREDIT_STATUS);
		transactionService.processCredit(creditRequest, workflowId, notifyEmail);
	}

	@Override
	public void notifyUser(String workflowId, String notifyEmail) {
		log.debug(NOTIFY_STATUS);
		transactionService.notifyUser("Dear User,\nWorkflow ID " + workflowId + " has been successfully completed.", notifyEmail);
	}

	@Override
	public boolean debitRollback(ServiceRequest debitRollbackRequest, String workflowId, String notifyEmail) {
		log.debug(DEBIT_ROLLBACK_STATUS);
		return transactionService.processDebitRollback(debitRollbackRequest, workflowId, notifyEmail);
	}
}
