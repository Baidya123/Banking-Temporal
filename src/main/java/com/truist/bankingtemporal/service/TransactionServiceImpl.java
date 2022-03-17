package com.truist.bankingtemporal.service;

import java.util.concurrent.CompletableFuture;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.truist.bankingtemporal.config.ServiceConfig;
import com.truist.bankingtemporal.model.BalanceRequest;
import com.truist.bankingtemporal.model.ServiceRequest;
import com.truist.bankingtemporal.model.response.CreditResponse;
import com.truist.bankingtemporal.model.response.DebitResponse;

import io.temporal.workflow.Functions.Proc;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j @RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {

    private final ServiceConfig serviceConfig;
    private final RestTemplate restTemplate;

    @Override
    public boolean processDebit(ServiceRequest debitRequest) {
    	
    	DebitResponse response = (DebitResponse) postRequestAndGetData(serviceConfig.getDebit(), debitRequest, DebitResponse.class);
        log.debug(response.toString());
        boolean flag = false;
        
        if(response.getMessage().equals("Success")) {
        	flag=true;
        }
        log.debug("Debit Successful from sender's account");
        return flag;
    }

    @Override
    public boolean processCredit(ServiceRequest creditRequest) {

        CreditResponse response = (CreditResponse) postRequestAndGetData(serviceConfig.getCredit(), creditRequest, CreditResponse.class);
        log.debug(response.toString());
        log.debug("Credit Successful to receiver's account");

        return true;
    }

    @Override
    public void notifyAll(ServiceRequest notifyRequest) {
    	String sourceURL = serviceConfig.getNotify()+"?accountNo="+notifyRequest.getSourceAccountNumber();
    	log.debug("Source URL "+sourceURL);
    	ResponseEntity<String> responseSource = getRequestAndGetData(sourceURL, notifyRequest, String.class);
        log.debug(responseSource.toString());
        
        String destinationURL = serviceConfig.getNotify()+"?accountNo="+notifyRequest.getDestinationAccountNumber();
        log.debug("Destination URL "+destinationURL);
        ResponseEntity<String> responseDestination = getRequestAndGetData(sourceURL, notifyRequest, String.class);
        log.debug(responseDestination.toString());
        log.debug("Notification: Dear Mr.Sender, your account balance is $x");
        log.debug("Notification: Dear Mr.Receiver, your account balance is $x");
    }

    @Override
    public CompletableFuture<Object> fetchBalance(BalanceRequest balanceRequest) {
        return new CompletableFuture<>();
    }

    private Object postRequestAndGetData(String url, ServiceRequest creditRequest, Class<?> responseClass) {
        HttpEntity<ServiceRequest> httpEntity = new HttpEntity<>(creditRequest);
        return restTemplate.exchange(url, HttpMethod.POST, httpEntity, responseClass).getBody();
    }
    
    private ResponseEntity<String> getRequestAndGetData(String url, ServiceRequest creditRequest, Class<?> responseClass) {
       
        HttpHeaders headers = new HttpHeaders();
        HttpEntity<ServiceRequest> requestEntity = new HttpEntity<>(null, headers);
        
        ResponseEntity<String> result = restTemplate.exchange(url, HttpMethod.GET, requestEntity, String.class);

        
        return result;
        
    }

    /**
     * To rollback the debited amount incase Credit or Notify service fails to process
     */
	@Override
	public boolean processDebitRollback(ServiceRequest debitRequest) {
		DebitResponse response = (DebitResponse) postRequestAndGetData(serviceConfig.getDebitRollback(), debitRequest, DebitResponse.class);
        log.debug(response.toString());
        boolean flag = false;
        if(response.getMessage().equals("Success")) {
        	flag=true;
        }
        log.debug("Debited Amount is Successfully rollbacked to sender's account");
        
        return flag;
	}
}
