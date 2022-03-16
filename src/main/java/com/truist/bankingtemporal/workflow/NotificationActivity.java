package com.truist.bankingtemporal.workflow;

import com.truist.bankingtemporal.model.ServiceRequest;

import io.temporal.activity.ActivityInterface;

@ActivityInterface
public interface NotificationActivity {
	boolean notifyAccounts(ServiceRequest transactionRequest);

}
