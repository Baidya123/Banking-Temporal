package com.truist.bankingtemporal.workflow;

import com.truist.bankingtemporal.model.TransferRequest;
import io.temporal.workflow.WorkflowInterface;
import io.temporal.workflow.WorkflowMethod;

/**
 * The Workflow Definition's Interface must contain one method annotated with @WorkflowMethod.
 *
 * <p>Workflow Definitions should not contain any heavyweight computations, non-deterministic
 * code, network calls, database operations, etc. Those things should be handled by the
 * Activities.
 *
 * @see io.temporal.workflow.WorkflowInterface
 * @see io.temporal.workflow.WorkflowMethod
 */
@WorkflowInterface
public interface TransactionProcessor {
	/**
	 * This is the method that is executed when the Workflow Execution is started.
	 * The Workflow Execution completes when this method finishes execution.
	 */
	@WorkflowMethod
	void process(TransferRequest transactionRequest);

}
