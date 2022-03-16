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

@Component
@RequiredArgsConstructor
public class TemporalClientRunner implements ApplicationRunner {
	private static WorkflowClient client;

	private final TransactionActivity transactionActivity;
	private final NotificationActivity notificationActivity;

	String taskQueue = "TransactionTaskQueue";

	public final Object processTransaction(TransferRequest transactionRequest) {
		
		// WorkflowServiceStubs is a gRPC stubs wrapper that talks to the local Docker instance of the Temporal server.
       WorkflowServiceStubs service = WorkflowServiceStubs.newInstance();
       
        WorkflowOptions options = WorkflowOptions.newBuilder()
                .setTaskQueue(taskQueue)
                // A WorkflowId prevents this it from having duplicate instances, remove it to duplicate.
               // .setWorkflowId()
               // .setWorkflowExecutionTimeout(Duration.ofSeconds(20))
                .setWorkflowIdReusePolicy(WorkflowIdReusePolicy.WORKFLOW_ID_REUSE_POLICY_ALLOW_DUPLICATE_FAILED_ONLY)
                .setRetryOptions(
						RetryOptions.newBuilder().setBackoffCoefficient(1).setMaximumAttempts(3).build())
                .build();
        // WorkflowClient can be used to start, signal, query, cancel, and terminate Workflows.
        WorkflowClient client = WorkflowClient.newInstance(service);
        // WorkflowStubs enable calls to methods as if the Workflow object is local, but actually perform an RPC.
        TransactionProcessor workflow = client.newWorkflowStub(TransactionProcessor.class, options);

     // Returns as soon as the workflow is scheduled to start on the server.
        WorkflowExecution we = WorkflowClient.start(workflow::process, transactionRequest);
        System.out.printf("\nTransfer of from account to account is processing\n", transactionRequest.toString());
        System.out.printf("\nWorkflowID: %s RunID: %s", we.getWorkflowId(), we.getRunId());
        
         
        
        return "Workflow Initiated !! \n Workflow ID:"+we.getWorkflowId()+" \n Run ID:"+we.getRunId();
	/*	UUID id = UUID.randomUUID(); // TODO: to be updated with transactionRequest data
		TransactionProcessor transactionProcessor = getClient().newWorkflowStub(TransactionProcessor.class,
				WorkflowOptions.newBuilder().setWorkflowId(id.toString()).setTaskQueue(taskQueue)
				//.setWorkflowExecutionTimeout(Duration.ofSeconds(20))
						.setRetryOptions(
								RetryOptions.newBuilder().setBackoffCoefficient(1).setMaximumAttempts(1).build())
						.build());
		try {
			 transactionProcessor.process(transactionRequest);
			 
			 return "Workflow Initiated!!";
		} catch (WorkflowException e) {
			throw e;
		}
*/
	}

	/*public WorkflowClient getClient() {
		return client;
	} */

	@Override
	public void run(ApplicationArguments args) {
		WorkflowServiceStubs service = WorkflowServiceStubs.newInstance();
		client = WorkflowClient.newInstance(service);
		WorkerFactory factory = WorkerFactory.newInstance(client);
		Worker worker = factory.newWorker(taskQueue);

		worker.registerWorkflowImplementationTypes(TransactionProcessorImpl.class);
		worker.registerActivitiesImplementations(transactionActivity);
		worker.registerActivitiesImplementations(notificationActivity);

		factory.start();
	}
}
