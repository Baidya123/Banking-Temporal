package com.truist.bankingtemporal.workflow;

import com.truist.bankingtemporal.model.ServiceRequest;

import io.temporal.activity.ActivityInterface;

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
public interface NotificationActivity {
	boolean notifyAccounts(ServiceRequest transactionRequest, String workflowId, String notifyEmail);

}
