package com.service;

import org.springframework.stereotype.Service;

import com.domain.BankAccount;
import com.domain.Credit;
import com.domain.Customer;
import com.domain.model.AccountModel;
import com.domain.model.CustomerModel;
import com.repository.BankAccountRepository;
import com.repository.CreditRepository;
import com.repository.CustomerRepository;
import com.utils.NoSuchAccountException;

import javax.transaction.Transactional;

import java.time.Instant;
import java.util.Optional;

@Service
@Transactional
public class CustomerService {

   
    private final CustomerRepository customerRepository;

    public CustomerService(CustomerRepository customerRepository) {
       
        this.customerRepository = customerRepository;
    }

	public Customer createCustomer(CustomerModel model)  {
		Customer customer=new Customer();
		customer.setName(model.getName());
		return customerRepository.save(customer);

		
	}

}
