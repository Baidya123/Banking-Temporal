package com.example.DebitMicroservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.example.DebitMicroservice.model.Debit;
import com.example.DebitMicroservice.model.WithdrawalModel;
import com.example.DebitMicroservice.service.WithdrawalService;
import com.example.DebitMicroservice.utils.NoSuchAccountException;
import com.example.DebitMicroservice.utils.ResponseModel;

@RestController
@RequestMapping("/api/")
public class DebitServiceController {
	
	private final WithdrawalService withdrawalService;

    public DebitServiceController(WithdrawalService withdrawalService) {
		super();
		this.withdrawalService = withdrawalService;
	}

	@PostMapping(value = "accounts/withdrawal")
    public ResponseEntity<ResponseModel> deposit(@RequestBody WithdrawalModel withdrawalModel)  {
    	ResponseModel responseModel=new ResponseModel();
    	Debit debit;
		try {
			debit = withdrawalService.doWithdrawal(withdrawalModel);
			responseModel.setData(debit);
	    	responseModel.setMessage("Success");
	    	
	    	return new ResponseEntity<ResponseModel>(responseModel, HttpStatus.OK);
		}
		catch (NoSuchAccountException e) {
			throw new ResponseStatusException(
			           HttpStatus.NOT_FOUND, "Account not found",e);
		} 
		catch (Exception e) {
			throw new ResponseStatusException(
			           HttpStatus.INTERNAL_SERVER_ERROR, "Something went wrong!",e);
		}
    }
}
