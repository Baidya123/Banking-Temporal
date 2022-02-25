package com.truist.bankingtemporal.service;

import com.truist.bankingtemporal.config.ServiceConfig;
import com.truist.bankingtemporal.model.BalanceRequest;
import com.truist.bankingtemporal.model.ServiceRequest;
import com.truist.bankingtemporal.model.response.CreditResponse;
import com.truist.bankingtemporal.model.response.DebitResponse;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
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
    	DebitResponse response = (DebitResponse) requestAndGetData(serviceConfig.getDebit(), debitRequest, DebitResponse.class);
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

        CreditResponse response = (CreditResponse) requestAndGetData(serviceConfig.getCredit(), creditRequest, CreditResponse.class);
        log.debug(response.toString());
        log.debug("Credit Successful to receiver's account");

        return true;
    }

    @Override
    public void notifyAll(Object transactionRequest) {
        log.debug("Notification: Dear Mr.Sender, your account balance is $x");
        log.debug("Notification: Dear Mr.Receiver, your account balance is $x");
    }

    @Override
    public CompletableFuture<Object> fetchBalance(BalanceRequest balanceRequest) {
        return new CompletableFuture<>();
    }

    private Object requestAndGetData(String url, ServiceRequest creditRequest, Class<?> responseClass) {
        HttpEntity<ServiceRequest> httpEntity = new HttpEntity<>(creditRequest);
        return restTemplate.exchange(url, HttpMethod.POST, httpEntity, responseClass).getBody();
    }
}
