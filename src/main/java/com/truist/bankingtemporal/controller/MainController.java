package com.truist.bankingtemporal.controller;

import com.truist.bankingtemporal.model.TransferRequest;
import com.truist.bankingtemporal.service.TransactionService;
import com.truist.bankingtemporal.temporal.TemporalClientRunner;
import com.truist.bankingtemporal.workflow.TransactionProcessor;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/account")
@RequiredArgsConstructor
public class MainController {


    private final TemporalClientRunner temporalClientRunner;
    //private final TransactionProcessor transactionProcessor;

    @Autowired
    private  TransactionService transactionService;

    @PostMapping("/fund-transfer")
    private ResponseEntity<?> initTransfer(@RequestBody TransferRequest transactionRequest) {
        transactionService.setNotifyEmail(transactionRequest.getNotifyEmail());
        return ResponseEntity.ok(temporalClientRunner.processTransaction(transactionRequest));
    }
}
