package com.truist.bankingtemporal.workflow;

import com.truist.bankingtemporal.model.TransferRequest;

import io.temporal.workflow.WorkflowInterface;
import io.temporal.workflow.WorkflowMethod;

@WorkflowInterface
public interface TransactionProcessor {
   @WorkflowMethod
   Object process(TransferRequest transactionRequest);

//    @SignalMethod
//    void anymehod();

}
