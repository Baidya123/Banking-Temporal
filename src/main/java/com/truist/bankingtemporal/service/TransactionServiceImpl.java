package com.truist.bankingtemporal.service;

import java.util.concurrent.CompletableFuture;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

import com.truist.bankingtemporal.config.ServiceConfig;
import com.truist.bankingtemporal.exception.NoSuchAccountException;
import com.truist.bankingtemporal.exception.TransactionProcessingException;
import com.truist.bankingtemporal.model.BalanceRequest;
import com.truist.bankingtemporal.model.ServiceRequest;
import com.truist.bankingtemporal.model.response.CreditResponse;
import com.truist.bankingtemporal.model.response.DebitResponse;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Implementation done to call the debit, credit and notifications services
 * via rest template
 *
 */

@Service
@Slf4j @RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {

    private final ServiceConfig serviceConfig;
    private final RestTemplate restTemplate;

    @Override
    public void processDebit(ServiceRequest debitRequest) {
    	try {
    	DebitResponse response = (DebitResponse) postRequestAndGetData(serviceConfig.getDebit(), debitRequest, DebitResponse.class);
        log.debug(response.toString());
        log.debug("Debit Successful from sender's account");
    	} catch (NoSuchAccountException e) {
    		throw e;
    	}
    }

    @Override
    public void processCredit(ServiceRequest creditRequest) {

        CreditResponse response = (CreditResponse) postRequestAndGetData(serviceConfig.getCredit(), creditRequest, CreditResponse.class);
        log.debug(response.toString());
        log.debug("Credit Successful to receiver's account");
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

    /**
     * Method to call the debit and credit services
     * @param url
     * @param creditRequest
     * @param responseClass
     * @return
     */
    private Object postRequestAndGetData(String url, ServiceRequest creditRequest, Class<?> responseClass) {
        HttpEntity<ServiceRequest> httpEntity = new HttpEntity<>(creditRequest);
       try {
        return restTemplate.exchange(url, HttpMethod.POST, httpEntity, responseClass).getBody();
       }catch(HttpClientErrorException e) {
    	   if(404==e.getStatusCode().value()) {
    		   throw new NoSuchAccountException("Account not found");
    	   }else {
    		   throw e;
    	   }
       }catch(HttpServerErrorException e) {
    	   if(500==e.getStatusCode().value()) {
    		   throw new TransactionProcessingException("Insufficient Balance");
    	   }else {
    		   throw e;
    	   }
       }
    }
    
    private ResponseEntity<String> getRequestAndGetData(String url, ServiceRequest creditRequest, Class<?> responseClass) {
       
        HttpHeaders headers = new HttpHeaders();
        HttpEntity<ServiceRequest> requestEntity = new HttpEntity<>(null, headers);
        try {
        ResponseEntity<String> result = restTemplate.exchange(url, HttpMethod.GET, requestEntity, String.class);
        return result;
        }catch(HttpClientErrorException e) {
        	throw new NoSuchAccountException();
        }        
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
