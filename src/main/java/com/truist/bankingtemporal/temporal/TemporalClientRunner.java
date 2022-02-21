package com.truist.bankingtemporal.temporal;

import com.truist.bankingtemporal.service.TransactionService;
import com.truist.bankingtemporal.workflow.TransactionActivityImpl;
import com.truist.bankingtemporal.workflow.TransactionProcessor;
import com.truist.bankingtemporal.workflow.TransactionProcessorImpl;
import io.temporal.client.WorkflowClient;
import io.temporal.client.WorkflowOptions;
import io.temporal.serviceclient.WorkflowServiceStubs;
import io.temporal.worker.Worker;
import io.temporal.worker.WorkerFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
//@RequiredArgsConstructor
public class TemporalClientRunner {
    private static WorkflowClient client;

//    private final TransactionService transactionService;

    String taskQueue = "TransactionTaskQueue";

    public final void run() {
        WorkflowServiceStubs service = WorkflowServiceStubs.newInstance();
        client = WorkflowClient.newInstance(service);
        WorkerFactory factory = WorkerFactory.newInstance(client);
        Worker worker = factory.newWorker(taskQueue);

        worker.registerWorkflowImplementationTypes(TransactionProcessorImpl.class);
        worker.registerActivitiesImplementations(new TransactionActivityImpl());

        factory.start();
    }

    public final Object processTransaction(Object transactionRequest) {
        UUID id = UUID.randomUUID(); // TODO: to be updated with transactionRequest data
        TransactionProcessor transactionProcessor = getClient()
                .newWorkflowStub(
                        TransactionProcessor.class, WorkflowOptions.newBuilder()
                                .setWorkflowId(id.toString())
                                .setTaskQueue(taskQueue)
                                .build()
                );
        return transactionProcessor.process(transactionRequest);
    }

    public WorkflowClient getClient() {
        return client;
    }
}
