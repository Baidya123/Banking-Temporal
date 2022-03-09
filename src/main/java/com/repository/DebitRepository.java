package com.repository;


import org.springframework.data.jpa.repository.JpaRepository;

import com.domain.Debit;

public interface DebitRepository  extends JpaRepository<Debit,Long>{
}
