package com.truist.bankingtemporal.util;

import java.time.Duration;

import org.springframework.web.client.HttpServerErrorException.InternalServerError;

import com.truist.bankingtemporal.workflow.NotificationActivity;
import com.truist.bankingtemporal.workflow.TransactionActivity;

import io.temporal.activity.ActivityOptions;
import io.temporal.common.RetryOptions;
import io.temporal.workflow.Workflow;

public class ActivityStubUtils {
    public static TransactionActivity getActivitiesStub() {
    	
    	/*
    	 * setScheduleToCloseTimeout :: Timeout options specify when to automatically timeout Activities if the process is taking too long.
    	 * 
    	 * setMaximumAttempts :: By default the maximum number of retry attempts is set to "unlimited" 
    	 * however you can change it by adding the "setMaximumAttempts" option to the retry options.
    	 * 
    	 * setBackoffCoefficient :: Coefficient used to calculate the next retry interval. The next retry interval is previous
    	 * interval multiplied by this coefficient. Must be 1 or larger. Default is 2.0.
    	 * 
    	 * setInitialInterval :: Interval of the first retry. If coefficient is 1.0 then it is used for all retries.
    	 * 
    	 * setDoNotRetry :: List of application failures types to not retry.
    	 * 
    	 * setMaximumInterval :: Maximum interval between retries. Exponential backoff leads to interval increase. 
    	 * This value is the cap of the increase.
    	 */
    	
        return Workflow.newActivityStub(
                TransactionActivity.class,
                ActivityOptions.newBuilder()
                        .setScheduleToCloseTimeout(Duration.ofMinutes(5))
                        .setRetryOptions(RetryOptions.newBuilder()
                        		//.setInitialInterval(Duration.ofSeconds(1))
                        		//.setDoNotRetry(InternalServerError.class.getName())
                                .setBackoffCoefficient(2)
                                .setMaximumAttempts(7)
                                .build())
                        .build());
    }
    
    public static NotificationActivity getNotificationActivitiesStub() {
        return Workflow.newActivityStub(
        		NotificationActivity.class,
                ActivityOptions.newBuilder()
                        .setScheduleToCloseTimeout(Duration.ofSeconds(120)) 
                        .setRetryOptions(RetryOptions.newBuilder()
                                .setBackoffCoefficient(2)  
                                .setMaximumAttempts(10)
                                .setMaximumInterval(Duration.ofSeconds(20))
                                .build())
                        .build());
    }
}
