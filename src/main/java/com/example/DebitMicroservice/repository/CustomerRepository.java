package com.example.DebitMicroservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.DebitMicroservice.model.Customer;

public interface CustomerRepository extends JpaRepository<Customer,Long> {

}
