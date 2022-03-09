package com.example.DebitMicroservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.DebitMicroservice.model.Account;


public interface AccountRepository extends JpaRepository<Account,Long> {

}
