package com.truist.bankingtemporal.controller;

import com.truist.bankingtemporal.model.TransferRequest;
import com.truist.bankingtemporal.temporal.TemporalClientRunner;
import lombok.RequiredArgsConstructor;
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

    @PostMapping("/fund-transfer")
    private ResponseEntity<?> initTransfer(@RequestBody TransferRequest transactionRequest) {
        return ResponseEntity.ok(temporalClientRunner.processTransaction(transactionRequest));
    }
}
