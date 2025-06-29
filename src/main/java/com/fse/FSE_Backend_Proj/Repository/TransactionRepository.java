package com.fse.FSE_Backend_Proj.Repository;

import com.fse.FSE_Backend_Proj.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, String> {
    List<Transaction> findByInvestorId(String investorId);
}