package com.truist.bankingtemporal.service;

import com.truist.bankingtemporal.config.ServiceConfig;
import com.truist.bankingtemporal.model.BalanceRequest;
import com.truist.bankingtemporal.model.ServiceRequest;
import com.truist.bankingtemporal.model.response.AccountBalanceResponse;
import com.truist.bankingtemporal.model.response.CreditResponse;
import com.truist.bankingtemporal.model.response.DebitResponse;
import com.truist.bankingtemporal.model.response.StandardJsonResponse;
import com.truist.bankingtemporal.model.response.StandardJsonResponseImpl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.CompletableFuture;

@Service
@Slf4j @RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {

    private final ServiceConfig serviceConfig;
    private final RestTemplate restTemplate;

    @Override
    public boolean processDebit(ServiceRequest debitRequest) {
    	//System.out.println("Debit call started");
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
        //HttpEntity<ServiceRequest> httpEntity = new HttpEntity<>(creditRequest);
        HttpHeaders headers = new HttpHeaders();
        HttpEntity<ServiceRequest> requestEntity = new HttpEntity<>(null, headers);
        
        ResponseEntity<String> result = restTemplate.exchange(url, HttpMethod.GET, requestEntity, String.class);

        
        return result;
        //return restTemplate.exchange(url, HttpMethod.GET, responseClass).getBody();
    }
}
