package com.service;

import org.springframework.stereotype.Service;

import com.domain.BankAccount;
import com.domain.Credit;
import com.domain.Customer;
import com.domain.model.AccountModel;
import com.repository.BankAccountRepository;
import com.repository.CreditRepository;

import com.utils.NoSuchAccountException;

import javax.transaction.Transactional;

import java.time.Instant;
import java.util.Optional;

@Service
@Transactional
public class AccountService {

	private final BankAccountRepository bankAccountRepository;

    public AccountService( BankAccountRepository bankAccountRepository) {
       
        this.bankAccountRepository = bankAccountRepository;
    }

	public BankAccount createAccount(AccountModel model)  {
		BankAccount account=new BankAccount();
		
		Customer customer=new Customer();
		customer.setCustomerId(model.getCustomer().getCustomerId());
		
		account.setBalance(model.getBalance());
        account.setCreationDate(Instant.now());
        account.setCustomer(customer);
        account.setEmail(model.getEmail());
        return bankAccountRepository.save(account);

		
	}

}
