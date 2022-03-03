package com.truist.bankingtemporal.util;

import com.truist.bankingtemporal.workflow.NotificationActivity;
import com.truist.bankingtemporal.workflow.TransactionActivity;
import com.truist.bankingtemporal.workflow.TransactionActivityImpl;
import io.temporal.activity.ActivityOptions;
import io.temporal.common.RetryOptions;
import io.temporal.workflow.Workflow;

import java.time.Duration;

public class ActivityStubUtils {
    public static TransactionActivity getActivitiesStub() {
        return Workflow.newActivityStub(
                TransactionActivity.class,
                ActivityOptions.newBuilder()
                        .setScheduleToCloseTimeout(Duration.ofSeconds(60)) // retry timeout
                        .setRetryOptions(RetryOptions.newBuilder()
                                .setBackoffCoefficient(1)
                                .setMaximumAttempts(3)
                                .build())
                        .build());
    }
    
    /**
     * 
     * @return
     */
    public static NotificationActivity getNotificationActivitiesStub() {
        return Workflow.newActivityStub(
        		NotificationActivity.class,
                ActivityOptions.newBuilder()
                        .setScheduleToCloseTimeout(Duration.ofSeconds(120)) // retry timeout
                        .setRetryOptions(RetryOptions.newBuilder()
                                .setBackoffCoefficient(2) //
                                .setMaximumAttempts(5)
                                .setMaximumInterval(Duration.ofSeconds(20))
                                .build())
                        .build());
    }
}
