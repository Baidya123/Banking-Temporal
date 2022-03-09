package com.example.demo.service;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.demo.model.Account;



@Repository
public interface AccountService extends CrudRepository<Account, Long> {
	
	@Query("SELECT a FROM Account a WHERE accountNumber = :accountNumber")
    public Account findByAccountNumber(@Param("accountNumber") long accountNumber);
	
	@Query("SELECT a FROM Account a")
    public List<Account> findAllAccountBalanceTest();
}


