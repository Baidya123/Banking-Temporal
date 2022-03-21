package com.example.demo.service;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.demo.model.Account;



@Repository
public interface AccountService extends CrudRepository<Account, Long> {
	
	@Query("SELECT a.balance , a.email FROM Account a WHERE a.accountNumber = :accountNumber")
	public List<Object[]> findByAccountNumber(@Param("accountNumber") Long accountNo);
	
	@Query("SELECT a FROM Account a")
        public List<Account> findAllAccountBalanceTest();
}





