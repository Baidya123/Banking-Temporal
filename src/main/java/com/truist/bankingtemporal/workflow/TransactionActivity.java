package com.truist.bankingtemporal.workflow;

import com.truist.bankingtemporal.model.BalanceRequest;
import com.truist.bankingtemporal.model.ServiceRequest;

import io.temporal.activity.ActivityInterface;
import io.temporal.activity.ActivityMethod;

/**
 * This is the Activity Definition's Interface. Activities are building blocks of any Temporal
 * Workflow and contain any business logic that could perform long running computation, network
 * calls, etc.
 *
 * <p>Annotating Activity Definition methods with @ActivityMethod is optional.
 *
 * @see io.temporal.activity.ActivityInterface
 * @see io.temporal.activity.ActivityMethod
 */
@ActivityInterface
public interface TransactionActivity {
	
	/* activity methods which can be called during workflow execution */
	@ActivityMethod
    void debitAccount(ServiceRequest debitRequest);
	@ActivityMethod
    void creditAccount(ServiceRequest creditRequest);
	@ActivityMethod
    void notifyUser(String workflowId);
	@ActivityMethod
    boolean debitRollback(ServiceRequest debitRollbackRequest);
}
