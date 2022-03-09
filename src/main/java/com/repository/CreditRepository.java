package com.repository;


import org.springframework.data.jpa.repository.JpaRepository;

import com.domain.Credit;

public interface CreditRepository  extends JpaRepository<Credit,Long>{
}
