package com.truist.bankingtemporal.service;


import com.truist.bankingtemporal.model.request.MailParam;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import com.truist.bankingtemporal.config.ServiceConfig;
import com.truist.bankingtemporal.exception.NoSuchAccountException;
import com.truist.bankingtemporal.exception.TransactionProcessingException;
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
    private static final String DEFAULT_SUBJECT = "TRANSACTION STATUS";


    @Override
    public void processDebit(ServiceRequest debitRequest, String workflowId, String notifyEmail) {

        DebitResponse response = (DebitResponse) sendPOST(serviceConfig.getDebit(), debitRequest, DebitResponse.class, workflowId, notifyEmail);
        log.debug(response.toString());
        log.debug("Debit Successful from sender's account");
    }

    @Override
    public void processCredit(ServiceRequest creditRequest, String workflowId, String notifyEmail) {

        CreditResponse response = (CreditResponse) sendPOST(serviceConfig.getCredit(), creditRequest, CreditResponse.class, workflowId, notifyEmail);
        log.debug(response.toString());
        log.debug("Credit Successful to receiver's account");
    }

    @Override
    public void notifyAll(ServiceRequest notifyRequest, String workflowId, String notifyEmail) {
    	String sourceURL = serviceConfig.getBalance()+"?accountNo="+notifyRequest.getSourceAccountNumber();
    	log.debug("Source URL "+sourceURL);
    	ResponseEntity<String> responseSource = sendGET(sourceURL, workflowId, String.class);
        log.debug(responseSource.toString());
        
        String destinationURL = serviceConfig.getBalance()+"?accountNo="+notifyRequest.getDestinationAccountNumber();
        log.debug("Destination URL "+destinationURL);
        ResponseEntity<String> responseDestination = sendGET(destinationURL, workflowId, String.class);
        log.debug(responseDestination.toString());
        log.debug("Notification: Dear Mr.Sender, your account balance is $x");
        log.debug("Notification: Dear Mr.Receiver, your account balance is $x");
    }

    /**
     * Method to call the debit and credit services
     * @param url
     * @param creditRequest
     * @param responseClass
     * @return
     */
    private Object sendPOST(String url, ServiceRequest creditRequest, Class<?> responseClass, String workflowId, String notifyEmail) {
        HttpEntity<ServiceRequest> httpEntity = new HttpEntity<>(creditRequest);
       try {
        return sendPost(url, httpEntity, responseClass);
       }catch(HttpClientErrorException e) {
    	   if(404==e.getStatusCode().value() && e.getResponseBodyAsString().contains("Account not found")) {
               String errorMessage = "Dear User,\nWorkflow ID " +
                       workflowId + " failed to complete. \n\n" +
               "Reason: Account number " + creditRequest.getDestinationAccountNumber()+ "  not found";
               notifyUser(errorMessage, notifyEmail);
    		   throw new NoSuchAccountException(errorMessage);
    	   }else {
    		   throw e;
    	   }
       }catch(HttpServerErrorException e) {
    	   if(e.getResponseBodyAsString().contains("Insufficient Balance")) {
               String errorMessage = "Dear User,\nWorkflow ID " +
                       workflowId + " failed to complete.\n\n" +
                       "Reason: Insufficient Balance in Account " + creditRequest.getDestinationAccountNumber();
               notifyUser(errorMessage, notifyEmail);
               throw new TransactionProcessingException(errorMessage);
    	   }else {
    		   throw e;
    	   }
       }
    }


    /**
     * Sends a http post request
     * @param url
     * @param httpEntity
     * @param responseClass
     * @return
     */
    private Object sendPost(String url, HttpEntity<?> httpEntity, Class<?> responseClass) {
        return restTemplate.exchange(url, HttpMethod.POST, httpEntity, responseClass).getBody();
    }

    /**
     * Returns an object required to send an error report by email
     * @param message
     * @return
     */
    private HttpEntity<MailParam> getNotifiableObj(String message, String notifyEmail) {
        return getNotifiableObj(notifyEmail, DEFAULT_SUBJECT, message);
    }

    /**
     * Returns an object required to send an error report by email
     * @param to
     * @param subject
     * @param message
     * @return
     */
    private HttpEntity<MailParam> getNotifiableObj(String to, String subject, String message) {
        MailParam mailParam = MailParam.builder()
                .to(to)
                .subject(subject)
                .message(message)
                .build();
        return new HttpEntity<>(mailParam);
    }
    
    
    
    /**
     * Method to call Notify service
     * @param url
     * @param responseClass
     * @return
     */
    private ResponseEntity<String> sendGET(String url, String workflowId, Class<?> responseClass) {
       
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
	public boolean processDebitRollback(ServiceRequest debitRequest, String workflowId, String notifyEmail) {
		DebitResponse response = (DebitResponse) sendPOST(serviceConfig.getDebitRollback(), debitRequest, DebitResponse.class, workflowId, notifyEmail);
        log.debug(response.toString());
        boolean flag = false;
        if(response.getMessage().equals("Success")) {
        	flag=true;
        }
        log.debug("Debited Amount is Successfully rollbacked to sender's account");
        String errorMessage = "Dear User,\nWorkflow ID " +
                workflowId + " failed to complete. \n\n" +
        "Reason: Credit Service is Unavailable !! Debited Amount has been rolled backed to Account Number : "+debitRequest.getDestinationAccountNumber();      
        notifyUser(errorMessage, notifyEmail);
        return flag;
	}

    @Override
    public void notifyUser(String message, String notifyEmail) {

        sendPost(serviceConfig.getNotify(),
                getNotifiableObj(message, notifyEmail),
                String.class);
    }

}
