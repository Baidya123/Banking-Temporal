package com.example.DebitMicroservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.DebitMicroservice.model.Debit;


public interface DebitRepository extends JpaRepository<Debit,Long> {

}
