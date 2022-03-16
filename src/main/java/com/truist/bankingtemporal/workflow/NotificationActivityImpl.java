package com.truist.bankingtemporal.workflow;

import org.springframework.stereotype.Component;

import com.truist.bankingtemporal.model.ServiceRequest;
import com.truist.bankingtemporal.service.TransactionService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
@RequiredArgsConstructor
public class NotificationActivityImpl implements NotificationActivity{
	
	private static final String NOTIFY_STATUS = "notifying all";
	
	private final TransactionService transactionService;
	
	@Override
    public boolean notifyAccounts(ServiceRequest transactionRequest) {
        log.debug(NOTIFY_STATUS);
        transactionService.notifyAll(transactionRequest);
        return true;
    }


}
