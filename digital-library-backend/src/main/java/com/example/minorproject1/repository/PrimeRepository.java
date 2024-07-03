package com.example.minorproject1.repository;

import com.example.minorproject1.model.PrimeTransactionDetails;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface PrimeRepository extends JpaRepository<PrimeTransactionDetails,Integer> {

}
