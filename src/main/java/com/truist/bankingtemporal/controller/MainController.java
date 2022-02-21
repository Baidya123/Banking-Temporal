package com.truist.bankingtemporal.controller;

import com.truist.bankingtemporal.temporal.TemporalClientRunner;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/temporal")
@RequiredArgsConstructor
public class MainController {

    private final TemporalClientRunner temporalClientRunner;

    @PostMapping("/init-transfer")
    private ResponseEntity<?> initTransfer(@RequestBody Object transactionRequest) {
        return ResponseEntity.ok(temporalClientRunner.processTransaction(transactionRequest));
    }
}
