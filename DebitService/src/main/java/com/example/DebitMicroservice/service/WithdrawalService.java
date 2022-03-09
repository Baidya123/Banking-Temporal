package com.example.DebitMicroservice.service;

import java.time.Instant;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.DebitMicroservice.model.Account;
import com.example.DebitMicroservice.model.Debit;
import com.example.DebitMicroservice.model.WithdrawalModel;
import com.example.DebitMicroservice.repository.AccountRepository;
import com.example.DebitMicroservice.repository.DebitRepository;
import com.example.DebitMicroservice.utils.NoSuchAccountException;


@Service
@Transactional
public class WithdrawalService {

	@Autowired
    private final AccountRepository accountRepository;
	
	@Autowired
    private final DebitRepository debitRepository;

    public WithdrawalService(AccountRepository accountRepository, DebitRepository debitRepository) {
        this.accountRepository = accountRepository;
		this.debitRepository = debitRepository;
    }

	public Debit doWithdrawal(WithdrawalModel withdrawalModel) throws NoSuchAccountException {
		Optional<Account> optionalBankAccount = accountRepository.findById(withdrawalModel.getDestinationAccountNumber());
        if(!optionalBankAccount.isPresent()){
            throw new NoSuchAccountException(": "+ withdrawalModel.getDestinationAccountNumber());
        }
        Account account = optionalBankAccount.get();
        account.setBalance(account.getBalance() - withdrawalModel.getAmount());
        //account.setTransactionDate(Instant.now());
        accountRepository.save(account);
        
		return updateTransactionTable(withdrawalModel);

		
	}

	private Debit updateTransactionTable(WithdrawalModel withdrawalModel) {
		Debit debit= new Debit();
		debit.setSourceAccountnumber(withdrawalModel.getSourceAccountNumber());
		debit.setDestinationAccountnumber(withdrawalModel.getDestinationAccountNumber());
		debit.setTransactionDate(Instant.now());
		debit.setAmount(withdrawalModel.getAmount());
		return debitRepository.save(debit);
		
	}
}
