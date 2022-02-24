package com.truist.bankingtemporal.temporal;

import com.truist.bankingtemporal.config.ServiceConfig;
import com.truist.bankingtemporal.model.TransferRequest;
import com.truist.bankingtemporal.service.TransactionService;
import com.truist.bankingtemporal.service.TransactionServiceImpl;
import com.truist.bankingtemporal.workflow.TransactionActivity;
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
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class TemporalClientRunner implements ApplicationRunner {
    private static WorkflowClient client;

    private final TransactionActivity transactionActivity;

    String taskQueue = "TransactionTaskQueue";

    public final Object processTransaction(TransferRequest transactionRequest) {
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


    @Override
    public void run(ApplicationArguments args) {
        WorkflowServiceStubs service = WorkflowServiceStubs.newInstance();
        client = WorkflowClient.newInstance(service);
        WorkerFactory factory = WorkerFactory.newInstance(client);
        Worker worker = factory.newWorker(taskQueue);

        worker.registerWorkflowImplementationTypes(TransactionProcessorImpl.class);
        worker.registerActivitiesImplementations(transactionActivity);

        factory.start();
    }
}
