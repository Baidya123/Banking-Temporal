package com.truist.bankingtemporal.workflow;

import io.temporal.workflow.WorkflowInterface;
import io.temporal.workflow.WorkflowMethod;

@WorkflowInterface
public interface TransactionProcessor {
    @WorkflowMethod
    Object process(Object transactionRequest);

}
