package com.truist.bankingtemporal.temporal;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import com.truist.bankingtemporal.model.TransferRequest;
import com.truist.bankingtemporal.workflow.NotificationActivity;
import com.truist.bankingtemporal.workflow.TransactionActivity;
import com.truist.bankingtemporal.workflow.TransactionProcessor;
import com.truist.bankingtemporal.workflow.TransactionProcessorImpl;

import io.temporal.api.common.v1.WorkflowExecution;
import io.temporal.api.enums.v1.WorkflowIdReusePolicy;
import io.temporal.client.WorkflowClient;
import io.temporal.client.WorkflowOptions;
import io.temporal.common.RetryOptions;
import io.temporal.serviceclient.WorkflowServiceStubs;
import io.temporal.worker.Worker;
import io.temporal.worker.WorkerFactory;
import lombok.RequiredArgsConstructor;

/**
 * Main Application runner class to initiate the temporal workflow.
 *
 */

@Component
@RequiredArgsConstructor
public class TemporalClientRunner implements ApplicationRunner {
	private static WorkflowClient client;

	private final TransactionActivity transactionActivity;
	private final NotificationActivity notificationActivity;

	String taskQueue = "TransactionTaskQueue";

	public final Object processTransaction(TransferRequest transactionRequest) {

		// WorkflowServiceStubs is a gRPC stubs wrapper that talks to the local Docker
		// instance of the Temporal server.
		WorkflowServiceStubs service = WorkflowServiceStubs.newInstance();

		/*
		 * Workflow Retry Options setScheduleToCloseTimeout :: Timeout options specify
		 * when to automatically timeout Activities if the process is taking too long.
		 * 
		 * setMaximumAttempts :: By default the maximum number of retry attempts is set
		 * to "unlimited" however you can change it by adding the "setMaximumAttempts"
		 * option to the retry options.
		 * 
		 * setBackoffCoefficient :: Coefficient used to calculate the next retry
		 * interval. The next retry interval is previous interval multiplied by this
		 * coefficient. Must be 1 or larger. Default is 2.0.
		 * 
		 * setInitialInterval :: Interval of the first retry. If coefficient is 1.0 then
		 * it is used for all retries.
		 * 
		 * setDoNotRetry :: List of application failures types to not retry.
		 * 
		 * setMaximumInterval :: Maximum interval between retries. Exponential backoff
		 * leads to interval increase. This value is the cap of the increase.
		 */
		RetryOptions retyOptions = RetryOptions.newBuilder().setBackoffCoefficient(1).setMaximumAttempts(1).build();
		/*
		 * setTaskQueue :: Task queue to use for workflow tasks. It should match a task
		 * queue specified when creating a {@link io.temporal.worker.Worker} that hosts
		 * the workflow code.
		 * 
		 * setWorkflowId :: A WorkflowId prevents this it from having duplicate
		 * instances, remove it to duplicate.
		 * 
		 * setWorkflowExecutionTimeout :: The time after which workflow execution (which
		 * includes run retries and continue as new) is automatically terminated by
		 * Temporal service.
		 * 
		 * setWorkflowTaskTimeout :: Maximum execution time of a single Workflow Task.
		 * Default is 10 seconds. Maximum value allowed by the Temporal Server is 1
		 * minute.
		 */

		WorkflowOptions options = WorkflowOptions.newBuilder().setTaskQueue(taskQueue)
				.setWorkflowIdReusePolicy(WorkflowIdReusePolicy.WORKFLOW_ID_REUSE_POLICY_ALLOW_DUPLICATE_FAILED_ONLY)
				.setRetryOptions(retyOptions).build();

		// WorkflowClient can be used to start, signal, query, cancel, and terminate
		// Workflows.
		WorkflowClient client = WorkflowClient.newInstance(service);

		// WorkflowStubs enable calls to methods as if the Workflow object is local, but
		// actually perform an RPC.
		TransactionProcessor workflow = client.newWorkflowStub(TransactionProcessor.class, options);

		// WorkflowClient.start :: Returns as soon as the workflow is scheduled to start
		// on the server.
		WorkflowExecution we = WorkflowClient.start(workflow::process, transactionRequest);

		System.out.printf("\nTransfer of from account to account is processing\n", transactionRequest.toString());
		System.out.printf("\nWorkflowID: %s RunID: %s", we.getWorkflowId(), we.getRunId());

		return "Workflow Initiated !! \n Workflow ID:" + we.getWorkflowId() + " \n Run ID:" + we.getRunId();
	}

	@Override
	public void run(ApplicationArguments args) {
		WorkflowServiceStubs service = WorkflowServiceStubs.newInstance();
		client = WorkflowClient.newInstance(service);
		// Create a Worker factory that can be used to create Workers that poll specific
		// Task Queues.
		WorkerFactory factory = WorkerFactory.newInstance(client);

		// Define the workflow worker.
		// Workflow workers listen to a defined task queue and process workflows and
		// activities.
		Worker worker = factory.newWorker(taskQueue);

		// Register Workflow implementation classes
		worker.registerWorkflowImplementationTypes(TransactionProcessorImpl.class);
		worker.registerActivitiesImplementations(transactionActivity);
		worker.registerActivitiesImplementations(notificationActivity);
		// Start polling the Task Queue.
		factory.start();
	}
}
