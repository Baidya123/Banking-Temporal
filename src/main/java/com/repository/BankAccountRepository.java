package com.repository;


import org.springframework.data.jpa.repository.JpaRepository;

import com.domain.BankAccount;

public interface BankAccountRepository  extends JpaRepository<BankAccount,Long>{
}
