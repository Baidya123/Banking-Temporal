package com.truist.bankingtemporal.workflow;

import org.springframework.stereotype.Component;

import com.truist.bankingtemporal.model.BalanceRequest;
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
	private static final String BALANCE_STATUS = "finishing and fetching balance";
	private static final String DEBIT_ROLLBACK_STATUS = "reverting debited amount";

	private final TransactionService transactionService;

	@Override
	public void debitAccount(ServiceRequest debitRequest) {
		log.debug(DEBIT_STATUS);
		transactionService.processDebit(debitRequest);

	}

	@Override
	public void creditAccount(ServiceRequest creditRequest) {
		log.debug(CREDIT_STATUS);
		transactionService.processCredit(creditRequest);
	}

	@Override
	public boolean fetchBalance(BalanceRequest balanceRequest) {
		log.debug(BALANCE_STATUS);
		transactionService.fetchBalance(balanceRequest).complete(true);
		return true;
	}

	@Override
	public boolean debitRollback(ServiceRequest debitRollbackRequest) {
		log.debug(DEBIT_ROLLBACK_STATUS);
		return transactionService.processDebitRollback(debitRollbackRequest);
	}
}
