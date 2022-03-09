package com.service;

import org.springframework.stereotype.Service;

import com.domain.BankAccount;
import com.domain.Credit;
import com.domain.model.DepositModel;
import com.repository.BankAccountRepository;
import com.repository.CreditRepository;

import com.utils.NoSuchAccountException;

import javax.transaction.Transactional;

import java.time.Instant;
import java.util.Optional;

@Service
@Transactional
public class DepositService {

   
    private final BankAccountRepository bankAccountRepository;
    private final CreditRepository creditRepository;

    public DepositService( BankAccountRepository bankAccountRepository,CreditRepository creditRepository) {
       
        this.bankAccountRepository = bankAccountRepository;
		this.creditRepository = creditRepository;
    }

	public Credit doDeposit(DepositModel oc) throws NoSuchAccountException {
		Optional<BankAccount> optionalBankAccount = bankAccountRepository.findById(oc.getDestinationAccountNumber());
        if(!optionalBankAccount.isPresent()){
            throw new NoSuchAccountException(": "+ oc.getDestinationAccountNumber());
        }
        BankAccount account = optionalBankAccount.get();
        account.setBalance(account.getBalance() + oc.getAmount());
        //account.setTransactionDate(Instant.now());
        bankAccountRepository.save(account);
        
		return updateTransactionTable(oc);

		
	}

	private Credit updateTransactionTable(DepositModel oc) {
		Credit credit= new Credit();
		credit.setSourceAccountnumber(oc.getSourceAccountNumber());
		credit.setDestinationAccountnumber(oc.getDestinationAccountNumber());
		credit.setTransactionDate(Instant.now());
		credit.setAmount(oc.getAmount());
		return creditRepository.save(credit);
		
	}
}
