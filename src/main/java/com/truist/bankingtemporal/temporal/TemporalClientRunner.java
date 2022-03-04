package com.truist.bankingtemporal.temporal;

import java.util.UUID;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import com.truist.bankingtemporal.model.TransferRequest;
import com.truist.bankingtemporal.workflow.NotificationActivity;
import com.truist.bankingtemporal.workflow.TransactionActivity;
import com.truist.bankingtemporal.workflow.TransactionProcessor;
import com.truist.bankingtemporal.workflow.TransactionProcessorImpl;

import io.temporal.client.WorkflowClient;
import io.temporal.client.WorkflowException;
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

		UUID id = UUID.randomUUID(); // TODO: to be updated with transactionRequest data
		TransactionProcessor transactionProcessor = getClient().newWorkflowStub(TransactionProcessor.class,
				WorkflowOptions.newBuilder().setWorkflowId(id.toString()).setTaskQueue(taskQueue)
				//.setWorkflowExecutionTimeout(Duration.ofSeconds(20))
						.setRetryOptions(
								RetryOptions.newBuilder().setBackoffCoefficient(1).setMaximumAttempts(1).build())
						.build());
		try {
			return transactionProcessor.process(transactionRequest);
		} catch (WorkflowException e) {
			throw e;
		}

	}

	public WorkflowClient getClient() {
		return client;
	}

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
