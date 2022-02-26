package com.truist.bankingtemporal.model.response;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter @ToString
public class AccountBalanceResponse {
	
	private Long id;
	private long accountBalance;
	private Long accountNumber;

}
